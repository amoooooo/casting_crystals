package coffee.amo.casting_crystals;

import coffee.amo.casting_crystals.caps.CrystalCooldownCapabilityProvider;
import coffee.amo.casting_crystals.config.CastingCrystalsConfig;
import coffee.amo.casting_crystals.net.PacketHandler;
import coffee.amo.casting_crystals.net.ServerboundCrystalCastPacket;
import coffee.amo.casting_crystals.registry.KeyMappingRegistry;
import coffee.amo.casting_crystals.util.CastingUtil;
import com.hollingsworth.arsnouveau.common.items.CasterTome;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.SlotTypeMessage;

import java.util.List;

import static coffee.amo.casting_crystals.registry.ItemRegistry.ITEMS;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CastingCrystals.MODID)
public class CastingCrystals {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "casting_crystals";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, MODID);
    public static final RegistryObject<Attribute> COOLDOWN_REDUCTION = ATTRIBUTES.register("cooldown_reduction", () -> new RangedAttribute("cooldown_reduction", 0.0D, -100.0D, 100.0D).setSyncable(true));
    public CastingCrystals() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CastingCrystalsConfig.GENERAL_SPEC, "casting_crystals.toml");
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::sendIMC);
        ITEMS.register(modEventBus);
        ATTRIBUTES.register(modEventBus);
        PacketHandler.registerMessages();
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void sendIMC(InterModEnqueueEvent event){
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("casting_crystal").size(3).build());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEvents {


        @SubscribeEvent
        public static void registerCapabilities(RegisterCapabilitiesEvent event){
            event.register(CrystalCooldownCapabilityProvider.class);
        }

        @SubscribeEvent
        public static void attrMod(final EntityAttributeModificationEvent event){
            event.add(EntityType.PLAYER, COOLDOWN_REDUCTION.get(), 0.0);
        }
    }
}
