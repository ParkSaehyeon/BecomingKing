package rebert.saehyeon.becomingking.event;

import me.saehyeon.saehyeonlib.role.Role;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rebert.saehyeon.becomingking.BecomingKing;
import rebert.saehyeon.becomingking.money.Money;

public class onCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(label.equals("king")) {
            switch (args[0]) {
                case "start":
                    BecomingKing.Start();

                    break;

                case "stop":
                    BecomingKing.Stop(Role.getByName(args[1]));
                    break;

                case "money":
                    ((Player)sender).getInventory().addItem(Money.getItemStack(Integer.parseInt(args[1])));
                    break;
            }
        }
        return false;
    }
}
