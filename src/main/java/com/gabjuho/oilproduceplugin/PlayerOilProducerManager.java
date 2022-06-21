package com.gabjuho.oilproduceplugin;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerOilProducerManager {
    private HashMap<Player,Integer> OilCoolTime;

    public HashMap<Player,Integer> getOilCoolTime(){
        return OilCoolTime;
    }
}
