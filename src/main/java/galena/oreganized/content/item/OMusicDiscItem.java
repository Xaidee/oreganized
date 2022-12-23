package galena.oreganized.content.item;

import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.*;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class OMusicDiscItem extends RecordItem {

    private final Item followItem;

    public OMusicDiscItem(int comparatorValue, Supplier<SoundEvent> soundSupplier, Properties itemProperties, int lengthInTicks, Item followItem) {
        super(comparatorValue, soundSupplier, itemProperties.rarity(Rarity.RARE));
        this.followItem = followItem;
    }

    public OMusicDiscItem(int comparatorValue, Supplier<SoundEvent> soundSupplier, Properties itemProperties, int lengthInTicks) {
        this(comparatorValue, soundSupplier, itemProperties, lengthInTicks, Items.MUSIC_DISC_OTHERSIDE);
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
        if (this.allowdedIn(tab))
            OItem.insert(new ItemStack(this), false, items, stack -> stack.getItem() == followItem);
    }
}
