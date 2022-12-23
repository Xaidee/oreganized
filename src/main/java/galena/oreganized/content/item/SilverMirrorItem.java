package galena.oreganized.content.item;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import galena.oreganized.content.ISilver;
import galena.oreganized.index.OItems;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

@MethodsReturnNonnullByDefault
public class SilverMirrorItem extends OItem implements ISilver {

    public static final int TexturedFrames = 8;

    public SilverMirrorItem(Item.Properties properties) {
        super(properties, Items.SPYGLASS);
    }

    /*@Override
    public InteractionResult interactLivingEntity(ItemStack item, Player player, LivingEntity entity, InteractionHand hand) {
        RandomSource random = player.getRandom();
        if (entity instanceof ZombieVillager zombieVillager && entity.hasEffect(MobEffects.WEAKNESS)) {
            int unbreakingLevel = 0;
            if (!player.getAbilities().instabuild && random.nextInt(1 + unbreakingLevel) == 0) {
                player.playSound(SoundEvents.ITEM_BREAK);
                player.setItemInHand(hand, new ItemStack(OItems.BROKEN_SILVER_MIRROR.get()));
            }
            if (!entity.level.isClientSide)
                zombieVillager.startConverting(player.getUUID(), player.getRandom().nextInt(2401) + 3600);

            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(item, player, entity, hand);
    }*/

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int i, boolean idk) {
        if (!(entity instanceof Player player)) return;
        BlockPos pos = player.getOnPos();
        int dist = getUndeadDistance(world, pos, player, TexturedFrames);

        CompoundTag nbt = stack.getOrCreateTag();
        nbt.putInt("Level", dist);
    }
}
