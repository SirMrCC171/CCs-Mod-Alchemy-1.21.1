package sirmrcc.alchemy.item.custom;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sirmrcc.alchemy.CCsModAlchemy;
import sirmrcc.alchemy.block.ModBlocks;
import sirmrcc.alchemy.item.ModItems;

public class PhilosophersStoneTest extends Item
{
    public PhilosophersStoneTest(Settings settings)
    {
        super(settings);
    }

    private boolean isActive = false;
    private boolean hasMaterials = false;
    private EquipmentSlot mySlot;
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
    {
        if (!world.isClient)
        {
            //check for valid materials
            //  changes active status depending on material status
            if (checkForMaterials(user, world) != null)
            {
                changeActiveStatus(hand);
                playActivationSoundChange(user, world);
                inventoryTick(user.getStackInHand(hand), world, user, user.getInventory().selectedSlot, true);
                hasGlint(user.getStackInHand(hand));
            }
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected)
    {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (!world.isClient)
        {
            PlayerEntity player = (PlayerEntity) entity;
            //receive material item and get corresponding block
            ItemStack myMaterial = checkForMaterials(player, world);
            CCsModAlchemy.LOGGER.info("Material: " + myMaterial);
            if (myMaterial == null)
            {
                deactivate();
                return;
            }
            BlockState material = getPhysicalMaterial(myMaterial);
            CCsModAlchemy.LOGGER.info("Blockstate: " + material);
            if (isNotSolidGround(player) && isActive && material != null)
            {
                BlockPos underFeet = (player.getBlockPos().down());
                world.setBlockState(underFeet, material);
                useMaterial(player, world);
                stack.damage(1, player, mySlot);
                whenDamaged(stack, player);
            }
        }
    }

    @Override
    public boolean hasGlint(ItemStack stack)
    {
        return isActive;
    }
    private void changeActiveStatus(Hand hand)
    {
        mySlot = LivingEntity.getSlotForHand(hand);
        if(!isActive) activate();
        else deactivate();
    }
    private void whenDamaged(ItemStack item, PlayerEntity player)
    {
        if (item.getDamage() == 0 && !player.isCreative())
        {
            ItemStack testBroken = new ItemStack(ModItems.PHILOSOPHERS_STONE_POLISHED_BROKEN);
            player.giveItemStack(testBroken);
        }
    }
    private void activate()
    {
        isActive = true;
    }
    private void deactivate()
    {
        isActive = false;
    }
    private void playActivationSoundChange(PlayerEntity player, World world)
    {
        if(!world.isClient)
        {
            CCsModAlchemy.LOGGER.info("Activation: " + isActive + "\nHas Material :" + hasMaterials);
            if (isActive && hasMaterials)
            {
                CCsModAlchemy.LOGGER.info("Playing activation sound");
                world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.PLAYERS, 2.0f, 1.0f);
                world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_MEDIUM_AMETHYST_BUD_BREAK, SoundCategory.PLAYERS, 2.0f, 2.0f);
            }
            else
            {
                CCsModAlchemy.LOGGER.info("Playing deactivation sound");
                world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.PLAYERS, 2.0f, 1.0f);
                world.playSound(null, player.getBlockPos(),SoundEvents.BLOCK_AMETHYST_CLUSTER_PLACE, SoundCategory.PLAYERS,2.0f, 0.0f);
            }
        }
    }
    private void outOfMaterial(PlayerEntity player, World world)
    {
        CCsModAlchemy.LOGGER.info("Checking for material. Has Materials: " + hasMaterials);
        if (hasMaterials)
        {
            playActivationSoundChange(player, world);
            hasMaterials = false;
        }
    }
    private boolean isNotSolidGround(PlayerEntity player)
    {
        BlockPos underFeet = player.getBlockPos().down();
        BlockState blockStateUnderFeet = player.getWorld().getBlockState(underFeet);
        return blockStateUnderFeet.isReplaceable();
    }
    private void useMaterial(PlayerEntity player, World world)
    {
        if (!world.isClient)
        {
            ItemStack myMaterial;
            myMaterial = checkForMaterials(player, world);
            boolean hasValidMaterial = myMaterial != null;
            if (hasValidMaterial)
            {
                int index = player.getInventory().indexOf(myMaterial);
                player.getInventory().removeStack(index, 1);
                world.playSound(null, player.getBlockPos() , materialSound(myMaterial), SoundCategory.BLOCKS, 1.0f, 1.0f);

            }
            else deactivate();
        }
    }
    private ItemStack checkForMaterials(PlayerEntity player, World world)
    {
        if (!world.isClient)
        {
            //deepslate block variants
            if (player.getInventory().contains(Items.DEEPSLATE.getDefaultStack())) {
                hasMaterials = true;
                return Items.DEEPSLATE.getDefaultStack();
            } else if (player.getInventory().contains(Items.COBBLED_DEEPSLATE.getDefaultStack()))
            {
                hasMaterials = true;
                return Items.COBBLED_DEEPSLATE.getDefaultStack();
            }
            //stone block variants
            else if (player.getInventory().contains(Items.STONE.getDefaultStack()))
            {
                hasMaterials = true;
                return Items.STONE.getDefaultStack();
            }
            else if (player.getInventory().contains(Items.COBBLESTONE.getDefaultStack()))
            {
                hasMaterials = true;
                return Items.COBBLESTONE.getDefaultStack();
            }
            else if (player.getInventory().contains(Items.MOSSY_COBBLESTONE.getDefaultStack()))
            {
                hasMaterials = true;
                return Items.MOSSY_COBBLESTONE.getDefaultStack();
            }
            else if (player.getInventory().contains(Items.ANDESITE.getDefaultStack()))
            {
                hasMaterials = true;
                return Items.ANDESITE.getDefaultStack();
            }
            else if (player.getInventory().contains(Items.GRANITE.getDefaultStack()))
            {
                hasMaterials = true;
                return Items.GRANITE.getDefaultStack();
            }
            else if (player.getInventory().contains(Items.DIORITE.getDefaultStack()))
            {
                hasMaterials = true;
                return Items.DIORITE.getDefaultStack();
            }
            //Limestone block variants - wip
            else if (player.getInventory().contains(ModBlocks.LIMESTONE.asItem().getDefaultStack()))
            {
                hasMaterials = true;
                return ModBlocks.LIMESTONE.asItem().getDefaultStack();
            }
            //desert block variants
            else if (player.getInventory().contains(Items.SANDSTONE.getDefaultStack()))
            {
                hasMaterials = true;
                return Items.SANDSTONE.getDefaultStack();
            }
            else if (player.getInventory().contains(Items.RED_SANDSTONE.getDefaultStack()))
            {
                hasMaterials = true;
                return Items.RED_SANDSTONE.getDefaultStack();
            }
            //blackstone block variants
            else if (player.getInventory().contains(Items.BLACKSTONE.getDefaultStack()))
            {
                hasMaterials = true;
                return Items.BLACKSTONE.getDefaultStack();
            }
            //basalt block variants
            else if (player.getInventory().contains(Items.BASALT.getDefaultStack()))
            {
                hasMaterials = true;
                return Items.BASALT.getDefaultStack();
            }
            else if (player.getInventory().contains(Items.SMOOTH_BASALT.getDefaultStack()))
            {
                hasMaterials = true;
                return Items.SMOOTH_BASALT.getDefaultStack();
            }
            //netherrack block
            else if (player.getInventory().contains(Items.NETHERRACK.getDefaultStack()))
            {
                hasMaterials = true;
                return Items.NETHERRACK.getDefaultStack();
            }
            //endstone block variants
            else if (player.getInventory().contains(Items.END_STONE.getDefaultStack()))
            {
                hasMaterials = true;
                return Items.END_STONE.getDefaultStack();
            }
            //dirt block variants
            else if (player.getInventory().contains(Items.COARSE_DIRT.getDefaultStack()))
            {
                hasMaterials = true;
                return Items.COARSE_DIRT.getDefaultStack();
            }
            else if (player.getInventory().contains(Items.DIRT.getDefaultStack()))
            {
                hasMaterials = true;
                return Items.DIRT.getDefaultStack();
            }
            //if no materials are found
            else
            {
                if (hasMaterials) outOfMaterial(player, world);
                return null;
            }
        }
        return null;
    }
    private BlockState getPhysicalMaterial(ItemStack itemMaterial)
    {
        //deepslate
        if (itemMaterial.isOf(Items.DEEPSLATE)) return Blocks.DEEPSLATE.getDefaultState();
        else if (itemMaterial.isOf(Items.COBBLED_DEEPSLATE)) return Blocks.COBBLED_DEEPSLATE.getDefaultState();
        //stone
        else if (itemMaterial.isOf(Items.STONE)) return Blocks.STONE.getDefaultState();
        else if (itemMaterial.isOf(Items.COBBLESTONE)) return Blocks.COBBLESTONE.getDefaultState();
        else if (itemMaterial.isOf(Items.MOSSY_COBBLESTONE)) return Blocks.MOSSY_COBBLESTONE.getDefaultState();
        else if (itemMaterial.isOf(Items.ANDESITE)) return Blocks.ANDESITE.getDefaultState();
        else if (itemMaterial.isOf(Items.GRANITE)) return Blocks.GRANITE.getDefaultState();
        else if (itemMaterial.isOf(Items.DIORITE)) return Blocks.DIORITE.getDefaultState();
        //limestone
        else if (itemMaterial.isOf(ModBlocks.LIMESTONE.asItem())) return ModBlocks.LIMESTONE.getDefaultState();
        //desert
        else if (itemMaterial.isOf(Items.SANDSTONE)) return Blocks.SANDSTONE.getDefaultState();
        else if (itemMaterial.isOf(Items.RED_SANDSTONE)) return Blocks.RED_SANDSTONE.getDefaultState();
        //blackstone
        else if (itemMaterial.isOf(Items.BLACKSTONE)) return Blocks.BLACKSTONE.getDefaultState();
        //basalt
        else if (itemMaterial.isOf(Items.BASALT)) return Blocks.BASALT.getDefaultState();
        else if (itemMaterial.isOf(Items.SMOOTH_BASALT)) return Blocks.SMOOTH_BASALT.getDefaultState();
        //netherrack
        else if (itemMaterial.isOf(Items.NETHERRACK)) return Blocks.NETHERRACK.getDefaultState();
        //endstone
        else if (itemMaterial.isOf(Items.END_STONE)) return Blocks.END_STONE.getDefaultState();
        //dirt
        else if (itemMaterial.isOf(Items.COARSE_DIRT)) return Blocks.COARSE_DIRT.getDefaultState();
        else if (itemMaterial.isOf(Items.DIRT)) return Blocks.DIRT.getDefaultState();
        //if nothing matches
        else return null;
    }
    private SoundEvent materialSound(ItemStack material)
    {
        //deepslate sound
         if (material.isOf(Items.DEEPSLATE) || material.isOf(Items.COBBLED_DEEPSLATE))
             return SoundEvents.BLOCK_DEEPSLATE_PLACE;
         //stone sound
        else if (material.isOf(Items.STONE) || material.isOf(Items.COBBLESTONE) ||
                 material.isOf(Items.MOSSY_COBBLESTONE) || material.isOf(Items.ANDESITE) ||
                 material.isOf(Items.GRANITE) || material.isOf(Items.DIORITE) ||
                 material.isOf(Items.SANDSTONE) || material.isOf(Items.RED_SANDSTONE) ||
                 material.isOf(Items.BLACKSTONE) || material.isOf(Items.END_STONE))
            return SoundEvents.BLOCK_STONE_PLACE;
        //dripstone/limestone sound
        else if (material.isOf(ModBlocks.LIMESTONE.asItem())) return SoundEvents.BLOCK_DRIPSTONE_BLOCK_PLACE;
        //basalt sounds
         else if (material.isOf(Items.BASALT) || material.isOf(Items.SMOOTH_BASALT))
             return SoundEvents.BLOCK_BASALT_PLACE;
         //netherrack
         else if (material.isOf(Items.NETHERRACK)) return SoundEvents.BLOCK_NETHERRACK_PLACE;
         //dirt sound
         else if (material.isOf(Items.COARSE_DIRT) || material.isOf(Items.DIRT))
             return SoundEvents.BLOCK_GRAVEL_PLACE;
         else return null;
    }
}
