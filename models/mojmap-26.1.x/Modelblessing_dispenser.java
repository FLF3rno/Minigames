// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class Modelblessing_dispenser<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "blessing_dispenser"), "main");
	private final ModelPart blessing_dispenser;
	private final ModelPart base;
	private final ModelPart insidebottom;
	private final ModelPart insidemiddle;
	private final ModelPart insidetop;
	private final ModelPart insidetoptop;
	private final ModelPart insidetopmost;
	private final ModelPart middle;
	private final ModelPart head;
	private final ModelPart top;
	private final ModelPart spikes;
	private final ModelPart bottom;
	private final ModelPart eye;

	public Modelblessing_dispenser(ModelPart root) {
		this.blessing_dispenser = root.getChild("blessing_dispenser");
		this.base = this.blessing_dispenser.getChild("base");
		this.insidebottom = this.base.getChild("insidebottom");
		this.insidemiddle = this.base.getChild("insidemiddle");
		this.insidetop = this.base.getChild("insidetop");
		this.insidetoptop = this.base.getChild("insidetoptop");
		this.insidetopmost = this.base.getChild("insidetopmost");
		this.middle = this.blessing_dispenser.getChild("middle");
		this.head = this.blessing_dispenser.getChild("head");
		this.top = this.head.getChild("top");
		this.spikes = this.top.getChild("spikes");
		this.bottom = this.head.getChild("bottom");
		this.eye = this.head.getChild("eye");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition blessing_dispenser = partdefinition.addOrReplaceChild("blessing_dispenser",
				CubeListBuilder.create(), PartPose.offsetAndRotation(2.0F, 24.0667F, -1.5F, 0.0F, -1.1781F, 0.0F));

		PartDefinition base = blessing_dispenser.addOrReplaceChild("base", CubeListBuilder.create().texOffs(54, 30)
				.addBox(-2.025F, -10.025F, 0.4F, 4.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 0.1333F, -4.0F));

		PartDefinition cube_r1 = base.addOrReplaceChild("cube_r1",
				CubeListBuilder.create().texOffs(64, 70).addBox(-2.0125F, -5.0505F, -0.4841F, 4.0F, 10.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-3.6125F, -4.9745F, 9.5841F, -3.0849F, 0.6973F, -3.1013F));

		PartDefinition cube_r2 = base.addOrReplaceChild("cube_r2",
				CubeListBuilder.create().texOffs(6, 76).addBox(-1.9875F, -4.8607F, -1.4993F, 4.0F, 10.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-3.6125F, -4.9745F, 9.5841F, 3.0238F, 0.6973F, -3.1013F));

		PartDefinition cube_r3 = base.addOrReplaceChild("cube_r3",
				CubeListBuilder.create().texOffs(6, 64).addBox(-1.9875F, -4.8607F, -1.4993F, 4.0F, 10.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(3.6875F, -4.9745F, 9.4091F, 2.9671F, -0.8203F, -3.1416F));

		PartDefinition cube_r4 = base.addOrReplaceChild("cube_r4",
				CubeListBuilder.create().texOffs(18, 68).addBox(-2.0125F, -5.0505F, -0.4841F, 4.0F, 10.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(3.6875F, -4.9745F, 9.4091F, -3.1416F, -0.8203F, 3.1416F));

		PartDefinition cube_r5 = base.addOrReplaceChild("cube_r5",
				CubeListBuilder.create().texOffs(74, 12).addBox(-2.0F, -10.0F, -1.0F, 4.0F, 10.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, -0.1745F, 0.0F, 0.0F));

		PartDefinition insidebottom = base.addOrReplaceChild("insidebottom",
				CubeListBuilder.create().texOffs(54, 42)
						.addBox(-3.9996F, -1.0F, -0.9912F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 54)
						.addBox(-0.9996F, -1.0F, -3.9912F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-0.0004F, -1.0F, 5.9912F));

		PartDefinition cube_r6 = insidebottom.addOrReplaceChild("cube_r6",
				CubeListBuilder.create().texOffs(10, 9).addBox(-2.0F, -1.0F, -3.5F, 4.0F, 2.0F, 7.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0173F, 0.0F, -0.0096F, 0.0F, -0.7418F, 0.0F));

		PartDefinition cube_r7 = insidebottom.addOrReplaceChild("cube_r7",
				CubeListBuilder.create().texOffs(10, 0).addBox(-2.0F, -2.0F, -3.525F, 4.0F, 2.0F, 7.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0004F, 1.0F, 0.0088F, 0.0F, 0.829F, 0.0F));

		PartDefinition insidemiddle = base.addOrReplaceChild("insidemiddle",
				CubeListBuilder.create().texOffs(18, 64)
						.addBox(-3.9996F, -1.0F, -0.9912F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(54, 0)
						.addBox(-0.9996F, -1.0F, -3.9912F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.0004F, -3.0F, 5.9912F, 0.0F, 0.7418F, 0.0F));

		PartDefinition cube_r8 = insidemiddle.addOrReplaceChild("cube_r8",
				CubeListBuilder.create().texOffs(0, 27).addBox(-2.0F, -1.0F, -3.5F, 4.0F, 2.0F, 7.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0173F, 0.0F, -0.0096F, 0.0F, -0.7418F, 0.0F));

		PartDefinition cube_r9 = insidemiddle.addOrReplaceChild("cube_r9",
				CubeListBuilder.create().texOffs(10, 18).addBox(-2.0F, -2.0F, -3.525F, 4.0F, 2.0F, 7.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0004F, 1.0F, 0.0088F, 0.0F, 0.829F, 0.0F));

		PartDefinition insidetop = base.addOrReplaceChild("insidetop",
				CubeListBuilder.create().texOffs(64, 46)
						.addBox(-3.9996F, -1.0F, -0.9912F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(54, 10)
						.addBox(-0.9996F, -1.0F, -3.9912F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-0.0004F, -5.0F, 5.9912F));

		PartDefinition cube_r10 = insidetop.addOrReplaceChild("cube_r10",
				CubeListBuilder.create().texOffs(32, 0).addBox(-2.0F, -1.0F, -3.5F, 4.0F, 2.0F, 7.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0173F, 0.0F, -0.0096F, 0.0F, -0.7418F, 0.0F));

		PartDefinition cube_r11 = insidetop.addOrReplaceChild("cube_r11",
				CubeListBuilder.create().texOffs(22, 27).addBox(-2.0F, -2.0F, -3.525F, 4.0F, 2.0F, 7.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0004F, 1.0F, 0.0088F, 0.0F, 0.829F, 0.0F));

		PartDefinition insidetoptop = base.addOrReplaceChild("insidetoptop",
				CubeListBuilder.create().texOffs(64, 50)
						.addBox(-3.9996F, -1.0F, -0.9912F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(20, 54)
						.addBox(-0.9996F, -1.0F, -3.9912F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.0004F, -7.0F, 5.9912F, 0.0F, 0.7418F, 0.0F));

		PartDefinition cube_r12 = insidetoptop.addOrReplaceChild("cube_r12",
				CubeListBuilder.create().texOffs(32, 18).addBox(-2.0F, -1.0F, -3.5F, 4.0F, 2.0F, 7.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0173F, 0.0F, -0.0096F, 0.0F, -0.7418F, 0.0F));

		PartDefinition cube_r13 = insidetoptop.addOrReplaceChild("cube_r13",
				CubeListBuilder.create().texOffs(32, 9).addBox(-2.0F, -2.0F, -3.525F, 4.0F, 2.0F, 7.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0004F, 1.0F, 0.0088F, 0.0F, 0.829F, 0.0F));

		PartDefinition insidetopmost = base.addOrReplaceChild("insidetopmost",
				CubeListBuilder.create().texOffs(64, 54)
						.addBox(-3.9996F, -1.0F, -0.9912F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(44, 48)
						.addBox(-0.9996F, -1.0F, -3.9912F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-0.0004F, -9.0F, 5.9912F));

		PartDefinition cube_r14 = insidetopmost.addOrReplaceChild("cube_r14",
				CubeListBuilder.create().texOffs(22, 36).addBox(-2.0F, -1.0F, -3.5F, 4.0F, 2.0F, 7.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0173F, 0.0F, -0.0096F, 0.0F, -0.7418F, 0.0F));

		PartDefinition cube_r15 = insidetopmost.addOrReplaceChild("cube_r15",
				CubeListBuilder.create().texOffs(0, 36).addBox(-2.0F, -2.0F, -3.525F, 4.0F, 2.0F, 7.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0004F, 1.0F, 0.0088F, 0.0F, 0.829F, 0.0F));

		PartDefinition middle = blessing_dispenser.addOrReplaceChild("middle", CubeListBuilder.create(),
				PartPose.offset(0.0F, -0.0667F, 2.0F));

		PartDefinition cube_r16 = middle.addOrReplaceChild("cube_r16",
				CubeListBuilder.create().texOffs(56, 58).addBox(-1.8F, -8.8F, -2.0F, 2.0F, 17.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.9277F, -17.2345F, -1.8315F, -0.1374F, 0.079F, -0.0309F));

		PartDefinition cube_r17 = middle.addOrReplaceChild("cube_r17",
				CubeListBuilder.create().texOffs(48, 58).addBox(-5.9875F, -11.1607F, -4.2993F, 2.0F, 17.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-3.6125F, -14.7745F, 3.5841F, 2.9705F, -0.8639F, -3.0941F));

		PartDefinition cube_r18 = middle.addOrReplaceChild("cube_r18",
				CubeListBuilder.create().texOffs(40, 58).addBox(-1.1875F, -11.7607F, 1.1007F, 2.0F, 17.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-3.6125F, -14.7745F, 3.5841F, 3.0325F, 0.6973F, -3.1013F));

		PartDefinition cube_r19 = middle.addOrReplaceChild("cube_r19",
				CubeListBuilder.create().texOffs(0, 64).addBox(2.9F, -15.8F, -1.6F, 0.0F, 17.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -10.0F, 0.0F, 1.5708F, -1.4835F, -1.5708F));

		PartDefinition cube_r20 = middle.addOrReplaceChild("cube_r20",
				CubeListBuilder.create().texOffs(0, 0).addBox(1.9F, -15.8F, -2.3F, 0.0F, 17.0F, 5.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -10.0F, 0.0F, -3.0963F, 0.478F, -3.0433F));

		PartDefinition cube_r21 = middle.addOrReplaceChild("cube_r21",
				CubeListBuilder.create().texOffs(44, 27).addBox(2.1F, -14.8F, -2.6F, 0.0F, 16.0F, 5.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -10.0F, 0.0F, -0.0361F, 0.3911F, -0.0944F));

		PartDefinition head = blessing_dispenser.addOrReplaceChild("head", CubeListBuilder.create(),
				PartPose.offset(0.0F, -0.0667F, 2.0F));

		PartDefinition top = head.addOrReplaceChild("top",
				CubeListBuilder.create().texOffs(64, 58)
						.addBox(-3.9996F, -1.0F, -0.9912F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(54, 20)
						.addBox(-0.9996F, -1.0F, -3.9912F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-0.0004F, -28.8F, -0.0088F));

		PartDefinition cube_r22 = top.addOrReplaceChild("cube_r22",
				CubeListBuilder.create().texOffs(22, 45).addBox(-2.0F, -1.0F, -3.5F, 4.0F, 2.0F, 7.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0173F, 0.0F, -0.0096F, 0.0F, -0.7418F, 0.0F));

		PartDefinition cube_r23 = top.addOrReplaceChild("cube_r23",
				CubeListBuilder.create().texOffs(0, 45).addBox(-2.0F, -2.0F, -3.525F, 4.0F, 2.0F, 7.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0004F, 1.0F, 0.0088F, 0.0F, 0.829F, 0.0F));

		PartDefinition spikes = top.addOrReplaceChild("spikes", CubeListBuilder.create(),
				PartPose.offset(-3.9535F, -3.9086F, 3.9776F));

		PartDefinition cube_r24 = spikes.addOrReplaceChild("cube_r24",
				CubeListBuilder.create().texOffs(76, 80).addBox(-2.2375F, -3.8607F, -0.5993F, 3.0F, 5.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.441F, -0.7004F, -0.4729F, 2.8929F, 0.6973F, -3.1013F));

		PartDefinition cube_r25 = spikes.addOrReplaceChild("cube_r25",
				CubeListBuilder.create().texOffs(18, 80).addBox(-1.475F, -0.8721F, -1.5207F, 3.0F, 7.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(7.4535F, 0.7986F, -1.8431F, -2.9803F, -0.9597F, -3.0878F));

		PartDefinition cube_r26 = spikes.addOrReplaceChild("cube_r26",
				CubeListBuilder.create().texOffs(28, 81).addBox(-1.525F, -5.0714F, -1.7516F, 3.0F, 5.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(7.4535F, 0.7986F, -1.8431F, 2.823F, -0.9597F, -3.0878F));

		PartDefinition cube_r27 = spikes.addOrReplaceChild("cube_r27",
				CubeListBuilder.create().texOffs(30, 68).addBox(-1.475F, -0.8721F, -1.5207F, 3.0F, 7.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(3.4535F, 0.7986F, -8.0431F, 0.2027F, 0.0877F, -0.031F));

		PartDefinition cube_r28 = spikes.addOrReplaceChild("cube_r28",
				CubeListBuilder.create().texOffs(80, 62).addBox(-1.525F, -5.0714F, -1.7516F, 3.0F, 5.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(3.4535F, 0.7986F, -8.0431F, -0.2773F, 0.0877F, -0.031F));

		PartDefinition cube_r29 = spikes.addOrReplaceChild("cube_r29",
				CubeListBuilder.create().texOffs(46, 77).addBox(-2.1875F, 0.1393F, -0.7993F, 3.0F, 7.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.3414F, -0.066F, -0.3846F, -2.9103F, 0.6973F, -3.1013F));

		PartDefinition bottom = head.addOrReplaceChild("bottom",
				CubeListBuilder.create().texOffs(64, 62)
						.addBox(-1.0471F, -1.0F, -3.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(30, 77)
						.addBox(-3.0471F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0471F, -27.0F, 0.0F));

		PartDefinition cube_r30 = bottom.addOrReplaceChild("cube_r30",
				CubeListBuilder.create().texOffs(76, 70)
						.addBox(-2.2F, -1.0F, -1.4F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(76, 75)
						.addBox(-2.8F, -1.0F, -1.4F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.0471F, 0.0F, 0.0F, -3.1416F, 0.7854F, 3.1416F));

		PartDefinition cube_r31 = bottom.addOrReplaceChild("cube_r31",
				CubeListBuilder.create().texOffs(74, 38)
						.addBox(-2.2F, -1.0F, -1.4F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(74, 24)
						.addBox(-2.8F, -1.0F, -1.4F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.0471F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition eye = head.addOrReplaceChild("eye", CubeListBuilder.create(),
				PartPose.offset(0.0F, -33.0F, 0.0F));

		PartDefinition cube_r32 = eye.addOrReplaceChild("cube_r32",
				CubeListBuilder.create().texOffs(66, 30).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.4956F, -0.1056F, 0.0385F, -0.456F, -0.6669F, 1.9697F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		blessing_dispenser.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
	}
}