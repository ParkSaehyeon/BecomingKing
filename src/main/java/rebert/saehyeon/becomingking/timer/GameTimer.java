package rebert.saehyeon.becomingking.timer;

import me.saehyeon.saehyeonlib.role.Role;
import me.saehyeon.saehyeonlib.timer.Timer;
import me.saehyeon.saehyeonlib.util.BukkitTaskf;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import rebert.saehyeon.becomingking.money.Money;

public class GameTimer {
    public static void Start() {
        Timer timer = Timer.findByName("king");

        if(timer != null)
            timer.clear();

        BossBar bossBar = Bukkit.createBossBar("§f§l남은 시간 {h}:{m}:{s}", BarColor.WHITE, BarStyle.SOLID);

        Timer.StartTimer("king",bossBar,3600);
    }

    public static void StartYangbanTimer() {
        Role yangban = Role.getByName("yangban");

        BukkitTaskf.timer(() -> {

            // 양반들한테 돈 주기
            yangban.getPlayers().forEach(p -> Money.add(p,5));

        },0,20*(60*2+30));
    }

    public static void StartRoleActionbarTimer() {
        BukkitTaskf.timer(() -> {

            Bukkit.getOnlinePlayers().forEach(p -> {
                Role role = Role.getByPlayer(p);

                if(role != null)
                    p.sendActionBar("당신은 "+role.getPrefix().replace(" ","")+"§f입니다.");
            });
        },0,20*2);
    }
}
