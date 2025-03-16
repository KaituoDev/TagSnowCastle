package fun.kaituo.tagsnowcastle.character;

import fun.kaituo.tagsnowcastle.util.Hunter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("unused")
public class Miranda extends Hunter {
    public static final String displayName = "米兰达";
    public static final String chooseMessage = "总有一天，这个虚假的世界会迎来崩坏的时刻......";
    public static final ChatColor color = ChatColor.DARK_GRAY;

    public Miranda(Player p) {
        super(p);
    }
}
