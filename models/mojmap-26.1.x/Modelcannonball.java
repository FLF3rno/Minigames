// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class Modelcannonball<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "cannonball"), "main");
	private final ModelPart ball;

	public Modelcannonball(ModelPart root) {
		this.ball = root.getChild("ball");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition ball = partdefinition.addOrReplaceChild("ball",
				CubeListBuilder.create().texOffs(0, 0)
						.addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(0, 12)
						.addBox(-2.0F, -2.0F, 2.6F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(10, 12)
						.addBox(-2.0F, -2.0F, -3.6F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 48.0F, 0.0F));

		PartDefinition cube_r1 = ball.addOrReplaceChild("cube_r1",
				CubeListBuilder.create().texOffs(20, 17).addBox(-2.0F, -2.0F, 2.5F, 4.0F, 4.0F, 1.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r2 = ball.addOrReplaceChild("cube_r2",
				CubeListBuilder.create().texOffs(20, 12).addBox(-2.0F, -2.0F, -3.4F, 4.0F, 4.0F, 1.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -1.5708F));

		PartDefinition cube_r3 = ball.addOrReplaceChild("cube_r3",
				CubeListBuilder.create().texOffs(10, 17)
						.addBox(-2.0F, -2.0F, 2.6F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 17)
						.addBox(-2.0F, -2.0F, -3.5F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		ball.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
	}
}