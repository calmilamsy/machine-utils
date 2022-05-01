package net.glasslauncher.mod.machineutils.impl.client;

import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.mod.machineutils.impl.common.AudioPosition;
import net.glasslauncher.mod.machineutils.impl.common.PositionSpec;
import net.glasslauncher.mod.machineutils.mixin.client.SoundHelperAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sound.SoundEntry;
import paulscode.sound.SoundSystem;

import java.net.URL;



public final class AudioSource {

    private final SoundSystem soundSystem;
    private String sourceName;
    private boolean valid;
    private final Object obj;
    private final PositionSpec positionSpec;
    private boolean isPlaying;

    public AudioSource(SoundSystem soundsystem, String s, Object obj1, PositionSpec positionspec, String s1, boolean flag, boolean flag1,
                       float f) {
        valid = false;
        isPlaying = false;
        soundSystem = soundsystem;
        sourceName = s;
        obj = obj1;
        positionSpec = positionspec;
        SoundEntry soundEntry = ((SoundHelperAccessor) ((Minecraft) FabricLoader.getInstance().getGameInstance()).soundHelper).getSounds().getRandomSoundForId(s1);
        URL url = soundEntry.soundUrl;
        s1 = soundEntry.soundName;
        //URL url = (AudioSource.class).getClassLoader().getResource((new StringBuilder()).append("/assets/ic2/stationloader/sounds/sound/").append(s1).toString());
        if (url == null) {
            System.out.println((new StringBuilder()).append("Invalid sound file: ").append(s1).toString());
            return;
        }
        AudioPosition audioposition = AudioPosition.getFrom(obj1, positionspec);
        soundsystem.newSource(flag1, s, url, s1, flag, audioposition.x, audioposition.y, audioposition.z, 2, AudioManager.fadingDistance * Math.max(f, 1.0F));
        if (f < 1.0F) {
            setVolume(f);
        }
        valid = true;
    }

    public void remove() {
        if (!valid) {
            return;
        } else {
            stop();
            soundSystem.removeSource(sourceName);
            sourceName = null;
            return;
        }
    }

    public void play() {
        if (!valid) {
            return;
        } else {
            isPlaying = true;
            soundSystem.play(sourceName);
            return;
        }
    }

    public void pause() {
        if (!valid) {
            return;
        }
        if (!isPlaying) {
            return;
        } else {
            soundSystem.pause(sourceName);
            return;
        }
    }

    public void stop() {
        if (!valid) {
            return;
        }
        if (!isPlaying) {
            return;
        } else {
            soundSystem.stop(sourceName);
            return;
        }
    }

    public void flush() {
        if (!valid) {
            return;
        }
        if (!isPlaying) {
            return;
        } else {
            soundSystem.flush(sourceName);
            return;
        }
    }

    public float getVolume() {
        if (!valid) {
            return 0.0F;
        } else {
            return soundSystem.getVolume(sourceName);
        }
    }

    public void setVolume(float f) {
        if (!valid) {
            return;
        } else {
            soundSystem.setVolume(sourceName, AudioManager.getMasterVolume() * Math.min(f, 1.0F));
            return;
        }
    }

    public void setPitch(float f) {
        if (!valid) {
            return;
        } else {
            soundSystem.setPitch(sourceName, f);
            return;
        }
    }

    public void updatePosition() {
        if (!valid) {
            return;
        } else {
            AudioPosition audioposition = AudioPosition.getFrom(obj, positionSpec);
            soundSystem.setPosition(sourceName, audioposition.x, audioposition.y, audioposition.z);
            return;
        }
    }
}
