package net.glasslauncher.mod.machineutils.api.energy;





import net.glasslauncher.mod.machineutils.api.Direction;

public interface IEnergySink
        extends IEnergyAcceptor {

    boolean demandsEnergy();

    int injectEnergy(Direction direction, int i);
}
