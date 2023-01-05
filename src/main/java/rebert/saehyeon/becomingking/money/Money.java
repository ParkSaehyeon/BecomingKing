package rebert.saehyeon.becomingking.money;

import me.saehyeon.saehyeonlib.util.Itemf;
import me.saehyeon.saehyeonlib.util.Playerf;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Money {
    public static ItemStack getItemStack(int amount) {
        return Itemf.createItemStack(Material.GOLD_NUGGET,amount,"§6옆전",null);
    }

    public static void add(Player player, int amount) {

        player.getInventory().addItem(getItemStack(amount));

    }

    public static void remove(Player player, int amount) {

        Playerf.removeItem(player, Material.GOLD_NUGGET,amount);

    }

}
