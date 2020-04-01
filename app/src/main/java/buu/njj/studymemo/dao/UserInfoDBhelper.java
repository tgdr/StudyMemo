package buu.njj.studymemo.dao;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import buu.njj.studymemo.bean.GridSubject;
import buu.njj.studymemo.bean.UserInfo;

public class UserInfoDBhelper {
    private static final String TAG = "UserInfoDBhelper";
    static Connection connection = null;
    public static Connection getSQLConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection
                    ( "jdbc:mysql://www.chunheculture.com:3306/mrnaquestion?useUnicode=true&characterEncoding=utf-8", "root", "091920gwy" );
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            return  connection;
        }
    }
    /**
     * 这是获取/注册用户信息的数据库帮助类
     */
    private UserInfo userInfo;
    public UserInfoDBhelper(){}


    public UserInfoDBhelper(UserInfo userInfo){
        this.userInfo = userInfo;
    }

    public int regUser() throws SQLException {
        int i=-1;
        String sqlstatment ="INSERT INTO user_info (phoneNum,nickname,upassWord) VALUES ('" +
                userInfo.getPhoneNum()+"','" +
                userInfo.getNickName()+"','" +
                userInfo.getPassWord()+"');";
        Statement statement = getSQLConnection().createStatement();
        i=statement.executeUpdate(sqlstatment);
        return i;
    }

    public static boolean login(String un,String pass) throws SQLException {
        String sqlstatment ="select upassWord from user_info where phoneNum='" +
                un+"'";
        Statement statement = getSQLConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(sqlstatment);
        if(resultSet.next()){
           String getpass = resultSet.getString("upassWord");
           if(getpass.equals(pass)){
               return true;
           }
        }
            return false;

    }


    public static List<GridSubject> getUserStudyType(String phoneNum) throws SQLException {
        String sqlstatment ="select  study_type.type_id,study_type.type_msg,study_type.type_img,study_type.type_table from user_study_type,study_type where user_study_type.phoneNum='" +phoneNum+
                "' and user_study_type.type_id = study_type.type_id;";
        Statement statement = getSQLConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(sqlstatment);
        List<GridSubject> data = new ArrayList();
        while (resultSet.next()){
           data.add(new GridSubject(resultSet.getInt("type_id"),resultSet.getString("type_img"),resultSet.getString("type_msg"),resultSet.getString("type_table")));
        }
        return data;

    }

    public static String getTableNameById(int id) throws SQLException {
        String sqlstatment ="select study_type.type_table from study_type where type_id = " +
                id+";";
        Statement statement = getSQLConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(sqlstatment);
        if(resultSet.next()){
            return resultSet.getString("type_table");
        }
        return null;
    }


    public static List<String> getQuestionType(int id) throws SQLException {
        String tableName = getTableNameById(id);
        if(tableName!=null){
            String sqlstatment ="select DISTINCT(que_type) from " +
                    tableName+";";
            Log.e(TAG, "getQuestionType: "+sqlstatment );
            Statement statement = getSQLConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(sqlstatment);
            List<String> data = new ArrayList();
            while (resultSet.next()){
                data.add(resultSet.getString("que_type"));
                Log.e(TAG, "getQuestionType: "+ resultSet.getString("que_type"));
            }
            return data;
        }
        return null;

    }

    public static float getScoreByuuid(String tableName,String _uuid) throws SQLException {

            String sqlstatment ="select score_table.score from score_table where score_table._uuid='"
                    +_uuid+"';";
            Log.e(TAG, "getQuestionType: "+sqlstatment );
            Statement statement = getSQLConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(sqlstatment);
            List<String> data = new ArrayList();
            if (resultSet.next()){
                data.add(resultSet.getString("que_type"));
                Log.e(TAG, "getQuestionType: "+ resultSet.getString("que_type"));
            }
            return 0.0f;


    }


    public static List<String> getQuestionUUid(int id) throws SQLException {
        String tableName = getTableNameById(id);
        if(tableName!=null){
            String sqlstatment ="select DISTINCT(que_type) from " +
                    tableName+";";
            Log.e(TAG, "getQuestionType: "+sqlstatment );
            Statement statement = getSQLConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(sqlstatment);
            List<String> data = new ArrayList();
            while (resultSet.next()){
                data.add(resultSet.getString("que_type"));
                Log.e(TAG, "getQuestionType: "+ resultSet.getString("que_type"));
            }
            return data;
        }
        return null;

    }




}
