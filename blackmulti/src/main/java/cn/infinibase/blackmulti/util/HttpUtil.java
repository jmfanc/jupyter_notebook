package cn.infinibase.blackmulti.util;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

public class HttpUtil {
    public static String sendGET(URI urlGet) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();


        HttpGet httpGet = new HttpGet(urlGet);
        httpGet.addHeader("User-Agent", "Mozilla/5.0");

        //设置超时
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000).setConnectionRequestTimeout(5000)
                .setSocketTimeout(5000).build();
        httpGet.setConfig(requestConfig);







        String result = null;
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        if( httpResponse.getStatusLine().getStatusCode() == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            String inputLine = null;
            StringBuffer response = new StringBuffer();
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();
            result = response.toString();
        }
        httpClient.close();
        return result;
    }
}
