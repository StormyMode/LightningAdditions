/*
 * ********************************************************************************
 * Copyright (c) 2017 StormyMode, MiningMark48. All Rights Reserved!
 * This file is part of Lightning Additions (MC-Mod).
 *
 * This project cannot be copied and/or distributed without the express
 * permission of StormyMode, MiningMark48 (Developers)!
 * ********************************************************************************
 */

package com.stormy.lightningadditions.block.ore;


import com.stormy.lightningadditions.compat.ModChecker;
import com.stormy.lightningadditions.init.ModBlocks;
import com.stormy.lightningadditions.init.ModItems;
import com.stormy.lightningadditions.utility.logger.ConfigurationManagerLA;
import ic2.api.item.IC2Items;
import ic2.api.recipe.Recipes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Recipes to get the ingots and stuff out of the ore, if they didn't drop normally, or if silk touch was used.
 * @author Rohzek
 *
 */
public class SmeltingRecipes
{
    public static void mainRegistry()
    {
        addSmeltingRecipes();

        if(ModChecker.check("ic2"))
        {
            moddedRecipes();
        }
    }

    private static void addSmeltingRecipes()
    {
        if(ConfigurationManagerLA.straight2Ingots)
        {
            GameRegistry.addSmelting(ModBlocks.NETHER_COAL_ORE, new ItemStack(Items.COAL), 0.1f);
            GameRegistry.addSmelting(ModBlocks.NETHER_DIAMOND_ORE, new ItemStack(Items.DIAMOND), 1f);
            GameRegistry.addSmelting(ModBlocks.NETHER_EMERALD_ORE, new ItemStack(Items.EMERALD), 1f);
            GameRegistry.addSmelting(ModBlocks.NETHER_IRON_ORE, new ItemStack(Items.IRON_INGOT), 0.7f);
            GameRegistry.addSmelting(ModBlocks.NETHER_GOLD_ORE, new ItemStack(Items.GOLD_INGOT), 1f);
            GameRegistry.addSmelting(ModBlocks.NETHER_LAPIS_ORE, new ItemStack(Items.DYE), 0.2f);
            GameRegistry.addSmelting(ModBlocks.NETHER_REDSTONE_ORE, new ItemStack(Items.REDSTONE), 0.7f);
        }
        else
        {
            GameRegistry.addSmelting(ModBlocks.NETHER_COAL_ORE, new ItemStack(Blocks.COAL_ORE), 0.1f);
            GameRegistry.addSmelting(ModBlocks.NETHER_DIAMOND_ORE, new ItemStack(Blocks.DIAMOND_ORE), 1f);
            GameRegistry.addSmelting(ModBlocks.NETHER_EMERALD_ORE, new ItemStack(Blocks.EMERALD_ORE), 1f);
            GameRegistry.addSmelting(ModBlocks.NETHER_IRON_ORE, new ItemStack(Blocks.IRON_ORE), 0.7f);
            GameRegistry.addSmelting(ModBlocks.NETHER_GOLD_ORE, new ItemStack(Blocks.GOLD_ORE), 1f);
            GameRegistry.addSmelting(ModBlocks.NETHER_LAPIS_ORE, new ItemStack(Blocks.LAPIS_ORE), 0.2f);
            GameRegistry.addSmelting(ModBlocks.NETHER_REDSTONE_ORE, new ItemStack(Blocks.REDSTONE_ORE), 0.7f);
        }
    }

    public static void moddedRecipes()
    {
        GameRegistry.addSmelting(ModBlocks.OVERWORLD_COPPER_ORE, new ItemStack(ModItems.COPPER_INGOT), 0.7f);
        GameRegistry.addSmelting(ModBlocks.OVERWORLD_LEAD_ORE, new ItemStack(ModItems.LEAD_INGOT), 0.7f);
        GameRegistry.addSmelting(ModBlocks.OVERWORLD_TIN_ORE, new ItemStack(ModItems.TIN_INGOT), 0.7f);

        if(ConfigurationManagerLA.straight2Ingots)
        {


            // Nether
            GameRegistry.addSmelting(ModBlocks.NETHER_COPPER_ORE, new ItemStack(ModItems.COPPER_INGOT), 0.7f);
            GameRegistry.addSmelting(ModBlocks.NETHER_LEAD_ORE, new ItemStack(ModItems.LEAD_INGOT), 0.7f);
            GameRegistry.addSmelting(ModBlocks.NETHER_TIN_ORE, new ItemStack(ModItems.TIN_INGOT), 0.7f);
        }
        else
        {

            // Nether
            GameRegistry.addSmelting(ModBlocks.NETHER_COPPER_ORE, new ItemStack(ModBlocks.OVERWORLD_COPPER_ORE), 0.7f);
            GameRegistry.addSmelting(ModBlocks.NETHER_LEAD_ORE, new ItemStack(ModBlocks.OVERWORLD_LEAD_ORE), 0.7f);
            GameRegistry.addSmelting(ModBlocks.NETHER_TIN_ORE, new ItemStack(ModBlocks.OVERWORLD_TIN_ORE), 0.7f);
        }
    }

    @Optional.Method(modid = "ic2")
    public static void addMaceratorRecipes()
    {
        NBTTagCompound compound = new NBTTagCompound();

        // Surface Ores
        Recipes.macerator.addRecipe(new MaceratorRecipeInput(new ItemStack(ModBlocks.OVERWORLD_COPPER_ORE)), compound, false, IC2Items.getItem("crushed", "copper"), IC2Items.getItem("crushed", "copper"));
        Recipes.macerator.addRecipe(new MaceratorRecipeInput(new ItemStack(ModBlocks.OVERWORLD_LEAD_ORE)), compound, false, IC2Items.getItem("crushed", "lead"), IC2Items.getItem("crushed", "lead"));
        Recipes.macerator.addRecipe(new MaceratorRecipeInput(new ItemStack(ModBlocks.OVERWORLD_TIN_ORE)), compound, false, IC2Items.getItem("crushed", "tin"), IC2Items.getItem("crushed", "tin"));

        // Nether Ores
        Recipes.macerator.addRecipe(new MaceratorRecipeInput(new ItemStack(ModBlocks.NETHER_COPPER_ORE)), compound, false, IC2Items.getItem("crushed", "copper"), IC2Items.getItem("crushed", "copper"));
        Recipes.macerator.addRecipe(new MaceratorRecipeInput(new ItemStack(ModBlocks.NETHER_GOLD_ORE)), compound, false, IC2Items.getItem("crushed", "gold"), IC2Items.getItem("crushed", "gold"));
        Recipes.macerator.addRecipe(new MaceratorRecipeInput(new ItemStack(ModBlocks.NETHER_IRON_ORE)), compound, false, IC2Items.getItem("crushed", "iron"), IC2Items.getItem("crushed", "iron"));
        Recipes.macerator.addRecipe(new MaceratorRecipeInput(new ItemStack(ModBlocks.NETHER_LEAD_ORE)), compound, false, IC2Items.getItem("crushed", "lead"), IC2Items.getItem("crushed", "lead"));
        Recipes.macerator.addRecipe(new MaceratorRecipeInput(new ItemStack(ModBlocks.NETHER_TIN_ORE)), compound, false, IC2Items.getItem("crushed", "tin"), IC2Items.getItem("crushed", "tin"));


    }
}