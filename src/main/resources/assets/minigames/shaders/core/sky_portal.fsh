#version 330

// Samples the sky snapshot captured right after the sky pass, in screen space,
// so the skybox block faces show exactly what the real sky looks like behind them.
uniform sampler2D Sampler0;

in vec4 texProj0;

out vec4 fragColor;

void main() {
    fragColor = vec4(textureProj(Sampler0, texProj0).rgb, 1.0);
}
