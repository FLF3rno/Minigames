// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class Modeltesla_coil_flavio<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "tesla_coil_flavio"), "main");
	private final ModelPart tesla_coil;

	public Modeltesla_coil_flavio(ModelPart root) {
		this.tesla_coil = root.getChild("tesla_coil");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition tesla_coil = partdefinition.addOrReplaceChild("tesla_coil",
				CubeListBuilder.create().texOffs(0, 0)
						.addBox(-14.0F, -5.0F, -14.0F, 28.0F, 5.0F, 28.0F, new CubeDeformation(0.0F)).texOffs(0, 33)
						.addBox(-11.0F, -10.0F, -11.0F, 22.0F, 5.0F, 22.0F, new CubeDeformation(0.0F)).texOffs(0, 60)
						.addBox(-11.0F, -26.0F, -11.0F, 22.0F, 5.0F, 22.0F, new CubeDeformation(0.0F)).texOffs(0, 87)
						.addBox(-10.0F, -49.0F, -10.0F, 20.0F, 5.0F, 20.0F, new CubeDeformation(0.0F)).texOffs(32, 137)
						.addBox(-2.0F, -45.0F, -2.0F, 4.0F, 19.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(130, 114)
						.addBox(-4.0F, -30.3F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(130, 124)
						.addBox(-4.0F, -34.3F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(106, 134)
						.addBox(-4.0F, -38.3F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(0, 137)
						.addBox(-4.0F, -42.3F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(106, 114)
						.addBox(-3.0F, -63.0F, -3.0F, 6.0F, 14.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(58, 114)
						.addBox(-6.0F, -74.0F, -6.0F, 12.0F, 11.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(80, 87)
						.addBox(-8.0F, -21.0F, -8.0F, 16.0F, 11.0F, 16.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.2F, 0.0F));

		PartDefinition cube_r1 = tesla_coil.addOrReplaceChild("cube_r1",
				CubeListBuilder.create().texOffs(88, 58).addBox(13.1068F, -5.6046F, -10.0F, 9.0F, 5.0F, 20.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -1.5117F, 0.0F, -1.5708F, -1.3963F, 1.5708F));

		PartDefinition cube_r2 = tesla_coil.addOrReplaceChild("cube_r2",
				CubeListBuilder.create().texOffs(112, 0).addBox(13.1068F, -5.6046F, -10.0F, 9.0F, 5.0F, 20.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -1.5117F, 0.0F, 1.5708F, 1.3963F, 1.5708F));

		PartDefinition cube_r3 = tesla_coil.addOrReplaceChild("cube_r3",
				CubeListBuilder.create().texOffs(88, 33).addBox(-14.0F, -5.0F, -10.0F, 9.0F, 5.0F, 20.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-26.8F, 2.6F, 0.0F, -3.1416F, 0.0F, 2.9671F));

		PartDefinition cube_r4 = tesla_coil
				.addOrReplaceChild("cube_r4",
						CubeListBuilder.create().texOffs(0, 112).addBox(-14.0F, -5.0F, -10.0F, 9.0F, 5.0F, 20.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(26.8F, 2.6F, 0.0F, 0.0F, 0.0F, 0.1745F));

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