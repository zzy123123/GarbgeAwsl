package com.example.garbgeawsl.Util;

import java.io.BufferedReader;
import java.util.Map;
import java.util.HashMap;
import java.io.UnsupportedEncodingException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.net.HttpURLConnection;

/**
 * created by zzyy
 **/
public class Api{

    public static void main(String[] args) {

        System.out.println(result());//测试数据
    }

    //    "http://route.showapi.com/1626-1?"
//    + "key=3fa8b5e3b009b336e0ae7638898203e8"
//    + "men=" ""
//    + "women=" ""
    public static String result() {
        //接口地址
        String requestUrl = "http://apis.juhe.cn/xzpd/query";
        //params用于存储请求数据的参数
        Map params = new HashMap();
        //请求数据
        //数字签名，###填你的数字签名，可以在你的个人中心看到
        params.put("key", "3fa8b5e3b009b336e0ae7638898203e8");
        //
        params.put("men", "白羊");
        //
        params.put("women","白羊");


        //调用httpRequest方法，这个方法主要用于请求地址，并加上请求参数
        String s = httpRequest(requestUrl, params);
        System.out.println(s);
        return s;
    }

    private static String httpRequest(String requestUrl, Map params) {
        //buffer用于接受返回的json数据
        StringBuffer buffer = new StringBuffer();
        try {
            //建立URL，把请求地址给补全，其中urlencode（）方法用于把params里的参数给取出来
            URL url = new URL(requestUrl + "?" + urlencode(params));
            //打开http连接
            HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();//连接
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setRequestMethod("GET");
            httpUrlConnection.connect();

            //获得输入
            InputStream inputStream = httpUrlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");//编码
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


            //将bufferReader的值给放到str里
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }


            //关闭bufferReader和输入流
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            // inputStream = null;

            //断开连接
            httpUrlConnection.disconnect();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //返回字符串
        return buffer.toString();
    }

    public static String urlencode(Map<String, Object> data) {

        //将map里的参数变成像 men=###&women=###&的样子
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
        }
        return sb.toString();
    }
}
