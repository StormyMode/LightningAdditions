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

import com.stormy.lightningadditions.config.ConfigurationManagerLA;
import com.stormy.lightninglib.lib.utils.KeyChecker;
import com.stormy.lightninglib.lib.utils.TranslateUtils;
import de.kitsunealex.frame.item.ItemBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemAtomicMagnet extends ItemBase
{

    public ItemAtomicMagnet(){
        setMaxStackSize(1);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List list, ITooltipFlag par4) {
        if (KeyChecker.isHoldingShift()) {
            if (!stack.hasTagCompound()) {
                stack.setTagCompound(new NBTTagCompound());
                stack.getTagCompound().setBoolean("enabled", false);
                stack.getTagCompound().setString("mode", "Attracts");
            }
            list.add(TextFormatting.GREEN + TranslateUtils.toLocal("tooltip.item.atomic_magnet.line4") + " " + TextFormatting.YELLOW + stack.getTagCompound().getString("mode"));
            list.add(TranslateUtils.toLocal("tooltip.item.atomic_magnet.line2.p1") + " " + ConfigurationManagerLA.atomicMagnetRange + " " + TranslateUtils.toLocal("tooltip.item.atomic_magnet.line2.p2"));
            list.add(TranslateUtils.toLocal("tooltip.item.atomic_magnet.line3"));

        }else{
            list.add(TranslateUtils.toLocal("tooltip.item.hold") + " " + TextFormatting.AQUA + TextFormatting.ITALIC + TranslateUtils.toLocal("tooltip.item.shift"));
        }

    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand handIn) {
        ItemStack stack = player.getHeldItem(handIn);
        if (!stack.hasTagCompound()){
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setString("mode", "Attracts");
        }

        if (!world.isRemote) {
            if(!player.isSneaking()) {
                if (stack.getTagCompound().getBoolean("enabled")) {
                    stack.getTagCompound().setBoolean("enabled", false);
                    player.sendMessage(new TextComponentString(TextFormatting.DARK_RED + TranslateUtils.toLocal("chat.item.atomic_magnet.disabled")));
                } else {
                    stack.getTagCompound().setBoolean("enabled", true);
                    player.sendMessage(new TextComponentString(TextFormatting.GOLD + TranslateUtils.toLocal("chat.item.atomic_magnet.enabled")));
                }
            }else{
                if (stack.getTagCompound().getString("mode").equalsIgnoreCase("attracts")) {
                    stack.getTagCompound().setString("mode", "Repels");
                    player.sendMessage(new TextComponentString(TextFormatting.RED + TranslateUtils.toLocal("chat.item.atomic_magnet.repels")));
                } else {
                    stack.getTagCompound().setString("mode", "Attracts");
                    player.sendMessage(new TextComponentString(TextFormatting.GREEN + TranslateUtils.toLocal("chat.item.atomic_magnet.attracts")));
                }
            }
        }

        return new ActionResult(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected){
        doUpdate(stack, world, entity);
    }



    private void doUpdate(ItemStack stack, World world, Entity entity){

        int range = ConfigurationManagerLA.atomicMagnetRange;
        float pullSpeed = ConfigurationManagerLA.atomicMagnetPullSpeed;

        if (!stack.hasTagCompound()){
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setBoolean("enabled", false);
            stack.getTagCompound().setString("mode", "Attracts");
        }

        if(entity instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) entity;

                double x = player.posX;
                double y = player.posY;
                double z = player.posZ;

                List<EntityItem> items = entity.world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(x - range, y - range, z - range, x + range, y + range, z + range));
                List<EntityXPOrb> xp = entity.world.getEntitiesWithinAABB(EntityXPOrb.class, new AxisAlignedBB(x - range, y - range, z - range, x + range, y + range, z + range));
                for (EntityItem e: items){
                    if (!player.isSneaking()){

                        if (stack.getTagCompound().getString("mode").equalsIgnoreCase("attracts")) {
                            e.addVelocity((x - e.posX) * pullSpeed, (y - e.posY) * pullSpeed, (z - e.posZ) * pullSpeed); //Attracts
                        }else {
                            e.addVelocity((e.posX - x) * pullSpeed, (e.posY - y) * pullSpeed, (e.posZ - z) * pullSpeed); //Repels
                        }

                        if (ConfigurationManagerLA.atomicMagnetParticles) {
                            world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, e.posX, e.posY + 0.3, e.posZ, 0.0D, 0.0D, 0.0D);
                        }

                    }
                }
                for (EntityXPOrb e: xp){
                    if (!player.isSneaking()){
                        if (stack.getTagCompound().getString("mode").equalsIgnoreCase("attracts")) {
                            e.addVelocity((x - e.posX) * pullSpeed, (y - e.posY) * pullSpeed, (z - e.posZ) * pullSpeed); //Attracts
                        }else {
                            e.addVelocity((e.posX - x) * pullSpeed, (e.posY - y) * pullSpeed, (e.posZ - z) * pullSpeed); //Repels
                        }
                    }
                }

            }

        }
    }
