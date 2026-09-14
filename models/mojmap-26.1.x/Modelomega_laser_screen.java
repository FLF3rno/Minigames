// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class Modelomega_laser_screen<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "omega_laser_screen"), "main");
	private final ModelPart bone;

	public Modelomega_laser_screen(ModelPart root) {
		this.bone = root.getChild("bone");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone",
				CubeListBuilder.create().texOffs(2, 1)
						.addBox(-4.0F, 0.5F, -5.4F, 8.0F, 24.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(42, 0)
						.addBox(-2.0F, -8.5F, -5.0F, 4.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(42, 12)
						.addBox(-2.0F, -3.5F, 1.0F, 4.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -0.3F, 1.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition cube_r1 = bone.addOrReplaceChild("cube_r1",
				CubeListBuilder.create().texOffs(0, 35).addBox(-11.0F, -10.5F, -1.5F, 18.0F, 15.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(2.0F, -3.6159F, 2.8846F, 0.829F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
	}
}