package me.dotto.mobiletowers;

import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Database {
    static Connection connection;

    public Database(FileConfiguration Config, Logger GetLogger) {
        try {

            connection = DriverManager.getConnection("jdbc:mysql://" + Config.getString("database.host") + ":" + Config.getString("database.port") + "/" + Config.getString("database.database"), Config.getString("database.username"), Config.getString("database.password"));
            GetLogger.info("Successfully connected to your MySQL database!");
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS MobileTowers(id varchar(4), x int, y int, z int, type varchar(2), world varchar(20), trange int);");
            statement.executeUpdate();
        } catch(SQLException error) {
            GetLogger.info("There was an error while connecting to MySQL!");
        }
    }

    void Disconnect() {
        try {
            if (connection!=null && !connection.isClosed()) {
                connection.close();
            }
        } catch(Exception ignored) {}
    }

    public static Connection GetConnection() {
        return connection;
    }
}
