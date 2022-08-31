package me.gleep.oreganized.data;

import me.gleep.oreganized.Oreganized;
import me.gleep.oreganized.registry.OSoundEvents;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

public class OSoundDefinitions extends SoundDefinitionsProvider {

    public OSoundDefinitions(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, Oreganized.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {
        this.add(OSoundEvents.MUSIC_DISC_PILLAGED, definition().with(
                sound(Oreganized.MOD_ID + ":music/disc/pillaged").stream()
        ));
        this.add(OSoundEvents.MUSIC_DISC_18, definition().with(
                sound(Oreganized.MOD_ID + ":music/disc/18").stream()
        ));
        this.add(OSoundEvents.MUSIC_DISC_SHULK, definition().with(
                sound(Oreganized.MOD_ID + ":music/disc/shulk").stream()
        ));
        /*this.add(OSoundEvents.MUSIC_DISC_STRUCTURE, definition().with(
                sound(Oreganized.MOD_ID + ":music/disc/structure").stream()
        ));*/
    }
}
