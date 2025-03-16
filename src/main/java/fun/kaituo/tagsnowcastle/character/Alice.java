package fun.kaituo.tagsnowcastle.character;

import fun.kaituo.tagsnowcastle.TagSnowCastle;
import fun.kaituo.tagsnowcastle.state.HuntState;
import fun.kaituo.tagsnowcastle.util.Corpse;
import fun.kaituo.tagsnowcastle.util.Human;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static fun.kaituo.gameutils.util.ItemUtils.removeItem;

@SuppressWarnings("unused")
public class Alice extends Human {
    public static final String displayName = "爱丽丝";
    public static final String chooseMessage = "去寻觅爱的浪漫吧~☆";
    public static final ChatColor color = ChatColor.AQUA;


    private final ItemStack initialItem;
    private final ItemStack usedItem;
    private boolean usedRevival = false;

    public boolean hasUsedRevival() {
        return usedRevival;
    }

    public Alice(Player p) {
        super(p);
        initialItem = TagSnowCastle.inst().getItem("AliceInitial");
        usedItem = TagSnowCastle.inst().getItem("AliceUsed");
    }

    @EventHandler
    public void onUse(PlayerInteractAtEntityEvent e) {
        Player p = e.getPlayer();
        if (!p.equals(player)) {
            return;
        }
        if (usedRevival) {
            return;
        }
        // Only revive if hand is empty.
        if (!p.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
            return;
        }
        Entity entity = e.getRightClicked();
        if (!(entity instanceof ArmorStand stand)) {
            return;
        }
        for (Corpse corpse: HuntState.INST.corpses) {
            if (!corpse.getMainPart().equals(stand)) {
                continue;
            }
            if (corpse.revive()) {
                removeItem(player.getInventory(), initialItem);
                player.getInventory().addItem(usedItem);
                usedRevival = true;
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, -1, 0, false, false));
                player.sendMessage("§a你复活了队友，获得永久加速！");
                return;
            }
        }
    }
}
