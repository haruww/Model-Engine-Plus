package me.haru301.megplus;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.UUID;

public class EventHandle implements Listener
{
    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent e)
    {
        Player p = e.getPlayer();
        Entity entity = e.getRightClicked();
        UUID uuid = entity.getUniqueId();
        Bukkit.getLogger().info("uuid: " + uuid);
    }
}
