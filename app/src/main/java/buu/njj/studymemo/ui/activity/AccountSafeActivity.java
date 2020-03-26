package buu.njj.studymemo.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import buu.njj.studymemo.R;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class AccountSafeActivity extends Activity {

    @BindView(R.id.img_mbl)
    ImageView img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_account_safe);
        ButterKnife.bind(this);
        Glide.with(AccountSafeActivity.this).load(R.mipmap.tx_back).bitmapTransform(new BlurTransformation(AccountSafeActivity.this, 25), new CenterCrop(AccountSafeActivity.this)).into(img);

    }

    @OnClick(R.id.img_mbl)
    public void imgclick(View v){
        this.finish();
        overridePendingTransition(R.anim.in_anim,R.anim.out_anim);
    }
}
