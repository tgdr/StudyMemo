package buu.njj.studymemo.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;

import butterknife.BindView;
import butterknife.ButterKnife;
import buu.njj.studymemo.MainActivity;
import buu.njj.studymemo.R;
import buu.njj.studymemo.StudyApplication;
import buu.njj.studymemo.dao.UserInfoDBhelper;

public class LoginActivity extends AppCompatActivity implements TextWatcher {

    StudyApplication app;
    @BindView(R.id.bt_login_submit)
    Button btn_login;
    @BindView(R.id.bt_login_register)
    Button btn_reg;
    @BindView(R.id.et_login_username)
    EditText editusername;
    @BindView(R.id.et_login_pwd)
    EditText editpassword;
    @BindView(R.id.cb_remember_login)
    CheckBox cbauto;
    SharedPreferences spf;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (StudyApplication) getApplication();
        spf = app.getSpf();
        if(spf.getString("username",null) !=null && spf.getString("password",null)!=null &&!spf.getString("username","").equals("")&&!spf.getString("password","").equals("")&&spf.getString("AUTOLOGIN","").equals("true")) {
            app.setUserName(spf.getString("username",""));
            Toast.makeText(app, "欢迎回来", Toast.LENGTH_SHORT).show();
            Intent it = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(it);
            finish();
        }
        setContentView(R.layout.activity_main_login);

        ButterKnife.bind(this);
        editusername.addTextChangedListener(this);
        editpassword.addTextChangedListener(this);
        btn_reg.setOnClickListener(v -> {
            Intent  it = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(it);
        });
        btn_login.setOnClickListener(v -> {
            if(!TextUtils.isEmpty(editusername.getText().toString()) && !TextUtils.isEmpty(editpassword.getText().toString())){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            boolean b=UserInfoDBhelper.login(editusername.getText().toString(),editpassword.getText().toString());
                            EventBus.getDefault().post(b);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            else{
                Toast.makeText(app, "请您填写好登录信息再去登录", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void getReturnData(Boolean b){
        if(b){
            if (cbauto.isChecked() == true) {
                app.getEditor().putString("AUTOLOGIN", "true").putString("username", editusername.getText().toString()).putString("password", editpassword.getText().toString()).commit();

            } else {
                app.getEditor().putString("AUTOLOGIN", "true").commit();
            }
            Toast.makeText(app, "欢迎回来", Toast.LENGTH_SHORT).show();
            app.setUserName(editusername.getText().toString());
            Intent it = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(it);
            finish();
        }
        else{
            Toast.makeText(app, "登录失败！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(app.getUserName()!=null && !app.getUserName().equals("")){
            editusername.setText(app.getUserName());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
//登录按钮是否可用
        if (!TextUtils.isEmpty(editusername.getText().toString()) && !TextUtils.isEmpty(editpassword.getText().toString())) {
            btn_login.setBackgroundResource(R.drawable.bg_login_submit);
            btn_login.setTextColor(getResources().getColor(R.color.white));
            btn_login.setClickable(true);
        } else {
            btn_login.setBackgroundResource(R.drawable.bg_login_submit_lock);
            btn_login.setTextColor(getResources().getColor(R.color.account_lock_font_color));
            btn_login.setClickable(false);
        }

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
