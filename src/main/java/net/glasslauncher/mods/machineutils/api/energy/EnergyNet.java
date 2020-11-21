// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.glasslauncher.mods.machineutils.api.energy;

import net.glasslauncher.mods.machineutils.api.Direction;
import net.minecraft.entity.Living;
import net.minecraft.level.Level;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.maths.Box;

import java.util.*;

public final class EnergyNet
{
    public static class EnergyTarget
    {

        TileEntityBase tileEntity;
        Direction direction;

        EnergyTarget(TileEntityBase tileentity, Direction direction1)
        {
            tileEntity = tileentity;
            direction = direction1;
        }
    }

    public static class EnergyBlockLink
    {

        Direction direction;
        double loss;

        EnergyBlockLink(Direction direction1, double d)
        {
            direction = direction1;
            loss = d;
        }
    }

    public static class EnergyPath
    {

        TileEntityBase target;
        Direction targetDirection;
        Set conductors;
        int minX;
        int minY;
        int minZ;
        int maxX;
        int maxY;
        int maxZ;
        double loss;
        int minInsulationEnergyAbsorption;
        int minInsulationBreakdownEnergy;
        int minConductorBreakdownEnergy;
        long totalEnergyConducted;

        EnergyPath()
        {
            target = null;
            conductors = new HashSet();
            minX = 0x7fffffff;
            minY = 0x7fffffff;
            minZ = 0x7fffffff;
            maxX = 0x80000000;
            maxY = 0x80000000;
            maxZ = 0x80000000;
            loss = 0.0D;
            minInsulationEnergyAbsorption = 0x7fffffff;
            minInsulationBreakdownEnergy = 0x7fffffff;
            minConductorBreakdownEnergy = 0x7fffffff;
            totalEnergyConducted = 0L;
        }
    }


    public static EnergyNet getForWorld(Level world1)
    {
        if(world1 == null)
        {
            return null;
        }
        if(!worldToEnergyNetMap.containsKey(world1))
        {
            worldToEnergyNetMap.put(world1, new EnergyNet(world1));
        }
        return worldToEnergyNetMap.get(world1);
    }

    public static void onTick()
    {
        Iterator iterator = entityLivingToShockEnergyMap.entrySet().iterator();
        do
        {
            if(!iterator.hasNext())
            {
                break;
            }
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            Living entityliving = (Living)entry.getKey();
            int i = (((Integer)entry.getValue()).intValue() + 63) / 64;
            if(entityliving.isAlive())
            {
                entityliving.damage(null, i);
            }
        } while(true);
        entityLivingToShockEnergyMap.clear();
    }

    private EnergyNet(Level world1)
    {
        energySourceToEnergyPathMap = new HashMap();
        world = world1;
    }

    public void addTileEntity(TileEntityBase tileentity)
    {
        if(!(tileentity instanceof IEnergyTile) || ((IEnergyTile)tileentity).isAddedToEnergyNet())
        {
            return;
        }
        if(tileentity instanceof IEnergyAcceptor)
        {
            List list = discover(tileentity, true, 0x7fffffff);
            Iterator iterator = list.iterator();
            do
            {
                if(!iterator.hasNext())
                {
                    break;
                }
                EnergyPath energypath = (EnergyPath)iterator.next();
                IEnergySource ienergysource = (IEnergySource)energypath.target;
                if(energySourceToEnergyPathMap.containsKey(ienergysource) && (double)ienergysource.getMaxEnergyOutput() > energypath.loss)
                {
                    energySourceToEnergyPathMap.remove(ienergysource);
                }
            } while(true);
        }
        if(!(tileentity instanceof IEnergySource));
    }

