package sirmrcc.alchemy.item.custom;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import sirmrcc.alchemy.block.ModBlocks;
import sirmrcc.alchemy.item.ModItems;

import java.util.function.Predicate;

import static net.minecraft.block.Block.getRawIdFromState;

public class BridgerStoneTestItem extends Item
{
    public BridgerStoneTestItem(Settings settings) { super(settings); }
    public static final Predicate<ItemStack> EARTH_MATERIALS = (stack) -> stack.isIn(ItemTags.DIRT);

    //values
    boolean isActive = false;
    EquipmentSlot handSlot;
    final int MAX_DELAY = 6;
    int delay = MAX_DELAY;

    //overriden functions
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
    {
        if (!world.isClient)
        {
            if (!user.isInCreativeMode() && !user.getInventory().contains(EARTH_MATERIALS))
            {
                return TypedActionResult.fail(user.getStackInHand(hand));
            }
            else
            {
                OnItemUse(user, hand, world);
                inventoryTick(user.getStackInHand(hand), world, user, user.getInventory().selectedSlot, true);
                return TypedActionResult.success(user.getStackInHand(hand));
            }

        }
        return TypedActionResult.fail(user.getStackInHand(hand));
    }

    @Override
    public boolean hasGlint(ItemStack item)
    {
        return isActive;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected)
    {
        PlayerEntity user = (PlayerEntity) entity;
        if(!user.isInCreativeMode())
        {
            ItemStack material = getMaterials(user);
            if (!material.isEmpty() && isActive)
            {
                if (user.isSneaking()) TransmuteDown(stack, material, user, world);
                else TransmuteEarth(stack, material, user, world);
            }
            else
            {
                boolean oldStatus = isActive;
                ChangeActivationStatus(false);
                PlayStatusChangeSound(world, user, oldStatus);
            }
        }
        else
        {
            if (isActive)
            {
                if (user.isSneaking()) TransmuteDown(user, world);
                else TransmuteEarth(user, world);
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }
    //functions
    public boolean IsSolidGround(PlayerEntity user)
    {
        BlockPos underFeet = user.getBlockPos().down();
        BlockState blockStateUnderFeet = user.getWorld().getBlockState(underFeet);
        return !blockStateUnderFeet.isReplaceable();
    }

    public boolean IsSolidGround(PlayerEntity user, int distance)
    {
        BlockPos underFeet = user.getBlockPos().down(distance);
        BlockState blockStateUnderFeet = user.getWorld().getBlockState(underFeet);
        return !blockStateUnderFeet.isReplaceable();
    }

    public void PlaceBlockBellowFeet(PlayerEntity user, World world)
    {
        BlockPos underFeet = user.getBlockPos().down();
        world.setBlockState(underFeet, ModBlocks.VOLATILE_TRANSMUTED_EARTH.getDefaultState());
    }

    public void PlaceBlockBellowFeet(PlayerEntity user, World world, int distance)
    {
        BlockPos underFeet = user.getBlockPos().down(distance);
        world.setBlockState(underFeet, ModBlocks.VOLATILE_TRANSMUTED_EARTH.getDefaultState());
    }

    public void PlayPlaceSound(World world, PlayerEntity user)
    {
        world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_DRIPSTONE_BLOCK_PLACE,
                SoundCategory.BLOCKS, 1.0f, 1.0f);
    }

    public void BreakBridgeBlock(World world, PlayerEntity user)
    {
        BlockPos underFeet = user.getBlockPos().down();
        BlockState blockStateUnderFeet = user.getWorld().getBlockState(underFeet);
        if (blockStateUnderFeet.isOf(ModBlocks.VOLATILE_TRANSMUTED_EARTH))
        {
            world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, underFeet,getRawIdFromState(blockStateUnderFeet));
            world.removeBlock(underFeet, false);
        }
    }

    public void ChangeActivationStatus()
    {
        isActive = !isActive;
    }

    public void ChangeActivationStatus(boolean bool)
    {
        isActive = bool;
    }

    public void PlayStatusChangeSound(World world, PlayerEntity user, boolean previousStatus)
    {
        if (previousStatus != isActive)
        {
            if (isActive)//enabled sound
            {
                world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE,
                        SoundCategory.PLAYERS, 2.0f, 1.0f);
                world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_AMETHYST_CLUSTER_HIT,
                        SoundCategory.PLAYERS, 2.0f, 2.0f);
            }
            else//disabled sound
            {
                world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE,
                        SoundCategory.PLAYERS, 2.0f, 1.0f);
                world.playSound(null, user.getBlockPos(),SoundEvents.BLOCK_AMETHYST_CLUSTER_PLACE,
                        SoundCategory.PLAYERS,2.0f, 0.0f);
            }
        }
    }

    public void OnItemUse(PlayerEntity user, Hand hand, World world)
    {
        boolean oldStatus = isActive;
        ItemStack stack = user.getStackInHand(hand);
        handSlot = LivingEntity.getSlotForHand(hand);
        ChangeActivationStatus();
        hasGlint(stack);
        PlayStatusChangeSound(world, user, oldStatus);
    }

    public void TransmuteEarth(PlayerEntity user, World world)
    {
        if (!IsSolidGround(user))
        {
            PlaceBlockBellowFeet(user, world);
            PlayPlaceSound(world, user);
        }
    }


    public void TransmuteEarth(ItemStack stack, ItemStack material, PlayerEntity user, World world)
    {
        if (!IsSolidGround(user) && !world.isClient)
        {
            ConsumeMaterial(material, user);
            PlaceBlockBellowFeet(user, world);
            PlayPlaceSound(world, user);
            TakeDamage(stack, user, handSlot);
        }
    }

    public void TransmuteDown(PlayerEntity user, World world)
    {
        if (delay == 0)
        {
            if (!IsSolidGround(user, 2))
            {
                PlaceBlockBellowFeet(user, world, 2);
                PlayPlaceSound(world, user);
            }
            BreakBridgeBlock(world, user);
            delay = MAX_DELAY;
        }
        else --delay;

    }

    public void TransmuteDown(ItemStack stack, ItemStack material, PlayerEntity user, World world)
    {
        if (!world.isClient)
        {
            if (delay == 0 && !user.isFallFlying())
            {
                if (!IsSolidGround(user, 2))
                {
                    ConsumeMaterial(material, user);
                    PlaceBlockBellowFeet(user, world, 2);
                    PlayPlaceSound(world, user);
                    TakeDamage(stack, user, handSlot);
                }
                BreakBridgeBlock(world, user);
                delay = MAX_DELAY;
            }
            else --delay;
        }

    }

    public ItemStack getMaterials(PlayerEntity user)
    {
        if(user.getInventory().contains(EARTH_MATERIALS))
        {
            for (int i = 0; i < user.getInventory().size(); ++i)
            {
                ItemStack stack = user.getInventory().getStack(i);
                if (EARTH_MATERIALS.test(stack))
                {
                    return stack;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public void ConsumeMaterial(ItemStack stack, PlayerEntity user)
    {
        int i = user.getInventory().indexOf(stack);
        user.getInventory().removeStack(i, 1);
    }

    public void TakeDamage(ItemStack stack, PlayerEntity user, EquipmentSlot slot)
    {
        stack.damage(1, user, slot);
        if (stack.getDamage() == 0)
        {
            ItemStack brokenStone = new ItemStack(ModItems.PHILOSOPHERS_STONE_POLISHED_BROKEN);
            user.giveItemStack(brokenStone);
        }
    }
}
