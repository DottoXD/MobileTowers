package me.dotto.mobiletowers.Commands;

import me.dotto.mobiletowers.Database;
import me.dotto.mobiletowers.MobileListener;
import me.dotto.mobiletowers.Types.Tower;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AddTower implements CommandExecutor {
    public AddTower(MobileListener l) {
        listener = l;
    }

    List<Tower> towers;
    MobileListener listener;

    @Override
    public boolean onCommand(CommandSender Sender, Command Cmd, String Label, String[] Args) {
        if(Sender instanceof Player player) {

            towers = listener.GetTowers();

            Location location = player.getLocation();

            int range = switch (Args[0]) {
                case "2G" -> listener.GetPlugin().getConfig().getInt("ranges.2g");
                case "3G" -> listener.GetPlugin().getConfig().getInt("ranges.3g");
                case "4G" -> listener.GetPlugin().getConfig().getInt("ranges.4g");
                case "5G" -> listener.GetPlugin().getConfig().getInt("ranges.5g");
                default -> listener.GetPlugin().getConfig().getInt("ranges.invalid");
            };

            Tower tower = new Tower(String.valueOf(towers.size() + 1), (int) Math.round(location.getX()), (int) Math.round(location.getY()), (int) Math.round(location.getZ()), Args[0], player.getWorld().getName(), range);
            towers.add(tower);

            listener.GetLogger().info(String.valueOf(tower));

            listener.SetTowers(towers);

            try {
                PreparedStatement statement = Database.GetConnection().prepareStatement("INSERT INTO MobileTowers(id, x, y, z, type, world, trange) VALUES(?, ?, ?, ?, ?, ?, ?);");
                statement.setString(1, String.valueOf(towers.size()));
                statement.setInt(2, (int) Math.round(location.getX()));
                statement.setInt(3, (int) Math.round(location.getY()));
                statement.setInt(4, (int) Math.round(location.getZ()));
                statement.setString(5, Args[0]);
                statement.setString(6, player.getWorld().getName());
                statement.setInt(7, range);

                statement.executeUpdate();

                player.sendMessage("Tower succesfully added! ID: " + towers.size() + ", Type: " + Args[0] + ".");

                return true;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return false;
    }
}
