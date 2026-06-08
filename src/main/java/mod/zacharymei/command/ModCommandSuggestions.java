package mod.zacharymei.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import mod.zacharymei.impl.events.biomeevent.ModBiomeEvents;
import net.minecraft.commands.CommandSourceStack;

import java.util.concurrent.CompletableFuture;

public class ModCommandSuggestions {

    public static class BiomeEventActions implements SuggestionProvider<CommandSourceStack> {

        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) throws CommandSyntaxException {
            return builder.suggest("start").suggest("end").buildFuture();
        }
    }


    public static class BiomeEventTypes implements SuggestionProvider<CommandSourceStack> {

        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) throws CommandSyntaxException {
            return builder.suggest("test").buildFuture();
        }
    }

    public static class BiomeEvents implements SuggestionProvider<CommandSourceStack> {

        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) throws CommandSyntaxException {
            for(ModBiomeEvents biomeEvent : ModBiomeEvents.values()) {
                builder.suggest(biomeEvent.name());
            }
            return builder.buildFuture();
        }
    }

}
