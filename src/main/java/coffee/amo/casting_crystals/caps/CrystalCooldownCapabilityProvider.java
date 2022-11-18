package coffee.amo.casting_crystals.caps;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CrystalCooldownCapabilityProvider implements ICapabilitySerializable<CompoundTag> {
    public static final ResourceLocation ID = new ResourceLocation("casting_crystals:crystal_cooldown");
    public static final Capability<CrystalCooldownCapabilityHandler> CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
    private final LazyOptional<CrystalCooldownCapability> implContainer = LazyOptional.of(CrystalCooldownCapability::new);
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return CAPABILITY == cap ? implContainer.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        if(!implContainer.isPresent())
            return new CompoundTag();
        return implContainer.resolve().get().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        implContainer.ifPresent(cap -> cap.deserializeNBT(nbt));
    }

    public static CrystalCooldownCapabilityHandler getOrDefault(ItemStack stack){
        return stack.getCapability(CAPABILITY).orElse(new CrystalCooldownCapability());
    }
}
