package net.mcreator.minigames.procedures;

import org.checkerframework.checker.units.qual.min;

import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;
import net.minecraft.ChatFormatting;

import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.init.MinigamesModMobEffects;
import net.mcreator.minigames.init.MinigamesModItems;

public class StartWinCutsceneProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		String sec = "";
		String min = "";
		String hour = "";
		MinigamesModVariables.MapVariables.get(world).minimap = true;
		MinigamesModVariables.MapVariables.get(world).displayTimer = false;
		MinigamesModVariables.MapVariables.get(world).nerfWinner = false;
		MinigamesModVariables.MapVariables.get(world).nightVision = false;
		MinigamesModVariables.MapVariables.get(world).randomizeSpawn = false;
		MinigamesModVariables.MapVariables.get(world).headStart = false;
		MinigamesModVariables.MapVariables.get(world).achievement = -1;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
		if (world instanceof Level _level) {
			PlayerTeam _pt = _level.getScoreboard().getPlayerTeam("spread");
			if (_pt != null)
				_level.getScoreboard().removePlayerTeam(_pt);
		}
		if (world instanceof ServerLevel _level) {
			_level.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("\n" + "\n" + "\n" + "\n" + "\n" + "Game Ended")).withColor(0xf9a934).withStyle(ChatFormatting.BOLD), false);
		}
		if ((new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).gameHours)).length() == 1) {
			hour = "0" + new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).gameHours);
		} else {
			hour = new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).gameHours);
		}
		if ((new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).gameMinutes)).length() == 1) {
			min = "0" + new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).gameMinutes);
		} else {
			min = new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).gameMinutes);
		}
		if ((new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).gameSeconds)).length() == 1) {
			sec = "0" + new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).gameSeconds);
		} else {
			sec = new java.text.DecimalFormat("##").format(MinigamesModVariables.MapVariables.get(world).gameSeconds);
		}
		if (world instanceof ServerLevel _level) {
			_level.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("In " + hour + ":" + min + ":" + sec + "\n" + "\n" + "\n" + "\n")).withColor(0xdc4fef), false);
		}
		if (world instanceof ServerLevel _level)
			_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
					"/effect clear @a minigames:crowned");
		if (!MinigamesModVariables.MapVariables.get(world).hunteraWinAnimation) {
			if (entity instanceof LivingEntity _living) {
				_living.setItemSlot(EquipmentSlot.HEAD, new ItemStack(MinigamesModItems.CROWN_HELMET_HELMET.get()));
			}
			if (world instanceof ServerLevel _origLevel) {
				LevelAccessor _worldorig = world;
				world = _origLevel.getServer().getLevel(Level.OVERWORLD);
				if (world != null) {
					if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
						_entity.addEffect(new MobEffectInstance(MinigamesModMobEffects.CROWNED, 100000000, 1, false, false));
				}
				world = _worldorig;
			}
		}
		if (world instanceof Level _level) {
			for (Player player : _level.players()) {
				MinigamesModVariables.PlayerVariables _vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);
				_vars.winner = false;
				_vars.markSyncDirty();
			}
		}
		if (entity instanceof Player _winner) {
			MinigamesModVariables.PlayerVariables _vars = _winner.getData(MinigamesModVariables.PLAYER_VARIABLES);
			_vars.winner = true;
			_vars.markSyncDirty();
		}
		MinigamesModVariables.MapVariables.get(world).winningTeam = entity.getData(MinigamesModVariables.PLAYER_VARIABLES).team;
		MinigamesModVariables.MapVariables.get(world).achievement = -1;
		MinigamesModVariables.MapVariables.get(world).winAnimationStart = true;
		MinigamesModVariables.MapVariables.get(world).winAnimationTick = 0;
		MinigamesModVariables.MapVariables.get(world).markSyncDirty();
	}
}
