package com.growuphappily.gamesystem.skills;

import com.growuphappily.gamesystem.annotations.EventSubscribe;
import com.growuphappily.gamesystem.enums.EnumFactionCategory;
import com.growuphappily.gamesystem.eventsystem.AttackEvent;
import com.growuphappily.gamesystem.eventsystem.AttackSucceedEvent;
import com.growuphappily.gamesystem.system.Dice;
import com.growuphappily.gamesystem.system.GamePlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class SkillOrbitAttack extends Skill{
    public static ArrayList<GamePlayer> openedPlayers = new ArrayList<>();
    public SkillOrbitAttack(){
        consumption = 60;
        category = EnumFactionCategory.FENCING;
    }

    public void run(GamePlayer player){
        if(super.preRun(player)){
            return;
        }
        openedPlayers.add(player);
        player.sendMessage("Your next attack will be strengthened.");
    }

    @SubscribeEvent
    public static void onAttack(AttackSucceedEvent event){
        if(openedPlayers.contains(event.player)){
            LogManager.getLogger().info(event.player.playerInstance.getLookAngle().toString());
            event.target.playerInstance.setDeltaMovement(
                    new Vec3(
                            event.player.playerInstance.getLookAngle().x,
                            1.0,
                            event.player.playerInstance.getLookAngle().z
                    )
            );
            LogManager.getLogger().info(event.target.playerInstance.getDeltaMovement());
            if((double)Dice.onedX(100) + (double)event.player.attributes.speed*0.2 + event.player.attributes.knowledge*0.8 >=
               ((double)Dice.onedX(100) + event.target.attributes.speed*0.6 + event.target.attributes.defence*0.4)){
                event.damage *= 1.5f;
                event.player.sendMessage("Your next attack will be strengthened.");
            }else{
                event.damage *= 1.2f;
                openedPlayers.remove(event.player);
            }
        }
    }
}
