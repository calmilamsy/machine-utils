package net.glasslauncher.mod.machineutils.event.init;

import net.modificationstation.stationapi.api.common.config.Category;
import net.modificationstation.stationapi.api.common.config.Configuration;
import net.modificationstation.stationapi.api.common.event.EventListener;
import net.modificationstation.stationapi.api.common.event.mod.Init;
import net.modificationstation.stationapi.api.common.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.common.registry.ModID;
import net.modificationstation.stationapi.api.common.util.API;
import net.modificationstation.stationapi.api.common.util.Null;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

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
    public void init(Init event) {
        generalConfig = CONFIG.getCategory("general");
        CONFIG.save();
    }
}
