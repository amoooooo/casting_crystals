package coffee.amo.casting_crystals.registry;

import coffee.amo.casting_crystals.CastingCrystals;
import coffee.amo.casting_crystals.item.CastingCrystal;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CastingCrystals.MODID);

    public static RegistryObject<Item> CASTING_CRYSTAL = ITEMS.register("casting_crystal", () -> new CastingCrystal(new Item.Properties().tab(ArsNouveau.itemGroup)));
}
