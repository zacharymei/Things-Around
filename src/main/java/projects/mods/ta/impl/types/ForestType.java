package projects.mods.ta.impl.types;

import projects.mods.ta.impl.AroundsType;

public interface ForestType extends IAroundsType {

    AroundsType type = AroundsType.FOREST;

    @Override
    default AroundsType getType(){
        return type;
    }
}
