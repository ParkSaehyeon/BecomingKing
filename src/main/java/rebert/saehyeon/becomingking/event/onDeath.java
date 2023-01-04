package rebert.saehyeon.becomingking.event;

import me.saehyeon.saehyeonlib.role.Role;
import me.saehyeon.saehyeonlib.util.BukkitTaskf;
import me.saehyeon.saehyeonlib.util.Playerf;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import rebert.saehyeon.becomingking.BecomingKing;
import rebert.saehyeon.becomingking.money.Money;

public class onDeath implements Listener {

    @EventHandler
    void onRespawn(PlayerRespawnEvent e) {
        BukkitTaskf.wait(() -> BecomingKing.respawn(e.getPlayer()),1);
    }

    @EventHandler
    void onDeath(PlayerDeathEvent e) {

        Player victim   = e.getEntity();
        Player attacker = victim.getKiller();

        // 공격자가 없음
        if(attacker == null) {
            e.setDeathMessage("§7"+victim.getName()+"§f(이)가 죽었습니다.");
            return;
        }

        // 공격자가 있음
        Role attackerRole   = Role.getByPlayer(attacker);
        Role victimRole     = Role.getByPlayer(victim);

        if(victimRole == null || attackerRole == null) {
            Bukkit.broadcastMessage("피해자("+victim.getName()+") 또는 가해자("+attacker.getName()+")가 역할을 가지고 있지 않아, 역할 바꾸는 작업을 하지 않곘음.");
            return;
        }

        // 피해자의 역할이 공격자보다 한 단계 높은 지 확인
        if((int)attackerRole.getState("power") + 1 == (int)victimRole.getState("power")) {

            // 공격자 피해자 역할 바꾸기
            attackerRole.add(victim);
            victimRole.add(attacker);

            BecomingKing.gotoWaitRoom(victim);

            e.setDeathMessage("§7"+attacker.getName()+"§f(이)가 §7"+victim.getName()+"§f(을)를 죽였습니다.");

            return;

        }

        // 공격자: 왕, 피해자: 모두
        if(attackerRole.getName().equals("king")) {

            // 피해자: 천민
            if(victimRole.getName().equals("cheonmin")) {

                // 천민 -> 노비로 강등
                Role.getByName("nobi").add(victim);

                BecomingKing.gotoWaitRoom(victim);

                e.setDeathMessage("§7"+attacker.getName()+"§f(이)가 §7"+victim.getName()+"§f(을)를 자신의 노비로 만들었습니다!");

                // 천민 다 죽었으면, 다 죽었다고 알리기
                if(BecomingKing.canKingKillEveryone())
                    Playerf.sendTitleAll("§c§l폭군","왕은 이제 모두를 죽일 수 있어요.");

                return;
            }

            // 왕이 지금 모두를 죽일 수 있는 상태임.
            if(BecomingKing.canKingKillEveryone()) {

                // 피해자 탈락
                BecomingKing.out(victim);

                e.setDeathMessage("§7"+attacker.getName()+"§f(이)가 §7"+victim.getName()+"§f(을)를 탈락시켰습니다!");

                return;

            }

            BecomingKing.checkKingWin();

        }

        // 공격자: 노비, 피해자: 왕
        if(attackerRole.getName().equals("nobi") && victimRole.getName().equals("king")) {
            BecomingKing.Stop(Role.getByName("nobi"));
            return;
        }

        // 어떤 조건에도 부합하지 않음 -> 사람 잘못 죽임.

        // 10 냥 있는지 확인
        if(attacker.getInventory().contains(Money.getItemStack(10))) {

            // 10냥 없애기
            Money.remove(attacker, 10);

            attacker.sendMessage("§c당신은 사람을 잘 못 죽였기 때문에 10냥을 잃었습니다.");

            e.setDeathMessage("§7"+attacker.getName()+"§f(이)가 §7"+victim.getName()+"§f(을)를 죽였습니다.");

        } else {

            // 10냥이 없음
            BecomingKing.out(attacker);

            e.setDeathMessage("§7"+attacker.getName()+"§f(이)가 §7"+victim.getName()+"§f(을)를 잘 못 죽였으나 벌금을 낼 수 없어, 탈락했습니다.");

        }

    }
}