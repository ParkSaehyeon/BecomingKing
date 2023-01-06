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

            Role role = Role.getByPlayer(e.getPlayer());

            // 플레이어가 관전자이거나 또는 왕일 경우 시간 정지를 상쇄함
            if(role != null && !role.getName().equals("king") && e.getPlayer().getGameMode() != GameMode.SPECTATOR)
                e.setCancelled(true);

            return;
        }

        // 또는 혁명 준비장소로 이동되어야하는 위치인지 확인
        if(!e.getFrom().toBlockLocation().equals(e.getTo().toBlockLocation())) {

            BecomingKing.checkAndGotoRevolution(e.getPlayer());

        }

    }
}
