package cn.infinibase.blackmulti.util;

import cn.infinibase.blackmulti.model.DataNode;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.ifb.creditscore.GetScore;
import com.ifb.fraudgrade.Fraudrule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: fanchao
 * @description:
 * @date: Create in 9:36 2018/7/3
 * @modified By:
 */
public class MachineModelUtils {
    private static final Logger logger = LoggerFactory.getLogger(MachineModelUtils.class);

    public static String createMachineModelInputMap(String name, String id, String phone) {

        Map<String, String> resultMap = FetchDataUtil.getData(name, id, phone);

        ///////////////////////////////////////////////////////////////
//        Map<String, String> resultMap = new HashMap<>();
//        String payoff = "{\"status\":0,\"overdue_hist\":{\"overview\":{\"ct\":10,\"item_ct\":85},\"detail\":[{\"range_type\":0,\"ct\":7,\"item_ct\":77,\"bct\":6,\"bitem_ct\":76},{\"range_type\":1,\"ct\":1,\"item_ct\":2,\"bct\":1,\"bitem_ct\":2},{\"range_type\":2,\"ct\":4,\"item_ct\":4,\"bct\":4,\"bitem_ct\":4},{\"range_type\":3,\"ct\":2,\"item_ct\":2,\"bct\":2,\"bitem_ct\":2},{\"range_type\":4,\"ct\":0,\"item_ct\":0,\"bct\":0,\"bitem_ct\":0},{\"range_type\":5,\"ct\":0,\"item_ct\":0,\"bct\":0,\"bitem_ct\":0},{\"range_type\":7,\"ct\":0,\"item_ct\":0,\"bct\":0,\"bitem_ct\":0}]}}\n";
//        String debtor = "{\"status\":0,\"overdue\":{\"overview\":{\"ct\":10,\"item_ct\":296005},\"detail\":[{\"range_type\":0,\"ct\":4,\"item_ct\":208005,\"bct\":0,\"bitem_ct\":0},{\"range_type\":1,\"ct\":3,\"item_ct\":83000,\"bct\":0,\"bitem_ct\":0},{\"range_type\":2,\"ct\":6,\"item_ct\":5000,\"bct\":0,\"bitem_ct\":0}]},\"detail\":[{\"range_type\":0,\"ct\":4,\"item_ct\":208005,\"bct\":0,\"bitem_ct\":0},{\"range_type\":1,\"ct\":3,\"item_ct\":83000,\"bct\":0,\"bitem_ct\":0},{\"range_type\":2,\"ct\":6,\"item_ct\":5000,\"bct\":0,\"bitem_ct\":0}]}\n";
//        String reject = "{\"status\":0,\"refused_hist\":{\"overview\":{\"ct\":8,\"item_ct\":14,\"item_r\":6},\"detail\":[{\"range_type\":0,\"ct\":3,\"item_ct\":6,\"item_r\":0,\"bct\":0,\"bitem_ct\":0},{\"range_type\":1,\"ct\":0,\"item_ct\":0,\"item_r\":0,\"bct\":0,\"bitem_ct\":0},{\"range_type\":2,\"ct\":0,\"item_ct\":0,\"item_r\":0,\"bct\":0,\"bitem_ct\":0},{\"range_type\":3,\"ct\":0,\"item_ct\":0,\"item_r\":0,\"bct\":0,\"bitem_ct\":0},{\"range_type\":4,\"ct\":1,\"item_ct\":1,\"item_r\":1,\"bct\":0,\"bitem_ct\":0},{\"range_type\":5,\"ct\":4,\"item_ct\":7,\"item_r\":5,\"bct\":0,\"bitem_ct\":0}]}}\n";
//
//        resultMap.put("payoff",payoff);
//        resultMap.put("debtor",debtor);
//        resultMap.put("reject",reject);
        ///////////////////////////////////////////////////////////////

        logger.info("resultMap-----{}", resultMap);

        List<DataNode> payOffDataNodeList= payOffFun(resultMap.get("payoff"));
        List<DataNode> debtorDataNodeList= debtorFun(resultMap.get("debtor"));
        List<DataNode> rejectDataNodeList= rejectFun(resultMap.get("reject"));


        Map machineModelInputMap = new HashMap<String, String>();
        for (int i = 0; i < payOffDataNodeList.size(); i++) {
            machineModelInputMap.put(payOffDataNodeList.get(i).getKey(), payOffDataNodeList.get(i).getValue());
        }
        for (int i = 0; i < debtorDataNodeList.size(); i++) {
            machineModelInputMap.put(debtorDataNodeList.get(i).getKey(), debtorDataNodeList.get(i).getValue());
        }
        for (int i = 0; i < rejectDataNodeList.size(); i++) {
            machineModelInputMap.put(rejectDataNodeList.get(i).getKey(), rejectDataNodeList.get(i).getValue());
        }

        //调用博士的机器学习模型
        String gradeResult = Fraudrule.getGrade(machineModelInputMap);
        int getCreditScore = new GetScore().getCreditScore(machineModelInputMap);

        Map<String,Object> machineCaclResult = new HashMap();
        machineCaclResult.put("grade", gradeResult);
        machineCaclResult.put("creditScore", String.valueOf(getCreditScore));

        JSONObject a = new JSONObject(machineCaclResult);

        return a.toJSONString();
    }



