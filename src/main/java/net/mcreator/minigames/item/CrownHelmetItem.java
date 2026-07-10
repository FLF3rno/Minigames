package net.mcreator.minigames.item;

import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.TagKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.minigames.procedures.CrownTickEventProcedure;

import javax.annotation.Nullable;

import java.util.Map;

public abstract class CrownHelmetItem extends Item {
	public static ArmorMaterial ARMOR_MATERIAL = new ArmorMaterial(1024, Map.of(ArmorType.BOOTS, 0, ArmorType.LEGGINGS, 0, ArmorType.CHESTPLATE, 0, ArmorType.HELMET, 5, ArmorType.BODY, 0), 9,
			BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.EMPTY), 2f, 0f, TagKey.create(Registries.ITEM, Identifier.parse("minigames:crown_helmet_repair_items")),
			ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.parse("minigames:crown_helmet")));

	private CrownHelmetItem(Item.Properties properties) {
		super(properties);
	}

	public static class Helmet extends CrownHelmetItem {
		public Helmet(Item.Properties properties) {
			super(properties.fireResistant().humanoidArmor(ARMOR_MATERIAL, ArmorType.HELMET));
		}

		@Override
		public boolean makesPiglinsNeutral(ItemStack itemstack, LivingEntity entity) {
			return true;
		}

		@Override
		public void inventoryTick(ItemStack itemstack, ServerLevel world, Entity entity, @Nullable EquipmentSlot equipmentSlot) {
			super.inventoryTick(itemstack, world, entity, equipmentSlot);
			if (entity instanceof Player player && (equipmentSlot != null && equipmentSlot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR)) {
				CrownTickEventProcedure.execute(world, entity, itemstack);
			}
		}
	}
}