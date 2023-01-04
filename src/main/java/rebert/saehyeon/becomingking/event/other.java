package rebert.saehyeon.becomingking.event;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class other implements Listener {
    @EventHandler
    void onFoodLevel(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    void onFarmHarm(BlockDestroyEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    void onUnbreak(PlayerItemDamageEvent e) {
        e.setCancelled(true);
    }
}
