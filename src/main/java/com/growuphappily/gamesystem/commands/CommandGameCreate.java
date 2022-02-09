package com.growuphappily.gamesystem.commands;

import com.growuphappily.gamesystem.enums.EnumGameMode;
import com.growuphappily.gamesystem.packages.Networking;
import com.growuphappily.gamesystem.packages.PackageOpenGUI;
import com.growuphappily.gamesystem.system.Game;
import com.growuphappily.gamesystem.system.GamePlayer;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;

public class CommandGameCreate implements Command<CommandSourceStack> {
    public static CommandGameCreate instance = new CommandGameCreate();
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Game.server = context.getSource().getServer();
        if(EntityArgument.getPlayers(context, "players").size() < 2){
            context.getSource().sendFailure(new TextComponent("Player not enough! I need 2 players at least."));
            return 0;
        }
        Game game = new Game(EnumGameMode.MODE_NORMAL, StringArgumentType.getString(context, "name"), new ArrayList<>(EntityArgument.getPlayers(context, "players")));
        game.world = context.getSource().getLevel();
        context.getSource().sendSuccess(new TextComponent("Test"),false);
        for (ServerPlayer p:context.getSource().getServer().getPlayerList().getPlayers()) {
            if(game.searchPlayerByName(p.getDisplayName().getString()).isEvil) {
                Networking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> p), new PackageOpenGUI("Evil"));
                continue;
            }
            Networking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> p), new PackageOpenGUI("Attr"));
        }
        return 0;
    }
}
