package rebert.saehyeon.becomingking.event;

import me.saehyeon.saehyeonlib.main.SaehyeonLibListener;
import me.saehyeon.saehyeonlib.role.Role;
import me.saehyeon.saehyeonlib.role.event.RolePlayerAddEvent;
import me.saehyeon.saehyeonlib.util.Itemf;
import me.saehyeon.saehyeonlib.util.Playerf;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import rebert.saehyeon.becomingking.item.GameItem;
import rebert.saehyeon.becomingking.item.GameItemType;

import java.util.ArrayList;
import java.util.Arrays;

public class onRole implements SaehyeonLibListener {
    void onRoleChange(RolePlayerAddEvent e) {

        Role role   = e.getRole();
        Player p    = e.getPlayer();

        p.sendTitle("","당신은 이제부터 "+role.getPrefix()+"§f입니다.",0,40,15);
        p.sendActionBar("당신은 "+role.getPrefix().replace(" ","")+"§f입니다.");

        // 쉘커 껍대기 없애기
        p.getInventory().remove(Material.PINK_DYE);
        p.getInventory().remove(Material.GRAY_DYE);
        p.getInventory().remove(Material.CLOCK);

        // 신속 버프 없애기
        p.removePotionEffect(PotionEffectType.SPEED);
        p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);

        p.getInventory().remove(Material.STONE_SWORD);
        p.getInventory().remove(Material.IRON_SWORD);
        p.getInventory().remove(Material.DIAMOND_SWORD);

        p.getInventory().addItem( Itemf.createItemStack(Material.STONE_SWORD,1,"§l단단한 검",Arrays.asList("","§7§o돌로 만들어진, 간단한 돌 검입니다.","")) );

        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, SoundCategory.MASTER,1,2);

        //Bukkit.broadcastMessage("역할이 변경됨! "+p.getName()+"의 역할이 "+role.getPrefix()+"§f(으)로 변경됨.");

        switch (role.getName()) {

            // 왕 바뀜 -> 모든 노비 해방
            case "king":

                // 시간정지 아이템 지급
                p.getInventory().addItem(GameItem.get(GameItemType.TIME_STOP));

                Role nobi = Role.getByName("nobi");
                Role cheonmin = Role.getByName("cheonmin");

                if(!nobi.getPlayers().isEmpty()) {

                    // 노비들은 천민으로
                    new ArrayList<>( nobi.getPlayers() ).forEach(cheonmin::add);

                    Playerf.sendTitleAll("§l정권교체","모든 노비가 해방되었습니다.",0,40,15);

                }

                break;

            // 선비 -> 신속 1
            case "sunbi":

                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,100000,1,false,false));

                break;

            // 평민 -> 선비 위치 알 수 있는 아이템, 8초 간 투명 아이템(5분 쿨타임)
            case "pyungmin":

                // 아이템 주기
                p.getInventory().addItem(GameItem.get(GameItemType.PYUNGMIN_FIND_SUNBI));
                p.getInventory().addItem(GameItem.get(GameItemType.PYUNGMIN_INVISIBLE));

                break;

            // 천민한테는 철칼 쥐어주기
            case "cheonmin":

                p.getInventory().remove(Material.STONE_SWORD);
                p.getInventory().addItem( Itemf.createItemStack(Material.IRON_SWORD,1,"§l민초의 난",null) );

                break;
        }

    }
}