    public void removeTileEntity(TileEntityBase tileentity)
    {
        if(!(tileentity instanceof IEnergyTile) || !((IEnergyTile)tileentity).isAddedToEnergyNet())
        {
            return;
        }
        if(tileentity instanceof IEnergyAcceptor)
        {
            List list = discover(tileentity, true, 0x7fffffff);
            Iterator iterator = list.iterator();
label0:
            do
            {
                if(!iterator.hasNext())
                {
                    break;
                }
                EnergyPath energypath = (EnergyPath)iterator.next();
                IEnergySource ienergysource = (IEnergySource)energypath.target;
                if(!energySourceToEnergyPathMap.containsKey(ienergysource) || (double)ienergysource.getMaxEnergyOutput() <= energypath.loss)
                {
                    continue;
                }
                if(tileentity instanceof IEnergyConductor)
                {
                    energySourceToEnergyPathMap.remove(ienergysource);
                    continue;
                }
                Iterator iterator1 = ((List)energySourceToEnergyPathMap.get(ienergysource)).iterator();
                do
                {
                    if(!iterator1.hasNext())
                    {
                        continue label0;
                    }
                } while(((EnergyPath)iterator1.next()).target != tileentity);
                iterator1.remove();
            } while(true);
        }
        if(tileentity instanceof IEnergySource)
        {
            energySourceToEnergyPathMap.remove(tileentity);
        }
    }

    public int emitEnergyFrom(IEnergySource ienergysource, int i)
    {
        if(!energySourceToEnergyPathMap.containsKey(ienergysource))
        {
            energySourceToEnergyPathMap.put(ienergysource, discover((TileEntityBase)ienergysource, false, ienergysource.getMaxEnergyOutput()));
        }
        int j = 0;
        Vector vector = new Vector();
        double d = 0.0D;
        Iterator iterator = ((List)energySourceToEnergyPathMap.get(ienergysource)).iterator();
label0:
        do
        {
            EnergyPath energypath;
            IEnergySink ienergysink;
            do
            {
                if(!iterator.hasNext())
                {
                    break label0;
                }
                energypath = (EnergyPath)iterator.next();
                if(!$assertionsDisabled && !(energypath.target instanceof IEnergySink))
                {
                    throw new AssertionError();
                }
                ienergysink = (IEnergySink)energypath.target;
            } while(!ienergysink.demandsEnergy());
            d += 1.0D / energypath.loss;
            vector.add(energypath);
        } while(vector.size() < i);
        for(Iterator iterator1 = vector.iterator(); iterator1.hasNext();)
        {
            EnergyPath energypath1 = (EnergyPath)iterator1.next();
            IEnergySink ienergysink1 = (IEnergySink)energypath1.target;
            int k = (int)Math.floor((double)Math.round(((double)i / d / energypath1.loss) * 100000D) / 100000D);
            int l = (int)Math.floor(energypath1.loss);
            if(k > l)
            {
                int i1 = ienergysink1.injectEnergy(energypath1.targetDirection, k - l);
                j += k - i1;
                int j1 = k - l - i1;
                energypath1.totalEnergyConducted += j1;
                if(j1 > energypath1.minInsulationEnergyAbsorption)
                {
                    List list = world.getEntities(Living.class, Box.create(energypath1.minX - 1, energypath1.minY - 1, energypath1.minZ - 1, energypath1.maxX + 2, energypath1.maxY + 2, energypath1.maxZ + 2));
                    for(Iterator iterator3 = list.iterator(); iterator3.hasNext();)
                    {
                        Living entityliving = (Living)iterator3.next();
                        int k1 = 0;
                        Iterator iterator5 = energypath1.conductors.iterator();
                        IEnergyConductor ienergyconductor2;
label1:
                        do
                        {
                            TileEntityBase tileentity;
                            do
                            {
                                if(!iterator5.hasNext())
                                {
                                    break label1;
                                }
                                ienergyconductor2 = (IEnergyConductor)iterator5.next();
                                tileentity = (TileEntityBase)ienergyconductor2;
                            } while(!entityliving.boundingBox.method_90(Box.create(tileentity.x - 1, tileentity.y - 1, tileentity.z - 1, tileentity.x + 2, tileentity.y + 2, tileentity.z + 2)));
                            int l1 = j1 - ienergyconductor2.getInsulationEnergyAbsorption();
                            if(l1 > k1)
                            {
                                k1 = l1;
                            }
                        } while(ienergyconductor2.getInsulationEnergyAbsorption() != energypath1.minInsulationEnergyAbsorption);
                        if(entityLivingToShockEnergyMap.containsKey(entityliving))
                        {
                            entityLivingToShockEnergyMap.put(entityliving, Integer.valueOf(entityLivingToShockEnergyMap.get(entityliving).intValue() + k1));
                        } else
                        {
                            entityLivingToShockEnergyMap.put(entityliving, Integer.valueOf(k1));
                        }
                    }

                    if(j1 >= energypath1.minInsulationBreakdownEnergy)
                    {
                        Iterator iterator4 = energypath1.conductors.iterator();
                        do
                        {
                            if(!iterator4.hasNext())
                            {
                                break;
                            }
                            IEnergyConductor ienergyconductor1 = (IEnergyConductor)iterator4.next();
                            if(j1 >= ienergyconductor1.getInsulationBreakdownEnergy())
                            {
                                ienergyconductor1.removeInsulation();
                                if(ienergyconductor1.getInsulationEnergyAbsorption() < energypath1.minInsulationEnergyAbsorption)
                                {
                                    energypath1.minInsulationEnergyAbsorption = ienergyconductor1.getInsulationEnergyAbsorption();
                                }
                            }
                        } while(true);
                    }
                }
                if(j1 >= energypath1.minConductorBreakdownEnergy)
                {
                    Iterator iterator2 = energypath1.conductors.iterator();
                    while(iterator2.hasNext()) 
                    {
                        IEnergyConductor ienergyconductor = (IEnergyConductor)iterator2.next();
                        if(j1 >= ienergyconductor.getConductorBreakdownEnergy())
                        {
                            ienergyconductor.removeConductor();
                        }
                    }
                }
            }
        }

        return i - j;
    }

