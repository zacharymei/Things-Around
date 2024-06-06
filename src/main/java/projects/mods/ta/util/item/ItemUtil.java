package projects.mods.ta.util.item;

import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;

import java.util.*;
import java.util.function.Predicate;

public class ItemUtil {

    MinecraftServer server;

    private static final String RECIPES_KEY = "Recipes";


    public ItemUtil(MinecraftServer server) {
        this.server = server;
    }


    public boolean isMaterial(ItemStack stack, Predicate<ItemStack> predicate){

        if(predicate.test(stack)) return true;
        Set<ItemStack> visited = new HashSet<>();
        return isMaterial(stack, predicate, visited);

    }

    public boolean isMaterial(ItemStack stack, Predicate<ItemStack> predicate, Set<ItemStack> visited){

        if(predicate.test(stack)) return true;

        for(RecipeEntry<?> entry: getRecipes(stack, RecipeType.CRAFTING)){
            for(Ingredient ingredient: entry.value().getIngredients()){
                for(ItemStack ingredient_stack: ingredient.getMatchingStacks()){
                    if(visited.contains(ingredient_stack)) continue;
                    visited.add(ingredient_stack);
                    if(isMaterial(ingredient_stack, predicate, visited)) return true;

                }
            }
        }
        return false;

    }


    public List<RecipeEntry<CraftingRecipe>> getRecipes(ItemStack stack, RecipeType<CraftingRecipe> type){
        List<RecipeEntry<CraftingRecipe>> list = new ArrayList<>();
        for(RecipeEntry<CraftingRecipe> entry: server.getRecipeManager().listAllOfType(type)){
            if(entry.value().getResult(server.getRegistryManager()).isOf(stack.getItem())) list.add(entry);
        }
        return list;
    }

}
