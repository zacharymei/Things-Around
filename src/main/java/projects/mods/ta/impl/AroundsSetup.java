package projects.mods.ta.impl;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import projects.mods.ta.impl.level.ForestLevelTrigger;
import projects.mods.ta.impl.level.PlayerAroundsLevelControl;

public class AroundsSetup implements ServerLifecycleEvents.ServerStarted {

    public PlayerAroundsLevelControl Leveling_control;

    public AroundsSetup() {
        ServerLifecycleEvents.SERVER_STARTED.register(this);
        setup();
    }

    private void setup(){
        initLeveling();
    }

    @Override
    public void onServerStarted(MinecraftServer server) {
        this.Leveling_control = new PlayerAroundsLevelControl();
    }

    private void initLeveling(){
        new ForestLevelTrigger();
    }
}
