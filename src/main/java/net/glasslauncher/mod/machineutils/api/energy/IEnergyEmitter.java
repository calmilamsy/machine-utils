package net.glasslauncher.mod.machineutils.api.energy;

import net.glasslauncher.mod.machineutils.api.Direction;
import net.minecraft.tileentity.TileEntityBase;



public interface IEnergyEmitter
        extends IEnergyTile {

    boolean emitsEnergyTo(TileEntityBase tileentity, Direction direction);
}
