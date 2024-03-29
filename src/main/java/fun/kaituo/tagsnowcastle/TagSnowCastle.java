package fun.kaituo.tagsnowcastle;

import fun.kaituo.gameutils.GameUtils;
import fun.kaituo.gameutils.event.PlayerChangeGameEvent;
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


public class TagSnowCastle extends JavaPlugin implements Listener {
    private GameUtils gameUtils;
    static List<Player> players;
    static long gameTime;
    Scoreboard scoreboard;
    List<String> teamNames = new ArrayList<>(List.of(new String[]{"tag3norden", "tag3cheshirecat", "tag3redhat", "tag3alice",
            "tag3lindamayer", "tag3mabel", "tag3kelti","tag3bill", "tag3dodo", "tag3eunice", "tag3leaf", "tag3miranda"}));
    List<Team> teams = new ArrayList<>();

    public static TagSnowCastleGame getGameInstance() {
        return TagSnowCastleGame.getInstance();
    }

    @EventHandler
    public void onButtonClicked(PlayerInteractEvent pie) {
        if (!pie.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if (!pie.getClickedBlock().getType().equals(Material.OAK_BUTTON)) {
            return;
        }
        if (pie.getClickedBlock().getLocation().equals(new Location(gameUtils.getWorld(), -1000, 77, 1015))) {
            TagSnowCastleGame.getInstance().startGame();
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
        if (TagSnowCastleGame.getInstance().running) {
            return;
        }
        if (x == -1002 && y == 77 && z == 1003) {
            if (scoreboard.getTeam("tag3norden").hasPlayer(player)) {
                return;
            }
            broadcastHumanChoiceMessage(player, "诺登", "§f");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join tag3norden " + player.getName());
            player.sendMessage("§f诺登§f： 欢迎回来，" + player.getName() + "大人");
        } else if (x == -1002 && y == 77 && z == 999) {
            if (scoreboard.getTeam("tag3cheshirecat").hasPlayer(player)) {
                return;
            }
            broadcastHumanChoiceMessage(player, "柴郡猫", "§d");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join tag3cheshirecat " + player.getName());
            player.sendMessage("§d柴郡猫§f： 能和你再说上话真是太好喵");
        } else if (x == -1002 && y == 77 && z == 997) {
            if (scoreboard.getTeam("tag3redhat").hasPlayer(player)) {
                return;
            }
            broadcastHumanChoiceMessage(player, "小红帽", "§c");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join tag3redhat " + player.getName());
            player.sendMessage("§c小红帽§f： .......好了,我们出发吧");
        } else if (x == -1002 && y == 77 && z == 995) {
            if (scoreboard.getTeam("tag3alice").hasPlayer(player)) {
                return;
            }
            broadcastHumanChoiceMessage(player, "爱丽丝", "§b");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join tag3alice " + player.getName());
            player.sendMessage("§b爱丽丝§f： 去寻觅爱的浪漫吧~☆");
        } else if (x == -998 && y == 77 && z == 1003) {
            if (scoreboard.getTeam("tag3lindamayer").hasPlayer(player)) {
                return;
            }
            broadcastDevilChoiceMessage(player, "琳达梅尔", "§8");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join tag3lindamayer " + player.getName());
            player.sendMessage("§8琳达梅尔§f： 我要重建新的黒之裁判，将盘踞于大地之上的罪人处刑！");
        } else if (x == -998 && y == 77 && z == 999) {
            if (scoreboard.getTeam("tag3mabel").hasPlayer(player)) {
                return;
            }
            broadcastHumanChoiceMessage(player, "梅贝尔", "§7");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join tag3mabel " + player.getName());
            player.sendMessage("§7梅贝尔§f： 真的可以么？不要后悔哟~");
        } else if (x == -998 && y == 77 && z == 995) {
            if (scoreboard.getTeam("tag3kelti").hasPlayer(player)) {
                return;
            }
            broadcastHumanChoiceMessage(player, "克缇", "§9");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join tag3kelti " + player.getName());
            player.sendMessage("§9克缇§f： 嗯，嗯，克缇，记住了哦。请多指教，" + player.getName() + "酱");
        } else if (x == -998 && y == 77 && z == 993) {
            if (scoreboard.getTeam("tag3leaf").hasPlayer(player)) {
                return;
            }
            broadcastHumanChoiceMessage(player, "莉耶芙", "§a");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join tag3leaf " + player.getName());
            player.sendMessage("§a莉耶芙§f： 对吧,这果然就是所谓的命运啊!");
        } else if (x == -1002 && y == 77 && z == 993) {
            if (scoreboard.getTeam("tag3dodo").hasPlayer(player)) {
                return;
            }
            broadcastHumanChoiceMessage(player, "渡渡", "§7");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join tag3dodo " + player.getName());
            player.sendMessage("§7渡渡§f： 哼哼——！看来你的心已被吾辈俘获，是这么回事吧？");
        } else if (x == -998 && y == 77 && z == 997) {
            if (scoreboard.getTeam("tag3eunice").hasPlayer(player)) {
                return;
            }
            broadcastHumanChoiceMessage(player, "尤妮丝", "§f");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join tag3eunice " + player.getName());
            player.sendMessage("§f尤妮丝§f： 很好，让我们一起守护平等而纯洁的世界吧");
        } else if (x == -1002 && y == 77 && z == 1001) {
            if (scoreboard.getTeam("tag3bill").hasPlayer(player)) {
                return;
            }
            broadcastHumanChoiceMessage(player, "比尔", "§2");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join tag3bill " + player.getName());
            player.sendMessage("§2比尔§f： 那个...我为"+ player.getName()+"做了便当...不介意的话请您尝尝看吧~");
        }  else if (x == -998 && y == 77 && z == 1001) {
            if (scoreboard.getTeam("tag3miranda").hasPlayer(player)) {
                return;
            }
            broadcastDevilChoiceMessage(player, "米兰达", "§8");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join tag3miranda " + player.getName());
            player.sendMessage("§8米兰达§f： 总有一天，这个虚假的世界会迎来崩坏的时刻......");
        }
    }

    public void onEnable() {
        gameUtils = (GameUtils) Bukkit.getPluginManager().getPlugin("GameUtils");
        players = new ArrayList<>();
        scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        for (String name : teamNames) {
            teams.add(scoreboard.getTeam(name));
        }
        Bukkit.getPluginManager().registerEvents(this, this);
        gameTime = 8400;
        Sign sign = (Sign) gameUtils.getWorld().getBlockAt(-1000, 78, 1015).getState();
        sign.setLine(2, "当前时间为 " + gameTime / 1200 + " 分钟");
        sign.update();
        gameUtils.registerGame(getGameInstance());
    }

    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        HandlerList.unregisterAll((Plugin) this);
        for (Player p: Bukkit.getOnlinePlayers()) {
            if (gameUtils.getPlayerGame(p) == getGameInstance()) {
                Bukkit.dispatchCommand(p, "join Lobby");
            }
        }
        gameUtils.unregisterGame(getGameInstance());
    }

    private void broadcastHumanChoiceMessage(Player player, String role, String color) {
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
    private void broadcastDevilChoiceMessage(Player player, String role, String color) {
        for (Team team : teams) {
            for (String entryName : team.getEntries()) {
                Player p = Bukkit.getPlayer(entryName);
                if (p != null) {
                    if (p.isOnline()) {
                        p.sendMessage(color + player.getName() + " §r选择成为 " + color + role);
                    }
                }
            }
        }
    }
}

