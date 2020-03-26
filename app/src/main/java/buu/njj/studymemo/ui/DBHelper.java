package buu.njj.studymemo.ui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHelper {
    static Connection connection = null;
    public static Connection getSQLConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection
                    ( "jdbc:mysql://www.chunheculture.com:3306/mrnaquestion", "root", "091920gwy" );
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            return  connection;
        }
    }



    public DBHelper() {

    }


    public ResultSet getdata() throws SQLException {
        String sqlstatment = "Select * from jiandati;";
        Statement statement = getSQLConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(sqlstatment);
return  resultSet;
    }


    public ResultSet getdatabyuuid(String _uuid) throws SQLException {
        String sqlstatment = "Select * from jiandati where _uuid='"+_uuid+"'";
        Statement statement = getSQLConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(sqlstatment);
        return  resultSet;
    }

    public int updatescore(String _uuid,double score) throws SQLException {
        int t=-1;
        String sqlstatment = "update jiandati set status =" +score+
                " where _uuid='"+_uuid+"';";
        Statement statement = getSQLConnection().createStatement();

        t = statement.executeUpdate(sqlstatment);
        statement.close();
        return  t;
    }







    /**
     * 下面是用户登录相关的数据操作
     *
     * **/

}
