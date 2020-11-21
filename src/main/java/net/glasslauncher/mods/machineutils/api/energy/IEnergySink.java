// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.glasslauncher.mods.machineutils.api.energy;


// Referenced classes of package ic2.api:
//            IEnergyAcceptor, Direction


import net.glasslauncher.mods.machineutils.api.Direction;

public interface IEnergySink
    extends IEnergyAcceptor
{

    boolean demandsEnergy();

    int injectEnergy(Direction direction, int i);
}
