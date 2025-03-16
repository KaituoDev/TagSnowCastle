package fun.kaituo.tagsnowcastle.item;

import fun.kaituo.tagsnowcastle.TagSnowCastle;
import fun.kaituo.tagsnowcastle.character.Dodo;
import fun.kaituo.tagsnowcastle.util.ActiveItem;
import fun.kaituo.tagsnowcastle.util.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@SuppressWarnings("unused")
public class DodoRunFaster extends ActiveItem {
    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }

    @Override
    public boolean canObtainDirectly() {
        return false;
    }

    @Override
    public boolean use(Player p) {
        p.sendMessage("§b获得加速！");
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1));

        PlayerData data = TagSnowCastle.inst().idDataMap.get(p.getUniqueId());
        assert data != null;
        if (data.getClass().equals(Dodo.class)) {
            p.sendMessage("§a获得额外治疗！");
            p.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_HEALTH, 1, 0));
        }
        return true;
    }
}
