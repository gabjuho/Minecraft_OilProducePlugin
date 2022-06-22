package com.gabjuho.oilproduceplugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class YamlManager {
    private final Main plugin; // 메인클래스 객체
    private static YamlManager instance = new YamlManager(Main.getPlugin(Main.class)); //yaml 매니저는 싱글톤으로 생성
    private FileConfiguration loc = null;
    private FileConfiguration coolTime = null;
    private FileConfiguration offlineOilList = null;
    private File locFile = null;
    private File coolTimeFile = null;
    private File offlineOilListFile = null;

    private YamlManager(Main plugin) // 생성자 막아놓음 (싱글톤 패턴으로 인한)
    {
        this.plugin = plugin;
        saveDefaultLoc();
        saveDefaultCoolTime();
        saveDefaultOfflineOilList();
    }

    public static YamlManager getInstance() // 싱글톤 인스턴스 반환
    {
        if(instance == null)
            instance = new YamlManager(Main.getPlugin(Main.class));
        return instance;
    }

    public void reloadLoc() // 데이터 리로드
    {
        if(this.locFile == null)
            this.locFile = new File(this.plugin.getDataFolder(), "loc.yml");

        this.loc = YamlConfiguration.loadConfiguration(this.locFile);
        InputStream defaultStream = this.plugin.getResource("loc.yml");
        if(defaultStream != null)
        {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.loc.setDefaults(defaultConfig);
        }
    }

    public void reloadCoolTime() // 데이터 리로드
    {
        if(this.coolTimeFile == null)
            this.coolTimeFile = new File(this.plugin.getDataFolder(), "coolTime.yml");

        this.coolTime = YamlConfiguration.loadConfiguration(this.coolTimeFile);
        InputStream defaultStream = this.plugin.getResource("loc.yml");
        if(defaultStream != null)
        {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.coolTime.setDefaults(defaultConfig);
        }
    }

    public void reloadOfflineOilList() // 데이터 리로드
    {
        if(this.offlineOilListFile == null)
            this.offlineOilListFile = new File(this.plugin.getDataFolder(), "offlineOilList.yml");

        this.offlineOilList = YamlConfiguration.loadConfiguration(this.offlineOilListFile);
        InputStream defaultStream = this.plugin.getResource("offlineOilList.yml");
        if(defaultStream != null)
        {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.offlineOilList.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getLoc()
    {
        if(this.loc == null)
            reloadLoc();

        return this.loc;
    }

    public FileConfiguration getCoolTime()
    {
        if(this.coolTime == null)
            reloadCoolTime();

        return this.coolTime;
    }

    public FileConfiguration getOfflineOilList()
    {
        if(this.offlineOilList == null)
            reloadOfflineOilList();

        return this.offlineOilList;
    }

    public void saveLoc() // 데이터 저장
    {
        if(this.loc == null || this.locFile == null)
            return;

        try
        {
            this.getLoc().save(this.locFile);
        }catch(IOException e)
        {
            plugin.getLogger().log(Level.SEVERE,"data 파일이 저장되지 않았습니다. -> " + this.locFile,e);
        }
    }

    public void saveCoolTime() // 데이터 저장
    {
        if(this.coolTime == null || this.coolTimeFile == null)
            return;

        try
        {
            this.getCoolTime().save(this.coolTimeFile);
        }catch(IOException e)
        {
            plugin.getLogger().log(Level.SEVERE,"data 파일이 저장되지 않았습니다. -> " + this.coolTimeFile,e);
        }
    }

    public void saveOfflineOilList() // 데이터 저장
    {
        if(this.offlineOilList == null || this.offlineOilListFile == null)
            return;

        try
        {
            this.getOfflineOilList().save(this.offlineOilListFile);
        }catch(IOException e)
        {
            plugin.getLogger().log(Level.SEVERE,"data 파일이 저장되지 않았습니다. -> " + this.offlineOilListFile,e);
        }
    }

    public void saveDefaultLoc() // 데이터 디폴트 저장
    {
        if(this.locFile == null)
            this.locFile = new File(this.plugin.getDataFolder(), "loc.yml");

        if(!this.locFile.exists())
        {
            this.plugin.saveResource("loc.yml",false);
        }
    }

    public void saveDefaultCoolTime() // 데이터 디폴트 저장
    {
        if(this.coolTimeFile == null)
            this.coolTimeFile = new File(this.plugin.getDataFolder(), "coolTime.yml");

        if(!this.coolTimeFile.exists())
        {
            this.plugin.saveResource("coolTime.yml",false);
        }
    }

    public void saveDefaultOfflineOilList() // 데이터 디폴트 저장
    {
        if(this.offlineOilListFile == null)
            this.offlineOilListFile = new File(this.plugin.getDataFolder(), "offlineOilList.yml");

        if(!this.offlineOilListFile.exists())
        {
            this.plugin.saveResource("offlineOilList.yml",false);
        }
    }

}
