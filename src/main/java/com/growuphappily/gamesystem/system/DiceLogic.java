package com.growuphappily.gamesystem.system;

import com.growuphappily.gamesystem.world.item.ItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

@Mod.EventBusSubscriber
public class DiceLogic {
    public static final Block[] climbBlocks = {};
    public static long lastTime;

    @SubscribeEvent
    public static void onInteractBlock(PlayerInteractEvent.RightClickBlock event) {
        if(event.getWorld().isClientSide){
            return;
        }
        if(new Date().getTime() - lastTime > (long)100) {
            for (Game game : Game.instances) {
                if (Game.getGameByPlayerName(event.getPlayer().getDisplayName().getString()) == game && game.isStarted) {
                    GamePlayer player = game.searchPlayerByName(event.getPlayer().getDisplayName().getString());
                    if (event.getItemStack().is(ItemRegistry.blankDice.get()) || event.getItemStack().is(ItemRegistry.motionDice.get())) {
                        if (player.playerInstance.level.getBlockState(event.getPos()).getBlock() == Blocks.OAK_LOG) {
                            int dice = Dice.onedX(6);
                            player.playerInstance.getInventory().add(new ItemStack(Items.OAK_PLANKS, dice));
                            player.sendMessage("You used one dice to get " + dice + " plank(s)!");
                            event.getItemStack().shrink(1);
                        }
                        else if (player.playerInstance.level.getBlockState(event.getPos()).getBlock() == Blocks.IRON_ORE) {
                            int dice = Dice.onedX(6);
                            player.playerInstance.getInventory().add(new ItemStack(Items.IRON_ORE, dice));
                            player.sendMessage("You used one dice to get " + dice + " iron ore(s)!");
                            event.getItemStack().shrink(1);
                        }
                        else if (player.playerInstance.level.getBlockState(event.getPos()).getBlock() == Blocks.GRAVEL) {
                            int dice = Dice.onedX(6);
                            player.playerInstance.getInventory().add(new ItemStack(Items.FLINT, dice));
                            player.sendMessage("You used one dice to get " + dice + " flint(s)!");
                            event.getItemStack().shrink(1);
                        }
                        else if (player.playerInstance.isShiftKeyDown()) {
                            int r = 0;
                            for (int i : Dice.NdX(6, 6)) {
                                r += i;
                            }
                            player.playerInstance.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, r * 20, 1));
                            player.sendMessage("You have gotten invisibility for " + r + " second(s)!");
                            event.getItemStack().shrink(1);
                        }
                        event.setCanceled(true);
                    }
                    if (event.getItemStack().is(ItemRegistry.blankDice.get()) || event.getItemStack().is(ItemRegistry.skillDice.get())) {
                        for (ItemStack stack : player.playerInstance.getInventory().items) {
                            if (stack.is(Items.IRON_ORE) && player.playerInstance.level.getBlockState(event.getPos()).getBlock() == Blocks.FURNACE) {
                                int dice = Dice.onedX(6);
                                player.playerInstance.getInventory().add(new ItemStack(Items.IRON_INGOT, dice));
                                player.sendMessage("You have fired the ore and got " + dice + " ingot(s)!");
                                stack.shrink(1);
                                event.getItemStack().shrink(1);
                                return;
                            }
                        }
                        try {
                            if (player.playerInstance.getInventory().countItem(Items.IRON_INGOT) > 0 && player.playerInstance.getInventory().countItem(Items.FLINT) > 0 && player.playerInstance.level.getBlockState(event.getPos()).getBlock() == Blocks.CRAFTING_TABLE) {
                                Objects.requireNonNull(getItemStack(player.playerInstance.getInventory(), Items.IRON_INGOT)).shrink(1);
                                Objects.requireNonNull(getItemStack(player.playerInstance.getInventory(), Items.FLINT)).shrink(1);
                                int dice = Dice.onedX(6);
                                player.sendMessage("You have used one iron and one flint to craft " + dice + " bullet(s)");
                                player.playerInstance.getInventory().add(new ItemStack(ItemRegistry.bullet.get(), dice));
                                event.getItemStack().shrink(1);
                                return;
                            } else if (player.playerInstance.getInventory().countItem(Items.IRON_INGOT) > 0 && player.playerInstance.getInventory().countItem(Items.OAK_PLANKS) > 5 && player.playerInstance.level.getBlockState(event.getPos()).getBlock() == Blocks.CRAFTING_TABLE) {
                                Objects.requireNonNull(getItemStack(player.playerInstance.getInventory(), Items.IRON_INGOT)).shrink(1);
                                Objects.requireNonNull(getItemStack(player.playerInstance.getInventory(), Items.OAK_PLANKS)).shrink(6);
                                int dice = Dice.onedX(6);
                                player.sendMessage("You have used one iron and 6 planks to craft " + dice + " shield(s)");
                                for (int i = 0; i < dice; i++) {
                                    ItemStack stack = new ItemStack(Items.SHIELD, 1);
                                    stack.setDamageValue(329);
                                    player.playerInstance.getInventory().add(stack);
                                }
                                event.getItemStack().shrink(1);
                                return;
                            }
                        } catch (NullPointerException ignored) {
                        }
                        //TODO: Repair / Break
                        if ((double) Dice.onedX(20) >= (double) Arrays.stream(Dice.NdX(3, 6)).sum() - player.attributes.IQ * 0.1) {
                            if (player.isEvil) {
                                ServerPlayer nearPlayer = null;
                                double d = 0.;
                                for (ServerPlayer p : player.playerInstance.server.getPlayerList().getPlayers()) {
                                    if (p == player.playerInstance) {
                                        continue;
                                    }
                                    double d1 = p.position().distanceTo(player.playerInstance.position());
                                    if (d > d1) {
                                        d = d1;
                                        nearPlayer = p;
                                    }
                                }
                                assert nearPlayer != null;
                                player.sendMessage("The nearest human is at " + nearPlayer.blockPosition());
                                nearPlayer.removeEffect(MobEffects.INVISIBILITY);
                            } else {
                                player.sendMessage("The evil is at " + Objects.requireNonNull(Game.getGameByPlayerName(player.playerInstance.getDisplayName().getString())).evil.playerInstance.blockPosition());
                                Objects.requireNonNull(Game.getGameByPlayerName(player.playerInstance.getDisplayName().getString())).evil.playerInstance.removeEffect(MobEffects.INVISIBILITY);
                            }
                        } else {
                            if (player.isEvil) {
                                ServerPlayer nearPlayer = null;
                                double d = 0.;
                                for (ServerPlayer p : player.playerInstance.server.getPlayerList().getPlayers()) {
                                    if (p == player.playerInstance) {
                                        continue;
                                    }
                                    double d1 = p.position().distanceTo(player.playerInstance.position());
                                    if (d > d1) {
                                        d = d1;
                                        nearPlayer = p;
                                    }
                                }
                                assert nearPlayer != null;
                                nearPlayer.removeEffect(MobEffects.INVISIBILITY);
                                player.sendMessage("The distance to nearest human is  " + nearPlayer.distanceTo(player.playerInstance));
                            } else {
                                player.sendMessage("The distance to evil is  " + Objects.requireNonNull(Game.getGameByPlayerName(player.playerInstance.getDisplayName().getString())).evil.playerInstance.position().distanceTo(player.playerInstance.position()));
                                Objects.requireNonNull(Game.getGameByPlayerName(player.playerInstance.getDisplayName().getString())).evil.playerInstance.removeEffect(MobEffects.INVISIBILITY);
                            }
                        }
                        event.getItemStack().shrink(1);
                        event.setCanceled(true);
                    }
                }
            }
        }
        lastTime = new Date().getTime();
    }

    public static ItemStack getItemStack(Inventory inv, Item item) {
        for (ItemStack stack : inv.items) {
            if (stack.is(item)) {
                return stack;
            }
        }
        return null;
    }
}
