package com.growuphappily.gamesystem.commands;

import com.growuphappily.gamesystem.system.Game;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TextComponent;

public class CommandGameKill implements Command<CommandSourceStack> {
    public static CommandGameKill instance = new CommandGameKill();
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Game game = Game.getGameByName(StringArgumentType.getString(context, "name"));
        if(game == null){
            context.getSource().sendFailure(new TextComponent("kill: (" + StringArgumentType.getString(context, "name") + ") - no such process.(no"));
        }else{
            game.gameOver();
        }
        return 0;
    }
}
