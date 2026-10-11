package net.fanya.lowdurabilityswitcher.util;

import com.mojang.blaze3d.platform.InputConstants;
//? if >=26.1 {
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
//?} else {
/*import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;*/
//?}
import net.minecraft.client.KeyMapping;
//? if <1.21.11 {
/*import net.minecraft.resources.ResourceLocation;*/
//?} else {
import net.minecraft.resources.Identifier;
//?}

public class KeyBindings {
    //? if >=1.21.9 {
    private static final KeyMapping.Category LOW_DURABILITY_SWITCHER_CATEGORY = KeyMapping.Category.register(
            //? if <1.21.11 {
            /*ResourceLocation.fromNamespaceAndPath("lowdurabilityswitcher", "main")*/
            //?} else {
            Identifier.fromNamespaceAndPath("lowdurabilityswitcher", "main")
            //?}
    );
    //?} else {
    /*private static final String LOW_DURABILITY_SWITCHER_CATEGORY = "key.category.lowdurabilityswitcher.main";*/
    //?}

    public static KeyMapping toggleToolKey;
    public static KeyMapping toggleArmorKey;

    public static void initialize() {
        //? if >=26.1 {
        toggleToolKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
        //?} else {
        /*toggleToolKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(*/
        //?}
                "key.lowdurabilityswitcher.toggle_tool",
                //? if >=26.3 {
                InputConstants.Type.KEYBOARD,
                //?} else {
                /*InputConstants.Type.KEYSYM,*/
                //?}
                InputConstants.KEY_R,
                LOW_DURABILITY_SWITCHER_CATEGORY
        ));

        //? if >=26.1 {
        toggleArmorKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
        //?} else {
        /*toggleArmorKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(*/
        //?}
                "key.lowdurabilityswitcher.toggle_armor",
                //? if >=26.3 {
                InputConstants.Type.KEYBOARD,
                //?} else {
                /*InputConstants.Type.KEYSYM,*/
                //?}
                InputConstants.KEY_G,
                LOW_DURABILITY_SWITCHER_CATEGORY
        ));
    }
}
