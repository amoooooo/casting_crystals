package coffee.amo.casting_crystals.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeyMappingRegistry {
    public static final KeyMapping CAST_1 = new KeyMapping("key.casting_crystals.casting_crystal_1", GLFW.GLFW_KEY_1, "key.categories.casting_crystals");
    public static final KeyMapping CAST_2 = new KeyMapping("key.casting_crystals.casting_crystal_2", GLFW.GLFW_KEY_2, "key.categories.casting_crystals");
    public static final KeyMapping CAST_3 = new KeyMapping("key.casting_crystals.casting_crystal_3", GLFW.GLFW_KEY_3, "key.categories.casting_crystals");
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onKeyMappingEvent(RegisterKeyMappingsEvent event){
        CAST_1.setKeyModifierAndCode(KeyModifier.CONTROL, CAST_1.getKey());
        CAST_2.setKeyModifierAndCode(KeyModifier.CONTROL, CAST_2.getKey());
        CAST_3.setKeyModifierAndCode(KeyModifier.CONTROL, CAST_3.getKey());
        event.register(CAST_1);
        event.register(CAST_2);
        event.register(CAST_3);
    }
}
