package me.gleep.oreganized.events;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import me.gleep.oreganized.armors.STABase;
import me.gleep.oreganized.blocks.EngravedBlock;
import me.gleep.oreganized.blocks.EngravedWeatheringCopperBlock;
import me.gleep.oreganized.blocks.ModCauldron;
import me.gleep.oreganized.capabilities.engravedblockscap.CapabilityEngravedBlocks;
import me.gleep.oreganized.capabilities.engravedblockscap.EngravedBlocks;
import me.gleep.oreganized.capabilities.engravedblockscap.IEngravedBlocks;
import me.gleep.oreganized.capabilities.stunning.CapabilityStunning;
import me.gleep.oreganized.capabilities.stunning.IStunning;
import me.gleep.oreganized.items.BushHammer;
import me.gleep.oreganized.registry.OBlocks;
import me.gleep.oreganized.registry.OEffects;
import me.gleep.oreganized.registry.OItems;
import me.gleep.oreganized.registry.OTags;
import me.gleep.oreganized.tools.STSBase;
import me.gleep.oreganized.util.messages.UpdateClientEngravedBlocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.PistonEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.Map;
import java.util.UUID;

import static me.gleep.oreganized.Oreganized.MOD_ID;
import static me.gleep.oreganized.util.RegistryHandler.*;
import static me.gleep.oreganized.util.SimpleNetwork.CHANNEL;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {
    private static final UUID ELECTRUM_BOOST_MODIFIER = UUID.fromString("ea10ee7a-da1a-4153-a4ae-53b5d2a09dbb");

    /**
     * Event to handle Stunning effect properly
     */
    @SubscribeEvent
    public static void tickEntityStunning( final LivingEvent.LivingUpdateEvent event ){
        LivingEntity entity = event.getEntityLiving();
        Level level = entity.getLevel();
        IStunning stunningCap = entity.getCapability(CapabilityStunning.STUNNING_CAPABILITY, null ).orElse(null);
        if (stunningCap != null) {
            if(stunningCap.getRemainingStunTime() > 0) {
                stunningCap.setRemainingStunTime(stunningCap.getRemainingStunTime() - 1);
                BlockPos previousPos = stunningCap.getPreviousPos();
                entity.setPos(previousPos.getX(), previousPos.getY(), previousPos.getZ());
            }
        }
    }

    /**
     * Event to handle Silver Tinted Swords break
     */
    @SubscribeEvent
    public static void onToolBreakEvent( final PlayerDestroyItemEvent event ){
        ItemStack stack = event.getOriginal();
        Player pl = event.getPlayer();
        ItemStack item = ItemStack.EMPTY;

        if(stack.getItem() instanceof STSBase){
            int count = (int) Math.round( ((double) stack.getOrCreateTag().getInt( "TintedDamage" ) / (double) STSBase.MAX_TINT_DURABILITY) * 9D );
            item = new ItemStack( OItems.SILVER_NUGGET.get() , count );
        }else if(stack.getItem() instanceof STABase){
            int count = (int) Math.round( ((double) stack.getOrCreateTag().getInt( "TintedDamage" ) / (double) STABase.MAX_TINT_DURABILITY) * 9D );
            item = new ItemStack( OItems.SILVER_NUGGET.get() , count );
        }

        pl.drop( item , true );
    }

    public static ImmutableBiMap <Block, EngravedWeatheringCopperBlock> ENGRAVED_WAXED_COPPER_BLOCKS;
    public static ImmutableBiMap <Block, Block> WAXED_BLOCKS;
    public static ImmutableList <Block> ENGRAVED_COPPER_BLOCKS;

    @SubscribeEvent
    public static void handleWaxingAndScraping( final PlayerInteractEvent.RightClickBlock event ){
        BlockState blockState = event.getWorld().getBlockState( event.getPos() );
        if(event.getItemStack().getItem() instanceof AxeItem){
            if(ENGRAVED_WAXED_COPPER_BLOCKS.get( blockState.getBlock() ) != null || WAXED_BLOCKS.get( blockState.getBlock() ) != null){
                if(!event.getWorld().isClientSide() && ENGRAVED_WAXED_COPPER_BLOCKS.get( blockState.getBlock() ) != null)
                    event.getWorld().setBlock( event.getPos() , ENGRAVED_WAXED_COPPER_BLOCKS.get( blockState.getBlock() ).defaultBlockState()
                            .setValue( EngravedBlock.ISZAXISDOWN , blockState.getValue( EngravedBlock.ISZAXISDOWN ) )
                            .setValue( EngravedBlock.ISZAXISUP , blockState.getValue( EngravedBlock.ISZAXISUP ) ) , 11 );
                if(WAXED_BLOCKS.get( blockState.getBlock() ) != null) {
                    if(!event.getWorld().isClientSide()) event.getWorld().setBlock(event.getPos(), WAXED_BLOCKS.get(blockState.getBlock()).defaultBlockState(), 11);
                    event.getPlayer().swing(event.getHand());
                }
                event.getWorld().playSound( event.getPlayer() , event.getPos() , SoundEvents.AXE_WAX_OFF , SoundSource.BLOCKS , 1.0F , 1.0F );
                event.getWorld().levelEvent( event.getPlayer() , 3004 , event.getPos() , 0 );
            }else{
                if(blockState.getBlock() instanceof EngravedWeatheringCopperBlock copperBlock){
                    if(EngravedWeatheringCopperBlock.PREVIOUS_BY_BLOCK.get().get( copperBlock ) != null){
                        if(!event.getWorld().isClientSide())
                            event.getWorld().setBlock( event.getPos() , EngravedWeatheringCopperBlock.PREVIOUS_BY_BLOCK.get().get( copperBlock ).defaultBlockState()
                                    .setValue( EngravedBlock.ISZAXISDOWN , blockState.getValue( EngravedBlock.ISZAXISDOWN ) )
                                    .setValue( EngravedBlock.ISZAXISUP , blockState.getValue( EngravedBlock.ISZAXISUP ) ) , 11 );
                        event.getWorld().playSound( event.getPlayer() , event.getPos() , SoundEvents.AXE_SCRAPE , SoundSource.BLOCKS , 1.0F , 1.0F );
                        event.getWorld().levelEvent( event.getPlayer() , 3005 , event.getPos() , 0 );
                    }
                }
            }
        }else if(event.getItemStack().getItem() == Items.HONEYCOMB){
            if(ENGRAVED_COPPER_BLOCKS.contains( blockState.getBlock() ) || WAXED_BLOCKS.inverse().get( blockState.getBlock() ) != null){
                if(event.getPlayer() instanceof ServerPlayer player){
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger( player , event.getPos() , event.getItemStack() );
                }
                event.getPlayer().swing(event.getHand());
                event.getItemStack().shrink( 1 );
                if(!event.getWorld().isClientSide() && ENGRAVED_COPPER_BLOCKS.contains( blockState.getBlock() )) {
                    EngravedWeatheringCopperBlock copperBlock = (EngravedWeatheringCopperBlock) blockState.getBlock();
                    event.getWorld().setBlock(event.getPos(), copperBlock.getWaxedBlock().defaultBlockState()
                            .setValue(EngravedBlock.ISZAXISDOWN, blockState.getValue(EngravedBlock.ISZAXISDOWN))
                            .setValue(EngravedBlock.ISZAXISUP, blockState.getValue(EngravedBlock.ISZAXISUP)), 11);
                }
                if(!event.getWorld().isClientSide() && WAXED_BLOCKS.inverse().get( blockState.getBlock() ) != null)
                    event.getWorld().setBlock( event.getPos() , WAXED_BLOCKS.inverse().get( blockState.getBlock() ).defaultBlockState(), 11 );
                event.getWorld().levelEvent( event.getPlayer() , 3003 , event.getPos() , 0 );
            }
        }
    }

    /**
     * Event to handle Cauldron replacement
     */
    @SubscribeEvent
    public static void onPlayerRightClick( final PlayerInteractEvent.RightClickBlock event ){
        ItemStack item = event.getItemStack();
        BlockPos pos = event.getPos();
        Level level = event.getWorld();

        if(level.getBlockState( pos ).equals( Blocks.CAULDRON.defaultBlockState() )){
            if(item.getItem().equals( OBlocks.LEAD_BLOCK.get().asItem() )){
                if(!level.isClientSide()){
                    level.removeBlock( pos , false );
                    level.setBlockAndUpdate( pos , OBlocks.LEAD_CAULDRON.get().defaultBlockState() );
                    if(!event.getPlayer().isCreative()) item.shrink( 1 );
                    level.playSound( (Player) null , pos , SoundEvents.STONE_PLACE , SoundSource.BLOCKS , 1.0F , 1.0F );
                    event.getPlayer().awardStat( Stats.USE_CAULDRON );
                }
                if(event.isCancelable()){
                    event.setCancellationResult( InteractionResult.sidedSuccess( level.isClientSide() ) );
                    event.setCanceled( true );
                }
            }else if(item.getItem().equals( OItems.MOLTEN_LEAD_BUCKET.get() )){
                if(!level.isClientSide()){
                    level.removeBlock( pos , false );
                    level.setBlockAndUpdate( pos , OBlocks.LEAD_CAULDRON.get().defaultBlockState().setValue( ModCauldron.LEVEL , 3 ) );
                    if(!event.getPlayer().isCreative())
                        event.getPlayer().setItemInHand( InteractionHand.MAIN_HAND , new ItemStack( Items.BUCKET , 1 ) );
                    level.playSound( (Player) null , pos , SoundEvents.BUCKET_EMPTY_LAVA , SoundSource.BLOCKS , 1.0F , 1.0F );
                    event.getPlayer().awardStat( Stats.USE_CAULDRON );
                }

                if(event.isCancelable()){
                    event.setCancellationResult( InteractionResult.sidedSuccess( level.isClientSide() ) );
                    event.setCanceled( true );
                }
            }
        }

        IEngravedBlocks cap = level.getCapability(CapabilityEngravedBlocks.ENGRAVED_BLOCKS_CAPABILITY).orElse(null);
        if (cap.isEngraved(pos) && item.getItem() instanceof AxeItem) {
            event.getPlayer().swing(event.getHand());
            level.playSound(event.getPlayer(), pos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
            cap.removeEngravedBlock(pos);
        }
    }

    /**
     * Event to handle entities in mod fluid
     */
    /*@SubscribeEvent
    public static void onEntityUpdate(final LivingEvent.LivingUpdateEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = event.getEntityLiving();
            Tag<Fluid> tag = FluidTags.getAllTags().getTag(new ResourceLocation(Oreganized.MOD_ID + ":lead"));

            if (entity.updateFluidHeightAndDoFluidPushing(tag, 0.009D)) {
                entity.setSecondsOnFire(10);
                entity.hurt(ModDamageSource.MOLTEN_LEAD, 3.0F);

                Vec3 travelVector = new Vec3((double)entity.xxa, (double)entity.yya, (double)entity.zza);
                double d0 = 0.08D;
                AttributeInstance gravity = entity.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());
                boolean flag = entity.getDeltaMovement().y <= 0.0D;
                d0 = gravity.getValue();

                double d7 = entity.getY();
                entity.moveRelative(0.01F, travelVector);
                entity.move(MoverType.SELF, entity.getDeltaMovement());
                if (entity.getFluidHeight(tag) <= entity.getFluidJumpThreshold()) {
                    entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.2D, (double)0.4F, 0.2D));
                    Vec3 vector3d3 = entity.getFluidFallingAdjustedMovement(d0, flag, entity.getDeltaMovement());
                    entity.setDeltaMovement(vector3d3);
                } else {
                    entity.setDeltaMovement(entity.getDeltaMovement().scale(0.2D));
                }

                if (!entity.isNoGravity()) {
                    entity.setDeltaMovement(entity.getDeltaMovement().add(0.0D, -d0 / 4.0D, 0.0D));
                }

                Vec3 vector3d4 = entity.getDeltaMovement();
                if (entity.horizontalCollision && entity.isFree(vector3d4.x, vector3d4.y + (double)0.2F - entity.getY() + d7, vector3d4.z)) {
                    entity.setDeltaMovement(vector3d4.x, (double)0.3F, vector3d4.z);
                }
            }
        }
    }*/

    /**
     * Event to handle block cracking and damaging BushHammer item
     */
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onBlockDestroyed( BlockEvent.BreakEvent event ){
        LevelAccessor world = event.getWorld();
        BlockPos pos = event.getPos();
        BlockState state = world.getBlockState( pos );
        ItemStack currentitem = event.getPlayer().getItemInHand( event.getPlayer().getUsedItemHand() );

        if (currentitem.getItem().equals(OItems.BUSH_HAMMER.get())) {
            for (Block b : BushHammer.EFFECTIVE_ON.keySet()) {
                if (state.getBlock().equals( b ) && !event.getPlayer().isCreative()){
                    world.setBlock( pos , BushHammer.EFFECTIVE_ON.get( b ).defaultBlockState() , 2 );
                    currentitem.hurtAndBreak( 1 , event.getPlayer() , ( player ) -> {
                        player.broadcastBreakEvent( event.getPlayer().getUsedItemHand() );
                    } );
                    event.setCanceled( true );
                }
            }
        }

    }

    @SubscribeEvent
    public static void applyLeadEffect( LivingEntityUseItemEvent.Finish event ) {
        if (event.getItem().getItem().isEdible()) {
            if (event.getEntityLiving() instanceof Player player) {
                for (int i = 0; i < 9; i++){
                    if (player.getInventory().items.get( i ).is(OTags.Items.INGOTS_LEAD) || player.getInventory().items.get( i ).is(OTags.Items.LEAD_SOURCE)) {
                        player.addEffect( new MobEffectInstance( OEffects.STUNNING.get(), 40 * 20 ) );
                        player.addEffect( new MobEffectInstance( MobEffects.POISON, 200 ) );
                        return;
                    }
                }
                if (player.getInventory().offhand.get( 0 ).is(OTags.Items.INGOTS_LEAD) || player.getInventory().items.get( 0 ).is(OTags.Items.LEAD_SOURCE)) {
                    player.addEffect( new MobEffectInstance( OEffects.STUNNING.get(), 40 * 20 ) );
                    player.addEffect( new MobEffectInstance( MobEffects.POISON, 200 ) );
                }
            }
        }
    }

    /**
     * Event to change fluid fog density for rendering
     */
    /*@OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void getFogDensity (EntityViewRenderEvent.FogDensity event) {
        Camera info = event.getCamera();
        BlockState blockState = info.getBlockAtCamera();

        if (blockState.getBlock().equals( OBlocks.MOLTEN_LEAD_BLOCK.get() )) {
            RenderSystem.enableCull();
            event.setDensity( 1.4F );
            event.setCanceled( true );
        }
    }*/

    /**
     * Event to change the fluid fog color for rendering
     */

    @SubscribeEvent
    public static void onEntityDeath( LivingDeathEvent event ){
        if(event.getSource().getDirectEntity() instanceof Player){
            Player player = (Player) event.getSource().getDirectEntity();
            LivingEntity living = event.getEntityLiving();

            if(living.isInvertedHealAndHarm()){
                CompoundTag nbt = new CompoundTag();
                nbt.putLong( "t" , player.level.getGameTime() );
                nbt.putBoolean( "Shine" , true );

                for(ItemStack stack : player.getInventory().items){
                    if(stack.getItem().equals( OItems.SILVER_INGOT.get() )) stack.setTag( nbt );
                }

                if(player.getInventory().offhand.get( 0 ).getItem().equals( OItems.SILVER_INGOT.get() )){
                    player.getInventory().offhand.get( 0 ).setTag( nbt );
                }
            }
        }
    }


    // ENGRAVING HANDLING STARTS HERE

    public static boolean recentlyActivatedPiston = false;
    public static byte recentlyActivatedPistonDelay = 0;

    @SubscribeEvent
    public static void updatePistonDelay( TickEvent.ServerTickEvent event ){
        if(recentlyActivatedPiston && recentlyActivatedPistonDelay == 0){
            recentlyActivatedPistonDelay = 20;
        }
        if(recentlyActivatedPistonDelay > 0){
            recentlyActivatedPistonDelay--;
        }
        if(recentlyActivatedPistonDelay == 0){
            recentlyActivatedPiston = false;
        }
    }

    @SubscribeEvent
    public static void updateEngravedBlocks( TickEvent.WorldTickEvent event ){
        Level level = event.world;
        if(!level.isClientSide()){
            IEngravedBlocks capability = level.getCapability( CapabilityEngravedBlocks.ENGRAVED_BLOCKS_CAPABILITY )
                    .orElse( null );
            if(!recentlyActivatedPiston){
                for(BlockPos pos : capability.getEngravedBlocks()){
                    byte count = 0;
                    for(Map.Entry <EngravedBlocks.Face, String> entry : capability.getEngravedFaces().get( pos ).entrySet()){
                        String text = entry.getValue();
                        if(text.equals( "" ) || text.equals( "\n\n\n\n\n\n\n" )) count++;
                    }
                    if(!level.getBlockState( pos ).is(OTags.Blocks.ENGRAVABLE) || count == 12){
                        capability.removeEngravedBlock( pos );
                        CHANNEL.send( PacketDistributor.ALL.noArg() , new UpdateClientEngravedBlocks( capability.getEngravedBlocks() ,
                                capability.getEngravedFaces() , capability.getEngravedColors() ) );
                        break;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEngravedBlockMoved( PistonEvent.Pre event ){
        if(event.getWorld() instanceof Level level){
            if(!level.isClientSide()){
                PistonStructureResolver pistonHelper = event.getStructureHelper();
                pistonHelper.resolve();
                Direction pushDirection = pistonHelper.getPushDirection();
                for(BlockPos oldPos : pistonHelper.getToPush()){
                    if(!(level.getBlockState( event.getPos() ).getBlock() == Blocks.PISTON && !event.getPistonMoveType().isExtend)){
                        BlockPos newPos = oldPos.relative( pushDirection );
                        IEngravedBlocks cap = level.getCapability( CapabilityEngravedBlocks.ENGRAVED_BLOCKS_CAPABILITY ).orElse( null );
                        if(cap.isEngraved( oldPos )){
                            cap.getEngravedBlocks().add( newPos );
                            cap.getEngravedFaces().put( newPos , cap.getEngravedFaces().get( oldPos ) );
                            cap.getEngravedColors().put( newPos , cap.getEngravedColors().get( oldPos ) );
                            cap.getEngravedBlocks().remove( oldPos );
                            cap.getEngravedFaces().remove( oldPos );
                            cap.getEngravedColors().remove( oldPos );
                            CHANNEL.send( PacketDistributor.ALL.noArg() ,
                                    new UpdateClientEngravedBlocks( cap.getEngravedBlocks() , cap.getEngravedFaces() , cap.getEngravedColors() ) );
                        }
                    }
                }
                if(!pistonHelper.getToPush().isEmpty()){
                    recentlyActivatedPiston = true;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin( PlayerEvent.PlayerLoggedInEvent event ){
        ServerPlayer player = (ServerPlayer) event.getPlayer();
        ServerLevel level = player.getLevel();
        IEngravedBlocks cap = level.getCapability( CapabilityEngravedBlocks.ENGRAVED_BLOCKS_CAPABILITY ).orElse( null );
        CHANNEL.send( PacketDistributor.PLAYER.with( () -> player ) ,
                new UpdateClientEngravedBlocks( cap.getEngravedBlocks() , cap.getEngravedFaces() , cap.getEngravedColors() ) );
    }

    @SubscribeEvent
    public static void onPlayerLogin( PlayerEvent.PlayerChangedDimensionEvent event ){
        ServerPlayer player = (ServerPlayer) event.getPlayer();
        ServerLevel level = player.getLevel();
        IEngravedBlocks cap = level.getCapability( CapabilityEngravedBlocks.ENGRAVED_BLOCKS_CAPABILITY ).orElse( null );
        CHANNEL.send( PacketDistributor.PLAYER.with( () -> player ) ,
                new UpdateClientEngravedBlocks( cap.getEngravedBlocks() , cap.getEngravedFaces() , cap.getEngravedColors() ) );
    }
}
