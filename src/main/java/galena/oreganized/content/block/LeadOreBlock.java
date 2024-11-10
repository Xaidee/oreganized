package galena.oreganized.content.block;

import galena.oreganized.Oreganized;
import galena.oreganized.OreganizedConfig;
import galena.oreganized.index.OCriteriaTriggers;
import galena.oreganized.index.OEffects;
import galena.oreganized.index.OItems;
import galena.oreganized.index.OParticleTypes;
import galena.oreganized.index.OTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = Oreganized.MOD_ID)
public class LeadOreBlock {

    private static Stream<MobEffectInstance> getEffects(int durationMultiplier) {
        if (OreganizedConfig.COMMON.poisonInsteadOfStunning.get()) {
            return Stream.of(new MobEffectInstance(MobEffects.POISON, 150 * durationMultiplier));
        }

        return Stream.of(
                new MobEffectInstance(OEffects.STUNNING.get(), 600 * durationMultiplier),
                new MobEffectInstance(MobEffects.POISON, 40)
        );
    }

    public static void trySpawnLeadCloud(BlockState state, Level level, BlockPos pos, ItemStack held) {
        if (shouldSpawnCloud(state, level, pos, held)) {
            spawnCloud(level, pos, 2F);
        }
    }

    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent event) {
        var state = event.getState();
        var pos = event.getPos();

        if (!(event.getLevel() instanceof Level level)) return;
        if (!(event.getPlayer() instanceof ServerPlayer player)) return;

        var held = player.getMainHandItem();

        if (shouldSpawnCloud(state, level, pos, held)) {
            OCriteriaTriggers.IN_LEAD_CLOUD.trigger(player);
        }
    }

    private static boolean shouldSpawnCloud(BlockState state, LevelAccessor level, BlockPos pos, ItemStack stack) {
        if (!OreganizedConfig.COMMON.leadDustCloud.get()) return false;
        if (stack.is(OItems.SCRIBE.get()) || EnchantmentHelper.hasSilkTouch(stack)) return false;
        if (!state.is(OTags.Blocks.CREATES_LEAD_CLOUD)) return false;

        for (var direction : Direction.values()) {
            var adjacentState = level.getBlockState(pos.relative(direction));
            if (adjacentState.is(OTags.Blocks.PREVENTS_LEAD_CLOUD)) return false;
        }

        return true;
    }

    public static AreaEffectCloud spawnCloud(Level level, BlockPos pos, float size) {
        var vec = Vec3.atCenterOf(pos);
        var cloud = new AreaEffectCloud(level, vec.x, vec.y, vec.z);

        getEffects(Math.max(1, (int) (size))).forEach(cloud::addEffect);

        cloud.setParticle(OParticleTypes.LEAD_CLOUD.get());
        cloud.setRadius(1.5F * size);
        cloud.setRadiusPerTick(-0.02F);
        cloud.setDuration((int) (120 * size));

        level.addFreshEntity(cloud);
        return cloud;
    }

    public static void blowParticles(LevelAccessor level, BlockPos pos, Direction facing, int maxDistance) {
        var speed = 0.5;
        maxDistance = Math.min(maxDistance, 8);

        for (int distance = 1; distance < maxDistance; distance++) {
            var frontPos = pos.relative(facing, distance);
            var frontState = level.getBlockState(frontPos);

            if (frontState.is(OTags.Blocks.BLOWS_LEAD_CLOUD)) {
                var vec = Vec3.atCenterOf(frontPos);
                level.addParticle(OParticleTypes.LEAD_BLOW.get(),
                        vec.x, vec.y, vec.z,
                        facing.getStepX() * speed, facing.getStepY() * speed, facing.getStepZ() * speed
                );

                var targets = level.getEntitiesOfClass(LivingEntity.class, new AABB(frontPos, pos.relative(facing, maxDistance)).expandTowards(1, 1, 1));

                targets.forEach(target -> {
                    getEffects(1)
                            .filter(it -> !target.hasEffect(it.getEffect()))
                            .forEach(target::addEffect);
                });

                return;
            }
        }
    }

}
