package sirmrcc.alchemy.item.custome;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sirmrcc.alchemy.CCsModAlchemy;

public class PhilosophersStoneTest extends Item
{
    public PhilosophersStoneTest(Settings settings)
    {
        super(settings);
    }

    boolean isActive = false;
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
    {
        if (!world.isClient)
        {
            //check for valid materials
            //  changes active status depending on material status
            if (checkForMaterials(user) != null)
            {
                changeActiveStatus();
                inventoryTick(user.getStackInHand(hand), world, user, user.getInventory().selectedSlot, true);
                hasGlint(user.getStackInHand(hand));
            }
        }
        user.playSound(SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, 2.0f, 1.0f);
        user.playSound(SoundEvents.BLOCK_MEDIUM_AMETHYST_BUD_BREAK, 2.0f, 2.0f);
        return TypedActionResult.success(user.getStackInHand(hand));
    }
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected)
    {
        super.inventoryTick(stack, world, entity, slot, selected);

        PlayerEntity player = (PlayerEntity) entity;
        //receive material item and get corresponding block
        ItemStack myMaterial = checkForMaterials(player);
        if (myMaterial == null)
        {
            deactivate();
            return;
        }
        BlockState material = getPhysicalMaterial(myMaterial);
        if (isNotSolidGround(player) && isActive && material != null)
        {
            BlockPos underFeet = (player.getBlockPos().down());
            world.setBlockState(underFeet, material);
            useMaterial(player);
        }
    }

    @Override
    public boolean hasGlint(ItemStack stack)
    {
        return isActive;
    }
    private void changeActiveStatus()
    {
        isActive = !isActive;
    }
    private void deactivate()
    {
        isActive = false;
    }
    private boolean isNotSolidGround(PlayerEntity player)
    {
        BlockPos underFeet = player.getBlockPos().down();
        BlockState blockStateUnderFeet = player.getWorld().getBlockState(underFeet);
        return blockStateUnderFeet.isReplaceable();
    }
    private void useMaterial(PlayerEntity player)
    {
        ItemStack myMaterial;
        myMaterial = checkForMaterials(player);
        boolean hasValidMaterial = false;
        if (myMaterial != null) hasValidMaterial = true;
        if (hasValidMaterial)
        {
            int index = player.getInventory().indexOf(myMaterial);
            player.getInventory().removeStack(index, 1);
        }
        else isActive = false;
    }
    private ItemStack checkForMaterials(PlayerEntity player)
    {
        //deepslate block variants
        if (player.getInventory().contains(Items.DEEPSLATE.getDefaultStack()))
        { return Items.DEEPSLATE.getDefaultStack(); }
        else if (player.getInventory().contains(Items.COBBLED_DEEPSLATE.getDefaultStack()))
        { return Items.COBBLED_DEEPSLATE.getDefaultStack(); }
        //stone block variants
        else if (player.getInventory().contains(Items.STONE.getDefaultStack()))
        { return Items.STONE.getDefaultStack(); }
        else if (player.getInventory().contains(Items.COBBLESTONE.getDefaultStack()))
        { return Items.COBBLESTONE.getDefaultStack(); }
        else if (player.getInventory().contains(Items.MOSSY_COBBLESTONE.getDefaultStack()))
        { return Items.MOSSY_COBBLESTONE.getDefaultStack(); }
        else if (player.getInventory().contains(Items.ANDESITE.getDefaultStack()))
        { return Items.ANDESITE.getDefaultStack(); }
        else if (player.getInventory().contains(Items.GRANITE.getDefaultStack()))
        { return Items.GRANITE.getDefaultStack(); }
        else if (player.getInventory().contains(Items.DIORITE.getDefaultStack()))
        { return Items.DIORITE.getDefaultStack(); }
        //Limestone block variants - wip
        //desert block variants
        else if (player.getInventory().contains(Items.SANDSTONE.getDefaultStack()))
        { return Items.SANDSTONE.getDefaultStack(); }
        else if (player.getInventory().contains(Items.RED_SANDSTONE.getDefaultStack()))
        { return Items.RED_SANDSTONE.getDefaultStack(); }
        //blackstone block variants
        else if (player.getInventory().contains(Items.BLACKSTONE.getDefaultStack()))
        { return Items.BLACKSTONE.getDefaultStack(); }
        //basalt block variants
        else if (player.getInventory().contains(Items.BASALT.getDefaultStack()))
        { return Items.BASALT.getDefaultStack(); }
        else if (player.getInventory().contains(Items.SMOOTH_BASALT.getDefaultStack()))
        { return Items.SMOOTH_BASALT.getDefaultStack(); }
        //netherrack block
        else if (player.getInventory().contains(Items.NETHERRACK.getDefaultStack()))
        { return Items.NETHERRACK.getDefaultStack(); }
        //endstone block variants
        else if (player.getInventory().contains(Items.END_STONE.getDefaultStack()))
        { return Items.END_STONE.getDefaultStack(); }
        //dirt block variants
        else if (player.getInventory().contains(Items.COARSE_DIRT.getDefaultStack()))
        { return Items.COARSE_DIRT.getDefaultStack(); }
        else if (player.getInventory().contains(Items.DIRT.getDefaultStack()))
        { return Items.DIRT.getDefaultStack(); }
        //if no materials are found
        else return null;
    }
    private BlockState getPhysicalMaterial(ItemStack itemMaterial)
    {
        //deepslate
        if (itemMaterial.isOf(Items.DEEPSLATE))
        { return Blocks.DEEPSLATE.getDefaultState(); }
        else if (itemMaterial.isOf(Items.COBBLED_DEEPSLATE))
        { return Blocks.COBBLED_DEEPSLATE.getDefaultState(); }
        //stone
        else if (itemMaterial.isOf(Items.STONE))
        { return Blocks.STONE.getDefaultState(); }
        else if (itemMaterial.isOf(Items.COBBLESTONE))
        { return Blocks.COBBLESTONE.getDefaultState(); }
        else if (itemMaterial.isOf(Items.MOSSY_COBBLESTONE))
        { return Blocks.MOSSY_COBBLESTONE.getDefaultState(); }
        else if (itemMaterial.isOf(Items.ANDESITE))
        { return Blocks.ANDESITE.getDefaultState(); }
        else if (itemMaterial.isOf(Items.GRANITE))
        { return Blocks.GRANITE.getDefaultState(); }
        else if (itemMaterial.isOf(Items.DIORITE))
        { return Blocks.DIORITE.getDefaultState(); }
        //limestone - wip
        //desert
        else if (itemMaterial.isOf(Items.SANDSTONE))
        { return Blocks.SANDSTONE.getDefaultState(); }
        else if (itemMaterial.isOf(Items.RED_SANDSTONE))
        { return Blocks.RED_SANDSTONE.getDefaultState(); }
        //blackstone
        else if (itemMaterial.isOf(Items.BLACKSTONE))
        { return Blocks.BLACKSTONE.getDefaultState(); }
        //basalt
        else if (itemMaterial.isOf(Items.BASALT))
        { return Blocks.BASALT.getDefaultState(); }
        else if (itemMaterial.isOf(Items.SMOOTH_BASALT))
        { return Blocks.SMOOTH_BASALT.getDefaultState(); }
        //netherrack
        else if (itemMaterial.isOf(Items.NETHERRACK))
        { return Blocks.NETHERRACK.getDefaultState(); }
        //endstone
        else if (itemMaterial.isOf(Items.END_STONE))
        { return Blocks.END_STONE.getDefaultState(); }
        //dirt
        else if (itemMaterial.isOf(Items.COARSE_DIRT))
        { return Blocks.COARSE_DIRT.getDefaultState(); }
        else if (itemMaterial.isOf(Items.DIRT))
        { return Blocks.DIRT.getDefaultState(); }
        //if nothing matches
        else return null;
    }
}
