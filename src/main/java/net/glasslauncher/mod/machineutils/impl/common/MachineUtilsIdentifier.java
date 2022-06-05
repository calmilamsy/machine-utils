package net.glasslauncher.mod.machineutils.impl.common;

import net.modificationstation.stationapi.api.registry.Identifier;

public enum MachineUtilsIdentifier {

    NBT_ENERGY(Identifier.of("machineutils:energy"));

    public final Identifier identifier;
    MachineUtilsIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }


    @Override
    public String toString() {
        return identifier.toString();
    }
}
