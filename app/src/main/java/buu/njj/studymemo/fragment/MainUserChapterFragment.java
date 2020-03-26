package buu.njj.studymemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import buu.njj.studymemo.R;
import buu.njj.studymemo.adapter.MainUserChapterAdapter;
import buu.njj.studymemo.bean.Chapter_question;
import buu.njj.studymemo.dao.QuestionDbHelper;
import buu.njj.studymemo.ui.DBHelper;
import buu.njj.studymemo.ui.DoAnswerActivity;

public class MainUserChapterFragment extends Fragment {

    private  String tableName;
    private String queType;
    private String _uuid;
    @BindView(R.id.ex_main_learing_list)
    ExpandableListView expandableListView;
    @BindView(R.id.myprocessbar)
    ProgressBar progressBar;
    @BindView(R.id.txt_status)
    TextView txt_status;
    MainUserChapterAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_main_learning_list,null);
        ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar.setVisibility(View.VISIBLE);
        txt_status.setVisibility(View.VISIBLE);
            new Thread(() -> {
                try {
                    List<Chapter_question> questions = QuestionDbHelper.getDatas(tableName,queType);
                    EventBus.getDefault().post(questions);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }).start();



    }
    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void setMyAdapter(List<Chapter_question> questions){
        adapter = new MainUserChapterAdapter(questions,getActivity());
        expandableListView.setVisibility(View.VISIBLE);
        expandableListView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
        txt_status.setVisibility(View.GONE);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ResultSet rs2;
                            final DBHelper helper1 = new DBHelper();

                            rs2= helper1.getdatabyuuid(questions.get(groupPosition).getChildren().get(childPosition).get_uuid());
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

                return true;
            }
        });
    }

    public void setData(String tableName,String queType){
        this.tableName = tableName;
        this.queType = queType;


    }


    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter!=null){

            expandableListView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            txt_status.setVisibility(View.VISIBLE);

            Log.e("TAG", "onResume: " );
            new Thread(() -> {
                try {
                    List<Chapter_question> questions = QuestionDbHelper.getDatas(tableName,queType);
                    EventBus.getDefault().post(questions);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }).start();
            adapter.notifyDataSetChanged();

        }

    }
}
