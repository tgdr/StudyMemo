package buu.njj.studymemo.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.kongzue.dialog.interfaces.OnMenuItemClickListener;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.BottomMenu;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import buu.njj.studymemo.MainActivity;
import buu.njj.studymemo.R;
import buu.njj.studymemo.StudyApplication;
import buu.njj.studymemo.adapter.MainUserHomeGridAdapter;
import buu.njj.studymemo.bean.GridSubject;
import buu.njj.studymemo.dao.UserInfoDBhelper;

public class MainUserHomeFragment extends Fragment {
    private static final String TAG = "MainUserHomeFragment";
    @BindView(R.id.grid_activity_user_home)
    GridView gridView;
    StudyApplication app;
    String un;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        app = (StudyApplication) getActivity().getApplication();

        DialogSettings.isUseBlur = true;
        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
        DialogSettings.isUseBlur = true;                   //是否开启模糊效果，默认关闭
        DialogSettings.modalDialog = true;                 //是否开启模态窗口模式，一次显示多个对话框将以队列形式一个一个显示，默认关闭
        DialogSettings.cancelable = false;
        View v = inflater.inflate(R.layout.activity_main_user_home,container,false);
        ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        un =app.getUserName();

        new Thread(() -> {
            try {
                List<GridSubject> girddata = UserInfoDBhelper.getUserStudyType(un);
                List<String> tableNames = new ArrayList<>();


                Map<Integer,List<String>> types= new LinkedHashMap<>();
                for(int i=0;i<girddata.size();i++){
                    types.put(girddata.get(i).get_id(),UserInfoDBhelper.getQuestionType(girddata.get(i).get_id()));
                    tableNames.add(UserInfoDBhelper.getTableNameById(girddata.get(i).get_id()));

                }
                EventBus.getDefault().post(new Object[]{girddata,types,tableNames});
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();

    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void setGridViewData(Object[] datas){
        List<GridSubject> girddata;
        List<String> tableNames;
        Map<Integer,List<String>> types;
        girddata = (List<GridSubject>) datas[0];
        types = (Map<Integer, List<String>>) datas[1];
        tableNames = (List<String>) datas[2];
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setAdapter(new MainUserHomeGridAdapter(getActivity(),girddata));
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            if(types.get(girddata.get(position).get_id())!=null){

                BottomMenu.show((AppCompatActivity) getActivity(), types.get(girddata.get(position).get_id()), new OnMenuItemClickListener() {
                    @Override
                    public void onClick(String text, int index) {
                            MainUserChapterFragment fragment = new MainUserChapterFragment();
                            fragment.setData(tableNames.get(position),text);
                            MainActivity activity = (MainActivity) getActivity();
                            activity.setOnTitleChangedListener(new MainActivity.onTitleChanged() {
                                @Override
                                public String setTitle() {
                                    return text+"";
                                }
                            });
                            getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,fragment).commit();

                    }
                }).setTitle("请选择想要学习的题目类型").setCancelButtonText("再看看别的科目");
            }
            else{
                Toast.makeText(app, "当前还没有此科目的题目分类！", Toast.LENGTH_SHORT).show();
            }
        });

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



}