    /**
     * 处理reject类型的请求及返回数据的处理
     * @param resultJson
     * @return
     */
    private static List<DataNode> rejectFun(String resultJson) {
        JSONObject jsonObject = JSON.parseObject(resultJson);

        String seekptcountall = "0";
        String seekloancount = "0";
        String rejectloancount ="0";

        String seekptcount1m = "0";
        String seekloancount1m = "0";
        String rejectloancount1m ="0";

        String seekptcount1m2m = "0";
        String seekloancount1m2m = "0";
        String rejectloancount1m2m ="0";

        String seekptcount2m3m = "0";
        String seekloancount2m3m = "0";
        String rejectloancount2m3m ="0";

        String seekptcount3m6m = "0";
        String seekloancount3m6m = "0";
        String rejectloancount3m6m ="0";

        String seekptcount6m12m = "0";
        String seekloancount6m12m = "0";
        String rejectloancount6m12m ="0";

        String seekptcountover12m = "0";
        String seekloancountover12m = "0";
        String rejectloancountover12m ="0";

        seekptcountall  = transFun(JSONPath.eval(jsonObject, "$.refused_hist.overview.ct") );
        seekloancount   = transFun(JSONPath.eval(jsonObject, "$.refused_hist.overview.item_ct") );
        rejectloancount = transFun(JSONPath.eval(jsonObject, "$.refused_hist.overview.item_r") );

        JSONArray detailArr = null;
        if(jsonObject.getJSONObject("refused_hist")!=null) {
            detailArr = jsonObject.getJSONObject("refused_hist").getJSONArray("detail");//临时
        }


        if(detailArr !=null && detailArr.size()>0) {
            for (int i = 0; i < detailArr.size(); i++) {
                String range_type = detailArr.getJSONObject(i).getString("range_type");
                if("0".equals(range_type)) {
                    seekptcount1m      = detailArr.getJSONObject(i).getString("ct");
                    seekloancount1m    = detailArr.getJSONObject(i).getString("item_ct");
                    rejectloancount1m  = detailArr.getJSONObject(i).getString("item_r");
                }else if("1".equals(range_type)) {
                    seekptcount1m2m      = detailArr.getJSONObject(i).getString("ct");
                    seekloancount1m2m    = detailArr.getJSONObject(i).getString("item_ct");
                    rejectloancount1m2m  = detailArr.getJSONObject(i).getString("item_r");
                }else if("2".equals(range_type)) {
                    seekptcount2m3m      = detailArr.getJSONObject(i).getString("ct");
                    seekloancount2m3m    = detailArr.getJSONObject(i).getString("item_ct");
                    rejectloancount2m3m  = detailArr.getJSONObject(i).getString("item_r");
                }else if("3".equals(range_type)) {
                    seekptcount3m6m      = detailArr.getJSONObject(i).getString("ct");
                    seekloancount3m6m    = detailArr.getJSONObject(i).getString("item_ct");
                    rejectloancount3m6m  = detailArr.getJSONObject(i).getString("item_r");
                }else if("4".equals(range_type)) {
                    seekptcount6m12m      = detailArr.getJSONObject(i).getString("ct");
                    seekloancount6m12m    = detailArr.getJSONObject(i).getString("item_ct");
                    rejectloancount6m12m  = detailArr.getJSONObject(i).getString("item_r");
                }else if("5".equals(range_type)) {
                    seekptcountover12m      = detailArr.getJSONObject(i).getString("ct");
                    seekloancountover12m    = detailArr.getJSONObject(i).getString("item_ct");
                    rejectloancountover12m  = detailArr.getJSONObject(i).getString("item_r");
                }
            }
        }

        //保存数据
        List<DataNode> dataNodeList = new ArrayList<>();
        dataNodeList.add(new DataNode("seekptcountall", seekptcountall));
        dataNodeList.add(new DataNode("seekloancount", seekloancount));
        dataNodeList.add(new DataNode("rejectloancount", rejectloancount));

        dataNodeList.add(new DataNode("seekptcount1m", seekptcount1m));
        dataNodeList.add(new DataNode("seekloancount1m", seekloancount1m));
        dataNodeList.add(new DataNode("rejectloancount1m", rejectloancount1m));

        dataNodeList.add(new DataNode("seekptcount1m2m", seekptcount1m2m));
        dataNodeList.add(new DataNode("seekloancount1m2m", seekloancount1m2m));
        dataNodeList.add(new DataNode("rejectloancount1m2m", rejectloancount1m2m));

        dataNodeList.add(new DataNode("seekptcount2m3m", seekptcount2m3m));
        dataNodeList.add(new DataNode("seekloancount2m3m", seekloancount2m3m));
        dataNodeList.add(new DataNode("rejectloancount2m3m", rejectloancount2m3m));

        dataNodeList.add(new DataNode("seekptcount3m6m", seekptcount3m6m));
        dataNodeList.add(new DataNode("seekloancount3m6m", seekloancount3m6m));
        dataNodeList.add(new DataNode("rejectloancount3m6m", rejectloancount3m6m));

        dataNodeList.add(new DataNode("seekptcount6m12m", seekptcount6m12m));
        dataNodeList.add(new DataNode("seekloancount6m12m", seekloancount6m12m));
        dataNodeList.add(new DataNode("rejectloancount6m12m", rejectloancount6m12m));

        dataNodeList.add(new DataNode("seekptcountover12m", seekptcountover12m));
        dataNodeList.add(new DataNode("seekloancountover12m", seekloancountover12m));
        dataNodeList.add(new DataNode("rejectloancountover12m", rejectloancountover12m));


        return dataNodeList;
    }




