// Made with Blockbench 5.2.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class Modeltesla_coil<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "tesla_coil"), "main");
	private final ModelPart tesla_coil;

	public Modeltesla_coil(ModelPart root) {
		this.tesla_coil = root.getChild("tesla_coil");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition tesla_coil = partdefinition.addOrReplaceChild("tesla_coil",
				CubeListBuilder.create().texOffs(18, 12)
						.addBox(-8.0F, -26.0F, -8.0F, 16.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)).texOffs(34, 138)
						.addBox(-1.5F, -45.0F, -1.5F, 3.0F, 19.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(19, 72)
						.addBox(-6.0F, -30.3F, -6.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(18, 99)
						.addBox(-5.0F, -34.3F, -5.0F, 10.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(106, 134)
						.addBox(-4.0F, -38.3F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(3, 139)
						.addBox(-3.0F, -42.3F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(64, 120)
						.addBox(-3.0F, -51.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 48.2F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		tesla_coil.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
	}
}