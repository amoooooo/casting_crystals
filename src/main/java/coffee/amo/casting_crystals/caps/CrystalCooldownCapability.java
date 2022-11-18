package coffee.amo.casting_crystals.caps;

import net.minecraft.nbt.CompoundTag;

public class CrystalCooldownCapability implements CrystalCooldownCapabilityHandler{
    private float cooldown = 0;
    @Override
    public float getCooldown() {
        return cooldown;
    }

    @Override
    public void setCooldown(float cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    public void decrementCooldown() {
        cooldown--;
    }

    @Override
    public void incrementCooldown() {
        cooldown++;
    }

    @Override
    public void resetCooldown() {
        cooldown = 0;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("cooldown", cooldown);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        cooldown = nbt.getFloat("cooldown");
    }
}
