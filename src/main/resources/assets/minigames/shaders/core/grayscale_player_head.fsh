vec4 color = texture(Sampler0, texCoord);

float gray =
      color.r * 0.299
    + color.g * 0.587
    + color.b * 0.114;

color.rgb = vec3(gray);

color *= vertexColor;

fragColor = color;