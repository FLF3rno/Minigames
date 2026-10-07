// Made with Blockbench 5.2.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class Modelcrypt_lurker<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "crypt_lurker"), "main");
	private final ModelPart entity;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart right_arm;
	private final ModelPart right_arm2;
	private final ModelPart shield;
	private final ModelPart left_arm;
	private final ModelPart left_arm2;
	private final ModelPart right_leg;
	private final ModelPart right_leg2;
	private final ModelPart left_leg;
	private final ModelPart left_leg2;

	public Modelcrypt_lurker(ModelPart root) {
		this.entity = root.getChild("entity");
		this.body = this.entity.getChild("body");
		this.head = this.body.getChild("head");
		this.right_arm = this.body.getChild("right_arm");
		this.right_arm2 = this.right_arm.getChild("right_arm2");
		this.shield = this.right_arm2.getChild("shield");
		this.left_arm = this.body.getChild("left_arm");
		this.left_arm2 = this.left_arm.getChild("left_arm2");
		this.right_leg = this.entity.getChild("right_leg");
		this.right_leg2 = this.right_leg.getChild("right_leg2");
		this.left_leg = this.entity.getChild("left_leg");
		this.left_leg2 = this.left_leg.getChild("left_leg2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition entity = partdefinition.addOrReplaceChild("entity", CubeListBuilder.create(),
				PartPose.offset(0.0F, 20.0F, -4.0F));

		PartDefinition body = entity.addOrReplaceChild("body", CubeListBuilder.create(),
				PartPose.offset(0.0F, -12.0F, 5.0F));

		PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1",
				CubeListBuilder.create().texOffs(68, 94).addBox(-9.0F, -23.2628F, -6.6999F, 19.0F, 23.0F, 11.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.45F, 0.025F, 0.975F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r2 = body.addOrReplaceChild(
				"cube_r2", CubeListBuilder.create().texOffs(0, 96).addBox(-9.0F, -22.2628F, -5.6999F, 18.0F, 22.0F,
						10.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -0.6F, 0.7F, 0.2182F, 0.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create(),
				PartPose.offset(0.0F, -7.0F, -5.0F));

		PartDefinition cube_r3 = head.addOrReplaceChild("cube_r3",
				CubeListBuilder.create().texOffs(0, 57).addBox(-6.0F, -11.2628F, -4.6999F, 10.0F, 10.0F, 8.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(1.1F, -14.0F, 1.1F, 0.2182F, 0.0F, 0.0F));

		PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create(),
				PartPose.offsetAndRotation(-9.9F, -18.9F, -5.0F, -0.4949F, -0.0208F, -0.0348F));

		PartDefinition cube_r4 = right_arm.addOrReplaceChild("cube_r4",
				CubeListBuilder.create().texOffs(86, 0).addBox(-6.0F, -3.0237F, -4.2164F, 6.0F, 11.0F, 8.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, -0.8866F, 2.0776F, 0.2182F, 0.0F, 0.0F));

		PartDefinition right_arm2 = right_arm.addOrReplaceChild("right_arm2", CubeListBuilder.create(),
				PartPose.offsetAndRotation(-3.501F, 6.7278F, 3.8773F, -0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r5 = right_arm2.addOrReplaceChild("cube_r5",
				CubeListBuilder.create().texOffs(0, 75).addBox(-2.0F, -2.0948F, -4.8658F, 6.0F, 13.0F, 7.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.1356F, 0.5753F, 0.2182F, 0.0F, 0.0F));

		PartDefinition shield = right_arm2.addOrReplaceChild("shield", CubeListBuilder.create(),
				PartPose.offsetAndRotation(-0.5F, 11.6982F, 0.568F, 1.2654F, 0.0F, 0.0F));

		PartDefinition cube_r6 = shield.addOrReplaceChild("cube_r6",
				CubeListBuilder.create().texOffs(58, 20).addBox(-6.0F, 13.0F, -1.0F, 13.0F, 3.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, -5.9565F, -1.5951F, 0.2618F, 0.0F, 0.0F));

		PartDefinition cube_r7 = shield.addOrReplaceChild("cube_r7",
				CubeListBuilder.create().texOffs(36, 57).addBox(-6.0F, 17.0F, -1.0F, 7.0F, 3.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, -5.9565F, -1.5981F, 0.2618F, 0.0F, 0.0F));

		PartDefinition cube_r8 = shield.addOrReplaceChild("cube_r8",
				CubeListBuilder.create().texOffs(56, 56).addBox(-8.0F, -12.0F, -1.0F, 13.0F, 29.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, -5.9565F, -1.5991F, 0.2618F, 0.0F, 0.0F));

		PartDefinition cube_r9 = shield.addOrReplaceChild("cube_r9",
				CubeListBuilder.create().texOffs(58, 29).addBox(-9.0F, -11.0F, -1.0F, 17.0F, 25.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, -5.9565F, -1.5961F, 0.2618F, 0.0F, 0.0F));

		PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create(),
				PartPose.offsetAndRotation(9.0F, -20.0F, -6.0F, -0.1309F, 0.0F, 0.0F));

		PartDefinition cube_r10 = left_arm.addOrReplaceChild("cube_r10",
				CubeListBuilder.create().texOffs(86, 0).addBox(0.0F, -3.0237F, -4.2164F, 6.0F, 11.0F, 8.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.11F, 0.0134F, 2.0776F, 0.2182F, 0.0F, 0.0F));

		PartDefinition left_arm2 = left_arm.addOrReplaceChild("left_arm2", CubeListBuilder.create(),
				PartPose.offsetAndRotation(4.1F, 7.6278F, 3.8773F, -0.1309F, 0.0F, 0.0F));

		PartDefinition cube_r11 = left_arm2.addOrReplaceChild("cube_r11",
				CubeListBuilder.create().texOffs(0, 75).addBox(-3.0F, -1.0948F, -4.8658F, 6.0F, 12.0F, 8.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-1.0F, 0.1356F, 0.5753F, 0.2182F, 0.0F, 0.0F));

		PartDefinition right_leg = entity.addOrReplaceChild("right_leg", CubeListBuilder.create(),
				PartPose.offset(-4.9F, -12.5F, 4.4F));

		PartDefinition cube_r12 = right_leg.addOrReplaceChild("cube_r12",
				CubeListBuilder.create().texOffs(2, 77).addBox(-3.0F, -4.5F, -3.0F, 6.0F, 9.0F, 6.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.3778F, -0.4977F, -0.0436F, 0.0F, 0.0F));

		PartDefinition right_leg2 = right_leg.addOrReplaceChild("right_leg2", CubeListBuilder.create(),
				PartPose.offset(0.0F, 8.7278F, -0.5227F));

		PartDefinition cube_r13 = right_leg2.addOrReplaceChild("cube_r13",
				CubeListBuilder.create().texOffs(88, 3).addBox(-3.0F, 0.9052F, -3.8658F, 6.0F, 9.0F, 6.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.001F, -1.5644F, 0.5753F, 0.1309F, 0.0F, 0.0F));

		PartDefinition left_leg = entity.addOrReplaceChild("left_leg", CubeListBuilder.create(),
				PartPose.offset(5.0F, -12.1F, 3.8F));

		PartDefinition cube_r14 = left_leg.addOrReplaceChild("cube_r14",
				CubeListBuilder.create().texOffs(2, 77).addBox(-3.0F, -4.5F, -3.0F, 6.0F, 9.0F, 6.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.1F, 3.9778F, 0.1023F, -0.0436F, 0.0F, 0.0F));

		PartDefinition left_leg2 = left_leg.addOrReplaceChild("left_leg2", CubeListBuilder.create(),
				PartPose.offset(0.1F, 8.3278F, -0.4227F));

		PartDefinition cube_r15 = left_leg2.addOrReplaceChild("cube_r15",
				CubeListBuilder.create().texOffs(88, 2).addBox(-3.0F, 0.9051F, -3.8667F, 6.0F, 9.0F, 6.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.001F, -1.5644F, 1.0763F, 0.1309F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		entity.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
	}
}