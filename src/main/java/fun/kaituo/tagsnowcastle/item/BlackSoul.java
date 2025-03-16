package fun.kaituo.tagsnowcastle.item;

import fun.kaituo.tagsnowcastle.TagSnowCastle;
import fun.kaituo.tagsnowcastle.character.Bill;
import fun.kaituo.tagsnowcastle.character.Dodo;
import fun.kaituo.tagsnowcastle.character.Norden;
import fun.kaituo.tagsnowcastle.util.ActiveItem;
import fun.kaituo.tagsnowcastle.util.PlayerData;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class BlackSoul extends ActiveItem {
    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    public boolean use(Player p) {
        PlayerData data = TagSnowCastle.inst().idDataMap.get(p.getUniqueId());
        assert data != null;
        if (data.getClass().equals(Bill.class) || data.getClass().equals(Dodo.class)) {
            p.sendMessage("§c你不能使用这个道具！");
            return false;
        }
        if (data.getClass().equals(Norden.class)) {
            p.setHealth(p.getMaxHealth());
            p.sendMessage("§c生命全部恢复！");
            return true;
        }
        if (p.getMaxHealth() > 3) {
            p.sendMessage("§c最大生命值减少，生命全部恢复！");
            p.setMaxHealth(p.getMaxHealth() - 3);
            p.setHealth(p.getMaxHealth());
            return true;
        }
        p.sendMessage("§c生命上限过低，无法使用！");
        return false;
    }

}
