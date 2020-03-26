package buu.njj.studymemo;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class StudyApplication extends Application {


    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");



    public String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String userId;

    OkHttpClient okHttpClient;

    public SharedPreferences getSpf() {
        return spf;
    }

    public void setSpf(SharedPreferences spf) {
        this.spf = spf;
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public void setEditor(SharedPreferences.Editor editor) {
        this.editor = editor;
    }

    SharedPreferences spf;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();
        spf= PreferenceManager.getDefaultSharedPreferences(this);
        editor = spf.edit();
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
        okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar)
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }
}
