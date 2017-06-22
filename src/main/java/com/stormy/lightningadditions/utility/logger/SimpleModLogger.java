/*
 * ********************************************************************************
 * Copyright (c) 2017 StormyMode, MiningMark48. All Rights Reserved!
 * This file is part of Lightning Additions (MC-Mod).
 *
 * This project cannot be copied and/or distributed without the express
 * permission of StormyMode, MiningMark48 (Developers)!
 * ********************************************************************************
 */

package com.stormy.lightningadditions.utility.logger;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;

public class SimpleModLogger implements IModLogger {
    private String modName;

    public SimpleModLogger(FMLPreInitializationEvent event) {
        this.modName = event.getModMetadata().name;
    }

    public void logDebug(Object message) {
        this.log(message, Level.DEBUG);
    }

    public void logInfo(Object message) {
        this.log(message, Level.INFO);
    }

    public void logWarn(Object message) {
        this.log(message, Level.WARN);
    }

    public void logError(Object message) {
        this.log(message, Level.ERROR);
    }

    private void log(Object message, Level level) {
        FMLLog.log(this.modName, level, String.valueOf(message), new Object[0]);
    }
}