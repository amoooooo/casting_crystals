package coffee.amo.casting_crystals;

import coffee.amo.casting_crystals.caps.CrystalCooldownCapabilityProvider;
import coffee.amo.casting_crystals.config.CastingCrystalsConfig;
import coffee.amo.casting_crystals.net.PacketHandler;
import coffee.amo.casting_crystals.net.ServerboundCrystalCastPacket;
import coffee.amo.casting_crystals.registry.KeyMappingRegistry;
import coffee.amo.casting_crystals.util.CastingUtil;
import com.hollingsworth.arsnouveau.common.items.CasterTome;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;

import static coffee.amo.casting_crystals.CastingCrystals.COOLDOWN_REDUCTION;
import static coffee.amo.casting_crystals.CastingCrystals.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEvents {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event){
        if(event.phase == TickEvent.Phase.START){
            if(Minecraft.getInstance().player != null && Minecraft.getInstance().level != null){
                Player player = Minecraft.getInstance().player;
                Level world = Minecraft.getInstance().level;
                if(KeyMappingRegistry.CAST_1.consumeClick()){
                    castCrystal(player, 0);
                }
                if(KeyMappingRegistry.CAST_2.consumeClick()){
                    castCrystal(player, 1);
                }
                if(KeyMappingRegistry.CAST_3.consumeClick()){
                    castCrystal(player, 2);
                }
            }
        }
    }

    public static void castCrystal(Player player, int slot) {
        List<SlotResult> stack = CuriosApi.getCuriosHelper().findCurios(player, "casting_crystal");
        if(stack.size() <= slot) return;
        if(stack.isEmpty() || stack.get(slot) == null) return;
        ItemStack crystal = stack.get(slot).stack();
        if(crystal.equals(ItemStack.EMPTY)) return;
        if(crystal.getItem() instanceof CasterTome tome){
            if(CastingCrystalsConfig.enableCooldowns.get()){
                crystal.getCapability(CrystalCooldownCapabilityProvider.CAPABILITY).ifPresent(s -> {
                    if(player.getAttributes().hasAttribute(COOLDOWN_REDUCTION.get())){
                        if(s.getCooldown() <= 0){
                            CastingUtil.use(player.level, player, InteractionHand.MAIN_HAND, crystal);
                            float reduction = (float) player.getAttributes().getValue(COOLDOWN_REDUCTION.get());
                            if(reduction > 0){
                                reduction = reduction/100f;
                            }
                            s.setCooldown((float) (CastingCrystalsConfig.baseCooldown.get() * (1 - (reduction))));
                            PacketHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), new ServerboundCrystalCastPacket(player.getUUID(), slot));
                        }
                    }
                });
            } else {
                CastingUtil.use(player.level, player, InteractionHand.MAIN_HAND, crystal);
                PacketHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), new ServerboundCrystalCastPacket(player.getUUID(), slot));
            }
        }

    }
}
