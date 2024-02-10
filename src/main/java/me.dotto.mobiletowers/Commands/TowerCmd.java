package me.dotto.mobiletowers.Commands;

import me.dotto.mobiletowers.MobileListener;
import me.dotto.mobiletowers.Types.Tower;
import me.dotto.mobiletowers.Types.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TowerCmd implements CommandExecutor {
    public TowerCmd(MobileListener l) {
        listener = l;
    }

    MobileListener listener;

    @Override
    public boolean onCommand(CommandSender Sender, Command Cmd, String Label, String[] Args) {
        if(Sender instanceof Player player) {

            List<User> users = listener.GetUsers();
            List<Tower> towers = listener.GetTowers();

            for(User u : users) {
                if(u.username.equals(player.getName())) {
                    Tower tower = null;

                    for(Tower t : towers) {
                        if(t.id.equals(u.tower)) tower = t;
                    }

                    if(tower == null) {
                        player.sendMessage("You're currently disconnected.");
                    } else {
                        player.sendMessage("You're connected! Tower ID: " + tower.id + ", Type: " + tower.type + ".");
                    }
                }
            }
        }

        return true;
    }
}
