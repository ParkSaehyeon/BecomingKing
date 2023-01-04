package rebert.saehyeon.becomingking.event;

import me.saehyeon.saehyeonlib.role.Role;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import rebert.saehyeon.becomingking.BecomingKing;

public class onMove implements Listener {
    @EventHandler
    void onMove(PlayerMoveEvent e) {

        if(BecomingKing.isTimeStop) {

            // 플레이어가 관전자이거나 또는 왕일 경우 시간 정지를 상쇄함
            if(!Role.getByPlayer(e.getPlayer()).getName().equals("king") && e.getPlayer().getGameMode() != GameMode.SPECTATOR)
                e.setCancelled(true);

        }

    }
}
