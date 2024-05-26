package projects.mods.ta.item;

import net.minecraft.item.Item;

public class ModItem extends Item {
    public ModItem(Settings settings) {
        super(settings);
    }

    public ModItem(){
        super(new Item.Settings());
    }
}