    /**
     * 处理debtor类型的请求及返回数据的处理
     * @param resultJson
     * @return
     */
    private static List<DataNode> debtorFun(String resultJson) {
        JSONObject jsonObject = JSON.parseObject(resultJson);

        String ptcountall = "0";
        String loansum = "0";

        String ptcount1m = "0";
        String loansum1m = "0";

        String ptcount1m3m = "0";
        String loansum1m3m = "0";

        String ptcountover3m = "0";
        String loansumover3m = "0";


        ptcountall =transFun(JSONPath.eval(jsonObject, "$.overdue.overview.ct") );
        loansum   = transFun(JSONPath.eval(jsonObject, "$.overdue.overview.item_ct") );


        JSONArray detailArr = null;
        if(jsonObject.getJSONObject("overdue")!=null) {
            detailArr = jsonObject.getJSONObject("overdue").getJSONArray("detail");//临时
        }

        if(detailArr !=null && detailArr.size()>0) {
            for (int i = 0; i < detailArr.size(); i++) {
                String range_type = detailArr.getJSONObject(i).getString("range_type");
                if("0".equals(range_type)) {
                    ptcount1m      = detailArr.getJSONObject(i).getString("ct");
                    loansum1m      = detailArr.getJSONObject(i).getString("item_ct");
                }else if("1".equals(range_type)) {
                    ptcount1m3m    = detailArr.getJSONObject(i).getString("ct");
                    loansum1m3m    = detailArr.getJSONObject(i).getString("item_ct");
                }else if("2".equals(range_type)) {
                    ptcountover3m  = detailArr.getJSONObject(i).getString("ct");
                    loansumover3m  = detailArr.getJSONObject(i).getString("item_ct");
                }
            }
        }

        //保存数据
        List<DataNode> dataNodeList = new ArrayList<>();
        dataNodeList.add(new DataNode("ptcountall", ptcountall));
        dataNodeList.add(new DataNode("loansum", loansum));

        dataNodeList.add(new DataNode("ptcount1m", ptcount1m));
        dataNodeList.add(new DataNode("loansum1m", loansum1m));

        dataNodeList.add(new DataNode("ptcount1m3m", ptcount1m3m));
        dataNodeList.add(new DataNode("loansum1m3m", loansum1m3m));

        dataNodeList.add(new DataNode("ptcountover3m", ptcountover3m));
        dataNodeList.add(new DataNode("loansumover3m", loansumover3m));


        return dataNodeList;

    }




