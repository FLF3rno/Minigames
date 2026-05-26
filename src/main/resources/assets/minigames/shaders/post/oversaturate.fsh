#version 150

uniform sampler2D InSampler;

in vec2 texCoord;

out vec4 fragColor;

vec3 applySaturation(vec3 color, float sat) {
    float luma = dot(color, vec3(0.2126, 0.7152, 0.0722));
    return mix(vec3(luma), color, sat);
}

vec3 applyContrast(vec3 color, float contrast) {
    // contrast = 1.0 no change; >1 increases contrast
    return (color - 0.5) * contrast + 0.5;
}

void main() {
    vec4 inColor = texture(InSampler, texCoord);
    vec3 base = inColor.rgb;

    // Approx pixel size for a small glow blur. Using textureSize keeps it resolution-independent.
    vec2 texSize = vec2(textureSize(InSampler, 0));
    vec2 px = 1.0 / max(texSize, vec2(1.0));

    // Lightweight "holy glow": blur + highlight extraction.
    vec3 blur =
        texture(InSampler, texCoord).rgb * 0.40 +
        texture(InSampler, texCoord + vec2( 1.0,  0.0) * px).rgb * 0.12 +
        texture(InSampler, texCoord + vec2(-1.0,  0.0) * px).rgb * 0.12 +
        texture(InSampler, texCoord + vec2( 0.0,  1.0) * px).rgb * 0.12 +
        texture(InSampler, texCoord + vec2( 0.0, -1.0) * px).rgb * 0.12 +
        texture(InSampler, texCoord + vec2( 1.0,  1.0) * px).rgb * 0.06 +
        texture(InSampler, texCoord + vec2(-1.0,  1.0) * px).rgb * 0.06;

    float baseLuma = dot(base, vec3(0.2126, 0.7152, 0.0722));
    float highlight = smoothstep(0.55, 0.92, baseLuma);
    vec3 glow = blur * highlight;

    // "Ethereal" grade: very high saturation, warm-gold tint, lifted whites.
    vec3 c = applySaturation(base, 2.60);
    c = applyContrast(c, 1.28);

    // Warm tint that strengthens with brightness.
    vec3 gold = vec3(1.08, 1.00, 0.86);
    c *= mix(vec3(1.0), gold, smoothstep(0.25, 0.95, baseLuma));

    // Lift and soften into a radiant look.
    c = c * 1.12 + 0.035;
    c += glow * 0.85;

    // Gentle vignette fade (subtle, keeps "heavenly focus" center-screen).
    vec2 d = texCoord - vec2(0.5);
    float vignette = smoothstep(0.95, 0.25, dot(d, d));
    c = mix(c * 0.92, c, vignette);

    fragColor = vec4(clamp(c, 0.0, 1.0), inColor.a);
}
