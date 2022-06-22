package com.gabjuho.oilproduceplugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class PlayerOilProducerManager {
    private final static HashMap<UUID,Integer> oilCoolTime = new HashMap<>();
    private final static HashMap<UUID, BukkitTask> coolTimeTask = new HashMap<>();

    private PlayerOilProducerManager(){
    }
    public static void ConsumingOilCoolTime(UUID uuid){
        int coolTime = oilCoolTime.get(uuid);

        if(coolTime == 0){
            coolTimeTask.get(uuid).cancel();
            coolTimeTask.remove(uuid);
            oilCoolTime.remove(uuid);

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            if(offlinePlayer.isOnline()){
                Player player = Bukkit.getPlayer(uuid);
                if(player != null) {
                    ItemStack oil = new ItemStack(Material.COAL);
                    ItemMeta oilMeta = oil.getItemMeta();
                    oilMeta.setDisplayName("석유");
                    oil.setItemMeta(oilMeta);
                    Inventory inv = player.getInventory();
                    boolean isEmpty = false;
                    if(inv.firstEmpty() == -1){
                        for (ItemStack invItem : inv.getContents()) {
                            if(invItem == null)
                                continue;
                            if(!invItem.hasItemMeta())
                                continue;
                            if(invItem.getType() == oil.getType() && invItem.getItemMeta().equals(oil.getItemMeta()) && invItem.getAmount() < invItem.getMaxStackSize()){
                                player.getInventory().addItem(oil);
                                player.sendMessage(ChatColor.GREEN + "석유가 생산되어 "+player.getName()+"님의 인벤토리에 들어갔습니다.");
                                isEmpty = true;
                                break;
                            }
                        }
                        if(!isEmpty)
                            player.sendMessage(ChatColor.RED + player.getName() + "님의 인벤토리가 꽉 찾으므로, 생산된 석유가 지급되지 않았습니다.");
                    }else {
                        player.getInventory().addItem(oil);
                        player.sendMessage(ChatColor.GREEN + "석유가 생산되어 "+player.getName()+"님의 인벤토리에 들어갔습니다.");
                    }


                }
            }
            return;
        }
        coolTime -= 1;
        oilCoolTime.put(uuid,coolTime);
    }
    public static void AddOilCoolTime(Player player){
        UUID uuid = player.getUniqueId();
        oilCoolTime.put(uuid,10);
    }
    public static void AddCoolTimeTask(UUID uuid,BukkitTask task){
        coolTimeTask.put(uuid,task);
    }

    public static HashMap<UUID,Integer> getOilCoolTime(){
        return oilCoolTime;
    }
}
