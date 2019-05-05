package me.fluxcapacitor.dragoninventoryfull;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    static String fullPlaceholderMsg, notFullPlaceholderMsg, percentFullPlaceholderMsg;
    private FileConfiguration config = getConfig();

    static boolean isFull(Player player) {
        return player.getInventory().firstEmpty() == -1;
    }

    private static boolean isFull(PlayerInventory inventory) {
        return inventory.firstEmpty() == -1;
    }

    static int percentFull(Player player) {
        int totalSlots = 0, fullSlots = 0;
        PlayerInventory inv = player.getInventory();
        ItemStack[] contents = inv.getContents();
        for (ItemStack stack : contents) {
            totalSlots++;
            if (stack != null) {
                fullSlots++;
            }
        }
        return Math.round((fullSlots / totalSlots) * 100);
    }

    @Override
    public void onEnable() {
        config.addDefault("forceGamemode.enable", true);
        config.addDefault("forceGamemode.gamemode", GameMode.SURVIVAL.name());

        config.addDefault("messages.chat.enable", true);
        config.addDefault("messages.title.enable", true);
        config.addDefault("messages.sound.enable", true);

        config.addDefault("messages.chat.message", "&cYour inventory is full!");
        config.addDefault("messages.title.title", "&cYour inventory is full!");
        config.addDefault("messages.title.subtitle", "&7Type /sell to sell your items.");
        config.addDefault("messages.sound.sound", Sound.NOTE_PLING.name());
        config.addDefault("messages.sound.volume", 1.0);
        config.addDefault("messages.sound.pitch", 1.0);

        config.addDefault("placeholderapi.fullMsg", "&cFull");
        config.addDefault("placeholderapi.notFullMsg", "&aNot full");
        config.addDefault("placeholderapi.percentFullMsg", "&3{PERCENT}% full");

        config.options().copyDefaults(true);
        saveConfig();

        fullPlaceholderMsg = config.getString("placeholderapi.fullMsg");
        percentFullPlaceholderMsg = config.getString("placeholderapi.percentFullMsg");
        notFullPlaceholderMsg = config.getString("placeholderapi.notFullMsg");

        //Register the event listener
        Bukkit.getPluginManager().registerEvents(this, this);

        //Register PAPI hook
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderHook().register();
        }
    }

    @Override
    public void onDisable() {
    }

    private String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    @EventHandler
    public boolean onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inv = player.getInventory();
        if (isFull(inv)) {
            if (config.getBoolean("forceGamemode.enable") && player.getGameMode() == GameMode.valueOf(config.getString("forceGamemode.gamemode"))) {
                if (config.getBoolean("messages.chat.enable"))
                    player.sendMessage(colorize(config.getString("messages.chat.message")));
                if (config.getBoolean("messages.title.enable"))
                    player.sendTitle(colorize(config.getString("messages.title.title")), colorize(config.getString("messages.title.subtitle")));
                if (config.getBoolean("messages.sound.enable")) {
                    player.playSound(player.getLocation(), Sound.valueOf(config.getString("messages.sound.sound")), (float) config.getInt("messages.sound.volume"), (float) config.getInt("messages.sound.pitch"));
                }
            }
        }
        return true;
    }
}
