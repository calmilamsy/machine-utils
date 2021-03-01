package net.glasslauncher.mod.machineutils.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.mod.machineutils.common.AudioPosition;
import net.glasslauncher.mod.machineutils.common.PositionSpec;
import net.glasslauncher.mod.machineutils.event.init.MachineUtilsConfig;
import net.minecraft.client.Minecraft;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;

import java.lang.reflect.Field;
import java.nio.IntBuffer;
import java.util.*;

import static net.glasslauncher.mod.machineutils.event.init.MachineUtilsConfig.CONFIG;
import static net.glasslauncher.mod.machineutils.event.init.MachineUtilsConfig.generalConfig;



public final class AudioManager {

    public static float defaultVolume = 1.2F;
    public static float fadingDistance = 16F;
    private static boolean enabled = true;
    private static int maxSourceCount = 32;
    private static final int streamingSourceCount = 4;
    private static SoundSystem soundSystem = null;
    private static float masterVolume = 0.5F;
    private static int ticker = 0;
    private static int nextId = 0;
    private static final Map objectToAudioSourceMap = new HashMap();

    public AudioManager() {
    }

    public static void initialize() {
        MachineUtilsConfig.LOGGER.info("Loading sound manager...");
        enabled = generalConfig.getProperty("soundsEnabled", FabricLoader.getInstance().getEnvironmentType() != EnvType.SERVER && enabled).getBooleanValue();
        maxSourceCount = generalConfig.getProperty("soundSourceLimit", maxSourceCount).getIntValue();
        CONFIG.save();
        if (maxSourceCount <= 6) {
            MachineUtilsConfig.LOGGER.warn("Max sound source count lower than 6! Disabling sounds.");
            enabled = false;
        }
        if (!enabled) {
            MachineUtilsConfig.LOGGER.info("Sound manager is disabled in config. Disabling sounds.");
            return;
        }
        int i = 0;
        int j = maxSourceCount + 1;
        try {
            AL.create();
            while (i < j - 1) {
                int k = (i + j) / 2;
                if (testSourceCount(k)) {
                    i = k;
                } else {
                    j = k;
                }
            }
            AL.destroy();
        } catch (Exception ignored) {
            MachineUtilsConfig.LOGGER.info("An exception occurred while setting up the sound system. Disabling sounds.");
        }
        maxSourceCount = i;
        if (maxSourceCount < 6) {
            enabled = false;
        } else {
            MachineUtilsConfig.LOGGER.info("Using " + maxSourceCount + " audio sources.");
            SoundSystemConfig.setNumberStreamingChannels(streamingSourceCount);
            SoundSystemConfig.setNumberNormalChannels(maxSourceCount - streamingSourceCount);
        }
    }

    public static void onTick() {
        if (!enabled || soundSystem == null) {
            return;
        }
        if (ticker++ % 8 == 0) {
            float f = ((Minecraft) FabricLoader.getInstance().getGameInstance()).options.sound;
            if (f != masterVolume) {
                float f1 = f / masterVolume;
                masterVolume = f;
                for (Object o : objectToAudioSourceMap.values()) {
                    List list = (List) o;
                    Iterator iterator1 = list.iterator();
                    while (iterator1.hasNext()) {
                        AudioSource audiosource = (AudioSource) iterator1.next();
                        audiosource.setVolume(audiosource.getVolume() * f1);
                    }
                }

            }
        }
    }

    public static AudioSource createSource(Object obj, String s) {
        return createSource(obj, PositionSpec.Center, s, false, false, defaultVolume);
    }

    public static AudioSource createSource(Object obj, PositionSpec positionspec, String s, boolean flag, boolean flag1, float f) {
        if (!enabled) {
            return null;
        }
        if (soundSystem == null) {
            getSoundSystem();
        }
        if (soundSystem == null) {
            return null;
        }
        String s1 = getSourceName(nextId);
        nextId++;
        AudioSource audiosource = new AudioSource(soundSystem, s1, obj, positionspec, s, flag, flag1, f);
        if (!objectToAudioSourceMap.containsKey(obj)) {
            objectToAudioSourceMap.put(obj, new LinkedList());
        }
        ((List) objectToAudioSourceMap.get(obj)).add(audiosource);
        return audiosource;
    }

    public static void removeSources(Object obj) {
        if (soundSystem == null) {
            return;
        }
        if (!objectToAudioSourceMap.containsKey(obj)) {
            return;
        }
        AudioSource audiosource;
        for (Iterator iterator = ((List) objectToAudioSourceMap.get(obj)).iterator(); iterator.hasNext(); audiosource.remove()) {
            audiosource = (AudioSource) iterator.next();
        }

        objectToAudioSourceMap.remove(obj);
    }

    public static void playOnce(Object obj, String s) {
        playOnce(obj, PositionSpec.Center, s, false, defaultVolume);
    }

    public static void playOnce(Object obj, PositionSpec positionspec, String s, boolean flag, float f) {
        if (!enabled) {
            return;
        }
        AudioPosition audioposition = AudioPosition.getFrom(obj, positionspec);
        ((Minecraft) FabricLoader.getInstance().getGameInstance()).level.playSound(audioposition.x, audioposition.y, audioposition.z, s, 1.0F, 1.0F);
    }

    public static float getMasterVolume() {
        return masterVolume;
    }

    private static boolean testSourceCount(int i) {
        IntBuffer intbuffer = BufferUtils.createIntBuffer(i);
        try {
            AL10.alGenSources(intbuffer);
            if (AL10.alGetError() == 0) {
                AL10.alDeleteSources(intbuffer);
                return true;
            }
        } catch (Exception exception) {
            AL10.alGetError();
        } catch (UnsatisfiedLinkError unsatisfiedlinkerror) {
        }
        return false;
    }

    private static void getSoundSystem() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            Field[] afield = (net.minecraft.client.sound.SoundHelper.class).getDeclaredFields();
            int i = afield.length;
            int j = 0;
            do {
                if (j >= i) {
                    break;
                }
                Field field = afield[j];
                if (field.getType() == (SoundSystem.class)) {
                    field.setAccessible(true);
                    try {
                        Object obj = field.get(null);
                        if (obj instanceof SoundSystem) {
                            soundSystem = (SoundSystem) obj;
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        soundSystem = null;
                    }
                    break;
                }
                j++;
            } while (true);
        }
    }

    private static String getSourceName(int i) {
        return "asm_snd" + i;
    }

}
