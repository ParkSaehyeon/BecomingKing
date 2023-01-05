package rebert.saehyeon.becomingking.event;

import me.saehyeon.saehyeonlib.gui.GUI;
import me.saehyeon.saehyeonlib.main.SaehyeonLibListener;
import me.saehyeon.saehyeonlib.shop.event.ShopClickItemEvent;
import me.saehyeon.saehyeonlib.util.Itemf;
import me.saehyeon.saehyeonlib.util.Stringf;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rebert.saehyeon.becomingking.item.GameItem;
import rebert.saehyeon.becomingking.item.GameItemType;
import rebert.saehyeon.becomingking.money.Money;

public class onShop implements SaehyeonLibListener {
    void onClick(ShopClickItemEvent e) {

        Player p = e.getPlayer();

        String itemName = Itemf.getDisplayName(e.getClickedItem());

        int cost;

        switch (itemName) {
            case "§f§l전력질주":
                cost = 15;
                break;

            case "§f§l두 방 검":
                cost = 20;
                break;

            case "§f§l랜덤 이동권":
                cost = 20;
                break;

            case "§f§l위치 공개":
                cost = 10;
                break;

            default:
                return;
        }

        // 돈이 있는지 확인 -> 있으면 상품 주기
        if(p.getInventory().contains(Material.GOLD_NUGGET, cost)) {

            Money.remove(p, cost);
            p.sendMessage("당신은 "+itemName+"§f(을)를 구매했습니다.");

            switch (itemName) {

                case "§f§l위치 공개":

                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage("§f   〈 §7"+p.getName()+"§f의 위치 공개 발동 〉");

                    Bukkit.getOnlinePlayers().forEach(_p -> {

                        if(_p.getGameMode() != GameMode.SPECTATOR) {

                            Bukkit.broadcastMessage(" - "+_p.getName()+": §7"+ Stringf.toLocationStr(_p.getLocation()));

                        }

                    });

                    break;

                case "§f§l두 방 검":

                    p.getInventory().addItem(GameItem.get(GameItemType.SWORD));

                    break;

                case "§f§l랜덤 이동권":
                    p.getInventory().addItem(GameItem.get(GameItemType.RANDOM_TP));
                    break;

                case "§f§l전력질주":
                    p.getInventory().addItem(GameItem.get(GameItemType.SPEED));
                    break;

            }

        } else {
            p.sendMessage("§c돈이 부족합니다.");
        }
    }
}
