package com.growuphappily.gamesystem.world.item;

import com.growuphappily.gamesystem.entity.EntityShooter;
import com.growuphappily.gamesystem.entity.EntityTypeRegistry;
import com.growuphappily.gamesystem.skills.SkillBlast;
import com.growuphappily.gamesystem.system.Game;
import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Objects;

public class ItemGun extends Item {
    public static long lastTime = 0;
    public ItemGun(){
        super(new Properties().tab(CreativeModeTab.TAB_COMBAT));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if(!world.isClientSide && lastTime + (long)250 <= new Date().getTime()) {
            if(player.getInventory().countItem(ItemRegistry.bullet.get()) < 1){
                player.sendMessage(new TextComponent("No bullets!"), Util.NIL_UUID);
                return super.use(world, player, hand);
            }
            EntityShooter shooter = new EntityShooter(EntityTypeRegistry.shooter.get(), world);
            shooter.setOwner(player);
            shooter.shootFromRotation(player, player.getXRot(), player.getYRot(), 0f, 5f, 0.5f);
            world.addFreshEntity(shooter);
            shooter.moveTo(player.getEyePosition().add(player.getLookAngle()));
            shooter.startPos = shooter.position();
            try {
                if (SkillBlast.openedPlayers.contains(Objects.requireNonNull(Game.getGameByPlayerName(player.getDisplayName().getString())).searchPlayerByName(player.getDisplayName().getString()))){
                    shooter.isBlast = true;
                }
            }catch (NullPointerException ignored){}
            for (ItemStack stack : player.getInventory().items) {
                if(stack.is(ItemRegistry.bullet.get())){
                    stack.shrink(1);
                    break;
                }
            }
        }
        return super.use(world, player, hand);
    }

}
