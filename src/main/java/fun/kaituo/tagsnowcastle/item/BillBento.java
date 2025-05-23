package fun.kaituo.tagsnowcastle.item;

import fun.kaituo.tagsnowcastle.TagSnowCastle;
import fun.kaituo.tagsnowcastle.character.Bill;
import fun.kaituo.tagsnowcastle.util.ActiveItem;
import fun.kaituo.tagsnowcastle.util.PlayerData;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class BillBento extends ActiveItem {
    @Override
    public boolean use(Player p) {
        PlayerData data = TagSnowCastle.inst().idDataMap.get(p.getUniqueId());
        assert data != null;
        if (data.getClass().equals(Bill.class)) {
            p.sendMessage("§c你不能使用这个道具！");
            return false;
        }
        if (p.getHealth() == p.getMaxHealth()) {
            p.sendMessage("§c你的血量已满，无需食用便当！");
            return false;
        }
        p.setHealth(p.getMaxHealth());
        p.sendMessage("§a你吃了比尔的便当，生命值已恢复！");
        return true;
    }

    @Override
    public boolean canObtainDirectly() {
        return false;
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }
}
