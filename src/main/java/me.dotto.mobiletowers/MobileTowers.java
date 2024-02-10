package me.dotto.mobiletowers;

import me.dotto.mobiletowers.Commands.AddTower;
import me.dotto.mobiletowers.Commands.TowerCmd;
import me.dotto.mobiletowers.Commands.Towers;
import me.dotto.mobiletowers.Types.Tower;
import me.dotto.mobiletowers.Types.User;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.economy.Economy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MobileTowers extends JavaPlugin {
    Database db;
    FileConfiguration config = getConfig();
    MobileListener listener;
    Economy economyProvider;
    List<Tower> towers = new ArrayList<>();
    List<User> users = new ArrayList<>();

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        getLogger().info("MobileTowers has been loaded! [1.20.2]");

        db = new Database(config, getLogger());

        try {
            PreparedStatement statement = Database.GetConnection().prepareStatement("SELECT * FROM MobileTowers;");
            ResultSet results = statement.executeQuery();

            while(results.next()) {
                Tower tower = new Tower(results.getString("id"), results.getInt("x"), results.getInt("y"), results.getInt("z"), results.getString("type"), results.getString("world"), results.getInt("trange"));
                towers.add(tower);
                getLogger().info("Adding tower " + results.getString("id"));
            }
        } catch (SQLException e) {
            getServer().getPluginManager().disablePlugin(this);
        }

        listener = new MobileListener(config, this, towers, users);

        RegisteredServiceProvider<Economy> Rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if(Rsp != null) economyProvider = Rsp.getProvider();

        getServer().getPluginManager().registerEvents(listener, this);

        this.getCommand("addtower").setExecutor(new AddTower(listener));
        this.getCommand("tower").setExecutor(new TowerCmd(listener));
        this.getCommand("towers").setExecutor(new Towers(listener));

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            getLogger().info("Just hooked into PlaceholderAPI!");
            new PlaceholderAPI(this, listener).register();
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("MobileTowers has been unloaded [1.20.2]!");
        db.Disconnect();
    }

    public Economy GetEcoProvider() {
        return economyProvider;
    }
}