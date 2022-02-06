package com.growuphappily.gamesystem.commands;

import com.growuphappily.gamesystem.enums.EnumGameMode;
import com.growuphappily.gamesystem.packages.Networking;
import com.growuphappily.gamesystem.packages.PackageOpenGUI;
import com.growuphappily.gamesystem.system.Game;
import com.growuphappily.gamesystem.system.GamePlayer;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

public class CommandGameCreate implements Command<CommandSourceStack> {
    public static CommandGameCreate instance = new CommandGameCreate();
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Game.server = context.getSource().getServer();
        if(Game.server.getPlayerCount() < 2){
            context.getSource().sendFailure(new TextComponent("Player not enough! I need 2 players at least."));
            return 0;
        }
        new Game(EnumGameMode.MODE_NORMAL);
        Game.instance.world = context.getSource().getLevel();
        context.getSource().sendSuccess(new TextComponent("Test"),false);
        for (ServerPlayer p:context.getSource().getServer().getPlayerList().getPlayers()) {
            if(Game.instance.searchPlayerByName(p.getDisplayName().getString()).isEvil) {
                Networking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> p), new PackageOpenGUI("Evil"));
                continue;
            }
            Networking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> p), new PackageOpenGUI("Attr"));
        }
        return 0;
    }
}
