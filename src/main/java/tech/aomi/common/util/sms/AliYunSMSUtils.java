package tech.aomi.common.util.sms;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Sean sean.snow@live.com createAt 2017/9/20
 */
@Slf4j
public class AliYunSMSUtils {

    private static final String SignatureMethod = "HMAC-SHA1";
    private static final String SignatureVersion = "1.0";
    private static final String SEND_SMS_ACTION = "SendSms";
    private static final String Version = "2017-05-25";
    private static final String RegionId = "cn-hangzhou";

    /**
     * @param accessKeyId    密钥id
     * @param signName       短信签名
     * @param templateCode   短信模板id
     * @param templateParam  短信模板参数 JSON 字符串
     * @param toPhoneNumbers 收件人最大1000个
     */
    public static void sendSms(String accessKeyId, String accessSecret, String signName, String templateCode, String templateParam, String... toPhoneNumbers) {

        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(new java.util.SimpleTimeZone(0, "GMT"));// 这里一定要设置GMT时区

        Map<String, String> paras = new HashMap<>();
        // 1. 系统参数
        paras.put("SignatureMethod", SignatureMethod);
        paras.put("SignatureNonce", java.util.UUID.randomUUID().toString());
        paras.put("AccessKeyId", accessKeyId);
        paras.put("SignatureVersion", SignatureVersion);
        paras.put("Timestamp", df.format(new java.util.Date()));
        paras.put("Format", "JSON");
        // 2. 业务API参数
        paras.put("Action", SEND_SMS_ACTION);
        paras.put("Version", Version);
        paras.put("RegionId", RegionId);
        paras.put("PhoneNumbers", String.join(",", toPhoneNumbers));
        paras.put("SignName", signName);
        if (null != templateParam) {
            paras.put("TemplateParam", templateParam);
        }
        paras.put("TemplateCode", templateCode);
//        paras.put("OutId", "123");

        String url = calUrl(accessSecret, paras);

        StringBuilder result = new StringBuilder();
        BufferedReader in = null;

        try {
            URL realUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setDoInput(true);
//            connection.setRequestProperty("connection", "Keep-Alive");
//            connection.setRequestProperty("content-type", "application/x-www-form-urlencoded");

            connection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            connection.setRequestProperty("Accept", "*/*");

            connection.connect();

            InputStream nativeIn;
            if (connection.getResponseCode() >= 200 && connection.getResponseCode() < 300) {
                nativeIn = connection.getInputStream();
            } else {
                nativeIn = connection.getErrorStream();
            }
            in = new BufferedReader(new InputStreamReader(nativeIn));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }

        } catch (IOException e) {
            LOGGER.error("短信发送失败:{}", e.getMessage(), e);
            result.append(e.getMessage());
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
        }
        LOGGER.debug("短信发送结果->{}", result);
    }


    private static String calUrl(String accessSecret, Map<String, String> args) {
        TreeMap<String, String> params = new TreeMap<>(args);

        StringBuffer data = new StringBuffer();

        params.forEach((k, v) -> data.append("&")
                .append(specialUrlEncode(k))
                .append("=")
                .append(specialUrlEncode(v)));
        String sortedQueryString = data.substring(1);

        StringBuilder stringToSign = new StringBuilder();
        stringToSign.append("GET").append("&");
        stringToSign.append(specialUrlEncode("/")).append("&");
        stringToSign.append(specialUrlEncode(sortedQueryString));

        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new javax.crypto.spec.SecretKeySpec((accessSecret + "&").getBytes(StandardCharsets.UTF_8), "HmacSHA1"));
            byte[] signData = mac.doFinal(stringToSign.toString().getBytes(StandardCharsets.UTF_8));

            // 6. 签名最后也要做特殊URL编码
            String sign = specialUrlEncode(Base64.encodeBase64String(signData));

            return "http://dysmsapi.aliyuncs.com/?Signature=" + sign + data;
        } catch (Exception e) {
            return "";
        }

    }

    private static String specialUrlEncode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8")
                    .replace("+", "%20")
                    .replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

}
