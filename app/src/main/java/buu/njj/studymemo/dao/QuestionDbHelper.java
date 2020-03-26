package buu.njj.studymemo.dao;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import buu.njj.studymemo.bean.Chapter_question;
import buu.njj.studymemo.bean.Chapter_question_item;

public class QuestionDbHelper {

    private static final String TAG = "QuestionDbHelper";
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

    public static List<Chapter_question>  getDatas(String tableName,String queType) throws SQLException {
        List<Chapter_question> chapter_questions = new ArrayList<>();
        int maxchapter =getMaxChapter(tableName);
            for(int k=1;k<=maxchapter;k++){
                Chapter_question chapter_question = new Chapter_question(k,queType);
                List<Chapter_question_item> itemList = getChapterQuestionListByType(tableName,queType,k);
                for(int j=0;j<itemList.size();j++){
                    chapter_question.addChild(itemList.get(j));
                }
                chapter_questions.add(chapter_question);
            }

        return chapter_questions;
    }

    public static int getMaxChapter(String tableName) throws SQLException {
        int maxchapter = -1;
        String sqlstatment = "select MAX(chapter) as MaxChapter from " +
                tableName+";";
        Statement statement = getSQLConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(sqlstatment);
        List<Chapter_question_item> itemList = new ArrayList<>();
        int i = 0;
        if (resultSet.next()) {
            maxchapter = resultSet.getInt("MaxChapter");
            Log.e(TAG, "getMaxChapter: "+maxchapter );
        }
        return maxchapter;

    }

    public static List getChapterQuestionListByType(String tableName,String quetype,int chapter) throws SQLException {
        Log.e(TAG, "getChapterQuestionListByType: "+ quetype+", "+chapter);
        String sqlstatment = "select question,_uuid,status from " +
                tableName+" where que_type ='" +
                quetype+"' and chapter=" +
                chapter+";";
        Statement statement = getSQLConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(sqlstatment);
        List<Chapter_question_item> itemList = new ArrayList<>();
        int i = 0;
        while (resultSet.next()) {
            itemList.add(new Chapter_question_item(i++, resultSet.getString("question"),resultSet.getString("_uuid"),resultSet.getFloat("status")));
        }
        return itemList;
    }
}
