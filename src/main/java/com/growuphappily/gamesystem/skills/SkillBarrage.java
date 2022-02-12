package com.growuphappily.gamesystem.skills;

import com.growuphappily.gamesystem.entity.EntityShooter;
import com.growuphappily.gamesystem.entity.EntityTypeRegistry;
import com.growuphappily.gamesystem.enums.EnumFactionCategory;
import com.growuphappily.gamesystem.system.Game;
import com.growuphappily.gamesystem.system.GamePlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber
public class SkillBarrage extends Skill{
    public static ArrayList<GamePlayer> openedPlayers;
    public static long lastTime = 0;

    public SkillBarrage(){
        consumption = 70;
        category = EnumFactionCategory.GUN;
    }

    public void run(GamePlayer player){
        openedPlayers.add(player);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                openedPlayers.remove(player);
            }
        }, 3000);
    }

    @SubscribeEvent
    public static void tick(TickEvent.ServerTickEvent event){
        if(lastTime + (long)400 >= new Date().getTime()){
            for (int i = 0; i < openedPlayers.size(); i++) {
                GamePlayer player;
                try {
                    player = openedPlayers.get(i);
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                    return;
                }
                try {
                    if (Objects.requireNonNull(Game.getGameByPlayerName(player.playerInstance.getDisplayName().getString())).isStarted) {
                        for (int j = 0; j < 20; j++) {
                            EntityShooter shooter = new EntityShooter(EntityTypeRegistry.shooter.get(), player.playerInstance.level);
                            shooter.setDeltaMovement(player.playerInstance.getLookAngle().add(getPVec(player.playerInstance.getLookAngle()).scale((j - 10)*0.5)));
                            player.playerInstance.level.addFreshEntity(shooter);
                            shooter.moveTo(player.playerInstance.getEyePosition().add(player.playerInstance.getLookAngle()));
                        }
                    }
                }catch (NullPointerException ignored){}
            }
            lastTime = new Date().getTime();
        }
    }

    public static Vec3 getPVec(Vec3 v){
        double k = v.z/v.x;
        Vec3 v1 = new Vec3(1,0,-1/k);
        v1 = v1.scale(v1.length());
        return v1;
    }
}
