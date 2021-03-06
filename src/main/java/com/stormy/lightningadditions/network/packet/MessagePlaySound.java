/*
 *
 *  * ********************************************************************************
 *  * Copyright (c) 2017 StormyMode, MiningMark48. All Rights Reserved!
 *  * This file is part of Lightning Additions (MC-Mod).
 *  *
 *  * This project cannot be copied and/or distributed without the express
 *  * permission of StormyMode, MiningMark48 (Developers)!
 *  * ********************************************************************************
 *
 */

package com.stormy.lightningadditions.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessagePlaySound implements IMessage, IMessageHandler<MessagePlaySound, IMessage>
{
    public BlockPos soundPos;
    public String soundName;
    public float volume;
    public float pitch;

    public MessagePlaySound() {
    }

    public MessagePlaySound(String name, BlockPos pos, float p, float v) {
        this.soundPos = pos;
        this.pitch = p;
        this.volume = v;
        this.soundName = name;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        soundPos = PacketHandler.readBlockPos(buf);
        soundName = ByteBufUtils.readUTF8String(buf);
        pitch = buf.readFloat();
        volume = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketHandler.writeBlockPos(buf, soundPos);
        ByteBufUtils.writeUTF8String(buf, soundName);
        buf.writeFloat(pitch);
        buf.writeFloat(volume);
    }

    @Override
    public IMessage onMessage(MessagePlaySound message, MessageContext ctx) {
        Minecraft mc = Minecraft.getMinecraft();

        if (message.soundName == null)
            return null;

        SoundEvent e = new SoundEvent(new ResourceLocation(message.soundName));
        mc.world.playSound(message.soundPos, e, SoundCategory.BLOCKS, message.volume, message.pitch, false);
        return null;
    }
}
