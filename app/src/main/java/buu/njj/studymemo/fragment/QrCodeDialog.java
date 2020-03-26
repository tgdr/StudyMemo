package buu.njj.studymemo.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import buu.njj.studymemo.R;
import buu.njj.studymemo.utils.QrCodeUtils;

public class QrCodeDialog extends Dialog {

    Context ct;
    ImageView img;
    public QrCodeDialog(Context ct){
        super(ct);
        this.ct = ct;
    }
    private static final String TAG = "QrCodeDialog";
    public QrCodeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected QrCodeDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_fragment_qrcode);
        setTitle("扫二维码加好友");
        img = findViewById(R.id.img_qr);
        img.setImageBitmap(QrCodeUtils.generateBitmap("刘嘉怡",600,600));

    }


    @Override
    public void show() {
        super.show();
    }
}
