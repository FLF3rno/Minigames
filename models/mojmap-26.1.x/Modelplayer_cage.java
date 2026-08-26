// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class Modelplayer_cage<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "player_cage"), "main");
	private final ModelPart cage;

	public Modelplayer_cage(ModelPart root) {
		this.cage = root.getChild("cage");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition cage = partdefinition.addOrReplaceChild("cage",
				CubeListBuilder.create().texOffs(0, 55)
						.addBox(-10.0F, -2.0F, -12.0F, 20.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(44, 55)
						.addBox(-10.0F, -2.0F, 10.0F, 20.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 59)
						.addBox(-10.0F, -39.0F, 10.0F, 20.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(44, 59)
						.addBox(-10.0F, -39.0F, -12.0F, 20.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r1 = cage.addOrReplaceChild("cube_r1",
				CubeListBuilder.create().texOffs(24, 63).addBox(-12.0F, -34.0F, 10.0F, 2.0F, 35.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(22.0F, -3.0F, 22.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r2 = cage.addOrReplaceChild("cube_r2",
				CubeListBuilder.create().texOffs(16, 63).addBox(-12.0F, -34.0F, 10.0F, 2.0F, 35.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -3.0F, 22.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r3 = cage.addOrReplaceChild("cube_r3",
				CubeListBuilder.create().texOffs(8, 63).addBox(-12.0F, -34.0F, 10.0F, 2.0F, 35.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(22.0F, -3.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r4 = cage.addOrReplaceChild("cube_r4",
				CubeListBuilder.create().texOffs(32, 99)
						.addBox(6.0F, -17.5F, -11.5F, 2.0F, 35.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(98, 91)
						.addBox(1.0F, -17.5F, -11.5F, 2.0F, 35.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(98, 55)
						.addBox(-8.0F, -17.5F, -11.5F, 2.0F, 35.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(96, 0)
						.addBox(-4.0F, -17.5F, -11.5F, 2.0F, 35.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(92, 91)
						.addBox(-8.0F, -17.5F, 10.5F, 2.0F, 35.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(92, 55)
						.addBox(-4.0F, -17.5F, 10.5F, 2.0F, 35.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(86, 63)
						.addBox(1.0F, -17.5F, 10.5F, 2.0F, 35.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(80, 63)
						.addBox(6.0F, -17.5F, 10.5F, 2.0F, 35.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -19.5F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition cube_r5 = cage.addOrReplaceChild("cube_r5",
				CubeListBuilder.create().texOffs(74, 63).addBox(-12.0F, -34.0F, 10.0F, 2.0F, 35.0F, 1.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(21.5F, -3.0F, 8.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r6 = cage.addOrReplaceChild("cube_r6",
				CubeListBuilder.create().texOffs(68, 63).addBox(-12.0F, -34.0F, 10.0F, 2.0F, 35.0F, 1.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(21.5F, -3.0F, 4.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r7 = cage.addOrReplaceChild("cube_r7",
				CubeListBuilder.create().texOffs(62, 63).addBox(-12.0F, -34.0F, 10.0F, 2.0F, 35.0F, 1.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(21.5F, -3.0F, 13.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r8 = cage.addOrReplaceChild("cube_r8",
				CubeListBuilder.create().texOffs(56, 63).addBox(-12.0F, -34.0F, 10.0F, 2.0F, 35.0F, 1.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(21.5F, -3.0F, 18.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r9 = cage.addOrReplaceChild("cube_r9",
				CubeListBuilder.create().texOffs(50, 63).addBox(-12.0F, -34.0F, 10.0F, 2.0F, 35.0F, 1.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.5F, -3.0F, 18.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r10 = cage.addOrReplaceChild("cube_r10",
				CubeListBuilder.create().texOffs(44, 63).addBox(-12.0F, -34.0F, 10.0F, 2.0F, 35.0F, 1.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.5F, -3.0F, 13.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r11 = cage.addOrReplaceChild("cube_r11",
				CubeListBuilder.create().texOffs(38, 63).addBox(-12.0F, -34.0F, 10.0F, 2.0F, 35.0F, 1.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.5F, -3.0F, 8.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r12 = cage.addOrReplaceChild("cube_r12",
				CubeListBuilder.create().texOffs(32, 63).addBox(-12.0F, -34.0F, 10.0F, 2.0F, 35.0F, 1.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.5F, -3.0F, 4.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r13 = cage.addOrReplaceChild("cube_r13",
				CubeListBuilder.create().texOffs(0, 63)
						.addBox(-12.0F, -34.0F, 10.0F, 2.0F, 35.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(52, 51)
						.addBox(-12.0F, -36.0F, 10.0F, 24.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(52, 47)
						.addBox(-12.0F, -36.0F, -12.0F, 24.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 51)
						.addBox(-12.0F, 1.0F, 10.0F, 24.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(80, 26)
						.addBox(-4.0F, -43.0F, -2.0F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(80, 36)
						.addBox(-4.0F, -44.0F, -2.0F, 8.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 26)
						.addBox(-10.0F, -39.0F, -10.0F, 20.0F, 1.0F, 20.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
						.addBox(-12.0F, -38.0F, -12.0F, 24.0F, 2.0F, 24.0F, new CubeDeformation(0.0F)).texOffs(0, 47)
						.addBox(-12.0F, 1.0F, -12.0F, 24.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r14 = cage.addOrReplaceChild("cube_r14",
				CubeListBuilder.create().texOffs(80, 40).addBox(-4.0F, 0.0F, -2.0F, 2.0F, 4.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -46.0F, 6.0F, 0.0F, -1.5708F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		cage.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
	}
}