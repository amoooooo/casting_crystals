package coffee.amo.casting_crystals.item;

import coffee.amo.casting_crystals.config.CastingCrystalsConfig;
import com.hollingsworth.arsnouveau.api.item.ICasterTool;
import com.hollingsworth.arsnouveau.api.spell.ISpellCaster;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.util.CasterUtil;
import com.hollingsworth.arsnouveau.api.util.ManaUtil;
import com.hollingsworth.arsnouveau.common.items.CasterTome;
import com.hollingsworth.arsnouveau.common.items.ModItem;
import com.hollingsworth.arsnouveau.common.items.SpellBook;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.List;

public class CastingCrystal extends ModItem implements ICasterTool {
    public CastingCrystal(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if(CastingCrystalsConfig.enableCooldowns.get()){
            if(!playerIn.getCooldowns().isOnCooldown(this)){
                playerIn.getCooldowns().addCooldown(this, (int)Math.floor(CastingCrystalsConfig.baseCooldown.get()));
                return useSuper(worldIn, playerIn, handIn);
            }
        }
        return useSuper(worldIn, playerIn, handIn);
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
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        if(pEntity instanceof Player player){
            if(player.getCooldowns().getCooldownPercent(pStack.getItem(), 0) == 0.1){
                player.playSound(SoundEvents.PLAYER_LEVELUP, 1.0F, 0.25F);
            }
        }
    }
    public InteractionResultHolder<ItemStack> useSuper(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        ISpellCaster caster = getSpellCaster(stack);
        Spell spell = caster.getSpell();
        // Let even a new player cast 1 charge of a tome
        if (spell.getDiscountedCost() > ManaUtil.getMaxMana(playerIn)) {
            spell.addDiscount(spell.getDiscountedCost() - ManaUtil.getMaxMana(playerIn));
        } else {
            spell.addDiscount(spell.getDiscountedCost() / 2);
        }
        return caster.castSpell(worldIn, playerIn, handIn, Component.translatable(""), spell);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip2, TooltipFlag flagIn) {
        if (worldIn == null)
            return;
        ISpellCaster caster = getSpellCaster(stack);
        Spell spell = caster.getSpell();
        tooltip2.add(Component.literal(spell.getDisplayString()));
        if (!caster.getFlavorText().isEmpty())
            tooltip2.add(Component.literal(caster.getFlavorText()).withStyle(Style.EMPTY.withItalic(true).withColor(ChatFormatting.BLUE)));

        //tooltip2.add(Component.translatable("tooltip.ars_nouveau.caster_tome"));
        super.appendHoverText(stack, worldIn, tooltip2, flagIn);
    }
}
