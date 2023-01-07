package rebert.saehyeon.becomingking.timer;

import me.saehyeon.saehyeonlib.role.Role;
import me.saehyeon.saehyeonlib.timer.Timer;
import me.saehyeon.saehyeonlib.util.BukkitTaskf;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import rebert.saehyeon.becomingking.money.Money;

public class GameTimer {
    public static void Start() {
        Timer timer = Timer.findByName("king");

        if(timer != null)
            timer.clear();

        BossBar bossBar = Bukkit.createBossBar("§f§l남은 시간 {h}:{m}:{s}", BarColor.WHITE, BarStyle.SOLID);

        Timer.StartTimer("king",bossBar,1800);
    }

    public static void StartRoleTimer() {
        Role yangban = Role.getByName("yangban");
        Role sunbi = Role.getByName("sunbi");

        BukkitTaskf.timer(() -> {

            // 양반들한테 돈 주기
            yangban.getPlayers().forEach(p -> Money.add(p,5));

            // 선비한테 신속주기
            sunbi.getPlayers().forEach(p -> p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10000,1,false,false)));

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

    public static void StartRevolutionTimer() {
        BukkitTaskf.timer(() -> {
            Bukkit.getOnlinePlayers().forEach(p -> {

                Role role = Role.getByPlayer(p);

                if(role != null && role.getName().equals("nobi")) {

                    if (p.getInventory().contains(Material.DIAMOND_SWORD)) {

                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,100000,2));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,100000,3));

                    } else {

                        p.removePotionEffect(PotionEffectType.SPEED);
                        p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);

                    }

                }

            });
        },0,20);
    }
}
