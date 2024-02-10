package me.dotto.mobiletowers;

import me.dotto.mobiletowers.Types.Tower;
import me.dotto.mobiletowers.Types.User;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.logging.Logger;

public class MobileListener implements Listener {
    FileConfiguration config;
    MobileTowers plugin;
    List<Tower> towers;
    List<User> users;

    public MobileListener(FileConfiguration cfg, MobileTowers pl, List<Tower> t, List<User> u) {
        config = cfg;
        plugin = pl;
        towers = t;
        users = u;

        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for(User u1 : users) {
                Tower tower = null;
                for(Tower t1 : towers) {
                    if(u1.tower.equals(t1.id)) tower = t1;
                }

                Location location = plugin.getServer().getPlayer(u1.username).getLocation();

                if(plugin.getConfig().getBoolean("tower.forcechange") || (u1.tower.isEmpty() || (int) Math.round(location.distance(new Location(plugin.getServer().getWorld(tower.world), tower.x, tower.y, tower.z))) > tower.trange)) {
                    Tower bestTower = null;
                    List<Tower> availableTowers = new ArrayList<>();

                    for(Tower t1 : towers) {
                        if(location.getWorld().getName().equals(t1.world)) {
                            int distance = (int) Math.round(location.distance(new Location(plugin.getServer().getWorld(t1.world), t1.x, t1.y, t1.z)));
                            if (distance < t1.trange) availableTowers.add(t1);
                        }
                    }

                    int defScore = 0;
                    int score = 0;
                    for(Tower t1 : availableTowers) {
                        int distance = (int) Math.round(location.distance(new Location(plugin.getServer().getWorld(t1.world), t1.x, t1.y, t1.z)));

                        score = switch (t1.type) {
                            case "2G" -> {
                                score = score + plugin.getConfig().getInt("scoreboosts.2g");
                                yield score + ((t1.trange / 2) - distance);
                            }
                            case "3G" -> {
                                score = score + plugin.getConfig().getInt("scoreboosts.3g");
                                yield score + (int) Math.round((t1.trange / 1.5) - (distance / 1.1));
                            }
                            case "4G" -> {
                                score = score + plugin.getConfig().getInt("scoreboosts.4g");
                                yield score + (int) Math.round(t1.trange - (distance / 1.3));
                            }
                            case "5G" -> {
                                score = score + plugin.getConfig().getInt("scoreboosts.5g");
                                yield score + (int) Math.round(t1.trange - (distance / 1.9));
                            }
                            default -> score;
                        };

                        if(score < 0) score = 0;

                        if(score >= defScore) {
                            defScore = score;
                            bestTower = t1;
                            bestTower.score = OptionalInt.of(score);
                        }

                        score = 0;
                    }

                    if(bestTower == null) {
                        u1.tower = "";
                    } else {
                        try {
                            plugin.GetEcoProvider().depositPlayer(Bukkit.getPlayer(u1.username), (double) (bestTower.score.getAsInt() * Integer.parseInt(bestTower.type.replace("G", ""))) / 2500);
                        } catch(Exception ignored) {}
                        u1.tower = bestTower.id;
                    }

                    users.set(users.indexOf(u1), u1);
                }
            }
        }, 20L, 100L);
    }

    public void SetTowers(List<Tower> t) {
        towers = t;
    }

    public List<Tower> GetTowers() {
        return towers;
    }

    public List<User> GetUsers() {
        return users;
    }

    public List<Tower> GetAvailableTowers(User user) {
        Location location = plugin.getServer().getPlayer(user.username).getLocation();

        List<Tower> availableTowers = new ArrayList<>();

        for(Tower t : towers) {
            if(location.getWorld().getName().equals(t.world)) {
                int distance = (int) Math.round(location.distance(new Location(plugin.getServer().getWorld(t.world), t.x, t.y, t.z)));
                if (distance < t.trange) availableTowers.add(t);
            }
        }

        int score = 0;
        for(Tower t : availableTowers) {
            int distance = (int) Math.round(location.distance(new Location(plugin.getServer().getWorld(t.world), t.x, t.y, t.z)));

            score = switch (t.type) {
                case "2G" -> {
                    score = score + plugin.getConfig().getInt("scoreboosts.2g");
                    yield score + ((t.trange / 2) - distance);
                }
                case "3G" -> {
                    score = score + plugin.getConfig().getInt("scoreboosts.3g");
                    yield score + (int) Math.round((t.trange / 1.5) - (distance / 1.1));
                }
                case "4G" -> {
                    score = score + plugin.getConfig().getInt("scoreboosts.4g");
                    yield score + (int) Math.round(t.trange - (distance / 1.3));
                }
                case "5G" -> {
                    score = score + plugin.getConfig().getInt("scoreboosts.5g");
                    yield score + (int) Math.round(t.trange - (distance / 1.9));
                }
                default -> score;
            };

            if(score < 0) score = 0;

            t.score = OptionalInt.of(score);

            score = 0;
        }

        return availableTowers;
    }

    public Logger GetLogger() {
        return plugin.getLogger();
    }

    public MobileTowers GetPlugin() {
        return plugin;
    }

    public Economy GetEco() {
        return plugin.GetEcoProvider();
    }

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent Event) {
        Player player = Event.getPlayer();

        users.add(new User(String.valueOf(player.getUniqueId()), player.getName(), ""));
    }

    @EventHandler
    public void PlayerQuit(PlayerQuitEvent Event) {
        Player player = Event.getPlayer();

        users.removeIf(u -> u.username.equals(player.getName()));
    }
}