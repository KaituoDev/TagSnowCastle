package fun.kaituo;


import fun.kaituo.event.PlayerChangeGameEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

import static fun.kaituo.GameUtils.unregisterGame;
import static fun.kaituo.GameUtils.world;

public class Tag3 extends JavaPlugin implements Listener {
    static List<Player> players;
    static long gameTime;
    Scoreboard scoreboard;
    List<String> teamNames = new ArrayList<>(List.of(new String[]{"tag3norden", "tag3cheshirecat", "tag3redhat", "tag3alice",
            "tag3lindamayer", "tag3mabel", "tag3kelti",}));
    List<Team> teams = new ArrayList<>();

    public static Tag3Game getGameInstance() {
        return Tag3Game.getInstance();
    }

    @EventHandler
    public void onButtonClicked(PlayerInteractEvent pie) {
        if (!pie.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if (!pie.getClickedBlock().getType().equals(Material.OAK_BUTTON)) {
            return;
        }
        if (pie.getClickedBlock().getLocation().equals(new Location(world, -1000, 77, 1015))) {
            Tag3Game.getInstance().startGame();
        }
    }

    @EventHandler
    public void setGameTime(PlayerInteractEvent pie) {
        Player player = pie.getPlayer();
        if (pie.getClickedBlock() == null) {
            return;
        }
        Location location = pie.getClickedBlock().getLocation();
        long x = location.getBlockX();
        long y = location.getBlockY();
        long z = location.getBlockZ();
        if (x == -1000 && y == 78 && z == 1015) {
            switch ((int) gameTime) {
                case 8400:
                case 10800:
                case 13200:
                case 15600:
                    gameTime += 2400;
                    break;
                case 18000:
                    gameTime = 8400;
                    break;
                default:
                    break;
            }
            Sign sign = (Sign) pie.getClickedBlock().getState();
            sign.setLine(2, "当前时间为 " + gameTime / 1200 + " 分钟");
            sign.update();
        }
        if (Tag3Game.getInstance().running) {
            return;
        }
        if (x == -1002 && y == 77 && z == 1003) {
            if (scoreboard.getTeam("tag3norden").hasPlayer(player)) {
                return;
            }
            sendMessageToTag3Players(player, "诺登", "§f");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join tag3norden " + player.getName());
            player.sendMessage("§f诺登§f： 欢迎回来，" + player.getName() + "大人");
        } else if (x == -1002 && y == 77 && z == 999) {
            if (scoreboard.getTeam("tag3cheshirecat").hasPlayer(player)) {
                return;
            }
            sendMessageToTag3Players(player, "柴郡猫", "§d");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join tag3cheshirecat " + player.getName());
            player.sendMessage("§d柴郡猫§f： 能和你再说上话真是太好喵");
        } else if (x == -1002 && y == 77 && z == 997) {
            if (scoreboard.getTeam("tag3redhat").hasPlayer(player)) {
                return;
            }
            sendMessageToTag3Players(player, "小红帽", "§c");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join tag3redhat " + player.getName());
            player.sendMessage("§c小红帽§f： .......好了,我们出发吧");
        } else if (x == -1002 && y == 77 && z == 995) {
            if (scoreboard.getTeam("tag3alice").hasPlayer(player)) {
                return;
            }
            sendMessageToTag3Players(player, "爱丽丝", "§b");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join tag3alice " + player.getName());
            player.sendMessage("§b爱丽丝§f： 去寻觅爱的浪漫吧~☆");
        } else if (x == -998 && y == 77 && z == 1003) {
            if (scoreboard.getTeam("tag3lindamayer").hasPlayer(player)) {
                return;
            }
            sendMessageToTag3Players(player, "琳达梅尔", "§8");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join tag3lindamayer " + player.getName());
            player.sendMessage("§8琳达梅尔§f： 我要重建新的黒之裁判，将盘踞于大地之上的罪人处刑！");
        } else if (x == -998 && y == 77 && z == 999) {
            if (scoreboard.getTeam("tag3mabel").hasPlayer(player)) {
                return;
            }
            sendMessageToTag3Players(player, "梅贝尔", "§7");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join tag3mabel " + player.getName());
            player.sendMessage("§7梅贝尔§f： 真的可以么？不要后悔哟~");
        } else if (x == -998 && y == 77 && z == 995) {
            if (scoreboard.getTeam("tag3kelti").hasPlayer(player)) {
                return;
            }
            sendMessageToTag3Players(player, "克缇", "§9");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join tag3kelti " + player.getName());
            player.sendMessage("§9克缇§f： 嗯，嗯，克缇，记住了哦。请多指教，" + player.getName() + "酱");
        }
    }

    public void onEnable() {
        players = new ArrayList<>();
        scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        for (String name : teamNames) {
            teams.add(scoreboard.getTeam(name));
        }
        Bukkit.getPluginManager().registerEvents(this, this);
        gameTime = 8400;
        Sign sign = (Sign) world.getBlockAt(-1000, 78, 1015).getState();
        sign.setLine(2, "当前时间为 " + gameTime / 1200 + " 分钟");
        sign.update();
        GameUtils.registerGame(getGameInstance());
    }

    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        HandlerList.unregisterAll((Plugin) this);
        if (players.size() > 0) {
            for (Player p : players) {
                p.teleport(new Location(world, 0.5, 89.0, 0.5));
                Bukkit.getPluginManager().callEvent(new PlayerChangeGameEvent(p, getGameInstance(), null));
            }
        }
        unregisterGame(getGameInstance());
    }

    private void sendMessageToTag3Players(Player player, String role, String color) {
        for (Team team : teams) {
            for (String entryName : team.getEntries()) {
                Player p = Bukkit.getPlayer(entryName);
                if (p != null) {
                    if (p.isOnline()) {
                        p.sendMessage(color + player.getName() + " §r誓约了 " + color + role);
                    }
                }
            }
        }
    }
}

