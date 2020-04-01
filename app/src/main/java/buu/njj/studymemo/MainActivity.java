package buu.njj.studymemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import buu.njj.studymemo.ui.DBHelper;
import buu.njj.studymemo.ui.DoAnswerActivity;
import buu.njj.studymemo.ui.fragment.MainUserHomeFragment;
import buu.njj.studymemo.ui.fragment.SimulateSettingFragment;
import buu.njj.studymemo.ui.notifications.NotificationsFragment;

public class  MainActivity extends AppCompatActivity {

    FragmentManager manager;
    @BindView(R.id.ib_navigation_back)
    ImageView img_back;
    @BindView(R.id.tv_navigation_label)
    TextView  txt_title;
    FragmentTransaction transaction;
    List<Fragment> fragments;
    @BindView(R.id.layout_title)
    LinearLayout layout_titles;
    @BindView(R.id.nav_host_fragment)
    FrameLayout frameLayout;
StudyApplication app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        txt_title.setText("主页");
        app = (StudyApplication) getApplication();
        fragments = new ArrayList<>();

        fragments.add(new MainUserHomeFragment());
        fragments.add(new SimulateSettingFragment());
       // fragments.add(new HomeFragment());
        fragments.add(new NotificationsFragment());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment,fragments.get(0)).commit();
        //navView.setItemIconTintList(null);

        if(app.getSpf().getString("_uuid","")!=null && app.getSpf().getString("answer","")!=null && !app.getSpf().getString("answer","").equals("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("是否恢复？").setMessage("检测到你有上次没做完的题目，是否跳转到上次的答题页面继续作答?");
            builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final ResultSet rs2;

                    new Thread(() -> {
                        try {
                            ResultSet rs21;
                            final DBHelper helper1 = new DBHelper();

                            rs21 = helper1.getdatabyuuid(app.getSpf().getString("_uuid",""));
                            if(rs21.next()){
                                Intent it = new Intent(MainActivity.this, DoAnswerActivity.class);
                                Bundle putdata = new Bundle();
                                putdata.putString("tv_quetitle", rs21.getString("question"));
                                putdata.putString("tv_quenum", rs21.getInt("_id")+"");
                                putdata.putString("tv_quetype", rs21.getString("que_type"));
                                putdata.putString("tv_quechapter", rs21.getString("chapter"));
                                putdata.putString("_uuid", rs21.getString("_uuid"));
                                putdata.putString("answersheet", rs21.getString("answer"));
                                it.putExtra("senddata",putdata);
                                startActivity(it);
                            }
                            rs21.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
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


        navView.setOnNavigationItemSelectedListener(menuItem -> {
            if(menuItem.getItemId() == R.id.navigation_user_center){

                layout_titles.setVisibility(View.GONE);
                transaction = manager.beginTransaction();

                transaction.replace(R.id.nav_host_fragment,fragments.get(2)).commit();
            }
            else if(menuItem.getItemId() == R.id.navigation_home){
                layout_titles.setVisibility(View.VISIBLE);
                txt_title.setText("主页");

                transaction = manager.beginTransaction();
                transaction.replace(R.id.nav_host_fragment,fragments.get(0)).commit();
            }
            else if(menuItem.getItemId() == R.id.navigation_dashboard){
                layout_titles.setVisibility(View.VISIBLE);

                txt_title.setText("模拟考试");
               // transaction = manager.beginTransaction();
               // transaction.replace(R.id.nav_host_fragment,fragments.get(1)).commit();
                SimulateSettingFragment simulateSettingFragment = new SimulateSettingFragment();
                simulateSettingFragment.setCancelable(false);

                simulateSettingFragment.show(getSupportFragmentManager(),"Mydialog");

            }
            return true;
        });
    }


    public void setOnTitleChangedListener(onTitleChanged onTitleChangedListener) {
        this.onTitleChangedListener = onTitleChangedListener;
        txt_title.setText(onTitleChangedListener.setTitle());
    }

    onTitleChanged onTitleChangedListener;


    public interface onTitleChanged{

        String setTitle();

    }
}
