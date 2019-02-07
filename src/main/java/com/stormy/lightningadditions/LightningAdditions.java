/*
 * ********************************************************************************
 * Copyright (c) 2017-2019 StormyMode. All Rights Reserved!
 * This file is part of Lightning Additions (Modification for MC).
 *
 * This project cannot be copied and/or distributed without the express
 * permission of StormyMode (Lead Developer)!
 * ********************************************************************************
 */

package com.stormy.lightningadditions;

import com.stormy.lightningadditions.block.ore.OreDictTooltipEvent;
import com.stormy.lightningadditions.config.ConfigurationManagerLA;
import com.stormy.lightningadditions.crafting.RegistryParticleAccelerator;
import com.stormy.lightningadditions.feature.debug.CommandReloadPARecipes;
import com.stormy.lightningadditions.feature.debug.CommandToggleDownfall;
import com.stormy.lightningadditions.feature.debug.CommandUUID;
import com.stormy.lightningadditions.feature.harvest.Harvest;
import com.stormy.lightningadditions.feature.lightchunkutil.ConfigHandler;
import com.stormy.lightningadditions.handler.LACapabilityHandler;
import com.stormy.lightningadditions.handler.fatality.FatalityEventHandler;
import com.stormy.lightningadditions.handler.generator.BioFuelRegistry;
import com.stormy.lightningadditions.handler.ritual.EventHandlerRitualCommon;
import com.stormy.lightningadditions.init.*;
import com.stormy.lightningadditions.network.GuiHandler;
import com.stormy.lightningadditions.proxy.CommonProxy;
import com.stormy.lightningadditions.reference.ModInformation;
import com.stormy.lightningadditions.utility.logger.LALogger;
import com.stormy.lightningadditions.utility.xpshare.CPacketRequest;
import com.stormy.lightningadditions.utility.xpshare.SPacketUpdate;
import com.stormy.lightningadditions.world.WorldGen;
import com.stormy.lightningadditions.world.jsonhelper.JsonLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

import static com.stormy.lightningadditions.reference.ModInformation.MODID;
import static com.stormy.lightningadditions.reference.ModInformation.MODNAME;

@Mod(   modid = MODID,
        name = MODNAME,
        version = ModInformation.VERSION,
        acceptedMinecraftVersions = ModInformation.acceptedMinecraftVersions )

public class LightningAdditions
{
    @Instance(ModInformation.MODID)
    public static LightningAdditions INSTANCE;
    @SidedProxy(clientSide = ModInformation.ClientProxy, serverSide = ModInformation.CommonProxy)
    public static CommonProxy proxy;
    public static SimpleNetworkWrapper network;
    public static final Logger logger = LogManager.getLogger(MODID);

    public LightningAdditions() {}

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LALogger.log("LA Pre-Initialisation!");
        proxy.preInit(event);

        ModInformation.LOCATION = new File(event.getModConfigurationDirectory().getAbsolutePath() + "/" + ModInformation.MODID);
        JsonLoader.loadData();
        ConfigurationManagerLA manager = new ConfigurationManagerLA(event);
        ConfigHandler.init(event);

        //General
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new EventHandlerRitualCommon());
        MinecraftForge.EVENT_BUS.register(new FatalityEventHandler());

        ModDimensions.init(); //Dimensions

        //XP Network Sharing
        network = NetworkRegistry.INSTANCE.newSimpleChannel(ModInformation.MODID);
        network.registerMessage(new SPacketUpdate.Handler(), SPacketUpdate.class, 0, Side.CLIENT);
        network.registerMessage(new CPacketRequest.Handler(), CPacketRequest.class, 1, Side.SERVER);

        Harvest.preInit(); //Right-Click harvesting

        //Mod Content Implementation
        ModTileEntities.init();
        ModItems.init();
        ModBlocks.init();
        ModSounds.registerSounds();
        ModRegistry.init();

        MinecraftForge.EVENT_BUS.register(new ModRegistry());
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        MinecraftForge.EVENT_BUS.register(new ModSounds());

        ModOreDict.registerOres();

    }


    @EventHandler
    public void init(FMLInitializationEvent event) {
        LALogger.log("LA Initialisation!");
        proxy.registerRenders();
        proxy.init(event);

        ModRecipes.init();
        RegistryParticleAccelerator.instance().registerRecipes();

        GameRegistry.registerWorldGenerator(new WorldGen(), 0);

        BioFuelRegistry.init(); //Initialize fuels for the Biofuel generator
        LACapabilityHandler.register();

    }


    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        LALogger.log("LA Post-Initialisation!");
        proxy.postInit(event);

        MinecraftForge.EVENT_BUS.register(new OreDictTooltipEvent()); //Shows OreDict tooltips
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandToggleDownfall()); //The Command that never works usually -.-
        event.registerServerCommand(new CommandReloadPARecipes()); //Reload PA-Machine Recipes
    }

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    public void serverStartingClientOnly(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandUUID()); //Fetch and Copy PlayerUUID/Username
    }

}
