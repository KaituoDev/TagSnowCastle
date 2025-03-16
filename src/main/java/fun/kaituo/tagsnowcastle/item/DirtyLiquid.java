package fun.kaituo.tagsnowcastle.item;

import fun.kaituo.tagsnowcastle.TagSnowCastle;
import fun.kaituo.tagsnowcastle.character.Bill;
import fun.kaituo.tagsnowcastle.character.Dodo;
import fun.kaituo.tagsnowcastle.util.ActiveItem;
import fun.kaituo.tagsnowcastle.util.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@SuppressWarnings("unused")
public class DirtyLiquid extends ActiveItem {
    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }
    @Override
    public boolean use(Player p) {
        PlayerData data = TagSnowCastle.inst().idDataMap.get(p.getUniqueId());
        assert data != null;
        if (data.getClass().equals(Bill.class) || data.getClass().equals(Dodo.class)) {
            p.sendMessage("§c你不能使用这个道具！");
            return false;
        }
        p.sendMessage("§a获得生命恢复和§c反胃效果！");
        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,  150, 1));
        p.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA,  300, 0));
        return true;
    }
}
