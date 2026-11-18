// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class Modelrock_thrower_church<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "rock_thrower_church"), "main");
	private final ModelPart entity;
	private final ModelPart leftleg;
	private final ModelPart leftlegtop;
	private final ModelPart rightleg;
	private final ModelPart rightlegtop;
	private final ModelPart body;
	private final ModelPart bodytop;
	private final ModelPart head;
	private final ModelPart leftarm;
	private final ModelPart leftbottom;
	private final ModelPart rightarm;
	private final ModelPart rightbottom;
	private final ModelPart rock;

	public Modelrock_thrower_church(ModelPart root) {
		this.entity = root.getChild("entity");
		this.leftleg = this.entity.getChild("leftleg");
		this.leftlegtop = this.leftleg.getChild("leftlegtop");
		this.rightleg = this.entity.getChild("rightleg");
		this.rightlegtop = this.rightleg.getChild("rightlegtop");
		this.body = this.entity.getChild("body");
		this.bodytop = this.body.getChild("bodytop");
		this.head = this.bodytop.getChild("head");
		this.leftarm = this.body.getChild("leftarm");
		this.leftbottom = this.leftarm.getChild("leftbottom");
		this.rightarm = this.body.getChild("rightarm");
		this.rightbottom = this.rightarm.getChild("rightbottom");
		this.rock = root.getChild("rock");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition entity = partdefinition.addOrReplaceChild("entity", CubeListBuilder.create(),
				PartPose.offset(-0.025F, 11.1167F, -2.1417F));

		PartDefinition leftleg = entity.addOrReplaceChild("leftleg", CubeListBuilder.create().texOffs(128, 69)
				.addBox(-3.0F, 0.0F, -2.5F, 6.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offset(6.025F, 5.8833F, 2.1417F));

		PartDefinition leftlegtop = leftleg.addOrReplaceChild("leftlegtop", CubeListBuilder.create().texOffs(128, 81)
				.addBox(-3.0F, -1.5F, -2.5F, 6.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -5.5F, 0.0F));

		PartDefinition rightleg = entity.addOrReplaceChild("rightleg", CubeListBuilder.create().texOffs(64, 130)
				.addBox(-3.0F, -0.25F, 1.75F, 6.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-5.975F, 6.1333F, -2.0583F));

		PartDefinition rightlegtop = rightleg.addOrReplaceChild("rightlegtop", CubeListBuilder.create().texOffs(86, 130)
				.addBox(-3.0F, 0.0F, 6.0F, 6.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -7.25F, -4.25F));

		PartDefinition body = entity.addOrReplaceChild("body", CubeListBuilder.create().texOffs(64, 102).addBox(-9.325F,
				-21.1322F, -0.7494F, 19.0F, 21.0F, 7.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-0.05F, -0.0167F, -1.0833F));

		PartDefinition bodytop = body.addOrReplaceChild("bodytop", CubeListBuilder.create(),
				PartPose.offset(-9.325F, -2.1322F, 2.2506F));

		PartDefinition head = bodytop.addOrReplaceChild("head",
				CubeListBuilder.create().texOffs(116, 102)
						.addBox(-6.5F, -12.0F, -4.85F, 13.0F, 12.0F, 11.0F, new CubeDeformation(0.0F)).texOffs(13, 187)
						.addBox(-7.0F, -12.5F, -5.65F, 14.0F, 13.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(9.5F, -19.0F, -0.15F));

		PartDefinition leftarm = body.addOrReplaceChild("leftarm", CubeListBuilder.create().texOffs(128, 46)
				.addBox(-1.0F, -2.5F, -4.0F, 8.0F, 15.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offset(9.075F, -18.2322F, 2.6506F));

		PartDefinition leftbottom = leftarm.addOrReplaceChild("leftbottom", CubeListBuilder.create().texOffs(128, 23)
				.addBox(-2.0F, -0.5F, -4.0F, 8.0F, 15.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offset(1.0F, 13.0F, 0.0F));

		PartDefinition rightarm = body.addOrReplaceChild("rightarm", CubeListBuilder.create().texOffs(128, 0)
				.addBox(-5.0345F, -1.3475F, 5.8601F, 8.0F, 15.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-7.925F, -21.1322F, -5.7494F));

		PartDefinition rightbottom = rightarm.addOrReplaceChild("rightbottom", CubeListBuilder.create()
				.texOffs(116, 125).addBox(-7.0F, -0.5F, -4.0F, 8.0F, 15.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offset(1.9655F, 14.1525F, 9.8601F));

		PartDefinition rock = partdefinition.addOrReplaceChild("rock",
				CubeListBuilder.create().texOffs(0, 34)
						.addBox(-8.0F, -4.0F, 8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)).texOffs(64, 0)
						.addBox(-8.0F, -20.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)).texOffs(64, 0)
						.addBox(8.0F, -20.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)).texOffs(64, 0)
						.addBox(-24.0F, -20.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)).texOffs(64, 34)
						.addBox(-8.0F, 12.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)).texOffs(0, 68)
						.addBox(-24.0F, -4.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)).texOffs(64, 68)
						.addBox(8.0F, -4.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)).texOffs(0, 102)
						.addBox(-8.0F, -4.0F, -24.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 44.6F, -15.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		entity.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		rock.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
		this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.head.xRot = headPitch / (180F / (float) Math.PI);
	}
}