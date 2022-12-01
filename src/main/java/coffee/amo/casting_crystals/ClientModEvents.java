package coffee.amo.casting_crystals;

import coffee.amo.casting_crystals.item.CastingCrystal;
import coffee.amo.casting_crystals.registry.ItemRegistry;
import com.hollingsworth.arsnouveau.api.spell.ISpellCaster;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = "casting_crystals", value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void registerItemColors(final RegisterColorHandlersEvent.Item event){
        event.register((stack, tintIndex) -> {
            if(tintIndex == 1){
                if(stack.getItem() instanceof CastingCrystal ce) {
                    ISpellCaster caster = ce.getSpellCaster(stack);
                    return caster.getSpell() == Spell.EMPTY ? 0xFFFFFF : caster.getColor().getColor();
                }
            }
            return 0xFFFFFF;
        }, ItemRegistry.CASTING_CRYSTAL.get());
    }
}
