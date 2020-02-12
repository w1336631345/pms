package com.kry.pms.util;

import com.google.gson.Gson;

import java.io.*;
import java.net.*;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {


    public static String doGet(String httpurl) {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;// 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(httpurl);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 封装输入流is，并指定字符集
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            connection.disconnect();// 关闭远程连接
        }

        return result;
    }


    public static void main(String[] args) throws IOException {

        String dz = doGet("http://pv.sohu.com/cityjson?ie=utf-8");//get请求
        String jsonStr = dz.split("=")[1].split(";")[0];
        Map map1 = json2map(jsonStr);
        Map map2 = HttpUtil.getTodayWeather1("510100");
        System.out.println("地址來了"+map1);
        System.out.println("map來了"+map2);
        /*interfaceUtil("http://172.83.28.221:7001/NSRTRegistration/test/add.do",
             "id=8888888&name=99999999");*///post请求
    }

    public static Map<String, Object> getTodayWeather1(String Cityid)
            throws IOException, NullPointerException {
        // 连接和风天气的API
        String url1= "https://free-api.heweather.net/s6/weather/now?location="+Cityid+"&key=3c3fa198cacc4152b94b20def11b2455";

        URL url = new URL(url1);
        BufferedReader br = null;
        String datas = null;
        URLConnection connectionData = url.openConnection();
        connectionData.setConnectTimeout(1000);
        try {
            br = new BufferedReader(new InputStreamReader(
                    connectionData.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null)
                sb.append(line);
            datas = sb.toString();
            //截取[]转化为json格式
            datas = datas.replace(datas.substring(datas.indexOf(":")+1,datas.indexOf(":")+2),"");
            datas = datas.replace(datas.substring(datas.length()-2,datas.length()-1),"");
//            System.out.println(datas);
        } catch (SocketTimeoutException e) {
            System.out.println("连接超时");
        } catch (FileNotFoundException e) {
            System.out.println("加载文件出错");
        } finally {
            //关闭流
            try {
                if(br!=null){
                    br.close();
                }

            } catch ( Exception  e) {
                e.printStackTrace();
            }
        }
        return HttpUtil.json2map(datas);
    }

    public static Map<String, Object> json2map(String jsonStr) {
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<String, Object>();
        map = gson.fromJson(jsonStr, map.getClass());
        return map;
    }

}
