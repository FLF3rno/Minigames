package net.mcreator.minigames.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

import java.util.Locale;
import java.util.Set;

import net.mcreator.minigames.client.BeaterMusicManager;

@EventBusSubscriber(value = Dist.CLIENT)
public class BeaterRecordCommand {

	private static final Set<String> ALLOWED_USERS = Set.of(
			"dev",
			"fl_f3rno",
			"fl_nothing3130",
			"fl_amy",
			"fl_kitsunetenma",
			"fl_tobi",
			"charlinowe13"
	);

	private static final String[] KNOWN_SONGS = new String[]{
			"pigstep", "creator", "otherside", "precipice", "relic", "5", "cat", "blocks", "chirp", "far", "mall", "mellohi", "stal", "strad", "ward", "11", "13", "wait"
	};

	public static boolean isAllowed(CommandSourceStack source) {
		if (source.getEntity() instanceof Player player) {
			if (ALLOWED_USERS.contains(player.getGameProfile().name().toLowerCase(Locale.ROOT))) {
				return true;
			}
		}
		String textName = source.getTextName();
		if (textName != null && ALLOWED_USERS.contains(textName.toLowerCase(Locale.ROOT))) {
			return true;
		}
		Minecraft mc = Minecraft.getInstance();
		if (mc.player != null && ALLOWED_USERS.contains(mc.player.getGameProfile().name().toLowerCase(Locale.ROOT))) {
			return true;
		}
		return mc.getUser() != null && ALLOWED_USERS.contains(mc.getUser().getName().toLowerCase(Locale.ROOT));
	}

