package com.gabjuho.oilproduceplugin;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class PlayerOilProducerManager {
    private final static HashMap<Player,Integer> oilCoolTime = new HashMap<>();
    private final static HashMap<Player, BukkitTask> coolTimeTask = new HashMap<>();
    private final static YamlManager yamlManager = YamlManager.getInstance();

    private PlayerOilProducerManager(){
    }
    public static void ConsumingOilCoolTime(Player player){
        int coolTime = oilCoolTime.get(player);

        if(coolTime == 0){
            coolTimeTask.get(player).cancel();
            coolTimeTask.remove(player);
            oilCoolTime.remove(player);
            return;
        }
        player.sendMessage(""+coolTime);
        coolTime -= 1;
        oilCoolTime.put(player,coolTime);
    }
    public static void AddOilCoolTime(Player player){
        oilCoolTime.put(player,10);
    }
    public static void AddCoolTimeTask(Player player,BukkitTask task){
        coolTimeTask.put(player,task);
    }

    public static HashMap<Player,Integer> getOilCoolTime(){
        return oilCoolTime;
    }
}
