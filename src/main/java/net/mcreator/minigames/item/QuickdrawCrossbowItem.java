package net.mcreator.minigames.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.core.component.DataComponents;

import net.mcreator.minigames.procedures.GetItemAttributeProcedure;
import net.mcreator.minigames.procedures.QuickdrawCrossbowDescriptionProcedure;
import net.mcreator.minigames.procedures.ShootProjectileProcedure;
import net.mcreator.minigames.init.MinigamesModAttributes;
import net.mcreator.minigames.MinigamesMod;

import javax.annotation.Nullable;

import java.util.function.Consumer;

import com.mojang.serialization.MapCodec;

public class QuickdrawCrossbowItem extends CrossbowItem {
    private boolean startSoundPlayed = false;
    private boolean midSoundPlayed = false;

    public QuickdrawCrossbowItem(Item.Properties properties) {
        super(properties.stacksTo(1).fireResistant().attributes(ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(
                                Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "quickdraw_crossbow_0"),
                                -2.4,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(MinigamesModAttributes.SALVAGE_VALUE,
                        new AttributeModifier(
                                Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "quickdraw_crossbow_1"),
                                35,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(MinigamesModAttributes.REPAIR_VALUE,
                        new AttributeModifier(
                                Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "quickdraw_crossbow_2"),
                                50,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(MinigamesModAttributes.LOAD_TIME,
                        new AttributeModifier(
                                Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "quickdraw_crossbow_3"),
                                35,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(MinigamesModAttributes.RANGED_DAMAGE,
                        new AttributeModifier(
                                Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "quickdraw_crossbow_4"),
                                4,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(MinigamesModAttributes.EFFECT_POTENCY,
                        new AttributeModifier(
                                Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "quickdraw_crossbow_5"),
                                6,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .build()));
    }

    public static int getMaxArrows(ItemStack stack) {
        double potency = GetItemAttributeProcedure.execute(stack, "minigames:effect_potency");
        return Math.max(1, (int) Math.round(potency));
    }

    public static int getLoadedArrows(ItemStack stack) {
        if (!isCharged(stack)) {
            return 0;
        }
        int count = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getIntOr("loaded_arrows", 0);
        return Math.max(1, count);
    }

    public static void setLoadedArrows(ItemStack stack, int count) {
        if (count <= 0) {
            stack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
            CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt("loaded_arrows", 0));
            stack.setCount(1);
        } else {
            stack.set(
                    DataComponents.CHARGED_PROJECTILES,
                    ChargedProjectiles.of(net.minecraft.world.item.ItemStackTemplate.fromNonEmptyStack(new ItemStack(Items.ARROW)))
            );
            CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt("loaded_arrows", count));
            stack.setCount(1);
        }
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack itemstack) {
        return ItemUseAnimation.CROSSBOW;
    }

    @Override
    public int getUseDuration(ItemStack itemstack, LivingEntity livingEntity) {
        return (int) GetItemAttributeProcedure.execute(
                itemstack,
                "minigames:load_time"
        );
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            Item.TooltipContext context,
            TooltipDisplay tooltipDisplay,
            Consumer<Component> consumer,
            TooltipFlag flag
    ) {
        String hoverText = QuickdrawCrossbowDescriptionProcedure.execute(stack);
        if (hoverText != null) {
            for (String line : hoverText.split("\n")) {
                consumer.accept(Component.literal(line));
            }
        }
    }

    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isShiftKeyDown() || player.isCrouching()) {
            if (getLoadedArrows(stack) >= getMaxArrows(stack)) {
                return InteractionResult.FAIL;
            }
            startSoundPlayed = false;
            midSoundPlayed = false;
            player.startUsingItem(hand);
            return InteractionResult.CONSUME;
        } else {
            if (getLoadedArrows(stack) > 0) {
                this.performShooting(
                        world,
                        player,
                        hand,
                        stack,
                        3.15F,
                        1.0F,
                        null
                );
                return InteractionResult.CONSUME;
            }
            return InteractionResult.FAIL;
        }
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseTicks) {
        if (!level.isClientSide()) {
            float progress = (float) (stack.getUseDuration(entity) - remainingUseTicks)
                    / (float) getChargeDuration(stack, entity);

            if (progress < 0.2F) {
                startSoundPlayed = false;
                midSoundPlayed = false;
            }

            if (progress >= 0.2F && !startSoundPlayed) {
                startSoundPlayed = true;
                level.playSound(
                        null,
                        entity.getX(),
                        entity.getY(),
                        entity.getZ(),
                        SoundEvents.CROSSBOW_LOADING_START,
                        entity.getSoundSource(),
                        0.5F,
                        1.0F
                );
            }

            if (progress >= 0.5F && !midSoundPlayed) {
                midSoundPlayed = true;
                level.playSound(
                        null,
                        entity.getX(),
                        entity.getY(),
                        entity.getZ(),
                        SoundEvents.CROSSBOW_LOADING_MIDDLE,
                        entity.getSoundSource(),
                        0.5F,
                        1.0F
                );
            }

            if (progress >= 1.0F) {
                int current = getLoadedArrows(stack);
                int max = getMaxArrows(stack);
                if (current < max) {
                    setLoadedArrows(stack, current + 1);
                }
                level.playSound(
                        null,
                        entity.getX(),
                        entity.getY(),
                        entity.getZ(),
                        SoundEvents.CROSSBOW_LOADING_END,
                        entity.getSoundSource(),
                        1.0F,
                        1.0F
                );
                entity.stopUsingItem();
            }
        }
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeLeft) {
        startSoundPlayed = false;
        midSoundPlayed = false;
        return false;
    }

    @Override
    public void performShooting(
            Level level,
            LivingEntity shooter,
            InteractionHand hand,
            ItemStack stack,
            float power,
            float uncertainty,
            @Nullable LivingEntity target) {

        int current = getLoadedArrows(stack);
        setLoadedArrows(stack, Math.max(0, current - 1));

        if (!level.isClientSide()) {
            ShootProjectileProcedure.execute(
                    level,
                    shooter,
                    GetItemAttributeProcedure.execute(
                            stack,
                            "minigames:ranged_damage"
                    ),
                    0,
                    0.8,
                    0,
                    2,
                    "arrow"
            );
        }

        level.playSound(
                null,
                shooter.getX(),
                shooter.getY(),
                shooter.getZ(),
                SoundEvents.CROSSBOW_SHOOT,
                shooter.getSoundSource(),
                1.0F,
                1.0F
        );
    }

    @Override
    public void inventoryTick(ItemStack stack, net.minecraft.server.level.ServerLevel world, Entity entity, @Nullable net.minecraft.world.entity.EquipmentSlot equipmentSlot) {
        super.inventoryTick(stack, world, entity, equipmentSlot);
        if (stack.getCount() != 1) {
            stack.setCount(1);
        }
    }

    public static double getLoadState(Entity entity, ItemStack itemstack) {
        if (entity == null)
            return 0;
        if (entity instanceof LivingEntity living && living.isUsingItem() && living.getUseItem() == itemstack) {
            double loadTime = GetItemAttributeProcedure.execute(itemstack, "minigames:load_time");
            if (loadTime <= 0)
                loadTime = 35;
            double progress = (double) living.getTicksUsingItem() / loadTime;
            if (progress <= 0) {
                return 0;
            } else if (progress < 0.35) {
                return 1;
            } else if (progress < 0.6) {
                return 2;
            } else if (progress < 0.95) {
                return 3;
            }
            return 4;
        }
        if (isCharged(itemstack)) {
            return 4;
        }
        return 0;
    }

    public record LoadstateProperty() implements RangeSelectItemModelProperty {
        public static final MapCodec<LoadstateProperty> MAP_CODEC =
                MapCodec.unit(new LoadstateProperty());

        @Override
        public float get(ItemStack itemStackToRender,
                         @Nullable ClientLevel clientWorld,
                         @Nullable ItemOwner entity,
                         int seed) {
            return entity instanceof Entity _entity
                    ? (float) getLoadState(_entity, itemStackToRender)
                    : 0;
        }

        @Override
        public MapCodec<? extends RangeSelectItemModelProperty> type() {
            return MAP_CODEC;
        }
    }
}
