package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.ItemTags;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModItems;

import java.util.ArrayList;

public class TeamUpgradePickedUpProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, ItemStack itemstack) {
		if (itemstack.is(ItemTags.create(Identifier.parse("minigames:dungeon_shop_team")))) {
			if (MinigamesModItems.SMALLIFICATION.get() == itemstack.getItem()) {
				for (Entity entityiterator : new ArrayList<>(world.players())) {
					if (entityiterator instanceof LivingEntity _livingEntity5 && _livingEntity5.getAttributes().hasAttribute(Attributes.SCALE))
						_livingEntity5.getAttribute(Attributes.SCALE)
								.setBaseValue(((entityiterator instanceof LivingEntity _livingEntity4 && _livingEntity4.getAttributes().hasAttribute(Attributes.SCALE) ? _livingEntity4.getAttribute(Attributes.SCALE).getBaseValue() : 0) / 2));
				}
			} else if (MinigamesModItems.EXTRA_ITEM_ROOM.get() == itemstack.getItem()) {
				MinigamesModVariables.MapVariables.get(world).extraItemRooms = MinigamesModVariables.MapVariables.get(world).extraItemRooms + 1;
				MinigamesModVariables.MapVariables.get(world).markSyncDirty();
			}
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.ender_dragon.growl")), SoundSource.PLAYERS, (float) 0.8, (float) 1.2);
				} else {
					_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.getValue(Identifier.parse("entity.ender_dragon.growl")), SoundSource.PLAYERS, (float) 0.8, (float) 1.2, false);
				}
			}
		}
	}
}