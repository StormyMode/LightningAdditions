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


import com.stormy.lightninglib.lib.utils.KeyChecker;
import com.stormy.lightninglib.lib.utils.TranslateUtils;
import com.stormy.lightninglib.lib.utils.UtilChat;
import de.kitsunealex.frame.item.ItemFoodBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemEmeraldApple extends ItemFood {
    private static final int CONVTIME = 200;

    public ItemEmeraldApple() {
        super(4, 1.0F, false);
        setMaxStackSize(1);
    }



    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack) {
        return EnumRarity.RARE;
    }

    private void startConverting(EntityZombieVillager v, int t) {
        ObfuscationReflectionHelper.setPrivateValue(EntityZombieVillager.class, v, t, "conversionTime", "field_82234_d");
        v.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, t, Math.min(v.world.getDifficulty().getDifficultyId() - 1, 0)));
        v.world.setEntityState(v, (byte) 16);
        try {
            DataParameter<Boolean> CONVERTING = ObfuscationReflectionHelper.getPrivateValue(EntityZombieVillager.class, v, "CONVERTING", "field_184739_bx");
            v.getDataManager().set(CONVERTING, Boolean.valueOf(true));
        }
        catch (Exception e) {}
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
        if (entity instanceof EntityZombieVillager) {
            EntityZombieVillager zombie = ((EntityZombieVillager) entity);
            startConverting(zombie, CONVTIME);
            itemstack.shrink(1);

            if (!player.getEntityWorld().isRemote) {
                UtilChat.addChatMessage(player, UtilChat.lang("item.emerald_apple.merchant"));
            }

            return true;
        }
        return super.itemInteractionForEntity(itemstack, player, entity, hand);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, @Nullable World world, List par3List, ITooltipFlag par4) {
        if (KeyChecker.isHoldingShift())
        { par3List.add(TextFormatting.GREEN + TranslateUtils.toLocal("tooltip.item.emerald_apple.line1"));
        }
        else{ par3List.add(TranslateUtils.toLocal("tooltip.item.hold") + " " + TextFormatting.AQUA + TextFormatting.ITALIC + TranslateUtils.toLocal("tooltip.item.shift")); }
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
        player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 1000, 0, true, false));
        player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 1000, 0, true, false));
    }



}
