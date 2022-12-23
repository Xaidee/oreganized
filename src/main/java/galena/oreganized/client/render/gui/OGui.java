package galena.oreganized.client.render.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import galena.oreganized.Oreganized;
import galena.oreganized.index.OEffects;
import galena.oreganized.index.OTags;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.ForgeIngameGui;

public class OGui extends ForgeIngameGui {

    protected static final ResourceLocation STUNNING_LOCATION = new ResourceLocation(Oreganized.MOD_ID, "textures/misc/stunning_outline.png");
    protected static final ResourceLocation STUNNING_VIGNETTE_LOCATION = new ResourceLocation(Oreganized.MOD_ID, "textures/misc/stunning_overlay.png");

    public OGui(Minecraft mc) {
        super(mc);
    }

    @Override
    public void render(PoseStack pPoseStack, float pPartialTick) {
        this.screenWidth = this.minecraft.getWindow().getGuiScaledWidth();
        this.screenHeight = this.minecraft.getWindow().getGuiScaledHeight();
        RenderSystem.enableBlend();

        if (this.minecraft.player.hasEffect(OEffects.STUNNING.get())) {
            this.renderTextureOverlay(STUNNING_VIGNETTE_LOCATION, 1);
            this.renderTextureOverlay(STUNNING_LOCATION, 0.8F);
        }

        if (this.minecraft.player.isEyeInFluid(OTags.Fluids.MOLTEN_LEAD))
            this.renderTextureOverlay(STUNNING_VIGNETTE_LOCATION, 1);
    }
}
