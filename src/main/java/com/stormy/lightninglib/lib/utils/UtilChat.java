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

import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.StringTokenizer;

public class UtilChat {
    public static void addChatMessage(EntityPlayer player, String text) {
        player.sendMessage(new TextComponentTranslation(lang(text)));
    }
    public static void addChatMessage(ICommandSender sender, String text) {
        sender.sendMessage(new TextComponentTranslation(lang(text)));
    }
    public static void addChatMessage(EntityPlayer player, ITextComponent textComponentTranslation) {
        player.sendMessage(textComponentTranslation);
    }
    public static String blockPosToString(BlockPos pos) {
        return pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
    }
    public static String lang(String string) {
        return I18n.format(string);
    }
    public static void addChatMessage(World worldObj, ITextComponent textComponentTranslation) {
        if (worldObj.getMinecraftServer() != null) {
            worldObj.getMinecraftServer().sendMessage(textComponentTranslation);
        }
    }
    public static void addChatMessage(World worldObj, String s) {
        addChatMessage(worldObj, new TextComponentTranslation(s));
    }
    public static String[] splitIntoLine(String input, int maxCharInLine) {
        StringTokenizer tok = new StringTokenizer(input, " ");
        StringBuilder output = new StringBuilder(input.length());
        int lineLen = 0;
        while (tok.hasMoreTokens()) {
            String word = tok.nextToken();
            while (word.length() > maxCharInLine) {
                output.append(word.substring(0, maxCharInLine - lineLen) + "\n");
                word = word.substring(maxCharInLine - lineLen);
                lineLen = 0;
            }
            if (lineLen + word.length() > maxCharInLine) {
                output.append("\n");
                lineLen = 0;
            }
            output.append(word + " ");
            lineLen += word.length() + 1;
        }
        return output.toString().split("\n");
    }
    public static String getDirectionsString(ICommandSender player, BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        int xDist, yDist, zDist;
        xDist = (int) player.getPosition().getX() - x;
        yDist = (int) player.getPosition().getY() - y;
        zDist = (int) player.getPosition().getZ() - z;

        boolean isNorth = (zDist > 0);
        boolean isSouth = (zDist < 0);
        boolean isWest = (xDist > 0);
        boolean isEast = (xDist < 0);
        boolean isUp = (yDist < 0);
        boolean isDown = (zDist > 0);
        String xStr = "";
        String yStr = "";
        String zStr = "";
        if (isWest)
            xStr = Math.abs(xDist) + " west ";
        if (isEast)
            xStr = Math.abs(xDist) + " east ";
        if (isNorth)
            zStr = Math.abs(zDist) + " north ";
        if (isSouth)
            zStr = Math.abs(zDist) + " south ";
        if (isUp)
            yStr = Math.abs(yDist) + " up ";
        if (isDown)
            yStr = Math.abs(yDist) + " down ";
        return xStr + yStr + zStr;
    }

    public static void sendStatusMessage(EntityPlayer player, String string) {
        player.sendStatusMessage(new TextComponentTranslation(string), true);
    }
    public static String formatSecondsToMinutes(int secontsTotal) {
        if (secontsTotal < 0) { return ""; }
        int minutes = secontsTotal / 60;
        int secs = secontsTotal % 60;
        return minutes + ":" + String.format("%02d", secs);
    }
}
