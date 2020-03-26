package buu.njj.studymemo.bean;

import java.io.Serializable;

public class CardData implements Serializable {

    String _id;
    String question;
    String answer;
    String status;

    public String get_uuid() {
        return _uuid;
    }

    public void set_uuid(String _uuid) {
        this._uuid = _uuid;
    }

    String _uuid;

    public CardData(String _id, String question, String answer, String status, String chapter, String que_type,String uuid) {
        this._id = _id;
        this.question = question;
        this.answer = answer;
        this.status = status;
        this.chapter = chapter;
        this.que_type = que_type;
        this._uuid= uuid;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getQue_type() {
        return que_type;
    }

    public void setQue_type(String que_type) {
        this.que_type = que_type;
    }

    String chapter;
    String que_type;


}
