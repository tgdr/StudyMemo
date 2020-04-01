package buu.njj.studymemo.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import buu.njj.studymemo.bean.CardData;

public class SimuDBhelper {
    private static final String TAG = "SimuDBhelper";
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


    public static List<CardData> getChapteritembyType (String tableName,String typeName,int timushu) throws SQLException {
        String sqlstatment ="select * from " +
                tableName+" where que_type='" +
                typeName+"' ORDER BY rand() LIMIT " +
                timushu+";";

        Statement statement = getSQLConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(sqlstatment);
        List<CardData> data = new ArrayList();
        while (resultSet.next()){
            CardData cardData = new CardData(resultSet.getInt("_id")+"",resultSet.getString("question"),resultSet.getString("answer"),"0",resultSet.getString("chapter"),typeName,resultSet.getString("_uuid"));
            data.add(cardData);

        }
        return data;
    }
}
