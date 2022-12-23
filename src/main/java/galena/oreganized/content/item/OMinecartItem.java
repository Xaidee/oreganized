package galena.oreganized.content.item;

import galena.oreganized.content.entity.MinecartShrapnelBomb;
import galena.oreganized.index.OEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class OMinecartItem extends MinecartItem {

    private static Item followItem = Items.HOPPER_MINECART;
    public final Supplier<EntityType<MinecartShrapnelBomb>> minecart;



    public OMinecartItem(AbstractMinecart.Type base, Supplier<EntityType<MinecartShrapnelBomb>> minecart) {
        super(base, new Properties().tab(CreativeModeTab.TAB_TRANSPORTATION));
        this.minecart = minecart;
        DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
    }

    public OMinecartItem(AbstractMinecart.Type base, Supplier<EntityType<MinecartShrapnelBomb>> minecart, ItemLike follow) {
        super(base, new Properties()
                .tab(Objects.requireNonNull(follow.asItem().getItemCategory()))
        );
        followItem = follow.asItem();
        this.minecart = minecart;
        DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
    }

    public static AbstractMinecart createMinecart(Level world, double x, double y, double z, EntityType<? extends AbstractMinecart> entityType) {
        if (entityType == OEntityTypes.SHRAPNEL_BOMB_MINECART.get()) {
            return new MinecartShrapnelBomb(world, x, y, z);
        } else {
            return new Minecart(world, x, y, z);
        }
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
        if (this.allowdedIn(tab))
            insert(new ItemStack(this), false, items, stack -> stack.getItem() == followItem);
    }

    public static void insert(ItemStack stack, boolean before, NonNullList<ItemStack> items, Predicate<ItemStack> filter) {
        if (items.stream().anyMatch(filter)) {
            Optional<ItemStack> optional = items.stream().filter(filter).max((a, b) ->
            {
                int valA = items.indexOf(a);
                int valB = items.indexOf(b);
                if (valA == -1 && valB == -1)
                    return 0;
                if (valA == -1)
                    return valB;
                if (valB == -1)
                    return valA;
                return before ? valB - valA : valA - valB;
            });
            if (optional.isPresent()) {
                items.add(items.indexOf(optional.get()) + (before ? 0 : 1), stack);
                return;
            }
        }
        items.add(stack);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);
        if (!blockstate.is(BlockTags.RAILS)) {
            return InteractionResult.FAIL;
        } else {
            ItemStack itemstack = context.getItemInHand();
            if (!level.isClientSide) {
                RailShape railshape = blockstate.getBlock() instanceof BaseRailBlock ? ((BaseRailBlock)blockstate.getBlock()).getRailDirection(blockstate, level, blockpos, null) : RailShape.NORTH_SOUTH;
                double d0 = 0.0D;
                if (railshape.isAscending()) {
                    d0 = 0.5D;
                }

                AbstractMinecart abstractminecart = createMinecart(level, (double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.0625D + d0, (double)blockpos.getZ() + 0.5D, this.minecart.get());
                if (itemstack.hasCustomHoverName()) {
                    abstractminecart.setCustomName(itemstack.getHoverName());
                }

                level.addFreshEntity(abstractminecart);
                level.gameEvent(GameEvent.ENTITY_PLACE, blockpos);
            }

            itemstack.shrink(1);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    private static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
        private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

        public ItemStack execute(BlockSource dispenser, ItemStack itemStack) {
            Direction direction = dispenser.getBlockState().getValue(DispenserBlock.FACING);
            Level level = dispenser.getLevel();
            double d0 = dispenser.x() + (double)direction.getStepX() * 1.125D;
            double d1 = Math.floor(dispenser.y()) + (double)direction.getStepY();
            double d2 = dispenser.z() + (double)direction.getStepZ() * 1.125D;
            BlockPos blockpos = dispenser.getPos().relative(direction);
            BlockState blockstate = level.getBlockState(blockpos);
            RailShape railshape = blockstate.getBlock() instanceof BaseRailBlock ? ((BaseRailBlock)blockstate.getBlock()).getRailDirection(blockstate, level, blockpos, null) : RailShape.NORTH_SOUTH;
            double d3;
            if (blockstate.is(BlockTags.RAILS)) {
                if (railshape.isAscending()) {
                    d3 = 0.6D;
                } else {
                    d3 = 0.1D;
                }
            } else {
                if (!blockstate.isAir() || !level.getBlockState(blockpos.below()).is(BlockTags.RAILS)) {
                    return this.defaultDispenseItemBehavior.dispense(dispenser, itemStack);
                }

                BlockState blockstate1 = level.getBlockState(blockpos.below());
                RailShape railshape1 = blockstate1.getBlock() instanceof BaseRailBlock ? blockstate1.getValue(((BaseRailBlock)blockstate1.getBlock()).getShapeProperty()) : RailShape.NORTH_SOUTH;
                if (direction != Direction.DOWN && railshape1.isAscending()) {
                    d3 = -0.4D;
                } else {
                    d3 = -0.9D;
                }
            }

            AbstractMinecart abstractminecart = OMinecartItem.createMinecart(level, d0, d1 + d3, d2, OEntityTypes.SHRAPNEL_BOMB_MINECART.get());
            if (itemStack.hasCustomHoverName()) {
                abstractminecart.setCustomName(itemStack.getHoverName());
            }

            level.addFreshEntity(abstractminecart);
            itemStack.shrink(1);
            return itemStack;
        }

        protected void playSound(BlockSource p_42947_) {
            p_42947_.getLevel().levelEvent(1000, p_42947_.getPos(), 0);
        }
    };
}
