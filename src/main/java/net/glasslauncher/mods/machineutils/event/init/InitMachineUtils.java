package net.glasslauncher.mods.machineutils.event.init;

import net.glasslauncher.mods.machineutils.common.NetworkManager;
import net.modificationstation.stationapi.api.common.config.Category;
import net.modificationstation.stationapi.api.common.config.Configuration;
import net.modificationstation.stationapi.api.common.event.packet.MessageListenerRegister;
import net.modificationstation.stationapi.api.common.mod.StationMod;

public class InitMachineUtils implements StationMod {
    public static Configuration config;
    public static Category generalConfig;

    public static boolean oresRequired = false;

    public static void setOresRequired() {
        oresRequired = true;
    }

    @Override
    public void init() {
        config = getDefaultConfig();
        generalConfig = config.getCategory("general");
        MessageListenerRegister.EVENT.register(new NetworkManager(), getModID());

        config.save();
    }
}
