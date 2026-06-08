package mod.zacharymei.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import mod.zacharymei.impl.events.biomeevent.ModBiomeEvents;
import mod.zacharymei.impl.events.biomeevent.ServerBiomeEventManager;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class BiomeEventCommand {

    public static void command(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext, Commands.CommandSelection selection){
        dispatcher.register(literal("BiomeEvent")
                .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                .then(argument("action", StringArgumentType.string())
                        .suggests(new ModCommandSuggestions.BiomeEventActions())
                        .then(argument("type", StringArgumentType.string())
                                .suggests(new ModCommandSuggestions.BiomeEventTypes())
                                .then(argument("identifier", StringArgumentType.string())
                                        .suggests(new ModCommandSuggestions.BiomeEvents())
                                        .executes(BiomeEventCommand::execute)

                                ))));
    }

    private static int execute(CommandContext<CommandSourceStack> context){

        //context.getArgument("identifier", Identifier.class);
        ServerLevel level = context.getSource().getLevel();
        ServerBiomeEventManager.start(level.dimension(), ModBiomeEvents.RainyDayEvent.getBiomeEvent());


        return 0;
    }
}
