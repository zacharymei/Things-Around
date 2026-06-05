package mod.zacharymei.command;

import com.google.gson.JsonElement;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import mod.zacharymei.ThingsAround;
import mod.zacharymei.impl.enchanment.DurationEnchantments;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Collection;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class DurationEnchantCommand {

    private static final DynamicCommandExceptionType ERROR_NOT_LIVING_ENTITY = new DynamicCommandExceptionType(
            target -> Component.translatableEscape("commands.enchant.failed.entity", target)
    );
    private static final DynamicCommandExceptionType ERROR_NO_ITEM = new DynamicCommandExceptionType(
            target -> Component.translatableEscape("commands.enchant.failed.itemless", target)
    );
    private static final DynamicCommandExceptionType ERROR_INCOMPATIBLE = new DynamicCommandExceptionType(
            item -> Component.translatableEscape("commands.enchant.failed.incompatible", item)
    );
    private static final Dynamic2CommandExceptionType ERROR_LEVEL_TOO_HIGH = new Dynamic2CommandExceptionType(
            (level, max) -> Component.translatableEscape("commands.enchant.failed.level", level, max)
    );
    private static final SimpleCommandExceptionType ERROR_NOTHING_HAPPENED = new SimpleCommandExceptionType(Component.translatable("commands.enchant.failed"));


    public static void initialize(){

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("duration").requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                    .then(argument("targets", EntityArgument.entities())
                            .then(argument("enchantment", ResourceArgument.resource(registryAccess, Registries.ENCHANTMENT))
                                    .executes((ctx)->execute(ctx, -1, -1))
                                    .then(argument("level", IntegerArgumentType.integer(1))
                                            .executes((ctx)->execute(ctx, IntegerArgumentType.getInteger(ctx, "level"), -1))
                                            .then(argument("duration in second", IntegerArgumentType.integer(1))
                                                    .executes((ctx)->execute(ctx, IntegerArgumentType.getInteger(ctx, "level"), IntegerArgumentType.getInteger(ctx, "duration in second")))))
                            )));
        });

    }

    public static int execute(CommandContext<CommandSourceStack> ctx, int level, int duration) throws CommandSyntaxException{
        Collection<? extends Entity> targets = EntityArgument.getEntities(ctx, "targets");
        final Holder<Enchantment> enchantmentHolder = ResourceArgument.getEnchantment(ctx, "enchantment");
        Enchantment enchantment = enchantmentHolder.value();

        RegistryAccess registies = ctx.getSource().registryAccess();




        int success = 0;
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity target) {
                ItemStack item = target.getMainHandItem();
                if (!item.isEmpty()) {
                    if (enchantment.canEnchant(item)) {
                        DurationEnchantments.enchant(item, enchantmentHolder, level, duration);
                        DurationEnchantments.enchant2(item, enchantmentHolder, level, duration, registies);
                        success++;
                    } else if (targets.size() == 1) {
                        throw ERROR_INCOMPATIBLE.create(item.getHoverName().getString());
                    }
                } else if (targets.size() == 1) {
                    throw ERROR_NO_ITEM.create(target.getName().getString());
                }
            } else if (targets.size() == 1) {
                throw ERROR_NOT_LIVING_ENTITY.create(entity.getName().getString());
            }
        }

        if (success == 0) {
            throw ERROR_NOTHING_HAPPENED.create();
        } else {
            if (targets.size() == 1) {
                ctx.getSource().sendSuccess(
                        () -> Component.translatable(
                                "commands.enchant.success.single", Enchantment.getFullname(enchantmentHolder, level), ((Entity)targets.iterator().next()).getDisplayName()
                        ),
                        true
                );
            } else {
                ctx.getSource().sendSuccess(
                        () -> Component.translatable("commands.enchant.success.multiple", Enchantment.getFullname(enchantmentHolder, level), targets.size()), true
                );
            }

            return success;
        }

        // return 1;
    }

}
