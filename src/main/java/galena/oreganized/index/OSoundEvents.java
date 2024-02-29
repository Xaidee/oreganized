package galena.oreganized.index;

import galena.oreganized.Oreganized;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class OSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Oreganized.MOD_ID);

    public static final RegistryObject<SoundEvent> MUSIC_DISC_STRUCTURE = register("music.disc.structure");

    public static final RegistryObject<SoundEvent> SHRAPNEL_BOMB_PRIMED = register("entity.shrapnel_bomb.primed");

    public static final RegistryObject<SoundEvent> BOLT_HIT = register("entity.bolt_hit");
    public static final RegistryObject<SoundEvent> BOLT_HIT_ARMOR = register("entity.bolt_hit_armor");

    private static RegistryObject<SoundEvent> register(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(Oreganized.modLoc(name)));
    }
}
