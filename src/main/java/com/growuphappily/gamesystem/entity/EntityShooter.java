package com.growuphappily.gamesystem.entity;

import com.growuphappily.gamesystem.effects.EffectBrokenArmor;
import com.growuphappily.gamesystem.effects.EffectBrokenSoul;
import com.growuphappily.gamesystem.enums.EnumFactionCategory;
import com.growuphappily.gamesystem.system.Dice;
import com.growuphappily.gamesystem.system.Game;
import com.growuphappily.gamesystem.system.GamePlayer;
import com.growuphappily.gamesystem.world.item.ItemRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Objects;

public class EntityShooter extends AbstractHurtingProjectile {
    public boolean isSkill = false;
    public boolean isBlast = false;
    public Vec3 startPos;
    public long lastTime = 0;

    public EntityShooter(EntityType<? extends AbstractHurtingProjectile> p_36833_, Level p_36834_) {
        super(p_36833_, p_36834_);
    }

    @Override
    protected void onHitEntity(EntityHitResult p_37259_) {
        if(level.isClientSide || getOwner() == null || p_37259_.getEntity() instanceof EntityShooter || Game.getGameByPlayerName(getOwner().getDisplayName().getString()) == null || Game.getGameByPlayerName(p_37259_.getEntity().getDisplayName().getString()) == null){
            return;
        }
        if(p_37259_.getEntity() instanceof Player t) {
            GamePlayer player = Objects.requireNonNull(Game.getGameByPlayerName(getOwner().getDisplayName().getString())).searchPlayerByName(getOwner().getDisplayName().getString());
            GamePlayer target = Objects.requireNonNull(Game.getGameByPlayerName(p_37259_.getEntity().getDisplayName().getString())).searchPlayerByName(p_37259_.getEntity().getDisplayName().getString());
            if(player.factions.contains(EnumFactionCategory.GUN)){
                player.playerInstance.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 2));
            }
            if (!isSkill) {
                float dice = (float)Dice.onedX(100);
                if(dice >= 10f + target.attributes.defence*0.8f + target.attributes.health*0.1f - player.attributes.knowledge*0.2f) {
                    player.sendMessage("CRITICAL!");
                    target.hurt(DamageSource.playerAttack(player.playerInstance), 8 + (player.attributes.strength * 0.3f) - target.attributes.defence * 0.1f);
                }else{
                    target.hurt(DamageSource.playerAttack(player.playerInstance), 4 + (player.attributes.strength * 0.1f) - target.attributes.defence * 0.1f);
                }
                if(dice % 7 == 0){
                    if(player.playerInstance.getInventory().countItem(ItemRegistry.bullet.get()) > 0){
                        for (ItemStack stack : player.playerInstance.getInventory().items) {
                            if(stack.is(ItemRegistry.bullet.get())){
                                stack.grow(1);
                                break;
                            }
                        }
                    }else{
                        player.playerInstance.getInventory().add(new ItemStack(ItemRegistry.bullet.get(), 1));
                    }
                }
            }else{
                int dice = Dice.onedX(10);
                if((float)dice >= target.attributes.defence*0.1f){
                    target.hurt(DamageSource.playerAttack(player.playerInstance), 14 + player.attributes.strength*0.2f - target.attributes.defence*0.1f);
                }else{
                    target.hurt(DamageSource.playerAttack(player.playerInstance), 10 + player.attributes.strength*0.1f - target.attributes.defence*0.2f);
                }
                if(dice % 2 == 0){
                    EffectBrokenSoul.addPlayer(target, 5);
                }else{
                    EffectBrokenArmor.addPlayer(target, 10);
                }
            }
        }
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected float getInertia() {
        if(startPos == null){
            return 1f;
        }
        return getEyePosition().distanceTo(startPos) > 15 ? 0.95f : 1f;
    }

    @Override
    protected @NotNull ParticleOptions getTrailParticle() {
        return ParticleTypes.CLOUD;
    }

    @Override
    protected void onHit(HitResult p_37260_) {
        if(!isBlast || level.isClientSide) {
            super.onHit(p_37260_);
        }else{
            level.explode(this, getX(), getY(), getZ(), 1.0f, Explosion.BlockInteraction.NONE);
            if(getOwner() instanceof Player t) {
                if (t.getInventory().countItem(ItemRegistry.bullet.get()) > 0) {
                    for (ItemStack stack : t.getInventory().items) {
                        if (stack.is(ItemRegistry.bullet.get())) {
                            stack.grow(1);
                            break;
                        }
                    }
                } else {
                    t.getInventory().add(new ItemStack(ItemRegistry.bullet.get(), 1));
                }
                GamePlayer player = Objects.requireNonNull(Game.getGameByPlayerName(getOwner().getDisplayName().getString())).searchPlayerByName(getOwner().getDisplayName().getString());
                for(ServerPlayer p : Objects.requireNonNull(level.getServer()).getPlayerList().getPlayers()){
                    if(p.position().distanceTo(position()) <= 5f){
                        GamePlayer target;
                        try {
                            target = Objects.requireNonNull(Game.getGameByPlayerName(p.getDisplayName().getString())).searchPlayerByName(p.getDisplayName().getString());
                        }catch (NullPointerException e){
                            continue;
                        }
                        if(target.playerInstance == getOwner()){
                            continue;
                        }
                        if((float)Dice.onedX(100) >= 20f + target.attributes.defence*0.5f){
                            target.hurt(DamageSource.playerAttack((Player)getOwner()), 15f + player.attributes.knowledge*0.1f + player.attributes.strength*0.1f - target.attributes.defence*0.6f);
                        }else{
                            target.hurt(DamageSource.playerAttack((Player)getOwner()), 10f + player.attributes.knowledge*0.1f + player.attributes.strength*0.1f - target.attributes.defence*0.8f);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void tick(){
        super.tick();
        if(startPos == null){
            return;
        }
        if(position().distanceTo(startPos) > 15 && lastTime + (long)1000 <= new Date().getTime()){
            setDeltaMovement(getDeltaMovement().x, getDeltaMovement().y - 0.98f, getDeltaMovement().z);
        }
    }
}
