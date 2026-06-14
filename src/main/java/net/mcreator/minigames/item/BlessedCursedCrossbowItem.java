package net.mcreator.minigames.item;

import net.mcreator.minigames.procedures.ShootProjectileProcedure;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;

import net.mcreator.minigames.procedures.ReturnLoadstateProcedure;
import net.mcreator.minigames.procedures.GetItemAttributeProcedure;
import net.mcreator.minigames.procedures.BlessedCursedCrossbowDescriptionProcedure;
import net.mcreator.minigames.init.MinigamesModAttributes;
import net.mcreator.minigames.MinigamesMod;

import javax.annotation.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

import com.mojang.serialization.MapCodec;

public class BlessedCursedCrossbowItem extends CrossbowItem {
    private boolean startSoundPlayed = false;
    private boolean midSoundPlayed = false;
    public BlessedCursedCrossbowItem(Item.Properties properties) {
        super(properties.stacksTo(1).fireResistant().attributes(ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(
                                ResourceLocation.fromNamespaceAndPath(MinigamesMod.MODID, "blessed_cursed_crossbow_0"),
                                -2.4,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(MinigamesModAttributes.SALVAGE_VALUE,
                        new AttributeModifier(
                                ResourceLocation.fromNamespaceAndPath(MinigamesMod.MODID, "blessed_cursed_crossbow_1"),
                                35,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(MinigamesModAttributes.REPAIR_VALUE,
                        new AttributeModifier(
                                ResourceLocation.fromNamespaceAndPath(MinigamesMod.MODID, "blessed_cursed_crossbow_2"),
                                50,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(MinigamesModAttributes.LOAD_TIME,
                        new AttributeModifier(
                                ResourceLocation.fromNamespaceAndPath(MinigamesMod.MODID, "blessed_cursed_crossbow_3"),
                                50,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(MinigamesModAttributes.RANGED_DAMAGE,
                        new AttributeModifier(
                                ResourceLocation.fromNamespaceAndPath(MinigamesMod.MODID, "blessed_cursed_crossbow_4"),
                                4,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.ANY)
                .build()));
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
        String hoverText =
                BlessedCursedCrossbowDescriptionProcedure.execute();

        if (hoverText != null) {
            for (String line : hoverText.split("\n")) {
                consumer.accept(Component.literal(line));
            }
        }
    }

    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (isCharged(stack)) {
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

        startSoundPlayed = false;
        midSoundPlayed = false;

        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }
    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseTicks) {
        if (!level.isClientSide) {

            float progress =
                    (float)(stack.getUseDuration(entity) - remainingUseTicks)
                            / (float)getChargeDuration(stack, entity);

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

            if (progress >= 1.0F && !isCharged(stack)) {

                stack.set(
                        DataComponents.CHARGED_PROJECTILES,
                        ChargedProjectiles.of(new ItemStack(Items.ARROW))
                );

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
            }
        }
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

        stack.set(
                DataComponents.CHARGED_PROJECTILES,
                ChargedProjectiles.EMPTY
        );

        if (!level.isClientSide) {
            ShootProjectileProcedure.execute(
                    level,
                    shooter,
                    GetItemAttributeProcedure.execute(
                            stack,
                            "minigames:ranged_damage"
                    ),
                    0,
                    0.6,
                    0,
                    2.5,
                    "spectral_arrow"
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

    public record LoadstateProperty() implements RangeSelectItemModelProperty {
        public static final MapCodec<LoadstateProperty> MAP_CODEC =
                MapCodec.unit(new LoadstateProperty());

        @Override
        public float get(ItemStack itemStackToRender,
                         @Nullable ClientLevel clientWorld,
                         @Nullable LivingEntity entity,
                         int seed) {
            return entity == null
                    ? 0
                    : (float) ReturnLoadstateProcedure.execute(entity, itemStackToRender);
        }

        @Override
        public MapCodec<LoadstateProperty> type() {
            return MAP_CODEC;
        }
    }
}