package com.growuphappily.gamesystem.skills;

import com.growuphappily.gamesystem.annotations.EventSubscribe;
import com.growuphappily.gamesystem.enums.EnumFactionCategory;
import com.growuphappily.gamesystem.eventsystem.AttackEvent;
import com.growuphappily.gamesystem.eventsystem.AttackSucceedEvent;
import com.growuphappily.gamesystem.system.Dice;
import com.growuphappily.gamesystem.system.GamePlayer;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;

public class SkillOrbitAttack extends Skill{
    public static ArrayList<GamePlayer> openedPlayers;
    public SkillOrbitAttack(){
        consumption = 60;
        category = EnumFactionCategory.FENCING;
    }

    public void run(GamePlayer player){
        openedPlayers.add(player);
    }

    @EventSubscribe
    public static void onAttack(AttackSucceedEvent event){
        if(openedPlayers.contains(event.player)){
            LogManager.getLogger().info(event.player.playerInstance.getLookAngle().toString());
            event.target.playerInstance.setDeltaMovement(
                    event.target.playerInstance.getDeltaMovement().add(
                            event.player.playerInstance.getLookAngle().x,
                            1.0,
                            event.player.playerInstance.getLookAngle().z
                    )
            );
            //if((double)Dice.onedX(100) + (double)event.player.attributes.speed*0.2 + event.player.attributes.knowledge*0.8 >=
            //   ((double)Dice.onedX(100) + event.target.attributes.speed*0.6 + event.target.attributes.defence*0.4))
        }
    }
}
