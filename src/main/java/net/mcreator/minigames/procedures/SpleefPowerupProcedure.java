package net.mcreator.minigames.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minigames.network.MinigamesModVariables;

public class SpleefPowerupProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (MinigamesModVariables.MapVariables.get(world).spleefPowerups) {
			if (entity.getData(MinigamesModVariables.PLAYER_VARIABLES).snowballCountSpleef >= 99) {
				{
					MinigamesModVariables.PlayerVariables _vars = entity.getData(MinigamesModVariables.PLAYER_VARIABLES);
					_vars.snowballCountSpleef = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).snowballCountSpleef - 99;
					_vars.markSyncDirty();
				}
				if (entity instanceof Player _player) {
					ItemStack _setstack = new ItemStack(
							(BuiltInRegistries.ITEM.getRandomElementOf(ItemTags.create(Identifier.parse("minigames:spleef_powerup")), RandomSource.create()).orElseGet(() -> BuiltInRegistries.ITEM.wrapAsHolder(Items.AIR)).value())).copy();
					_setstack.setCount(1);
					_player.getInventory().placeItemBackInInventory(_setstack);
				}
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.level().getServer() != null) {
						_ent.level().getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null,
								LevelBasedPermissionSet.OWNER, _ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "/playsound minecraft:block.anvil.use master @p ~ ~ ~ 0.5 2");
					}
				}
				if (entity instanceof ServerPlayer _player)
					_player.sendSystemMessage(Component.literal("\u00A76Your \u00A7a99 \u00A76snowballs have been compacted into a \u00A7arandom powerup"), false);
			}
		}
	}
}