package buu.njj.studymemo.utils;

import android.util.Log;

import com.baidu.aip.nlp.AipNlp;

import org.json.JSONObject;

import java.util.HashMap;

public class AIPUtils {
    //设置APPID/AK/SK
    public static final String APP_ID = "17344770";
    public static final String API_KEY = "tBTXwgyAGORw4XGC9jfngmFv";
    public static final String SECRET_KEY = "I9MooMf3ErEztfrB8pL9dbNUIMLfLnrg";

    public static JSONObject getFamiliar(String userinput,String sqldata){
        // 传入可选参数调用接口
        // 初始化一个AipNlp
        Log.e("commit","commit");
        AipNlp client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        HashMap<String, Object> options = new HashMap<String, Object>();
        options.put("model", "CNN");
        // 短文本相似度
        JSONObject res = client.simnet(sqldata, userinput, options);

        System.out.println(res.toString());
        return res;
    }



}
