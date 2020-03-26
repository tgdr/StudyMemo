package buu.njj.studymemo.ui.notifications;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import buu.njj.studymemo.R;
import buu.njj.studymemo.fragment.QrCodeDialog;
import buu.njj.studymemo.ui.activity.AccountSafeActivity;
import buu.njj.studymemo.utils.GridShowUtil;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class NotificationsFragment extends Fragment {

    ImageView img, mbl;
    GridView gridView;
    TextView tv_myname;
    List<Map<String, Object>> mydatas;
    @BindView(R.id.user_icon)
    ImageView myqrcode;
    TextView setting;

    TextView setting1;
    private static final String TAG = "NotificationsFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ButterKnife.bind(getActivity());

        return inflater.inflate(R.layout.frag_usercenter, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        img = getView().findViewById(R.id.user_icon);
        setting = getView().findViewById(R.id.me_settings);
        setting1 = getView().findViewById(R.id.me_settings_tv);
        mbl = getView().findViewById(R.id.img_mbl);
        tv_myname = getView().findViewById(R.id.my_name);
        myqrcode = getView().findViewById(R.id.img_qrcode);
        Glide.with(getActivity()).load(R.drawable.test).bitmapTransform(new CropCircleTransformation(getActivity())).into(img);
        Glide.with(getActivity()).load(R.mipmap.tx_back).bitmapTransform(new BlurTransformation(getActivity(), 25), new CenterCrop(getActivity())).into(mbl);
        gridView = getView().findViewById(R.id.gridview);
        initGridDatas();
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        tv_myname.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/wendao.ttf"));
        TextPaint tp = tv_myname.getPaint();
        tp.setFakeBoldText(true);
    }

    private void initGridDatas() {
        mydatas = new ArrayList<>();
        for (int i = 0; i < GridShowUtil.img_src.length; i++) {
            Map<String, Object> data = new HashMap<>();
            data.put("img", GridShowUtil.img_src[i]);
            data.put("txt", GridShowUtil.txt_src[i]);
            mydatas.add(data);
        }
        String[] from = {"img", "txt"};
        int[] to = {R.id.img_grid, R.id.txt_grid};
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), mydatas, R.layout.frag_user_grid, from, to);
        gridView.setAdapter(adapter);

        myqrcode.setOnClickListener(v -> {
            QrCodeDialog qrCodeDialog = new QrCodeDialog(getActivity());
            qrCodeDialog.show();
        });


        setting.setOnClickListener(v -> {

            Intent it = new Intent(getActivity(), AccountSafeActivity.class);

            startActivity(it);
            getActivity().overridePendingTransition(R.anim.in_anim, R.anim.out_anim);
        });
        setting1.setOnClickListener(v -> {
            Intent it = new Intent(getActivity(), AccountSafeActivity.class);
            startActivity(it);
            getActivity().overridePendingTransition(R.anim.in_anim, R.anim.out_anim);
        });
    }
}