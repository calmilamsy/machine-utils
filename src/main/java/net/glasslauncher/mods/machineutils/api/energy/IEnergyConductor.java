// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.glasslauncher.mods.machineutils.api.energy;


// Referenced classes of package ic2.api:
//            IEnergyAcceptor, IEnergyEmitter

public interface IEnergyConductor
        extends IEnergyAcceptor, IEnergyEmitter {

    double getConductionLoss();

    int getInsulationEnergyAbsorption();

    int getInsulationBreakdownEnergy();

    int getConductorBreakdownEnergy();

    void removeInsulation();

    void removeConductor();
}
