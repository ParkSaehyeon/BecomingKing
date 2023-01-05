package rebert.saehyeon.becomingking.event;

import me.saehyeon.saehyeonlib.util.Playerf;
import org.bukkit.Bukkit;
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

            // 두 방 검으로 공격자가 공격했다면 내구도 감소
            if(Playerf.getMainHand(attacker).getType() == Material.NETHERITE_SWORD) {

                e.setDamage(new Random().nextInt(20)+1);

                Bukkit.broadcastMessage(""+Playerf.getMainHand(attacker).getDurability());

                if(Playerf.getMainHand(attacker).getDurability() == 1025) {

                    Playerf.removeHandItem(attacker,1);

                } else {

                    Playerf.getMainHand(attacker).setDurability((short)1025);

                }

            }
        }
    }
}
