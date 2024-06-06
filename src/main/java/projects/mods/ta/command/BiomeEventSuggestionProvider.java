package projects.mods.ta.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import projects.mods.ta.impl.AroundsType;
import projects.mods.ta.impl.events.biome.BiomeEvent;
import projects.mods.ta.impl.events.biome.BiomeEventImpl;
import projects.mods.ta.impl.events.biome.BiomeEvents;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class BiomeEventSuggestionProvider {

    public static class BiomeEventCommandSuggestion implements SuggestionProvider<ServerCommandSource>{

        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
            builder.suggest("start");
            builder.suggest("end");
            return builder.buildFuture();
        }
    }

    public static class AroundsTypeSuggestion implements SuggestionProvider<ServerCommandSource>{

        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {

            for(Enum<AroundsType> type: AroundsType.values()){
                builder.suggest(type.toString());
            }

            return builder.buildFuture();
        }
    }

    public static class BiomeEventSuggestion implements SuggestionProvider<ServerCommandSource>{

        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
            AroundsType type = AroundsType.getType(context.getArgument("type", String.class));

            for(Map.Entry<Identifier, BiomeEvent> e: BiomeEvents.getAllAvailableEvents().entrySet()){
                if(e.getValue().getType() == type){
                    builder.suggest(e.getKey().toString());
                }
            }


            return builder.buildFuture();
        }
    }



}