    public long getTotalEnergyConducted(TileEntityBase tileentity)
    {
        long l = 0L;
        if((tileentity instanceof IEnergyConductor) || (tileentity instanceof IEnergySink))
        {
            List list = discover(tileentity, true, 0x7fffffff);
            for(Iterator iterator1 = list.iterator(); iterator1.hasNext();)
            {
                EnergyPath energypath1 = (EnergyPath)iterator1.next();
                IEnergySource ienergysource = (IEnergySource)energypath1.target;
                if(energySourceToEnergyPathMap.containsKey(ienergysource) && (double)ienergysource.getMaxEnergyOutput() > energypath1.loss)
                {
                    Iterator iterator2 = ((List)energySourceToEnergyPathMap.get(ienergysource)).iterator();
                    while(iterator2.hasNext()) 
                    {
                        EnergyPath energypath2 = (EnergyPath)iterator2.next();
                        if((tileentity instanceof IEnergySink) && energypath2.target == tileentity || (tileentity instanceof IEnergyConductor) && energypath2.conductors.contains(tileentity))
                        {
                            l += energypath2.totalEnergyConducted;
                        }
                    }
                }
            }

        }
        if((tileentity instanceof IEnergySource) && energySourceToEnergyPathMap.containsKey(tileentity))
        {
            for(Iterator iterator = ((List)energySourceToEnergyPathMap.get(tileentity)).iterator(); iterator.hasNext();)
            {
                EnergyPath energypath = (EnergyPath)iterator.next();
                l += energypath.totalEnergyConducted;
            }

        }
        return l;
    }

