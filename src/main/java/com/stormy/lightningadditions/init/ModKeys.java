package com.stormy.lightningadditions.init;

import com.stormy.lightningadditions.feature.KeyHideGui;
import com.stormy.lightningadditions.feature.calc.CalcKey;
import com.stormy.lightningadditions.feature.lightchunkutil.ChunkBoundariesHandler;
import com.stormy.lightningadditions.feature.lightchunkutil.LightChunkKeyBinds;
import com.stormy.lightningadditions.feature.lightchunkutil.LightOverlayHandler;

public class ModKeys {

    public static void init() {
        LightChunkKeyBinds.init();
        LightOverlayHandler.init();
        ChunkBoundariesHandler.init();
        CalcKey.init();
        KeyHideGui.init();
    }
}