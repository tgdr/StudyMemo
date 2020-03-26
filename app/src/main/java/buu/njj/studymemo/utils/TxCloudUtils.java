package buu.njj.studymemo.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class TxCloudUtils {
    public static String algorithm = "TC3-HMAC-SHA256";
    public static String service = "nlp";
    public static String SecretId = "AKIDDMFsKRhcr9ICqFALTP5Kr894nJWVwoFT";
    public static String SecretKey = "B6rPozziBNL2cBUhhV64nG1WkiNingVM";
    public static String APPID = "1251811493";
    public static String httpRequestMethod= "POST";
    public  final static Charset UTF8 = StandardCharsets.UTF_8;
    private final static String CT_JSON = "application/json; charset=utf-8";
    public static String host = "nlp.tencentcloudapi.com";
    public static String region = "ap-guangzhou";
    public static String action = "SentenceSimilarity";
    public static String version = "2019-04-08";
    public static String canonicalUri = "/";
    public static String canonicalQueryString = "";
    public static String canonicalHeaders = "content-type:application/json; charset=utf-8\n" + "host:" + host + "\n";
    public static String signedHeaders = "content-type;host";

    public static byte[] hmac256(byte[] key, String msg) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, mac.getAlgorithm());
        mac.init(secretKeySpec);
        return mac.doFinal(msg.getBytes(UTF8));
    }

    public static String sha256Hex(String s) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] d = md.digest(s.getBytes(UTF8));
        return new String(org.apache.commons.codec.binary.Hex.encodeHex(d)).toLowerCase();
    }

//    public static String[] getsign()  {
//        String [] datas= new String [3];
//        //datas0:credentialScope
//        StringBuilder sb = null;
//        try{
//            sb = new StringBuilder();
//
//
//
//
//        String timestamp = System.currentTimeMillis()+"";
//        //String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        // 注意时区，否则容易出错
//        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
//        String date = sdf.format(new Date(Long.valueOf(timestamp)));
//
//        // ************* 步骤 1：拼接规范请求串 *************
//
//
//        String payload = "{\"Limit\": 1, \"Filters\": [{\"Values\": [\"\\u672a\\u547d\\u540d\"], \"Name\": \"instance-name\"}]}";
//        String hashedRequestPayload = sha256Hex(payload);
//        String canonicalRequest = httpRequestMethod + "\n" + canonicalUri + "\n" + canonicalQueryString + "\n"
//                + canonicalHeaders + "\n" + signedHeaders + "\n" + hashedRequestPayload;
//        System.out.println(canonicalRequest);
//
//        // ************* 步骤 2：拼接待签名字符串 *************
//        String credentialScope = date + "/" + service + "/" + "tc3_request";
//
//        String hashedCanonicalRequest = sha256Hex(canonicalRequest);
//        String stringToSign = algorithm + "\n" + timestamp + "\n" + credentialScope + "\n" + hashedCanonicalRequest;
//        System.out.println(stringToSign);
//
//        // ************* 步骤 3：计算签名 *************
//        byte[] secretDate = hmac256(("TC3" + SECRET_KEY).getBytes(UTF8), date);
//        byte[] secretService = hmac256(secretDate, service);
//        byte[] secretSigning = hmac256(secretService, "tc3_request");
//        String signature =new String(org.apache.commons.codec.binary.Hex.encodeHex(hmac256(secretSigning, stringToSign))).toLowerCase();
//        System.out.println(signature);
//
//        // ************* 步骤 4：拼接 Authorization *************
//        String authorization = algorithm + " " + "Credential=" + SECRET_ID + "/" + credentialScope + ", "
//                + "SignedHeaders=" + signedHeaders + ", " + "Signature=" + signature;
//        System.out.println(authorization);
//        datas[0]= authorization;
//        TreeMap<String, String> headers = new TreeMap<String, String>();
//        headers.put("Authorization", authorization);
//        headers.put("Content-Type", CT_JSON);
//        headers.put("Host", host);
//        headers.put("X-TC-Action", action);
//        headers.put("X-TC-Timestamp", timestamp);
//        datas[1]= timestamp;
//        headers.put("X-TC-Version", version);
//        headers.put("X-TC-Region", region);
//
//
//        sb.append("curl -X POST https://").append(host)
//                .append(" -H \"Authorization: ").append(authorization).append("\"")
//                .append(" -H \"Content-Type: application/json; charset=utf-8\"")
//                .append(" -H \"Host: ").append(host).append("\"")
//                .append(" -H \"X-TC-Action: ").append(action).append("\"")
//                .append(" -H \"X-TC-Timestamp: ").append(timestamp).append("\"")
//                .append(" -H \"X-TC-Version: ").append(version).append("\"")
//                .append(" -H \"X-TC-Region: ").append(region).append("\"")
//                .append(" -d '").append(payload).append("'");
//        System.out.println(sb.toString());
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//        return  datas;
//    }




//
//    public static String  getSentenceSimilarityResult(String SrcText,String TargetText) {
//        String result = null;
//        try {
//            Credential cred = new Credential(SecretId, SecretKey);
//
//            HttpProfile httpProfile = new HttpProfile();
//            httpProfile.setEndpoint("nlp.tencentcloudapi.com");
//
//            ClientProfile clientProfile = new ClientProfile();
//            clientProfile.setHttpProfile(httpProfile);
//
//            NlpClient client = new NlpClient(cred, "ap-guangzhou", clientProfile);
//            JSONObject object = new JSONObject();
//            object.put("SrcText",SrcText);
//            object.put("TargetText",TargetText);
//            SentenceSimilarityRequest req = SentenceSimilarityRequest.fromJsonString(object.toString(), SentenceSimilarityRequest.class);
//            SentenceSimilarityResponse resp = client.SentenceSimilarity(req);
//            result = SentenceSimilarityRequest.toJsonString(resp);
//            System.out.println(result);
//        } catch (TencentCloudSDKException e) {
//            System.out.println(e.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//return result;
//    }

    public static String hmacSha256(String KEY, String VALUE) {
        return hmacSha(KEY, VALUE, "HmacSHA256");
    }

    private static String hmacSha(String KEY, String VALUE, String SHA_TYPE) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(KEY.getBytes("UTF-8"), SHA_TYPE);
            Mac mac = Mac.getInstance(SHA_TYPE);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(VALUE.getBytes("UTF-8"));

            byte[] hexArray = {
                    (byte)'0', (byte)'1', (byte)'2', (byte)'3',
                    (byte)'4', (byte)'5', (byte)'6', (byte)'7',
                    (byte)'8', (byte)'9', (byte)'a', (byte)'b',
                    (byte)'c', (byte)'d', (byte)'e', (byte)'f'
            };
            byte[] hexChars = new byte[rawHmac.length * 2];
            for ( int j = 0; j < rawHmac.length; j++ ) {
                int v = rawHmac[j] & 0xFF;
                hexChars[j * 2] = hexArray[v >>> 4];
                hexChars[j * 2 + 1] = hexArray[v & 0x0F];
            }
            return new String(hexChars);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
