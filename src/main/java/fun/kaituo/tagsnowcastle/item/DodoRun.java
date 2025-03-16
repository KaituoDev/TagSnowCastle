package fun.kaituo.tagsnowcastle.item;

import fun.kaituo.tagsnowcastle.TagSnowCastle;
import fun.kaituo.tagsnowcastle.util.ActiveItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@SuppressWarnings("unused")
public class DodoRun extends ActiveItem {
    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }

    @Override
    public boolean use(Player p) {
        ItemStack dodoRunFaster = TagSnowCastle.inst().getItem("DodoRunFaster");
        assert dodoRunFaster != null;
        p.getInventory().addItem(dodoRunFaster);
        p.sendMessage("§b获得加速！");
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 0));
        return true;
    }
}
