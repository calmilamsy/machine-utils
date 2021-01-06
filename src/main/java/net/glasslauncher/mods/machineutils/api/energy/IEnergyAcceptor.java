package net.glasslauncher.mods.machineutils.api.energy;

import net.glasslauncher.mods.machineutils.api.Direction;
import net.minecraft.tileentity.TileEntityBase;



public interface IEnergyAcceptor
        extends IEnergyTile {

    boolean acceptsEnergyFrom(TileEntityBase tileentity, Direction direction);
}
