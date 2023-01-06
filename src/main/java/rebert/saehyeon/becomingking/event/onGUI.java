package rebert.saehyeon.becomingking.event;

import me.saehyeon.saehyeonlib.gui.event.GUIOpenEvent;
import me.saehyeon.saehyeonlib.main.SaehyeonLibListener;
import me.saehyeon.saehyeonlib.role.Role;
import me.saehyeon.saehyeonlib.util.BukkitTaskf;
import me.saehyeon.saehyeonlib.util.Playerf;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class onGUI implements Listener {

    @EventHandler
    void onGUIOpen(InventoryOpenEvent e) {
        BukkitTaskf.wait(() -> {

            Player p = (Player)e.getPlayer();
            Inventory inv = p.getOpenInventory().getTopInventory();

            if(inv.contains(Material.DIAMOND_SWORD)) {

                Playerf.sendTitleAll("§c§l반란위험","§c혁명§f을 꿈꾸는 이가 탄생한 것 같습니다.",0,40,15);
                Bukkit.getOnlinePlayers().forEach(_p -> _p.playSound(_p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.MASTER,1,1));

            }

        },2);
    }

}
