package projects.mods.ta.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import projects.mods.ta.ThingsAround;
import projects.mods.ta.impl.AroundsType;
import static projects.mods.ta.impl.level.PlayerAroundsLevelControl.LEVEL_CONFIG;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;


public class ModConfigLoader {

    static final String mod_config = ThingsAround.MOD_ID + ".json";

    static final Gson gson = new Gson();
    static private final Path config_path = FabricLoader.getInstance().getModContainer(ThingsAround.MOD_ID).get().findPath("/").orElse(Path.of("/"));

    public static Map<AroundsType, Map<String, Integer>> getLevelingMap() {

        Map<AroundsType, Map<String, Integer>> map = new HashMap<>();

        Path file_path = config_path.resolve(mod_config);
        TypeToken<Map<String, Integer>> mapType = new TypeToken<>(){};
        try(Reader reader = Files.newBufferedReader(file_path)){
            JsonElement leveling = gson.fromJson(reader, JsonObject.class).get(LEVEL_CONFIG);
            for(Map.Entry<String, JsonElement> level_entry: leveling.getAsJsonObject().entrySet()){
                String type = level_entry.getKey();
                JsonElement element = level_entry.getValue();
                Map<String, Integer> m = gson.fromJson(element, mapType);
                map.put(AroundsType.getType(type), m);
            }
        }
        catch (IOException e){
            ThingsAround.LOGGER.error("Missing default config at: "+mod_config+": "+LEVEL_CONFIG);
        }
        return map;






    }

}
