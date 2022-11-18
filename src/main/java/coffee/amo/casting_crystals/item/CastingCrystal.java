package coffee.amo.casting_crystals.item;

import coffee.amo.casting_crystals.caps.CrystalCooldownCapabilityProvider;
import coffee.amo.casting_crystals.config.CastingCrystalsConfig;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.api.spell.ISpellCaster;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.util.CasterUtil;
import com.hollingsworth.arsnouveau.common.items.CasterTome;
import com.hollingsworth.arsnouveau.common.items.SpellBook;
import com.hollingsworth.arsnouveau.setup.SoundRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

public class CastingCrystal extends CasterTome {
    public CastingCrystal(Properties properties) {
        super(properties);
    }
    @Override
    public boolean overrideOtherStackedOnMe(ItemStack pStack, ItemStack pOther, Slot pSlot, ClickAction pAction, Player pPlayer, SlotAccess pAccess) {
        if(pOther.getItem() instanceof CasterTome) {
            ISpellCaster otherCaster = CasterUtil.getCaster(pOther);
            ISpellCaster thisCaster = CasterUtil.getCaster(pStack);
            if(thisCaster.getSpell().isEmpty()){
                thisCaster.setSpell(otherCaster.getSpell());
                int discount = CastingCrystalsConfig.enableCooldowns.get() ? 1000000000 : thisCaster.getSpell().getDiscountedCost()/2;
                thisCaster.getSpell().addDiscount(discount);
                otherCaster.setSpell(Spell.EMPTY);
                pPlayer.playSound(SoundEvents.BOOK_PAGE_TURN, 1.0F, 1.0F);
                return true;
            }
        }
        if(pOther.getItem() instanceof SpellBook sb) {
            ISpellCaster thisCaster = CasterUtil.getCaster(pStack);
            if(thisCaster.getSpell().isEmpty()){
                thisCaster.setSpell(sb.getSpellCaster().getSpell());
                int discount = CastingCrystalsConfig.enableCooldowns.get() ? 1000000000 : thisCaster.getSpell().getDiscountedCost()/2;
                thisCaster.getSpell().addDiscount(discount);
                pPlayer.playSound(SoundEvents.BOOK_PAGE_TURN, 1.0F, 1.0F);
                return true;
            }
        }
        return super.overrideOtherStackedOnMe(pStack, pOther, pSlot, pAction, pPlayer, pAccess);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if(pStack.getItem() instanceof CastingCrystal){
            pStack.getCapability(CrystalCooldownCapabilityProvider.CAPABILITY).ifPresent(cap -> {
                if(cap.getCooldown() > 0){
                    cap.decrementCooldown();
                }
                if(cap.getCooldown() == 1) {
                    pEntity.playSound(SoundEvents.PLAYER_LEVELUP, 1.0F, 1.0F);
                }
            });
        }
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    @Override
    public @Nullable CompoundTag getShareTag(ItemStack stack) {
        CompoundTag tag = super.getShareTag(stack);
        if(tag == null)
            tag = new CompoundTag();
        tag.put("cooldown", CrystalCooldownCapabilityProvider.getOrDefault(stack).serializeNBT());
        return tag;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        super.readShareTag(stack, nbt);
        if(nbt != null)
            CrystalCooldownCapabilityProvider.getOrDefault(stack).deserializeNBT(nbt.getCompound("cooldown"));
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        if(stack.getItem() instanceof CastingCrystal)
            return new CrystalCooldownCapabilityProvider();
        return super.initCapabilities(stack, nbt);
    }
}
