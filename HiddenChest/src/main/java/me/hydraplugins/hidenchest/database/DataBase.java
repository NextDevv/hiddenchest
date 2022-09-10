package me.hydraplugins.hidenchest.database;

import me.hydraplugins.hidenchest.HidenChest;
import org.bukkit.configuration.Configuration;

import java.sql.*;
import java.util.Date;

public class DataBase {
    private static Connection connection;
    static String prefix = "[HidenChest] (DataBase) ";

    static HidenChest plugin = HidenChest.getPlugin(HidenChest.class);
    static Configuration config = plugin.getConfig();
    public static Connection getConnection() {
        if(connection != null) {
            return connection;
        }
        String host = plugin.getConfig().getString("host");
        String name = plugin.getConfig().getString("name");
        String username = plugin.getConfig().getString("user");
        String password = plugin.getConfig().getString("password");

        String url = "jdbc:mysql://"+host+"/"+name+"?characterEncoding=utf8";

        try {
            connection = DriverManager.getConnection(url, username, password);

            System.out.println(prefix+"Connected to the database");


        } catch (SQLException e) {
            System.out.println(prefix+"Failed to connect to the database: " + e.getMessage());
        }

        return connection;
    }

    public static void InitilizeDatabase() {
        try {
            Statement statement = getConnection().createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS player_data(Id int, Name text,World text, locX double, locY double, locZ double);";
            statement.execute(sql);

            System.out.println(prefix+"Successfully created the table in the database");
            statement.close();
        } catch (SQLException e) {
            System.out.println(prefix+"Failed to create the table in the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static PlayerData getPlayerFromUUID(int ID) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM player_data WHERE Id = ?");
            statement.setInt(1, ID);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String name = rs.getString("Name");
                String world = rs.getString("World");
                double x = rs.getDouble("locX");
                double y = rs.getDouble("locY");
                double z = rs.getDouble("locZ");
                int id = rs.getInt("Id");

                PlayerData playerData = new PlayerData(id, name,world, x,y,z);

                statement.close();

                return playerData;
            }

        } catch (SQLException e) {
            System.out.println(prefix+"Unable to get player data: " + e.getMessage());
            System.out.println(prefix+"At: " + e.getErrorCode());
            e.printStackTrace();
        }

        return null;
    }

    public void createPlayerDatabase(PlayerData d) {
        try {
            PreparedStatement statement = getConnection()
                    .prepareStatement("INSERT INTO player_data(Id, Name, World, LocX, LocY, LocZ) VALUES(?,?,?,?,?,?)");
            statement.setInt(1, d.getId());
            statement.setString(2, d.getName());
            statement.setString(3, d.getWorld());
            statement.setDouble(4, d.getLocX());
            statement.setDouble(5, d.getLocY());
            statement.setDouble(6, d.getLocZ());

            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            System.out.println(prefix+"Unable to create player database: " + e.getMessage());
            System.out.println(prefix+"At: " + e.getErrorCode());
            e.printStackTrace();
        }

    }

    public boolean getChestID(int ID) {
        try{
            PreparedStatement statement = getConnection()
                    .prepareStatement("SELECT * FROM player_data WHERE Id = ?");
            statement.setInt(1, ID);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {

                statement.close();

                return true;
            }

        }catch (SQLException e) {}

        return false;
    }

    public void updatePlayerDatabase(PlayerData d) {
        try {
            PreparedStatement statement = getConnection()
                    .prepareStatement("UPDATE player_data SET Id = ? Name = ?, World = ?, LocX = ?, LocY = ?, LocZ = ? WHERE uuid = ?");
            statement.setInt(7, d.getId());
            statement.setString(1, d.getName());
            statement.setString(2, d.getWorld());
            statement.setDouble(3, d.getLocX());
            statement.setDouble(4, d.getLocY());
            statement.setDouble(5, d.getLocZ());

            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            System.out.println(prefix+"Unable to create player database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deletePlayerDatabase(PlayerData d) {
        try {
            PreparedStatement statement = getConnection()
                    .prepareStatement("DELETE FROM player_data WHERE Id = ?");
            statement.setInt(1, d.getId());

            statement.executeUpdate();

            statement.close();
        }catch (SQLException e) {
            System.out.println(prefix+"Unable to delete player database: " + e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            getConnection().close();
        }catch (SQLException e) {
            System.out.println(prefix+"Unable to close connection: " + e.getMessage());
        }
    }
}
