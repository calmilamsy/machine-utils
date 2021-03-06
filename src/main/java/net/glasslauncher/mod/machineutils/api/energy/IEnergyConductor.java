package net.glasslauncher.mod.machineutils.api.energy;




public interface IEnergyConductor
        extends IEnergyAcceptor, IEnergyEmitter {

    double getConductionLoss();

    int getInsulationEnergyAbsorption();

    int getInsulationBreakdownEnergy();

    int getConductorBreakdownEnergy();

    void removeInsulation();

    void removeConductor();
}
