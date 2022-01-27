package com.growuphappily.gamesystem.commands;

import com.growuphappily.gamesystem.system.Game;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;

public class CommandGameStart implements Command<CommandSourceStack> {
    public static CommandGameStart instance = new CommandGameStart();
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Game.instance.start();
        return 0;
    }
}
