package com.growuphappily.gamesystem.commands;

import com.growuphappily.gamesystem.system.Game;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TextComponent;

public class CommandGameStart implements Command<CommandSourceStack> {
    public static CommandGameStart instance = new CommandGameStart();
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Game game = Game.getGameByName(StringArgumentType.getString(context, "name"));
        if(game == null){
            context.getSource().sendFailure(new TextComponent("start: " + StringArgumentType.getString(context, "name") + ": no such file or dictionary.(no"));
        }else {
            game.start();
        }
        return 0;
    }
}
