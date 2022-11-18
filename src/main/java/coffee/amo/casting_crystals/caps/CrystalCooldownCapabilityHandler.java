package coffee.amo.casting_crystals.caps;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface CrystalCooldownCapabilityHandler extends INBTSerializable<CompoundTag> {
    float getCooldown();
    void setCooldown(float cooldown);
    void decrementCooldown();
    void incrementCooldown();
    void resetCooldown();
}
