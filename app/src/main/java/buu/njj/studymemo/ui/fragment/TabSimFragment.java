package buu.njj.studymemo.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.androidkun.xtablayout.XTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import buu.njj.studymemo.R;
import buu.njj.studymemo.bean.CardData;

public class TabSimFragment extends Fragment {

    ArrayList<Float> scoreList = new ArrayList<>();
    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.txt_progress)
    TextView txt_progress;
    long leftTime=0;
    @BindView(R.id.tab)
    XTabLayout tabLayout;
    @BindView(R.id.pagers)
    ViewPager2 pagers;
    @BindView(R.id.txt_countnow)
    TextView txt_countdown;
//    @BindView(R.id.btn_submit)
//    Button btn_submit;
    List<CardData> cardDatas;

    List<Fragment> fragments = new ArrayList<>();
    Handler handler = new Handler();
    Runnable update_thread = new Runnable() {
        @Override
        public void run() {
            leftTime--;
          //  LogUtil.e("leftTime="+leftTime);
            if (leftTime > 0) {
                //倒计时效果展示
                String formatLongToTimeStr = formatLongToTimeStr(leftTime);
                txt_countdown.setText(formatLongToTimeStr);
                //每一秒执行一次
                handler.postDelayed(this, 1000);
            } else {//倒计时结束
                //处理业务流程

                //发送消息，结束倒计时
                Message message = Message.obtain();
                message.what = 1;
                handlerStop.sendMessage(message);
            }
        }
    };

    final Handler handlerStop = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    leftTime = 0;
                    handler.removeCallbacks(update_thread);
                    break;
            }
            super.handleMessage(msg);
        }

    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_simtest_main,null,false);
        cardDatas = onDataSet.setdata();
        ButterKnife.bind(this,v);
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        leftTime = onDataSet.setTime();
        handler.postDelayed(update_thread, 10);
        for(int i =0;i<cardDatas.size();i++){
            XTabLayout.Tab t = tabLayout.newTab().setText((i+1)+"");
            tabLayout.addTab(t);
            TabSimContentFragment tabSimContentFragment = new TabSimContentFragment();
            int finalI = i;
            tabSimContentFragment.setOnDataListener(new TabSimContentFragment.onDataListener() {
                @Override
                public String setNum() {
                    return (finalI+1)+"";
                }

                @Override
                public String setQuestionTitle() {
                    return cardDatas.get(finalI).getQuestion();
                }

                @Override
                public String setType() {
                    return cardDatas.get(finalI).getQue_type();
                }

                @Override
                public String setChapter() {
                    return cardDatas.get(finalI).getChapter();
                }

                @Override
                public String setAnswer() {
                    return cardDatas.get(finalI).getAnswer();
                }

                @Override
                public String _uuid() {
                    return cardDatas.get(finalI).get_uuid();
                }
            });
            fragments.add(tabSimContentFragment);
        }

        pagers.setAdapter(new FragmentStateAdapter(getFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
             //   txt_progress.setText("当前进度："+(position+1)+"/"+fragments.size());
                return fragments.get(position);
            }

            @Override
            public int getItemCount() {
                return fragments.size();
            }
        });
        pagers.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                txt_progress.setText("当前进度："+(position+1)+"/"+fragments.size());
            }
        });
        tabLayout.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                int position = tab.getPosition();
                pagers.setCurrentItem(position);

            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {

            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        leftTime = 0;
        handler.removeCallbacks(update_thread);
    }

    public String formatLongToTimeStr(Long l) {
        int hour = 0;
        int minute = 0;
        int second = 0;
        second = l.intValue() ;
        if (second > 60) {
            minute = second / 60;   //取整
            second = second % 60;   //取余
        }
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        String strtime = "剩余："+hour+"小时"+minute+"分"+second+"秒";
        return strtime;
    }

    public void setOnDataSet(TabSimFragment.onDataSet onDataSet) {
        this.onDataSet = onDataSet;
    }

    onDataSet onDataSet;
    @OnClick({R.id.btn_submit})
    public void myclicksubmit(View v){
        /*new Thread(new Runnable() {
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
                                    Intent it = new Intent(DoAnswerActivity.this, TextColorActivity.class);
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
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                //    progressDialog.dismiss();
                                                    NumberFormat nf = NumberFormat.getNumberInstance();
                                                    nf.setMaximumFractionDigits(2);
                                                    final String lastscore= nf.format(score);
                                                    System.out.println(lastscore);
                                                  //  tvscore.setText(lastscore);
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
        }).start();*/
    }

    public interface onDataSet{
        List<CardData> setdata();
        long setTime();
    }

}
