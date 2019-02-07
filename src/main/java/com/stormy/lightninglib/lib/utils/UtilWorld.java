/*
 * ********************************************************************************
 * Copyright (c) 2017 StormyMode, MiningMark48. All Rights Reserved!
 * This file is part of Lightning Additions (MC-Mod).
 *
 * This project cannot be copied and/or distributed without the express
 * permission of StormyMode, MiningMark48 (Developers)!
 * ********************************************************************************
 */

package com.stormy.lightninglib.lib.utils;

import com.stormy.lightningadditions.tile.resource.TileEntityCompressedBase;
import com.stormy.lightninglib.lib.utils.UtilChat;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.command.ICommandSender;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.*;

public class UtilWorld {
    public static int blockToChunk(int blockVal) {
        return blockVal >> 4; // ">>4" == "/16"
    }
    public static int chunkToBlock(int chunkVal) {
        return chunkVal << 4; // "<<4" == "*16"
    }
    public static boolean isNight(World world) {
        long t = world.getWorldTime();
        int timeOfDay = (int) t % 24000;
        return timeOfDay > 12000;
    }

    public static LinkedList<BlockPos> getBlocksWithinArea(BlockPos centralPos, int horizontal, int vertical) {
        return getBlocksWithinArea(centralPos, horizontal, vertical, vertical);
    }

    public static LinkedList<BlockPos> getBlocksWithinArea(BlockPos centralPos, int horizontal, int up, int down) {
        BlockPos from = centralPos.add(-horizontal, -down, -horizontal);
        BlockPos to = centralPos.add(horizontal, up, horizontal);
        LinkedList<BlockPos> blockList = new LinkedList<>();
        BlockPos.getAllInBox(from, to).forEach(blockList::add);
        return blockList;
    }

