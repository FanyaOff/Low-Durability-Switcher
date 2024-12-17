package net.fanya.lowdurabilityswitcher.util;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static KeyBinding toggleToolKey;
    public static KeyBinding toggleArmorKey;

    public static void initialize() {
        toggleToolKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.lowdurabilityswitcher.toggle_tool",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.lowdurabilityswitcher"
        ));

        toggleArmorKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.lowdurabilityswitcher.toggle_armor",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "category.lowdurabilityswitcher"
        ));
    }
}
