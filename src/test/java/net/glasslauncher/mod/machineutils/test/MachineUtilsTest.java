package net.glasslauncher.mod.machineutils.test;

import net.glasslauncher.mod.machineutils.api.energy.EnergyTier;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.event.item.IsItemEffectiveOnBlockEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.tags.TagRegistry;

public class MachineUtilsTest {
    public static ElectricDrill electricDrill;

    @EventListener
    public static void event(IsItemEffectiveOnBlockEvent event) {
        if (event.itemInstance.getType() instanceof ElectricDrill) {
            event.effective = ((ElectricDrill) event.itemInstance.getType()).getEnergy(event.itemInstance) >= 1000;
        }
    }

    @EventListener
    public static void init(ItemRegistryEvent event) {
        for (int i = 0; i != 100; i++) {
            System.out.println("e");
        }
        electricDrill = new ElectricDrill(Identifier.of("machineutilstest:electricdrill"), EnergyTier.MV, 2);
        //electricDrill.setTexture(Identifier.of("machineutilstest:drill"));
        electricDrill.setTranslationKey("machineutilstest:electricdrill");

        ItemInstance drill = new ItemInstance(electricDrill, 1);
        TagRegistry.INSTANCE.register(Identifier.of("tools/pickaxes"), drill, drill::isDamageAndIDIdentical);
    }

    @EventListener
    public static void textures(TextureRegisterEvent event) {
        electricDrill.setTexture(Identifier.of("machineutilstest:drill"));
    }
}
