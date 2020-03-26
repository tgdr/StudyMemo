package buu.njj.studymemo.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import buu.njj.studymemo.R;
import buu.njj.studymemo.StudyApplication;
import buu.njj.studymemo.utils.TxCloudUtils;
import okhttp3.Call;
import okhttp3.MediaType;

public class DoAnswerActivity extends Activity implements TextWatcher {
    Bundle getdata=null;
    StudyApplication app;
    String strquestiontitle;
    String stranswer;
    String [] listanswer,listinput;
    TextView tvscore;
    String strquenum;
    boolean commitstatus = false;
    private Dialog progressDialog;
    TextView tvquestion;
    double scoreavg=-1;
    com.rengwuxian.materialedittext.MaterialEditText edidinput;
 String strquechapter;
    Button btnMorph;
 String strquetype;


 String struuid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (StudyApplication) getApplication();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_doanswer);
        tvscore = findViewById(R.id.showscore);
        tvquestion = findViewById(R.id.tv_question);
        getdata = getIntent().getBundleExtra("senddata");
        Log.e("getdata",getdata.getString("tv_quetitle")+"     ");
        strquestiontitle = getdata.getString("tv_quetitle");
        strquenum = getdata.getString("tv_quenum");
        stranswer = getdata.getString("answersheet");
        strquetype = getdata.getString("tv_quetype");
        strquechapter = getdata.getString("tv_quechapter");
        struuid = getdata.getString("_uuid");
        edidinput = findViewById(R.id.etinput);
        if(app.getSpf().getString("_uuid","")!=null && app.getSpf().getString("answer","")!=null&&app.getSpf().getString("_uuid","").equals(struuid) && !app.getSpf().getString("answer","").equals("")){
            edidinput.setText(app.getSpf().getString("answer","")+"");
            Toast.makeText(DoAnswerActivity.this,"已为你恢复上次的答案！",Toast.LENGTH_SHORT).show();
        }
        edidinput.addTextChangedListener(this);
        findViewById(R.id.title_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(commitstatus == false){
                    if(!edidinput.getText().toString().equals("")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(DoAnswerActivity.this);
                        builder.setTitle("退出提示").setMessage("您还没有提交结果是否保存当前输入并退出？");
                        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                commitstatus = true;
                                app.getEditor().putString("_uuid",struuid).putString("answer",edidinput.getText().toString()).commit();
                                finish();
                            }
                        }).setNegativeButton("直接退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                commitstatus= true;
                                app.getEditor().clear().commit();
                                finish();
                            }
                        });
                        builder.setCancelable(false);
                        builder.show();
                    }else{
                        finish();
                    }


                }
                else{
                    Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                    intent.putExtra("data","refresh");
                    LocalBroadcastManager.getInstance(DoAnswerActivity.this).sendBroadcast(intent);
                    sendBroadcast(intent);
                    finish();
                }

            }
        });
        // sample demonstrate how to morph button to green circle with icon
       btnMorph =  findViewById(R.id.btnMorph2);
// inside on click event
      //  Log.e("TAGTAGTAG", String.valueOf(getResources().getDimension(R.dimen.mb_height_56)));
//        MorphingButton.Params circle = MorphingButton.Params.create()
//                .duration(1500)
//
//                .cornerRadius(56) // 56 dp
//                .width(getWindowManager().getDefaultDisplay().getWidth()) // 56 dp
//                .height(56) // 56 dp
//                .color(getResources().getColor(R.color.green)) // normal state color
//                .colorPressed(getResources().getColor(R.color.green_dark)) // pressed state color
//                .icon(R.drawable.ic_commit); // icon
//        btnMorph.morph(circle);
        tvquestion.setText(strquechapter+":"+strquetype+"\n    "+strquestiontitle);