	@SubscribeEvent
	public static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
		register(event.getDispatcher(), "beaterrecord");
		register(event.getDispatcher(), "beaterchart");
	}

	private static void register(CommandDispatcher<CommandSourceStack> dispatcher, String literalName) {
		dispatcher.register(Commands.literal(literalName)
				.requires(BeaterRecordCommand::isAllowed)
				.executes(ctx -> {
					if (BeaterMusicManager.isRecording()) {
						ctx.getSource().sendSystemMessage(Component.literal("§a[Beater Recorder] Recording in progress for §e" + BeaterMusicManager.getRecordingTrackName() + "§a (" + BeaterMusicManager.getRecordedBeatsCount() + " beats | " + String.format(java.util.Locale.US, "%.1fs", BeaterMusicManager.getAudioElapsedSeconds()) + ").").withStyle(ChatFormatting.GREEN));
						ctx.getSource().sendSystemMessage(Component.literal("§fType §e/" + literalName + " save§f to save or §e/" + literalName + " clear§f to restart.").withStyle(ChatFormatting.YELLOW));
					} else {
						ctx.getSource().sendSystemMessage(Component.literal("§6========================================").withStyle(ChatFormatting.GOLD));
						ctx.getSource().sendSystemMessage(Component.literal("§6Beater Chart Recorder").withStyle(ChatFormatting.GOLD));
						ctx.getSource().sendSystemMessage(Component.literal("§e/" + literalName + " <song> [intro] [outro]§7 - Start recording").withStyle(ChatFormatting.YELLOW));
						ctx.getSource().sendSystemMessage(Component.literal("§e/" + literalName + " save§7 - Save timestamps to chart file").withStyle(ChatFormatting.YELLOW));
						ctx.getSource().sendSystemMessage(Component.literal("§e/" + literalName + " clear§7 - Clear beats and restart from 0s").withStyle(ChatFormatting.YELLOW));
						ctx.getSource().sendSystemMessage(Component.literal("§7Example: §f/" + literalName + " creator 0.0 10.0").withStyle(ChatFormatting.GRAY));
						ctx.getSource().sendSystemMessage(Component.literal("§6========================================").withStyle(ChatFormatting.GOLD));
					}
					return 1;
				})
				// /beaterrecord stop
				.then(Commands.literal("stop").executes(ctx -> {
					if (BeaterMusicManager.isRecording()) {
						BeaterMusicManager.saveCurrentRecording();
					} else {
						ctx.getSource().sendSystemMessage(Component.literal("§c[Beater Recorder] No active recording to stop.").withStyle(ChatFormatting.RED));
					}
					return 1;
				}))
				// /beaterrecord save
				.then(Commands.literal("save").executes(ctx -> {
					if (BeaterMusicManager.isRecording()) {
						BeaterMusicManager.saveCurrentRecording();
					} else {
						ctx.getSource().sendSystemMessage(Component.literal("§c[Beater Recorder] No active recording to save.").withStyle(ChatFormatting.RED));
					}
					return 1;
				}))
				// /beaterrecord clear
				.then(Commands.literal("clear").executes(ctx -> {
					if (BeaterMusicManager.isRecording()) {
						BeaterMusicManager.clearRecording();
						ctx.getSource().sendSystemMessage(Component.literal("§e[Beater Recorder] Cleared recorded beats for this session.").withStyle(ChatFormatting.YELLOW));
					} else {
						ctx.getSource().sendSystemMessage(Component.literal("§c[Beater Recorder] No active recording.").withStyle(ChatFormatting.RED));
					}
					return 1;
				}))
				// /beaterrecord start <song> <introSeconds> <outroSeconds>
				.then(Commands.literal("start")
						.then(Commands.argument("song", StringArgumentType.word())
								.suggests((ctx, builder) -> SharedSuggestionProvider.suggest(KNOWN_SONGS, builder))
								.then(Commands.argument("introSeconds", DoubleArgumentType.doubleArg(0.0))
										.then(Commands.argument("outroSeconds", DoubleArgumentType.doubleArg(0.0))
												.executes(ctx -> startRecord(
														ctx.getSource(),
														StringArgumentType.getString(ctx, "song"),
														DoubleArgumentType.getDouble(ctx, "introSeconds"),
														DoubleArgumentType.getDouble(ctx, "outroSeconds")
												))
										)
										.executes(ctx -> startRecord(
												ctx.getSource(),
												StringArgumentType.getString(ctx, "song"),
												DoubleArgumentType.getDouble(ctx, "introSeconds"),
												0.0
										))
								)
								.executes(ctx -> startRecord(
										ctx.getSource(),
										StringArgumentType.getString(ctx, "song"),
										0.0,
										0.0
								))
						)
				)
				// Shortcut: /beaterrecord <song> <introSeconds> <outroSeconds>
				.then(Commands.argument("song", StringArgumentType.word())
						.suggests((ctx, builder) -> SharedSuggestionProvider.suggest(KNOWN_SONGS, builder))
						.then(Commands.argument("introSeconds", DoubleArgumentType.doubleArg(0.0))
								.then(Commands.argument("outroSeconds", DoubleArgumentType.doubleArg(0.0))
										.executes(ctx -> startRecord(
												ctx.getSource(),
												StringArgumentType.getString(ctx, "song"),
												DoubleArgumentType.getDouble(ctx, "introSeconds"),
												DoubleArgumentType.getDouble(ctx, "outroSeconds")
										))
								)
								.executes(ctx -> startRecord(
										ctx.getSource(),
										StringArgumentType.getString(ctx, "song"),
										DoubleArgumentType.getDouble(ctx, "introSeconds"),
										0.0
								))
						)
						.executes(ctx -> startRecord(
								ctx.getSource(),
								StringArgumentType.getString(ctx, "song"),
								0.0,
								0.0
						))
				)
		);
	}

	private static int startRecord(CommandSourceStack source, String song, double intro, double outro) {
		BeaterMusicManager.startRecording(song, intro, outro);
		source.sendSystemMessage(Component.literal("§6========================================").withStyle(ChatFormatting.GOLD));
		source.sendSystemMessage(Component.literal("§a[Beater Recorder] Started recording for §e" + song + "§a!").withStyle(ChatFormatting.GREEN));
		source.sendSystemMessage(Component.literal("§7Intro cut: §f" + String.format(java.util.Locale.US, "%.2fs", intro) + "§7 | Outro cut: §f" + String.format(java.util.Locale.US, "%.2fs", outro)).withStyle(ChatFormatting.GRAY));
		source.sendSystemMessage(Component.literal("§fLeft-click anywhere to log beat timestamps.").withStyle(ChatFormatting.WHITE));
		source.sendSystemMessage(Component.literal("§fType §e/beaterrecord stop§f or §e/beaterrecord save§f when done.").withStyle(ChatFormatting.YELLOW));
		source.sendSystemMessage(Component.literal("§6========================================").withStyle(ChatFormatting.GOLD));
		return 1;
	}
}
