package galena.oreganized.client;

import com.mojang.math.Axis;
import galena.oreganized.Oreganized;
import galena.oreganized.client.render.entity.LeadBoltRender;
import galena.oreganized.client.render.entity.ShrapnelBombMinecartRender;
import galena.oreganized.client.render.entity.ShrapnelBombRender;
import galena.oreganized.client.render.gui.StunningOverlay;
import galena.oreganized.index.OBlocks;
import galena.oreganized.index.OEntityTypes;
import galena.oreganized.index.OItems;
import galena.oreganized.world.IDoorProgressHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Oreganized.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OreganizedClient {

    private static void render(Supplier<? extends Block> block, RenderType render) {
        ItemBlockRenderTypes.setRenderLayer(block.get(), render);
    }

    @SubscribeEvent
    public static void setup(FMLClientSetupEvent event) {
        OreganizedClient.registerBlockRenderers();
        OreganizedClient.registerItemProperties();
    }

    private static void registerItemProperties() {
        ItemProperties.register(OItems.SILVER_MIRROR.get(), new ResourceLocation("level"), (stack, world, entity, seed) -> {
            if (entity == null) {
                return 8;
            } else {
                return stack.getOrCreateTag().getInt("Level");
            }
        });

        ItemProperties.register(Items.CROSSBOW, new ResourceLocation(Oreganized.MOD_ID, "lead_bolt"), (stack, level, user, i) ->
                CrossbowItem.isCharged(stack) && CrossbowItem.containsChargedProjectile(stack, OItems.LEAD_BOLT.get()) ? 1.0F : 0.0F
        );

        ItemProperties.register(OItems.ELECTRUM_SHIELD.get(), new ResourceLocation("blocking"), (stack, level, user, i) ->
                user != null && user.isUsingItem() && user.getUseItem() == stack ? 1.0F : 0.0F
        );
    }

    private static void registerBlockRenderers() {
        RenderType cutout = RenderType.cutout();
        RenderType translucent = RenderType.translucent();

        render(OBlocks.LEAD_DOOR, cutout);
        render(OBlocks.LEAD_TRAPDOOR, cutout);
        render(OBlocks.LEAD_BARS, cutout);
        render(OBlocks.GARGOYLE, cutout);

        OBlocks.CRYSTAL_GLASS.forEach((c, b) -> render(b, translucent));
        OBlocks.CRYSTAL_GLASS_PANES.forEach((c, b) -> render(b, translucent));

        render(OBlocks.GROOVED_ICE, translucent);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(OEntityTypes.SHRAPNEL_BOMB.get(), ShrapnelBombRender::new);
        event.registerEntityRenderer(OEntityTypes.SHRAPNEL_BOMB_MINECART.get(), ShrapnelBombMinecartRender::new);
        event.registerEntityRenderer(OEntityTypes.LEAD_BOLT.get(), LeadBoltRender::new);
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        MinecraftForge.EVENT_BUS.register(new StunningOverlay());
        event.registerAbove(VanillaGuiOverlay.FROSTBITE.id(), "stunning", new StunningOverlay());
    }

    public static void renderThirdPersonArm(ModelPart arm, boolean rightArm) {
        arm.xRot = -1.7F;
        arm.yRot = rightArm ? -0.1F : 0.2F;
    }

    @Mod.EventBusSubscriber(modid = Oreganized.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeBusEvents {

        @SubscribeEvent
        public static void addTooltips(ItemTooltipEvent event) {
            if (event.getItemStack().is(OItems.BUSH_HAMMER.get())) {
                List<Component> tooltip = event.getToolTip();
                MutableComponent wipTitle = Component.translatable("tooltip.oreganized.wip.title");
                MutableComponent wipDesc = Component.translatable("tooltip.oreganized.wip.description");

                tooltip.add(wipTitle.withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.BOLD));
                tooltip.add(wipDesc.withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC));
            }
        }

        @SubscribeEvent
        public static void renderHand(RenderHandEvent event) {
            var player = Minecraft.getInstance().player;
            if (!(player instanceof IDoorProgressHolder progressHolder)) return;
            var progress = progressHolder.oreganised$getOpeningProgress();
            if (progress == 0) return;
            if (event.getHand() == InteractionHand.OFF_HAND) return;

            var poseStack = event.getPoseStack();

            poseStack.pushPose();

            var rightArm = player.getMainArm() == HumanoidArm.RIGHT;
            float factor = rightArm ? 1.0F : -1.0F;
            poseStack.translate(factor * 0.84000005F, -0.4F, -0.4F);
            poseStack.mulPose(Axis.YP.rotationDegrees(factor * -20F - event.getSwingProgress()));
            poseStack.mulPose(Axis.ZP.rotationDegrees(factor * 45F));
            poseStack.mulPose(Axis.XP.rotationDegrees(-45F));

            var renderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player);

            if (rightArm) {
                renderer.renderRightHand(poseStack, event.getMultiBufferSource(), event.getPackedLight(), player);
            } else {
                renderer.renderLeftHand(poseStack, event.getMultiBufferSource(), event.getPackedLight(), player);
            }

            poseStack.popPose();

            event.setCanceled(true);
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
