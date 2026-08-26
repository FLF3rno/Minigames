// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class Modelflavio_trapdoor<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "flavio_trapdoor"), "main");
	private final ModelPart trapdoor;
	private final ModelPart left;
	private final ModelPart right;

	public Modelflavio_trapdoor(ModelPart root) {
		this.trapdoor = root.getChild("trapdoor");
		this.left = this.trapdoor.getChild("left");
		this.right = this.trapdoor.getChild("right");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition trapdoor = partdefinition.addOrReplaceChild("trapdoor", CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.01F, 0.0F));

		PartDefinition left = trapdoor.addOrReplaceChild("left", CubeListBuilder.create().texOffs(64, 96).addBox(-8.0F,
				-16.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.01F, 0.0F));

		PartDefinition right = trapdoor.addOrReplaceChild("right", CubeListBuilder.create(),
				PartPose.offset(0.0F, -0.01F, 16.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		trapdoor.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
	}
}