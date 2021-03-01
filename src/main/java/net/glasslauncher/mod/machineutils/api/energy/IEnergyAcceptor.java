package net.glasslauncher.mod.machineutils.api.energy;

import net.glasslauncher.mod.machineutils.api.Direction;
import net.minecraft.tileentity.TileEntityBase;



public interface IEnergyAcceptor
        extends IEnergyTile {

    boolean acceptsEnergyFrom(TileEntityBase tileentity, Direction direction);
}
