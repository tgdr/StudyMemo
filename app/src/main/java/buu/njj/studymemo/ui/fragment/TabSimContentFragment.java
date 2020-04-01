package buu.njj.studymemo.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import buu.njj.studymemo.R;

public class TabSimContentFragment extends Fragment {

    String _uuid;
String answer;
@BindView(R.id.tv_card_que_num)
    TextView tv_num;
@BindView(R.id.tv_card_que_title)
TextView tv_quetitle;

@BindView(R.id.tv_card_chapter)
TextView tv_chapter;
@BindView(R.id.tv_card_quetype)
TextView tv_type;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_simtest_content,null,false);
        ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _uuid = onDataListener._uuid();
        tv_num.setText(onDataListener.setNum());
        tv_quetitle.setText(onDataListener.setQuestionTitle());
        tv_type.setText(onDataListener.setType());
        answer =  onDataListener.setAnswer();
        tv_chapter.setText("第"+onDataListener.setChapter()+"章");

    }

    public void setOnDataListener(TabSimContentFragment.onDataListener onDataListener) {
        this.onDataListener = onDataListener;
    }

    onDataListener onDataListener;

    public interface onDataListener{
        String setNum();
        String setQuestionTitle();
        String setType();
        String setChapter();
        String setAnswer();
        String _uuid();

    }
}