    public static boolean doesAreaContainUnbreakable(World world, LinkedList<BlockPos> blockList) {
        for (BlockPos blockPos : blockList) {
            IBlockState block = world.getBlockState(blockPos);
            if (block.getBlockHardness(world, blockPos) < 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean doesAreaContainTileEntity(World world, LinkedList<BlockPos> blockList) {
        for (BlockPos blockPos : blockList) {
            if (world.getTileEntity(blockPos) != null && !(world.getTileEntity(blockPos) instanceof TileEntityCompressedBase)) {
                return true;
            }
        }
        return false;
    }

    //For sorting blocks based on distance to player, mainly used for rendering
    //Borrowed code from DW20 :P
    public static ArrayList<BlockPos> sortByDistance(ArrayList<BlockPos> unSortedList, EntityPlayer player) {
        ArrayList<BlockPos> sortedList = new ArrayList<>();
        Map<Double, BlockPos> rangeMap = new HashMap<>();
        Double distances[] = new Double[unSortedList.size()];
        Double distance;
        double x = player.posX;
        double y = player.posY + player.getEyeHeight();
        double z = player.posZ;
        int i = 0;
        for (BlockPos pos : unSortedList) {
            distance = pos.distanceSqToCenter(x, y, z);
            rangeMap.put(distance, pos);
            distances[i] = distance;
            i++;
        }
        Arrays.sort(distances);

        for (Double dist : distances) {
            sortedList.add(rangeMap.get(dist));
        }
        return sortedList;
    }

    public static void dropItemsRandom(World world, ItemStack items, float x, float y, float z)
    {
        float f = 0.7F;
        double x1 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
        double y1 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
        double z1 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
        dropItems(world, items, x + x1, y + y1, z + z1);
    }

    public static void dropItems(World world, ItemStack items, double x, double y, double z)
    {
        EntityItem entityitem = new EntityItem(world, x, y, z, items);
        entityitem.setDefaultPickupDelay();
        world.spawnEntity(entityitem);
    }

    public static void addClientEvent(TileEntity te, int type, int arg)
    {
        te.getWorld().addBlockEvent(te.getPos(), te.getBlockType(), type, arg);
    }

    public static boolean isBlockAir(IBlockAccess world, BlockPos pos)
    {
        return (isBlockAir(world.getBlockState(pos))) || (world.isAirBlock(pos));
    }

    public static boolean isBlockAir(IBlockState block)
    {
        return (block == null) || (block.getBlock() == Blocks.AIR) || (block.getMaterial() == Material.AIR);
    }

    public static void dropItemsRandom(World world, ItemStack item, BlockPos pos)
    {
        dropItemsRandom(world, item, pos.getX(), pos.getY(), pos.getZ());
    }

    public static BlockPos convertIposToBlockpos(IPosition here) {
        return new BlockPos(here.getX(), here.getY(), here.getZ());
    }
    public static BlockPos getFirstBlockAbove(World world, BlockPos pos) {
        BlockPos posCurrent = null;
        for (int y = pos.getY() + 1; y < world.getHeight() ; y++) {
            posCurrent = new BlockPos(pos.getX(), y, pos.getZ());
            if (world.getBlockState(posCurrent).getBlock() == Blocks.AIR &&
                    world.getBlockState(posCurrent.up()).getBlock() == Blocks.AIR &&
                    world.getBlockState(posCurrent.down()).getBlock() != Blocks.AIR) { return posCurrent; }
        }
        return null;
    }
    public static List<BlockPos> getPositionsInRange(BlockPos center, int xMin, int xMax, int yMin, int yMax, int zMin, int zMax) {
        List<BlockPos> found = new ArrayList<BlockPos>();
        for (int x = xMin; x <= xMax; x++)
            for (int y = yMin; y <= yMax; y++)
                for (int z = zMin; z <= zMax; z++) {
                    found.add(new BlockPos(x, y, z));
                }
        return found;
    }
    public static BlockPos getRandomPos(Random rand, BlockPos here, int hRadius) {
        int x = here.getX();
        int z = here.getZ();
        // search in a square
        int xMin = x - hRadius;
        int xMax = x + hRadius;
        int zMin = z - hRadius;
        int zMax = z + hRadius;
        int posX = MathHelper.getInt(rand, xMin, xMax);
        int posZ = MathHelper.getInt(rand, zMin, zMax);
        return new BlockPos(posX, here.getY(), posZ);
    }
    public static boolean tryTpPlayerToBed(World world, EntityPlayer player) {
        if (world.isRemote) { return false; }
        if (player.dimension != 0) {
            UtilChat.addChatMessage(player, "command.home.overworld");
            return false;
        }
        BlockPos pos = player.getBedLocation(0);
        if (pos == null) {
            // has not been sent in a bed
            UtilChat.addChatMessage(player, "command.gethome.bed");
            return false;
        }
        return true;
    }
    public static Map<IInventory, BlockPos> findTileEntityInventories(ICommandSender player, int RADIUS) {
        // function imported
        // https://github.com/PrinceOfAmber/SamsPowerups/blob/master/Commands/src/main/java/com/lothrazar/samscommands/ModCommands.java#L193
        Map<IInventory, BlockPos> found = new HashMap<IInventory, BlockPos>();
        int xMin = (int) player.getPosition().getX() - RADIUS;
        int xMax = (int) player.getPosition().getX() + RADIUS;
        int yMin = (int) player.getPosition().getY() - RADIUS;
        int yMax = (int) player.getPosition().getY() + RADIUS;
        int zMin = (int) player.getPosition().getZ() - RADIUS;
        int zMax = (int) player.getPosition().getZ() + RADIUS;
        BlockPos posCurrent = null;
        for (int xLoop = xMin; xLoop <= xMax; xLoop++) {
            for (int yLoop = yMin; yLoop <= yMax; yLoop++) {
                for (int zLoop = zMin; zLoop <= zMax; zLoop++) {
                    posCurrent = new BlockPos(xLoop, yLoop, zLoop);
                    if (player.getEntityWorld().getTileEntity(posCurrent) instanceof IInventory) {
                        found.put((IInventory) player.getEntityWorld().getTileEntity(posCurrent), posCurrent);
                    }
                }
            }
        }
        return found;
    }
    public static int searchTileInventory(String search, IInventory inventory) {
        int foundQty;
        foundQty = 0;
        for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
            ItemStack invItem = inventory.getStackInSlot(slot);
            if (invItem == null) {
                continue;
            } // empty slot in chest (or generator)
            String invItemName = invItem.getDisplayName().toLowerCase();
            // find any overlap: so if x ==y , or if x substring of y, or y substring
            // of x
            if (search.equals(invItemName) || search.contains(invItemName) || invItemName.contains(search)) {
                foundQty += invItem.getCount();
            }
        } // end loop on current tile entity
        return foundQty;
    }
    public static BlockPos findClosestBlock(EntityPlayer player, Block blockHunt, int RADIUS) {
        BlockPos found = null;
        int xMin = (int) player.posX - RADIUS;
        int xMax = (int) player.posX + RADIUS;
        int yMin = (int) player.posY - RADIUS;
        int yMax = (int) player.posY + RADIUS;
        int zMin = (int) player.posZ - RADIUS;
        int zMax = (int) player.posZ + RADIUS;
        int distance = 0, distanceClosest = RADIUS * RADIUS;
        BlockPos posCurrent = null;
        for (int xLoop = xMin; xLoop <= xMax; xLoop++) {
            for (int yLoop = yMin; yLoop <= yMax; yLoop++) {
                for (int zLoop = zMin; zLoop <= zMax; zLoop++) {
                    posCurrent = new BlockPos(xLoop, yLoop, zLoop);
                    if (player.getEntityWorld().getBlockState(posCurrent).getBlock().equals(blockHunt)) {
                        // find closest?
                        if (found == null) {
                            found = posCurrent;
                        }
                        else {
                            distance = (int) distanceBetweenHorizontal(player.getPosition(), posCurrent);
                            if (distance < distanceClosest) {
                                found = posCurrent;
                                distanceClosest = distance;
                            }
                        }
                    }
                }
            }
        }
        return found;
    }
    public static ArrayList<BlockPos> findBlocks(World world, BlockPos start, Block blockHunt, int RADIUS) {
        ArrayList<BlockPos> found = new ArrayList<BlockPos>();
        int xMin = (int) start.getX() - RADIUS;
        int xMax = (int) start.getX() + RADIUS;
        int yMin = (int) start.getY() - RADIUS;
        int yMax = (int) start.getY() + RADIUS;
        int zMin = (int) start.getZ() - RADIUS;
        int zMax = (int) start.getZ() + RADIUS;
        BlockPos posCurrent = null;
        for (int xLoop = xMin; xLoop <= xMax; xLoop++) {
            for (int yLoop = yMin; yLoop <= yMax; yLoop++) {
                for (int zLoop = zMin; zLoop <= zMax; zLoop++) {
                    posCurrent = new BlockPos(xLoop, yLoop, zLoop);
                    if (world.getBlockState(posCurrent).getBlock().equals(blockHunt)) {
                        found.add(posCurrent);
                    }
                }
            }
        }
        return found;
    }
    public static double distanceBetweenHorizontal(BlockPos start, BlockPos end) {
        int xDistance = Math.abs(start.getX() - end.getX());
        int zDistance = Math.abs(start.getZ() - end.getZ());
        // ye olde pythagoras
        return Math.sqrt(xDistance * xDistance + zDistance * zDistance);
    }
    public static double distanceBetweenVertical(BlockPos start, BlockPos end) {
        return Math.abs(start.getY() - end.getY());
    }
    public static boolean doBlockStatesMatch(IBlockState replaced, IBlockState newToPlace) {
        return replaced.getBlock() == newToPlace.getBlock() &&
                replaced.getBlock().getMetaFromState(replaced) == newToPlace.getBlock().getMetaFromState(newToPlace);
    }
    public static boolean isAirOrWater(World world, BlockPos pos) {
        ArrayList<Block> waterBoth = new ArrayList<Block>();
        waterBoth.add(Blocks.FLOWING_WATER);
        waterBoth.add(Blocks.WATER);
        if (pos == null) { return false; }
        return world.isAirBlock(pos) || world.getBlockState(pos).getBlock().getUnlocalizedName().equalsIgnoreCase("tile.water") || (world.getBlockState(pos) != null
                && waterBoth.contains(world.getBlockState(pos).getBlock()));
    }
    public static BlockPos nextAirInDirection(World world, BlockPos posIn, EnumFacing facing, int max, @Nullable Block blockMatch) {
        BlockPos posToPlaceAt = new BlockPos(posIn);
        BlockPos posLoop = new BlockPos(posIn);
        for (int i = 0; i < max; i++) {
            if (world.isAirBlock(posLoop)) {
                posToPlaceAt = posLoop;
                break;
            }
            else {
                posLoop = posLoop.offset(facing);
            }
        }
        return posToPlaceAt;
    }
    /**
     * Everything in this inner class is From a mod that has MIT license owned by
     * romelo333 and maintained by McJty
     *
     * License is here:
     * https://github.com/romelo333/notenoughwands1.8.8/blob/master/LICENSE
     *
     * Specific source of code from the GenericWand class:
     * https://github.com/romelo333/notenoughwands1.8.8/blob/20952f50e7c1ab3fd676ed3da302666295e3cac8/src/main/java/romelo333/notenoughwands/Items/GenericWand.java
     *
     */
    public static class OutlineRenderer {
        public static void renderOutlines(RenderWorldLastEvent evt, EntityPlayerSP p, Set<BlockPos> coordinates, int r, int g, int b) {
            double doubleX = p.lastTickPosX + (p.posX - p.lastTickPosX) * evt.getPartialTicks();
            double doubleY = p.lastTickPosY + (p.posY - p.lastTickPosY) * evt.getPartialTicks();
            double doubleZ = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * evt.getPartialTicks();
            GlStateManager.pushAttrib();
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.pushMatrix();
            GlStateManager.translate(-doubleX, -doubleY, -doubleZ);
            renderOutlines(coordinates, r, g, b, 4);
            GlStateManager.popMatrix();
            GlStateManager.popAttrib();
        }
        private static void renderOutlines(Set<BlockPos> coordinates, int r, int g, int b, int thickness) {
            Tessellator tessellator = Tessellator.getInstance();
            net.minecraft.client.renderer.BufferBuilder buffer = tessellator.getBuffer();
            buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
            //    GlStateManager.color(r / 255.0f, g / 255.0f, b / 255.0f);
            GL11.glLineWidth(thickness);
            for (BlockPos coordinate : coordinates) {
                float x = coordinate.getX();
                float y = coordinate.getY();
                float z = coordinate.getZ();
                renderHighLightedBlocksOutline(buffer, x, y, z, r / 255.0f, g / 255.0f, b / 255.0f, 1.0f); // .02f
            }
            tessellator.draw();
        }
        public static void renderHighLightedBlocksOutline(BufferBuilder buffer, float mx, float my, float mz, float r, float g, float b, float a) {
            buffer.pos(mx, my, mz).color(r, g, b, a).endVertex();
            buffer.pos(mx + 1, my, mz).color(r, g, b, a).endVertex();
            buffer.pos(mx, my, mz).color(r, g, b, a).endVertex();
            buffer.pos(mx, my + 1, mz).color(r, g, b, a).endVertex();
            buffer.pos(mx, my, mz).color(r, g, b, a).endVertex();
            buffer.pos(mx, my, mz + 1).color(r, g, b, a).endVertex();
            buffer.pos(mx + 1, my + 1, mz + 1).color(r, g, b, a).endVertex();
            buffer.pos(mx, my + 1, mz + 1).color(r, g, b, a).endVertex();
            buffer.pos(mx + 1, my + 1, mz + 1).color(r, g, b, a).endVertex();
            buffer.pos(mx + 1, my, mz + 1).color(r, g, b, a).endVertex();
            buffer.pos(mx + 1, my + 1, mz + 1).color(r, g, b, a).endVertex();
            buffer.pos(mx + 1, my + 1, mz).color(r, g, b, a).endVertex();
            buffer.pos(mx, my + 1, mz).color(r, g, b, a).endVertex();
            buffer.pos(mx, my + 1, mz + 1).color(r, g, b, a).endVertex();
            buffer.pos(mx, my + 1, mz).color(r, g, b, a).endVertex();
            buffer.pos(mx + 1, my + 1, mz).color(r, g, b, a).endVertex();
            buffer.pos(mx + 1, my, mz).color(r, g, b, a).endVertex();
            buffer.pos(mx + 1, my, mz + 1).color(r, g, b, a).endVertex();
            buffer.pos(mx + 1, my, mz).color(r, g, b, a).endVertex();
            buffer.pos(mx + 1, my + 1, mz).color(r, g, b, a).endVertex();
            buffer.pos(mx, my, mz + 1).color(r, g, b, a).endVertex();
            buffer.pos(mx + 1, my, mz + 1).color(r, g, b, a).endVertex();
            buffer.pos(mx, my, mz + 1).color(r, g, b, a).endVertex();
            buffer.pos(mx, my + 1, mz + 1).color(r, g, b, a).endVertex();
        }
    }
    /**
     * Functions from this inner class are not authored by me (Sam Bassett aka
     * Lothrazar) they are from BuildersGuides by
     *
     * @author Ipsis
     *
     *         All credit goes to author for this
     *
     *         Source code: https://github.com/Ipsis/BuildersGuides Source License
     *         https://github.com/Ipsis/BuildersGuides/blob/master/COPYING.LESSER
     *
     *         I used and modified two functions from this library
     *         https://github.com/Ipsis/BuildersGuides/blob/master/src/main/java/ipsis/buildersguides/util/RenderUtils.java
     *
     *
     */
    public static class RenderShadow {
        public static void renderBlockList(List<BlockPos> blockPosList, BlockPos center, double relX, double relY, double relZ, float red, float green, float blue) {
            GlStateManager.pushAttrib();
            GlStateManager.pushMatrix();
            // translate to center or te
            GlStateManager.translate(relX + 0.5F, relY + 0.5F, relZ + 0.5F);
            GlStateManager.disableLighting(); // so colors are correct
            GlStateManager.disableTexture2D(); // no texturing needed
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.enableBlend();
            float alpha = 0.5F;
            GlStateManager.color(red, green, blue, alpha);
            if (Minecraft.isAmbientOcclusionEnabled())
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
            else
                GlStateManager.shadeModel(GL11.GL_FLAT);
            for (BlockPos p : blockPosList) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(
                        (center.getX() - p.getX()) * -1.0F,
                        (center.getY() - p.getY()) * -1.0F,
                        (center.getZ() - p.getZ()) * -1.0F);
                shadedCube(0.4F);
                GlStateManager.popMatrix();
            }
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
            GlStateManager.popAttrib();
        }
        private static void shadedCube(float scale) {
            float size = 1.0F * scale;
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder worldRenderer = tessellator.getBuffer();
            // Front - anticlockwise vertices
            // Back - clockwise vertices
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
            // xy anti-clockwise - front
            worldRenderer.pos(-size, -size, size).endVertex();
            worldRenderer.pos(size, -size, size).endVertex();
            worldRenderer.pos(size, size, size).endVertex();
            worldRenderer.pos(-size, size, size).endVertex();
            // xy clockwise - back
            worldRenderer.pos(-size, -size, -size).endVertex();
            worldRenderer.pos(-size, size, -size).endVertex();
            worldRenderer.pos(size, size, -size).endVertex();
            worldRenderer.pos(size, -size, -size).endVertex();
            // anti-clockwise - left
            worldRenderer.pos(-size, -size, -size).endVertex();
            worldRenderer.pos(-size, -size, size).endVertex();
            worldRenderer.pos(-size, size, size).endVertex();
            worldRenderer.pos(-size, size, -size).endVertex();
            // clockwise - right
            worldRenderer.pos(size, -size, -size).endVertex();
            worldRenderer.pos(size, size, -size).endVertex();
            worldRenderer.pos(size, size, size).endVertex();
            worldRenderer.pos(size, -size, size).endVertex();
            // anticlockwise - top
            worldRenderer.pos(-size, size, -size).endVertex();
            worldRenderer.pos(-size, size, size).endVertex();
            worldRenderer.pos(size, size, size).endVertex();
            worldRenderer.pos(size, size, -size).endVertex();
            // clockwise - bottom
            worldRenderer.pos(-size, -size, -size).endVertex();
            worldRenderer.pos(size, -size, -size).endVertex();
            worldRenderer.pos(size, -size, size).endVertex();
            worldRenderer.pos(-size, -size, size).endVertex();
            tessellator.draw();
        }
    }

    public static boolean isDateAroundHalloween(Calendar cal)
    {
        return cal.get(2) + 1 == 10 && cal.get(5) >= 20 || cal.get(2) + 1 == 11 && cal.get(5) <= 3;
    }

}