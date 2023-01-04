package rebert.saehyeon.becomingking.item;

import me.saehyeon.saehyeonlib.util.Itemf;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class GameItem {
    public static ItemStack get(GameItemType type) {
        switch (type) {
            case PYUNGMIN_FIND_SUNBI:
                return Itemf.createItemStack(Material.PINK_DYE,1,"§l선비찾기", Arrays.asList("§7우클릭§f하여 선비 위치를 찾습니다."));

            case PYUNGMIN_INVISIBLE:
                return Itemf.createItemStack(Material.GRAY_DYE,1,"§l투명", Arrays.asList("§7우클릭§f하여 §78초§f간 투명 상태가 됩니다.","§f§75분§f의 쿨타임이 있습니다."));

            case SPEED:
                return Itemf.createItemStack(Material.LIGHT_BLUE_DYE,1,"§l전력질주", Arrays.asList("§7우클릭§f하여 §75초§f간 전력질주를 할 수 있습니다."));

            case SWORD:
                return Itemf.createItemStack(Material.NETHERITE_SWORD,1,"§l두 방 검", null);

            case RANDOM_TP:
                return Itemf.createItemStack(Material.PAPER,1,"§l랜덤 이동권", Arrays.asList("§7우클릭§f하여 당신을 제외한 한 사람에게 이동됩니다. §71회용§f입니다."));

            case ANNOUNCE_LOCATION:
                return Itemf.createItemStack(Material.PAPER,1,"§l위치 공개", Arrays.asList("§f모든 사람의 좌표를 공지합니다."));

            case SHOP_SPEED:
                return Itemf.createItemStack(Material.LIGHT_BLUE_DYE,1,"§l전력질주", Arrays.asList("§7우클릭§f하여 §75초§f간 전력질주를 할 수 있습니다.","§f  구매를 위해 §6§l10냥§f이 필요합니다."));

            case SHOP_SWORD:
                return Itemf.createItemStack(Material.NETHERITE_SWORD,1,"§l두 방 검", null);

            case SHOP_RANDOM_TP:
                return Itemf.createItemStack(Material.PAPER,1,"§l랜덤 이동권", Arrays.asList("§7우클릭§f하여 당신을 제외한 한 사람에게 이동됩니다. §71회용§f입니다.","§f  구매를 위해 §6§l10냥§f이 필요합니다."));

            case SHOP_ANNOUNCE_LOCATION:
                return Itemf.createItemStack(Material.PAPER,1,"§l위치 공개", Arrays.asList("§f모든 사람의 좌표를 공지합니다.","§f  구매를 위해 §6§l10냥§f이 필요합니다."));

            case TIME_STOP:
                return Itemf.createItemStack(Material.CLOCK, 1, "§l시간 정지", Arrays.asList("§7우클릭§f하여 당신을 제외한 모든 사람의 시간을 §730초§f동안 멈춥니다.","§f  구매를 위해 §6§l10냥§f이 필요합니다."));

        }

        return null;
    }
}
