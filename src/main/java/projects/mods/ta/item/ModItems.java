package projects.mods.ta.item;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import projects.mods.ta.ThingsAround;

public class ModItems implements ModInitializer {
    public static final Item TOUGH_BARK = Registry.register(Registries.ITEM, new Identifier("things-around", "tough_bark"),
            new ToughBarkItem(new Item.Settings()));

    private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
            .displayName(Text.translatable("itemGroup.things-around"))
            .entries((displayContext, entries) -> {
                entries.add(TOUGH_BARK);
            }).build();

    @Override
    public void onInitialize() {

        Registry.register(Registries.ITEM_GROUP, new Identifier(ThingsAround.MOD_ID, "item_group"), ITEM_GROUP);

        FuelRegistry.INSTANCE.add(TOUGH_BARK, 300);
    }


}
