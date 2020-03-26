package buu.njj.studymemo.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import buu.njj.studymemo.R;
import buu.njj.studymemo.StudyApplication;
import buu.njj.studymemo.adapter.RecyclerViewAdapter;
import buu.njj.studymemo.bean.CardData;
import buu.njj.studymemo.ui.DBHelper;
import buu.njj.studymemo.ui.DoAnswerActivity;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter mAdapter;
    StudyApplication app;
    private ArrayList<CardData> dataList;
    ArrayList question;
    ArrayList _id;
    ArrayList answer;
    ArrayList que_type;
    ArrayList chapter;
    ArrayList status;
    ArrayList _uuid;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
      //  final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        app = (StudyApplication) getActivity().getApplication();
        //初始化数据集
        dataList = new ArrayList<>();
        recyclerView = getView().findViewById(R.id.recycler_view);
        question = new ArrayList();
        _id = new ArrayList();
        answer = new ArrayList();
        que_type = new ArrayList();
        chapter = new ArrayList();
        status = new ArrayList();
        _uuid = new ArrayList();
        initData();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("hahahahahahaha", "hahahahahahaha");
                RecyclerView.LayoutManager linearManager = new LinearLayoutManager(getActivity());
                mAdapter = new RecyclerViewAdapter(getActivity(), dataList);

                //设置Adapter
                recyclerView.setAdapter(mAdapter);
                //设置布局类型为线性布局
                recyclerView.setLayoutManager(linearManager);
                if(app.getSpf().getString("_uuid","")!=null && app.getSpf().getString("answer","")!=null && !app.getSpf().getString("answer","").equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("是否恢复？").setMessage("检测到你有上次没做完的题目，是否跳转到上次的答题页面继续作答?");
                    builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final ResultSet rs2;

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        ResultSet rs2;
                                        final DBHelper helper1 = new DBHelper();

                                        rs2= helper1.getdatabyuuid(app.getSpf().getString("_uuid",""));
                                        if(rs2.next()){
                                            Intent it = new Intent(getActivity(), DoAnswerActivity.class);
                                            Bundle putdata = new Bundle();
                                            putdata.putString("tv_quetitle",rs2.getString("question"));
                                            putdata.putString("tv_quenum",rs2.getInt("_id")+"");
                                            putdata.putString("tv_quetype",rs2.getString("que_type"));
                                            putdata.putString("tv_quechapter",rs2.getString("chapter"));
                                            putdata.putString("_uuid",rs2.getString("_uuid"));
                                            putdata.putString("answersheet",rs2.getString("answer"));
                                            it.putExtra("senddata",putdata);
                                            startActivity(it);
                                        }
                                        rs2.close();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();




                        }
                    });
                    builder.setNegativeButton("算了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            app.getEditor().clear().commit();
                        }
                    });
                    builder.show();
                }




            }
        });

    }


        private void initData() {
            question.clear();
            _id.clear();
            answer.clear();
            que_type.clear();
            chapter.clear();
            status.clear();
            _uuid.clear();
            dataList.clear();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final DBHelper helper = new DBHelper();
                    try {

                        ResultSet rs= helper.getdata();

                        while (rs.next()){
                            //Log.e("nnnnnnnn","nnnnnnnnnn");
                            question.add(rs.getString("question"));
                            _id.add(rs.getInt("_id")+"");
                            answer.add(rs.getString("answer"));
                            que_type.add(rs.getString("que_type"));
                            chapter.add(rs.getString("chapter"));
                            status.add(rs.getInt("status"));
                            _uuid.add(rs.getString("_uuid"));
                        }
                        for(int i=0;i<_id.size();i++){
                            Log.e("idididid",_id.get(i).toString());
                            dataList.add(new CardData(_id.get(i).toString(),question.get(i).toString(),answer.get(i).toString(),status.get(i).toString()
                                    ,chapter.get(i).toString(),que_type.get(i).toString(),_uuid.get(i).toString()));
                        }
                        rs.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("hahahahahahaha", "hahahahahahaha");
                            RecyclerView.LayoutManager linearManager = new LinearLayoutManager(getActivity());
                            mAdapter = new RecyclerViewAdapter(getActivity(), dataList);

                            //设置Adapter
                            recyclerView.setAdapter(mAdapter);
                            //设置布局类型为线性布局
                            recyclerView.setLayoutManager(linearManager);
                            if(app.getSpf().getString("_uuid","")!=null && app.getSpf().getString("answer","")!=null && !app.getSpf().getString("answer","").equals("")){
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("是否恢复？").setMessage("检测到你有上次没做完的题目，是否跳转到上次的答题页面继续作答?");
                                builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        final ResultSet rs2;

                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    ResultSet rs2;
                                                    final DBHelper helper1 = new DBHelper();

                                                    rs2= helper1.getdatabyuuid(app.getSpf().getString("_uuid",""));
                                                    if(rs2.next()){
                                                        Intent it = new Intent(getActivity(), DoAnswerActivity.class);
                                                        Bundle putdata = new Bundle();
                                                        putdata.putString("tv_quetitle",rs2.getString("question"));
                                                        putdata.putString("tv_quenum",rs2.getInt("_id")+"");
                                                        putdata.putString("tv_quetype",rs2.getString("que_type"));
                                                        putdata.putString("tv_quechapter",rs2.getString("chapter"));
                                                        putdata.putString("_uuid",rs2.getString("_uuid"));
                                                        putdata.putString("answersheet",rs2.getString("answer"));
                                                        it.putExtra("senddata",putdata);
                                                        startActivity(it);
                                                    }
                                                    rs2.close();
                                                } catch (SQLException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).start();




                                    }
                                });
                                builder.setNegativeButton("算了", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        app.getEditor().clear().commit();
                                    }
                                });
                                builder.show();
                            }




                        }
                    });

                }
            }).start();




    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume
            Log.e("hahahah","rererere");
        } else {
            //相当于Fragment的onPause
            Log.e("hahahahah","p[auauauau");
            initData();

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("hahaha","onpause");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CART_BROADCAST");
        BroadcastReceiver mItemViewListClickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                String msg = intent.getStringExtra("data");
                if("refresh".equals(msg)){
                initData();
                    mAdapter.notifyDataSetChanged();
                }
            }
        };
        broadcastManager.registerReceiver(mItemViewListClickReceiver, intentFilter);
    }

    @Override
    public void onResume() {
        super.onResume();


    }
}