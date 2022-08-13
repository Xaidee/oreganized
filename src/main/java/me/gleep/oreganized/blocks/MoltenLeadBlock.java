package me.gleep.oreganized.blocks;

import me.gleep.oreganized.registry.OBlocks;
import me.gleep.oreganized.registry.OItems;
import me.gleep.oreganized.registry.OParticleTypes;
import me.gleep.oreganized.registry.OTags;
import me.gleep.oreganized.util.ModDamageSource;
import me.gleep.oreganized.util.RegistryHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

public class MoltenLeadBlock extends Block implements BucketPickup {

    private static final BooleanProperty MOVING = BooleanProperty.create( "ismoving" );

    public MoltenLeadBlock(BlockBehaviour.Properties properties){
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition( StateDefinition.Builder <Block, BlockState> pBuilder ){
        pBuilder.add( MOVING );
    }

    @Override
    public boolean skipRendering(BlockState pState, BlockState pAdjacentBlockState, Direction pDirection){
        return pAdjacentBlockState.is(this);
    }

    @Override
    public BlockState updateShape( BlockState pState , Direction pDirection , BlockState neighbour , LevelAccessor pLevel , BlockPos pPos , BlockPos neighbourPos ){
        if(pLevel.isWaterAt( neighbourPos )){
            pLevel.levelEvent( 1501 , pPos , 0 );
            return OBlocks.LEAD_BLOCK.get().defaultBlockState();
        }
        if(pDirection == Direction.DOWN){
            pLevel.scheduleTick( pPos , this , 30 );
        }
        return super.updateShape( pState , pDirection , neighbour , pLevel , pPos , neighbourPos );
    }

    @Override
    public void neighborChanged( BlockState p_60509_ , Level level , BlockPos pos , Block p_60512_ , BlockPos neighbourPos , boolean p_60514_ ){
        if(level.getFluidState( neighbourPos ).is( FluidTags.WATER )){
            level.setBlockAndUpdate( pos , OBlocks.LEAD_BLOCK.get().defaultBlockState() );

            level.levelEvent( 1501 , pos , 0 );
        } //else if (level.getFluidState(neighbourPos).is(FluidTags.LAVA)) {}


        super.neighborChanged( p_60509_ , level , pos , p_60512_ , neighbourPos , p_60514_ );
    }

    /**
     * Get the {@code PathNodeType} for this block. Return {@code null} for vanilla behavior.
     *
     * @param state
     * @param world
     * @param pos
     * @param entity
     * @return the PathNodeType
     */
    @Nullable
    @Override
    public BlockPathTypes getAiPathNodeType( BlockState state , BlockGetter world , BlockPos pos , @Nullable Mob entity ){
        return BlockPathTypes.WALKABLE;
    }

    /**
     * Determines if this block is can be destroyed by the specified entities normal behavior.
     *
     * @param state  The current state
     * @param world  The current world
     * @param pos    Block position in world
     * @param entity
     * @return True to allow the ender dragon to destroy this block
     */
    @Override
    public boolean canEntityDestroy( BlockState state , BlockGetter world , BlockPos pos , Entity entity ){
        return false;
    }

    @Override
    public VoxelShape getCollisionShape( BlockState p_60572_ , BlockGetter p_60573_ , BlockPos p_60574_ , CollisionContext p_60575_ ){
        if(((EntityCollisionContext) p_60575_).getEntity() != null){
            if(!(((EntityCollisionContext) p_60575_).getEntity() instanceof LivingEntity)) return Shapes.empty();
            LivingEntity entity = (LivingEntity) ((EntityCollisionContext) p_60575_).getEntity();
            if(entity.getType().is(OTags.Entities.LIGHTER_THAN_LEAD)) return Shapes.block();
            for(ItemStack item : entity.getArmorSlots()){
                if(item.getItem().equals( Items.IRON_BOOTS )) return Shapes.block();
            }
        }
        return Shapes.empty();
    }

    @Override
    public VoxelShape getShape( BlockState state , BlockGetter world , BlockPos pos , CollisionContext ctx ){
        if (((EntityCollisionContext) ctx).getEntity() instanceof Player player) {
            return player.getMainHandItem().is(Items.BUCKET) || player.getMainHandItem().is(OItems.MOLTEN_LEAD_BUCKET.get()) ||
                    player.getOffhandItem().is(Items.BUCKET) || player.getOffhandItem().is(OItems.MOLTEN_LEAD_BUCKET.get())
                    ? Shapes.block() : Shapes.empty();
        }
        return Shapes.block();
    }

    /**
     * Determines if the player can harvest this block, obtaining it's drops when the block is destroyed.
     *
     * @param state
     * @param world  The current world
     * @param pos    The block's current position
     * @param player The player damaging the block
     * @return True to spawn the drops
     */
    @Override
    public boolean canHarvestBlock( BlockState state , BlockGetter world , BlockPos pos , Player player ){
        return false;
    }

    @Override
    public void entityInside( BlockState p_60495_ , Level p_60496_ , BlockPos p_60497_ , Entity p_60498_ ){
        if(p_60498_ instanceof LivingEntity){
            p_60498_.setSecondsOnFire( 10 );
            p_60498_.hurt( ModDamageSource.MOLTEN_LEAD , 0.0F );
            p_60498_.makeStuckInBlock( p_60495_ , new Vec3( (double) 0.7F , 1.0D , (double) 0.7F ) );
        }

        super.entityInside( p_60495_ , p_60496_ , p_60497_ , p_60498_ );
    }

    @Override
    public ItemStack pickupBlock( LevelAccessor p_152719_ , BlockPos p_152720_ , BlockState p_152721_ ){
        p_152719_.setBlock( p_152720_ , Blocks.AIR.defaultBlockState() , 11 );
        if(!p_152719_.isClientSide()){
            p_152719_.levelEvent( 2001 , p_152720_ , Block.getId( p_152721_ ) );
        }

        return new ItemStack( OItems.MOLTEN_LEAD_BUCKET.get() , 1 );
    }

    @Override
    public Optional <SoundEvent> getPickupSound(){
        return Optional.of( SoundEvents.BUCKET_FILL_LAVA );
    }

    @Override
    public void onPlace( BlockState pState , Level pLevel , BlockPos pPos , BlockState pOldState , boolean pIsMoving ){
        if(pIsMoving){
            if(!pOldState.getFluidState().is( FluidTags.WATER )){
                scheduleFallingTick( pLevel , pPos , 30 );
                pLevel.setBlock( pPos , pState.setValue( MOVING , true ) , 3 );
            }else{
                pLevel.levelEvent( 1501 , pPos , 0 );
                pLevel.setBlock( pPos , OBlocks.LEAD_BLOCK.get().defaultBlockState() , 3 );
            }
        }else{
            if(!pOldState.getFluidState().is( FluidTags.WATER )){
                pLevel.setBlock( pPos , pState.setValue( MOVING , false ) , 3 );
                pLevel.scheduleTick( pPos , this , 300 );
            }else{
                pLevel.levelEvent( 1501 , pPos , 0 );
                pLevel.setBlock( pPos , OBlocks.LEAD_BLOCK.get().defaultBlockState() , 3 );
            }
        }
    }

    @Override
    public void tick( BlockState pState , ServerLevel pLevel , BlockPos pPos , Random pRandom ){
        if (pLevel.getBlockState(pPos.below()).getBlock() == Blocks.AIR || pLevel.getBlockState(pPos.below()).is(BlockTags.REPLACEABLE_PLANTS)
                || pLevel.getBlockState(pPos.below()).getFluidState().is(FluidTags.WATER)
                || pLevel.getBlockState(pPos.below()).is(BlockTags.SMALL_FLOWERS)
                || pLevel.getBlockState(pPos.below()).is(BlockTags.TALL_FLOWERS)){
            pLevel.setBlock( pPos , Blocks.AIR.defaultBlockState() , 67 );
            pLevel.setBlock( pPos.below() , OBlocks.MOLTEN_LEAD_BLOCK.get().defaultBlockState() , 67 );
        }
    }


    public void animateTick( BlockState pState , Level pLevel , BlockPos pPos , Random pRand ){
        if(!pState.getValue( MOVING )){
            for(int i = 0; i < pRand.nextInt( 7 ) + 1; ++i){
                this.trySpawnDripParticles( pLevel , pPos , pState );
            }
        }
    }

    private void trySpawnDripParticles( Level pLevel , BlockPos pPos , BlockState pState ){
        if(pState.getFluidState().isEmpty() && !(pLevel.random.nextFloat() < 0.5F)){
            VoxelShape voxelshape = OBlocks.LEAD_BLOCK.get().defaultBlockState().getCollisionShape( pLevel , pPos );
            double d0 = voxelshape.max( Direction.Axis.Y );
            if(d0 >= 1.0D){
                double d1 = voxelshape.min( Direction.Axis.Y );
                if(d1 > 0.0D){
                    this.spawnParticle( pLevel , pPos , voxelshape , (double) pPos.getY() + d1 - 0.05D );
                }else{
                    BlockPos blockpos = pPos.below();
                    BlockState blockstate = pLevel.getBlockState( blockpos );
                    VoxelShape voxelshape1 = blockstate.getCollisionShape( pLevel , blockpos );
                    double d2 = voxelshape1.max( Direction.Axis.Y );
                    if((d2 < 1.0D || !blockstate.isCollisionShapeFullBlock( pLevel , blockpos )) && blockstate.getFluidState().isEmpty()){
                        this.spawnParticle( pLevel , pPos , voxelshape , (double) pPos.getY() - 0.05D );
                    }
                }
            }

        }
    }

    private void spawnParticle( Level pLevel , BlockPos pPos , VoxelShape pShape , double pY ){
        this.spawnFluidParticle( pLevel , (double) pPos.getX() + pShape.min( Direction.Axis.X ) , (double) pPos.getX() + pShape.max( Direction.Axis.X ) , (double) pPos.getZ() + pShape.min( Direction.Axis.Z ) , (double) pPos.getZ() + pShape.max( Direction.Axis.Z ) , pY );
    }

    private void spawnFluidParticle( Level pParticleData , double pX1 , double pX2 , double pZ1 , double pZ2 , double pY ){
        pParticleData.addParticle( OParticleTypes.DRIPPING_LEAD.get() , Mth.lerp( pParticleData.random.nextDouble() , pX1 , pX2 ) , pY , Mth.lerp( pParticleData.random.nextDouble() , pZ1 , pZ2 ) , 0.0D , 0.0D , 0.0D );
    }

    private boolean scheduleFallingTick( LevelAccessor pLevel , BlockPos pPos , int pDelay ){
        if(pLevel.getBlockState( pPos.below() ).getBlock() == Blocks.AIR || pLevel.getBlockState(pPos.below()).is(BlockTags.REPLACEABLE_PLANTS)
                || pLevel.getBlockState( pPos.below() ).getFluidState().is(FluidTags.WATER)
                || pLevel.getBlockState( pPos.below() ).is(BlockTags.SMALL_FLOWERS)
                || pLevel.getBlockState(pPos.below()).is(BlockTags.TALL_FLOWERS)){
            pLevel.scheduleTick( pPos , this , pDelay );
            return true;
        }
        return false;
    }


}
