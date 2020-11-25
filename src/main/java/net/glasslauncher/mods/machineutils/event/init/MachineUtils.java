package net.glasslauncher.mods.machineutils.event.init;

import net.glasslauncher.mods.machineutils.common.NetworkManager;
import net.modificationstation.stationloader.api.common.config.Category;
import net.modificationstation.stationloader.api.common.config.Configuration;
import net.modificationstation.stationloader.api.common.event.mod.PostInit;
import net.modificationstation.stationloader.api.common.event.packet.PacketRegister;
import net.modificationstation.stationloader.api.common.mod.StationMod;

public class MachineUtils implements StationMod, PostInit {
    public static Configuration config;
    public static Category generalConfig;

    public static boolean oresRequired = false;

    @Override
    public void preInit() {
        config = getDefaultConfig();
        generalConfig = config.getCategory("general");
        PacketRegister.EVENT.register(new NetworkManager(), getContainer().getMetadata());

        config.save();
    }

    public static void setOresRequired() {
        oresRequired = true;
    }

    @Override
    public void postInit() {
        if (oresRequired) {
            
        }
    }
}
