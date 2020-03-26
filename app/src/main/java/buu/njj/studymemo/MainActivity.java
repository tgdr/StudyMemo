package buu.njj.studymemo;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import buu.njj.studymemo.fragment.MainUserHomeFragment;
import buu.njj.studymemo.fragment.SimulateSettingFragment;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        txt_title.setText("主页");
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
