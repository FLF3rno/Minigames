// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class Modelspider<T extends spider> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "spider"), "main");
	private final ModelPart head;
	private final ModelPart crown;
	private final ModelPart body0;
	private final ModelPart body1;
	private final ModelPart leg0;
	private final ModelPart leg1;
	private final ModelPart leg2;
	private final ModelPart leg3;
	private final ModelPart leg4;
	private final ModelPart leg5;
	private final ModelPart leg6;
	private final ModelPart leg7;

	public Modelspider(ModelPart root) {
		this.head = root.getChild("head");
		this.crown = this.head.getChild("crown");
		this.body0 = root.getChild("body0");
		this.body1 = root.getChild("body1");
		this.leg0 = root.getChild("leg0");
		this.leg1 = root.getChild("leg1");
		this.leg2 = root.getChild("leg2");
		this.leg3 = root.getChild("leg3");
		this.leg4 = root.getChild("leg4");
		this.leg5 = root.getChild("leg5");
		this.leg6 = root.getChild("leg6");
		this.leg7 = root.getChild("leg7");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(32, 4).addBox(
				-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, -3.0F));

		PartDefinition crown = head.addOrReplaceChild("crown",
				CubeListBuilder.create().texOffs(49, 28)
						.addBox(0.0F, -6.0F, -9.0F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(50, 28)
						.addBox(-5.0F, -6.0F, -9.0F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 23)
						.addBox(-2.7F, -5.5F, -9.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(55, 22)
						.addBox(-0.5F, -5.5F, -9.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(60, 23)
						.addBox(1.7F, -5.5F, -9.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(50, 28)
						.addBox(-5.0F, -7.0F, -9.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(50, 28)
						.addBox(3.0F, -7.0F, -9.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(50, 28)
						.addBox(-1.5F, -7.0F, -9.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(50, 28)
						.addBox(3.0F, -7.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(50, 28)
						.addBox(-1.5F, -7.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(50, 28)
						.addBox(-5.0F, -7.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(49, 28)
						.addBox(0.0F, -6.0F, 0.0F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(50, 28)
						.addBox(-5.0F, -6.0F, 0.0F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 0.3F, 0.0F));

		PartDefinition cube_r1 = crown.addOrReplaceChild("cube_r1",
				CubeListBuilder.create().texOffs(50, 28)
						.addBox(-1.5F, 0.0F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(51, 28)
						.addBox(-4.0F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(50, 28)
						.addBox(3.0F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(4.5F, -7.0F, -4.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r2 = crown.addOrReplaceChild("cube_r2",
				CubeListBuilder.create().texOffs(51, 28)
						.addBox(-4.0F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(50, 28)
						.addBox(-1.5F, 0.0F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(50, 28)
						.addBox(3.0F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-4.5F, -7.0F, -4.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r3 = crown.addOrReplaceChild("cube_r3",
				CubeListBuilder.create().texOffs(51, 28)
						.addBox(-4.0F, -0.5F, -5.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(49, 28)
						.addBox(0.0F, -0.5F, -5.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(49, 28)
						.addBox(0.0F, -0.5F, 4.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(51, 28)
						.addBox(-4.0F, -0.5F, 4.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.5F, -4.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition body0 = partdefinition.addOrReplaceChild("body0", CubeListBuilder.create().texOffs(0, 0).addBox(
				-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, 0.0F));

		PartDefinition body1 = partdefinition.addOrReplaceChild("body1", CubeListBuilder.create().texOffs(0, 12)
				.addBox(-5.0F, -4.0F, -6.0F, 10.0F, 8.0F, 12.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 15.0F, 9.0F));

		PartDefinition leg0 = partdefinition.addOrReplaceChild("leg0",
				CubeListBuilder.create().texOffs(18, 0).addBox(-15.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-4.0F, 15.0F, 2.0F, 0.0F, 0.7854F, -0.7854F));

		PartDefinition leg1 = partdefinition.addOrReplaceChild("leg1",
				CubeListBuilder.create().texOffs(18, 0).addBox(-1.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(4.0F, 15.0F, 2.0F, 0.0F, -0.7854F, 0.7854F));

		PartDefinition leg2 = partdefinition.addOrReplaceChild("leg2",
				CubeListBuilder.create().texOffs(18, 0).addBox(-15.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-4.0F, 15.0F, 1.0F, 0.0F, 0.2618F, -0.6109F));

		PartDefinition leg3 = partdefinition.addOrReplaceChild("leg3",
				CubeListBuilder.create().texOffs(18, 0).addBox(-1.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(4.0F, 15.0F, 1.0F, 0.0F, -0.2618F, 0.6109F));

		PartDefinition leg4 = partdefinition.addOrReplaceChild("leg4",
				CubeListBuilder.create().texOffs(18, 0).addBox(-15.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-4.0F, 15.0F, 0.0F, 0.0F, -0.2618F, -0.6109F));

		PartDefinition leg5 = partdefinition.addOrReplaceChild("leg5",
				CubeListBuilder.create().texOffs(18, 0).addBox(-1.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(4.0F, 15.0F, 0.0F, 0.0F, 0.2618F, 0.6109F));

		PartDefinition leg6 = partdefinition.addOrReplaceChild("leg6",
				CubeListBuilder.create().texOffs(17, -1).addBox(-15.0F, -1.0F, -2.0F, 16.0F, 2.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-4.0F, 15.0F, -1.0F, 0.0F, -0.7854F, -0.7854F));

		PartDefinition leg7 = partdefinition.addOrReplaceChild("leg7",
				CubeListBuilder.create().texOffs(18, 0).addBox(-1.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(4.0F, 15.0F, -1.0F, 0.0F, 0.7854F, 0.7854F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		body0.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		body1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leg0.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leg1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leg2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leg3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leg4.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leg5.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leg6.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leg7.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
		this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.head.xRot = headPitch / (180F / (float) Math.PI);
		this.leg0.yRot = Mth.cos(limbSwing * 1.0F) * -1.0F * limbSwingAmount;
		this.leg1.yRot = Mth.cos(limbSwing * 1.0F) * 1.0F * limbSwingAmount;
		this.leg4.yRot = Mth.cos(limbSwing * 1.0F) * -1.0F * limbSwingAmount;
		this.leg5.yRot = Mth.cos(limbSwing * 1.0F) * 1.0F * limbSwingAmount;
		this.leg2.yRot = Mth.cos(limbSwing * 1.0F) * 1.0F * limbSwingAmount;
		this.leg3.yRot = Mth.cos(limbSwing * 1.0F) * -1.0F * limbSwingAmount;
		this.leg6.yRot = Mth.cos(limbSwing * 1.0F) * 1.0F * limbSwingAmount;
		this.leg7.yRot = Mth.cos(limbSwing * 1.0F) * -1.0F * limbSwingAmount;
	}
}