/*
 * ********************************************************************************
 * Copyright (c) 2017 StormyMode, MiningMark48. All Rights Reserved!
 * This file is part of Lightning Additions (MC-Mod).
 *
 * This project cannot be copied and/or distributed without the express
 * permission of StormyMode, MiningMark48 (Developers)!
 * ********************************************************************************
 */

package com.stormy.lightningadditions.item.resource;

import com.stormy.lightningadditions.init.ModItems;
import com.stormy.lightningadditions.init.ModSounds;
import com.stormy.lightninglib.lib.item.ItemBase;
import com.stormy.lightninglib.lib.utils.KeyChecker;
import com.stormy.lightninglib.lib.utils.TranslateUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPhiloStone extends ItemBase
{
    public static int maxDamage = 2500;

    public ItemPhiloStone() {
        this.setMaxDamage(2500);
        this.setMaxStackSize(1);
        this.setContainerItem(this);
    }

    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
        if (par1ItemStack.getItemDamage() < par1ItemStack.getMaxDamage()) {
            par1ItemStack.setItemDamage(par1ItemStack.getItemDamage() - 1);
            //System.out.println(par1ItemStack.getItemDamage());
        }

        if (par5 && (par3Entity instanceof EntityPlayer)) {
            EntityPlayer entityplayer = (EntityPlayer) par3Entity;
            if (!par2World.isRemote && !entityplayer.capabilities.isCreativeMode) {
                if (entityplayer.getHeldItemMainhand().getItem() == ModItems.philosopher_stone) {
                    if (par1ItemStack.getItemDamage() > 0) {
                        entityplayer.fallDistance = 0.0F;
                    }
                }
            }
        }
    }

    //TODO Transmutation Sound Effect on Right-Click
    //TODO "Savitar Features" -- The Flash CW TV Show

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        player.playSound(ModSounds.philosopher_stone, 0.8f, 1.0f);
        worldIn.spawnParticle(EnumParticleTypes.CRIT_MAGIC, player.posX, player.posY + 0.5 , player.posZ, 0.0D, 0.0D, 0.0D);

        //Equal Block Transmutations
             if (worldIn.getBlockState(pos).getBlock() == Blocks.GRASS) { worldIn.setBlockState(pos, Blocks.DIRT.getDefaultState()); }
        else if (worldIn.getBlockState(pos).getBlock() == Blocks.DIRT) { worldIn.setBlockState(pos, Blocks.GRAVEL.getDefaultState()); }
        else if (worldIn.getBlockState(pos).getBlock() == Blocks.GRAVEL) { worldIn.setBlockState(pos, Blocks.SAND.getDefaultState()); }
        else if (worldIn.getBlockState(pos).getBlock() == Blocks.SAND) { worldIn.setBlockState(pos, Blocks.GRASS.getDefaultState()); }

        //Water Transmutations
        else if (worldIn.getBlockState(pos).getBlock() == Blocks.WATER) { worldIn.setBlockState(pos, Blocks.SNOW.getDefaultState()); }
        else if (worldIn.getBlockState(pos).getBlock() == Blocks.SNOW) { worldIn.setBlockState(pos, Blocks.ICE.getDefaultState()); }
        else if (worldIn.getBlockState(pos).getBlock() == Blocks.ICE) { worldIn.setBlockState(pos, Blocks.FROSTED_ICE.getDefaultState()); }
        else if (worldIn.getBlockState(pos).getBlock() == Blocks.FROSTED_ICE) { worldIn.setBlockState(pos, Blocks.PACKED_ICE.getDefaultState()); }
        else if (worldIn.getBlockState(pos).getBlock() == Blocks.PACKED_ICE) { worldIn.setBlockState(pos, Blocks.WATER.getDefaultState()); }

        //Lava Transmutations
             else if (worldIn.getBlockState(pos).getBlock() == Blocks.LAVA) { worldIn.setBlockState(pos, Blocks.MAGMA.getDefaultState()); }
             else if (worldIn.getBlockState(pos).getBlock() == Blocks.MAGMA) { worldIn.setBlockState(pos, Blocks.LAVA.getDefaultState()); }
             //TODO More Transmutations Methods

        //Misc Transmutations
             else if (worldIn.getBlockState(pos).getBlock() == Blocks.END_STONE) { worldIn.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState()); }
             else if (worldIn.getBlockState(pos).getBlock() == Blocks.OBSIDIAN) { worldIn.setBlockState(pos, Blocks.END_STONE.getDefaultState()); }

             else if (worldIn.getBlockState(pos).getBlock() == Blocks.COBBLESTONE) { worldIn.setBlockState(pos, Blocks.MOSSY_COBBLESTONE.getDefaultState()); }
             else if (worldIn.getBlockState(pos).getBlock() == Blocks.MOSSY_COBBLESTONE) { worldIn.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState()); }

        return null;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, @Nullable World world, List par3List, ITooltipFlag par4) {
        if (KeyChecker.isHoldingShift())
        { par3List.add(TextFormatting.ITALIC + TranslateUtils.toLocal("tooltip.item.philosopher_stone.line1"));
        }
        else{ par3List.add(TranslateUtils.toLocal("tooltip.item.hold") + " " + TextFormatting.AQUA + TextFormatting.ITALIC + TranslateUtils.toLocal("tooltip.item.shift")); }
    }
}
