package buu.njj.studymemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import buu.njj.studymemo.R;
import buu.njj.studymemo.bean.Chapter_question;
import buu.njj.studymemo.bean.Chapter_question_item;

public class MainUserChapterAdapter extends BaseExpandableListAdapter {

    private List<Chapter_question> mDatas;
    private Context ct;

    public MainUserChapterAdapter(List<Chapter_question> mDatas, Context ct) {
        this.mDatas = mDatas;
        this.ct = ct;
    }

    @Override
    public int getGroupCount() {
        return mDatas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mDatas.get(groupPosition).getChildren().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mDatas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mDatas.get(groupPosition).getChildren().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        ParentViewHolder parentViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(ct).inflate(R.layout.item_parent_chapter, parent, false);
            parentViewHolder = new ParentViewHolder(convertView);
            convertView.setTag(parentViewHolder);
        } else {
            parentViewHolder = (ParentViewHolder) convertView.getTag();
        }
        Chapter_question chapter_question = mDatas.get(groupPosition);
        parentViewHolder.tvChapterName.setText("第" + chapter_question.get_id() + "章");
        parentViewHolder.imgindicator.setSelected(isExpanded);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(ct).inflate(R.layout.item_child_chapter, parent, false);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        Chapter_question_item chapter_question_item = mDatas.get(groupPosition).getChildren().get(childPosition);
        childViewHolder.tvChapterItem.setText(chapter_question_item.getName() + "");
        childViewHolder.tv_score.setText(chapter_question_item.getScore()+"分");
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class ParentViewHolder {
        @BindView(R.id.tv_chapter_name)
        TextView tvChapterName;
        @BindView(R.id.id_iv_indicator)
        ImageView imgindicator;

        ParentViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildViewHolder {
        @BindView(R.id.tv_chapter_item)
        TextView tvChapterItem;

        @BindView(R.id.tv_chapter_score)
        TextView tv_score;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
