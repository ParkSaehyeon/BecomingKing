package rebert.saehyeon.becomingking.event;

import me.saehyeon.saehyeonlib.cool.event.CoolTimeEndEvent;
import me.saehyeon.saehyeonlib.main.SaehyeonLibListener;
import me.saehyeon.saehyeonlib.role.Role;
import me.saehyeon.saehyeonlib.util.Itemf;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rebert.saehyeon.becomingking.item.GameItem;
import rebert.saehyeon.becomingking.item.GameItemType;

import java.util.Arrays;

public class onCoolTime implements SaehyeonLibListener {
    void onCoolEnd(CoolTimeEndEvent e) {
        Player p = e.getPlayer();
        Role role = Role.getByPlayer(p);

        switch (e.getCoolTime().getName()) {
            case "item.invisible":

                // 만약 플레이어가 아직도 평민라면 아이템 투명 아이템 재 지급하기
                if(role != null && role.getName().equals("pyungmin")) {

                    p.getInventory().addItem(GameItem.get(GameItemType.PYUNGMIN_INVISIBLE));

                }

                break;
        }
    }
}
