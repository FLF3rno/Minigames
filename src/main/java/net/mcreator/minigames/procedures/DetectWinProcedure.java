package net.mcreator.minigames.procedures;

import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;

import net.mcreator.minigames.network.MinigamesModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class DetectWinProcedure {
	@SubscribeEvent
	public static void onAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
		execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		boolean logic = false;
		if (MinigamesModVariables.MapVariables.get(world).achievementHunterMode == true) {
			if ((MinigamesModVariables.MapVariables.get(world).hunterAchievement).equals(entity.getDisplayName().getString())) {
				logic = true;
			} else {
				logic = false;
			}
		} else {
			logic = true;
		}
		if (logic == true) {
			if (entity instanceof ServerPlayer _plr1 && _plr1.level() instanceof ServerLevel _serverLevel1
					&& _plr1.getAdvancements().getOrStartProgress(_serverLevel1.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:story/mine_stone"))).isDone() && 0 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr2 && _plr2.level() instanceof ServerLevel _serverLevel2
					&& _plr2.getAdvancements().getOrStartProgress(_serverLevel2.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:story/upgrade_tools"))).isDone() && 1 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr3 && _plr3.level() instanceof ServerLevel _serverLevel3
					&& _plr3.getAdvancements().getOrStartProgress(_serverLevel3.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:story/smelt_iron"))).isDone() && 2 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr4 && _plr4.level() instanceof ServerLevel _serverLevel4
					&& _plr4.getAdvancements().getOrStartProgress(_serverLevel4.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:story/obtain_armor"))).isDone() && 3 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr5 && _plr5.level() instanceof ServerLevel _serverLevel5
					&& _plr5.getAdvancements().getOrStartProgress(_serverLevel5.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:story/lava_bucket"))).isDone() && 4 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr6 && _plr6.level() instanceof ServerLevel _serverLevel6
					&& _plr6.getAdvancements().getOrStartProgress(_serverLevel6.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:story/iron_tools"))).isDone() && 5 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr7 && _plr7.level() instanceof ServerLevel _serverLevel7
					&& _plr7.getAdvancements().getOrStartProgress(_serverLevel7.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:story/deflect_arrow"))).isDone() && 6 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr8 && _plr8.level() instanceof ServerLevel _serverLevel8
					&& _plr8.getAdvancements().getOrStartProgress(_serverLevel8.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:story/form_obsidian"))).isDone() && 7 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr9 && _plr9.level() instanceof ServerLevel _serverLevel9
					&& _plr9.getAdvancements().getOrStartProgress(_serverLevel9.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:story/mine_diamond"))).isDone() && 8 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr10 && _plr10.level() instanceof ServerLevel _serverLevel10
					&& _plr10.getAdvancements().getOrStartProgress(_serverLevel10.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:story/enter_the_nether"))).isDone()
					&& 9 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr11 && _plr11.level() instanceof ServerLevel _serverLevel11
					&& _plr11.getAdvancements().getOrStartProgress(_serverLevel11.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:story/shiny_gear"))).isDone() && 10 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr12 && _plr12.level() instanceof ServerLevel _serverLevel12
					&& _plr12.getAdvancements().getOrStartProgress(_serverLevel12.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:story/enchant_item"))).isDone()
					&& 11 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr13 && _plr13.level() instanceof ServerLevel _serverLevel13
					&& _plr13.getAdvancements().getOrStartProgress(_serverLevel13.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:story/cure_zombie_villager"))).isDone()
					&& 12 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr14 && _plr14.level() instanceof ServerLevel _serverLevel14
					&& _plr14.getAdvancements().getOrStartProgress(_serverLevel14.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:story/follow_ender_eye"))).isDone()
					&& 13 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr15 && _plr15.level() instanceof ServerLevel _serverLevel15
					&& _plr15.getAdvancements().getOrStartProgress(_serverLevel15.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:story/follow_ender_eye"))).isDone()
					&& 14 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr16 && _plr16.level() instanceof ServerLevel _serverLevel16
					&& _plr16.getAdvancements().getOrStartProgress(_serverLevel16.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:nether/find_bastion"))).isDone()
					&& 15 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr17 && _plr17.level() instanceof ServerLevel _serverLevel17
					&& _plr17.getAdvancements().getOrStartProgress(_serverLevel17.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:nether/obtain_ancient_debris"))).isDone()
					&& 16 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr18 && _plr18.level() instanceof ServerLevel _serverLevel18
					&& _plr18.getAdvancements().getOrStartProgress(_serverLevel18.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:nether/fast_travel"))).isDone()
					&& 17 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr19 && _plr19.level() instanceof ServerLevel _serverLevel19
					&& _plr19.getAdvancements().getOrStartProgress(_serverLevel19.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:nether/find_fortress"))).isDone()
					&& 18 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr20 && _plr20.level() instanceof ServerLevel _serverLevel20
					&& _plr20.getAdvancements().getOrStartProgress(_serverLevel20.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:nether/obtain_crying_obsidian"))).isDone()
					&& 19 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr21 && _plr21.level() instanceof ServerLevel _serverLevel21
					&& _plr21.getAdvancements().getOrStartProgress(_serverLevel21.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:nether/distract_piglin"))).isDone()
					&& 20 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr22 && _plr22.level() instanceof ServerLevel _serverLevel22
					&& _plr22.getAdvancements().getOrStartProgress(_serverLevel22.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:nether/ride_strider"))).isDone()
					&& 21 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr23 && _plr23.level() instanceof ServerLevel _serverLevel23
					&& _plr23.getAdvancements().getOrStartProgress(_serverLevel23.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:nether/uneasy_alliance"))).isDone()
					&& 22 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr24 && _plr24.level() instanceof ServerLevel _serverLevel24
					&& _plr24.getAdvancements().getOrStartProgress(_serverLevel24.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:nether/loot_bastion"))).isDone()
					&& 23 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr25 && _plr25.level() instanceof ServerLevel _serverLevel25
					&& _plr25.getAdvancements().getOrStartProgress(_serverLevel25.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:nether/get_wither_skull"))).isDone()
					&& 24 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr26 && _plr26.level() instanceof ServerLevel _serverLevel26
					&& _plr26.getAdvancements().getOrStartProgress(_serverLevel26.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:nether/obtain_blaze_rod"))).isDone()
					&& 25 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr27 && _plr27.level() instanceof ServerLevel _serverLevel27
					&& _plr27.getAdvancements().getOrStartProgress(_serverLevel27.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:nether/charge_respawn_anchor"))).isDone()
					&& 26 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr28 && _plr28.level() instanceof ServerLevel _serverLevel28
					&& _plr28.getAdvancements().getOrStartProgress(_serverLevel28.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:nether/ride_strider_in_overworld_lava"))).isDone()
					&& 27 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr29 && _plr29.level() instanceof ServerLevel _serverLevel29
					&& _plr29.getAdvancements().getOrStartProgress(_serverLevel29.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:nether/explore_nether"))).isDone()
					&& 28 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr30 && _plr30.level() instanceof ServerLevel _serverLevel30
					&& _plr30.getAdvancements().getOrStartProgress(_serverLevel30.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:nether/brew_potion"))).isDone()
					&& 29 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr31 && _plr31.level() instanceof ServerLevel _serverLevel31
					&& _plr31.getAdvancements().getOrStartProgress(_serverLevel31.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:adventure/voluntary_exile"))).isDone()
					&& 30 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr32 && _plr32.level() instanceof ServerLevel _serverLevel32
					&& _plr32.getAdvancements().getOrStartProgress(_serverLevel32.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:adventure/spyglass_at_parrot"))).isDone()
					&& 31 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr33 && _plr33.level() instanceof ServerLevel _serverLevel33
					&& _plr33.getAdvancements().getOrStartProgress(_serverLevel33.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:adventure/use_lodestone"))).isDone()
					&& 32 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr34 && _plr34.level() instanceof ServerLevel _serverLevel34
					&& _plr34.getAdvancements().getOrStartProgress(_serverLevel34.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:adventure/kill_a_mob"))).isDone()
					&& 33 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr35 && _plr35.level() instanceof ServerLevel _serverLevel35
					&& _plr35.getAdvancements().getOrStartProgress(_serverLevel35.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:adventure/read_power_of_chiseled_bookshelf"))).isDone()
					&& 34 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr36 && _plr36.level() instanceof ServerLevel _serverLevel36
					&& _plr36.getAdvancements().getOrStartProgress(_serverLevel36.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:adventure/trade"))).isDone() && 35 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr37 && _plr37.level() instanceof ServerLevel _serverLevel37
					&& _plr37.getAdvancements().getOrStartProgress(_serverLevel37.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:adventure/trim_with_any_armor_pattern"))).isDone()
					&& 36 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr38 && _plr38.level() instanceof ServerLevel _serverLevel38
					&& _plr38.getAdvancements().getOrStartProgress(_serverLevel38.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:adventure/honey_block_slide"))).isDone()
					&& 37 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr39 && _plr39.level() instanceof ServerLevel _serverLevel39
					&& _plr39.getAdvancements().getOrStartProgress(_serverLevel39.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:adventure/ol_betsy"))).isDone()
					&& 38 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr40 && _plr40.level() instanceof ServerLevel _serverLevel40
					&& _plr40.getAdvancements().getOrStartProgress(_serverLevel40.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:adventure/lightning_rod_with_villager_no_fire"))).isDone()
					&& 39 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr41 && _plr41.level() instanceof ServerLevel _serverLevel41
					&& _plr41.getAdvancements().getOrStartProgress(_serverLevel41.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:adventure/fall_from_world_height"))).isDone()
					&& 40 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr42 && _plr42.level() instanceof ServerLevel _serverLevel42
					&& _plr42.getAdvancements().getOrStartProgress(_serverLevel42.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:adventure/salvage_sherd"))).isDone()
					&& 41 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr43 && _plr43.level() instanceof ServerLevel _serverLevel43
					&& _plr43.getAdvancements().getOrStartProgress(_serverLevel43.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:adventure/avoid_vibration"))).isDone()
					&& 42 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr44 && _plr44.level() instanceof ServerLevel _serverLevel44
					&& _plr44.getAdvancements().getOrStartProgress(_serverLevel44.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:adventure/sleep_in_bed"))).isDone()
					&& 43 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr45 && _plr45.level() instanceof ServerLevel _serverLevel45
					&& _plr45.getAdvancements().getOrStartProgress(_serverLevel45.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:adventure/hero_of_the_village"))).isDone()
					&& 44 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr46 && _plr46.level() instanceof ServerLevel _serverLevel46
					&& _plr46.getAdvancements().getOrStartProgress(_serverLevel46.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:adventure/spyglass_at_ghast"))).isDone()
					&& 45 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr47 && _plr47.level() instanceof ServerLevel _serverLevel47
					&& _plr47.getAdvancements().getOrStartProgress(_serverLevel47.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:adventure/throw_trident"))).isDone()
					&& 46 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr48 && _plr48.level() instanceof ServerLevel _serverLevel48
					&& _plr48.getAdvancements().getOrStartProgress(_serverLevel48.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:adventure/kill_mob_near_sculk_catalyst"))).isDone()
					&& 47 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr49 && _plr49.level() instanceof ServerLevel _serverLevel49
					&& _plr49.getAdvancements().getOrStartProgress(_serverLevel49.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:adventure/shoot_arrow"))).isDone()
					&& 48 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr50 && _plr50.level() instanceof ServerLevel _serverLevel50
					&& _plr50.getAdvancements().getOrStartProgress(_serverLevel50.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:adventure/kill_all_mobs"))).isDone()
					&& 49 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr51 && _plr51.level() instanceof ServerLevel _serverLevel51
					&& _plr51.getAdvancements().getOrStartProgress(_serverLevel51.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:adventure/totem_of_undying"))).isDone()
					&& 50 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr52 && _plr52.level() instanceof ServerLevel _serverLevel52
					&& _plr52.getAdvancements().getOrStartProgress(_serverLevel52.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:adventure/summon_iron_golem"))).isDone()
					&& 51 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr53 && _plr53.level() instanceof ServerLevel _serverLevel53
					&& _plr53.getAdvancements().getOrStartProgress(_serverLevel53.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:adventure/trade_at_world_height"))).isDone()
					&& 52 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr54 && _plr54.level() instanceof ServerLevel _serverLevel54
					&& _plr54.getAdvancements().getOrStartProgress(_serverLevel54.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:adventure/whos_the_pillager_now"))).isDone()
					&& 53 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr55 && _plr55.level() instanceof ServerLevel _serverLevel55
					&& _plr55.getAdvancements().getOrStartProgress(_serverLevel55.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:adventure/craft_decorated_pot_using_only_sherds"))).isDone()
					&& 54 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr56 && _plr56.level() instanceof ServerLevel _serverLevel56
					&& _plr56.getAdvancements().getOrStartProgress(_serverLevel56.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:adventure/play_jukebox_in_meadows"))).isDone()
					&& 55 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr57 && _plr57.level() instanceof ServerLevel _serverLevel57
					&& _plr57.getAdvancements().getOrStartProgress(_serverLevel57.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:adventure/walk_on_powder_snow_with_leather_boots"))).isDone()
					&& 56 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr58 && _plr58.level() instanceof ServerLevel _serverLevel58
					&& _plr58.getAdvancements().getOrStartProgress(_serverLevel58.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:adventure/spyglass_at_dragon"))).isDone()
					&& 57 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr59 && _plr59.level() instanceof ServerLevel _serverLevel59
					&& _plr59.getAdvancements().getOrStartProgress(_serverLevel59.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:adventure/sniper_duel"))).isDone()
					&& 58 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr60 && _plr60.level() instanceof ServerLevel _serverLevel60
					&& _plr60.getAdvancements().getOrStartProgress(_serverLevel60.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:adventure/bullseye"))).isDone()
					&& 59 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr61 && _plr61.level() instanceof ServerLevel _serverLevel61
					&& _plr61.getAdvancements().getOrStartProgress(_serverLevel61.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:husbandry/safely_harvest_honey"))).isDone()
					&& 60 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr62 && _plr62.level() instanceof ServerLevel _serverLevel62
					&& _plr62.getAdvancements().getOrStartProgress(_serverLevel62.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:husbandry/place_dried_ghast_in_water"))).isDone()
					&& 61 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr63 && _plr63.level() instanceof ServerLevel _serverLevel63
					&& _plr63.getAdvancements().getOrStartProgress(_serverLevel63.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:husbandry/breed_an_animal"))).isDone()
					&& 62 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr64 && _plr64.level() instanceof ServerLevel _serverLevel64
					&& _plr64.getAdvancements().getOrStartProgress(_serverLevel64.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:husbandry/allay_deliver_item_to_player"))).isDone()
					&& 63 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr65 && _plr65.level() instanceof ServerLevel _serverLevel65
					&& _plr65.getAdvancements().getOrStartProgress(_serverLevel65.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:husbandry/ride_a_boat_with_a_goat"))).isDone()
					&& 64 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr66 && _plr66.level() instanceof ServerLevel _serverLevel66
					&& _plr66.getAdvancements().getOrStartProgress(_serverLevel66.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:husbandry/tame_an_animal"))).isDone()
					&& 65 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr67 && _plr67.level() instanceof ServerLevel _serverLevel67
					&& _plr67.getAdvancements().getOrStartProgress(_serverLevel67.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:husbandry/make_a_sign_glow"))).isDone()
					&& 66 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr68 && _plr68.level() instanceof ServerLevel _serverLevel68
					&& _plr68.getAdvancements().getOrStartProgress(_serverLevel68.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:husbandry/fishy_business"))).isDone()
					&& 67 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr69 && _plr69.level() instanceof ServerLevel _serverLevel69
					&& _plr69.getAdvancements().getOrStartProgress(_serverLevel69.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:husbandry/silk_touch_nest"))).isDone()
					&& 68 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr70 && _plr70.level() instanceof ServerLevel _serverLevel70
					&& _plr70.getAdvancements().getOrStartProgress(_serverLevel70.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:husbandry/tadpole_in_a_bucket"))).isDone()
					&& 69 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr71 && _plr71.level() instanceof ServerLevel _serverLevel71
					&& _plr71.getAdvancements().getOrStartProgress(_serverLevel71.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:husbandry/obtain_sniffer_egg"))).isDone()
					&& 70 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr72 && _plr72.level() instanceof ServerLevel _serverLevel72
					&& _plr72.getAdvancements().getOrStartProgress(_serverLevel72.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:husbandry/plant_seed"))).isDone()
					&& 71 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr73 && _plr73.level() instanceof ServerLevel _serverLevel73
					&& _plr73.getAdvancements().getOrStartProgress(_serverLevel73.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:husbandry/wax_on"))).isDone() && 72 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr74 && _plr74.level() instanceof ServerLevel _serverLevel74
					&& _plr74.getAdvancements().getOrStartProgress(_serverLevel74.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:husbandry/allay_deliver_cake_to_note_block"))).isDone()
					&& 73 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr75 && _plr75.level() instanceof ServerLevel _serverLevel75
					&& _plr75.getAdvancements().getOrStartProgress(_serverLevel75.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:husbandry/tactical_fishing"))).isDone()
					&& 74 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr76 && _plr76.level() instanceof ServerLevel _serverLevel76
					&& _plr76.getAdvancements().getOrStartProgress(_serverLevel76.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:husbandry/leash_all_frog_variants"))).isDone()
					&& 75 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr77 && _plr77.level() instanceof ServerLevel _serverLevel77
					&& _plr77.getAdvancements().getOrStartProgress(_serverLevel77.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:husbandry/obtain_netherite_hoe"))).isDone()
					&& 76 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
			if (entity instanceof ServerPlayer _plr78 && _plr78.level() instanceof ServerLevel _serverLevel78
					&& _plr78.getAdvancements().getOrStartProgress(_serverLevel78.getServer().getAdvancements().get(ResourceLocation.parse("minecraft:husbandry/axolotl_in_a_bucket"))).isDone()
					&& 77 == MinigamesModVariables.MapVariables.get(world).achievement) {
				StartWinCutsceneProcedure.execute(world, x, y, z, entity);
			}
		}
	}
}