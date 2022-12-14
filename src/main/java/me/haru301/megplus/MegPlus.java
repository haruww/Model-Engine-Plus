package me.haru301.megplus;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.animation.AnimationHandler;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
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
                .withArguments(new EntitySelectorArgument<Collection<Entity>>("target Entity", EntitySelector.MANY_ENTITIES))
                .executesPlayer(this::motionlist)
                .register();

        new CommandAPICommand("megplus")
                .withArguments(new LiteralArgument("disguise"))
                .withArguments(new StringArgument("Model ID"))
                .withArguments(new EntitySelectorArgument<Collection<Entity>>("target Entity", EntitySelector.MANY_ENTITIES))
                .executes((this::disguise))
                .register();

        new CommandAPICommand("megplus")
                .withArguments(new LiteralArgument("undisguise"))
                .withArguments(new EntitySelectorArgument<Collection<Entity>>("target Entity", EntitySelector.MANY_ENTITIES))
                .executes((this::undisguise))
                .register();

        new CommandAPICommand("megplus")
                .withArguments(new LiteralArgument("motion"))
                .withArguments(new StringArgument("Motion ID"))
                .withArguments(new EntitySelectorArgument<Collection<Entity>>("target Entity", EntitySelector.MANY_ENTITIES))
                .executes((this::motion))
                .register();

        new CommandAPICommand("megplus")
                .withArguments(new LiteralArgument("nomotion"))
                .withArguments(new EntitySelectorArgument<Collection<Entity>>("target Entity", EntitySelector.MANY_ENTITIES))
                .executes((this::nomotion))
                .register();
    }

    private void motionlist(CommandSender sender, Object[] args)
    {
        Collection<Entity> entities = (Collection<Entity>) args[0];

        for(Entity e : entities)
        {
            UUID targetUUID = e.getUniqueId();
            ModeledEntity modeledEntity = ModelEngineAPI.getModeledEntity(targetUUID);
            if(modeledEntity == null)
            {
                sender.sendMessage(ChatColor.YELLOW + targetUUID.toString());
                sender.sendMessage(ChatColor.RED + "???????????? ????????? ??????????????? ????????????. (?????? ?????? ??????)");
                sender.sendMessage(" ");
                return;
            }

            Map<String, ActiveModel> model = modeledEntity.getModels();
            ActiveModel activeModel = null;
            for(String strKey : model.keySet())
                activeModel = model.get(strKey);

            sender.sendMessage(ChatColor.YELLOW + targetUUID.toString());
            for(String keySet : activeModel.getBlueprint().getAnimations().keySet())
            {
                sender.sendMessage(activeModel.getBlueprint().getAnimations().get(keySet).getName());
            }
            sender.sendMessage(" ");
        }
    }

    private void disguise(CommandSender sender, Object[] args)
    {
        String modelName = (String) args[0];
        Collection<Entity> entities = (Collection<Entity>) args[1];

        if(entities == null)
        {
            sender.sendMessage(ChatColor.RED + "???????????? ?????? ??? ????????????. : ");
            return;
        }

        for(Entity e : entities)
        {
            UUID targetUUID = e.getUniqueId();
            ModeledEntity modeledEntity = ModelEngineAPI.getModeledEntity(targetUUID);
            if(modeledEntity == null)
            {
                modeledEntity = ModelEngineAPI.createModeledEntity(e);
                modeledEntity.setBaseEntityVisible(false);
            }
            else
            {
                sender.sendMessage(ChatColor.RED + "????????? ?????? ????????????????????????. undisguise??? ?????? ????????????.");
                continue;
            }

            ActiveModel model = ModelEngineAPI.createActiveModel(modelName);
            if(model == null)
            {
                sender.sendMessage(ChatColor.RED + "????????? ????????? ??? ????????????. : " + modelName);
                return;
            }

            modeledEntity.addModel(model, true);
        }
    }

    private void undisguise(CommandSender sender, Object[] args)
    {
        Collection<Entity> entities = (Collection<Entity>) args[0];

        for(Entity e : entities)
        {
            UUID targetUUID = e.getUniqueId();
            ModeledEntity modeledEntity = ModelEngineAPI.getModeledEntity(targetUUID);
            if(modeledEntity == null)
                continue;
            modeledEntity.setBaseEntityVisible(true);
            ModelEngineAPI.removeModeledEntity(targetUUID);
        }
    }

    private void motion(CommandSender sender, Object[] args)
    {
        String motionName = (String) args[0];
        Collection<Entity> entities = (Collection<Entity>) args[1];

        for(Entity e : entities)
        {
            UUID targetUUID = e.getUniqueId();

            ModeledEntity modeledEntity = ModelEngineAPI.getModeledEntity(targetUUID);
            if(modeledEntity == null)
            {
                sender.sendMessage(ChatColor.RED + "???????????? ????????? ??????????????? ????????????. (?????? ?????? ??????)");
                continue;
            }

            Map<String, ActiveModel> model = modeledEntity.getModels();
            ActiveModel activeModel = null;
            for(String strKey : model.keySet())
                activeModel = model.get(strKey);

            AnimationHandler animationHandler = activeModel.getAnimationHandler();
            if(animationHandler == null)
            {
                sender.sendMessage(ChatColor.RED + "AnimationHandler??? ????????? ??? ????????????.");
                continue;
            }

            if(!animationHandler.playAnimation(motionName, 0, 0, 1, true))
                sender.sendMessage(ChatColor.RED + "?????????????????? ???????????? ????????????.");
        }
    }

    private void nomotion(CommandSender sender, Object[] args)
    {
        Collection<Entity> entities = (Collection<Entity>) args[0];

        for(Entity e : entities)
        {
            UUID targetUUID = e.getUniqueId();

            ModeledEntity modeledEntity = ModelEngineAPI.getModeledEntity(targetUUID);
            if(modeledEntity == null)
            {
                sender.sendMessage(ChatColor.RED + "???????????? ????????? ??????????????? ????????????. (?????? ?????? ??????)");
                continue;
            }

            Map<String, ActiveModel> model = modeledEntity.getModels();
            ActiveModel activeModel = null;
            for(String strKey : model.keySet())
                activeModel = model.get(strKey);

            AnimationHandler animationHandler = activeModel.getAnimationHandler();
            if(animationHandler == null)
            {
                sender.sendMessage(ChatColor.RED + "AnimationHandler??? ????????? ??? ????????????.");
                continue;
            }
            animationHandler.forceStopAllAnimations();
        }
    }
}
