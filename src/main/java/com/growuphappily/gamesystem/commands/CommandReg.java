package com.growuphappily.gamesystem.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommandReg {
    public static MinecraftServer serverInstance;

    @SubscribeEvent
    public static void onCommandReg(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("game").requires((c) -> c.hasPermission(0)).then(
                        Commands.literal("create").then(
                                Commands.argument("players", EntityArgument.players()).then(
                                        Commands.argument("name", StringArgumentType.string()).executes(
                                                CommandGameCreate.instance
                                        )
                                )
                        )).then(
                        Commands.literal("start").then(
                                Commands.argument("name", StringArgumentType.string()).executes(
                                        CommandGameStart.instance
                                )
                        )
                ).then(
                        Commands.literal("kill").then(
                                Commands.argument("name", StringArgumentType.string()).executes(
                                        CommandGameKill.instance
                                )
                        )
                )
        );
    }

    @SubscribeEvent
    public static void onServerStart(ServerStartingEvent event) {
        serverInstance = event.getServer();
    }
}
