package com.growuphappily.gamesystem.commands;

import com.growuphappily.gamesystem.system.Game;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;

public class CommandGameKill implements Command<CommandSourceStack> {
    public static CommandGameKill instance = new CommandGameKill();
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Game.isStarted = false;
        return 0;
    }
}
