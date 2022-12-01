package coffee.amo.casting_crystals.loot;

import coffee.amo.casting_crystals.registry.ItemRegistry;
import com.hollingsworth.arsnouveau.api.spell.ISpellCaster;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.util.CasterUtil;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import com.hollingsworth.arsnouveau.common.spell.effect.*;
import com.hollingsworth.arsnouveau.common.spell.method.MethodProjectile;
import com.hollingsworth.arsnouveau.common.spell.method.MethodSelf;
import com.hollingsworth.arsnouveau.setup.ItemsRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;

import static com.hollingsworth.arsnouveau.api.loot.DungeonLootTables.RARE_LOOT;

public class DungeonLootTables {
    public static void init() {
        RARE_LOOT.add(() -> makeCrystal("Rallying Shout", new Spell()
                .add(MethodSelf.INSTANCE)
                .add(EffectHeal.INSTANCE)
                .add(EffectLinger.INSTANCE)
                .add(AugmentDecelerate.INSTANCE)
                .add(AugmentDecelerate.INSTANCE)
                .add(AugmentDecelerate.INSTANCE)
                .add(AugmentDurationDown.INSTANCE)
                .add(AugmentDurationDown.INSTANCE)
                .withColor(new ParticleColor(25, 255, 110)),
                "A rallying shout that heals nearby allies."));

        RARE_LOOT.add(() -> makeCrystal("Firebolt", new Spell()
                .add(MethodProjectile.INSTANCE)
                .add(AugmentPierce.INSTANCE)
                .add(EffectIgnite.INSTANCE)
                .add(AugmentDurationDown.INSTANCE)
                .add(EffectFlare.INSTANCE)
                .withColor(new ParticleColor(255, 124, 0)),
                "A bolt of flame that ignites and explodes."));

        RARE_LOOT.add(() -> makeCrystal("Ice Shard", new Spell()
                .add(MethodProjectile.INSTANCE)
                .add(EffectFreeze.INSTANCE)
                .add(AugmentDurationDown.INSTANCE)
                .add(EffectColdSnap.INSTANCE)
                .add(AugmentExtendTime.INSTANCE)
                .add(AugmentAOE.INSTANCE)
                .withColor(new ParticleColor(47, 131, 255)),
                "A shard of ice that shatters on impact."));

        RARE_LOOT.add(() -> makeCrystal("Lightning Bolt", new Spell()
                .add(MethodProjectile.INSTANCE)
                .add(EffectLightning.INSTANCE)
                .add(AugmentAOE.INSTANCE)
                .add(AugmentAOE.INSTANCE)
                .add(AugmentAOE.INSTANCE)
                .add(AugmentAOE.INSTANCE)
                .withColor(new ParticleColor(255, 255, 255)),
                "A bolt of lightning that arcs to nearby targets."));
    }

    public static ItemStack makeCrystal(String name, Spell spell) {
        ItemStack stack = new ItemStack(ItemRegistry.CASTING_CRYSTAL.get());
        ISpellCaster spellCaster = CasterUtil.getCaster(stack);
        spellCaster.setSpell(spell);
        stack.setHoverName(Component.literal(name).setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE).withItalic(true)));
        return stack;
    }

    public static ItemStack makeCrystal(String name, Spell spell, String flavorText) {
        ItemStack stack = makeCrystal(name, spell);
        ISpellCaster spellCaster = CasterUtil.getCaster(stack);
        spellCaster.setFlavorText(flavorText);
        return stack;
    }

    public static ItemStack makeCrystal(String name, Spell spell, String flavorText, ParticleColor particleColor) {
        ItemStack stack = makeCrystal(name, spell);
        ISpellCaster spellCaster = CasterUtil.getCaster(stack);
        spellCaster.setFlavorText(flavorText);
        spellCaster.setColor(particleColor);
        return stack;
    }
}
