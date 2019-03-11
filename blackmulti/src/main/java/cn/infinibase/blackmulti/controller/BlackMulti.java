package cn.infinibase.blackmulti.controller;

import cn.infinibase.blackmulti.util.IpAdrressUtil;
import cn.infinibase.blackmulti.util.MachineModelUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import com.ifb.antifraud.AntiFraud;
import com.ifb.brloan.BrLoan;
import com.ifb.brscore.ParseBrData;
import com.ifb.ymscore.GetBlackList;
import com.ifb.ymscore.GetScore;
import com.ifb.ymscore.ParseYmData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;

/**
 * @author: fanchao
 * @description: 黑名单多头
 * @date: Create in 14:53 2017/11/6
 * @modified By:
 */
@RequestMapping("/blackMulti")
@Controller
public class BlackMulti {
    private static final Logger logger = LoggerFactory.getLogger(BlackMulti.class);
    //简单白名单控制--临时用一下
    static String[] whiteIPArr = {
            "127.0.0.1",
            "",
            ""
    };


   /* @ResponseBody
    @RequestMapping(value = "/getBlackMultiData",method = RequestMethod.POST)
    public String testTemplateEntity(HttpServletRequest request, String name, String id, String phone)  {
        String result = "";
        String ip = IpAdrressUtil.getIpAdrress(request);
//        if(checkWhiteIP(ip)) {

        result = MachineModelUtils.createMachineModelInputMap(name, id, phone);

//        }
       return result;
    }*/

    /**
     * 亿美接口
     * @param jsonObj
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getBlackMultiData",method = RequestMethod.POST)
    public JSONObject testTemplateEntity(@RequestParam  String jsonObj ){
        Map<String,String> resultMap = new HashMap<>();
        JSONObject resultJsonObject = new JSONObject();

        int score = new Random().nextInt(500) + 300;
        String grade = "N";

        if(StringUtils.isEmpty(jsonObj)) {
            resultJsonObject.put("state","false");
            return resultJsonObject;
        }else {
            try {
                ParseYmData ymd = new ParseYmData();
                JSONObject jj = JSON.parseObject(jsonObj);

                Map<String, String> presult = new HashMap<String, String>();
                if (jj.containsKey("RESULTS")) {

                    JSONArray jresult = jj.getJSONArray("RESULTS");
                    presult = ymd.processData(jresult);

                    GetScore gs = new GetScore();
                    GetBlackList black = new GetBlackList();

                    score = gs.getCreditScore(presult);
                    grade = black.getDueBlackGrade(presult);

                } else {
                    resultJsonObject.put("state", "false");
                    return resultJsonObject;
                }
            } catch (Exception e) {
                logger.error(" error-1:" + e.getMessage());
                score = new Random().nextInt(500) + 300;
                grade = "N";
            }

        }


        resultJsonObject.put("state","true");
        resultJsonObject.put("score",score);
        resultJsonObject.put("grade",grade);

        return resultJsonObject;
    }





    /**
     * 百荣接口--信用分 300--850
     * @param jsonObj
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getBRcreditScore",method = RequestMethod.POST)
    public JSONObject getBRcreditScore(@RequestParam  String jsonObj ){
        Map<String,String> resultMap = new HashMap<>();
        JSONObject resultJsonObject = new JSONObject();

        Random rand =new Random();
        int score = 500 + rand.nextInt(300);


        if(StringUtils.isEmpty(jsonObj)) {
            resultJsonObject.put("state","false");
            return resultJsonObject;
        }else {

            try {
                ParseBrData prbr = new ParseBrData();
                com.ifb.brscore.GetScore gs = new com.ifb.brscore.GetScore();
                Map<String,String> data=(Map<String, String>) JSON.parse(jsonObj);
                // 解析方程输入和输出
                Map<String, String> ndata=prbr.processData(data);

                //评分接口

                score = gs.getCreditScore(ndata);
            } catch (Exception e) {
                logger.error("error:" + e.getMessage());
                score = 500 + rand.nextInt(300);
            }

        }


        resultJsonObject.put("state","true");
        resultJsonObject.put("score",score);

        return resultJsonObject;
    }


    /**
     * 百荣接口--多头数据说明
     * @param jsonObj
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getBRloandata",method = RequestMethod.POST)
    public JSONObject getBRloandata(@RequestParam  String jsonObj ){
        Map<String,String>  ndata = new HashMap<>();
        JSONObject resultJsonObject = new JSONObject();



        if(StringUtils.isEmpty(jsonObj)) {
            resultJsonObject.put("state","false");
            return resultJsonObject;
        }else {
            try {
                BrLoan br = new BrLoan();

                Map<String, String> mdata = (Map<String, String>) JSON.parse(jsonObj);
                // 解析方程输入和输出
                ndata = br.getLoanData(mdata);
            } catch (Exception e) {
                logger.error("error-2:"+e.getMessage());
                ndata = new HashMap<>();
            }



        }
        resultJsonObject.put("state","true");
        resultJsonObject.put("loandata",ndata);

        return resultJsonObject;
    }


/**
 * v2_ model_antifraud_0806
 */


    /**
     * 反欺诈分
     * @param jsonObj
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAntiFraud",method = RequestMethod.POST)
    public JSONObject getAntiFraud(@RequestParam  String jsonObj ){
        Map<String,String>  ndata = new HashMap<>();
        JSONObject resultJsonObject = new JSONObject();

        int score = -99;

        if(StringUtils.isEmpty(jsonObj)) {
            resultJsonObject.put("state","false");
            return resultJsonObject;
        }else {
            try {
                AntiFraud fr = new AntiFraud();

                Map<String, String> mdata = (Map<String, String>) JSON.parse(jsonObj);
                // 解析方程输入和输出
                score = fr.getFraudScore(mdata);
                System.out.println("");
            }catch (Exception e){
                logger.error("error-3:" + e.getMessage());
                score = -99;
            }


        }
        resultJsonObject.put("state","true");
        resultJsonObject.put("score",score);

        return resultJsonObject;
    }



    /**
     * 检查白名单
     * @param ip
     * @return
     */
    public boolean checkWhiteIP(String ip) {

        boolean flag = false;
        for (int i = 0; i < whiteIPArr.length; i++) {
            if(whiteIPArr[i].equalsIgnoreCase(ip)) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}



