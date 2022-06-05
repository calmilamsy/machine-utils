package net.glasslauncher.mod.machineutils.impl.event.init;

import net.glasslauncher.mods.api.gcapi.api.GConfig;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.util.API;
import net.modificationstation.stationapi.api.util.Null;
import org.apache.logging.log4j.Logger;

public class MachineUtilsConfig {

    @GConfig(value = "config", visibleName = "MachineUtils Config")
    public static net.glasslauncher.mod.machineutils.impl.common.MachineUtilsConfig generalConfig;

    @Entrypoint.ModID
    public static final ModID MOD_ID = Null.get();

    @Entrypoint.Logger
    public static final Logger LOGGER = Null.get();

    public static boolean oresRequired = false;

    @API
    public static void setOresRequired() {
        oresRequired = true;
    }
}
