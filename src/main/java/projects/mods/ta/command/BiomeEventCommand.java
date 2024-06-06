package projects.mods.ta.command;

import com.mojang.brigadier.CommandDispatcher;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import projects.mods.ta.impl.events.CurrentBiomeEventManager;
import projects.mods.ta.impl.events.biome.BiomeEvent;
import projects.mods.ta.impl.events.biome.BiomeEventImpl;
import projects.mods.ta.impl.events.biome.BiomeEvents;

public class BiomeEventCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(literal("BiomeEvent")
                .requires(source->source.hasPermissionLevel(2))
                .then(argument("action", StringArgumentType.string())
                        .suggests(new BiomeEventSuggestionProvider.BiomeEventCommandSuggestion())
                        .then(argument("type", StringArgumentType.string())
                                .suggests(new BiomeEventSuggestionProvider.AroundsTypeSuggestion())
                                .then(argument("identifier", IdentifierArgumentType.identifier())
                                        .suggests(new BiomeEventSuggestionProvider.BiomeEventSuggestion())
                                        .executes(BiomeEventCommand::execute)

                                ))));
    }

    private static int execute(CommandContext<ServerCommandSource> context){

        BiomeEvent event = BiomeEvents.getEvent(context.getArgument("identifier", Identifier.class));
        if(event == null) return 0;

        PlayerEntity player = context.getSource().getPlayer();
        if(player != null && player.getServer() != null && player.getServer().getWorld(player.getWorld().getRegistryKey()) != null){

            CurrentBiomeEventManager manager = CurrentBiomeEventManager.getManager(player.getServer().getWorld(player.getWorld().getRegistryKey()));

            switch (context.getArgument("action", String.class)){
                case "start"-> manager.register(event);
                case "end"-> manager.timeout(event);
            }

            return 1;
        }




        return 0;
    }
}
