package projects.mods.ta.misc;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import projects.mods.ta.event.MouseScrollCallback;
import projects.mods.ta.network.ToolToggleAction;

public class KeyBindEntrypoint implements ClientModInitializer {

    private static KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.tooltoggle.toggleNext",
            InputUtil.Type.KEYSYM,
            InputUtil.GLFW_KEY_LEFT_CONTROL,
            "category.tooltoggle"
    ));

    @Override
    public void onInitializeClient() {

        MouseScrollCallback.EVENT.register((long window, double horizontal, double vertical)->{

            if(keyBinding.isPressed()){
                System.out.println("Scrolling..");
                System.out.println(horizontal);
                System.out.println(vertical);
                return true;
            }
            return false;

        });

//        ClientTickEvents.END_CLIENT_TICK.register((client)->{
//
//            boolean ms = client.options.getDiscreteMouseScroll().getValue();
//            double ss = client.options.getMouseWheelSensitivity().getValue();
//            System.out.println(ms);
//            System.out.println(ss);
//
//            if(keyBinding.isPressed()){
////                ClientPlayNetworking.send(new Identifier("others", "tooltoggle"),
////                        PacketByteBufs.create().writeEnumConstant(ToolToggleAction.HOLD));
//
//                ms = client.options.getDiscreteMouseScroll().getValue();
//                ss = client.options.getMouseWheelSensitivity().getValue();
//                System.out.println(ms);
//                System.out.println(ss);
//
//            }
//
//
//        });
//
//
//

    }
}
