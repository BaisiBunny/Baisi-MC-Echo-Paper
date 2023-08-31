package tech.baisi.mc.echo.paper;

import org.bukkit.Bukkit;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MySQLManager {
    // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://e5.baisi.tech:23306/";


    // 数据库的用户名与密码，需要根据自己的设置
    private static final String USER = "root";
    private static final String PASS = "BaisiTech";
    private static final String GET_PLAYERS_SQL = "SELECT name, game_key FROM mc.`user`";

    private Connection connection;
    private Statement statement;

    public void init(){
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL,USER,PASS);
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String,String> getPlayerKeys(){
        Map<String,String> toReturn = new HashMap<>();
        try {
            ResultSet resultSet = statement.executeQuery(GET_PLAYERS_SQL);
            while (resultSet.next()){
                toReturn.put(resultSet.getString("name"),resultSet.getString("game_key"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return toReturn;
    }

    public void addMoney(String name, int add_money){
        try {
            ResultSet resultSet = statement.executeQuery("SELECT money FROM mc.`user` WHERE name='"+name+"'");
            int old_money = 0;
            while (resultSet.next()){
                old_money = resultSet.getInt("money");
            }
            int new_money = old_money + add_money;
            statement.executeUpdate("UPDATE mc.`user` SET money="+new_money+" WHERE name='"+name+"';");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
