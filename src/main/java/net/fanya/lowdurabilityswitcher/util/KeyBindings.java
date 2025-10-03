package net.fanya.lowdurabilityswitcher.util;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    private static final KeyBinding.Category LOW_DURABILITY_SWITCHER_CATEGORY = KeyBinding.Category.create(
            Identifier.of("lowdurabilityswitcher", "main")
    );

    public static KeyBinding toggleToolKey;
    public static KeyBinding toggleArmorKey;

    public static void initialize() {
        toggleToolKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.lowdurabilityswitcher.toggle_tool",
                InputUtil.Type.KEYSYM,
                InputUtil.GLFW_KEY_R,
                LOW_DURABILITY_SWITCHER_CATEGORY
        ));

        toggleArmorKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.lowdurabilityswitcher.toggle_armor",
                InputUtil.Type.KEYSYM,
                InputUtil.GLFW_KEY_G,
                LOW_DURABILITY_SWITCHER_CATEGORY
        ));
    }
}
