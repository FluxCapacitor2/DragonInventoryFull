package me.fluxcapacitor.dragoninventoryfull;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlaceholderHook extends PlaceholderExpansion {
    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getAuthor() {
        return "FluxCapacitor";
    }

    @Override
    public String getIdentifier() {
        return "invfull";
    }

    @Override
    public String getVersion() {
        return "0.1";
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {
        if (identifier.equalsIgnoreCase("full")) {
            if (Main.isFull((Player) player)) return Main.fullPlaceholderMsg;
            else return Main.notFullPlaceholderMsg;
        }

        if (identifier.equalsIgnoreCase("percentFull")) {
            return Main.percentFullPlaceholderMsg.replaceAll("%percent%", Main.percentFull((Player) player) + "");
        }

        return null;
    }
}
