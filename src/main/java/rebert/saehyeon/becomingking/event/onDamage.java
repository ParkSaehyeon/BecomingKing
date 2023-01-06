package rebert.saehyeon.becomingking.event;

import me.saehyeon.saehyeonlib.region.Region;
import me.saehyeon.saehyeonlib.role.Role;
import me.saehyeon.saehyeonlib.util.Locationf;
import me.saehyeon.saehyeonlib.util.Playerf;
import me.saehyeon.saehyeonlib.util.Stringf;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class onDamage implements Listener {
    @EventHandler
    void onDamage(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player attacker = (Player)e.getDamager();
            Player victim   = (Player)e.getEntity();

            // 만약 NO pvp 존에 있으면 데미지 X
            Region noPVP = Region.findByName("king-nopvp");
            Location[] pos = noPVP.getPosition();

            if(Locationf.isWithin(e.getEntity().getLocation(),pos[0],pos[1])) {
                e.setCancelled(true);
                attacker.sendMessage("§c피해자가 PVP 금지 지역에 있습니다.");
                return;
            }

            Role victimRole = Role.getByPlayer(victim);

            // 왕이 다이아몬드 검으로 공격받으면 15데미지
            if(victimRole != null && victimRole.getName().equals("king") && Playerf.getMainHand(attacker).getType() == Material.DIAMOND_SWORD) {
                e.setDamage(15);
            }

            // 두 방 검으로 공격자가 공격했다면 내구도 감소
            if(Playerf.getMainHand(attacker).getType() == Material.NETHERITE_SWORD) {

                e.setDamage(new Random().nextInt(20)+1);

                if(Playerf.getMainHand(attacker).getDurability() == 1025) {

                    Playerf.removeHandItem(attacker,1);

                } else {

                    Playerf.getMainHand(attacker).setDurability((short)1025);

                }

            }
        }
    }
}
