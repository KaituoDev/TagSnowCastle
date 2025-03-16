package fun.kaituo.tagsnowcastle;

import fun.kaituo.gameutils.GameUtils;
import fun.kaituo.gameutils.game.Game;
import fun.kaituo.tagsnowcastle.character.Norden;
import fun.kaituo.tagsnowcastle.state.HuntState;
import fun.kaituo.tagsnowcastle.state.ReadyState;
import fun.kaituo.tagsnowcastle.state.WaitState;
import fun.kaituo.tagsnowcastle.util.PlayerData;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;

public class TagSnowCastle extends Game {
    private static TagSnowCastle instance;
    public static TagSnowCastle inst() { return instance; }

    @Getter private Scoreboard mainBoard;
    @Getter private Scoreboard tagBoard;
    @Getter private Team tagTeam;

    public final Set<UUID> playerIds = new HashSet<>();
    public final Map<UUID, PlayerData> idDataMap = new HashMap<>();
    public final Map<UUID, Class<? extends PlayerData>> playerCharacterChoices = new HashMap<>();


    public Set<Player> getPlayers() {
        Set<Player> players = new HashSet<>();
        for (UUID id : playerIds) {
            Player p = Bukkit.getPlayer(id);
            assert p != null;
            players.add(p);
        }
        return players;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addPlayer(Player p) {
        p.setBedSpawnLocation(location, true);
        playerIds.add(p.getUniqueId());
        p.setScoreboard(tagBoard);
        playerCharacterChoices.putIfAbsent(p.getUniqueId(), Norden.class);
        super.addPlayer(p);
    }

    @Override
    public void removePlayer(Player p) {
        p.setScoreboard(mainBoard);
        playerIds.remove(p.getUniqueId());
        super.removePlayer(p);
    }

    @Override
    public void forceStop() {
        super.forceStop();
    }

    @Override
    public void tick() {
        super.tick();
    }

    private void initScoreboard() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        mainBoard = manager.getMainScoreboard();
        tagBoard = manager.getNewScoreboard();

        tagTeam = tagBoard.registerNewTeam("tag");
        tagTeam.setAllowFriendlyFire(true);
        tagTeam.setCanSeeFriendlyInvisibles(false);
        tagTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
    }

    private void initStates() {
        WaitState.INST.init();
        ReadyState.INST.init();
        HuntState.INST.init();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        instance = this;
        saveDefaultConfig();
        updateExtraInfo("§3地下箱庭", getLoc("hub"));
        initScoreboard();
        Bukkit.getScheduler().runTaskLater(this, () -> {
            initStates();
            setState(WaitState.INST);
        }, 1);
    }

    @Override
    public void onDisable() {
        for (Player p : getPlayers()) {
            removePlayer(p);
            GameUtils.inst().join(p, GameUtils.inst().getLobby());
        }
        this.state.exit();
        Bukkit.getScheduler().cancelTasks(this);
        super.onDisable();
    }
}