    public List discover(TileEntityBase tileentity, boolean flag, int i)
    {
        HashMap hashmap = new HashMap();
        LinkedList linkedlist = new LinkedList();
        linkedlist.add(tileentity);
        do
        {
            if(linkedlist.isEmpty())
            {
                break;
            }
            TileEntityBase tileentity1 = (TileEntityBase)linkedlist.remove();
            if(!tileentity1.isInvalid())
            {
                double d = 0.0D;
                if(tileentity1 != tileentity)
                {
                    d = ((EnergyBlockLink)hashmap.get(tileentity1)).loss;
                }
                List list = getValidReceivers(tileentity1, flag);
                Iterator iterator1 = list.iterator();
                do
                {
                    if(!iterator1.hasNext())
                    {
                        break;
                    }
                    EnergyTarget energytarget = (EnergyTarget)iterator1.next();
                    if(energytarget.tileEntity == tileentity)
                    {
                        continue;
                    }
                    double d1 = 0.0D;
                    if(energytarget.tileEntity instanceof IEnergyConductor)
                    {
                        d1 = ((IEnergyConductor)energytarget.tileEntity).getConductionLoss();
                        if(d1 < 0.0001D)
                        {
                            d1 = 0.0001D;
                        }
                        if(d + d1 >= (double)i)
                        {
                            continue;
                        }
                    }
                    if(!hashmap.containsKey(energytarget.tileEntity) || ((EnergyBlockLink)hashmap.get(energytarget.tileEntity)).loss > d + d1)
                    {
                        hashmap.put(energytarget.tileEntity, new EnergyBlockLink(energytarget.direction, d + d1));
                        if(energytarget.tileEntity instanceof IEnergyConductor)
                        {
                            linkedlist.remove(energytarget.tileEntity);
                            linkedlist.add(energytarget.tileEntity);
                        }
                    }
                } while(true);
            }
        } while(true);
        LinkedList linkedlist1 = new LinkedList();
        Iterator iterator = hashmap.entrySet().iterator();
label0:
        do
        {
            if(!iterator.hasNext())
            {
                break;
            }
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            TileEntityBase tileentity2 = (TileEntityBase)entry.getKey();
            if((flag || !(tileentity2 instanceof IEnergySink)) && (!flag || !(tileentity2 instanceof IEnergySource)))
            {
                continue;
            }
            EnergyBlockLink energyblocklink = (EnergyBlockLink)entry.getValue();
            EnergyPath energypath = new EnergyPath();
            if(energyblocklink.loss > 0.10000000000000001D)
            {
                energypath.loss = energyblocklink.loss;
            } else
            {
                energypath.loss = 0.10000000000000001D;
            }
            energypath.target = tileentity2;
            energypath.targetDirection = energyblocklink.direction;
            IEnergyConductor ienergyconductor;
            if(!flag && (tileentity instanceof IEnergySource))
            {
                do
                {
                    tileentity2 = energyblocklink.direction.applyToTileEntity(tileentity2);
                    if(tileentity2 == tileentity)
                    {
                        break;
                    }
                    if(tileentity2 instanceof IEnergyConductor)
                    {
                        ienergyconductor = (IEnergyConductor)tileentity2;
                        if(tileentity2.x < energypath.minX)
                        {
                            energypath.minX = tileentity2.x;
                        }
                        if(tileentity2.y < energypath.minY)
                        {
                            energypath.minY = tileentity2.y;
                        }
                        if(tileentity2.z < energypath.minZ)
                        {
                            energypath.minZ = tileentity2.z;
                        }
                        if(tileentity2.x > energypath.maxX)
                        {
                            energypath.maxX = tileentity2.x;
                        }
                        if(tileentity2.y > energypath.maxY)
                        {
                            energypath.maxY = tileentity2.y;
                        }
                        if(tileentity2.z > energypath.maxZ)
                        {
                            energypath.maxZ = tileentity2.z;
                        }
                        energypath.conductors.add(ienergyconductor);
                        if(ienergyconductor.getInsulationEnergyAbsorption() < energypath.minInsulationEnergyAbsorption)
                        {
                            energypath.minInsulationEnergyAbsorption = ienergyconductor.getInsulationEnergyAbsorption();
                        }
                        if(ienergyconductor.getInsulationBreakdownEnergy() < energypath.minInsulationBreakdownEnergy)
                        {
                            energypath.minInsulationBreakdownEnergy = ienergyconductor.getInsulationBreakdownEnergy();
                        }
                        if(ienergyconductor.getConductorBreakdownEnergy() < energypath.minConductorBreakdownEnergy)
                        {
                            energypath.minConductorBreakdownEnergy = ienergyconductor.getConductorBreakdownEnergy();
                        }
                        energyblocklink = (EnergyBlockLink)hashmap.get(tileentity2);
                        if(energyblocklink == null)
                        {
                            throw new RuntimeException((new StringBuilder()).append("EnergyNet: reachedTileEntities corrupted (").append(energypath.target).append(" [").append(energypath.target.x).append(" ").append(energypath.target.y).append(" ").append(energypath.target.z).append("] -> ").append(tileentity2).append(" [").append(tileentity2.x).append(" ").append(tileentity2.y).append(" ").append(tileentity2.z).append("] -> ").append(tileentity).append(" [").append(tileentity.x).append(" ").append(tileentity.y).append(" ").append(tileentity.z).append("])").toString());
                        }
                    } else
                    {
                        if(tileentity2 != null)
                        {
                            System.out.println((new StringBuilder()).append("EnergyNet: EnergyBlockLink corrupted (").append(energypath.target).append(" [").append(energypath.target.x).append(" ").append(energypath.target.y).append(" ").append(energypath.target.z).append("] -> ").append(tileentity2).append(" [").append(tileentity2.x).append(" ").append(tileentity2.y).append(" ").append(tileentity2.z).append("] -> ").append(tileentity).append(" [").append(tileentity.x).append(" ").append(tileentity.y).append(" ").append(tileentity.z).append("])").toString());
                        }
                        continue label0;
                    }
                } while(true);
            }
            linkedlist1.add(energypath);
        } while(true);
        return linkedlist1;
    }

