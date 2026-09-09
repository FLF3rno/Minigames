// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class Modellaser_statue<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "laser_statue"), "main");
	private final ModelPart body;
	private final ModelPart arms;
	private final ModelPart left_arm;
	private final ModelPart left_hand;
	private final ModelPart gem;
	private final ModelPart right_arm;
	private final ModelPart right_hand;
	private final ModelPart left_wings_1;
	private final ModelPart left_wings_2;
	private final ModelPart left_wings_3;
	private final ModelPart right_wings_1;
	private final ModelPart right_wings2;
	private final ModelPart right_wings_3;

	public Modellaser_statue(ModelPart root) {
		this.body = root.getChild("body");
		this.arms = this.body.getChild("arms");
		this.left_arm = this.arms.getChild("left_arm");
		this.left_hand = this.left_arm.getChild("left_hand");
		this.gem = this.left_hand.getChild("gem");
		this.right_arm = this.arms.getChild("right_arm");
		this.right_hand = this.right_arm.getChild("right_hand");
		this.left_wings_1 = this.body.getChild("left_wings_1");
		this.left_wings_2 = this.left_wings_1.getChild("left_wings_2");
		this.left_wings_3 = this.left_wings_2.getChild("left_wings_3");
		this.right_wings_1 = this.body.getChild("right_wings_1");
		this.right_wings2 = this.right_wings_1.getChild("right_wings2");
		this.right_wings_3 = this.right_wings2.getChild("right_wings_3");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 19)
				.addBox(-3.5607F, 1.9643F, -4.8571F, 3.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(22, 31)
				.addBox(0.2393F, 1.9643F, -4.8571F, 3.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
				.addBox(-5.1607F, -13.0357F, -5.8571F, 10.0F, 9.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(22, 19)
				.addBox(-3.1607F, -4.0357F, -3.8571F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.1607F, 16.0357F, 0.8571F));

		PartDefinition arms = body.addOrReplaceChild("arms", CubeListBuilder.create(),
				PartPose.offset(-0.0357F, -0.2857F, -0.8571F));

		PartDefinition left_arm = arms.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(44, 31)
				.addBox(0.25F, -1.5F, -2.0F, 9.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offset(2.625F, -0.25F, 0.0F));

		PartDefinition left_hand = left_arm.addOrReplaceChild("left_hand", CubeListBuilder.create().texOffs(44, 38)
				.addBox(0.0F, -1.5F, -2.0F, 9.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offset(9.25F, 0.0F, 0.0F));

		PartDefinition gem = left_hand.addOrReplaceChild("gem", CubeListBuilder.create(),
				PartPose.offset(10.5F, -2.9F, -0.4929F));

		PartDefinition cube_r1 = gem.addOrReplaceChild("cube_r1",
				CubeListBuilder.create().texOffs(0, 108).addBox(-5.0F, -6.0F, -5.0F, 10.0F, 10.0F, 10.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.5F, 0.5F, -0.7071F, -0.7854F, 0.0F, 0.7854F));

		PartDefinition right_arm = arms.addOrReplaceChild("right_arm", CubeListBuilder.create(),
				PartPose.offset(-3.375F, -0.25F, 0.0F));

		PartDefinition cube_r2 = right_arm.addOrReplaceChild("cube_r2",
				CubeListBuilder.create().texOffs(40, 12).addBox(-0.5F, -1.5F, -2.0F, 10.0F, 3.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.25F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition right_hand = right_arm.addOrReplaceChild("right_hand", CubeListBuilder.create(),
				PartPose.offset(-9.75F, 0.0F, 0.0F));

		PartDefinition cube_r3 = right_hand
				.addOrReplaceChild("cube_r3",
						CubeListBuilder.create().texOffs(0, 45).addBox(0.0F, -1.5F, -2.0F, 9.0F, 3.0F, 4.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition left_wings_1 = body.addOrReplaceChild("left_wings_1", CubeListBuilder.create(),
				PartPose.offset(-0.1607F, -0.0357F, 2.1429F));

		PartDefinition cube_r4 = left_wings_1.addOrReplaceChild("cube_r4",
				CubeListBuilder.create().texOffs(50, 45).addBox(-2.5F, -14.0F, 0.0F, 6.0F, 21.0F, 0.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(2.5F, -1.0F, 0.7F, 0.0F, -0.2618F, 0.0F));

		PartDefinition left_wings_2 = left_wings_1.addOrReplaceChild("left_wings_2", CubeListBuilder.create(),
				PartPose.offset(5.8F, 0.0F, 1.55F));

		PartDefinition cube_r5 = left_wings_2.addOrReplaceChild("cube_r5",
				CubeListBuilder.create().texOffs(0, 52).addBox(-2.5F, -14.0F, 0.0F, 6.0F, 21.0F, 0.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(2.5F, -1.0F, 0.7F, 0.0F, -0.2618F, 0.0F));

		PartDefinition left_wings_3 = left_wings_2.addOrReplaceChild("left_wings_3", CubeListBuilder.create(),
				PartPose.offset(6.0F, 0.0F, 1.6F));

		PartDefinition cube_r6 = left_wings_3.addOrReplaceChild("cube_r6",
				CubeListBuilder.create().texOffs(62, 45).addBox(-2.5F, -14.0F, 0.0F, 6.0F, 21.0F, 0.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(2.3F, -1.0F, 0.65F, 0.0F, -0.2618F, 0.0F));

		PartDefinition right_wings_1 = body.addOrReplaceChild("right_wings_1", CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.8393F, -0.0357F, 2.1429F, -3.1416F, -0.5236F, 3.1416F));

		PartDefinition cube_r7 = right_wings_1.addOrReplaceChild("cube_r7",
				CubeListBuilder.create().texOffs(50, 45).addBox(-2.5F, -14.0F, 0.0F, 6.0F, 21.0F, 0.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(2.5F, -1.0F, 0.7F, 0.0F, -0.2618F, 0.0F));

		PartDefinition right_wings2 = right_wings_1.addOrReplaceChild("right_wings2", CubeListBuilder.create(),
				PartPose.offset(5.8F, 0.0F, 1.55F));

		PartDefinition cube_r8 = right_wings2.addOrReplaceChild("cube_r8",
				CubeListBuilder.create().texOffs(0, 52).addBox(-2.5F, -14.0F, 0.0F, 6.0F, 21.0F, 0.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(2.5F, -1.0F, 0.7F, 0.0F, -0.2618F, 0.0F));

		PartDefinition right_wings_3 = right_wings2.addOrReplaceChild("right_wings_3", CubeListBuilder.create(),
				PartPose.offset(6.0F, 0.0F, 1.6F));

		PartDefinition cube_r9 = right_wings_3.addOrReplaceChild("cube_r9",
				CubeListBuilder.create().texOffs(62, 45).addBox(-2.5F, -14.0F, 0.0F, 6.0F, 21.0F, 0.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(2.3F, -1.0F, 0.65F, 0.0F, -0.2618F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
	}
}