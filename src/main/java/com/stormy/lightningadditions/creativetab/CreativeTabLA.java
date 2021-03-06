/*
 * ********************************************************************************
 * Copyright (c) 2017 StormyMode, MiningMark48. All Rights Reserved!
 * This file is part of Lightning Additions (MC-Mod).
 *
 * This project cannot be copied and/or distributed without the express
 * permission of StormyMode, MiningMark48 (Developers)!
 * ********************************************************************************
 */

package com.stormy.lightningadditions.creativetab;

import com.stormy.lightningadditions.config.ConfigurationManagerLA;
import com.stormy.lightningadditions.init.ModItems;
import com.stormy.lightningadditions.reference.ModInformation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTabLA{

    public static final CreativeTabs LA_TAB = new CreativeTabs(ModInformation.MODID) {

        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ModItems.tachyon_shard);
        }

        @Override
        public boolean hasSearchBar() {
            return ConfigurationManagerLA.creativeTabSearchBar;
        }
    };

    public static final CreativeTabs LA_TAB_ORES = new CreativeTabs(ModInformation.MODID + ".ores") {

        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ModItems.SILVER_INGOT);
        }

        @Override
        public boolean hasSearchBar() {
            return ConfigurationManagerLA.creativeTabSearchBar;
        }
    };

}
