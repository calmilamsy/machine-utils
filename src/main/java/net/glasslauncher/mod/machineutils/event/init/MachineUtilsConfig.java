package net.glasslauncher.mod.machineutils.event.init;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.config.Category;
import net.modificationstation.stationapi.api.config.Configuration;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.util.API;
import net.modificationstation.stationapi.api.util.Null;
import org.apache.logging.log4j.Logger;

public class MachineUtilsConfig {
    @Entrypoint.Config
    public static final Configuration CONFIG = Null.get();
    public static Category generalConfig;

    @Entrypoint.ModID
    public static final ModID MOD_ID = Null.get();

    @Entrypoint.Logger
    public static final Logger LOGGER = Null.get();

    public static boolean oresRequired = false;

    @API
    public static void setOresRequired() {
        oresRequired = true;
    }

    @EventListener
    public void init(InitEvent event) {
        generalConfig = CONFIG.getCategory("general");
        CONFIG.save();
    }
}
