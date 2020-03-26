package buu.njj.studymemo.bean;

public class Chapter_question_item {
    private int _id;
    private String name;
    private String _uuid;
    private int pid;
    private float score;

    public Chapter_question_item(int _id, String name,String _uuid) {
        this._id = _id;
        this.name = name;
        this._uuid = _uuid;
    }

    public Chapter_question_item(int _id, String name,String _uuid,float score) {
        this._id = _id;
        this.name = name;
        this._uuid = _uuid;
        this.score = score;
    }

    public Chapter_question_item() {

    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String get_uuid() {
        return _uuid;
    }

    public void set_uuid(String _uuid) {
        this._uuid = _uuid;
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

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }
}