btnMorph.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        commitstatus = true;
        app.getEditor().clear().commit();



        progressDialog = new Dialog(DoAnswerActivity.this,R.style.progress_dialog);
        progressDialog.setContentView(R.layout.dialog_wait);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText("卖力计算中");
        progressDialog.show();


        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                int maxlength=-1;
                if(stranswer.length()>500 || edidinput.getText().toString().length()>500){
                    if(stranswer.length()>edidinput.getText().toString().length()){
                        listanswer =  new String[(int)Math.round(stranswer.length()/500.0)];
                        listinput =  new String[(int)Math.round(stranswer.length()/500.0)];
                        maxlength = listanswer.length;
                    }
                    else{
                        listanswer =  new String[(int)Math.round(edidinput.getText().toString().length()/500.0)];
                        listinput =  new String[(int)Math.round(edidinput.getText().toString().length()/500.0)];
                        maxlength = listanswer.length;
                    }

                    for(int i=0;i<maxlength;i++){
                        Log.e("ffffff",maxlength+"");
                        if(i<(maxlength-1)){
                            Log.e("index","start:"+(500*i)+" end:"+(500*(i+1)-1));
                            listinput[i]=edidinput.getText().toString().substring(500*i,500*(i+1)-1);
                            listanswer[i]=stranswer.substring(500*i,500*(i+1)-1);
                        }else{

                            listinput[i]=edidinput.getText().toString().substring(500*i,edidinput.getText().toString().length()-1);
                            listanswer[i]=stranswer.substring(500*i,stranswer.length()-1);
                        }

                    }




                    scoreavg=0;

                    for(int i=0;i<maxlength;i++){
                        JSONObject senddata = new JSONObject();
                        try {
                            Log.e("src"+i,listanswer[i]+"");
                            senddata.put("SrcText",listanswer[i]).put("TargetText",listinput[i]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        long nottime = System.currentTimeMillis();
                        String timestamp = (nottime/1000)+"";
                        //String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        // 注意时区，否则容易出错
                        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                        String date = sdf.format(new Date(Long.valueOf(nottime)));

                        try {
                            String payload = senddata.toString();
                            String hashedRequestPayload = TxCloudUtils.sha256Hex(payload);
                            String canonicalRequest = TxCloudUtils.httpRequestMethod + "\n" + TxCloudUtils.canonicalUri + "\n"
                                    + TxCloudUtils.canonicalQueryString + "\n"
                                    + TxCloudUtils.canonicalHeaders + "\n" + TxCloudUtils.signedHeaders + "\n"+hashedRequestPayload;
                            System.out.println(canonicalRequest);

                            // ************* 步骤 2：拼接待签名字符串 *************
                            String credentialScope = date + "/" + TxCloudUtils.service + "/" + "tc3_request";
                            String hashedCanonicalRequest = TxCloudUtils.sha256Hex(canonicalRequest);
                            String stringToSign = TxCloudUtils.algorithm + "\n" + ((Long.parseLong(timestamp))) + "\n"
                                    + credentialScope + "\n" + hashedCanonicalRequest;
                            System.out.println(stringToSign);

                            // ************* 步骤 3：计算签名 *************
                            byte[] secretDate = TxCloudUtils.hmac256(("TC3" + TxCloudUtils.SecretKey).getBytes(TxCloudUtils.UTF8), date);
                            byte[] secretService = TxCloudUtils.hmac256(secretDate, TxCloudUtils.service);
                            byte[] secretSigning = TxCloudUtils.hmac256(secretService, "tc3_request");
                            String signature =new String(org.apache.commons.codec.binary.Hex.encodeHex
                                    (TxCloudUtils.hmac256(secretSigning, stringToSign)));
                            //System.out.println(signature);
                            //  Log.e("timestamps",timestamp);
                            String authorization = TxCloudUtils.algorithm + " " + "Credential=" + TxCloudUtils.SecretId
                                    + "/" + credentialScope + ","+ "SignedHeaders=" + TxCloudUtils.signedHeaders + "," +
                                    "Signature=" + signature;
                            //    Log.e("authorization",authorization);
                            OkHttpUtils.postString().mediaType(MediaType.parse("application/json; charset=utf-8"))
                                    .addHeader("Host","nlp.tencentcloudapi.com").addHeader("Authorization",authorization)
                                    .addHeader("X-TC-Action",TxCloudUtils.action)
                                    .addHeader("X-TC-Timestamp", timestamp)
                                    .addHeader("X-TC-Version", TxCloudUtils.version)
                                    .addHeader("X-TC-Region", TxCloudUtils.region)
                                    .url("https://nlp.tencentcloudapi.com")
                                    .content(senddata.toString())
                                    .build().execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    Log.e("eeeerrrrooorrr",e.getMessage());
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    Log.e("eeeerrrrooorrr",response);
                                    try {
                                        JSONObject getdata = new JSONObject(response);
                                        if(getdata.has("Response")){
                                            getdata = getdata.getJSONObject("Response");
                                            if(getdata.has("Similarity")){
                                                final double score = getdata.getDouble("Similarity")*100;
                                                scoreavg +=score;
                                            Log.e("i",scoreavg+"");
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    try {
                        Thread.sleep(2500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Log.e("scoreavg","total="+scoreavg+",count="+listanswer.length);
                    scoreavg = scoreavg/listanswer.length;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            NumberFormat nf = NumberFormat.getNumberInstance();
                            nf.setMaximumFractionDigits(2);
                            final String lastscore= nf.format(scoreavg);
                            System.out.println(lastscore);
                            tvscore.setText(lastscore);
                            tvscore.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent it = new Intent(DoAnswerActivity.this,TextColorActivity.class);
                                    it.putExtra("useroffer",edidinput.getText().toString());
                                    it.putExtra("bzoffer",stranswer);
                                    startActivity(it);
                                    finish();
                                }
                            });
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    int rs;
                                    final DBHelper helper1 = new DBHelper();

                                    try {
                                        rs= helper1.updatescore(struuid,Float.parseFloat(lastscore));
                                        if(rs >=1){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(DoAnswerActivity.this,"分数提交成功！",Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                        }
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    });

                }
                else{



                    scoreavg=0;

                        JSONObject senddata = new JSONObject();
                        try {
                            senddata.put("SrcText",stranswer).put("TargetText",edidinput.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        long nottime = System.currentTimeMillis();
                        String timestamp = (nottime/1000)+"";
                        //String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        // 注意时区，否则容易出错
                        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                        String date = sdf.format(new Date(Long.valueOf(nottime)));

                        try {
                            String payload = senddata.toString();
                            String hashedRequestPayload = TxCloudUtils.sha256Hex(payload);
                            String canonicalRequest = TxCloudUtils.httpRequestMethod + "\n" + TxCloudUtils.canonicalUri + "\n"
                                    + TxCloudUtils.canonicalQueryString + "\n"
                                    + TxCloudUtils.canonicalHeaders + "\n" + TxCloudUtils.signedHeaders + "\n"+hashedRequestPayload;
                            System.out.println(canonicalRequest);

                            // ************* 步骤 2：拼接待签名字符串 *************
                            String credentialScope = date + "/" + TxCloudUtils.service + "/" + "tc3_request";
                            String hashedCanonicalRequest = TxCloudUtils.sha256Hex(canonicalRequest);
                            String stringToSign = TxCloudUtils.algorithm + "\n" + ((Long.parseLong(timestamp))) + "\n"
                                    + credentialScope + "\n" + hashedCanonicalRequest;
                            System.out.println(stringToSign);

                            // ************* 步骤 3：计算签名 *************
                            byte[] secretDate = TxCloudUtils.hmac256(("TC3" + TxCloudUtils.SecretKey).getBytes(TxCloudUtils.UTF8), date);
                            byte[] secretService = TxCloudUtils.hmac256(secretDate, TxCloudUtils.service);
                            byte[] secretSigning = TxCloudUtils.hmac256(secretService, "tc3_request");
                            String signature =new String(org.apache.commons.codec.binary.Hex.encodeHex
                                    (TxCloudUtils.hmac256(secretSigning, stringToSign)));
                            //System.out.println(signature);
                            //  Log.e("timestamps",timestamp);
                            String authorization = TxCloudUtils.algorithm + " " + "Credential=" + TxCloudUtils.SecretId
                                    + "/" + credentialScope + ","+ "SignedHeaders=" + TxCloudUtils.signedHeaders + "," +
                                    "Signature=" + signature;
                            //    Log.e("authorization",authorization);
                            OkHttpUtils.postString().mediaType(MediaType.parse("application/json; charset=utf-8"))
                                    .addHeader("Host","nlp.tencentcloudapi.com").addHeader("Authorization",authorization)
                                    .addHeader("X-TC-Action",TxCloudUtils.action)
                                    .addHeader("X-TC-Timestamp", timestamp)
                                    .addHeader("X-TC-Version", TxCloudUtils.version)
                                    .addHeader("X-TC-Region", TxCloudUtils.region)
                                    .url("https://nlp.tencentcloudapi.com")
                                    .content(senddata.toString())
                                    .build().execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    Log.e("eeeerrrrooorrr",e.getMessage());
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    Log.e("eeeerrrrooorrr",response);
                                    try {
                                        JSONObject getdata = new JSONObject(response);
                                        if(getdata.has("Response")){
                                            getdata = getdata.getJSONObject("Response");
                                            if(getdata.has("Similarity")){
                                                final double score = getdata.getDouble("Similarity")*100;
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        progressDialog.dismiss();
                                                        NumberFormat nf = NumberFormat.getNumberInstance();
                                                        nf.setMaximumFractionDigits(2);
                                                        final String lastscore= nf.format(score);
                                                        System.out.println(lastscore);
                                                        tvscore.setText(lastscore);
                                                        tvscore.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                Intent it = new Intent(DoAnswerActivity.this,TextColorActivity.class);
                                                                it.putExtra("useroffer",edidinput.getText().toString());
                                                                it.putExtra("bzoffer",stranswer);
                                                                startActivity(it);
                                                                finish();
                                                            }
                                                        });
                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                int rs;
                                                                final DBHelper helper1 = new DBHelper();

                                                                try {
                                                                    rs= helper1.updatescore(struuid,Float.parseFloat(lastscore));
                                                                    if(rs >=1){
                                                                        runOnUiThread(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                Toast.makeText(DoAnswerActivity.this,"分数提交成功！",Toast.LENGTH_SHORT).show();

                                                                            }
                                                                        });
                                                                    }
                                                                } catch (SQLException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }).start();
                                                    }
                                                });
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                }
            }
        }).start();


    }

});

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(edidinput.getText().toString()) && !edidinput.getText().equals("")){
            btnMorph.setBackgroundResource(R.drawable.bg_login_submit);
            btnMorph.setTextColor(getResources().getColor(R.color.white));
            btnMorph.setClickable(true);
        }

        else{
            btnMorph.setBackgroundResource(R.drawable.bg_login_submit_lock);
            btnMorph.setTextColor(getResources().getColor(R.color.account_lock_font_color));
            btnMorph.setClickable(false);
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!edidinput.getText().equals("")){

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!edidinput.getText().toString().equals("") && commitstatus == false){
         //  Log.e("ffffdffsdfsdfsdfs",edidinput.getText().toString());
            app.getEditor().putString("_uuid",struuid).putString("answer",edidinput.getText().toString()).commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!edidinput.getText().toString().equals("") && commitstatus ==false){
            app.getEditor().putString("_uuid",struuid).putString("answer",edidinput.getText().toString()).commit();

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 此处写你按返回键之后要执行的事件的逻辑
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
