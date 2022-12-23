package galena.oreganized.content.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class OBlockItem extends BlockItem {

    @Nullable
    private final Item followItem;

    public OBlockItem(Block block, CreativeModeTab tab) {
        super(block, new Properties()
                .tab(tab));
        this.followItem = null;
    }

    public OBlockItem(Block block, int stackSize, CreativeModeTab tab) {
        super(block, new Properties()
                .tab(tab)
                .stacksTo(stackSize)
        );
        this.followItem = null;
    }

    public OBlockItem(Block block, boolean inflammable, CreativeModeTab tab) {
        super(block, new Properties()
                .tab(tab)
                .fireResistant()
        );
        this.followItem = null;
    }

    public OBlockItem(Block block, ItemLike follow) {
        super(block, new Properties()
                .tab(Objects.requireNonNull(follow.asItem().getItemCategory()))
        );
        this.followItem = follow.asItem();
    }

    public OBlockItem(Block block, Supplier<? extends ItemLike> follow) {
        this(block, follow.get());
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
        if (this.followItem == null) super.fillItemCategory(tab, items);
        if (this.allowdedIn(tab) && this.followItem != null)
            OItem.insert(new ItemStack(this), false, items, stack -> stack.getItem() == followItem);
    }
}
