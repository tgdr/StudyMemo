package buu.njj.studymemo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import buu.njj.studymemo.R;
import buu.njj.studymemo.bean.GridSubject;

public class MainUserHomeGridAdapter extends BaseAdapter {
    private static final String TAG = "MainUserHomeGridAdapter";
    List<GridSubject> datas;
    Context ct;

    public MainUserHomeGridAdapter(Context ct, List<GridSubject> datas) {
        this.ct = ct;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView ==null){
            convertView = LayoutInflater.from(ct).inflate(R.layout.activity_main_user_home_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if(position<datas.size()){
            Log.e(TAG, "datasize: "+position+"" );
            try{
                Glide.with(ct).load(datas.get(position).getImgurl()).into(viewHolder.imgGridItem);
                viewHolder.txtGridItem.setText(datas.get(position).getMsg()+"");
            }
            catch (Exception e){

            }

        }
        else{
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(viewHolder.imgGridItem.getLayoutParams());
            lp.setMargins(100, 30, 0, 0);
            viewHolder.imgGridItem.setLayoutParams(lp);
            viewHolder.imgGridItem.setScaleType(ImageView.ScaleType.FIT_XY);
            viewHolder.imgGridItem.setImageResource(R.mipmap.icon_add);
            viewHolder.txtGridItem.setVisibility(View.GONE);
        }
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.img_grid_item)
        ImageView imgGridItem;
        @BindView(R.id.txt_grid_item)
        TextView txtGridItem;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
