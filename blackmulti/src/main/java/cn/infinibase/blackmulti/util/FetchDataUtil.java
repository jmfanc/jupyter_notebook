package cn.infinibase.blackmulti.util;

import org.apache.commons.codec.binary.Hex;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: fanchao
 * @description:
 * @date: Create in 17:50 2018/7/2
 * @modified By:
 */
public class FetchDataUtil {

    public static Map<String,String> getData(String name, String id, String phone) {
        Map resultMap = new HashMap<String, String>();

        String account = "zhongsheng", password = "zs@Test%1122";

        //根据博士要求控制types的类型
//        String[] types = {"overdue", "payoff", "reject", "debtor", "overlend"};
        String[] types = {"payoff", "debtor", "reject"};


        for (int j = 0; j < types.length; j++) {
            try {
                URI uri = new URIBuilder()
                        .setScheme("http")
                        .setHost("fin.xindatek.com")
                        .setPath("/" + types[j])  // overdue, payoff, reject, debtor, overlend
                        .setParameter("auth"    , encode(account + " " + password))
                        .setParameter("name"    , AESUtil.encrypt(name  , password))
                        .setParameter("id"      , AESUtil.encrypt(id    , password))
                        .setParameter("phone"   , AESUtil.encrypt(phone , password))
                        .build();
                String strResponse = HttpUtil.sendGET(uri);
                if(StringUtils.isNotEmpty(strResponse)) {
                    resultMap.put(types[j], strResponse);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return resultMap;
    }




    public static String encode(String str) {
        return Hex.encodeHexString(str.getBytes());
    }

    public static class Query {

        private String account;

        private String password;

        private String name;

        private String id;

        private String phone;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getID() {
            return id;
        }

        public void setID(String id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }


    }




}
