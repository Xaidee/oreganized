package galena.oreganized.client;

import galena.oreganized.Oreganized;
import galena.oreganized.client.render.entity.ShrapnelBombMinecartRender;
import galena.oreganized.client.render.entity.ShrapnelBombRender;
import galena.oreganized.index.OBlocks;
import galena.oreganized.index.OEntityTypes;
import galena.oreganized.index.OItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Oreganized.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OreganizedClient {

    private static void render(Supplier<? extends Block> block, RenderType render) {
        ItemBlockRenderTypes.setRenderLayer(block.get(), render);
    }

    public static void registerBlockRenderers() {
        RenderType cutout = RenderType.cutout();
        RenderType mipped = RenderType.cutoutMipped();
        RenderType translucent = RenderType.translucent();

        for (int i = 0; OBlocks.CRYSTAL_GLASS.size() > i; i++) {
            render(OBlocks.CRYSTAL_GLASS.get(i), translucent);
            render(OBlocks.CRYSTAL_GLASS_PANES.get(i), translucent);
        }
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(OEntityTypes.SHRAPNEL_BOMB.get(), ShrapnelBombRender::new);
        event.registerEntityRenderer(OEntityTypes.SHRAPNEL_BOMB_MINECART.get(), ShrapnelBombMinecartRender::new);
    }

    @Mod.EventBusSubscriber(modid = Oreganized.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeBusEvents {

        @SubscribeEvent
        public static void addTooltips(ItemTooltipEvent event) {
            if (event.getItemStack().is(OItems.BUSH_HAMMER.get())) {
                List<Component> tooltip = event.getToolTip();
                MutableComponent wipTitle = (MutableComponent) Component.nullToEmpty("tooltip.oreganized.wip.title");
                MutableComponent wipDesc = (MutableComponent) Component.nullToEmpty("tooltip.oreganized.wip.description");

                tooltip.add(wipTitle.withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.BOLD));
                tooltip.add(wipDesc.withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC));
            }
        }

        /*@SubscribeEvent
        public static void renderMoltenLeadFogColor(ViewportEvent.ComputeFogColor event) {
            Camera camera = event.getCamera();
            FluidState fluidState = camera.getBlockAtCamera().getFluidState();

            if(fluidState.getType().isSame(OFluids.MOLTEN_LEAD.get())) {
                event.setRed(57F / 255F);
                event.setGreen(57F / 255F);
                event.setBlue(95F / 255F);
            }
        }
        @SubscribeEvent
        public static void renderMoltenLeadFogDensity(ViewportEvent.RenderFog event) {
            Camera camera = event.getCamera();
            FluidState fluidState = camera.getBlockAtCamera().getFluidState();

            if(fluidState.getType().isSame(OFluids.MOLTEN_LEAD.get())) {
                event.setFarPlaneDistance(15.0F);
                event.setCanceled(true);
            }
        }*/
    }
}
