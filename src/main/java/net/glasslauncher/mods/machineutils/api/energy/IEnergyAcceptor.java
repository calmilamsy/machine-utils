// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.glasslauncher.mods.machineutils.api.energy;

import net.glasslauncher.mods.machineutils.api.Direction;
import net.minecraft.tileentity.TileEntityBase;

// Referenced classes of package ic2.api:
//            IEnergyTile, Direction

public interface IEnergyAcceptor
        extends IEnergyTile {

    boolean acceptsEnergyFrom(TileEntityBase tileentity, Direction direction);
}