    /**
     * 处理payOff类型的请求及返回数据的处理
     * @param resultJson
     */
    private static List<DataNode> payOffFun(String resultJson) {
        JSONObject jsonObject = JSON.parseObject(resultJson);

        String dueptcount = "0";
        String duecount = "0";

        String dueptcount1m = "0";
        String duecount1m = "0";
        String clearptcount1m = "0";
        String clearcount1m= "0";

        String dueptcount1m2m = "0";
        String duecount1m2m = "0";
        String clearptcount1m2m = "0";
        String clearcount1m2m= "0";

        String dueptcount2m3m = "0";
        String duecount2m3m = "0";
        String clearptcount2m3m = "0";
        String clearcount2m3m= "0";

        String dueptcount3m6m = "0";
        String duecount3m6m = "0";
        String clearptcount3m6m = "0";
        String clearcount3m6m= "0";

        String dueptcount6m12m = "0";
        String duecount6m12m = "0";
        String clearptcount6m12m = "0";
        String clearcount6m12m= "0";

        String dueptcount12m24m = "0";
        String duecount12m24m = "0";
        String clearptcount12m24m = "0";
        String clearcount12m24m= "0";

        String dueptcountover24m = "0";
        String duecountover24m = "0";
        String clearptcountover24m = "0";
        String clearcountover24m= "0";

        dueptcount =transFun(JSONPath.eval(jsonObject, "$.overdue_hist.overview.ct") );
        duecount   = transFun(JSONPath.eval(jsonObject, "$.overdue_hist.overview.item_ct") );

        JSONArray detailArr = null;
        if(jsonObject.getJSONObject("overdue_hist")!=null) {
            detailArr = jsonObject.getJSONObject("overdue_hist").getJSONArray("detail");//临时
        }

        if(detailArr !=null && detailArr.size()>0) {
            for (int i = 0; i < detailArr.size(); i++) {
                String range_type = detailArr.getJSONObject(i).getString("range_type");
                if("0".equals(range_type)) {
                    dueptcount1m    = detailArr.getJSONObject(i).getString("ct");
                    duecount1m      = detailArr.getJSONObject(i).getString("item_ct");
                    clearptcount1m  = detailArr.getJSONObject(i).getString("bct");
                    clearcount1m    = detailArr.getJSONObject(i).getString("bitem_ct");
                }else if("1".equals(range_type)) {
                    dueptcount1m2m    = detailArr.getJSONObject(i).getString("ct");
                    duecount1m2m      = detailArr.getJSONObject(i).getString("item_ct");
                    clearptcount1m2m  = detailArr.getJSONObject(i).getString("bct");
                    clearcount1m2m    = detailArr.getJSONObject(i).getString("bitem_ct");
                }else if("2".equals(range_type)) {
                    dueptcount2m3m    = detailArr.getJSONObject(i).getString("ct");
                    duecount2m3m      = detailArr.getJSONObject(i).getString("item_ct");
                    clearptcount2m3m  = detailArr.getJSONObject(i).getString("bct");
                    clearcount2m3m    = detailArr.getJSONObject(i).getString("bitem_ct");
                }else if("3".equals(range_type)) {
                    dueptcount3m6m    = detailArr.getJSONObject(i).getString("ct");
                    duecount3m6m      = detailArr.getJSONObject(i).getString("item_ct");
                    clearptcount3m6m  = detailArr.getJSONObject(i).getString("bct");
                    clearcount3m6m    = detailArr.getJSONObject(i).getString("bitem_ct");
                }else if("4".equals(range_type)) {
                    dueptcount6m12m    = detailArr.getJSONObject(i).getString("ct");
                    duecount6m12m      = detailArr.getJSONObject(i).getString("item_ct");
                    clearptcount6m12m  = detailArr.getJSONObject(i).getString("bct");
                    clearcount6m12m    = detailArr.getJSONObject(i).getString("bitem_ct");
                }else if("5".equals(range_type)) {
                    dueptcount12m24m    = detailArr.getJSONObject(i).getString("ct");
                    duecount12m24m      = detailArr.getJSONObject(i).getString("item_ct");
                    clearptcount12m24m  = detailArr.getJSONObject(i).getString("bct");
                    clearcount12m24m    = detailArr.getJSONObject(i).getString("bitem_ct");
                }else if("7".equals(range_type)) {
                    dueptcountover24m    = detailArr.getJSONObject(i).getString("ct");
                    duecountover24m      = detailArr.getJSONObject(i).getString("item_ct");
                    clearptcountover24m  = detailArr.getJSONObject(i).getString("bct");
                    clearcountover24m    = detailArr.getJSONObject(i).getString("bitem_ct");
                }
            }
        }

        //保存数据
        List<DataNode> dataNodeList = new ArrayList<>();
        dataNodeList.add(new DataNode("dueptcount", dueptcount));
        dataNodeList.add(new DataNode("duecount", duecount));

        dataNodeList.add(new DataNode("dueptcount1m", dueptcount1m));
        dataNodeList.add(new DataNode("duecount1m", duecount1m));
        dataNodeList.add(new DataNode("clearptcount1m", clearptcount1m));
        dataNodeList.add(new DataNode("clearcount1m", clearcount1m));

        dataNodeList.add(new DataNode("dueptcount1m2m", dueptcount1m2m));
        dataNodeList.add(new DataNode("duecount1m2m", duecount1m2m));
        dataNodeList.add(new DataNode("clearptcount1m2m", clearptcount1m2m));
        dataNodeList.add(new DataNode("clearcount1m2m", clearcount1m2m));

        dataNodeList.add(new DataNode("dueptcount2m3m", dueptcount2m3m));
        dataNodeList.add(new DataNode("duecount2m3m", duecount2m3m));
        dataNodeList.add(new DataNode("clearptcount2m3m", clearptcount2m3m));
        dataNodeList.add(new DataNode("clearcount2m3m", clearcount2m3m));

        dataNodeList.add(new DataNode("dueptcount3m6m", dueptcount3m6m));
        dataNodeList.add(new DataNode("duecount3m6m", duecount3m6m));
        dataNodeList.add(new DataNode("clearptcount3m6m", clearptcount3m6m));
        dataNodeList.add(new DataNode("clearcount3m6m", clearcount3m6m));

        dataNodeList.add(new DataNode("dueptcount6m12m", dueptcount6m12m));
        dataNodeList.add(new DataNode("duecount6m12m", duecount6m12m));
        dataNodeList.add(new DataNode("clearptcount6m12m", clearptcount6m12m));
        dataNodeList.add(new DataNode("clearcount6m12m", clearcount6m12m));

        dataNodeList.add(new DataNode("dueptcount12m24m", dueptcount12m24m));
        dataNodeList.add(new DataNode("duecount12m24m", duecount12m24m));
        dataNodeList.add(new DataNode("clearptcount12m24m", clearptcount12m24m));
        dataNodeList.add(new DataNode("clearcount12m24m", clearcount12m24m));

        dataNodeList.add(new DataNode("dueptcountover24m", dueptcountover24m));
        dataNodeList.add(new DataNode("duecountover24m", duecountover24m));
        dataNodeList.add(new DataNode("clearptcountover24m", clearptcountover24m));
        dataNodeList.add(new DataNode("clearcountover24m", clearcountover24m));


        for (int i = 0; i < dataNodeList.size(); i++) {
            System.out.println(dataNodeList.get(i).getKey() + " -- " + dataNodeList.get(i).getValue());
        }


        return dataNodeList;

    }


