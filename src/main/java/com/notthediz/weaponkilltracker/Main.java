//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
package com.notthediz.weaponkilltracker;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Material;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener {

    // =========================
    // DATA STORAGE
    // =========================
    private final Map<UUID, Integer> kills = new HashMap<>();
    private final Map<UUID, Map<String, Integer>> weaponKills = new HashMap<>();

    // =========================
    // FILE STORAGE
    // =========================
    private File statsFile;
    private FileConfiguration statsConfig;

    // =========================
    // ENABLE PLUGIN
    // =========================
    @Override
    public void onEnable() {
        getLogger().info("WeaponKillTracker enabled!");

        getServer().getPluginManager().registerEvents(this, this);

        if (getCommand("stats") != null) {
            getCommand("stats").setExecutor(new StatsCommand(this));
        }

        setupStatsFile();
        loadStats();
    }

    // =========================
    // FILE SETUP
    // =========================
    private void setupStatsFile() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        statsFile = new File(getDataFolder(), "stats.yml");

        if (!statsFile.exists()) {
            try {
                statsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        statsConfig = YamlConfiguration.loadConfiguration(statsFile);
    }

    // =========================
    // LOAD DATA
    // =========================
    private void loadStats() {
        if (statsConfig.getConfigurationSection("kills") == null) return;

        for (String uuidString : statsConfig.getConfigurationSection("kills").getKeys(false)) {

            UUID uuid = UUID.fromString(uuidString);
            int killCount = statsConfig.getInt("kills." + uuidString);

            kills.put(uuid, killCount);

            if (statsConfig.getConfigurationSection("weapons." + uuidString) != null) {

                for (String weaponKey : statsConfig.getConfigurationSection("weapons." + uuidString).getKeys(false)) {

                    int count = statsConfig.getInt("weapons." + uuidString + "." + weaponKey);

                    weaponKills.putIfAbsent(uuid, new HashMap<>());
                    weaponKills.get(uuid).put(weaponKey, count);
                }
            }
        }
    }

    // =========================
    // SAVE DATA
    // =========================
    private void saveStats() {

        if (statsConfig == null || statsFile == null) return;

        for (UUID uuid : kills.keySet()) {
            statsConfig.set("kills." + uuid, kills.get(uuid));
        }

        for (UUID uuid : weaponKills.keySet()) {
            for (Map.Entry<String, Integer> entry : weaponKills.get(uuid).entrySet()) {
                statsConfig.set(
                        "weapons." + uuid + "." + entry.getKey(),
                        entry.getValue()
                );
            }
        }

        try {
            statsConfig.save(statsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // =========================
    // DISABLE PLUGIN
    // =========================
    @Override
    public void onDisable() {
        saveStats();
    }

    // =========================
    // KILL EVENT
    // =========================
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {

        LivingEntity victim = event.getEntity();
        Player killer = victim.getKiller();

        if (killer == null) return;

        UUID id = killer.getUniqueId();

        // ---- GLOBAL KILLS ----
        kills.put(id, kills.getOrDefault(id, 0) + 1);

        // ---- WEAPON TRACKING ----
        Material weapon = killer.getInventory().getItemInMainHand().getType();

        String weaponKey;

        if (weapon == Material.AIR) {
            weaponKey = "AIR";
        } else {
            weaponKey = weapon.name();
        }

        weaponKills.putIfAbsent(id, new HashMap<>());
        Map<String, Integer> weapons = weaponKills.get(id);

        weapons.put(weaponKey, weapons.getOrDefault(weaponKey, 0) + 1);

        killer.sendMessage("§aTracked kill! Total: " + kills.get(id));
    }

    // =========================
    // GETTERS (FOR COMMANDS)
    // =========================
    public Map<UUID, Integer> getKills() {
        return kills;
    }

    public Map<UUID, Map<String, Integer>> getWeaponKills() {
        return weaponKills;
    }
}