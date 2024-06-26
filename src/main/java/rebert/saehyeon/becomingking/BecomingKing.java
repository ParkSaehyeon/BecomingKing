package rebert.saehyeon.becomingking;

import me.saehyeon.saehyeonlib.main.SaehyeonLibEvent;
import me.saehyeon.saehyeonlib.region.Region;
import me.saehyeon.saehyeonlib.role.ExPlayer;
import me.saehyeon.saehyeonlib.role.Role;
import me.saehyeon.saehyeonlib.shop.Shop;
import me.saehyeon.saehyeonlib.timer.Timer;
import me.saehyeon.saehyeonlib.util.BukkitTaskf;
import me.saehyeon.saehyeonlib.util.Itemf;
import me.saehyeon.saehyeonlib.util.Locationf;
import me.saehyeon.saehyeonlib.util.Playerf;
import org.bukkit.*;
import org.bukkit.block.Chest;
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

    public static ArrayList<Location> SpawnLoc = new ArrayList<>();

    public static BecomingKing ins;

    @Override
    public void onEnable() {
        ins = this;

        // 이벤트 등록
        Bukkit.getPluginManager().registerEvents(new onDeath(), this);
        Bukkit.getPluginManager().registerEvents(new onClick(), this);
        Bukkit.getPluginManager().registerEvents(new onMove(), this);
        Bukkit.getPluginManager().registerEvents(new other(), this);
        Bukkit.getPluginManager().registerEvents(new other(), this);
        Bukkit.getPluginManager().registerEvents(new onGUI(), this);
        Bukkit.getPluginManager().registerEvents(new onDamage(), this);

        Bukkit.getPluginCommand("king").setExecutor(new onCommand());

        SaehyeonLibEvent.register(new GameTimerEvent());
        SaehyeonLibEvent.register(new onCoolTime());
        SaehyeonLibEvent.register(new onShop());
        SaehyeonLibEvent.register(new onRole());

        // 역할 등록
        Role.createRole("king","§e§l왕§f ",1).setState("power",5);
        Role.createRole("yangban","§6§l양반§f ",1).setState("power",4);
        Role.createRole("sunbi","§f§l선비§f ",1).setState("power",3);
        Role.createRole("pyungmin","§7평민§f ",2).setState("power",2); // TODO 이거 필요 인원 다시 2명으로 바꾸기
        Role.createRole("cheonmin","§8천민§f ",0).setState("power",1);
        Role.createRole("nobi","§8노비 ",0).setState("power",0);

        // 상점 등록
        Shop shop = new Shop("king","상점",5);
        shop.addNPCName("§l상인");

        ItemStack slot1 = GameItem.get(GameItemType.ANNOUNCE_LOCATION);
        Itemf.addLore(slot1, "", "§f  〉§610냥§f을 소비하여 구매합니다.", "");

        ItemStack slot2 = GameItem.get(GameItemType.SPEED);
        Itemf.addLore(slot2, "","§f  〉§615냥§f을 소비하여 구매합니다.","");

        ItemStack slot3 = GameItem.get(GameItemType.RANDOM_TP);
        Itemf.addLore(slot3, "","§f  〉§620냥§f을 소비하여 구매합니다.","");

        ItemStack slot4 = GameItem.get(GameItemType.SWORD);
        Itemf.addLore(slot4, "","§f  〉§620냥§f을 소비하여 구매합니다.","");

        ItemStack slot5 = GameItem.get(GameItemType.HEAL);
        Itemf.addLore(slot5, "","§f  〉§610냥§f을 소비하여 구매합니다.","");

        shop.addGUIItem(slot1,10);
        shop.addGUIItem(slot2,12);
        shop.addGUIItem(slot3,14);
        shop.addGUIItem(slot4,16);
        shop.addGUIItem(slot5,28);

        shop.register();

        // 양반한테 옆전 주는 타이머 시작
        GameTimer.StartRoleTimer();

        // 역할 엑션바 띄우는 타이머 시작
        GameTimer.StartRoleActionbarTimer();

        // 노비 타이머 시작
        GameTimer.StartRevolutionTimer();

    }

    @Override
    public void onDisable() {

    }

    public static void Start() {

        World world = Bukkit.getWorld("world");

        // 스폰좌표 설정
        SpawnLoc = new ArrayList<>(Arrays.asList(
                new Location(world,-36,64,201),
                new Location(world,59,65,214),
                new Location(world,72,66,168),
                new Location(world,34,66,136),
                new Location(world,19,71,76),
                new Location(world,-35,66,76),
                new Location(world,-76,67,91),
                new Location(world,-74,66,118),
                new Location(world,-70,64,141),
                new Location(world,-75,64,199),
                new Location(world,-82,64,251),
                new Location(world,-15,65,263),
                new Location(world,21,64,263),
                new Location(world,81,67,291),
                new Location(world,183,64,295),
                new Location(world,199,64,241)
        ));

        Timer.countDown(3, () -> {

            // 게임 시간 제한 타이머 시작
            GameTimer.Start();

            // 바닥에 옆전 떨구기 시작 TODO 여기 지역 주석 풀어야함
            Region.findByName("king").getDropItem().StartDrop(true);

            Bukkit.getOnlinePlayers().forEach(p -> {

                if(!ExPlayer.contains(p)) {

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

                    // 게임모드 설정
                    p.setGameMode(GameMode.ADVENTURE);

                }

            });

            // 혁명가의 검 리젠
            regenRevolutionSword();

            // 역할 랜덤 분배
            Role.applyRandomAll(Role.getByName("nobi"));

            // 대사 출처 밝히기
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("§c§l주의: 본 컨텐츠에서 사용된 모든 대사는 M으로부터 창시되었습니다.");
            Bukkit.broadcastMessage("");


        });
    }

    public static void Stop(Role winRole) {

        // 타이머 종료
        if(Timer.findByName("king") != null)
            Timer.findByName("king").setPause(true);

        // 소리 내기
        Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(),Sound.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.MASTER,1,1));

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
                Bukkit.broadcastMessage("§c§l   〈 혁명을 이룩한 노비들 〉");
                Bukkit.broadcastMessage("");

                winRole.getPlayers().forEach(p -> {
                    Bukkit.broadcastMessage("§f - "+p.getName());
                });

                Bukkit.broadcastMessage("");
                break;
        }

    }

    public static void checkKingWin() {

        Player winner = null;

        for(Player p : Bukkit.getOnlinePlayers()) {
            if(p.getGameMode() != GameMode.SPECTATOR) {

                if(winner != null)
                    return;

                winner = p;

            }
        }

        // 마지막까지 남은 한 사람을 왕으로 추대
        Role.getByName("king").add(winner);

        // 왕이 이긴 게임으로 종료
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
        player.sendTitle("","당신은 탈락했습니다.",0,40,15);

        Role playerRole = Role.getByPlayer(player);

        playerRole.remove(player);

        checkKingWin();

        // 이 역할이 천민이 아니라면 탈락자의 역할의 아래 단계의 역할을 가진 플레이어 중
        // 각 역할에서 랜덤으로 한 명이 다음 역할로 올라가기

        // 혁명가의 검을 들고 있는 사람이 탈락됨 -> 검 리젠
        if(player.getInventory().contains(Material.DIAMOND_SWORD))
            regenRevolutionSword();

        if(!playerRole.getName().equals("cheonmin") && !playerRole.getName().equals("nobi")) {

            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(playerRole.getPrefix().replace(" ", "")+"§f의 자리가 공백이 됨에 따라 직급 변동이 발생했습니다.");

            ArrayList<Role> roles = Role.getRoles();
            roles.removeIf(role -> (int) role.getState("power") >= (int) playerRole.getState("power"));
            roles.removeIf(role -> role.getName().equals("nobi"));

            boolean isChanged = false;

            for (Role role : roles) {

                if(role.getPlayers().isEmpty())
                    continue;

                isChanged = true;
                int randomIndex = role.getPlayers().size() > 1 ? new Random().nextInt(role.getPlayers().size() - 1) : 0;

                Player target   = new ArrayList<>(role.getPlayers()).get(randomIndex);
                Role oldRole    = Role.getByPlayer(target);
                Role nextRole   = Role.getByState("power", (int) role.getState("power") + 1).get(0);

                nextRole.add(target);

                Bukkit.broadcastMessage(" 〉 §f"+target.getName()+": "+oldRole.getPrefix()+"§f→ "+nextRole.getPrefix());

            }

            if(!isChanged) {
                Bukkit.broadcastMessage("§7변동 사항이 없습니다.");
            }

            Bukkit.broadcastMessage("");

        }

    }

    public static void gotoWaitRoom(Player player) {

        player.sendMessage("당신은 §730초§f동안 §7신분 교체의 방§f에 갇히게 됩니다.");

        player.teleport( new Location(player.getWorld(),-65,81,207) );

        BukkitTaskf.wait(() -> respawn(player),20*30);
    }

    // 혁명 준비 장소로 이동
    public static void checkAndGotoRevolution(Player player) {
        Role role = Role.getByPlayer(player);

        if(role != null && !role.getName().equals("nobi"))
            return;

        if(player.getLocation().clone().add(0,-1,0).getBlock().getType() == Material.GOLD_BLOCK) {
            player.teleport( new Location(player.getWorld(), 15,66, 369) );
            player.sendTitle("","§c§l혁명 준비 장소§f에 도착했습니다.");
        }

        if(Locationf.equal( new Location(player.getWorld(), 14,66,373) ,player.getLocation().toBlockLocation())) {
            player.teleport( new Location(player.getWorld(), 58,93,340) );
            player.sendTitle("","§c§l혁명 준비 장소§f에서 나왔습니다.");
        }
    }

    public static void regenRevolutionSword() {

        Chest chest = (Chest)( new Location(Bukkit.getWorld("world"), 23,56,421) ).getBlock().getState();
        chest.getInventory().clear();
        chest.getInventory().setItem(13,GameItem.get(GameItemType.REVOLUTION_SWORD));


    }
}
