package net.glasslauncher.mod.machineutils.impl.event.ingame;

import net.glasslauncher.mod.machineutils.impl.client.AudioManager;
import net.glasslauncher.mod.machineutils.api.energy.EnergyNet;
import net.minecraft.entity.player.PlayerBase;

import java.util.List;
import java.util.Random;

public class TickManager {
    public static Random random = new Random();
    public static int windStrength;
    public static int globalTicker;

    public static void init() {
        windStrength = 10 + random.nextInt(10);
        globalTicker = 0;
    }

    public static void onTickInGame(List<PlayerBase> list) {

        if (globalTicker % 128 == 0) {
            updateWind();
        }
        globalTicker++;
        AudioManager.onTick();
        EnergyNet.onTick();
        NetworkManager.onTick();
    }

    public static void updateWind() {
        int i = 10;
        int j = 10;
        if (windStrength > 20) {
            i -= windStrength - 20;
        }
        if (windStrength < 10) {
            j -= 10 - windStrength;
        }
        if (random.nextInt(100) <= i) {
            windStrength++;
            return;
        }
        if (random.nextInt(100) <= j) {
            windStrength--;
            return;
        } else {
            return;
        }
    }
}
