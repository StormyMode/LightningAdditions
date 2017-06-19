package com.stormy.lightningadditions.sounds;

import com.stormy.lightningadditions.reference.ModInformation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@SuppressWarnings("WeakerAccess")
public class ModSoundEvents {
    public static SoundEvent philosopher_stone;

    public static void registerSounds() {
        philosopher_stone = registerSound("philosopher_stone");
    }

    private static SoundEvent registerSound(String soundName) {
        final ResourceLocation soundID = new ResourceLocation(ModInformation.MODID, soundName);
        return GameRegistry.register(new SoundEvent(soundID).setRegistryName(soundID));
    }
}
