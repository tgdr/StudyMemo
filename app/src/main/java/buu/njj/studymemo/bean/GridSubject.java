package buu.njj.studymemo.bean;

public class GridSubject {

    private int _id;
    private String imgurl;
    private String msg;
    private String tableName;

    public GridSubject(String imgurl, String msg) {
        this.imgurl = imgurl;
        this.msg = msg;
    }

    public GridSubject(int _id, String imgurl, String msg) {
        this._id = _id;
        this.imgurl = imgurl;
        this.msg = msg;

    }
    public GridSubject(int _id, String imgurl, String msg,String tableName) {
        this._id = _id;
        this.imgurl = imgurl;
        this.msg = msg;
        this.tableName = tableName;

    }


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "GridSubject{" +
                "imgurl='" + imgurl + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
