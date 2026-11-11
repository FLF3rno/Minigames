// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class Modelpriest<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "priest"), "main");
	private final ModelPart entity;
	private final ModelPart rail;
	private final ModelPart cart;
	private final ModelPart wheelsfront;
	private final ModelPart wheelsback;
	private final ModelPart priest;
	private final ModelPart head;
	private final ModelPart arm;
	private final ModelPart pearl;

	public Modelpriest(ModelPart root) {
		this.entity = root.getChild("entity");
		this.rail = this.entity.getChild("rail");
		this.cart = this.entity.getChild("cart");
		this.wheelsfront = this.cart.getChild("wheelsfront");
		this.wheelsback = this.cart.getChild("wheelsback");
		this.priest = this.cart.getChild("priest");
		this.head = this.priest.getChild("head");
		this.arm = this.priest.getChild("arm");
		this.pearl = this.arm.getChild("pearl");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition entity = partdefinition.addOrReplaceChild("entity", CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition rail = entity.addOrReplaceChild("rail",
				CubeListBuilder.create().texOffs(0, 0)
						.addBox(-8.0F, 0.3333F, -16.0F, 16.0F, 0.0F, 32.0F, new CubeDeformation(0.0F)).texOffs(0, 32)
						.addBox(-6.0F, -0.6667F, -16.0F, 3.0F, 1.0F, 32.0F, new CubeDeformation(0.0F)).texOffs(0, 65)
						.addBox(3.0F, -0.6667F, -16.0F, 3.0F, 1.0F, 32.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -0.3333F, 8.0F));

		PartDefinition cart = entity.addOrReplaceChild("cart", CubeListBuilder.create().texOffs(70, 32).addBox(-7.0F,
				-8.5F, -8.0F, 14.0F, 7.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 2.0F));

		PartDefinition wheelsfront = cart.addOrReplaceChild("wheelsfront", CubeListBuilder.create().texOffs(62, 103)
				.addBox(-6.5F, -1.5F, -1.5F, 13.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -1.55F, -4.5F));

		PartDefinition wheelsback = cart.addOrReplaceChild("wheelsback", CubeListBuilder.create().texOffs(62, 103)
				.addBox(-6.5F, -1.5F, -1.5F, 13.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -1.5F, 4.5F));

		PartDefinition priest = cart.addOrReplaceChild("priest",
				CubeListBuilder.create().texOffs(70, 55)
						.addBox(-4.5F, -13.2F, -5.0F, 9.0F, 7.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(96, 0)
						.addBox(-4.51F, -18.2F, -5.0F, 9.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = priest.addOrReplaceChild("cube_r1",
				CubeListBuilder.create().texOffs(104, 95).addBox(-5.0F, -1.0F, -4.0F, 9.0F, 6.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.54F, -25.8704F, -0.5052F, -0.48F, 0.0F, 0.0F));

		PartDefinition cube_r2 = priest.addOrReplaceChild("cube_r2",
				CubeListBuilder.create().texOffs(70, 72).addBox(-5.0F, -1.0F, -4.0F, 9.0F, 6.0F, 9.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, -23.3704F, -2.3052F, 0.2618F, 0.0F, 0.0F));

		PartDefinition cube_r3 = priest.addOrReplaceChild("cube_r3",
				CubeListBuilder.create().texOffs(96, 13).addBox(-5.0F, -2.0F, -2.0F, 9.0F, 7.0F, 7.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.51F, -17.0821F, -0.7771F, 0.1745F, 0.0F, 0.0F));

		PartDefinition head = priest.addOrReplaceChild("head", CubeListBuilder.create(),
				PartPose.offsetAndRotation(-0.01F, -29.0704F, -3.6802F, 0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r4 = head.addOrReplaceChild("cube_r4",
				CubeListBuilder.create().texOffs(104, 87).addBox(-5.0F, -2.0F, -3.5F, 10.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.02F, -1.2136F, 5.0323F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r5 = head
				.addOrReplaceChild("cube_r5",
						CubeListBuilder.create().texOffs(32, 98).addBox(-5.0F, 1.0F, -8.0F, 10.0F, 4.0F, 5.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.03F, 1.1F, 9.1F, -0.48F, 0.0F, 0.0F));

		PartDefinition cube_r6 = head.addOrReplaceChild("cube_r6",
				CubeListBuilder.create().texOffs(0, 98).addBox(-5.0F, -4.0F, -5.5F, 10.0F, 6.0F, 6.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.01F, -3.9886F, 3.1823F, 0.9599F, 0.0F, 0.0F));

		PartDefinition cube_r7 = head.addOrReplaceChild("cube_r7",
				CubeListBuilder.create().texOffs(106, 72).addBox(-5.0F, -1.0F, -3.0F, 10.0F, 6.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.03F, -3.6F, 1.125F, -1.0036F, 0.0F, 0.0F));

		PartDefinition cube_r8 = head.addOrReplaceChild("cube_r8",
				CubeListBuilder.create().texOffs(70, 87).addBox(-5.0F, -4.0F, -4.0F, 10.0F, 9.0F, 7.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -0.325F, 0.1F, 0.2182F, 0.0F, 0.0F));

		PartDefinition arm = priest.addOrReplaceChild("arm", CubeListBuilder.create(),
				PartPose.offset(0.0051F, -16.8F, -6.2134F));

		PartDefinition cube_r9 = arm.addOrReplaceChild("cube_r9",
				CubeListBuilder.create().texOffs(2, 128).addBox(-2.0F, -4.0F, -6.0F, 3.0F, 4.0F, 7.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-5.7673F, -2.2F, 3.5647F, 0.0F, 0.1309F, 0.0F));

		PartDefinition cube_r10 = arm.addOrReplaceChild("cube_r10",
				CubeListBuilder.create().texOffs(67, 124).addBox(-2.0F, -4.0F, -6.0F, 3.0F, 4.0F, 7.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-6.2668F, -2.2F, -2.493F, 0.0F, -0.3491F, 0.0F));

		PartDefinition cube_r11 = arm.addOrReplaceChild("cube_r11",
				CubeListBuilder.create().texOffs(41, 131).addBox(-1.0F, -4.0F, -6.0F, 3.0F, 4.0F, 7.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(6.3399F, -2.2F, -1.6695F, 0.0F, 0.3491F, 0.0F));

		PartDefinition cube_r12 = arm.addOrReplaceChild("cube_r12",
				CubeListBuilder.create().texOffs(85, 175).addBox(-1.0F, -4.0F, -6.0F, 3.0F, 4.0F, 7.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(5.5949F, -2.2F, 4.2134F, 0.0F, -0.1745F, 0.0F));

		PartDefinition pearl = arm.addOrReplaceChild("pearl", CubeListBuilder.create().texOffs(86, 109).addBox(-2.1082F,
				-2.0823F, -2.009F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.1031F, -3.1177F, -7.7776F));

		PartDefinition cube_r13 = pearl.addOrReplaceChild("cube_r13",
				CubeListBuilder.create().texOffs(102, 109).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.2918F, 0.9177F, -0.509F, -0.7463F, -0.703F, 0.3226F));

		return LayerDefinition.create(meshdefinition, 256, 256);
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