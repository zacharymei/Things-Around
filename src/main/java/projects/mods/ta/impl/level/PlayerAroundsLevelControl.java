package projects.mods.ta.impl.level;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.entity.player.PlayerEntity;
import projects.mods.ta.impl.AroundsType;
import projects.mods.ta.impl.arounds.AroundsLevelTracker;
import projects.mods.ta.impl.types.IAroundsType;
import projects.mods.ta.util.ModConfigLoader;

import java.util.HashMap;
import java.util.Map;

public class PlayerAroundsLevelControl {

    public static final String LEVEL_CONFIG = "Leveling";
    Map<AroundsType, Map<String, Integer>> level_map;

    public PlayerAroundsLevelControl(){
        this.level_map = initLevelMap();
    }

    private Map<AroundsType, Map<String, Integer>> initLevelMap(){
        return ModConfigLoader.getLevelingMap();
    }

    protected void update(PlayerEntity player, String action){
        AroundsType type = ((IAroundsType) this).getType();
        int amount = (level_map.containsKey(type))? level_map.get(type).getOrDefault(action, 0): 0;
        AroundsLevelTracker.getPlayer(player).addLevel(type, amount);
    }

}
