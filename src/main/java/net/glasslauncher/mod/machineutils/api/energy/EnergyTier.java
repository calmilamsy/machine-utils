package net.glasslauncher.mod.machineutils.api.energy;

public enum EnergyTier {

    LV(1, 32),
    MV(2, 128),
    HV(3, 512),
    EV(4, 2048),
    IV(5, 8192),
    QV(6, 32768);

    public final int tier;
    public final int transfer;
    EnergyTier(int tier, int transfer) {
        this.tier = tier;
        this.transfer = transfer;

    }

    public boolean betterThan(EnergyTier other) {
        return tier > other.tier;
    }

    public boolean worseThan(EnergyTier other) {
        return tier < other.tier;
    }
}
