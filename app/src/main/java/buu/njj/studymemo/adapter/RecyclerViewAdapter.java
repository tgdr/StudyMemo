package buu.njj.studymemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import buu.njj.studymemo.R;
import buu.njj.studymemo.bean.CardData;
import buu.njj.studymemo.defui.SelectableRoundedImageView;
import buu.njj.studymemo.ui.DoAnswerActivity;

/**
 * Created by lty on 2019-09-06.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CardViewHolder> {
    private Context context;
    Map piliangdata;
    private ArrayList<CardData> datalist;
    View view;
    ArrayList marjor, minor;
    Handler handler, handlerphoto;

    public RecyclerViewAdapter() {


    }



    /*
     * 带参构造函数，传入上下文和需要绑定的数据集合
     * */
    public RecyclerViewAdapter(Context cx, ArrayList datalist) {
        this.context = cx;
        this.datalist = datalist;


    }


    /*
     *创建ViewHolder，持有布局映射
     * */
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //通过布局加载器获取到CardView布局view
        view = LayoutInflater.from(context).inflate(R.layout.fragment_cardview_content, parent, false);
        //通过获取到的布局view实例化一个自己实现的CardViewHolder
        CardViewHolder cardViewHolder = new CardViewHolder(view);
        //返回一个已绑定布局的viewHolder，避免重复findViewById()


        return cardViewHolder;
    }


    /*
     *onBindViewHolder方法做药作用就是将数据集绑定到布局view，以及添加一些事件点击监听
     * */
    @Override
    public void onBindViewHolder(final RecyclerViewAdapter.CardViewHolder holder, final int position) {

        final int pos = position;


        holder.tv_quechapter.setText(datalist.get(position).getChapter());
        holder.tv_quetype.setText(datalist.get(position).getQue_type());

        if (datalist.get(position).getStatus().equals("0")) {
            // holder.cardView.setCardBackgroundColor(Color.argb(12, 160, 50, 0));
            holder.cardImage.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.cardImage.setImageResource(R.mipmap.icon_wait);
        }
        else{
            if(Float.valueOf(datalist.get(position).getStatus())>=95.0f){
                holder.cardImage.setScaleType(ImageView.ScaleType.FIT_XY);
                holder.cardImage.setImageResource(R.mipmap.icon_corrert);
            }
            else{
                holder.cardImage.setScaleType(ImageView.ScaleType.FIT_XY);
                holder.cardImage.setImageResource(R.mipmap.icon_error);
            }
        }
//        } else {
//            holder.cardView.setCardBackgroundColor(Color.argb(12, 160, 160, 0));
//         //   holder.cardImage.setImageResource(R.mipmap.icon_wait);
//        }

        holder.tv_questatus.setText(datalist.get(position).getStatus()+"");
        holder.tv_quetitle.setText(datalist.get(position).getQuestion());
        holder.tv_quenum.setText(datalist.get(position).get_id()+"");
        //  Log.e("drtdrgtdfgcfgd",datalist.get(position).getCourseposition());
//        holder.tvcourseposition.setText(datalist.get(position).getCourseposition());
//        holder.tvcourseteacher.setText(datalist.get(position).getTeachername());

        //给整个cardview添加点击事件
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You click card" + pos, Toast.LENGTH_SHORT).show();
                Intent it = new Intent(context,DoAnswerActivity.class);
                Bundle putdata = new Bundle();
                putdata.putString("tv_quetitle",holder.tv_quetitle.getText().toString());
                putdata.putString("tv_quenum", holder.tv_quenum.getText().toString());
                putdata.putString("tv_quetype",holder.tv_quetype.getText().toString());
                putdata.putString("tv_quechapter",holder.tv_quechapter.getText().toString());
                putdata.putString("_uuid",datalist.get(position).get_uuid());
                putdata.putString("answersheet",datalist.get(position).getAnswer());
                it.putExtra("senddata",putdata);
                context.startActivity(it);
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (holder.piliang.isChecked() == true) {
                    holder.piliang.setVisibility(View.INVISIBLE);
                    holder.piliang.setChecked(false);
                    piliangdata.put(position + "", "false");
                } else {
                    holder.piliang.setVisibility(View.VISIBLE);
                    holder.piliang.setChecked(true);
                    piliangdata.put(position + "", "true");
                }


                return true;
            }
        });


//        //设置不喜欢的按钮点击监听事件
//        holder.disLikeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "你为啥不喜欢我呢？", Toast.LENGTH_SHORT).show();
//            }
//        });
//        //设置喜欢的按钮点击监听事件
//        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "我就知道你喜欢我！", Toast.LENGTH_SHORT).show();
//            }
//        });
    }





    /*
     * 返回recyclerview数据项的个数
     * */
    @Override
    public int getItemCount() {
        return datalist.size();
    }


    /*
     * 自定义实现viewholder类
     * */
    class CardViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        CheckBox piliang;
        SelectableRoundedImageView cardImage;
        TextView tv_quenum,tv_questatus,tv_quetype,tv_quetitle,tv_quechapter;
        //  Button likeBtn, disLikeBtn;


        public CardViewHolder(View itemView) {
            super(itemView);
            piliangdata = new HashMap();
         //   piliang = itemView.findViewById(R.id.checkpiliang);
            cardView = itemView.findViewById(R.id.cv1);
            cardImage = itemView.findViewById(R.id.img_card_status);
            tv_quetitle = itemView.findViewById(R.id.tv_card_que_title);
            tv_quenum = itemView.findViewById(R.id.tv_card_que_num);
            tv_questatus = itemView.findViewById(R.id.tv_card_questatus);
            tv_quetype = itemView.findViewById(R.id.tv_card_quetype);
           // tv_quechapter = itemView.findViewById(R.id.tv_card_chapter);
        }
    }

}