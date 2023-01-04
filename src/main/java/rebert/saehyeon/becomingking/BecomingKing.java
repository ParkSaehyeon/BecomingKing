package rebert.saehyeon.becomingking;

import me.saehyeon.saehyeonlib.main.SaehyeonLibEvent;
import me.saehyeon.saehyeonlib.region.Region;
import me.saehyeon.saehyeonlib.role.Role;
import me.saehyeon.saehyeonlib.shop.Shop;
import me.saehyeon.saehyeonlib.timer.Timer;
import me.saehyeon.saehyeonlib.util.BukkitTaskf;
import me.saehyeon.saehyeonlib.util.Itemf;
import me.saehyeon.saehyeonlib.util.Playerf;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import rebert.saehyeon.becomingking.event.*;
import rebert.saehyeon.becomingking.item.GameItem;
import rebert.saehyeon.becomingking.item.GameItemType;
import rebert.saehyeon.becomingking.timer.GameTimer;
import rebert.saehyeon.becomingking.timer.GameTimerEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public final class BecomingKing extends JavaPlugin {

    public static boolean isTimeStop = false;
    public static World WORLD = Bukkit.getWorld("world");
    public static Location WaitingRoomLoc = new Location(WORLD,-65,81,207);
    public static ArrayList<Location> SpawnLoc = new ArrayList<>(Arrays.asList(
        new Location(WORLD,-36,64,201),
        new Location(WORLD,59,65,214),
        new Location(WORLD,72,66,168),
        new Location(WORLD,34,66,136),
        new Location(WORLD,19,71,76),
        new Location(WORLD,-35,66,76),
        new Location(WORLD,-76,67,91),
        new Location(WORLD,-74,66,118),
        new Location(WORLD,-70,64,141),
        new Location(WORLD,-75,64,199),
        new Location(WORLD,-82,64,251),
        new Location(WORLD,-15,65,263),
        new Location(WORLD,21,64,263),
        new Location(WORLD,81,67,291),
        new Location(WORLD,183,63,295),
        new Location(WORLD,199,64,241)
    ));

    public static BecomingKing ins;

    @Override
    public void onEnable() {
        ins = this;

        // 이벤트 등록
        Bukkit.getPluginManager().registerEvents(new onDeath(), this);
        Bukkit.getPluginManager().registerEvents(new onClick(), this);
        Bukkit.getPluginManager().registerEvents(new onMove(), this);
        Bukkit.getPluginManager().registerEvents(new other(), this);

        Bukkit.getPluginCommand("king").setExecutor(new onCommand());

        SaehyeonLibEvent.register(new GameTimerEvent());
        SaehyeonLibEvent.register(new onCoolTime());
        SaehyeonLibEvent.register(new onShop());
        SaehyeonLibEvent.register(new onRole());

        // 역할 등록
        Role.createRole("king","§e§l왕§f ",1).setState("power",5);
        Role.createRole("yangban","§6§l양반§f ",1).setState("power",4);
        Role.createRole("sunbi","§f§l선비§f ",1).setState("power",3);
        Role.createRole("pyungmin","§7평민§f ",1).setState("power",2); // TODO 이거 필요 인원 다시 2명으로 바꾸기
        Role.createRole("cheonmin","§8천민§f ",0).setState("power",1);
        Role.createRole("nobi","§8노비 ",0).setState("power",0);

        // 상점 등록
        Shop shop = new Shop("king","상점");
        shop.addNPCName("§l상인");

        ItemStack slot1 = GameItem.get(GameItemType.SHOP_ANNOUNCE_LOCATION);

        ItemStack slot2 = GameItem.get(GameItemType.SHOP_SPEED);

        ItemStack slot3 = GameItem.get(GameItemType.SHOP_RANDOM_TP);

        ItemStack slot4 = GameItem.get(GameItemType.SHOP_SWORD);

        shop.addGUIItem(slot1,10);
        shop.addGUIItem(slot2,12);
        shop.addGUIItem(slot3,14);
        shop.addGUIItem(slot4,16);

        shop.register();

        // 양반한테 옆전 주는 타이머 시작
        GameTimer.StartYangbanTimer();

    }

    @Override
    public void onDisable() {

    }

    public static void Start() {

        Timer.countDown(3, () -> {

            // 역할 랜덤 분배
            Role.applyRandomAll(Role.getByName("nobi"));

            // 게임 시간 제한 타이머 시작
            GameTimer.Start();

            // 바닥에 옆전 떨구기 시작 TODO 여기 지역 주석 풀어야함
            //Region.findByName("king").getDropItem().StartDrop(true);

            Bukkit.getOnlinePlayers().forEach(p -> {

                if(p.getGameMode() != GameMode.SPECTATOR) {

                    // 랜덤 좌표로 TP
                    respawn(p);

                    // 인벤 초기화
                    p.getInventory().clear();

                    // 체력 만땅
                    p.setHealth(20);

                    // 허기 만땅
                    p.setFoodLevel(20);

                    // 속도 기본으로 설정
                    p.setWalkSpeed(0.2f);

                    // 기본템 지급 (천민: 철 검,
                    if(Role.getByPlayer(p).getName().equals("cheonmin")) {

                        p.getInventory().addItem( Itemf.createItemStack(Material.IRON_SWORD,1,"§l민초의 난",null) );

                    } else {

                        p.getInventory().addItem( Itemf.createItemStack(Material.STONE_SWORD,1,"§l단단한 검",Arrays.asList("","§7§o돌로 만들어진, 간단한 돌 검입니다.","")) );

                    }

                }

            });

        });
    }

    public static void Stop(Role winRole) {

        // 타이머 종료
        Timer.findByName("king").stop();

        // 왕이 이김
        switch (winRole.getName()) {
            case "king":

                Playerf.sendTitleAll("§l게임종료!","§6§l"+winRole.getPlayers().get(0).getName()+"§f(이)가 마지막 왕으로 기억될 것 입니다.");
                break;

                // 천민이 왕 죽여서 혁명엔딩
            case "nobi":
                Playerf.sendTitleAll("§6§l혁명!","아무것도 아니었던 이들이 세상의 주인이 될 것 입니다.");

                // 천민 출력
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage("§c   〈 혁명을 이룩한 노비들 〉");

                winRole.getPlayers().forEach(p -> {
                    Bukkit.broadcastMessage("§l - "+p.getName());
                });

                Bukkit.broadcastMessage("");
                break;
        }

    }

    public static void checkKingWin() {

        AtomicInteger i = new AtomicInteger(0);

        Bukkit.getOnlinePlayers().forEach(p -> {

            if(p.getGameMode() != GameMode.SPECTATOR)
                i.getAndIncrement();

        });

        Stop(Role.getByName("king"));
    }
    public static boolean canKingKillEveryone() {

        // 천민이 없고 노비만 있으면 됨.
        return Role.getByName("cheonmin").getPlayers().isEmpty();

    }

    public static void respawn(Player player) {

        player.teleport(SpawnLoc.get(new Random().nextInt(SpawnLoc.size()-1)));

    }

    public static void out(Player player) {

        player.setGameMode(GameMode.SPECTATOR);
        player.sendTitle("","당신은 탈락했습니다.",0,20,15);

    }

    public static void gotoWaitRoom(Player player) {

        player.sendMessage("당신은 §730초§f동안 §7신분 교체의 방§f에 갇히게 됩니다.");

        BukkitTaskf.wait(() -> respawn(player),20*30);
    }
}
