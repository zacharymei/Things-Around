package projects.mods.ta.base.item;


import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import projects.mods.ta.base.block.ModBlocks;

import static projects.mods.ta.ThingsAround.MOD_ID;

public class ModItems {
    public static final Item TOUGH_BARK = Registry.register(Registries.ITEM, new Identifier("things-around", "tough_bark"),
            new ToughBarkItem(new Item.Settings()));

    public static final Item LUCK_LEAVES = Registry.register(Registries.ITEM, new Identifier(MOD_ID, "luck_leaves"), new BlockItem(ModBlocks.LUCK_LEAVES, new Item.Settings()));

    private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
            .displayName(Text.translatable("itemGroup.things-around"))
            .entries((displayContext, entries) -> {
                entries.add(TOUGH_BARK);
            }).build();


    public static void register(){
        Registry.register(Registries.ITEM_GROUP, new Identifier(MOD_ID, "item_group"), ITEM_GROUP);


        FuelRegistry.INSTANCE.add(TOUGH_BARK, 300);
    }




}