    public List getValidReceivers(TileEntityBase tileentity, boolean flag)
    {
        LinkedList linkedlist = new LinkedList();
        Direction[] adirection = Direction.values();
        int i = adirection.length;
        for(int j = 0; j < i; j++)
        {
            Direction direction = adirection[j];
            TileEntityBase tileentity1 = direction.applyToTileEntity(tileentity);
            if(!(tileentity1 instanceof IEnergyTile) || !((IEnergyTile)tileentity1).isAddedToEnergyNet())
            {
                continue;
            }
            Direction direction1 = direction.getInverse();
            if((!flag && (tileentity instanceof IEnergyEmitter) && ((IEnergyEmitter)tileentity).emitsEnergyTo(tileentity1, direction) || flag && (tileentity instanceof IEnergyAcceptor) && ((IEnergyAcceptor)tileentity).acceptsEnergyFrom(tileentity1, direction)) && (!flag && (tileentity1 instanceof IEnergyAcceptor) && ((IEnergyAcceptor)tileentity1).acceptsEnergyFrom(tileentity, direction1) || flag && (tileentity1 instanceof IEnergyEmitter) && ((IEnergyEmitter)tileentity1).emitsEnergyTo(tileentity, direction1)))
            {
                linkedlist.add(new EnergyTarget(tileentity1, direction1));
            }
        }

        return linkedlist;
    }

    public static final double minConductionLoss = 0.0001D;
    private static final Map<Level, EnergyNet> worldToEnergyNetMap = new HashMap<>();
    private static final Map<Living, Integer> entityLivingToShockEnergyMap = new HashMap<>();
    private final Level world;
    private final Map<IEnergySource, List<EnergyPath>> energySourceToEnergyPathMap;
    static final boolean $assertionsDisabled; /* synthetic field */

    static 
    {
        $assertionsDisabled = !(EnergyNet.class).desiredAssertionStatus();
    }
}
