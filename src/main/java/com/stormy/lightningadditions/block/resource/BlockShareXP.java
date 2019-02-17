/*
 * ********************************************************************************
 * Copyright (c) 2017 StormyMode, MiningMark48. All Rights Reserved!
 * This file is part of Lightning Additions (MC-Mod).
 *
 * This project cannot be copied and/or distributed without the express
 * permission of StormyMode, MiningMark48 (Developers)!
 * ********************************************************************************
 */

package com.stormy.lightningadditions.block.resource;

import com.stormy.lightninglib.lib.block.BlockBase;
import com.stormy.lightninglib.lib.utils.KeyChecker;
import com.stormy.lightninglib.lib.utils.TranslateUtils;
import com.stormy.lightningadditions.tile.resource.TileEntitySharingXP;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("deprecation")
public class BlockShareXP extends BlockBase
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing");

    private static AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.125, 0.0D, 0.125D, 0.875D, 0.875D, 0.875D);

    public BlockShareXP() {
        super(Material.IRON);
        setHardness(12.5F);
        setResistance(2000.0F);
        setSoundType(SoundType.METAL);
        setLightLevel(1.0F);
        setLightOpacity(16);
    }
    @Override
    public void addInformation(ItemStack par1ItemStack, @Nullable World world, List par3List, ITooltipFlag par4) {
        if (KeyChecker.isHoldingShift())
        { par3List.add(TextFormatting.GOLD + TranslateUtils.toLocal("tooltip.block.share_xp.line1"));
          par3List.add(TextFormatting.GREEN + TranslateUtils.toLocal("tooltip.block.share_xp.line1.p2"));
        }
        else
        {
            par3List.add(TranslateUtils.toLocal("tooltip.item.hold") + " " + TextFormatting.AQUA + TextFormatting.ITALIC + TranslateUtils.toLocal("tooltip.item.shift"));
        }
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {return this.getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer)); }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        if (EnumFacing.getDirectionFromEntityLiving(pos, placer) != EnumFacing.UP && EnumFacing.getDirectionFromEntityLiving(pos, placer) != EnumFacing.DOWN) {
            worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer).rotateY()), 2);
        }else{
            worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer)), 2);
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
        return BOUNDING_BOX;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
        super.addCollisionBoxToList(pos, entityBox, collidingBoxes, BOUNDING_BOX);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    { return false; }

    @Override
    public boolean isFullCube(IBlockState state)
    { return false; }

    @Override
    public BlockRenderLayer getBlockLayer()
    { return BlockRenderLayer.CUTOUT; }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    { if(!worldIn.isRemote)
        {
            if(playerIn.isSneaking()) //add all levels to the block
            {
                ((TileEntitySharingXP)worldIn.getTileEntity(pos)).addLevel(playerIn.experienceLevel);
                playerIn.experienceLevel = 0;
            }
            else //remove one level from the block
            {
                TileEntitySharingXP te = ((TileEntitySharingXP)worldIn.getTileEntity(pos));

                if(te.getStoredLevels() == 0)
                    return true;

                te.removeLevel();
                playerIn.addExperienceLevel(1);
            }
        }

        return true;
    }




    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntitySharingXP();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] { FACING });
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = state.getValue(FACING).getIndex();

        return i;
    }

}
