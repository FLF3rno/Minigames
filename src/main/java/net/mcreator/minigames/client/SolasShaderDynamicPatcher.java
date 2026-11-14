package net.mcreator.minigames.client;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SolasShaderDynamicPatcher {
	private static volatile boolean solasActive = false;

	public static boolean isSolasActive() {
		return solasActive;
	}

	public static void setSolasActive(boolean active) {
		solasActive = active;
	}

	public static String patchProperties(String name, String content) {
		if (content == null || name == null) {
			return content;
		}

		if (name.endsWith("shaders.properties")) {
			boolean isSolas = content.contains("SOLAS_BY_SEPTONIOUS") || content.contains("screen.OF=");
			setSolasActive(isSolas);
			if (isSolas) {
				return patchShadersProperties(content);
			}
			return content;
		}

		if (!isSolasActive()) {
			return content;
		}

		if (name.endsWith("block.properties")) {
			return patchBlockProperties(content);
		} else if (name.endsWith("item.properties")) {
			return patchItemProperties(content);
		}

		return content;
	}

	public static String patchGlslFile(Path path, String content) {
		if (path == null || content == null) {
			return content;
		}
		if (content.contains("SOLAS_BY_SEPTONIOUS")) {
			setSolasActive(true);
		}
		if (!isSolasActive()) {
			return content;
		}

		String p = path.toString().replace('\\', '/');

		if (p.endsWith("lib/common.glsl")) {
			return patchCommonGlsl(content);
		} else if (p.endsWith("lib/vx/blocklightColor.glsl")) {
			return patchBlocklightColor(content);
		} else if (p.endsWith("lib/pbr/blocks/froglights.glsl")) {
			return patchFroglightsBlock(content);
		} else if (p.endsWith("lib/pbr/hand/froglights.glsl")) {
			return patchFroglightsHand(content);
		} else if (p.endsWith("lib/pbr/items/froglights.glsl")) {
			return patchFroglightsItems(content);
		} else if (p.endsWith("programs/gbuffers_entities.glsl") || p.endsWith("gbuffers_entities.glsl")) {
			return patchGbuffersEntities(content);
		}

		return content;
	}

	public static Map<String, String> patchTranslations(Map<String, String> original) {
		Map<String, String> map = (original != null) ? new HashMap<>(original) : new HashMap<>();
		map.put("screen.RF", "Ruby Froglight (Red)");
		map.put("screen.AF", "Azure Froglight (Light Blue)");
		map.put("option.RF_R", "Red");
		map.put("option.RF_G", "Green");
		map.put("option.RF_B", "Blue");
		map.put("option.RF_I", "Intensity");
		map.put("option.AF_R", "Red");
		map.put("option.AF_G", "Green");
		map.put("option.AF_B", "Blue");
		map.put("option.AF_I", "Intensity");
		return map;
	}

	private static String patchShadersProperties(String content) {
		if (!content.contains("[RF] [AF]")) {
			content = content.replace("[PF] [LIGHT]", "[PF] [RF] [AF] [LIGHT]");
		}
		if (!content.contains("screen.RF=")) {
			Matcher m = Pattern.compile("(screen\\.PF=.*?(?:\\r?\\n))").matcher(content);
			if (m.find()) {
				String fullLine = m.group(1);
				String prefix = fullLine.contains("<empty>") ? "<empty> <empty> " : "";
				String nl = fullLine.contains("\r\n") ? "\r\n" : "\n";
				String rfLine = "screen.RF=" + prefix + "RF_R RF_G RF_B RF_I" + nl;
				String afLine = "screen.AF=" + prefix + "AF_R AF_G AF_B AF_I" + nl;
				content = content.substring(0, m.end()) + rfLine + afLine + content.substring(m.end());
			}
		}
		int slidersIdx = content.indexOf("sliders=");
		if (slidersIdx != -1) {
			String afterSliders = content.substring(slidersIdx);
			if (!afterSliders.contains("RF_R")) {
				content = content.replace("PF_I ", "PF_I RF_R RF_G RF_B RF_I AF_R AF_G AF_B AF_I ");
			}
		}
		return content;
	}

	private static String patchCommonGlsl(String content) {
		if (!content.contains("#define RF_R")) {
			String pfMarker = "#define PF_I 1.50";
			int idx = content.indexOf(pfMarker);
			if (idx != -1) {
				int lineEnd = content.indexOf('\n', idx);
				if (lineEnd != -1) {
					boolean crlf = lineEnd > 0 && content.charAt(lineEnd - 1) == '\r';
					String nl = crlf ? "\r\n" : "\n";
					String rfAfDefs = nl + nl +
							"#define RF_R 1.00 //[0.00 0.05 0.10 0.15 0.20 0.25 0.30 0.35 0.40 0.45 0.50 0.55 0.60 0.65 0.70 0.75 0.80 0.85 0.90 0.95 1.00]" + nl +
							"#define RF_G 0.10 //[0.00 0.05 0.10 0.15 0.20 0.25 0.30 0.35 0.40 0.45 0.50 0.55 0.60 0.65 0.70 0.75 0.80 0.85 0.90 0.95 1.00]" + nl +
							"#define RF_B 0.10 //[0.00 0.05 0.10 0.15 0.20 0.25 0.30 0.35 0.40 0.45 0.50 0.55 0.60 0.65 0.70 0.75 0.80 0.85 0.90 0.95 1.00]" + nl +
							"#define RF_I 1.50 //[0.00 0.05 0.10 0.15 0.20 0.25 0.30 0.35 0.40 0.45 0.50 0.55 0.60 0.65 0.70 0.75 0.80 0.85 0.90 0.95 1.00 1.05 1.10 1.15 1.20 1.25 1.30 1.35 1.40 1.45 1.50 1.55 1.60 1.65 1.70 1.75 1.80 1.85 1.90 1.95 2.00 2.05 2.10 2.15 2.20 2.25 2.30 2.35 2.40 2.45 2.50 2.55 2.60 2.65 2.70 2.75 2.80 2.85 2.90 2.95 3.00 3.05 3.10 3.15 3.20 3.25 3.30 3.35 3.40 3.45 3.50 3.55 3.60 3.65 3.70 3.75 3.80 3.85 3.90 3.95 4.00 4.05 4.10 4.15 4.20 4.25 4.30 4.35 4.40 4.45 4.50 4.55 4.60 4.65 4.70 4.75 4.80 4.85 4.90 4.95 5.00 5.05 5.10 5.15 5.20 5.25 5.30 5.35 5.40 5.45 5.50 5.55 5.60 5.65 5.70 5.75 5.80 5.85 5.90 5.95 6.00]" + nl + nl +
							"#define AF_R 0.10 //[0.00 0.05 0.10 0.15 0.20 0.25 0.30 0.35 0.40 0.45 0.50 0.55 0.60 0.65 0.70 0.75 0.80 0.85 0.90 0.95 1.00]" + nl +
							"#define AF_G 0.60 //[0.00 0.05 0.10 0.15 0.20 0.25 0.30 0.35 0.40 0.45 0.50 0.55 0.60 0.65 0.70 0.75 0.80 0.85 0.90 0.95 1.00]" + nl +
							"#define AF_B 1.00 //[0.00 0.05 0.10 0.15 0.20 0.25 0.30 0.35 0.40 0.45 0.50 0.55 0.60 0.65 0.70 0.75 0.80 0.85 0.90 0.95 1.00]" + nl +
							"#define AF_I 1.50 //[0.00 0.05 0.10 0.15 0.20 0.25 0.30 0.35 0.40 0.45 0.50 0.55 0.60 0.65 0.70 0.75 0.80 0.85 0.90 0.95 1.00 1.05 1.10 1.15 1.20 1.25 1.30 1.35 1.40 1.45 1.50 1.55 1.60 1.65 1.70 1.75 1.80 1.85 1.90 1.95 2.00 2.05 2.10 2.15 2.20 2.25 2.30 2.35 2.40 2.45 2.50 2.55 2.60 2.65 2.70 2.75 2.80 2.85 2.90 2.95 3.00 3.05 3.10 3.15 3.20 3.25 3.30 3.35 3.40 3.45 3.50 3.55 3.60 3.65 3.70 3.75 3.80 3.85 3.90 3.95 4.00 4.05 4.10 4.15 4.20 4.25 4.30 4.35 4.40 4.45 4.50 4.55 4.60 4.65 4.70 4.75 4.80 4.85 4.90 4.95 5.00 5.05 5.10 5.15 5.20 5.25 5.30 5.35 5.40 5.45 5.50 5.55 5.60 5.65 5.70 5.75 5.80 5.85 5.90 5.95 6.00]";
					content = content.substring(0, lineEnd) + rfAfDefs + content.substring(lineEnd);
				}
			}
		}

		if (!content.contains("#ifdef RF_R")) {
			Matcher m = Pattern.compile("(#ifdef PF_I(?:\\r?\\n)#endif(?:\\r?\\n))").matcher(content);
			if (m.find()) {
				String fullM = m.group(1);
				String nl = fullM.contains("\r\n") ? "\r\n" : "\n";
				String rfAfIfdef = nl +
						"#ifdef RF_R" + nl + "#endif" + nl + "#ifdef RF_G" + nl + "#endif" + nl + "#ifdef RF_B" + nl + "#endif" + nl + "#ifdef RF_I" + nl + "#endif" + nl + nl +
						"#ifdef AF_R" + nl + "#endif" + nl + "#ifdef AF_G" + nl + "#endif" + nl + "#ifdef AF_B" + nl + "#endif" + nl + "#ifdef AF_I" + nl + "#endif" + nl;
				content = content.substring(0, m.end()) + rfAfIfdef + content.substring(m.end());
			} else {
				content += "\n\n#ifdef RF_R\n#endif\n#ifdef RF_G\n#endif\n#ifdef RF_B\n#endif\n#ifdef RF_I\n#endif\n\n#ifdef AF_R\n#endif\n#ifdef AF_G\n#endif\n#ifdef AF_B\n#endif\n#ifdef AF_I\n#endif\n";
			}
		}
		return content;
	}

	private static String patchBlocklightColor(String content) {
		if (!content.contains("id == 86")) {
			Matcher m = Pattern.compile("(else if\\(id == 34\\)[^\\r\\n]*(?:\\r?\\n))").matcher(content);
			if (m.find()) {
				String fullM = m.group(1);
				String nl = fullM.contains("\r\n") ? "\r\n" : "\n";
				String add = "    //Ruby Froglight" + nl +
						"    else if(id == 86) color = vec3(RF_R, RF_G, RF_B) * RF_I;" + nl +
						"    //Azure Froglight" + nl +
						"    else if(id == 87) color = vec3(AF_R, AF_G, AF_B) * AF_I;" + nl;
				content = content.substring(0, m.end()) + add + content.substring(m.end());
			}
		}
		return content;
	}

	private static String patchFroglightsBlock(String content) {
		if (!content.contains("material == 86")) {
			content = content.replace("material >= 32 && material <= 34", "material >= 32 && material <= 34 || material == 86 || material == 87");
		}
		return content;
	}

	private static String patchFroglightsHand(String content) {
		if (!content.contains("heldItemId2 == 86")) {
			content = content.replace("heldItemId2 >= 32 && heldItemId2 <= 34", "heldItemId2 >= 32 && heldItemId2 <= 34 || heldItemId2 == 86 || heldItemId2 == 87");
		}
		return content;
	}

	private static String patchFroglightsItems(String content) {
		if (!content.contains("currentRenderedItemId == 10086")) {
			content = content.replace("currentRenderedItemId >= 10032 && currentRenderedItemId <= 10034", "currentRenderedItemId >= 10032 && currentRenderedItemId <= 10034 || currentRenderedItemId == 10086 || currentRenderedItemId == 10087");
		}
		return content;
	}

	private static String patchBlockProperties(String content) {
		content = content.replace("minigames:ruby_froglight", "");
		content = content.replace("minigames:azure_froglight", "");
		if (!content.contains("block.10086=")) {
			Matcher m = Pattern.compile("(block\\.10085=light(?:\\r?\\n))").matcher(content);
			if (m.find()) {
				String fullM = m.group(1);
				String nl = fullM.contains("\r\n") ? "\r\n" : "\n";
				String add = "block.10086=minigames:ruby_froglight" + nl + "block.10087=minigames:azure_froglight" + nl;
				content = content.substring(0, m.end()) + add + content.substring(m.end());
			}
		}
		return content;
	}

	private static String patchGbuffersEntities(String content) {
		return content;
	}

	private static String patchItemProperties(String content) {
		content = content.replace("item.86=minigames:ruby_froglight\r\n", "");
		content = content.replace("item.86=minigames:ruby_froglight\n", "");
		content = content.replace("item.87=minigames:azure_froglight\r\n", "");
		content = content.replace("item.87=minigames:azure_froglight\n", "");
		if (!content.contains("item.86=")) {
			if (content.contains("item.83=")) {
				int idx = content.indexOf("item.83=");
				int lineEnd = content.indexOf('\n', idx);
				if (lineEnd != -1) {
					boolean crlf = lineEnd > 0 && content.charAt(lineEnd - 1) == '\r';
					String nl = crlf ? "\r\n" : "\n";
					content = content.substring(0, lineEnd + 1) + "item.86=minigames:ruby_froglight" + nl + "item.87=minigames:azure_froglight" + nl + content.substring(lineEnd + 1);
				}
			} else if (content.contains("item.34=")) {
				int idx = content.indexOf("item.34=");
				int lineEnd = content.indexOf('\n', idx);
				if (lineEnd != -1) {
					boolean crlf = lineEnd > 0 && content.charAt(lineEnd - 1) == '\r';
					String nl = crlf ? "\r\n" : "\n";
					content = content.substring(0, lineEnd + 1) + "item.86=minigames:ruby_froglight" + nl + "item.87=minigames:azure_froglight" + nl + content.substring(lineEnd + 1);
				}
			}
		}
		return content;
	}
}
