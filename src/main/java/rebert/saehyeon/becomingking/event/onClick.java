package rebert.saehyeon.becomingking.event;

import me.saehyeon.saehyeonlib.cool.CoolTime;
import me.saehyeon.saehyeonlib.gui.GUI;
import me.saehyeon.saehyeonlib.gui.GUIRule;
import me.saehyeon.saehyeonlib.role.Role;
import me.saehyeon.saehyeonlib.util.BukkitTaskf;
import me.saehyeon.saehyeonlib.util.Itemf;
import me.saehyeon.saehyeonlib.util.Playerf;
import me.saehyeon.saehyeonlib.util.Stringf;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import rebert.saehyeon.becomingking.BecomingKing;
import rebert.saehyeon.becomingking.item.GameItem;
import rebert.saehyeon.becomingking.item.GameItemType;

import java.util.Arrays;

public class onClick implements Listener {
    @EventHandler
    void onClick(PlayerInteractEvent e) {

        Player p        = e.getPlayer();
        ItemStack item  = Playerf.getMainHand(p);

        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            switch (Itemf.getDisplayName(item)) {

                case "§l시간 정지":

                    // 한 번 쓰면 사라지기
                    p.getInventory().remove(Playerf.getMainHand(p));

                    // 모두를 정지시키기
                    BecomingKing.isTimeStop = true;
                    Playerf.sendTitleAll("","§b§l시간이 정지되었습니다.",0,100,20);

                    // 30초 동안 시간 정지
                    BukkitTaskf.wait(() -> {

                        BecomingKing.isTimeStop = false;
                        Playerf.sendTitleAll("","§b§l시간 정지가 해제되었습니다.",0,100,20);

                    },20*30);

                    break;

                case "§l선비찾기":

                    // 선비의 좌표를 알려기
                    p.sendMessage("\n현재 선비의 위치는 다음과 같습니다: ");

                    Role.getByName("sunbi").getPlayers().forEach(_p -> {
                        p.sendMessage(" - " + _p.getName() + ": §7" + Stringf.toLocationStr(_p.getLocation()));
                    });

                    break;

                case "§l투명":

                    if (!CoolTime.contains(p, "item.invisible")) {

                        // 쿨타임 설정
                        CoolTime.add(p, new CoolTime("item.invisible", 300));

                        // 8초간 투명
                        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 8, 5, false, false));

                        // 아이템 없애기
                        p.getInventory().remove(Playerf.getMainHand(p));

                    } else {

                        p.sendMessage("§c" + CoolTime.findByName(p, "item.invisible").getLeftTime() + "초 후에 다시 사용할 수 있습니다.");

                    }
                    break;

            }

        }
    }
}
