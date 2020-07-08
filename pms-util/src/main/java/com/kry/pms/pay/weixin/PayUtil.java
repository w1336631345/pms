package com.kry.pms.pay.weixin;

import com.kry.pms.pay.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.*;

public class PayUtil {
	 /**  
     * 签名字符串 (MD5)
     * @param text需要签名的字符串  
     * @param key 密钥  
     * @param input_charset编码格式  
     * @return 签名结果  
     */   
    public static String sign(String text, String key, String input_charset) {   
        text = text + "&key=" + key;   
        return DigestUtils.md5Hex(getContentBytes(text, input_charset));   
    }
    /**
     * 签名字符串 (HMAC-SHA256)
     * @param text需要签名的字符串
     * @param key 密钥
     * @param input_charset编码格式
     * @return 签名结果
     */
    public static String signSha(String text, String key, String input_charset) {
        text = text + "&key=" + key;
        return DigestUtils.sha256Hex(getContentBytes(text, input_charset));
//        return DigestUtils.sha256Hex(text);
    }
    public static String signSha2(String text, String key, String input_charset) {
        text = text + "&key=" + key;
//        return DigestUtils.sha256Hex(getContentBytes(text, input_charset));
        return DigestUtils.sha256Hex(text);
    }
    /**
     * sha256_HMAC加密(支付押金用到)
     * @param message 消息
     * @param secret  秘钥
     * @return 加密后字符串
     */
    public static String signHMAC_SHA256(String string1, String key) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String stringSignTemp = string1 + "&key=" + key;
        //return DigestUtils.sha256Hex(stringSignTemp).toUpperCase();
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        //  utf-8 : 解决中文加密不一致问题,必须指定编码格式
        return byteArrayToHexString(sha256_HMAC.doFinal(stringSignTemp.getBytes("utf-8"))).toUpperCase();
    }

    public  static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b!=null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }
    /**  
     * 签名字符串  
     *  @param text需要签名的字符串  
     * @param sign 签名结果  
     * @param key密钥
     * @param input_charset 编码格式  
     * @return 签名结果  
     */   
    public static boolean verify(String text, String sign, String key, String input_charset) {   
        text = text + key;   
        String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));   
        if (mysign.equals(sign)) {   
            return true;   
        } else {   
            return false;   
        }   
    }   
    /**  
     * @param content  
     * @param charset  
     * @return  
     * @throws SignatureException  
     * @throws UnsupportedEncodingException  
     */   
    public static byte[] getContentBytes(String content, String charset) {   
        if (charset == null || "".equals(charset)) {   
            return content.getBytes();   
        }   
        try {   
            return content.getBytes(charset);   
        } catch (UnsupportedEncodingException e) {   
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);   
        }   
    }   
    /**  
     * 生成6位或10位随机数 param codeLength(多少位)  
     * @return  
     */   
    public static String createCode(int codeLength) {   
        String code = "";   
        for (int i = 0; i < codeLength; i++) {   
            code += (int) (Math.random() * 9);   
        }   
        return code;   
    }   
    private static boolean isValidChar(char ch) {   
        if ((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z'))   
            return true;   
        if ((ch >= 0x4e00 && ch <= 0x7fff) || (ch >= 0x8000 && ch <= 0x952f))   
            return true;// 简体中文汉字编码   
        return false;   
    }   
    /**  
     * 除去数组中的空值和签名参数  
     * @param sArray 签名参数组  
     * @return 去掉空值与签名参数后的新签名参数组  
     */   
    public static Map<String, String> paraFilter(Map<String, String> sArray) {   
        Map<String, String> result = new HashMap<String, String>();   
        if (sArray == null || sArray.size() <= 0) {   
            return result;   
        }   
        for (String key : sArray.keySet()) {   
            String value = sArray.get(key);   
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")   
                    || key.equalsIgnoreCase("sign_type")) {   
                continue;   
            }   
            result.put(key, value);   
        }   
        return result;   
    }
    public static Map<String, String> paraFilter2(Map<String, String> sArray) {
        Map<String, String> result = new HashMap<String, String>();
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }
    /**  
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串  
     * @param params 需要排序并参与字符拼接的参数组  
     * @return 拼接后字符串  
     */   
    public static String createLinkString(Map<String, String> params) {   
        List<String> keys = new ArrayList<String>(params.keySet());   
        Collections.sort(keys);   
        String prestr = "";   
        for (int i = 0; i < keys.size(); i++) {   
            String key = keys.get(i);   
            String value = params.get(key);   
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符   
                prestr = prestr + key + "=" + value;   
            } else {   
                prestr = prestr + key + "=" + value + "&";   
            }   
        }   
        return prestr;   
    }  
    /** 
	   *  
	   * 方法用途: 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序），并且生成url参数串<br> 
	   * 实现步骤: <br> 
	   *  
	   * @param paraMap   要排序的Map对象 
	   * @param urlEncode   是否需要URLENCODE 
	   * @param keyToLower    是否需要将Key转换为全小写 
	   *            true:key转化成小写，false:不转化 
	   * @return 
	   */
    public static String formatUrlMap(Map<String, String> paraMap, boolean urlEncode, boolean keyToLower){  
       String buff = "";  
       Map<String, String> tmpMap = paraMap;  
       try  
       {  
           List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());  
           // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）  
           Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>()  
           {  
               @Override  
               public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2)  
               {  
                   return (o1.getKey()).toString().compareTo(o2.getKey());  
               }  
           });  
           // 构造URL 键值对的格式  
           StringBuilder buf = new StringBuilder();  
           for (Map.Entry<String, String> item : infoIds)  
           {  
               if (StringUtils.isNotBlank(item.getKey()))
               {  
                   String key = item.getKey();  
                   String val = item.getValue();  
                   if (urlEncode)  
                   {  
                       val = URLEncoder.encode(val, "utf-8");  
                   }  
                   if (keyToLower)  
                   {  
                       buf.append(key.toLowerCase() + "=" + val);  
                   } else  
                   {  
                       buf.append(key + "=" + val);  
                   }  
                   buf.append("&");  
               }  
   
           }  
           buff = buf.toString();  
           if (buff.isEmpty() == false)  
           {  
               buff = buff.substring(0, buff.length() - 1);  
           }  
       } catch (Exception e)  
       {  
          return null;  
       }  
       return buff;  
   }  
    /**  
     *  
     * @param requestUrl请求地址
     * @param requestMethod请求方法
     * @param outputStr参数
     */   
    public static String httpRequest(String requestUrl,String requestMethod,String outputStr){   
        // 创建SSLContext   
        StringBuffer buffer = null;   
        try{   
	        URL url = new URL(requestUrl);   
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();   
	        conn.setRequestMethod(requestMethod);   
	        conn.setDoOutput(true);   
	        conn.setDoInput(true);   
	        conn.connect();   
	        //往服务器端写内容   
	        if(null !=outputStr){   
	            OutputStream os=conn.getOutputStream();   
	            os.write(outputStr.getBytes("utf-8"));   
	            os.close();   
	        }   
	        // 读取服务器端返回的内容   
	        InputStream is = conn.getInputStream();   
	        InputStreamReader isr = new InputStreamReader(is, "utf-8");   
	        BufferedReader br = new BufferedReader(isr);   
	        buffer = new StringBuffer();   
	        String line = null;   
	        while ((line = br.readLine()) != null) {   
	        	buffer.append(line);   
	        }   
	        br.close();
        }catch(Exception e){   
            e.printStackTrace();   
        }
        return buffer.toString();
    }
    /**
     * 方法名: getRemotePortData
     * 描述: 发送远程请求 获得代码示例
     * 参数：  @param urls 访问路径
     * 参数：  @param param 访问参数-字符串拼接格式, 例：port_d=10002&port_g=10007&country_a=
     * 创建人: huanghaibin
     * 创建时间: 2018年8月29日 下午13:20:32
     * 版本号: v1.0   
     * 返回类型: String
    */
    public static String getRemotePortData(String urls, String param){
       try {
           URL url = new URL(urls);
           HttpURLConnection conn = (HttpURLConnection) url.openConnection();
           // 设置连接超时时间
           conn.setConnectTimeout(30000);
           // 设置读取超时时间
           conn.setReadTimeout(30000);
           conn.setRequestMethod("POST");
           if(StringUtils.isNotBlank(param)) {
               conn.setRequestProperty("Origin", "https://sirius.searates.com");// 主要参数
               conn.setRequestProperty("Referer", "https://sirius.searates.com/cn/port?A=ChIJP1j2OhRahjURNsllbOuKc3Y&D=567&G=16959&shipment=1&container=20st&weight=1&product=0&request=&weightcargo=1&");
               conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");// 主要参数
           }
           // 需要输出
           conn.setDoInput(true);
           // 需要输入
           conn.setDoOutput(true);
           // 设置是否使用缓存
           conn.setUseCaches(false);
           // 设置请求属性
           conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
           conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
           conn.setRequestProperty("Charset", "UTF-8");
           
           if(StringUtils.isNotBlank(param)) {
               // 建立输入流，向指向的URL传入参数
               DataOutputStream dos=new DataOutputStream(conn.getOutputStream());
               dos.writeBytes(param);
               dos.flush();
               dos.close();
           }
           // 输出返回结果
           InputStream input = conn.getInputStream();
           int resLen =0;
           byte[] res = new byte[1024];
           StringBuilder sb=new StringBuilder();
           while((resLen=input.read(res))!=-1){
               sb.append(new String(res, 0, resLen));
           }
           return sb.toString();
       } catch (MalformedURLException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }
       return "";
   }
    public static String urlEncodeUTF8(String source){   
        String result=source;   
        try {   
            result= URLEncoder.encode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {   
            // TODO Auto-generated catch block   
            e.printStackTrace();   
        }   
        return result;   
    } 
    /**
	 * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
	 * @param strxml
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static Map doXMLParse(String strxml) throws Exception {
		if(null == strxml || "".equals(strxml)) {
			return null;
		}
		
		Map m = new HashMap();
		InputStream in = String2Inputstream(strxml);
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(in);
		Element root = doc.getRootElement();
		List list = root.getChildren();
		Iterator it = list.iterator();
		while(it.hasNext()) {
			Element e = (Element) it.next();
			String k = e.getName();
			String v = "";
			List children = e.getChildren();
			if(children.isEmpty()) {
				v = e.getTextNormalize();
			} else {
				v = getChildrenText(children);
			}
			
			m.put(k, v);
		}
		
		//关闭流
		in.close();
		
		return m;
	}
	/**
	 * 获取子结点的xml
	 * @param children
	 * @return String
	 */
	public static String getChildrenText(List children) {
		StringBuffer sb = new StringBuffer();
		if(!children.isEmpty()) {
			Iterator it = children.iterator();
			while(it.hasNext()) {
				Element e = (Element) it.next();
				String name = e.getName();
				String value = e.getTextNormalize();
				List list = e.getChildren();
				sb.append("<" + name + ">");
				if(!list.isEmpty()) {
					sb.append(getChildrenText(list));
				}
				sb.append(value);
				sb.append("</" + name + ">");
			}
		}
		
		return sb.toString();
	}
	public static InputStream String2Inputstream(String str) {
		return new ByteArrayInputStream(str.getBytes());
	}
}
