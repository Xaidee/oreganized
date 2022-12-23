package galena.oreganized.client.render.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

@OnlyIn(Dist.CLIENT)
public class StunningOverlay implements IIngameOverlay {
    @Override
    public void render(ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        OGui fakeGui = new OGui(minecraft);

        fakeGui.render(poseStack, partialTick);
    }

}
