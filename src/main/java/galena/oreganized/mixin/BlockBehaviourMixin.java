package galena.oreganized.mixin;

import galena.oreganized.content.block.LeadOreBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {

    @Inject(
            method = "spawnAfterBreak",
            at = @At("HEAD")
    )
    private void spawnLeadCloud(BlockState state, ServerLevel level, BlockPos pos, ItemStack stack, boolean xp, CallbackInfo ci) {
        LeadOreBlock.trySpawnLeadCloud(state, level, pos, stack);
    }

}
