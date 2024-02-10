package me.dotto.mobiletowers.Commands;

import me.dotto.mobiletowers.MobileListener;
import me.dotto.mobiletowers.Types.Tower;
import me.dotto.mobiletowers.Types.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Towers implements CommandExecutor {
    public Towers(MobileListener l) {
        listener = l;
    }

    MobileListener listener;

    @Override
    public boolean onCommand(CommandSender Sender, Command Cmd, String Label, String[] Args) {
        if(Sender instanceof Player player) {

            List<User> users = listener.GetUsers();

            for(User u : users) {
                if(u.username.equals(player.getName())) {
                    List<Tower> towers = listener.GetAvailableTowers(u);

                    if(towers.isEmpty()) {
                        player.sendMessage("No towers are currently available!");
                    } else {
                        player.sendMessage("Available towers:");
                        for(Tower t : towers) {
                            player.sendMessage("ID: " + t.id + "(" + t.type + "), Signal Score: " + t.score.getAsInt() + ", x: " + t.x + ", y: " + t.y + ", z: " + t.z + ".");
                        }
                    }
                }
            }
        }

        return true;
    }
}
