package net.mcreator.minigames.client;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenericShaderSkyboxPatcher {
	private static volatile boolean gbuffersPatched = false;
	private static volatile boolean depthSamplingPatched = false;
	private static volatile boolean liveSkyPatched = false;
	private static volatile int skyboxBlockId = -1;
	private static volatile int markerColortex = -1;
	private static volatile boolean markerColortexFromBlockShader = false;
	private static volatile boolean awaitingBlockProperties = false;
	private static volatile boolean cycleCompleted = false;

	private static final int DEFAULT_SKYBOX_BLOCK_ID = 10089;
	// Fallback sentinel for colortex0 only when a shader pack has no secondary drawbuffer (DRAWBUFFERS:0)
	private static final String MARKER_COLOR0_VEC3 = "vec3(0.01569, 0.03137, 0.04706)";
	private static final String MARKER_COLOR0_VEC4 = "vec4(0.01569, 0.03137, 0.04706, 1.0)";
	// Secondary buffer marker: .r = 0.0 (zero smoothness), .g = 0.0 (zero metalness/refraction), .b = 179/255 = 0.70196 (skybox marker)
	private static final String MARKER_SEC_VEC4 = "vec4(0.0, 0.0, 0.70196, 1.0)";

	private static final String DEPTH_SAMPLERS_REGEX = "(depthtex0|depthtex1|gdepthtex|dhDepthTex|dhDepthTex0|dhDepthTex1|vxDepthTexOpaque|vxDepthTexTrans)";

	private static final Pattern BLOCK_ID_PATTERN = Pattern.compile("\\bblock\\.(\\d+)\\s*=");
	private static final Pattern DRAWBUFFERS_PATTERN = Pattern.compile("/\\*\\s*DRAWBUFFERS\\s*:\\s*([0-9A-Za-z]+)\\s*\\*/");
	private static final Pattern RENDERTARGETS_PATTERN = Pattern.compile("/\\*\\s*RENDERTARGETS\\s*:\\s*([0-9,\\s]+)\\s*\\*/");
	private static final Pattern UNIFORM_BLOCK_ENTITY_ID_PATTERN = Pattern.compile("\\buniform\\s+int\\s+blockEntityId\\s*;");
	private static final Pattern ANY_DEPTH_SAMPLER_PATTERN = Pattern.compile("\\b" + DEPTH_SAMPLERS_REGEX + "\\b");
	private static final Pattern TEXTURE2D_DEPTH_PATTERN = Pattern.compile("\\btexture2D\\s*\\(\\s*" + DEPTH_SAMPLERS_REGEX + "\\s*,");
	private static final Pattern TEXTURE_DEPTH_PATTERN = Pattern.compile("\\btexture\\s*\\(\\s*" + DEPTH_SAMPLERS_REGEX + "\\s*,");
	private static final Pattern TEXTURE2DLOD_DEPTH_PATTERN = Pattern.compile("\\btexture2DLod\\s*\\(\\s*" + DEPTH_SAMPLERS_REGEX + "\\s*,");
	private static final Pattern TEXTURELOD_DEPTH_PATTERN = Pattern.compile("\\btextureLod\\s*\\(\\s*" + DEPTH_SAMPLERS_REGEX + "\\s*,");
	private static final Pattern TEXELFETCH_DEPTH_PATTERN = Pattern.compile("\\btexelFetch\\s*\\(\\s*" + DEPTH_SAMPLERS_REGEX + "\\s*,");
	private static final Pattern INCLUDE_PATTERN = Pattern.compile("#include\\s+\"([^\"]+)\"");
	private static final Pattern MAIN_FUNC_PATTERN = Pattern.compile("\\bvoid\\s+main\\s*\\(\\s*(?:void)?\\s*\\)");
	private static final Pattern FSH_IFDEF_PATTERN = Pattern.compile("(?m)^[ \\t]*#\\s*if(?:def\\s+FSH|\\s+defined\\s*\\(?\\s*FSH\\s*\\)?)[^\\r\\n]*(?:\\r?\\n)?");
	private static final Pattern CAVE_FACTOR_DECL_PATTERN = Pattern.compile("(?m)^([ \\t]*float\\s+caveFactor\\s*=[^;\\r\\n]+;)");

	public static boolean isLiveSkyReady() {
		return liveSkyPatched && IrisCompat.isShaderPackInUse();
	}

	public static int getSkyboxBlockId() {
		return skyboxBlockId;
	}

	private static void resetState() {
		gbuffersPatched = false;
		depthSamplingPatched = false;
		liveSkyPatched = false;
		skyboxBlockId = -1;
		markerColortex = -1;
		markerColortexFromBlockShader = false;
		cycleCompleted = false;
	}

	public static String patchProperties(String name, String content) {
		if (content == null || name == null) {
			return content;
		}

		if (name.endsWith("dimension.properties")) {
			resetState();
			awaitingBlockProperties = false;
			return content;
		}

		if (name.endsWith("shaders.properties")) {
			if (!awaitingBlockProperties) {
				resetState();
			}
			return content;
		}

		if (name.endsWith("block.properties")) {
			awaitingBlockProperties = false;
			int assignedId = pickSkyboxBlockId(content);
			skyboxBlockId = assignedId;
			cycleCompleted = gbuffersPatched || depthSamplingPatched;

			// Sanitize trailing line-continuation backslashes before blank lines (fixes Iris PropertiesPreprocessor bug in packs like Solas)
			String cleaned = content.replaceAll("\\\\[ \\t]*(\\r?\\n(?:[ \\t]*\\r?\\n)+)", "$1");
			cleaned = cleaned.replaceAll("(?m)^\\s*block\\.\\d+\\s*=\\s*minigames:skybox_simulation[^\\r\\n]*(?:\\r?\\n)?", "");
			String nl = cleaned.contains("\r\n") ? "\r\n" : "\n";
			StringBuilder sb = new StringBuilder(cleaned);
			if (sb.length() > 0 && sb.charAt(sb.length() - 1) != '\n' && sb.charAt(sb.length() - 1) != '\r') {
				sb.append(nl);
			}
			sb.append("block.").append(assignedId).append("=minigames:skybox_simulation").append(nl);
			return sb.toString();
		}

		return content;
	}

	public static String patchGlslFile(Path path, String content) {
		if (path == null || content == null) {
			return content;
		}

		String p = path.toString().replace('\\', '/');
		String lower = p.toLowerCase();
		int slashIdx = lower.lastIndexOf('/');
		String fileName = slashIdx >= 0 ? lower.substring(slashIdx + 1) : lower;

		// Skip shadow files and non-fragment stages
		if (lower.contains("/shadow") || lower.contains("dh_shadow") || fileName.startsWith("shadow")
				|| fileName.endsWith(".vsh") || fileName.endsWith(".gsh") || fileName.endsWith(".csh")) {
			return content;
		}

		if (cycleCompleted) {
			resetState();
		}

		if (skyboxBlockId == -1 || markerColortex == -1) {
			awaitingBlockProperties = true;
			preDiscoverFromShaderRoot(path);
			if (skyboxBlockId == -1) {
				skyboxBlockId = DEFAULT_SKYBOX_BLOCK_ID;
			}
			if (markerColortex == -1) {
				markerColortex = 0;
			}
		}

		String result = content;
		if (isGbuffersBlockShader(lower, fileName, result)) {
			result = patchGbuffersBlockShader(lower, fileName, result);
		}

		if (isDeferredOrCompositeOrInclude(lower, fileName)) {
			result = patchDepthSamplingShader(result);
		}

		return result;
	}

	private static int pickSkyboxBlockId(String blockPropsContent) {
		Set<Integer> usedIds = new HashSet<>();
		int maxId = DEFAULT_SKYBOX_BLOCK_ID - 1;
		Matcher m = BLOCK_ID_PATTERN.matcher(blockPropsContent);
		while (m.find()) {
			try {
				int id = Integer.parseInt(m.group(1));
				usedIds.add(id);
				if (id > maxId && id < 20000) {
					maxId = id;
				}
			} catch (NumberFormatException ignored) {
			}
		}
		if (!usedIds.contains(DEFAULT_SKYBOX_BLOCK_ID)) {
			return DEFAULT_SKYBOX_BLOCK_ID;
		}
		int candidate = maxId + 1;
		while (usedIds.contains(candidate)) {
			candidate++;
		}
		return candidate;
	}

	private static void preDiscoverFromShaderRoot(Path fileInPack) {
		try {
			Path shadersRoot = findShadersRoot(fileInPack);
			if (shadersRoot == null) {
				return;
			}
			if (skyboxBlockId == -1) {
				Path blockProps = shadersRoot.resolve("block.properties");
				if (Files.isRegularFile(blockProps)) {
					String blockContent = Files.readString(blockProps, StandardCharsets.UTF_8);
					skyboxBlockId = pickSkyboxBlockId(blockContent);
				} else {
					skyboxBlockId = DEFAULT_SKYBOX_BLOCK_ID;
				}
			}

			if (markerColortex == -1) {
				List<Path> searchDirs = new ArrayList<>();
				Path parent = fileInPack.getParent();
				if (parent != null && !parent.equals(shadersRoot) && parent.startsWith(shadersRoot)) {
					searchDirs.add(parent);
				}
				Path world0 = shadersRoot.resolve("world0");
				if (Files.isDirectory(world0) && !searchDirs.contains(world0)) {
					searchDirs.add(world0);
				}
				searchDirs.add(shadersRoot);

				String[] entryNames = {
					"gbuffers_block.fsh",
					"gbuffers_entities.fsh",
					"gbuffers_terrain.fsh",
					"gbuffers_textured.fsh"
				};

				for (Path dir : searchDirs) {
					for (String entry : entryNames) {
						Path candidate = dir.resolve(entry);
						if (Files.isRegularFile(candidate)) {
							int[] mapping = findDrawbufferMappingRecursive(shadersRoot, candidate, new HashSet<>(), 0);
							if (mapping != null && mapping[1] > 0) {
								markerColortex = mapping[1];
								markerColortexFromBlockShader = true;
								return;
							}
						}
					}
				}
			}
		} catch (Throwable ignored) {
		}
	}

	private static int[] findDrawbufferMappingRecursive(Path shadersRoot, Path file, Set<Path> visited, int depth) {
		if (depth > 6 || file == null || !visited.add(file.normalize())) {
			return null;
		}
		try {
			if (!Files.isRegularFile(file)) {
				return null;
			}
			String content = Files.readString(file, StandardCharsets.UTF_8);
			int[] mapping = findDrawbufferMapping(content);
			if (mapping != null) {
				return mapping;
			}
			Matcher inc = INCLUDE_PATTERN.matcher(content);
			while (inc.find()) {
				String raw = inc.group(1).trim();
				Path target;
				if (raw.startsWith("/")) {
					target = shadersRoot.resolve(raw.substring(1));
				} else {
					Path parent = file.getParent();
					target = (parent != null ? parent : shadersRoot).resolve(raw);
				}
				int[] sub = findDrawbufferMappingRecursive(shadersRoot, target, visited, depth + 1);
				if (sub != null) {
					return sub;
				}
			}
		} catch (Throwable ignored) {
		}
		return null;
	}

	private static Path findShadersRoot(Path path) {
		try {
			Path curr = path.getParent();
			for (int i = 0; i < 6 && curr != null; i++) {
				if (Files.isRegularFile(curr.resolve("shaders.properties"))
						|| Files.isRegularFile(curr.resolve("block.properties"))) {
					return curr;
				}
				curr = curr.getParent();
			}
			curr = path.getParent();
			for (int i = 0; i < 6 && curr != null; i++) {
				if (Files.isRegularFile(curr.resolve("gbuffers_terrain.fsh"))
						|| Files.isRegularFile(curr.resolve("deferred.fsh"))
						|| Files.isRegularFile(curr.resolve("composite.fsh"))) {
					Path parent = curr.getParent();
					Path fileNamePath = curr.getFileName();
					String dirName = fileNamePath != null ? fileNamePath.toString().toLowerCase() : "";
					if (parent != null && (Files.isRegularFile(parent.resolve("shaders.properties"))
							|| Files.isRegularFile(parent.resolve("block.properties"))
							|| dirName.startsWith("world"))) {
						return parent;
					}
					return curr;
				}
				curr = curr.getParent();
			}
		} catch (Throwable ignored) {
		}
		return null;
	}

	private static boolean isGbuffersBlockShader(String lowerPath, String fileName, String content) {
		if (!content.contains("gl_FragData[") || !content.contains("main")) {
			return false;
		}

		// Exclude non-opaque-block programs (deferred, composite, water, translucent, sky, shadow, etc.)
		if (fileName.startsWith("deferred") || fileName.startsWith("composite") || fileName.startsWith("final")
				|| fileName.startsWith("prepare") || fileName.startsWith("setup") || fileName.startsWith("begin")
				|| fileName.startsWith("dh_") || fileName.startsWith("voxy")
				|| lowerPath.contains("/deferred") || lowerPath.contains("/composite")
				|| lowerPath.contains("water") || lowerPath.contains("translucent")
				|| lowerPath.contains("weather") || lowerPath.contains("clouds")
				|| lowerPath.contains("skybasic") || lowerPath.contains("skytextured")
				|| lowerPath.contains("glint") || lowerPath.contains("beaconbeam")
				|| lowerPath.contains("eyes") || lowerPath.contains("hand")
				|| lowerPath.contains("damagedblock") || lowerPath.contains("crumbling")
				|| lowerPath.contains("line") || lowerPath.contains("particles")) {
			return false;
		}

		boolean isGbuffersOrProgramFile = fileName.startsWith("gbuffers_")
				|| fileName.startsWith("clrwl_gbuffers")
				|| lowerPath.contains("/program/")
				|| lowerPath.contains("/programs/")
				|| lowerPath.contains("/gbuffers")
				|| content.contains("#define GBUFFERS_");
		if (!isGbuffersOrProgramFile) {
			return false;
		}

		return findFragmentMainSpan(content) != null;
	}

	private static boolean isDeferredOrCompositeOrInclude(String lowerPath, String fileName) {
		if (fileName.startsWith("gbuffers_") || fileName.startsWith("clrwl_gbuffers")
				|| fileName.startsWith("dh_") || fileName.startsWith("voxy") || fileName.startsWith("final")) {
			return false;
		}
		return fileName.startsWith("deferred") || fileName.startsWith("composite")
				|| fileName.startsWith("prepare") || fileName.startsWith("setup")
				|| lowerPath.contains("/deferred") || lowerPath.contains("/composite")
				|| lowerPath.contains("/lib/") || lowerPath.contains("/include/")
				|| lowerPath.contains("/program");
	}

	/**
	 * Returns [fragDataIndex, colortexNumber], or null if no DRAWBUFFERS / RENDERTARGETS comment is present.
	 */
	private static int[] findDrawbufferMapping(String content) {
		List<List<Integer>> directives = new ArrayList<>();

		Matcher dbMatcher = DRAWBUFFERS_PATTERN.matcher(content);
		while (dbMatcher.find()) {
			String spec = dbMatcher.group(1).trim();
			List<Integer> list = new ArrayList<>();
			for (int i = 0; i < spec.length(); i++) {
				char c = spec.charAt(i);
				if (c >= '0' && c <= '9') {
					list.add(c - '0');
				}
			}
			if (!list.isEmpty()) {
				directives.add(list);
			}
		}

		Matcher rtMatcher = RENDERTARGETS_PATTERN.matcher(content);
		while (rtMatcher.find()) {
			String spec = rtMatcher.group(1).trim();
			String[] parts = spec.split(",");
			List<Integer> list = new ArrayList<>();
			for (String part : parts) {
				String trimmed = part.trim();
				if (!trimmed.isEmpty()) {
					try {
						list.add(Integer.parseInt(trimmed));
					} catch (NumberFormatException ignored) {
					}
				}
			}
			if (!list.isEmpty()) {
				directives.add(list);
			}
		}

		if (directives.isEmpty()) {
			return null;
		}

		int minLen = Integer.MAX_VALUE;
		for (List<Integer> d : directives) {
			minLen = Math.min(minLen, d.size());
		}

		// Prefer a secondary render target (index >= 1) that maps to the same colortexN across all directives
		for (int idx = 1; idx < minLen; idx++) {
			int target = directives.get(0).get(idx);
			boolean allMatch = true;
			for (int d = 1; d < directives.size(); d++) {
				if (directives.get(d).get(idx) != target) {
					allMatch = false;
					break;
				}
			}
			if (allMatch) {
				return new int[]{idx, target};
			}
		}

		// Otherwise use index 1 of the first directive if available, or index 0 -> colortex0
		List<Integer> first = directives.get(0);
		if (first.size() > 1) {
			return new int[]{1, first.get(1)};
		}
		return new int[]{0, first.get(0)};
	}

	/**
	 * Finds the [mainDeclStart, openBrace, closeBrace] of the fragment shader's void main()
	 * by selecting the void main() whose body writes to gl_FragData[.
	 */
	private static int[] findFragmentMainSpan(String content) {
		Matcher m = MAIN_FUNC_PATTERN.matcher(content);
		while (m.find()) {
			int declStart = m.start();
			int openBrace = content.indexOf('{', m.end());
			if (openBrace == -1) {
				continue;
			}
			int depth = 0;
			int closeBrace = -1;
			for (int i = openBrace; i < content.length(); i++) {
				char c = content.charAt(i);
				if (c == '{') {
					depth++;
				} else if (c == '}') {
					depth--;
					if (depth == 0) {
						closeBrace = i;
						break;
					}
				}
			}
			if (closeBrace != -1) {
				String body = content.substring(openBrace, closeBrace);
				if (body.contains("gl_FragData[")) {
					return new int[]{declStart, openBrace, closeBrace};
				}
			}
		}
		return null;
	}

	private static String patchGbuffersBlockShader(String lowerPath, String fileName, String content) {
		if (content.contains("MG_SKYBOX_BLOCK_PATCHED")) {
			return content;
		}

		int[] mainSpan = findFragmentMainSpan(content);
		if (mainSpan == null) {
			return content;
		}

		boolean isPrimaryBlockProgram = fileName.contains("gbuffers_block")
				|| fileName.contains("gbuffers_entities")
				|| fileName.contains("gbuffers_terrain")
				|| content.contains("#define GBUFFERS_BLOCK")
				|| content.contains("#define GBUFFERS_ENTITIES")
				|| content.contains("#define GBUFFERS_TERRAIN");

		int[] mapping = findDrawbufferMapping(content);
		int fragDataIdx = (mapping != null) ? mapping[0] : -1;
		if (mapping != null && mapping[1] > 0) {
			if (isPrimaryBlockProgram || (!markerColortexFromBlockShader && markerColortex <= 0)) {
				markerColortex = mapping[1];
				if (isPrimaryBlockProgram) {
					markerColortexFromBlockShader = true;
				}
			}
		}

		int targetBlockId = (skyboxBlockId != -1) ? skyboxBlockId : DEFAULT_SKYBOX_BLOCK_ID;
		String nl = content.contains("\r\n") ? "\r\n" : "\n";

		int mainDeclStart = mainSpan[0];
		int openBrace = mainSpan[1];
		int mainCloseBrace = mainSpan[2];
		String mainBody = content.substring(openBrace, mainCloseBrace);

		// Write secondary target only when it maps to markerColortex
		boolean writeSecondary = false;
		if (mapping != null && fragDataIdx >= 1 && (mapping[1] == markerColortex || markerColortex <= 0)) {
			writeSecondary = true;
		} else if (mapping == null && markerColortex > 0 && mainBody.contains("gl_FragData[1]")) {
			fragDataIdx = 1;
			writeSecondary = true;
		}

		String color0Value = (markerColortex > 0) ? "vec4(0.0, 0.0, 0.0, 1.0)" : MARKER_COLOR0_VEC4;

		// Inject marker write at the end of the fragment void main()
		StringBuilder inject = new StringBuilder();
		inject.append(nl).append("\t// MG_SKYBOX_BLOCK_PATCHED").append(nl);
		inject.append("\tif (blockEntityId == ").append(targetBlockId).append(") {").append(nl);
		inject.append("\t\tgl_FragData[0] = ").append(color0Value).append(";").append(nl);
		if (writeSecondary && fragDataIdx >= 1) {
			inject.append("\t\tgl_FragData[").append(fragDataIdx).append("] = ").append(MARKER_SEC_VEC4).append(";").append(nl);
		}
		inject.append("\t}").append(nl);

		content = content.substring(0, mainCloseBrace) + inject + content.substring(mainCloseBrace);

		// Ensure uniform int blockEntityId; is declared in the fragment shader scope right before void main()
		if (!UNIFORM_BLOCK_ENTITY_ID_PATTERN.matcher(content).find()) {
			String uniformDecl = "#ifndef MG_SKYBOX_BE_ID_DECLARED" + nl
					+ "#define MG_SKYBOX_BE_ID_DECLARED" + nl
					+ "uniform int blockEntityId;" + nl
					+ "#endif" + nl;
			content = content.substring(0, mainDeclStart) + uniformDecl + content.substring(mainDeclStart);
		}

		gbuffersPatched = true;
		liveSkyPatched = gbuffersPatched && depthSamplingPatched;
		return content;
	}

	private static String guardSamplerDeclaration(String content, String samplerName, String guardMacro, String nl) {
		Pattern singleDecl = Pattern.compile("(?m)^([ \\t]*uniform\\s+sampler2D\\s+" + Pattern.quote(samplerName) + "\\s*;)");
		content = singleDecl.matcher(content).replaceAll(
				"#ifndef " + guardMacro + nl +
				"#define " + guardMacro + nl +
				"$1" + nl +
				"#endif");

		// Also handle comma-separated multi-sampler declarations like: uniform sampler2D colortex1, colortex3;
		Pattern multiDecl = Pattern.compile("(?m)^([ \\t]*)uniform\\s+sampler2D\\s+([^;\\r\\n]*\\b" + Pattern.quote(samplerName) + "\\b[^;\\r\\n]*);");
		Matcher m = multiDecl.matcher(content);
		StringBuilder sb = new StringBuilder();
		int lastEnd = 0;
		boolean changed = false;
		while (m.find()) {
			String namesRaw = m.group(2).trim();
			if (!namesRaw.contains(",")) {
				continue;
			}
			String indent = m.group(1);
			String[] parts = namesRaw.split(",");
			List<String> others = new ArrayList<>();
			boolean foundTarget = false;
			for (String part : parts) {
				String t = part.trim();
				if (t.equals(samplerName)) {
					foundTarget = true;
				} else if (!t.isEmpty()) {
					others.add(t);
				}
			}
			if (!foundTarget) {
				continue;
			}
			sb.append(content, lastEnd, m.start());
			if (!others.isEmpty()) {
				sb.append(indent).append("uniform sampler2D ").append(String.join(", ", others)).append(";").append(nl);
			}
			sb.append("#ifndef ").append(guardMacro).append(nl)
					.append("#define ").append(guardMacro).append(nl)
					.append(indent).append("uniform sampler2D ").append(samplerName).append(";").append(nl)
					.append("#endif");
			lastEnd = m.end();
			changed = true;
		}
		if (changed) {
			sb.append(content, lastEnd, content.length());
			content = sb.toString();
		}
		return content;
	}

	private static String patchDepthSamplingShader(String content) {
		if (content.contains("MG_SKYBOX_DEPTH_HELPER")) {
			return content;
		}

		String nl = content.contains("\r\n") ? "\r\n" : "\n";
		int secondaryIdx = markerColortex > 0 ? markerColortex : 0;
		String markerSampler = secondaryIdx > 0 ? ("colortex" + secondaryIdx) : "colortex0";
		String guardMacro = "MG_SKYBOX_MARKER_SAMPLER_DECLARED";

		// Always guard existing declarations of markerSampler in deferred/composite/include files
		// so that outer files (e.g. composite1.glsl) and inner #includes (e.g. volumetrics.glsl) never conflict
		if (content.contains(markerSampler)) {
			content = guardSamplerDeclaration(content, markerSampler, guardMacro, nl);
		}

		if (!ANY_DEPTH_SAMPLER_PATTERN.matcher(content).find()) {
			return content;
		}

		boolean hasSamplingCall = TEXTURE2D_DEPTH_PATTERN.matcher(content).find()
				|| TEXTURE_DEPTH_PATTERN.matcher(content).find()
				|| TEXTURE2DLOD_DEPTH_PATTERN.matcher(content).find()
				|| TEXTURELOD_DEPTH_PATTERN.matcher(content).find()
				|| TEXELFETCH_DEPTH_PATTERN.matcher(content).find();

		if (!hasSamplingCall) {
			return content;
		}

		boolean useLegacyTexture2D = content.contains("texture2D(") || !content.contains("texture(");
		String texFn = useLegacyTexture2D ? "texture2D" : "texture";
		String texLodFn = useLegacyTexture2D ? "texture2DLod" : "textureLod";

		String markerCondition = secondaryIdx > 0
				? "abs(c.r) < 0.008 && abs(c.g) < 0.008 && abs(c.b - 0.70196) < 0.015"
				: "distance(c, " + MARKER_COLOR0_VEC3 + ") < 0.012";

		String nonDeferredGuard = "#if !defined GBUFFERS_WATER && !defined GBUFFERS_HAND_WATER && !defined GBUFFERS_TERRAIN"
				+ " && !defined GBUFFERS_ENTITIES && !defined GBUFFERS_BLOCK && !defined GBUFFERS_HAND"
				+ " && !defined GBUFFERS_TEXTURED && !defined GBUFFERS_TEXTURED_LIT && !defined GBUFFERS_BASIC"
				+ " && !defined GBUFFERS_TRANSLUCENT && !defined DH_WATER && !defined DH_TERRAIN"
				+ " && !defined VOXY_TRANSLUCENT && !defined VOXY_OPAQUE && !defined VOXY_PATCH && !defined SHADOW";

		StringBuilder helperSb = new StringBuilder();
		helperSb.append("#ifndef MG_SKYBOX_DEPTH_HELPER").append(nl)
				.append("#define MG_SKYBOX_DEPTH_HELPER").append(nl)
				.append(nonDeferredGuard).append(nl)
				.append("#ifndef ").append(guardMacro).append(nl)
				.append("#define ").append(guardMacro).append(nl)
				.append("uniform sampler2D ").append(markerSampler).append(";").append(nl)
				.append("#endif").append(nl)
				.append("bool mgIsSkyboxMarker(vec3 c) {").append(nl)
				.append("\treturn ").append(markerCondition).append(";").append(nl)
				.append("}").append(nl)
				.append("bool mgIsSkyboxPixel(vec2 coord) {").append(nl)
				.append("\treturn mgIsSkyboxMarker(").append(texFn).append("(").append(markerSampler).append(", coord).rgb);").append(nl)
				.append("}").append(nl)
				.append("bool mgIsSkyboxTexel(ivec2 coord, int lod) {").append(nl)
				.append("\treturn mgIsSkyboxMarker(texelFetch(").append(markerSampler).append(", coord, lod).rgb);").append(nl)
				.append("}").append(nl)
				.append("#else").append(nl)
				.append("bool mgIsSkyboxPixel(vec2 coord) { return false; }").append(nl)
				.append("bool mgIsSkyboxTexel(ivec2 coord, int lod) { return false; }").append(nl)
				.append("#endif").append(nl)
				.append("vec4 mgSampleDepth(sampler2D s, vec2 coord) {").append(nl)
				.append("\tif (mgIsSkyboxPixel(coord)) return vec4(1.0);").append(nl)
				.append("\treturn ").append(texFn).append("(s, coord);").append(nl)
				.append("}").append(nl)
				.append("vec4 mgSampleDepth(sampler2D s, vec2 coord, float bias) {").append(nl)
				.append("\tif (mgIsSkyboxPixel(coord)) return vec4(1.0);").append(nl)
				.append("\treturn ").append(texFn).append("(s, coord, bias);").append(nl)
				.append("}").append(nl)
				.append("vec4 mgSampleDepthLod(sampler2D s, vec2 coord, float lod) {").append(nl)
				.append("\tif (mgIsSkyboxPixel(coord)) return vec4(1.0);").append(nl)
				.append("\treturn ").append(texLodFn).append("(s, coord, lod);").append(nl)
				.append("}").append(nl)
				.append("vec4 mgTexelFetchDepth(sampler2D s, ivec2 coord, int lod) {").append(nl)
				.append("\tif (mgIsSkyboxTexel(coord, lod)) return vec4(1.0);").append(nl)
				.append("\treturn texelFetch(s, coord, lod);").append(nl)
				.append("}").append(nl)
				.append("#endif").append(nl);
		String helperBlock = helperSb.toString();

		// Rewrite depth sampling calls before injecting helperBlock
		content = TEXTURE2D_DEPTH_PATTERN.matcher(content).replaceAll("mgSampleDepth($1,");
		content = TEXTURE_DEPTH_PATTERN.matcher(content).replaceAll("mgSampleDepth($1,");
		content = TEXTURE2DLOD_DEPTH_PATTERN.matcher(content).replaceAll("mgSampleDepthLod($1,");
		content = TEXTURELOD_DEPTH_PATTERN.matcher(content).replaceAll("mgSampleDepthLod($1,");
		content = TEXELFETCH_DEPTH_PATTERN.matcher(content).replaceAll("mgTexelFetchDepth($1,");

		// If this shader defines a mutable global `float caveFactor = ...;` and has `texCoord`,
		// ensure skybox pixels always use `caveFactor = 1.0` so underground/enclosed skyboxes still render clouds, sun, moon, and stars
		if (content.contains("caveFactor") && content.contains("texCoord")) {
			Matcher caveMatcher = CAVE_FACTOR_DECL_PATTERN.matcher(content);
			if (caveMatcher.find()) {
				content = caveMatcher.replaceAll("$1" + nl + "#define MG_HAS_CAVE_FACTOR");
				Matcher mainMatcher = MAIN_FUNC_PATTERN.matcher(content);
				if (mainMatcher.find()) {
					int openBrace = content.indexOf('{', mainMatcher.end());
					if (openBrace != -1) {
						String caveOverride = nl + "\t#ifdef MG_HAS_CAVE_FACTOR" + nl
								+ "\tif (mgIsSkyboxPixel(texCoord.xy)) caveFactor = 1.0;" + nl
								+ "\t#endif";
						content = content.substring(0, openBrace + 1) + caveOverride + content.substring(openBrace + 1);
					}
				}
			}
		}

		// Inject helperBlock at the start of the fragment section (#ifdef FSH) if present,
		// or at the top of the file (after #version / #extension) for included fragment/library files
		int insertPos = findFragmentHelperInsertPos(content);
		content = content.substring(0, insertPos) + helperBlock + content.substring(insertPos);

		depthSamplingPatched = true;
		liveSkyPatched = gbuffersPatched && depthSamplingPatched;
		return content;
	}

	private static int findFragmentHelperInsertPos(String content) {
		int insertPos = 0;
		Matcher fshMatcher = FSH_IFDEF_PATTERN.matcher(content);
		if (fshMatcher.find()) {
			insertPos = fshMatcher.end();
		} else {
			int verIdx = content.indexOf("#version");
			if (verIdx != -1) {
				int lineEnd = content.indexOf('\n', verIdx);
				if (lineEnd != -1) {
					insertPos = lineEnd + 1;
				}
			}
		}

		// Advance past any #extension directives near insertPos
		Pattern extPattern = Pattern.compile("(?m)^[ \\t]*#\\s*extension\\b[^\\r\\n]*(?:\\r?\\n)?");
		Matcher extMatcher = extPattern.matcher(content);
		while (extMatcher.find()) {
			if (extMatcher.start() >= insertPos && extMatcher.start() - insertPos < 500) {
				insertPos = extMatcher.end();
			}
		}
		return insertPos;
	}
}
