package com.growuphappily.gamesystem.entity;

import com.growuphappily.gamesystem.system.Dice;
import com.growuphappily.gamesystem.system.Game;
import com.growuphappily.gamesystem.system.GamePlayer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class EntityMoonGas extends AbstractHurtingProjectile {
    public float Xrot;
    public float Yrot;
    public boolean isRoted = false;
    public EntityMoonGas(EntityType<? extends AbstractHurtingProjectile> p_36833_, Level p_36834_) {
        super(p_36833_, p_36834_);
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult p_37259_) {
        if(getOwner() == null){
            return;
        }
        if(p_37259_.getEntity() instanceof Player target){
            if(Game.getGameByPlayerName(target.getDisplayName().getString()) == Game.getGameByPlayerName(getOwner().getDisplayName().getString())){
                GamePlayer p;
                GamePlayer t;
                try {
                    p = Objects.requireNonNull(Game.getGameByPlayerName(getOwner().getDisplayName().getString())).searchPlayerByName(getOwner().getDisplayName().getString());
                    t = Objects.requireNonNull(Game.getGameByPlayerName(target.getDisplayName().getString())).searchPlayerByName(target.getDisplayName().getString());
                if((double)Dice.onedX(100) <= (double)60 - t.attributes.defence*0.05){
                    t.hurt(DamageSource.playerAttack(p.playerInstance), 10 + p.attributes.strength*0.1f + p.attributes.knowledge*0.1f - t.attributes.defence*0.3f);
                }else{
                    t.hurt(DamageSource.playerAttack(p.playerInstance), 5 + p.attributes.strength*0.1f + p.attributes.knowledge*0.1f - t.attributes.defence*0.5f);
                }
                }catch (NullPointerException ignored){}
            }
        }
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected float getInertia() {
        return 1f;
    }

    @Override
    protected @NotNull ParticleOptions getTrailParticle() {
        return ParticleTypes.CLOUD;
    }


}
