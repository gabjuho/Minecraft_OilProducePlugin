package com.gabjuho.oilproduceplugin;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class PlayerOilProducerManager {
    private final static HashMap<UUID,Integer> oilCoolTime = new HashMap<>();
    private final static HashMap<UUID, BukkitTask> coolTimeTask = new HashMap<>();
    private final static YamlManager yamlManager = YamlManager.getInstance();

    private PlayerOilProducerManager(){
    }
    public static void ConsumingOilCoolTime(UUID uuid){
        int coolTime = oilCoolTime.get(uuid);

        if(coolTime == 0){
            coolTimeTask.get(uuid).cancel();
            coolTimeTask.remove(uuid);
            oilCoolTime.remove(uuid);
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
