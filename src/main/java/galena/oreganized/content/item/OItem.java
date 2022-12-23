package galena.oreganized.content.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public class OItem extends Item {

    @Nullable
    private final Item followItem;

    public OItem(Properties properties) {
        super(properties);
        this.followItem = null;
    }

    public OItem(Properties properties, ItemLike follow) {
        super(properties.tab(Objects.requireNonNull(follow.asItem().getItemCategory())));
        this.followItem = follow.asItem();
    }

    public OItem() {
        super(new Properties());
        this.followItem = null;
    }

    public OItem(CreativeModeTab tab) {
        super(new Properties()
                .tab(tab)
        );
        this.followItem = null;
    }

    public OItem(int stackSize, CreativeModeTab tab) {
        super(new Properties()
                .tab(tab)
                .stacksTo(stackSize)
        );
        this.followItem = null;
    }

    public OItem(boolean inflammable, CreativeModeTab tab) {
        super(new Properties()
                .tab(tab)
                .fireResistant()
        );
        this.followItem = null;
    }

    public OItem(ItemLike follow) {
        super(new Properties()
                .tab(Objects.requireNonNull(follow.asItem().getItemCategory()))
        );
        this.followItem = follow.asItem();
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
        if (this.followItem == null) super.fillItemCategory(tab, items);
        if (this.allowdedIn(tab) && this.followItem != null)
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
}
