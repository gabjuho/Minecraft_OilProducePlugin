package com.gabjuho.oilproduceplugin.events;

import com.gabjuho.oilproduceplugin.Main;
import com.gabjuho.oilproduceplugin.PlayerOilProducerManager;
import com.gabjuho.oilproduceplugin.YamlManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;

public class Event implements Listener {

    YamlManager yamlManager = YamlManager.getInstance();

    @EventHandler
    void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(yamlManager.getOfflineOilList().contains(player.getUniqueId().toString())) {
            yamlManager.getOfflineOilList().set(player.getUniqueId().toString(), null);
            player.sendMessage(ChatColor.DARK_GRAY + "석유 생성이 완료되었지만, "+player.getName()+"님께서 오프라인 상태였기에 석유를 받지 못하였습니다.");
            yamlManager.saveOfflineOilList();
        }
    }

    @EventHandler
    void onBlockPlaceEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        Location loc1 = null, loc2 = null, loc3 = null, loc4 = null;

        if (!player.getWorld().getName().equals("world")) //후차에 변경해야함.
            return;

        if (event.getBlock().getType() == Material.CRAFTING_TABLE) {
            loc1 = event.getBlock().getLocation(); // craftTable loc
            loc2 = new Location(world, loc1.getBlockX(), loc1.getBlockY() - 1, loc1.getBlockZ()); // dropper loc
            loc3 = new Location(world, loc1.getBlockX(), loc1.getBlockY() - 2, loc1.getBlockZ()); // hopper loc
            loc4 = new Location(world, loc1.getBlockX(), loc1.getBlockY() - 3, loc1.getBlockZ()); // bedrock loc
        } else if (event.getBlock().getType() == Material.DROPPER) {
            loc2 = event.getBlock().getLocation(); // dropper loc
            loc1 = new Location(world, loc2.getBlockX(), loc2.getBlockY() + 1, loc2.getBlockZ()); // craftTable loc
            loc3 = new Location(world, loc2.getBlockX(), loc2.getBlockY() - 1, loc2.getBlockZ()); // hopper loc
            loc4 = new Location(world, loc2.getBlockX(), loc2.getBlockY() - 2, loc2.getBlockZ()); // bedrock loc
        } else if (event.getBlock().getType() == Material.HOPPER) {
            loc3 = event.getBlock().getLocation(); // hopper loc
            loc1 = new Location(world, loc3.getBlockX(), loc3.getBlockY() + 2, loc3.getBlockZ()); // craftTable loc
            loc2 = new Location(world, loc3.getBlockX(), loc3.getBlockY() + 1, loc3.getBlockZ()); // dropper loc
            loc4 = new Location(world, loc3.getBlockX(), loc3.getBlockY() - 1, loc3.getBlockZ()); // bedrock loc
        }
        if (loc1 == null)
            return;

        if (loc1.getBlock().getType() == Material.CRAFTING_TABLE && loc2.getBlock().getType() == Material.DROPPER && loc3.getBlock().getType() == Material.HOPPER && loc4.getBlock().getType() == Material.BEDROCK) {
            if (yamlManager.getLoc().contains(player.getUniqueId().toString())) {
                player.sendMessage(ChatColor.RED + "이미 소유하고 있는 석유 생성기가 있습니다.");
                event.setCancelled(true);
                return;
            }
            yamlManager.getLoc().set(player.getUniqueId().toString(), loc3);
            yamlManager.saveLoc();
            player.sendMessage(ChatColor.GREEN + "석유 생성기가 만들어졌습니다!!");
        }
    }

    @EventHandler
    void onInsertCoalEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getCursor() == null || event.getClickedInventory() == null)
            return;

        if (event.getClickedInventory().getType() == InventoryType.HOPPER && event.getCursor().getType() == Material.COAL && event.getCursor().getAmount() == 1 && !event.getCursor().hasItemMeta()) {
            if (Objects.equals(yamlManager.getLoc().getLocation(player.getUniqueId().toString()), event.getClickedInventory().getLocation())) {
                if (!PlayerOilProducerManager.getOilCoolTime().containsKey(player.getUniqueId())) {
                    Bukkit.getScheduler().runTask(Main.getPlugin(Main.class), () -> useCoalInOilProducer(event.getClickedInventory()));
                    PlayerOilProducerManager.AddOilCoolTime(player);
                    BukkitTask task = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(Main.class), () -> PlayerOilProducerManager.ConsumingOilCoolTime(player.getUniqueId()), 0, 20);
                    PlayerOilProducerManager.AddCoolTimeTask(player.getUniqueId(), task);
                    player.sendMessage(ChatColor.GREEN + "석유 생성기가 가동되었습니다. 30분 후에 석유가 생성됩니다."); //후차에 석유 생성기가 가동중일 때, 석유를 못넣게 해야함. 혹은 자동으로 석탄이 있으면 빨아먹게 하던지
                    player.sendMessage(ChatColor.GRAY + "(30분 후에 플레이어가 접속되어 있지 않는 경우 석유를 받지 못합니다.)");
                }
            }
        } else if (event.getClickedInventory().getType() == InventoryType.HOPPER && event.getCursor().getType() == Material.COAL && event.getCursor().getAmount() != 1 && !event.getCursor().hasItemMeta()) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "석유를 하나만 넣어주세요.");
        }
    }

    @EventHandler
    void onOpenInventoryEvent(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        if (yamlManager.getLoc().contains(player.getUniqueId().toString())) { //플레이어가 석유 생성기를 가지고 있을 때
            if (!Objects.equals(event.getInventory().getLocation(), yamlManager.getLoc().getLocation(player.getUniqueId().toString()))) {
                boolean isOilHopper = false; //오일 호퍼로서 존재하는 호퍼인가, 일반 호퍼인가 구분해주는 bool값
                for (String uuid : yamlManager.getLoc().getKeys(true)) {
                    if (Objects.equals(event.getInventory().getLocation(), yamlManager.getLoc().getLocation(uuid)))
                        isOilHopper = true;
                }
                if (isOilHopper) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "다른 플레이어의 석유 생성기입니다.");
                }
            }
        } else { //플레이어가 석유 생성기를 가지고 있지 않을 때
            boolean isOilHopper = false;
            for (String uuid : yamlManager.getLoc().getKeys(true)) {
                if (Objects.equals(event.getInventory().getLocation(), yamlManager.getLoc().getLocation(uuid)))
                    isOilHopper = true;
            }
            if (isOilHopper) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "다른 플레이어의 석유 생성기입니다.");
            }
        }
    }

    @EventHandler
    void onBreakOilProducer(BlockBreakEvent event) {

        Player player = event.getPlayer();

        if (event.getBlock().getType() == Material.HOPPER || event.getBlock().getType() == Material.DROPPER || event.getBlock().getType() == Material.CRAFTING_TABLE) {
            for (String uuid : yamlManager.getLoc().getKeys(true)) {
                if (event.getBlock().getLocation().getBlockX() == yamlManager.getLoc().getLocation(uuid).getBlockX() && event.getBlock().getLocation().getBlockZ() == yamlManager.getLoc().getLocation(uuid).getBlockZ()) {
                    if (event.getBlock().getLocation().getBlockY() == yamlManager.getLoc().getLocation(uuid).getBlockY() || event.getBlock().getLocation().getBlockY() == yamlManager.getLoc().getLocation(uuid).getBlockY() + 1 || event.getBlock().getLocation().getBlockY() == yamlManager.getLoc().getLocation(uuid).getBlockY() + 2) {
                        if (player.getUniqueId().toString().equals(uuid)) {
                            player.sendMessage(ChatColor.GRAY + "플레이어의 석유 생성기가 파괴되었습니다.");
                            yamlManager.getLoc().set(player.getUniqueId().toString(), null);
                            yamlManager.saveLoc();
                            return;
                        }
                        event.setCancelled(true);
                        player.sendMessage(ChatColor.RED + "다른 플레이어의 석유 생성기이므로, 파괴가 불가능합니다.");
                    }
                }
            }
        }
    }

    private void useCoalInOilProducer(Inventory hopper) {
        for (ItemStack item : hopper.getContents()) {
            if (item == null)
                continue;
            if (item.getType() == Material.COAL)
                item.setAmount(item.getAmount() - 1);
        }
    }

}
