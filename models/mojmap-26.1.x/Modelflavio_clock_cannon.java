// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class Modelflavio_clock_cannon<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "flavio_clock_cannon"), "main");
	private final ModelPart flavio_turret;
	private final ModelPart head;
	private final ModelPart Clocks;
	private final ModelPart Clock_1;
	private final ModelPart minute;
	private final ModelPart hour;
	private final ModelPart Clock_2;
	private final ModelPart minute2;
	private final ModelPart hour2;
	private final ModelPart Clock_3;
	private final ModelPart minute3;
	private final ModelPart hour3;
	private final ModelPart Clock_4;
	private final ModelPart minute4;
	private final ModelPart hour4;
	private final ModelPart hat;
	private final ModelPart main_structure;
	private final ModelPart filling;
	private final ModelPart filling2;
	private final ModelPart filling3;
	private final ModelPart filling4;
	private final ModelPart feet;
	private final ModelPart cogs;
	private final ModelPart cog1;
	private final ModelPart cog2;
	private final ModelPart cannon;
	private final ModelPart cannon_body;
	private final ModelPart cannon_head;
	private final ModelPart cannon_head2;
	private final ModelPart cannon_head3;
	private final ModelPart cannon_head4;

	public Modelflavio_clock_cannon(ModelPart root) {
		this.flavio_turret = root.getChild("flavio_turret");
		this.head = this.flavio_turret.getChild("head");
		this.Clocks = this.head.getChild("Clocks");
		this.Clock_1 = this.Clocks.getChild("Clock_1");
		this.minute = this.Clock_1.getChild("minute");
		this.hour = this.Clock_1.getChild("hour");
		this.Clock_2 = this.Clocks.getChild("Clock_2");
		this.minute2 = this.Clock_2.getChild("minute2");
		this.hour2 = this.Clock_2.getChild("hour2");
		this.Clock_3 = this.Clocks.getChild("Clock_3");
		this.minute3 = this.Clock_3.getChild("minute3");
		this.hour3 = this.Clock_3.getChild("hour3");
		this.Clock_4 = this.Clocks.getChild("Clock_4");
		this.minute4 = this.Clock_4.getChild("minute4");
		this.hour4 = this.Clock_4.getChild("hour4");
		this.hat = this.head.getChild("hat");
		this.main_structure = this.hat.getChild("main_structure");
		this.filling = this.hat.getChild("filling");
		this.filling2 = this.hat.getChild("filling2");
		this.filling3 = this.hat.getChild("filling3");
		this.filling4 = this.hat.getChild("filling4");
		this.feet = this.flavio_turret.getChild("feet");
		this.cogs = this.flavio_turret.getChild("cogs");
		this.cog1 = this.cogs.getChild("cog1");
		this.cog2 = this.cogs.getChild("cog2");
		this.cannon = this.cogs.getChild("cannon");
		this.cannon_body = this.cannon.getChild("cannon_body");
		this.cannon_head = this.cannon.getChild("cannon_head");
		this.cannon_head2 = this.cannon.getChild("cannon_head2");
		this.cannon_head3 = this.cannon.getChild("cannon_head3");
		this.cannon_head4 = this.cannon.getChild("cannon_head4");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition flavio_turret = partdefinition.addOrReplaceChild("flavio_turret", CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition head = flavio_turret.addOrReplaceChild("head",
				CubeListBuilder.create().texOffs(202, 160)
						.addBox(-7.0F, -12.375F, -7.0F, 14.0F, 14.0F, 14.0F, new CubeDeformation(0.0F)).texOffs(0, 154)
						.addBox(-9.0F, 1.625F, -9.0F, 18.0F, 2.0F, 18.0F, new CubeDeformation(0.0F)).texOffs(72, 154)
						.addBox(-9.0F, -14.375F, -9.0F, 18.0F, 2.0F, 18.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -37.625F, 0.0F));

		PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1",
				CubeListBuilder.create().texOffs(216, 138)
						.addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)).texOffs(216, 24)
						.addBox(15.0F, -1.0F, -8.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-8.0F, -4.375F, -8.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2",
				CubeListBuilder.create().texOffs(216, 122)
						.addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)).texOffs(216, 106)
						.addBox(15.0F, -1.0F, -8.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-8.0F, -4.375F, 8.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition Clocks = head.addOrReplaceChild("Clocks", CubeListBuilder.create(),
				PartPose.offset(0.0F, 37.625F, 0.0F));

		PartDefinition Clock_1 = Clocks.addOrReplaceChild("Clock_1",
				CubeListBuilder.create().texOffs(44, 228)
						.addBox(-8.0F, -4.25F, -12.5F, 1.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(216, 82)
						.addBox(-8.5F, -5.25F, -13.5F, 1.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)),
				PartPose.offset(15.5F, -43.75F, 7.6F));

		PartDefinition minute = Clock_1.addOrReplaceChild("minute", CubeListBuilder.create().texOffs(70, 243)
				.addBox(-0.5F, -4.5F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-7.3F, 0.75F, -7.5F));

		PartDefinition hour = Clock_1.addOrReplaceChild("hour", CubeListBuilder.create().texOffs(82, 38).addBox(-0.5F,
				-0.5F, -0.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.25F, 0.75F, -7.5F));

		PartDefinition Clock_2 = Clocks.addOrReplaceChild("Clock_2",
				CubeListBuilder.create().texOffs(44, 228)
						.addBox(15.0F, -4.25F, -4.9F, 1.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(216, 82)
						.addBox(14.5F, -5.25F, -5.9F, 1.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(7.5F, -43.75F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition minute2 = Clock_2.addOrReplaceChild("minute2", CubeListBuilder.create().texOffs(70, 243).addBox(
				-0.5F, -4.5F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(15.7F, 0.75F, 0.1F));

		PartDefinition hour2 = Clock_2.addOrReplaceChild("hour2", CubeListBuilder.create().texOffs(82, 38).addBox(0.25F,
				-0.5F, -0.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(15.0F, 0.75F, 0.1F));

		PartDefinition Clock_3 = Clocks.addOrReplaceChild("Clock_3",
				CubeListBuilder.create().texOffs(44, 228)
						.addBox(22.5F, -4.25F, -12.4F, 1.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(216, 82)
						.addBox(22.0F, -5.25F, -13.4F, 1.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-7.3F, -43.75F, -15.4F, 0.0F, -1.5708F, 0.0F));

		PartDefinition minute3 = Clock_3.addOrReplaceChild("minute3", CubeListBuilder.create().texOffs(70, 243)
				.addBox(-0.5F, -4.5F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(23.2F, 0.75F, -7.4F));

		PartDefinition hour3 = Clock_3.addOrReplaceChild("hour3", CubeListBuilder.create().texOffs(82, 38).addBox(-0.5F,
				-0.5F, -0.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(23.25F, 0.75F, -7.4F));

		PartDefinition Clock_4 = Clocks.addOrReplaceChild("Clock_4",
				CubeListBuilder.create().texOffs(44, 228)
						.addBox(-0.5F, -4.25F, -5.0F, 1.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(216, 82)
						.addBox(-1.0F, -5.25F, -6.0F, 1.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -43.75F, -8.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition minute4 = Clock_4.addOrReplaceChild("minute4", CubeListBuilder.create().texOffs(70, 243).addBox(
				-0.5F, -4.5F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.2F, 0.75F, 0.0F));

		PartDefinition hour4 = Clock_4.addOrReplaceChild("hour4", CubeListBuilder.create().texOffs(82, 38).addBox(-0.5F,
				-0.5F, -0.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.25F, 0.75F, 0.0F));

		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create(),
				PartPose.offset(0.0F, -23.8494F, -0.0045F));

		PartDefinition main_structure = hat.addOrReplaceChild("main_structure", CubeListBuilder.create(),
				PartPose.offset(0.0F, -3.0677F, 0.0045F));

		PartDefinition cube_r3 = main_structure.addOrReplaceChild("cube_r3",
				CubeListBuilder.create().texOffs(116, 189).addBox(-1.0F, -1.0F, -16.0F, 2.0F, 2.0F, 27.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(6.0F, 4.5421F, -6.0F, -2.0344F, 0.6591F, -0.6847F));

		PartDefinition cube_r4 = main_structure.addOrReplaceChild("cube_r4",
				CubeListBuilder.create().texOffs(144, 160).addBox(-1.0F, -1.0F, -16.0F, 2.0F, 2.0F, 27.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-6.0F, 4.5421F, -6.0F, -2.0944F, 0.7854F, 0.0F));

		PartDefinition cube_r5 = main_structure.addOrReplaceChild("cube_r5",
				CubeListBuilder.create().texOffs(58, 174).addBox(-1.0F, -1.0F, -16.0F, 2.0F, 2.0F, 27.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(6.0F, 4.5421F, 6.0F, -1.0472F, 0.7854F, 0.0F));

		PartDefinition cube_r6 = main_structure.addOrReplaceChild("cube_r6",
				CubeListBuilder.create().texOffs(0, 174).addBox(-1.0F, -1.0F, -16.0F, 2.0F, 2.0F, 27.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-6.0F, 4.5421F, 6.0F, -1.1071F, 0.6591F, 0.6847F));

		PartDefinition cube_r7 = main_structure.addOrReplaceChild("cube_r7",
				CubeListBuilder.create().texOffs(154, 226).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -9.5079F, 0.0F, -1.5708F, 0.7854F, 0.0F));

		PartDefinition filling = hat.addOrReplaceChild("filling", CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.7669F, 5.7239F));

		PartDefinition cube_r8 = filling.addOrReplaceChild("cube_r8",
				CubeListBuilder.create().texOffs(128, 60).addBox(-2.5F, -0.5F, -2.0F, 4.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, -9.2425F, -3.7193F, -1.2217F, 0.0F, 0.0F));

		PartDefinition cube_r9 = filling.addOrReplaceChild("cube_r9",
				CubeListBuilder.create().texOffs(156, 218).addBox(-3.5F, -0.5F, -2.0F, 6.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, -6.7925F, -2.7193F, -1.2217F, 0.0F, 0.0F));

		PartDefinition cube_r10 = filling.addOrReplaceChild("cube_r10",
				CubeListBuilder.create().texOffs(66, 228).addBox(-4.5F, -0.5F, -3.0F, 8.0F, 1.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, -3.7925F, -1.7193F, -1.2217F, 0.0F, 0.0F));

		PartDefinition cube_r11 = filling.addOrReplaceChild("cube_r11",
				CubeListBuilder.create().texOffs(116, 174).addBox(-5.5F, -0.5F, -3.0F, 10.0F, 1.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, -0.7925F, -0.7193F, -1.2217F, 0.0F, 0.0F));

		PartDefinition cube_r12 = filling.addOrReplaceChild("cube_r12",
				CubeListBuilder.create().texOffs(146, 236).addBox(-6.5F, -0.5F, -2.0F, 12.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, 1.2075F, 0.2807F, -1.0472F, 0.0F, 0.0F));

		PartDefinition cube_r13 = filling.addOrReplaceChild("cube_r13",
				CubeListBuilder.create().texOffs(204, 228).addBox(-7.5F, -0.5F, -2.0F, 14.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, 3.2075F, 1.2807F, -1.0472F, 0.0F, 0.0F));

		PartDefinition cube_r14 = filling.addOrReplaceChild("cube_r14",
				CubeListBuilder.create().texOffs(204, 220).addBox(-8.5F, -0.5F, -2.0F, 16.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, 5.2075F, 2.2807F, -1.0472F, 0.0F, 0.0F));

		PartDefinition cube_r15 = filling.addOrReplaceChild("cube_r15",
				CubeListBuilder.create().texOffs(216, 154).addBox(-9.5F, -0.5F, -1.0F, 18.0F, 1.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, 7.2075F, 3.2807F, -1.0472F, 0.0F, 0.0F));

		PartDefinition cube_r16 = filling.addOrReplaceChild("cube_r16",
				CubeListBuilder.create().texOffs(200, 40).addBox(-9.5F, -0.5F, -4.0F, 18.0F, 1.0F, 5.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, 9.2075F, 4.2807F, -1.0472F, 0.0F, 0.0F));

		PartDefinition filling2 = hat.addOrReplaceChild("filling2", CubeListBuilder.create(),
				PartPose.offsetAndRotation(-5.75F, 0.7669F, -0.0261F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r17 = filling2.addOrReplaceChild("cube_r17",
				CubeListBuilder.create().texOffs(128, 64).addBox(-2.5F, -0.5F, -2.0F, 4.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, -9.2425F, -3.7193F, -1.2217F, 0.0F, 0.0F));

		PartDefinition cube_r18 = filling2.addOrReplaceChild("cube_r18",
				CubeListBuilder.create().texOffs(154, 222).addBox(-3.5F, -0.5F, -2.0F, 6.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, -6.7925F, -2.7193F, -1.2217F, 0.0F, 0.0F));

		PartDefinition cube_r19 = filling2.addOrReplaceChild("cube_r19",
				CubeListBuilder.create().texOffs(66, 233).addBox(-4.5F, -0.5F, -3.0F, 8.0F, 1.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, -3.7925F, -1.7193F, -1.2217F, 0.0F, 0.0F));

		PartDefinition cube_r20 = filling2.addOrReplaceChild("cube_r20",
				CubeListBuilder.create().texOffs(116, 179).addBox(-5.5F, -0.5F, -3.0F, 10.0F, 1.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, -0.7925F, -0.7193F, -1.2217F, 0.0F, 0.0F));

		PartDefinition cube_r21 = filling2.addOrReplaceChild("cube_r21",
				CubeListBuilder.create().texOffs(176, 236).addBox(-6.5F, -0.5F, -2.0F, 12.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, 1.2075F, 0.2807F, -1.0472F, 0.0F, 0.0F));

		PartDefinition cube_r22 = filling2.addOrReplaceChild("cube_r22",
				CubeListBuilder.create().texOffs(112, 229).addBox(-7.5F, -0.5F, -2.0F, 14.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, 3.2075F, 1.2807F, -1.0472F, 0.0F, 0.0F));

		PartDefinition cube_r23 = filling2.addOrReplaceChild("cube_r23",
				CubeListBuilder.create().texOffs(116, 221).addBox(-8.5F, -0.5F, -2.0F, 16.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, 5.2075F, 2.2807F, -1.0472F, 0.0F, 0.0F));

		PartDefinition cube_r24 = filling2.addOrReplaceChild("cube_r24",
				CubeListBuilder.create().texOffs(216, 157).addBox(-9.5F, -0.5F, -1.0F, 18.0F, 1.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, 7.2075F, 3.2807F, -1.0472F, 0.0F, 0.0F));

		PartDefinition cube_r25 = filling2.addOrReplaceChild("cube_r25",
				CubeListBuilder.create().texOffs(200, 46).addBox(-9.5F, -0.5F, -4.0F, 18.0F, 1.0F, 5.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, 9.2075F, 4.2807F, -1.0472F, 0.0F, 0.0F));

		PartDefinition filling3 = hat.addOrReplaceChild("filling3", CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 0.7669F, -5.7261F, 0.0F, 3.1416F, 0.0F));

		PartDefinition cube_r26 = filling3.addOrReplaceChild("cube_r26",
				CubeListBuilder.create().texOffs(128, 68).addBox(-2.5F, -0.5F, -2.0F, 4.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, -9.2425F, -3.7193F, -1.2217F, 0.0F, 0.0F));

		PartDefinition cube_r27 = filling3.addOrReplaceChild("cube_r27",
				CubeListBuilder.create().texOffs(238, 188).addBox(-3.5F, -0.5F, -2.0F, 6.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, -6.7925F, -2.7193F, -1.2217F, 0.0F, 0.0F));

		PartDefinition cube_r28 = filling3.addOrReplaceChild("cube_r28",
				CubeListBuilder.create().texOffs(112, 237).addBox(-4.5F, -0.5F, -3.0F, 8.0F, 1.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, -3.7925F, -1.7193F, -1.2217F, 0.0F, 0.0F));

		PartDefinition cube_r29 = filling3.addOrReplaceChild("cube_r29",
				CubeListBuilder.create().texOffs(116, 184).addBox(-5.5F, -0.5F, -3.0F, 10.0F, 1.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, -0.7925F, -0.7193F, -1.2217F, 0.0F, 0.0F));

		PartDefinition cube_r30 = filling3.addOrReplaceChild("cube_r30",
				CubeListBuilder.create().texOffs(206, 236).addBox(-6.5F, -0.5F, -2.0F, 12.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, 1.2075F, 0.2807F, -1.0472F, 0.0F, 0.0F));

		PartDefinition cube_r31 = filling3.addOrReplaceChild("cube_r31",
				CubeListBuilder.create().texOffs(204, 232).addBox(-7.5F, -0.5F, -2.0F, 14.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, 3.2075F, 1.2807F, -1.0472F, 0.0F, 0.0F));

		PartDefinition cube_r32 = filling3.addOrReplaceChild("cube_r32",
				CubeListBuilder.create().texOffs(204, 224).addBox(-8.5F, -0.5F, -2.0F, 16.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, 5.2075F, 2.2807F, -1.0472F, 0.0F, 0.0F));

		PartDefinition cube_r33 = filling3.addOrReplaceChild("cube_r33",
				CubeListBuilder.create().texOffs(204, 217).addBox(-9.5F, -0.5F, -1.0F, 18.0F, 1.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, 7.2075F, 3.2807F, -1.0472F, 0.0F, 0.0F));

		PartDefinition cube_r34 = filling3.addOrReplaceChild("cube_r34",
				CubeListBuilder.create().texOffs(200, 52).addBox(-9.5F, -0.5F, -4.0F, 18.0F, 1.0F, 5.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, 9.2075F, 4.2807F, -1.0472F, 0.0F, 0.0F));

		PartDefinition filling4 = hat.addOrReplaceChild("filling4", CubeListBuilder.create(),
				PartPose.offsetAndRotation(5.75F, 0.7669F, 0.0239F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r35 = filling4.addOrReplaceChild("cube_r35",
				CubeListBuilder.create().texOffs(238, 196).addBox(-2.5F, -0.5F, -2.0F, 4.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, -9.2425F, -3.7193F, -1.2217F, 0.0F, 0.0F));

		PartDefinition cube_r36 = filling4.addOrReplaceChild("cube_r36",
				CubeListBuilder.create().texOffs(238, 192).addBox(-3.5F, -0.5F, -2.0F, 6.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, -6.7925F, -2.7193F, -1.2217F, 0.0F, 0.0F));

		PartDefinition cube_r37 = filling4.addOrReplaceChild("cube_r37",
				CubeListBuilder.create().texOffs(66, 238).addBox(-4.5F, -0.5F, -3.0F, 8.0F, 1.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, -3.7925F, -1.7193F, -1.2217F, 0.0F, 0.0F));

		PartDefinition cube_r38 = filling4.addOrReplaceChild("cube_r38",
				CubeListBuilder.create().texOffs(146, 229).addBox(-5.5F, -0.5F, -3.0F, 10.0F, 1.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, -0.7925F, -0.7193F, -1.2217F, 0.0F, 0.0F));

		PartDefinition cube_r39 = filling4.addOrReplaceChild("cube_r39",
				CubeListBuilder.create().texOffs(236, 236).addBox(-6.5F, -0.5F, -2.0F, 12.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, 1.2075F, 0.2807F, -1.0472F, 0.0F, 0.0F));

		PartDefinition cube_r40 = filling4.addOrReplaceChild("cube_r40",
				CubeListBuilder.create().texOffs(112, 233).addBox(-7.5F, -0.5F, -2.0F, 14.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, 3.2075F, 1.2807F, -1.0472F, 0.0F, 0.0F));

		PartDefinition cube_r41 = filling4.addOrReplaceChild("cube_r41",
				CubeListBuilder.create().texOffs(116, 225).addBox(-8.5F, -0.5F, -2.0F, 16.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, 5.2075F, 2.2807F, -1.0472F, 0.0F, 0.0F));

		PartDefinition cube_r42 = filling4.addOrReplaceChild("cube_r42",
				CubeListBuilder.create().texOffs(116, 218).addBox(-9.5F, -0.5F, -1.0F, 18.0F, 1.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, 7.2075F, 3.2807F, -1.0472F, 0.0F, 0.0F));

		PartDefinition cube_r43 = filling4.addOrReplaceChild("cube_r43",
				CubeListBuilder.create().texOffs(204, 211).addBox(-9.5F, -0.5F, -4.0F, 18.0F, 1.0F, 5.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, 9.2075F, 4.2807F, -1.0472F, 0.0F, 0.0F));

		PartDefinition feet = flavio_turret.addOrReplaceChild("feet",
				CubeListBuilder.create().texOffs(0, 0)
						.addBox(-9.0F, -24.0F, -9.0F, 18.0F, 24.0F, 18.0F, new CubeDeformation(0.0F)).texOffs(0, 203)
						.addBox(-7.0F, -24.0F, -10.0F, 14.0F, 24.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 203)
						.addBox(-7.0F, -24.0F, 9.0F, 14.0F, 24.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r44 = feet
				.addOrReplaceChild("cube_r44",
						CubeListBuilder.create().texOffs(174, 211).addBox(-7.0F, -24.0F, -4.0F, 14.0F, 24.0F, 1.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(6.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r45 = feet.addOrReplaceChild("cube_r45",
				CubeListBuilder.create().texOffs(60, 203).addBox(-7.0F, -24.0F, -4.0F, 14.0F, 24.0F, 1.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-13.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cogs = flavio_turret.addOrReplaceChild("cogs", CubeListBuilder.create(),
				PartPose.offset(0.0F, -29.0F, 0.0F));

		PartDefinition cog1 = cogs.addOrReplaceChild("cog1", CubeListBuilder.create().texOffs(72, 0).addBox(-9.0F,
				-1.0F, -9.0F, 18.0F, 2.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));

		PartDefinition cube_r46 = cog1
				.addOrReplaceChild("cube_r46",
						CubeListBuilder.create().texOffs(128, 40).addBox(-9.0F, -2.0F, -9.0F, 18.0F, 2.0F, 18.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 1.3963F, 0.0F));

		PartDefinition cube_r47 = cog1
				.addOrReplaceChild("cube_r47",
						CubeListBuilder.create().texOffs(72, 114).addBox(-9.0F, -2.0F, -9.0F, 18.0F, 2.0F, 18.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 1.2217F, 0.0F));

		PartDefinition cube_r48 = cog1
				.addOrReplaceChild("cube_r48",
						CubeListBuilder.create().texOffs(0, 114).addBox(-9.0F, -2.0F, -9.0F, 18.0F, 2.0F, 18.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		PartDefinition cube_r49 = cog1
				.addOrReplaceChild("cube_r49",
						CubeListBuilder.create().texOffs(72, 94).addBox(-9.0F, -2.0F, -9.0F, 18.0F, 2.0F, 18.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.8727F, 0.0F));

		PartDefinition cube_r50 = cog1
				.addOrReplaceChild("cube_r50",
						CubeListBuilder.create().texOffs(0, 94).addBox(-9.0F, -2.0F, -9.0F, 18.0F, 2.0F, 18.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.6981F, 0.0F));

		PartDefinition cube_r51 = cog1
				.addOrReplaceChild("cube_r51",
						CubeListBuilder.create().texOffs(72, 74).addBox(-9.0F, -2.0F, -9.0F, 18.0F, 2.0F, 18.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		PartDefinition cube_r52 = cog1
				.addOrReplaceChild("cube_r52",
						CubeListBuilder.create().texOffs(0, 74).addBox(-9.0F, -2.0F, -9.0F, 18.0F, 2.0F, 18.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.3491F, 0.0F));

		PartDefinition cube_r53 = cog1
				.addOrReplaceChild("cube_r53",
						CubeListBuilder.create().texOffs(72, 20).addBox(-9.0F, -2.0F, -9.0F, 18.0F, 2.0F, 18.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.1745F, 0.0F));

		PartDefinition cog2 = cogs.addOrReplaceChild("cog2", CubeListBuilder.create().texOffs(0, 134).addBox(-9.0F,
				-1.0F, -9.0F, 18.0F, 2.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

		PartDefinition cube_r54 = cog2
				.addOrReplaceChild("cube_r54",
						CubeListBuilder.create().texOffs(144, 140).addBox(-9.0F, -2.0F, -9.0F, 18.0F, 2.0F, 18.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 1.3963F, 0.0F));

		PartDefinition cube_r55 = cog2
				.addOrReplaceChild("cube_r55",
						CubeListBuilder.create().texOffs(144, 120).addBox(-9.0F, -2.0F, -9.0F, 18.0F, 2.0F, 18.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 1.2217F, 0.0F));

		PartDefinition cube_r56 = cog2
				.addOrReplaceChild("cube_r56",
						CubeListBuilder.create().texOffs(144, 100).addBox(-9.0F, -2.0F, -9.0F, 18.0F, 2.0F, 18.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		PartDefinition cube_r57 = cog2
				.addOrReplaceChild("cube_r57",
						CubeListBuilder.create().texOffs(144, 80).addBox(-9.0F, -2.0F, -9.0F, 18.0F, 2.0F, 18.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.8727F, 0.0F));

		PartDefinition cube_r58 = cog2
				.addOrReplaceChild("cube_r58",
						CubeListBuilder.create().texOffs(144, 60).addBox(-9.0F, -2.0F, -9.0F, 18.0F, 2.0F, 18.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.6981F, 0.0F));

		PartDefinition cube_r59 = cog2
				.addOrReplaceChild("cube_r59",
						CubeListBuilder.create().texOffs(144, 20).addBox(-9.0F, -2.0F, -9.0F, 18.0F, 2.0F, 18.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		PartDefinition cube_r60 = cog2
				.addOrReplaceChild("cube_r60",
						CubeListBuilder.create().texOffs(144, 0).addBox(-9.0F, -2.0F, -9.0F, 18.0F, 2.0F, 18.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.3491F, 0.0F));

		PartDefinition cube_r61 = cog2
				.addOrReplaceChild("cube_r61",
						CubeListBuilder.create().texOffs(72, 134).addBox(-9.0F, -2.0F, -9.0F, 18.0F, 2.0F, 18.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.1745F, 0.0F));

		PartDefinition cannon = cogs.addOrReplaceChild("cannon", CubeListBuilder.create(),
				PartPose.offset(-0.01F, -0.2043F, 0.0011F));

		PartDefinition cannon_body = cannon.addOrReplaceChild("cannon_body", CubeListBuilder.create().texOffs(0, 42)
				.addBox(-3.0F, -3.0F, -13.0F, 6.0F, 6.0F, 26.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.01F, 0.2043F, -0.0011F));

		PartDefinition cube_r62 = cannon_body
				.addOrReplaceChild("cube_r62",
						CubeListBuilder.create().texOffs(174, 189).addBox(-8.0F, -3.0F, -8.0F, 16.0F, 6.0F, 16.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition cube_r63 = cannon_body
				.addOrReplaceChild("cube_r63",
						CubeListBuilder.create().texOffs(64, 42).addBox(-3.0F, -5.0F, -13.0F, 6.0F, 6.0F, 26.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cannon_head = cannon.addOrReplaceChild("cannon_head", CubeListBuilder.create().texOffs(238, 205)
				.addBox(-0.9875F, -3.9947F, -2.0013F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(238, 200)
				.addBox(-0.9875F, 3.0053F, -2.0013F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offset(13.9975F, 0.1989F, 0.0003F));

		PartDefinition cube_r64 = cannon_head.addOrReplaceChild("cube_r64",
				CubeListBuilder.create().texOffs(242, 70).addBox(-1.0F, -0.5F, -1.5F, 2.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.0125F, 2.6482F, -2.5906F, 2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r65 = cannon_head.addOrReplaceChild("cube_r65",
				CubeListBuilder.create().texOffs(242, 102).addBox(-1.0F, -0.5F, -1.5F, 2.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.0125F, -2.6268F, 2.6094F, 2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r66 = cannon_head.addOrReplaceChild("cube_r66",
				CubeListBuilder.create().texOffs(242, 66).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.0125F, -1.2197F, -3.3263F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r67 = cannon_head.addOrReplaceChild("cube_r67",
				CubeListBuilder.create().texOffs(242, 62).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.0125F, 4.0053F, 1.8987F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r68 = cannon_head.addOrReplaceChild("cube_r68",
				CubeListBuilder.create().texOffs(136, 240).addBox(-1.0F, -1.0F, 2.0F, 2.0F, 1.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0125F, -3.9947F, 2.9987F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r69 = cannon_head.addOrReplaceChild("cube_r69",
				CubeListBuilder.create().texOffs(238, 228).addBox(-1.0F, -1.0F, 2.0F, 2.0F, 1.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0125F, -3.9947F, -4.0013F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cannon_head2 = cannon.addOrReplaceChild("cannon_head2", CubeListBuilder.create()
				.texOffs(148, 240).addBox(-0.9875F, -3.9947F, -2.0013F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(184, 240).addBox(-0.9875F, 3.0053F, -2.0013F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-14.0025F, 0.1989F, 0.0003F));

		PartDefinition cube_r70 = cannon_head2.addOrReplaceChild("cube_r70",
				CubeListBuilder.create().texOffs(242, 82).addBox(-1.0F, -0.5F, -1.5F, 2.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.0125F, 2.6482F, -2.5906F, 2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r71 = cannon_head2.addOrReplaceChild("cube_r71",
				CubeListBuilder.create().texOffs(112, 242).addBox(-1.0F, -0.5F, -1.5F, 2.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.0125F, -2.6268F, 2.6094F, 2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r72 = cannon_head2.addOrReplaceChild("cube_r72",
				CubeListBuilder.create().texOffs(242, 78).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.0125F, -1.2197F, -3.3263F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r73 = cannon_head2.addOrReplaceChild("cube_r73",
				CubeListBuilder.create().texOffs(242, 74).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.0125F, 4.0053F, 1.8987F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r74 = cannon_head2.addOrReplaceChild("cube_r74",
				CubeListBuilder.create().texOffs(172, 240).addBox(-1.0F, -1.0F, 2.0F, 2.0F, 1.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0125F, -3.9947F, 2.9987F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r75 = cannon_head2.addOrReplaceChild("cube_r75",
				CubeListBuilder.create().texOffs(160, 240).addBox(-1.0F, -1.0F, 2.0F, 2.0F, 1.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0125F, -3.9947F, -4.0013F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cannon_head3 = cannon.addOrReplaceChild("cannon_head3", CubeListBuilder.create()
				.texOffs(196, 240).addBox(-0.9875F, -3.9947F, -2.0013F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(232, 240).addBox(-0.9875F, 3.0053F, -2.0013F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.0025F, 0.1989F, -13.9997F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r76 = cannon_head3.addOrReplaceChild("cube_r76",
				CubeListBuilder.create().texOffs(242, 20).addBox(-1.0F, -0.5F, -1.5F, 2.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.0125F, 2.6482F, -2.5906F, 2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r77 = cannon_head3.addOrReplaceChild("cube_r77",
				CubeListBuilder.create().texOffs(122, 242).addBox(-1.0F, -0.5F, -1.5F, 2.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.0125F, -2.6268F, 2.6094F, 2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r78 = cannon_head3.addOrReplaceChild("cube_r78",
				CubeListBuilder.create().texOffs(242, 90).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.0125F, -1.2197F, -3.3263F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r79 = cannon_head3.addOrReplaceChild("cube_r79",
				CubeListBuilder.create().texOffs(242, 86).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.0125F, 4.0053F, 1.8987F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r80 = cannon_head3.addOrReplaceChild("cube_r80",
				CubeListBuilder.create().texOffs(220, 240).addBox(-1.0F, -1.0F, 2.0F, 2.0F, 1.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0125F, -3.9947F, 2.9987F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r81 = cannon_head3.addOrReplaceChild("cube_r81",
				CubeListBuilder.create().texOffs(208, 240).addBox(-1.0F, -1.0F, 2.0F, 2.0F, 1.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0125F, -3.9947F, -4.0013F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cannon_head4 = cannon.addOrReplaceChild("cannon_head4", CubeListBuilder.create().texOffs(242, 0)
				.addBox(-0.9875F, -3.9947F, -2.0013F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(242, 15)
				.addBox(-0.9875F, 3.0053F, -2.0013F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.0025F, 0.1989F, 14.0003F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r82 = cannon_head4.addOrReplaceChild("cube_r82",
				CubeListBuilder.create().texOffs(242, 98).addBox(-1.0F, -0.5F, -1.5F, 2.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.0125F, 2.6482F, -2.5906F, 2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r83 = cannon_head4.addOrReplaceChild("cube_r83",
				CubeListBuilder.create().texOffs(242, 58).addBox(-1.0F, -0.5F, -1.5F, 2.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.0125F, -2.6268F, 2.6094F, 2.3562F, 0.0F, 0.0F));

		PartDefinition cube_r84 = cannon_head4.addOrReplaceChild("cube_r84",
				CubeListBuilder.create().texOffs(242, 94).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.0125F, -1.2197F, -3.3263F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r85 = cannon_head4.addOrReplaceChild("cube_r85",
				CubeListBuilder.create().texOffs(242, 220).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.0125F, 4.0053F, 1.8987F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r86 = cannon_head4.addOrReplaceChild("cube_r86",
				CubeListBuilder.create().texOffs(242, 10).addBox(-1.0F, -1.0F, 2.0F, 2.0F, 1.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0125F, -3.9947F, 2.9987F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r87 = cannon_head4.addOrReplaceChild("cube_r87",
				CubeListBuilder.create().texOffs(242, 5).addBox(-1.0F, -1.0F, 2.0F, 2.0F, 1.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0125F, -3.9947F, -4.0013F, -1.5708F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 512, 512);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		flavio_turret.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
	}
}