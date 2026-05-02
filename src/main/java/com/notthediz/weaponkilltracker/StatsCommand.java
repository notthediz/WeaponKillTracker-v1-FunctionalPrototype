package com.notthediz.weaponkilltracker;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class StatsCommand implements CommandExecutor {

    private final Main plugin;

    public StatsCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        UUID id = player.getUniqueId();

        int kills = plugin.getKills().getOrDefault(id, 0);

        player.sendMessage("§6--- Your Stats ---");
        player.sendMessage("Kills: " + kills);

        Map<String, Integer> weapons = plugin.getWeaponKills().get(id);

        if (weapons != null && !weapons.isEmpty()) {
            player.sendMessage("§eWeapons:");

            for (Map.Entry<String, Integer> entry : weapons.entrySet()) {
                player.sendMessage(" - " + entry.getKey() + ": " + entry.getValue());
            }
        }

        return true;
    }
}