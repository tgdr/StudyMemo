package buu.njj.studymemo.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;

import butterknife.BindView;
import butterknife.ButterKnife;
import buu.njj.studymemo.R;
import buu.njj.studymemo.StudyApplication;
import buu.njj.studymemo.bean.UserInfo;
import buu.njj.studymemo.dao.UserInfoDBhelper;

public class RegisterActivity extends AppCompatActivity implements TextWatcher {

    private static final String TAG = "RegisterActivity";
    @BindView(R.id.bt_register_submit)
    Button btn_reg_sugmit;
    @BindView(R.id.et_register_phonenum)
    EditText edit_phone;
    @BindView(R.id.et_register_nick_name)
    EditText edit_nick;
    @BindView(R.id.et_register_password)
    EditText edit_pass;
    StudyApplication app;
    @BindView(R.id.ib_navigation_back)
    ImageView imgback;
    @BindView(R.id.cb_protocol)
    CheckBox cb;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register_step_one);
        app = (StudyApplication) getApplication();
        ButterKnife.bind(this);
        edit_phone.addTextChangedListener(this);
        edit_nick.addTextChangedListener(this);
        edit_pass.addTextChangedListener(this);
        cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                     if(!TextUtils.isEmpty(edit_pass.getText().toString()) && !TextUtils.isEmpty(edit_nick.getText().toString())&& !TextUtils.isEmpty(edit_phone.getText().toString()) &&isChecked){
                         btn_reg_sugmit.setBackgroundResource(R.drawable.bg_login_submit);
                         btn_reg_sugmit.setTextColor(getResources().getColor(R.color.white));
                         btn_reg_sugmit.setClickable(true);
                     }
                     else{
                         btn_reg_sugmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                         btn_reg_sugmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                         btn_reg_sugmit.setClickable(false);
                     }
        });
        btn_reg_sugmit.setOnClickListener(v -> {
            new Thread(() -> {

                if(!TextUtils.isEmpty(edit_phone.getText().toString()) && !TextUtils.isEmpty(edit_nick.getText().toString())&&!TextUtils.isEmpty(edit_pass.getText().toString())){
                    UserInfoDBhelper userInfoDBhelper = new UserInfoDBhelper(new UserInfo(edit_phone.getText().toString(),edit_nick.getText().toString(),edit_pass.getText().toString()));
                    try {
                        int i=userInfoDBhelper.regUser();
                        if(i>=0){
                            EventBus.getDefault().post("注册成功，请您使用手机号进行登录！");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        EventBus.getDefault().post("注册失败！该手机号已经被注册过，如果忘记密码请联系管理员进行修改");

                    }
                }
                else
                EventBus.getDefault().post("输入状态非法，请检查您的输入，手机号，姓名和密码不能为空");
            }).start();

        });

        imgback.setOnClickListener(v -> {
            onBackPressed();
        });
    }



    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void getdata(String userInfo){
        Toast.makeText(RegisterActivity.this,userInfo,Toast.LENGTH_LONG).show();
        if(userInfo.equals("注册成功，请您使用手机号进行登录！")){
            app.setUserName(edit_phone.getText().toString());
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
        if (!TextUtils.isEmpty(edit_pass.getText().toString()) && !TextUtils.isEmpty(edit_nick.getText().toString())&& !TextUtils.isEmpty(edit_phone.getText().toString()) &&cb.isChecked()) {
            btn_reg_sugmit.setBackgroundResource(R.drawable.bg_login_submit);
            btn_reg_sugmit.setTextColor(getResources().getColor(R.color.white));
            btn_reg_sugmit.setClickable(true);
        } else {
            btn_reg_sugmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
            btn_reg_sugmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
            btn_reg_sugmit.setClickable(false);
        }
        if(edit_nick.getText().toString().length() ==3){
            hideKeyboard(RegisterActivity.this);

        }
    }
    public static void hideKeyboard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), 0);
    }
}
