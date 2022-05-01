package net.glasslauncher.mod.machineutils.impl.common;

import net.glasslauncher.mods.api.gcapi.api.ConfigName;

public class MachineUtilsConfig {

    @ConfigName("Enable Sounds")
    public Boolean soundsEnabled = true;

    @ConfigName("Sound Source Limit")
    public Integer soundSourceLimit = 32;
}
