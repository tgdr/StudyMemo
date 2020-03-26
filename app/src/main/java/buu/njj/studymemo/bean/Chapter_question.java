package buu.njj.studymemo.bean;

import java.util.ArrayList;
import java.util.List;

public class Chapter_question {
    private int _id;
    private String name;
    private List<Chapter_question_item> children = new ArrayList<>();

    public Chapter_question(int _id, String name) {
        this._id = _id;
        this.name = name;
    }

    public Chapter_question(){

    }

    public void addChild(Chapter_question_item item){
        item.setPid(get_id());
        children.add(item);
    }

    public void addChild(int cid,String cname,String _uuid,float score){
        Chapter_question_item chapter_question_item = new Chapter_question_item(cid,cname,_uuid,score);
        chapter_question_item.setPid(get_id());
        children.add(chapter_question_item);
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Chapter_question_item> getChildren() {
        return children;
    }

    public void setChildren(List<Chapter_question_item> children) {
        this.children = children;
    }
}
