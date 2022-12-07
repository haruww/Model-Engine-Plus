package me.haru301.megplus;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.animation.AnimationHandler;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.Map;
import java.util.UUID;

public class EventHandle implements Listener
{
    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent e)
    {
        Entity entity = e.getRightClicked();
        UUID uuid = entity.getUniqueId();
        Bukkit.getLogger().info("uuid: " + uuid);
    }

    @EventHandler
    public void onRightClickArmorStand(PlayerInteractAtEntityEvent e)
    {
        Entity entity = e.getRightClicked();
        if(entity instanceof ArmorStand)
        {
            UUID uuid = entity.getUniqueId();
            Bukkit.getLogger().info("uuid: " + uuid);
        }
    }

    @EventHandler
    public void onEntityAttackEntity(EntityDamageByEntityEvent e)
    {
        Entity attacker = e.getDamager();
        UUID targetUUID = attacker.getUniqueId();
        ModeledEntity modeledEntity = ModelEngineAPI.getModeledEntity(targetUUID);
        if(modeledEntity == null)
            return;

        Map<String, ActiveModel> model = modeledEntity.getModels();
        ActiveModel activeModel = null;
        for(String strKey : model.keySet())
            activeModel = model.get(strKey);

        AnimationHandler animationHandler = activeModel.getAnimationHandler();
        if(animationHandler == null)
            return;

        //test

        animationHandler.playAnimation("attack", 0, 0, 1, true);
    }
}