    private static String transFun(Object o ){
        if( o instanceof String ) {
            return  (String) o;
        } else if(o instanceof Number ) {
            return String.valueOf( o);
        }

        if (o == null) {
            return "0";
        }

        return "0";
    }

    public static void main(String[] args){
        String payoff = "{\"status\":0,\"overdue_hist\":{\"overview\":{\"ct\":10,\"item_ct\":85},\"detail\":[{\"range_type\":0,\"ct\":7,\"item_ct\":77,\"bct\":6,\"bitem_ct\":76},{\"range_type\":1,\"ct\":1,\"item_ct\":2,\"bct\":1,\"bitem_ct\":2},{\"range_type\":2,\"ct\":4,\"item_ct\":4,\"bct\":4,\"bitem_ct\":4},{\"range_type\":3,\"ct\":2,\"item_ct\":2,\"bct\":2,\"bitem_ct\":2},{\"range_type\":4,\"ct\":0,\"item_ct\":0,\"bct\":0,\"bitem_ct\":0},{\"range_type\":5,\"ct\":0,\"item_ct\":0,\"bct\":0,\"bitem_ct\":0},{\"range_type\":7,\"ct\":0,\"item_ct\":0,\"bct\":0,\"bitem_ct\":0}]}}\n";
        String debtor = "{\"status\":0,\"overdue\":{\"overview\":{\"ct\":10,\"item_ct\":296005},\"detail\":[{\"range_type\":0,\"ct\":4,\"item_ct\":208005,\"bct\":0,\"bitem_ct\":0},{\"range_type\":1,\"ct\":3,\"item_ct\":83000,\"bct\":0,\"bitem_ct\":0},{\"range_type\":2,\"ct\":6,\"item_ct\":5000,\"bct\":0,\"bitem_ct\":0}]},\"detail\":[{\"range_type\":0,\"ct\":4,\"item_ct\":208005,\"bct\":0,\"bitem_ct\":0},{\"range_type\":1,\"ct\":3,\"item_ct\":83000,\"bct\":0,\"bitem_ct\":0},{\"range_type\":2,\"ct\":6,\"item_ct\":5000,\"bct\":0,\"bitem_ct\":0}]}\n";
        String reject = "{\"status\":0,\"refused_hist\":{\"overview\":{\"ct\":8,\"item_ct\":14,\"item_r\":6},\"detail\":[{\"range_type\":0,\"ct\":3,\"item_ct\":6,\"item_r\":0,\"bct\":0,\"bitem_ct\":0},{\"range_type\":1,\"ct\":0,\"item_ct\":0,\"item_r\":0,\"bct\":0,\"bitem_ct\":0},{\"range_type\":2,\"ct\":0,\"item_ct\":0,\"item_r\":0,\"bct\":0,\"bitem_ct\":0},{\"range_type\":3,\"ct\":0,\"item_ct\":0,\"item_r\":0,\"bct\":0,\"bitem_ct\":0},{\"range_type\":4,\"ct\":1,\"item_ct\":1,\"item_r\":1,\"bct\":0,\"bitem_ct\":0},{\"range_type\":5,\"ct\":4,\"item_ct\":7,\"item_r\":5,\"bct\":0,\"bitem_ct\":0}]}}\n";

//        MachineModelUtils.payOffFun(payoff);
//        MachineModelUtils.debtorFun(debtor);
//        MachineModelUtils.rejectFun(reject);

/*        String name ="苏志勇";
        String id="35032219831012385X";
        String phone="15959296932";

        long start_time = System.currentTimeMillis();
        for (int i = 0; i < 1 ; i++) {
            MachineModelUtils.createMachineModelInputMap(name, id, phone);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(i % 100 ==0) {
                logger.info(" ************* {}" , i);

            }
        }
        long end_time = System.currentTimeMillis();
        logger.info("total_time = {}" , (end_time - start_time) / 1000);
        */


    }



}
