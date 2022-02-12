package com.growuphappily.gamesystem.world.item;

import com.growuphappily.gamesystem.entity.EntityMoonGas;
import com.growuphappily.gamesystem.entity.EntityShooter;
import com.growuphappily.gamesystem.entity.EntityTypeRegistry;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemGun extends Item {
    public ItemGun(){
        super(new Properties().tab(CreativeModeTab.TAB_COMBAT));
    }

    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if(!world.isClientSide) {
            if(player.getInventory().countItem(ItemRegistry.bullet.get()) < 1){
                player.sendMessage(new TextComponent("No bullets!"), Util.NIL_UUID);
                return InteractionResultHolder.success(player.getItemInHand(hand));
            }
            EntityShooter shooter = new EntityShooter(EntityTypeRegistry.moonGas.get(), world);
            shooter.setOwner(player);
            shooter.shootFromRotation(player, player.getXRot(), player.getYRot(), 0f, 5f, 0.5f);
            world.addFreshEntity(shooter);
            shooter.moveTo(player.getEyePosition().add(player.getLookAngle()));
            for (ItemStack stack : player.getInventory().items) {
                if(stack.is(ItemRegistry.bullet.get())){
                    stack.shrink(1);
                    break;
                }
            }
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
