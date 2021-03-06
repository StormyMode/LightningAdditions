/*
 * ********************************************************************************
 * Copyright (c) 2017 StormyMode, MiningMark48. All Rights Reserved!
 * This file is part of Lightning Additions (MC-Mod).
 *
 * This project cannot be copied and/or distributed without the express
 * permission of StormyMode, MiningMark48 (Developers)!
 * ********************************************************************************
 */

package com.stormy.lightningadditions.feature.lightchunkutil;

import com.stormy.lightningadditions.reference.ModInformation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class LightChunkKeyBinds {

    public static KeyBinding lightOverlay = new KeyBinding("key." + ModInformation.MODID + ".lightoverlay.desc", KeyConflictContext.IN_GAME, Keyboard.KEY_F7, "key." + ModInformation.MODID + ".category");
    public static KeyBinding chunkBounds = new KeyBinding("key." + ModInformation.MODID + ".chunkbounds.desc", KeyConflictContext.IN_GAME, Keyboard.KEY_F9, "key." + ModInformation.MODID + ".category");

    public static void init()
    {
        ClientRegistry.registerKeyBinding(lightOverlay);
        ClientRegistry.registerKeyBinding(chunkBounds);
        MinecraftForge.EVENT_BUS.register(new  LightChunkKeyBinds());
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(receiveCanceled = true)
    public void onKeyEvent(InputEvent.KeyInputEvent event) {
        if (lightOverlay.isPressed()) {
            LightOverlayHandler.toggleMode();
        }

        if (chunkBounds.isPressed()) {
            ChunkBoundariesHandler.toggleMode();
        }

    }

}
