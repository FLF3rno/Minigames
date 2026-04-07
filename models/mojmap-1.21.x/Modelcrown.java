// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class Modelcrown<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "crown"), "main");
	private final ModelPart Crown;

	public Modelcrown(ModelPart root) {
		this.Crown = root.getChild("Crown");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Crown = partdefinition.addOrReplaceChild("Crown",
				CubeListBuilder.create().texOffs(6, 39)
						.addBox(-3.2F, -0.0526F, -4.2579F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(25, 9)
						.addBox(-4.8F, -1.0526F, -4.2579F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(14, 27)
						.addBox(-4.8F, -0.0526F, -4.2829F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(14, 27)
						.addBox(-4.8F, -0.0526F, 4.7171F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 21)
						.addBox(2.8F, -1.0526F, -4.2579F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 17)
						.addBox(-1.0F, -1.0526F, -4.2579F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 106)
						.addBox(-0.5F, 0.4474F, -4.4579F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(101, 23)
						.addBox(-2.5F, 0.4474F, -4.4579F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(68, 69)
						.addBox(1.5F, 0.4474F, -4.4579F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 9)
						.addBox(-1.0F, -1.0526F, 4.7421F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 17)
						.addBox(2.8F, -1.0526F, 4.7421F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(21, 29)
						.addBox(-4.8F, -1.0526F, 4.7421F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(1, 29)
						.addBox(-4.8F, -1.0526F, 3.7421F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(13, 17)
						.addBox(-4.8F, -1.0526F, -3.2579F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 24)
						.addBox(-4.8F, -1.0526F, -0.2579F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(8, 16)
						.addBox(3.8F, -1.0526F, -0.2579F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(17, 21)
						.addBox(3.8F, -1.0526F, -3.2579F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(10, 18)
						.addBox(3.8F, -1.0526F, 3.7421F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(20, 55)
						.addBox(-4.2F, -0.0526F, 4.7421F, 9.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(7, 26)
						.addBox(3.8F, -0.0526F, -3.2579F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(24, 11)
						.addBox(-4.8F, -0.0526F, -3.2579F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -7.9474F, -0.7421F));

		return LayerDefinition.create(meshdefinition, 131, 131);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		Crown.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}