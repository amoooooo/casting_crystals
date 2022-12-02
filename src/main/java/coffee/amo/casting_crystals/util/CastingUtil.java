package coffee.amo.casting_crystals.util;

import com.hollingsworth.arsnouveau.api.item.ICasterTool;
import com.hollingsworth.arsnouveau.api.spell.ISpellCaster;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.util.ManaUtil;
import com.hollingsworth.arsnouveau.common.items.CasterTome;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CastingUtil {
    public static InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn, ItemStack stack) {
        if(stack.getItem() instanceof ICasterTool tome){
            ISpellCaster caster = tome.getSpellCaster(stack);
            Spell spell = caster.getSpell();
            // Let even a new player cast 1 charge of a tome
            spell.addDiscount(spell.getNoDiscountCost());
            return caster.castSpell(worldIn, playerIn, handIn, Component.translatable(""), spell);
        }
        return InteractionResultHolder.pass(stack);
    }
}
