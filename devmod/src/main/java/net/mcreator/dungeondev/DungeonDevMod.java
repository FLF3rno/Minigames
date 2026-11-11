package net.mcreator.dungeondev;

import net.mcreator.dungeondev.commands.RoomCommands;
import net.mcreator.dungeondev.events.GameEvents;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

@Mod(DungeonDevMod.MODID)
public class DungeonDevMod {
    public static final String MODID = "dungeon_dev_mod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public DungeonDevMod() {
        LOGGER.info("Dungeon Dev Mod initializing...");

        // Register Server / Game Events
        NeoForge.EVENT_BUS.addListener(RoomCommands::onRegisterCommands);
        NeoForge.EVENT_BUS.addListener(GameEvents::onPlayerLoggedIn);

        LOGGER.info("Dungeon Dev Mod initialized successfully!");
    }
}
