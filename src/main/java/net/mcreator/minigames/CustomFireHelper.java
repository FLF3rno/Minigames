package net.mcreator.minigames;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.mcreator.minigames.network.CustomFireStateSyncMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CustomFireHelper {
	public static final int SOUL_FIRE_COLOR = 0xFF34C6E7;
	public static final ContextKey<Integer> FIRE_COLOR_KEY = new ContextKey<>(Identifier.fromNamespaceAndPath(MinigamesMod.MODID, "fire_color"));
	public static final TagKey<EntityType<?>> DUNGEON_TAG = TagKey.create(Registries.ENTITY_TYPE, Identifier.parse("minigames:dungeon"));

	public record FireData(int entityId, UUID sourceUuid, int sourceId, double damage, int color) {}

	private static final Map<Integer, FireData> SERVER_FIRE_DATA = new ConcurrentHashMap<>();
	private static final Map<Integer, Integer> CLIENT_FIRE_COLORS = new ConcurrentHashMap<>();
	private static final ThreadLocal<Boolean> HANDLING_CUSTOM_DAMAGE = ThreadLocal.withInitial(() -> false);

	public static boolean isDungeonEntity(LivingEntity living) {
		if (living == null || living.level() == null) {
			return false;
		}
		if (living.is(DUNGEON_TAG)) {
			return true;
		}
		PlayerTeam team = living.level().getScoreboard().getPlayersTeam(living instanceof Player player ? player.getName().getString() : living.getStringUUID());
		return team != null && "dungeon_mobs".equals(team.getName());
	}

	public static boolean isHandlingCustomDamage() {
		return HANDLING_CUSTOM_DAMAGE.get();
	}

	public static void setHandlingCustomDamage(boolean handling) {
		HANDLING_CUSTOM_DAMAGE.set(handling);
	}

	public static void handleClientSync(int entityId, int color) {
		if (color == 0) {
			CLIENT_FIRE_COLORS.remove(entityId);
		} else {
			CLIENT_FIRE_COLORS.put(entityId, color);
		}
	}

	public static void clearClientFire(int entityId) {
		CLIENT_FIRE_COLORS.remove(entityId);
	}

	public static boolean isTouchingSoulFire(Entity entity) {
		if (entity == null || entity.level() == null) {
			return false;
		}
		AABB box = entity.getBoundingBox().inflate(0.1, 0.2, 0.1);
		int minX = Mth.floor(box.minX);
		int maxX = Mth.floor(box.maxX);
		int minY = Mth.floor(box.minY);
		int maxY = Mth.floor(box.maxY);
		int minZ = Mth.floor(box.minZ);
		int maxZ = Mth.floor(box.maxZ);
		BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();
		Level level = entity.level();
		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				for (int z = minZ; z <= maxZ; z++) {
					mpos.set(x, y, z);
					BlockState state = level.getBlockState(mpos);
					if (state.is(Blocks.SOUL_FIRE) || state.is(Blocks.SOUL_CAMPFIRE)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static void setOnFire(LevelAccessor world, Entity entity, Entity sourceentity, double damage, double ticks, String color) {
		if (entity == null) {
			return;
		}
		if (ticks <= 0) {
			entity.clearFire();
			clearFire(entity);
			return;
		}
		int fireTicks = (int) Math.max(1, Math.round(ticks));
		if (entity instanceof LivingEntity living) {
			fireTicks = Mth.ceil(fireTicks * living.getAttributeValue(Attributes.BURNING_TIME));
		}
		entity.setRemainingFireTicks(fireTicks);
		entity.clearFreeze();
		int parsedColor = parseColor(color);
		UUID sourceUuid = sourceentity != null ? sourceentity.getUUID() : null;
		int sourceId = sourceentity != null ? sourceentity.getId() : -1;
		CompoundTag data = entity.getPersistentData();
		data.putDouble("CustomFire_Damage", damage);
		data.putInt("CustomFire_Color", parsedColor);
		if (sourceUuid != null) {
			data.putString("CustomFire_SourceUUID", sourceUuid.toString());
			data.putInt("CustomFire_SourceId", sourceId);
		}
		SERVER_FIRE_DATA.put(entity.getId(), new FireData(entity.getId(), sourceUuid, sourceId, damage, parsedColor));
		if (world instanceof ServerLevel serverLevel) {
			CustomFireStateSyncMessage msg = new CustomFireStateSyncMessage(entity.getId(), parsedColor);
			PacketDistributor.sendToPlayersTrackingEntity(entity, msg);
			if (entity instanceof ServerPlayer serverPlayer) {
				PacketDistributor.sendToPlayer(serverPlayer, msg);
			}
		} else {
			CLIENT_FIRE_COLORS.put(entity.getId(), parsedColor);
		}
	}

	public static void setSoulFire(LevelAccessor world, Entity entity, Entity sourceentity, double damage, double ticks) {
		setOnFire(world, entity, sourceentity, damage, ticks, "soul_fire");
	}

	public static void setSoulFire(LevelAccessor world, Entity entity, double ticks) {
		setOnFire(world, entity, null, 2.0, ticks, "soul_fire");
	}

	public static void setSoulFireColor(Entity entity) {
		if (entity == null) {
			return;
		}
		int entityId = entity.getId();
		CompoundTag data = entity.getPersistentData();
		data.putInt("CustomFire_Color", SOUL_FIRE_COLOR);
		FireData existing = SERVER_FIRE_DATA.get(entityId);
		double damage = existing != null ? existing.damage() : 2.0;
		UUID sourceUuid = existing != null ? existing.sourceUuid() : null;
		int sourceId = existing != null ? existing.sourceId() : -1;
		SERVER_FIRE_DATA.put(entityId, new FireData(entityId, sourceUuid, sourceId, damage, SOUL_FIRE_COLOR));
		if (entity.level() instanceof ServerLevel) {
			CustomFireStateSyncMessage msg = new CustomFireStateSyncMessage(entityId, SOUL_FIRE_COLOR);
			PacketDistributor.sendToPlayersTrackingEntity(entity, msg);
			if (entity instanceof ServerPlayer serverPlayer) {
				PacketDistributor.sendToPlayer(serverPlayer, msg);
			}
		} else {
			CLIENT_FIRE_COLORS.put(entityId, SOUL_FIRE_COLOR);
		}
	}

	public static void clearFire(Entity entity) {
		if (entity == null) {
			return;
		}
		int entityId = entity.getId();
		CompoundTag data = entity.getPersistentData();
		data.remove("CustomFire_Damage");
		data.remove("CustomFire_Color");
		data.remove("CustomFire_SourceUUID");
		data.remove("CustomFire_SourceId");
		boolean removed = SERVER_FIRE_DATA.remove(entityId) != null;
		CLIENT_FIRE_COLORS.remove(entityId);
		if (removed && entity.level() instanceof ServerLevel) {
			CustomFireStateSyncMessage msg = new CustomFireStateSyncMessage(entityId, 0);
			PacketDistributor.sendToPlayersTrackingEntity(entity, msg);
			if (entity instanceof ServerPlayer serverPlayer) {
				PacketDistributor.sendToPlayer(serverPlayer, msg);
			}
		}
	}

	public static FireData getFireData(int entityId) {
		return SERVER_FIRE_DATA.get(entityId);
	}

	public static int getFireColor(Entity entity) {
		if (entity == null) {
			return 0;
		}
		int entityId = entity.getId();
		if (entity.level().isClientSide()) {
			if (isTouchingSoulFire(entity)) {
				CLIENT_FIRE_COLORS.put(entityId, SOUL_FIRE_COLOR);
				return SOUL_FIRE_COLOR;
			}
			Integer clientColor = CLIENT_FIRE_COLORS.get(entityId);
			if (clientColor != null && clientColor != 0) {
				return clientColor;
			}
			int persistentColor = entity.getPersistentData().getIntOr("CustomFire_Color", 0);
			if (persistentColor != 0) {
				CLIENT_FIRE_COLORS.put(entityId, persistentColor);
				return persistentColor;
			}
			return 0;
		}
		if (isTouchingSoulFire(entity)) {
			setSoulFireColor(entity);
			return SOUL_FIRE_COLOR;
		}
		FireData serverData = SERVER_FIRE_DATA.get(entityId);
		if (serverData != null) {
			return serverData.color();
		}
		return entity.getPersistentData().getIntOr("CustomFire_Color", 0);
	}

	public static boolean isOnFire(Entity entity) {
		return entity != null && (entity.isOnFire() || isTouchingSoulFire(entity));
	}

	public static boolean isSoulFire(Entity entity) {
		if (!isOnFire(entity)) {
			return false;
		}
		int color = getFireColor(entity);
		return color == SOUL_FIRE_COLOR || isTouchingSoulFire(entity);
	}

	public static String getFireType(Entity entity) {
		if (!isOnFire(entity)) {
			return "NONE";
		}
		int color = getFireColor(entity);
		if (color == SOUL_FIRE_COLOR || isTouchingSoulFire(entity)) {
			return "SOUL";
		}
		if (color == 0) {
			return "NORMAL";
		}
		return "CUSTOM";
	}

	public static String getFireColorHex(Entity entity) {
		int color = getFireColor(entity);
		if (color == 0) {
			return "#FF8000";
		}
		return String.format(Locale.ROOT, "#%06X", (0xFFFFFF & color));
	}

	public static int getRemainingFireTicks(Entity entity) {
		if (entity == null) {
			return 0;
		}
		return Math.max(0, entity.getRemainingFireTicks());
	}

	public static double getRemainingFireSeconds(Entity entity) {
		if (entity == null) {
			return 0.0;
		}
		return Math.max(0, entity.getRemainingFireTicks()) / 20.0;
	}

	public static Integer findEntityFireColorAt(double x, double y, double z) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null) {
			return null;
		}
		if (mc.player != null && (mc.player.isOnFire() || isTouchingSoulFire(mc.player))) {
			double dx = mc.player.getX() - x;
			double dy = mc.player.getY() - y;
			double dz = mc.player.getZ() - z;
			if (dx * dx + dy * dy + dz * dz < 4.0) {
				int c = getFireColor(mc.player);
				if (c != 0) {
					return c;
				}
			}
		}
		AABB box = new AABB(x - 0.7, y - 0.7, z - 0.7, x + 0.7, y + 2.5, z + 0.7);
		List<Entity> list = mc.level.getEntities((Entity) null, box);
		for (Entity e : list) {
			if (e.isOnFire() || isTouchingSoulFire(e)) {
				int c = getFireColor(e);
				if (c != 0) {
					return c;
				}
			}
		}
		return null;
	}

	public static int parseColor(String color) {
		if (color == null || color.isBlank()) {
			return SOUL_FIRE_COLOR;
		}
		String clean = color.trim().toLowerCase(Locale.ROOT);
		if (clean.startsWith("#")) {
			clean = clean.substring(1);
		} else if (clean.startsWith("0x")) {
			clean = clean.substring(2);
		}
		switch (clean) {
			case "soul":
			case "soul_fire":
			case "soulfire":
			case "blue":
			case "light_blue":
			case "ice":
				return SOUL_FIRE_COLOR;
			case "cyan":
			case "aqua":
				return 0xFF00FFFF;
			case "green":
			case "lime":
				return 0xFF00FF00;
			case "dark_green":
				return 0xFF008000;
			case "red":
				return 0xFFFF2222;
			case "dark_red":
				return 0xFF8B0000;
			case "purple":
			case "violet":
				return 0xFFA020F0;
			case "magenta":
			case "pink":
				return 0xFFFF69B4;
			case "yellow":
				return 0xFFFFFF00;
			case "orange":
			case "gold":
				return 0xFFFF8000;
			case "white":
				return 0xFFFFFFFF;
			case "black":
			case "dark":
				return 0xFF222222;
			default:
				try {
					long val = Long.parseLong(clean, 16);
					if (clean.length() <= 6) {
						return (int) (0xFF000000 | val);
					}
					return (int) val;
				} catch (NumberFormatException ignored) {
					return SOUL_FIRE_COLOR;
				}
		}
	}

	public static boolean isCoolColor(int color) {
		int r = (color >> 16) & 0xFF;
		int g = (color >> 8) & 0xFF;
		int b = color & 0xFF;
		return (b > r && b > 100) || (g > r && g > 150) || (r < 100 && (g > 100 || b > 100));
	}

	public static void renderCustomEntityFlame(PoseStack.Pose pose, MultiBufferSource bufferSource, EntityRenderState state, Quaternionf rotation, AtlasManager atlasManager, int color) {
		boolean cool = isCoolColor(color);
		SpriteId id1 = cool ? Sheets.BLOCKS_MAPPER.defaultNamespaceApply("soul_fire_0") : ModelBakery.FIRE_0;
		SpriteId id2 = cool ? Sheets.BLOCKS_MAPPER.defaultNamespaceApply("soul_fire_1") : ModelBakery.FIRE_1;
		TextureAtlasSprite fire1 = atlasManager.get(id1);
		TextureAtlasSprite fire2 = atlasManager.get(id2);
		if (fire1 == null || fire2 == null) {
			fire1 = atlasManager.get(ModelBakery.FIRE_0);
			fire2 = atlasManager.get(ModelBakery.FIRE_1);
		}
		float s = state.boundingBoxWidth * 1.4F;
		pose.scale(s, s, s);
		float r = 0.5F;
		float h = state.boundingBoxHeight / s;
		float yo = 0.0F;
		pose.rotate(rotation);
		pose.translate(0.0F, 0.0F, 0.3F - (int) h * 0.02F);
		float zo = 0.0F;
		int ss = 0;
		VertexConsumer buffer = bufferSource.getBuffer(Sheets.cutoutBlockSheet());
		int vertexColor = (color == SOUL_FIRE_COLOR) ? -1 : color;
		for (int lightCoords = LightCoordsUtil.withBlock(state.lightCoords, 15); h > 0.0F; ss++) {
			TextureAtlasSprite tex = ss % 2 == 0 ? fire1 : fire2;
			float u0 = tex.getU0();
			float v0 = tex.getV0();
			float u1 = tex.getU1();
			float v1 = tex.getV1();
			if (ss / 2 % 2 == 0) {
				float tmp = u1;
				u1 = u0;
				u0 = tmp;
			}
			vertexCustom(pose, buffer, -r, -yo, zo, u1, v1, lightCoords, vertexColor);
			vertexCustom(pose, buffer, r, -yo, zo, u0, v1, lightCoords, vertexColor);
			vertexCustom(pose, buffer, r, 1.4F - yo, zo, u0, v0, lightCoords, vertexColor);
			vertexCustom(pose, buffer, -r, 1.4F - yo, zo, u1, v0, lightCoords, vertexColor);
			h -= 0.45F;
			yo -= 0.45F;
			r *= 0.9F;
			zo -= 0.03F;
		}
	}

	private static void vertexCustom(PoseStack.Pose pose, VertexConsumer buffer, float x, float y, float z, float u, float v, int lightCoords, int color) {
		buffer.addVertex(pose, x, y, z).setColor(color).setUv(u, v).setUv1(0, 10).setLight(lightCoords).setNormal(pose, 0.0F, 1.0F, 0.0F);
	}

	public static boolean isLowFire() {
		try {
			return net.mcreator.minigames.client.gui.options.MinigamesSettingsConfig.getBoolean("lowFire", true);
		} catch (Throwable ignored) {
			return true;
		}
	}

	public static void renderFirstPersonFire(PoseStack poseStack, MultiBufferSource bufferSource, SpriteGetter sprites, int color) {
		boolean cool = isCoolColor(color);
		SpriteId spriteId = cool ? Sheets.BLOCKS_MAPPER.defaultNamespaceApply("soul_fire_1") : ModelBakery.FIRE_1;
		TextureAtlasSprite sprite = sprites.get(spriteId);
		if (sprite == null) {
			sprite = sprites.get(ModelBakery.FIRE_1);
		}
		VertexConsumer builder = bufferSource.getBuffer(RenderTypes.fireScreenEffect(sprite.atlasLocation()));
		float u0 = sprite.getU0();
		float u1 = sprite.getU1();
		float v0 = sprite.getV0();
		float v1 = sprite.getV1();
		float red = (color == 0 || color == SOUL_FIRE_COLOR) ? 1.0F : (((color >> 16) & 0xFF) / 255.0F);
		float green = (color == 0 || color == SOUL_FIRE_COLOR) ? 1.0F : (((color >> 8) & 0xFF) / 255.0F);
		float blue = (color == 0 || color == SOUL_FIRE_COLOR) ? 1.0F : ((color & 0xFF) / 255.0F);
		float yOffset = isLowFire() ? -0.6F : -0.3F;
		for (int i = 0; i < 2; i++) {
			poseStack.pushPose();
			poseStack.translate(-(i * 2 - 1) * 0.24F, yOffset, 0.0F);
			poseStack.mulPose(Axis.YP.rotationDegrees((i * 2 - 1) * 10.0F));
			Matrix4f pose = poseStack.last().pose();
			builder.addVertex(pose, -0.5F, -0.5F, -0.5F).setUv(u1, v1).setColor(red, green, blue, 0.9F);
			builder.addVertex(pose, 0.5F, -0.5F, -0.5F).setUv(u0, v1).setColor(red, green, blue, 0.9F);
			builder.addVertex(pose, 0.5F, 0.5F, -0.5F).setUv(u0, v0).setColor(red, green, blue, 0.9F);
			builder.addVertex(pose, -0.5F, 0.5F, -0.5F).setUv(u1, v0).setColor(red, green, blue, 0.9F);
			poseStack.popPose();
		}
	}
}
