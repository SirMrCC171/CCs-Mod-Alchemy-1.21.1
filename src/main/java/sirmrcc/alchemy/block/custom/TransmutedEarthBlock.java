package sirmrcc.alchemy.block.custom;


import net.minecraft.block.BlockState;
import net.minecraft.block.MushroomBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;


public class TransmutedEarthBlock extends MushroomBlock
{
    public TransmutedEarthBlock(Settings settings){super(settings);}

    @Override
    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify)
    {
        super.onBlockAdded(state, world, pos, oldState, notify);
        world.scheduleBlockTick(pos, this, 100);
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.scheduledTick(state, world, pos, random);
        destroyBlock(world, pos, state);
    }

    private void destroyBlock(World world, BlockPos pos, BlockState state)
    {
        world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, pos,getRawIdFromState(state));
        world.removeBlock(pos, false);
    }
}