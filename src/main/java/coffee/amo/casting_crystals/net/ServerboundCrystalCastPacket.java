package coffee.amo.casting_crystals.net;

import coffee.amo.casting_crystals.caps.CrystalCooldownCapabilityProvider;
import coffee.amo.casting_crystals.config.CastingCrystalsConfig;
import coffee.amo.casting_crystals.util.CastingUtil;
import com.hollingsworth.arsnouveau.common.items.CasterTome;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.UUID;
import java.util.function.Supplier;

import static coffee.amo.casting_crystals.CastingCrystals.COOLDOWN_REDUCTION;

public class ServerboundCrystalCastPacket {
    public final UUID playerUUID;
    public final int crystalSlot;

    public ServerboundCrystalCastPacket(UUID playerUUID, int crystalSlot) {
        this.playerUUID = playerUUID;
        this.crystalSlot = crystalSlot;
    }

    public static void encode(ServerboundCrystalCastPacket msg, FriendlyByteBuf buffer) {
        buffer.writeUUID(msg.playerUUID);
        buffer.writeInt(msg.crystalSlot);
    }

    public static ServerboundCrystalCastPacket decode(FriendlyByteBuf buffer) {
        return new ServerboundCrystalCastPacket(buffer.readUUID(), buffer.readInt());
    }

    public static void handle(ServerboundCrystalCastPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            if(player == null) return;
            ItemStack crystal = CuriosApi.getCuriosHelper().findCurios(player, "casting_crystal").get(msg.crystalSlot).stack();
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
                            }
                        }
                    });
                } else {
                    CastingUtil.use(player.level, player, InteractionHand.MAIN_HAND, crystal);
                }
            }

        });
        ctx.get().setPacketHandled(true);
    }
}
