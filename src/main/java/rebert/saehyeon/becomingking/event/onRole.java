package rebert.saehyeon.becomingking.event;

import me.saehyeon.saehyeonlib.main.SaehyeonLibListener;
import me.saehyeon.saehyeonlib.role.Role;
import me.saehyeon.saehyeonlib.role.event.RolePlayerAddEvent;
import me.saehyeon.saehyeonlib.util.Itemf;
import me.saehyeon.saehyeonlib.util.Playerf;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import rebert.saehyeon.becomingking.item.GameItem;
import rebert.saehyeon.becomingking.item.GameItemType;

import java.util.ArrayList;
import java.util.Arrays;

public class onRole implements SaehyeonLibListener {
    void onRoleChange(RolePlayerAddEvent e) {

        Role role   = e.getRole();
        Player p    = e.getPlayer();

        p.sendTitle("","당신은 이제부터 "+role.getPrefix()+"§f입니다.",0,20,15);

        // 쉘커 껍대기 없애기
        p.getInventory().remove(Material.SHULKER_SHELL);

        // 신속 버프 없애기
        p.removePotionEffect(PotionEffectType.SPEED);

        Bukkit.broadcastMessage("역할이 변경됨! "+p.getName()+"의 역할이 "+role.getName()+"(으)로 변경됨.");

        switch (role.getName()) {

            // 왕 바뀜 -> 모든 노비 해방
            case "king":

                Role nobi = Role.getByName("nobi");
                Role cheonmin = Role.getByName("cheonmin");

                if(!nobi.getPlayers().isEmpty()) {

                    // 노비들은 천민으로
                    new ArrayList<>( nobi.getPlayers() ).forEach(cheonmin::add);

                    Playerf.sendTitleAll("§l정권교체","모든 노비가 해방되었습니다.",0,20,15);

                }

                break;

            // 선비 -> 신속 1
            case "sunbi":

                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,100000,1,false,false));

                break;

            // 평민 -> 선비 위치 알 수 있는 아이템, 8초 간 투명 아이템(5분 쿨타임)
            case "pyungmin":

                // 아이템 주기
                p.getInventory().addItem(GameItem.get(GameItemType.PYUNGMIN_FIND_SUNBI));
                p.getInventory().addItem(GameItem.get(GameItemType.PYUNGMIN_INVISIBLE));

                break;
        }

    }
}
