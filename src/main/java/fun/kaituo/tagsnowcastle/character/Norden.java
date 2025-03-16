package fun.kaituo.tagsnowcastle.character;

import fun.kaituo.tagsnowcastle.TagSnowCastle;
import fun.kaituo.tagsnowcastle.util.Human;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@SuppressWarnings("unused")
public class Norden extends Human {
    public static final String displayName = "诺登";
    public static final String chooseMessage = "欢迎回来，大人。";
    public static final ChatColor color = ChatColor.WHITE;

    public Norden(Player p) {
        super(p);
        ItemStack pocketWatch = TagSnowCastle.inst().getItem("PocketWatch");
        assert pocketWatch != null;
        p.getInventory().addItem(pocketWatch);
    }

    @Override
    public void applyPotionEffects() {
        super.applyPotionEffects();
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, -1, 4, false, false));
    }
}
