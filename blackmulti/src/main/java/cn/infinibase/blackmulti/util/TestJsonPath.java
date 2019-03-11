package cn.infinibase.blackmulti.util;

import com.jayway.jsonpath.JsonPath;

import java.util.List;

/**
 * @author: fanchao
 * @description:
 * @date: Create in 20:25 2018/7/2
 * @modified By:
 */
public class TestJsonPath {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
//        String sjson = readtxt();
        String sjson = "{\"store\": {\"book\": [{\"category\": \"reference\",\"author\": \"Nigel Rees\",\"title\": \"Sayings of the Century\",\"price\": 8.95},{\"category\": \"fiction\",\"author\": \"Evelyn Waugh\",\"title\": \"Sword of Honour\",\"price\": 12.99},{\"category\": \"fiction\",\"author\": \"Herman Melville\",\"title\": \"Moby Dick\",\"isbn\": \"0-553-21311-3\",\"price\": 8.99},{\"category\": \"fiction\",\"author\": \"J. R. R. Tolkien\",\"title\": \"The Lord of the Rings\",\"isbn\": \"0-395-19395-8\",\"price\": 22.99}],\"bicycle\": {\"color\": \"red\",\"price\": 19.95}},\"expensive\": 10}";


        getJsonValue(sjson);


        getJsonValue(sjson);
    }


    private static void getJsonValue(String json) {
        //The authors of all books：获取json中store下book下的所有author值
//        List<String> authors1 = JsonPath.read(json, "$.store.book[*].author");
//
//        //All authors：获取所有json中所有author的值
//        List<String> authors2 = JsonPath.read(json, "$..author");
//
//        //All things, both books and bicycles //authors3返回的是net.minidev.json.JSONArray：获取json中store下的所有value值，不包含key，如key有两个，book和bicycle
//        List<Object> authors3 = JsonPath.read(json, "$.store.*");
//
//
//        //The price of everything：获取json中store下所有price的值
//        List<Object> authors4 = JsonPath.read(json, "$.store..price");

        String authors4 = JsonPath.read(json, "$.store.bicycle.color");

        System.out.println(authors4);

    }
}
