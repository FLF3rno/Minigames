// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class Modelflavio_sweeper<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "flavio_sweeper"), "main");
	private final ModelPart sweeper;
	private final ModelPart rotating_thingy;
	private final ModelPart ouchyy_thingy;

	public Modelflavio_sweeper(ModelPart root) {
		this.sweeper = root.getChild("sweeper");
		this.rotating_thingy = this.sweeper.getChild("rotating_thingy");
		this.ouchyy_thingy = this.rotating_thingy.getChild("ouchyy_thingy");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition sweeper = partdefinition.addOrReplaceChild("sweeper",
				CubeListBuilder.create().texOffs(42, 56)
						.addBox(-7.0F, -19.0F, 3.0F, 4.0F, 19.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(0, 18)
						.addBox(-5.0F, -27.0F, -7.0F, 10.0F, 8.0F, 14.0F, new CubeDeformation(0.0F)).texOffs(48, 18)
						.addBox(-7.0F, -25.0F, -5.0F, 2.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(72, 18)
						.addBox(5.0F, -25.0F, -5.0F, 2.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(60, 56)
						.addBox(3.0F, -19.0F, 3.0F, 4.0F, 19.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(42, 40)
						.addBox(-4.5F, -19.0F, -6.3F, 9.0F, 3.0F, 13.0F, new CubeDeformation(0.0F)).texOffs(0, 67)
						.addBox(3.0F, -19.0F, -8.0F, 4.0F, 19.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(18, 67)
						.addBox(-7.0F, -19.0F, -8.0F, 4.0F, 19.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r1 = sweeper.addOrReplaceChild("cube_r1",
				CubeListBuilder.create().texOffs(96, 124).addBox(-3.0F, -1.0F, -1.0F, 6.0F, 3.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -20.0F, 6.4F, -0.4363F, 0.0F, 0.0F));

		PartDefinition cube_r2 = sweeper.addOrReplaceChild("cube_r2",
				CubeListBuilder.create().texOffs(112, 66).addBox(-3.0F, -2.0F, -1.0F, 6.0F, 4.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -22.0F, 7.0F, 0.3054F, 0.0F, 0.0F));

		PartDefinition cube_r3 = sweeper.addOrReplaceChild("cube_r3",
				CubeListBuilder.create().texOffs(78, 66).addBox(-2.0F, -3.0F, -2.5F, 4.0F, 5.0F, 5.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(6.3F, -22.6F, -7.5F, 0.0F, -0.4363F, 0.0F));

		PartDefinition cube_r4 = sweeper.addOrReplaceChild("cube_r4",
				CubeListBuilder.create().texOffs(78, 56).addBox(-2.0F, -3.0F, -2.5F, 4.0F, 5.0F, 5.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-5.7F, -22.6F, -7.5F, 0.0F, 0.2618F, 0.0F));

		PartDefinition rotating_thingy = sweeper.addOrReplaceChild("rotating_thingy", CubeListBuilder.create()
				.texOffs(0, 40).addBox(-5.0F, -8.0F, -5.5F, 10.0F, 16.0F, 11.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -8.0F, 0.5F));

		PartDefinition ouchyy_thingy = rotating_thingy
				.addOrReplaceChild(
						"ouchyy_thingy", CubeListBuilder.create().texOffs(0, 0).addBox(-186.2815F, -4.5985F, -4.5F,
								367.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)),
						PartPose.offset(3.2815F, 6.5985F, 0.0F));

		PartDefinition cube_r5 = ouchyy_thingy.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(64, 124)
				.addBox(-54.6854F, 35.1103F, -39.1103F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(52, 80)
				.addBox(-59.169F, 32.0691F, -36.0691F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(16, 123)
				.addBox(-42.9489F, 21.5594F, -25.5594F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(0, 123)
				.addBox(-38.4653F, 24.6006F, -28.6006F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(48, 120)
				.addBox(-21.5079F, 13.6132F, -17.6132F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(80, 124)
				.addBox(-25.9915F, 10.572F, -14.572F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(32, 120)
				.addBox(-8.2968F, -0.8932F, -3.1068F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(118, 42)
				.addBox(-3.8132F, 2.148F, -6.148F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(118, 34)
				.addBox(13.1441F, -8.8394F, 4.8394F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(112, 116)
				.addBox(8.6606F, -11.8806F, 7.8806F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(96, 116)
				.addBox(24.1434F, -21.9126F, 17.9126F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(78, 76)
				.addBox(28.627F, -18.8714F, 14.8714F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(116, 88)
				.addBox(41.8979F, -27.4703F, 23.4703F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(48, 32)
				.addBox(37.4144F, -30.5115F, 26.5115F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(116, 80)
				.addBox(55.169F, -36.0691F, 32.0691F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(80, 116)
				.addBox(50.6854F, -39.1103F, 35.1103F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(85.9185F, 0.0015F, 0.0F, -0.8445F, 0.7409F, -0.0399F));

		PartDefinition cube_r6 = ouchyy_thingy.addOrReplaceChild("cube_r6",
				CubeListBuilder.create().texOffs(64, 116).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(170.2185F, -3.0985F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r7 = ouchyy_thingy.addOrReplaceChild("cube_r7",
				CubeListBuilder.create().texOffs(16, 115).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(170.6185F, 3.1015F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r8 = ouchyy_thingy.addOrReplaceChild("cube_r8",
				CubeListBuilder.create().texOffs(0, 115).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(152.2185F, -3.0985F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r9 = ouchyy_thingy.addOrReplaceChild("cube_r9",
				CubeListBuilder.create().texOffs(112, 108).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(152.6185F, 3.1015F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r10 = ouchyy_thingy.addOrReplaceChild("cube_r10",
				CubeListBuilder.create().texOffs(112, 100).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(134.6185F, 3.1015F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r11 = ouchyy_thingy.addOrReplaceChild("cube_r11",
				CubeListBuilder.create().texOffs(112, 58).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(134.2185F, -3.0985F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r12 = ouchyy_thingy.addOrReplaceChild("cube_r12",
				CubeListBuilder.create().texOffs(112, 50).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(113.2185F, -3.0985F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r13 = ouchyy_thingy.addOrReplaceChild("cube_r13",
				CubeListBuilder.create().texOffs(48, 112).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(113.6185F, 3.1015F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r14 = ouchyy_thingy.addOrReplaceChild("cube_r14",
				CubeListBuilder.create().texOffs(32, 112).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(90.6185F, 3.1015F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r15 = ouchyy_thingy.addOrReplaceChild("cube_r15",
				CubeListBuilder.create().texOffs(112, 26).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(90.2185F, -3.0985F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r16 = ouchyy_thingy.addOrReplaceChild("cube_r16",
				CubeListBuilder.create().texOffs(112, 18).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(66.2185F, -3.0985F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r17 = ouchyy_thingy.addOrReplaceChild("cube_r17",
				CubeListBuilder.create().texOffs(110, 72).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(66.6185F, 3.1015F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r18 = ouchyy_thingy.addOrReplaceChild("cube_r18",
				CubeListBuilder.create().texOffs(96, 108).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(43.6185F, 3.1015F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r19 = ouchyy_thingy.addOrReplaceChild("cube_r19",
				CubeListBuilder.create().texOffs(80, 108).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(43.2185F, -3.0985F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r20 = ouchyy_thingy.addOrReplaceChild("cube_r20",
				CubeListBuilder.create().texOffs(64, 108).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(21.2185F, -3.0985F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r21 = ouchyy_thingy.addOrReplaceChild("cube_r21",
				CubeListBuilder.create().texOffs(16, 107).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(21.6185F, 3.1015F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r22 = ouchyy_thingy.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(0, 107)
				.addBox(50.6854F, -39.1103F, 35.1103F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(48, 104)
				.addBox(55.1689F, -36.0691F, 32.0691F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(32, 104)
				.addBox(37.4144F, -30.5115F, 26.5115F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(102, 42)
				.addBox(41.8979F, -27.4703F, 23.4703F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(64, 32)
				.addBox(28.627F, -18.8714F, 14.8714F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(102, 34)
				.addBox(24.1434F, -21.9126F, 17.9126F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(96, 100)
				.addBox(8.6606F, -11.8806F, 7.8806F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(100, 92)
				.addBox(13.1441F, -8.8394F, 4.8394F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(100, 84)
				.addBox(-3.8132F, 2.148F, -6.148F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(80, 100)
				.addBox(-8.2968F, -0.8932F, -3.1068F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(64, 100)
				.addBox(-25.9915F, 10.572F, -14.572F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(16, 99)
				.addBox(-21.5079F, 13.6132F, -17.6132F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(0, 99)
				.addBox(-38.4653F, 24.6006F, -28.6006F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(96, 64)
				.addBox(-42.9489F, 21.5594F, -25.5594F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(36, 80)
				.addBox(-59.169F, 32.0691F, -36.0691F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(96, 56)
				.addBox(-54.6854F, 35.1103F, -39.1103F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-102.0815F, 0.0015F, 0.0F, -0.8445F, 0.7409F, -0.0399F));

		PartDefinition cube_r23 = ouchyy_thingy.addOrReplaceChild("cube_r23",
				CubeListBuilder.create().texOffs(48, 96).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-166.3815F, 3.1015F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r24 = ouchyy_thingy.addOrReplaceChild("cube_r24",
				CubeListBuilder.create().texOffs(32, 96).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-166.7815F, -3.0985F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r25 = ouchyy_thingy.addOrReplaceChild("cube_r25",
				CubeListBuilder.create().texOffs(96, 26).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-144.7815F, -3.0985F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r26 = ouchyy_thingy.addOrReplaceChild("cube_r26",
				CubeListBuilder.create().texOffs(96, 18).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-144.3815F, 3.1015F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r27 = ouchyy_thingy.addOrReplaceChild("cube_r27",
				CubeListBuilder.create().texOffs(94, 76).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-121.3815F, 3.1015F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r28 = ouchyy_thingy.addOrReplaceChild("cube_r28",
				CubeListBuilder.create().texOffs(84, 92).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-121.7815F, -3.0985F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r29 = ouchyy_thingy.addOrReplaceChild("cube_r29",
				CubeListBuilder.create().texOffs(68, 92).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-97.7815F, -3.0985F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r30 = ouchyy_thingy.addOrReplaceChild("cube_r30",
				CubeListBuilder.create().texOffs(16, 91).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-97.3815F, 3.1015F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r31 = ouchyy_thingy.addOrReplaceChild("cube_r31",
				CubeListBuilder.create().texOffs(0, 91).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-74.3815F, 3.1015F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r32 = ouchyy_thingy.addOrReplaceChild("cube_r32",
				CubeListBuilder.create().texOffs(52, 88).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-74.7815F, -3.0985F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r33 = ouchyy_thingy.addOrReplaceChild("cube_r33",
				CubeListBuilder.create().texOffs(36, 88).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-53.7815F, -3.0985F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r34 = ouchyy_thingy.addOrReplaceChild("cube_r34",
				CubeListBuilder.create().texOffs(86, 48).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-53.3815F, 3.1015F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r35 = ouchyy_thingy.addOrReplaceChild("cube_r35",
				CubeListBuilder.create().texOffs(86, 40).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-35.3815F, 3.1015F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r36 = ouchyy_thingy.addOrReplaceChild("cube_r36",
				CubeListBuilder.create().texOffs(84, 84).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-35.7815F, -3.0985F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r37 = ouchyy_thingy.addOrReplaceChild("cube_r37",
				CubeListBuilder.create().texOffs(80, 32).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-17.3815F, 3.1015F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		PartDefinition cube_r38 = ouchyy_thingy.addOrReplaceChild("cube_r38",
				CubeListBuilder.create().texOffs(68, 84).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-17.7815F, -3.0985F, 0.0F, 0.7854F, 0.0F, 0.7418F));

		return LayerDefinition.create(meshdefinition, 1024, 1024);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		sweeper.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
	}
}