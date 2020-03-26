package buu.njj.studymemo.bean;

public class UserInfo {
    private String phoneNum;
    private String nickName;
    private String passWord;

    public UserInfo(String phoneNum, String nickName, String passWord) {
        this.phoneNum = phoneNum;
        this.nickName = nickName;
        this.passWord = passWord;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }


    @Override
    public String toString() {
        return "UserInfo{" +
                "phoneNum='" + phoneNum + '\'' +
                ", nickName='" + nickName + '\'' +
                ", passWord='" + passWord + '\'' +
                '}';
    }
}
