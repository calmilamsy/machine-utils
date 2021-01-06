package net.glasslauncher.mods.machineutils.api.energy;





import net.glasslauncher.mods.machineutils.api.Direction;

public interface IEnergySink
        extends IEnergyAcceptor {

    boolean demandsEnergy();

    int injectEnergy(Direction direction, int i);
}
