package net.mcreator.minigames.procedures;

public class GetAchievementProcedure {
	public static String execute(double AchievementCategory, double AchievementNumber) {
		String category = "";
		double achievementNumber = 0;
		double achievementCategory = 0;
		achievementNumber = AchievementNumber;
		achievementCategory = AchievementCategory;
		if (achievementCategory == 1) {
			category = "story/";
		} else if (achievementCategory == 2) {
			category = "nether/";
		} else if (achievementCategory == 3) {
			category = "end/";
		} else if (achievementCategory == 4) {
			category = "adventure/";
		} else if (achievementCategory == 5) {
			category = "husbandry/";
		}
		if (("story/").equals(category)) {
			if (achievementNumber == 1) {
				return category + "mine_stone";
			} else if (achievementNumber == 2) {
				return category + "upgrade_tools";
			} else if (achievementNumber == 3) {
				return category + "smelt_iron";
			} else if (achievementNumber == 4) {
				return category + "obtain_armor";
			} else if (achievementNumber == 5) {
				return category + "lava_bucket";
			} else if (achievementNumber == 6) {
				return category + "iron_tools";
			} else if (achievementNumber == 7) {
				return category + "deflect_arrow";
			} else if (achievementNumber == 8) {
				return category + "form_obsidian";
			} else if (achievementNumber == 9) {
				return category + "mine_diamond";
			} else if (achievementNumber == 10) {
				return category + "enter_the_nether";
			} else if (achievementNumber == 11) {
				return category + "shiny_gear";
			} else if (achievementNumber == 12) {
				return category + "enchant_item";
			} else if (achievementNumber == 13) {
				return category + "cure_zombie_villager";
			} else if (achievementNumber == 14) {
				return category + "follow_ender_eye";
			} else if (achievementNumber == 15) {
				return category + "enter_the_end";
			}
		} else if (("nether/").equals(category)) {
			if (achievementNumber == 1) {
				return category + "return_to_sender";
			} else if (achievementNumber == 2) {
				return category + "find_bastion";
			} else if (achievementNumber == 3) {
				return category + "obtain_ancient_debris";
			} else if (achievementNumber == 4) {
				return category + "fast_travel";
			} else if (achievementNumber == 5) {
				return category + "find_fortress";
			} else if (achievementNumber == 6) {
				return category + "obtain_crying_obsidian";
			} else if (achievementNumber == 7) {
				return category + "distract_piglin";
			} else if (achievementNumber == 8) {
				return category + "ride_strider";
			} else if (achievementNumber == 9) {
				return category + "uneasy_alliance";
			} else if (achievementNumber == 10) {
				return category + "loot_bastion";
			} else if (achievementNumber == 11) {
				return category + "netherite_armor";
			} else if (achievementNumber == 12) {
				return category + "get_wither_skull";
			} else if (achievementNumber == 13) {
				return category + "obtain_blaze_rod";
			} else if (achievementNumber == 14) {
				return category + "charge_respawn_anchor";
			} else if (achievementNumber == 15) {
				return category + "ride_strider_in_overworld_lava";
			} else if (achievementNumber == 16) {
				return category + "explore_nether";
			} else if (achievementNumber == 17) {
				return category + "summon_wither";
			} else if (achievementNumber == 18) {
				return category + "brew_potion";
			} else if (achievementNumber == 19) {
				return category + "create_beacon";
			} else if (achievementNumber == 20) {
				return category + "all_potions";
			} else if (achievementNumber == 21) {
				return category + "create_full_beacon";
			} else if (achievementNumber == 22) {
				return category + "all_effects";
			}
		} else if (("end/").equals(category)) {
			if (achievementNumber == 1) {
				return category + "kill_dragon";
			}
		} else if (("adventure/").equals(category)) {
			if (achievementNumber == 1) {
				return category + "voluntary_exile";
			} else if (achievementNumber == 2) {
				return category + "spyglass_at_parrot";
			} else if (achievementNumber == 3) {
				return category + "use_lodestone";
			} else if (achievementNumber == 4) {
				return category + "kill_a_mob";
			} else if (achievementNumber == 5) {
				return category + "read_power_of_chiseled_bookshelf";
			} else if (achievementNumber == 6) {
				return category + "trade";
			} else if (achievementNumber == 7) {
				return category + "trim_with_any_armor_pattern";
			} else if (achievementNumber == 8) {
				return category + "honey_block_slide";
			} else if (achievementNumber == 9) {
				return category + "ol_betsy";
			} else if (achievementNumber == 10) {
				return category + "lightning_rod_with_villager_no_fire";
			} else if (achievementNumber == 11) {
				return category + "fall_from_world_height";
			} else if (achievementNumber == 12) {
				return category + "salvage_sherd";
			} else if (achievementNumber == 13) {
				return category + "avoid_vibration";
			} else if (achievementNumber == 14) {
				return category + "sleep_in_bed";
			} else if (achievementNumber == 15) {
				return category + "hero_of_the_village";
			} else if (achievementNumber == 16) {
				return category + "spyglass_at_ghast";
			} else if (achievementNumber == 17) {
				return category + "throw_trident";
			} else if (achievementNumber == 18) {
				return category + "kill_mob_near_sculk_catalyst";
			} else if (achievementNumber == 19) {
				return category + "shoot_arrow";
			} else if (achievementNumber == 20) {
				return category + "kill_all_mobs";
			} else if (achievementNumber == 21) {
				return category + "totem_of_undying";
			} else if (achievementNumber == 22) {
				return category + "spear_many_mobs";
			} else if (achievementNumber == 23) {
				return category + "summon_iron_golem";
			} else if (achievementNumber == 24) {
				return category + "trade_at_world_height";
			} else if (achievementNumber == 25) {
				return category + "trim_with_all_exclusive_armor_patterns";
			} else if (achievementNumber == 26) {
				return category + "two_birds_one_arrow";
			} else if (achievementNumber == 27) {
				return category + "whos_the_pillager_now";
			} else if (achievementNumber == 28) {
				return category + "arbalistic";
			} else if (achievementNumber == 29) {
				return category + "craft_decorated_pot_using_only_sherds";
			} else if (achievementNumber == 30) {
				return category + "adventuring_time";
			} else if (achievementNumber == 31) {
				return category + "play_jukebox_in:meadows";
			} else if (achievementNumber == 32) {
				return category + "spyglass_at_dragon";
			} else if (achievementNumber == 33) {
				return category + "very_very_frightening";
			} else if (achievementNumber == 34) {
				return category + "sniper_duel";
			} else if (achievementNumber == 35) {
				return category + "bullseye";
			} else if (achievementNumber == 36) {
				return category + "minecraft_trials_edition";
			} else if (achievementNumber == 37) {
				return category + "crafters_crafting_crafters";
			} else if (achievementNumber == 38) {
				return category + "lighten_up";
			} else if (achievementNumber == 39) {
				return category + "who_needs_rockets";
			} else if (achievementNumber == 40) {
				return category + "under_lock_and_key";
			} else if (achievementNumber == 41) {
				return category + "revaulting";
			} else if (achievementNumber == 42) {
				return category + "blowback";
			} else if (achievementNumber == 43) {
				return category + "overoverkill";
			}
		} else if (("husbandry/").equals(category)) {
			if (achievementNumber == 1) {
				return category + "safely_harvest_honey";
			} else if (achievementNumber == 2) {
				return category + "place_dried_ghast_in_water";
			} else if (achievementNumber == 3) {
				return category + "breed_an_animal";
			} else if (achievementNumber == 4) {
				return category + "allay_deliver_item_to_player";
			} else if (achievementNumber == 5) {
				return category + "ride_a_boat_with_a_goat";
			} else if (achievementNumber == 6) {
				return category + "tame_an_animal";
			} else if (achievementNumber == 7) {
				return category + "make_a_sign_glow";
			} else if (achievementNumber == 8) {
				return category + "fishy_business";
			} else if (achievementNumber == 9) {
				return category + "silk_touch_nest";
			} else if (achievementNumber == 10) {
				return category + "tadpole_in_a_bucket";
			} else if (achievementNumber == 11) {
				return category + "obtain_sniffer_egg";
			} else if (achievementNumber == 12) {
				return category + "plant_seed";
			} else if (achievementNumber == 13) {
				return category + "wax_on";
			} else if (achievementNumber == 14) {
				return category + "bred_all_animals";
			} else if (achievementNumber == 15) {
				return category + "allay_deliver_cake_to_note_block";
			} else if (achievementNumber == 16) {
				return category + "complete_catalogue";
			} else if (achievementNumber == 17) {
				return category + "tactical_fishing";
			} else if (achievementNumber == 18) {
				return category + "leash_all_frog_variants";
			} else if (achievementNumber == 19) {
				return category + "feed_snifflet";
			} else if (achievementNumber == 20) {
				return category + "balanced_diet";
			} else if (achievementNumber == 21) {
				return category + "obtain_netherite_hoe";
			} else if (achievementNumber == 22) {
				return category + "wax_off";
			} else if (achievementNumber == 23) {
				return category + "axolotl_in_a_bucket";
			} else if (achievementNumber == 24) {
				return category + "froglights";
			} else if (achievementNumber == 25) {
				return category + "plant_any_sniffer_seed";
			} else if (achievementNumber == 26) {
				return category + "kill_axolotl_target";
			} else if (achievementNumber == 27) {
				return category + "good_as_new";
			}
		}
		return category + "mine";
	}
}