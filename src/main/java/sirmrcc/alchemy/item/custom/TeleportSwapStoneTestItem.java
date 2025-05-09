package sirmrcc.alchemy.item.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import sirmrcc.alchemy.item.ModItems;

import java.util.List;
import java.util.function.Predicate;

public class TeleportSwapStoneTestItem extends Item
{
    public TeleportSwapStoneTestItem(Settings settings) { super(settings); }
    public static final Predicate<ItemStack> MATERIALS = (stack) -> stack.isOf(Items.CHORUS_FRUIT);

    //Global Values
    boolean teleport = false;

    //Overridden Functions
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
    {
        if (!world.isClient)
        {
            if (!user.getInventory().contains(MATERIALS) && !user.isCreative())
            {
                return TypedActionResult.fail(user.getStackInHand(hand));
            }
            else
            {
                Entity entity = getTargetEntity(user, 512);
                if (entity != null)
                {
                    swapTeleport(entity, user);
                    setTeleport(true);
                    PlayUseageSound(world, user, entity);
                    if (!user.isCreative())
                    {
                        ItemStack material = getMaterials(user);
                        ConsumeMaterial(material, user);
                        TakeDamage(user.getStackInHand(hand), user, LivingEntity.getSlotForHand(hand));
                        user.getItemCooldownManager().set(this, 100);
                    }
                    //ServerPlayNetworking.send((ServerPlayerEntity) user, );
                    return TypedActionResult.success(user.getStackInHand(hand));
                }
            }
        }
        if (world.isClient)
        {
//            PlayTeleportParticles(world, (Entity) user, teleport);
//            PlayTeleportParticles(world, entity, teleport);
        }
        return TypedActionResult.fail(user.getStackInHand(hand));
    }

    private Entity getTargetEntity(PlayerEntity user, double range) {
        Vec3d start = user.getCameraPosVec(1.0F);
        Vec3d look = user.getRotationVec(1.0F);
        Vec3d end = start.add(look.multiply(range));

        Box box = user.getBoundingBox().stretch(look.multiply(range)).expand(1.0D, 1.0D, 1.0D);
        List<Entity> entities = user.getWorld().getOtherEntities(user, box,
                (entity) -> !entity.isSpectator() && entity.isAlive());

        Entity closestEntity = null;
        double closestDistance = range * range;

        for (Entity entity : entities)
        {
            Box entityBox = entity.getBoundingBox().expand(0.3D);
            var optionalHit = entityBox.raycast(start, end);
            if (optionalHit.isPresent())
            {
                double distance = start.squaredDistanceTo(optionalHit.get());
                if (distance < closestDistance)
                {
                    closestEntity = entity;
                    closestDistance = distance;
                }
            }
        }

        return closestEntity;
    }

    public void swapTeleport(Entity entity, PlayerEntity user)
    {
        Vec3d entityPosition = entity.getPos();
        Vec3d userPosition = user.getPos();

        user.requestTeleport(entityPosition.getX(), entityPosition.getY(), entityPosition.getZ());
        entity.requestTeleport(userPosition.getX(), userPosition.getY(), userPosition.getZ());

    }

    public ItemStack getMaterials(PlayerEntity user)
    {
        if(user.getInventory().contains(MATERIALS))
        {
            for (int i = 0; i < user.getInventory().size(); ++i)
            {
                ItemStack stack = user.getInventory().getStack(i);
                if (MATERIALS.test(stack))
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
    public void PlayUseageSound(World world, PlayerEntity user, Entity entity)
    {
        world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE,
                SoundCategory.PLAYERS, 2.0f, 1.0f);
        world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_AMETHYST_CLUSTER_HIT,
                SoundCategory.PLAYERS, 2.0f, 2.0f);
        world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_PLAYER_TELEPORT,
                SoundCategory.PLAYERS, 1.0f, 1.0f);

        world.playSound(null, entity.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE,
                SoundCategory.PLAYERS, 2.0f, 1.0f);
        world.playSound(null, entity.getBlockPos(),SoundEvents.BLOCK_AMETHYST_CLUSTER_PLACE,
                SoundCategory.PLAYERS,2.0f, 0.0f);
        world.playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_PLAYER_TELEPORT,
                SoundCategory.PLAYERS, 1.0f, 1.0f);
    }

    //TO BE DONE WHEN I GET AROUND TO NETWORKING FOR PARTICLE EFFECTS
    public void PlayTeleportParticles(World world, Entity target, boolean bool)
    {
        if (bool)
        {
            for (int i = 0; i < 100; ++i)
            {
                double offsetX = (world.random.nextDouble() - 0.5);
                double offsetY = world.random.nextDouble();
                double offsetZ = (world.random.nextDouble() - 0.5);

                world.addParticle(ParticleTypes.PORTAL,
                        target.getX() + offsetX, target.getY() + offsetY, target.getZ() + offsetZ,
                        0, 0, 0);
            }
        }
    }
    void setTeleport(boolean bool)
    {
        teleport = bool;
    }
}
