package com.growuphappily.gamesystem.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.DEDICATED_SERVER)
public class CommandReg {
    public static MinecraftServer serverInstance;
    @SubscribeEvent
    public static void onCommandReg(RegisterCommandsEvent event){
        event.getDispatcher().register(
                Commands.literal("game").then(
                        Commands.literal("create")
                                .requires((CommandSource) -> CommandSource.hasPermission(0))
                                .executes(CommandGameCreate.instance)
                )
        );
        event.getDispatcher().register(
                Commands.literal("game").then(
                        Commands.literal("start")
                                .requires((CommandSource) -> CommandSource.hasPermission(0))
                                .executes(CommandGameStart.instance)
                )
        );
    }
    @SubscribeEvent
    public static void onServerStart(ServerStartingEvent event){
        serverInstance = event.getServer();
    }
}
