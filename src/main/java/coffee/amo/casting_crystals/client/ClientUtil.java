package coffee.amo.casting_crystals.client;

import com.hollingsworth.arsnouveau.api.spell.ISpellCaster;
import com.hollingsworth.arsnouveau.common.items.CasterTome;
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.common.color.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import top.theillusivec4.curios.api.CuriosApi;

public class ClientUtil {
    public static ResourceLocation CRYSTAL = new ResourceLocation("casting_crystals", "textures/gui/crystal.png");
    public static void drawHud(RenderGuiOverlayEvent.Post event) {
        float width = event.getWindow().getGuiScaledWidth();
        float height = event.getWindow().getGuiScaledHeight();
        int size = 0;
        if(!CuriosApi.getCuriosHelper().findCurios(Minecraft.getInstance().player, "casting_crystal").isEmpty()){
            size = CuriosApi.getCuriosHelper().findCurios(Minecraft.getInstance().player, "casting_crystal").size();
        }
        for(int i = 0; i < size; i++){
            ItemStack crystal = CuriosApi.getCuriosHelper().findCurios(Minecraft.getInstance().player, "casting_crystal").get(i).stack();
            if(!(crystal.getItem() instanceof CasterTome tome)) return;
            ISpellCaster caster = tome.getSpellCaster(crystal);
            int color = lerpColor(0xFFFFFF, caster.getSpell().color.getColor(), Minecraft.getInstance().player.getCooldowns().getCooldownPercent(crystal.getItem(), event.getPartialTick()));
            float x = (width/2)-88+(20*i);
            float y = height-19;
            x+=5;
            y-=4;
            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int b = (color) & 0xFF;
            RenderSystem.setShaderTexture(0, CRYSTAL);
            RenderSystem.setShaderColor(r/255f, g/255f, b/255f, 1.0F);
            ForgeGui.blit(event.getPoseStack(), (int)x, (int)y, 0, 0, 6, 6, 6, 6);
        }
    }

    private static int lerpColor(int color, int i, float cooldownPercent) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color) & 0xFF;
        int r2 = (i >> 16) & 0xFF;
        int g2 = (i >> 8) & 0xFF;
        int b2 = (i) & 0xFF;
        return toRGBA(
                (int) (r2 + (r - r2) * cooldownPercent),
                (int) (g2 + (g - g2) * cooldownPercent),
                (int) (b2 + (b - b2) * cooldownPercent),
                255
        );
    }

    private static int toRGBA(int r, int g, int b, int a) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
