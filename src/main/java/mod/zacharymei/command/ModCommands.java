package mod.zacharymei.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ModCommands {

    public static void initialize(){

        DurationEnchantCommand.initialize();
        CommandRegistrationCallback.EVENT.register(BiomeEventCommand::command);

    }
}
