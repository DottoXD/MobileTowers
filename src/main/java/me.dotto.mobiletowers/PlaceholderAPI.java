package me.dotto.mobiletowers;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.dotto.mobiletowers.Types.Tower;
import me.dotto.mobiletowers.Types.User;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlaceholderAPI extends PlaceholderExpansion {
    private final MobileTowers plugin;
    private final MobileListener listener;

    public PlaceholderAPI(MobileTowers Plugin, MobileListener Listener) {
        this.plugin = Plugin;
        this.listener = Listener;
    }

    @Override
    public String getAuthor() {
        return "Dotto";
    }

    @Override
    public String getIdentifier() {
        return "mobiletowers";
    }

    @Override
    public String getVersion() {
        return "0.0.1";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if(params.equalsIgnoreCase("network")) {
            List<User> users = listener.GetUsers();

            for(User u : users) {
                if(u.username.equals(player.getName())) {
                    Tower tower = null;

                    for(Tower t : listener.GetTowers()) {
                        if(String.valueOf(t.id).equals(String.valueOf(u.tower))) tower = t;
                    }

                    if(tower == null) {
                        return "N/A";
                    } else {
                        Location location = player.getLocation();
                        int ratio = tower.trange - (int) Math.round(location.distance(new Location(plugin.getServer().getWorld(tower.world), tower.x, tower.y, tower.z)));
                        String signalString = getString(ratio, tower);

                        return tower.type + " " + signalString;
                    }
                }
            }
        }

        return null;
    }

    @NotNull
    private static String getString(int ratio, Tower tower) {
        String signalString;

        if(ratio > tower.trange / 1.5) {
            signalString = "▁▂▃▅▆▇";
        } else if(ratio > tower.trange / 2) {
            signalString = "▁▂▃▅▆";
        } else if(ratio > tower.trange / 3) {
            signalString = "▁▂▃▅";
        } else if(ratio > tower.trange / 4) {
            signalString = "▁▂▃";
        } else {
            signalString = "▁▂";
        }
        return signalString;
    }
}
