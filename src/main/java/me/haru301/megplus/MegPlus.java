package me.haru301.megplus;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.animation.AnimationHandler;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.arguments.UUIDArgument;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;

public final class MegPlus extends JavaPlugin
{
    @Override
    public void onEnable()
    {
        Bukkit.getPluginManager().registerEvents(new EventHandle(), this);

        new CommandAPICommand("megplus")
                .withArguments(new LiteralArgument("motionlist"))
                .withArguments(new UUIDArgument("target UUID"))
                .executesPlayer(this::motionlist)
                .register();

        new CommandAPICommand("megplus")
                .withArguments(new LiteralArgument("disguise"))
                .withArguments(new StringArgument("Model ID"))
                .withArguments(new UUIDArgument("target UUID"))
                .executes((this::disguise))
                .register();

        new CommandAPICommand("megplus")
                .withArguments(new LiteralArgument("undisguise"))
                .withArguments(new UUIDArgument("target UUID"))
                .executes((this::undisguise))
                .register();

        new CommandAPICommand("megplus")
                .withArguments(new LiteralArgument("motion"))
                .withArguments(new StringArgument("Motion ID"))
                .withArguments(new UUIDArgument("target UUID"))
                .executes((this::motion))
                .register();

        new CommandAPICommand("megplus")
                .withArguments(new LiteralArgument("nomotion"))
                .withArguments(new UUIDArgument("target UUID"))
                .executes((this::nomotion))
                .register();
    }

    private void motionlist(CommandSender sender, Object[] args)
    {
        UUID targetUUID = (UUID) args[0];

        ModeledEntity modeledEntity = ModelEngineAPI.getModeledEntity(targetUUID);
        if(modeledEntity == null)
        {
            sender.sendMessage(ChatColor.RED + "대상에게 모델이 입혀져있지 않습니다. (또는 다른 이유)");
            return;
        }

        Map<String, ActiveModel> model = modeledEntity.getModels();
        ActiveModel activeModel = null;
        for(String strKey : model.keySet())
            activeModel = model.get(strKey);

        for(String keySet : activeModel.getBlueprint().getAnimations().keySet())
        {
            sender.sendMessage(activeModel.getBlueprint().getAnimations().get(keySet).getName());

        }
    }

    private void disguise(CommandSender sender, Object[] args)
    {
        String modelName = (String) args[0];
        UUID targetUUID = (UUID) args[1];

        Entity targetEntity = getEntityByUniqueId(targetUUID);
        if(targetEntity == null)
        {
            sender.sendMessage(ChatColor.RED + "엔티티를 찾을 수 없습니다. : " + targetUUID);
            return;
        }

        ModeledEntity modeledEntity = ModelEngineAPI.getModeledEntity(targetUUID);
        if(modeledEntity == null)
        {
            modeledEntity = ModelEngineAPI.createModeledEntity(targetEntity);
            modeledEntity.setBaseEntityVisible(false);
        }
        else
        {
            sender.sendMessage(ChatColor.RED + "모델이 이미 적용되어있습니다. undisguise를 먼저 해주세요.");
            return;
        }

        ActiveModel model = ModelEngineAPI.createActiveModel(modelName);
        if(model == null)
        {
            sender.sendMessage(ChatColor.RED + "모델을 불러올 수 없습니다. : " + modelName);
            return;
        }

        modeledEntity.addModel(model, true);
    }

    private void undisguise(CommandSender sender, Object[] args)
    {
        UUID targetUUID = (UUID) args[0];

        ModeledEntity modeledEntity = ModelEngineAPI.getModeledEntity(targetUUID);
        if(modeledEntity == null)
            return;
        modeledEntity.setBaseEntityVisible(true);
        ModelEngineAPI.removeModeledEntity(targetUUID);
    }

    private void motion(CommandSender sender, Object[] args)
    {
        String motionName = (String) args[0];
        UUID targetUUID = (UUID) args[1];

        ModeledEntity modeledEntity = ModelEngineAPI.getModeledEntity(targetUUID);
        if(modeledEntity == null)
        {
            sender.sendMessage(ChatColor.RED + "대상에게 모델이 입혀져있지 않습니다. (또는 다른 이유)");
            return;
        }

        Map<String, ActiveModel> model = modeledEntity.getModels();
        ActiveModel activeModel = null;
        for(String strKey : model.keySet())
            activeModel = model.get(strKey);

        AnimationHandler animationHandler = activeModel.getAnimationHandler();
        if(animationHandler == null)
        {
            sender.sendMessage(ChatColor.RED + "AnimationHandler를 불러올 수 없습니다.");
            return;
        }

        if(!animationHandler.playAnimation(motionName, 0, 0, 1, true))
            sender.sendMessage(ChatColor.RED + "애니메이션이 존재하지 않습니다.");
    }

    private void nomotion(CommandSender sender, Object[] args)
    {
        UUID targetUUID = (UUID) args[0];

        ModeledEntity modeledEntity = ModelEngineAPI.getModeledEntity(targetUUID);
        if(modeledEntity == null)
        {
            sender.sendMessage(ChatColor.RED + "대상에게 모델이 입혀져있지 않습니다. (또는 다른 이유)");
            return;
        }

        Map<String, ActiveModel> model = modeledEntity.getModels();
        ActiveModel activeModel = null;
        for(String strKey : model.keySet())
            activeModel = model.get(strKey);

        AnimationHandler animationHandler = activeModel.getAnimationHandler();
        if(animationHandler == null)
        {
            sender.sendMessage(ChatColor.RED + "AnimationHandler를 불러올 수 없습니다.");
            return;
        }
        animationHandler.forceStopAllAnimations();
    }

    private Entity getEntityByUniqueId(UUID uniqueId)
    {
        for (World world : Bukkit.getWorlds())
        {
            for (Entity entity : world.getEntities())
            {
                if (entity.getUniqueId().equals(uniqueId))
                    return entity;
            }
        }
        return null;
    }
}
