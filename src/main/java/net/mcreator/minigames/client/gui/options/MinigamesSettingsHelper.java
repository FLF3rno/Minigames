package net.mcreator.minigames.client.gui.options;

import net.mcreator.minigames.init.MinigamesModKeyMappings;
import net.mcreator.minigames.network.MinigamesModVariables;
import net.mcreator.minigames.network.MinigamesSettingSyncMessage;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MinigamesSettingsHelper {
	@FunctionalInterface
	public interface BooleanApplier {
		void apply(boolean value, Level level, Player player);
	}

	@FunctionalInterface
	public interface DoubleApplier {
		void apply(double value, Level level, Player player);
	}

	public interface SettingRow {
		default void applyToWorld(Level level, Player player) {
		}
	}

	public static class HeaderRow implements SettingRow {
		private final Component text;
		private final boolean centered;
		private final Identifier leftTexture;
		private final Identifier rightTexture;
		private final int iconWidth;
		private final int iconHeight;
		private final int texWidth;
		private final int texHeight;

		public HeaderRow(Component text, boolean centered) {
			this(text, centered, null, null, 16, 16, 16, 16);
		}

		public HeaderRow(Component text, boolean centered, Identifier texture) {
			this(text, centered, texture, texture, 16, 16, 16, 16);
		}

		public HeaderRow(Component text, boolean centered, Identifier texture, int iconWidth, int iconHeight) {
			this(text, centered, texture, texture, iconWidth, iconHeight, iconWidth, iconHeight);
		}

		public HeaderRow(Component text, boolean centered, Identifier leftTexture, Identifier rightTexture) {
			this(text, centered, leftTexture, rightTexture, 16, 16, 16, 16);
		}

		public HeaderRow(Component text, boolean centered, Identifier leftTexture, Identifier rightTexture, int iconWidth, int iconHeight) {
			this(text, centered, leftTexture, rightTexture, iconWidth, iconHeight, iconWidth, iconHeight);
		}

		public HeaderRow(Component text, boolean centered, Identifier leftTexture, Identifier rightTexture, int iconWidth, int iconHeight, int texWidth, int texHeight) {
			this.text = text;
			this.centered = centered;
			this.leftTexture = leftTexture;
			this.rightTexture = rightTexture;
			this.iconWidth = iconWidth;
			this.iconHeight = iconHeight;
			this.texWidth = texWidth > 0 ? texWidth : iconWidth;
			this.texHeight = texHeight > 0 ? texHeight : iconHeight;
		}

		public Component getText() {
			return text;
		}

		public boolean isCentered() {
			return centered;
		}

		public Identifier getLeftTexture() {
			return leftTexture;
		}

		public Identifier getRightTexture() {
			return rightTexture;
		}

		public int getIconWidth() {
			return iconWidth;
		}

		public int getIconHeight() {
			return iconHeight;
		}

		public int getTexWidth() {
			return texWidth;
		}

		public int getTexHeight() {
			return texHeight;
		}
	}

	public static class BooleanSettingRow implements SettingRow {
		private final String key;
		private final Component label;
		private final boolean defaultValue;
		private final Function<Boolean, Component> displayMapper;
		private final BooleanApplier applier;

		public BooleanSettingRow(String key, Component label, boolean defaultValue, Function<Boolean, Component> displayMapper, BooleanApplier applier) {
			this.key = key;
			this.label = label;
			this.defaultValue = defaultValue;
			this.displayMapper = displayMapper;
			this.applier = applier;
		}

		public String getKey() {
			return key;
		}

		public Component getLabel() {
			return label;
		}

		public boolean getDefaultValue() {
			return defaultValue;
		}

		public boolean getValue() {
			return MinigamesSettingsConfig.getBoolean(key, defaultValue);
		}

		public void setValue(boolean value) {
			MinigamesSettingsConfig.setBoolean(key, value);
			if (FMLEnvironment.getDist().isClient()) {
				net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
				if (mc.level != null && applier != null) {
					applier.apply(value, mc.level, mc.player);
				}
				if (mc.getConnection() != null) {
					ClientPacketDistributor.sendToServer(new MinigamesSettingSyncMessage(key, 0.0, value, true));
				}
			}
		}

		public void toggle() {
			setValue(!getValue());
		}

		public Component getButtonMessage() {
			return displayMapper.apply(getValue());
		}

		@Override
		public void applyToWorld(Level level, Player player) {
			if (applier != null) {
				applier.apply(getValue(), level, player);
			}
		}
	}

	public static class DoubleSettingRow implements SettingRow {
		private final String key;
		private final Component label;
		private final double defaultValue;
		private final double min;
		private final double max;
		private final double step;
		private final Function<Double, Component> displayMapper;
		private final DoubleApplier applier;

		public DoubleSettingRow(String key, Component label, double defaultValue, double min, double max, double step, Function<Double, Component> displayMapper, DoubleApplier applier) {
			this.key = key;
			this.label = label;
			this.defaultValue = defaultValue;
			this.min = min;
			this.max = max;
			this.step = step;
			this.displayMapper = displayMapper;
			this.applier = applier;
		}

		public String getKey() {
			return key;
		}

		public Component getLabel() {
			return label;
		}

		public double getDefaultValue() {
			return defaultValue;
		}

		public double getMin() {
			return min;
		}

		public double getMax() {
			return max;
		}

		public double getStep() {
			return step;
		}

		public double getValue() {
			return MinigamesSettingsConfig.getDouble(key, defaultValue);
		}

		public void setValue(double value) {
			double clamped = Math.max(min, Math.min(max, value));
			MinigamesSettingsConfig.setDouble(key, clamped);
			if (FMLEnvironment.getDist().isClient()) {
				net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
				if (mc.level != null && applier != null) {
					applier.apply(clamped, mc.level, mc.player);
				}
				if (mc.getConnection() != null) {
					ClientPacketDistributor.sendToServer(new MinigamesSettingSyncMessage(key, clamped, false, false));
				}
			}
		}

		public void cycle() {
			double current = getValue();
			double next = current + step;
			if (next > max + 0.0001) {
				next = min;
			}
			setValue(next);
		}

		public Component getButtonMessage() {
			return displayMapper.apply(getValue());
		}

		@Override
		public void applyToWorld(Level level, Player player) {
			if (applier != null) {
				applier.apply(getValue(), level, player);
			}
		}
	}

	public static class DoubleSliderSettingRow implements SettingRow {
		private final String key;
		private final Component label;
		private final double defaultValue;
		private final double min;
		private final double max;
		private final double step;
		private final Function<Double, Component> displayMapper;
		private final DoubleApplier applier;

		public DoubleSliderSettingRow(String key, Component label, double defaultValue, double min, double max, double step, Function<Double, Component> displayMapper, DoubleApplier applier) {
			this.key = key;
			this.label = label;
			this.defaultValue = defaultValue;
			this.min = min;
			this.max = max;
			this.step = step;
			this.displayMapper = displayMapper;
			this.applier = applier;
		}

		public String getKey() {
			return key;
		}

		public Component getLabel() {
			return label;
		}

		public double getDefaultValue() {
			return defaultValue;
		}

		public double getMin() {
			return min;
		}

		public double getMax() {
			return max;
		}

		public double getStep() {
			return step;
		}

		public double getValue() {
			return MinigamesSettingsConfig.getDouble(key, defaultValue);
		}

		public void setValue(double value) {
			double clamped = Math.max(min, Math.min(max, value));
			if (step > 0) {
				clamped = Math.round((clamped - min) / step) * step + min;
				clamped = Math.max(min, Math.min(max, clamped));
			}
			MinigamesSettingsConfig.setDouble(key, clamped);
			if (FMLEnvironment.getDist().isClient()) {
				net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
				if (mc.level != null && applier != null) {
					applier.apply(clamped, mc.level, mc.player);
				}
				if (mc.getConnection() != null) {
					ClientPacketDistributor.sendToServer(new MinigamesSettingSyncMessage(key, clamped, false, false));
				}
			}
		}

		public Component getDisplayMessage(double val) {
			return displayMapper.apply(val);
		}

		public Component getButtonMessage() {
			return displayMapper.apply(getValue());
		}

		@Override
		public void applyToWorld(Level level, Player player) {
			if (applier != null) {
				applier.apply(getValue(), level, player);
			}
		}
	}

	public static class KeyMappingSettingRow implements SettingRow {
		private final Component label;
		private final java.util.function.Supplier<KeyMapping> keyMappingSupplier;

		public KeyMappingSettingRow(Component label, KeyMapping keyMapping) {
			this.label = label;
			this.keyMappingSupplier = () -> keyMapping;
		}

		public KeyMappingSettingRow(Component label, java.util.function.Supplier<KeyMapping> keyMappingSupplier) {
			this.label = label;
			this.keyMappingSupplier = keyMappingSupplier;
		}

		public Component getLabel() {
			if (label != null) {
				return label;
			}
			KeyMapping km = getKeyMapping();
			return km != null ? Component.translatable(km.getName()) : Component.empty();
		}

		public KeyMapping getKeyMapping() {
			return keyMappingSupplier != null ? keyMappingSupplier.get() : null;
		}
	}

	public static final List<SettingRow> ROWS = new ArrayList<>();

	public static void addHeader(Component text, boolean centered) {
		ROWS.add(new HeaderRow(text, centered));
	}

	public static void addHeader(Component text, boolean centered, Identifier texture) {
		ROWS.add(new HeaderRow(text, centered, texture));
	}

	public static void addHeader(Component text, boolean centered, Identifier texture, int iconWidth, int iconHeight) {
		ROWS.add(new HeaderRow(text, centered, texture, iconWidth, iconHeight));
	}

	public static void addHeader(Component text, boolean centered, Identifier leftTexture, Identifier rightTexture) {
		ROWS.add(new HeaderRow(text, centered, leftTexture, rightTexture));
	}

	public static void addHeader(Component text, boolean centered, Identifier leftTexture, Identifier rightTexture, int iconWidth, int iconHeight) {
		ROWS.add(new HeaderRow(text, centered, leftTexture, rightTexture, iconWidth, iconHeight));
	}

	public static void addHeader(Component text, boolean centered, Identifier leftTexture, Identifier rightTexture, int iconWidth, int iconHeight, int texWidth, int texHeight) {
		ROWS.add(new HeaderRow(text, centered, leftTexture, rightTexture, iconWidth, iconHeight, texWidth, texHeight));
	}

	public static void addBoolean(String key, Component label, boolean defaultValue, Function<Boolean, Component> displayMapper, BooleanApplier applier) {
		ROWS.add(new BooleanSettingRow(key, label, defaultValue, displayMapper, applier));
	}

	public static void addDouble(String key, Component label, double defaultValue, double min, double max, double step, Function<Double, Component> displayMapper, DoubleApplier applier) {
		ROWS.add(new DoubleSettingRow(key, label, defaultValue, min, max, step, displayMapper, applier));
	}

	public static void addDoubleSlider(String key, Component label, double defaultValue, double min, double max, double step, Function<Double, Component> displayMapper, DoubleApplier applier) {
		ROWS.add(new DoubleSliderSettingRow(key, label, defaultValue, min, max, step, displayMapper, applier));
	}

	public static void addKeyMapping(Component label, KeyMapping keyMapping) {
		ROWS.add(new KeyMappingSettingRow(label, keyMapping));
	}

	public static void addKeyMapping(KeyMapping keyMapping) {
		ROWS.add(new KeyMappingSettingRow(null, keyMapping));
	}

	public static void addKeyMapping(Component label, java.util.function.Supplier<KeyMapping> keyMappingSupplier) {
		ROWS.add(new KeyMappingSettingRow(label, keyMappingSupplier));
	}

	public static void addKeyMapping(java.util.function.Supplier<KeyMapping> keyMappingSupplier) {
		ROWS.add(new KeyMappingSettingRow(null, keyMappingSupplier));
	}

	public static void syncToWorld(Level level, Player player) {
		for (SettingRow row : ROWS) {
			row.applyToWorld(level, player);
		}
	}

	public static void applySettingDirectly(String key, double doubleVal, boolean boolVal, boolean isBoolean, Level level, Player player) {
		for (SettingRow row : ROWS) {
			if (isBoolean && row instanceof BooleanSettingRow bRow && bRow.getKey().equals(key)) {
				if (bRow.applier != null) {
					bRow.applier.apply(boolVal, level, player);
				}
				return;
			} else if (!isBoolean && row instanceof DoubleSettingRow dRow && dRow.getKey().equals(key)) {
				if (dRow.applier != null) {
					dRow.applier.apply(doubleVal, level, player);
				}
				return;
			} else if (!isBoolean && row instanceof DoubleSliderSettingRow dsRow && dsRow.getKey().equals(key)) {
				if (dsRow.applier != null) {
					dsRow.applier.apply(doubleVal, level, player);
				}
				return;
			}
		}
	}

	public static void syncClientToServer() {
		if (!FMLEnvironment.getDist().isClient()) {
			return;
		}
		Minecraft mc = Minecraft.getInstance();
		if (mc.getConnection() == null) {
			return;
		}
		for (SettingRow row : ROWS) {
			if (row instanceof BooleanSettingRow bRow) {
				ClientPacketDistributor.sendToServer(new MinigamesSettingSyncMessage(bRow.getKey(), 0.0, bRow.getValue(), true));
			} else if (row instanceof DoubleSettingRow dRow) {
				ClientPacketDistributor.sendToServer(new MinigamesSettingSyncMessage(dRow.getKey(), dRow.getValue(), false, false));
			} else if (row instanceof DoubleSliderSettingRow dsRow) {
				ClientPacketDistributor.sendToServer(new MinigamesSettingSyncMessage(dsRow.getKey(), dsRow.getValue(), false, false));
			}
		}
	}

	static {
		Identifier generalTexture = Identifier.fromNamespaceAndPath("minigames", "textures/item/compass_16.png");
		addHeader(Component.literal("General"), true, generalTexture, 16, 16);

		MinigamesSettingsHelper.addKeyMapping(MinigamesModKeyMappings.VOTE_YES);
		MinigamesSettingsHelper.addKeyMapping(MinigamesModKeyMappings.VOTE_NO);

		addDoubleSlider("timerScale", Component.literal("Timer Scale"), 2.5, 0.5, 5.0, 0.5,
				val -> Component.literal(String.format(java.util.Locale.ROOT, "%.1fx", val)),
				(val, level, player) -> {
					if (player != null) {
						MinigamesModVariables.PlayerVariables vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);
						vars.timerScale = val;
						vars.markSyncDirty();
					}
				}
		);

		Identifier achievementRunIcon = Identifier.fromNamespaceAndPath("minigames", "textures/screens/achievementrunicon.png");
		addHeader(Component.literal("Achievement Run"), true, achievementRunIcon, 33, 13);

		addBoolean("AlwaysShowReminder", Component.literal("Always Show Reminder"), true,
			val -> switch (val ? 1 : 0) {
				case 1 -> Component.literal("Always Show");
				default -> Component.literal("Only Show in Inventory");
			},
			(val, level, player) -> {
				if (player != null) {
					MinigamesModVariables.PlayerVariables vars = player.getData(MinigamesModVariables.PLAYER_VARIABLES);
					vars.AlwaysShowReminder = val;
					vars.markSyncDirty();
				}
			}
		);

		Identifier crownHuntIcon = Identifier.fromNamespaceAndPath("minigames", "textures/screens/crownhunt.png");
		addHeader(Component.literal("Crown Hunt"), true, crownHuntIcon, 19, 19);

		Identifier spleefIcon = Identifier.fromNamespaceAndPath("minigames", "textures/screens/snowball.png");
		addHeader(Component.literal("Spleef"), true, spleefIcon, 16, 16);

		Identifier roguelikeIcon = Identifier.fromNamespaceAndPath("minigames", "textures/screens/roguelike.png");
		addHeader(Component.literal("Roguelike"), true, roguelikeIcon, 16, 16);

		addKeyMapping(net.mcreator.minigames.init.MinigamesModKeyMappings.DASH);
	}
}
