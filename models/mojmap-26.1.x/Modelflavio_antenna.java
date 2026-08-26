// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class Modelflavio_antenna<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "flavio_antenna"), "main");
	private final ModelPart antenna;
	private final ModelPart head;

	public Modelflavio_antenna(ModelPart root) {
		this.antenna = root.getChild("antenna");
		this.head = this.antenna.getChild("head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition antenna = partdefinition.addOrReplaceChild("antenna",
				CubeListBuilder.create().texOffs(0, 0)
						.addBox(-14.0F, -4.8F, -14.0F, 28.0F, 5.0F, 28.0F, new CubeDeformation(0.0F)).texOffs(0, 65)
						.addBox(-6.0F, -64.8F, -6.0F, 12.0F, 60.0F, 12.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r1 = antenna
				.addOrReplaceChild("cube_r1",
						CubeListBuilder.create().texOffs(164, 164).addBox(-14.0F, -5.0F, -10.0F, 9.0F, 5.0F, 20.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(26.8F, 2.8F, 0.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition cube_r2 = antenna.addOrReplaceChild("cube_r2",
				CubeListBuilder.create().texOffs(0, 169).addBox(13.1068F, -5.6046F, -10.0F, 9.0F, 5.0F, 20.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -1.3117F, 0.0F, -1.5708F, -1.3963F, 1.5708F));

		PartDefinition cube_r3 = antenna.addOrReplaceChild("cube_r3",
				CubeListBuilder.create().texOffs(58, 189).addBox(-14.0F, -5.0F, -10.0F, 9.0F, 5.0F, 20.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-26.8F, 2.8F, 0.0F, -3.1416F, 0.0F, 2.9671F));

		PartDefinition cube_r4 = antenna.addOrReplaceChild("cube_r4",
				CubeListBuilder.create().texOffs(116, 189).addBox(13.1068F, -5.6046F, -10.0F, 9.0F, 5.0F, 20.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -1.3117F, 0.0F, 1.5708F, 1.3963F, 1.5708F));

		PartDefinition head = antenna.addOrReplaceChild("head", CubeListBuilder.create(),
				PartPose.offsetAndRotation(-5.7762F, -64.2397F, 0.5F, 0.0F, 0.0F, -0.5672F));

		PartDefinition cube_r5 = head.addOrReplaceChild("cube_r5",
				CubeListBuilder.create().texOffs(48, 65).addBox(-10.1373F, -35.2443F, -14.0F, 2.0F, 25.0F, 27.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(10.9711F, -6.0543F, -13.414F, 0.4927F, 0.9149F, -0.4082F));

		PartDefinition cube_r6 = head.addOrReplaceChild("cube_r6",
				CubeListBuilder.create().texOffs(106, 137).addBox(-10.0291F, 10.4419F, -13.5F, 2.0F, 25.0F, 27.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(10.9711F, -6.0543F, -13.414F, -0.5409F, 0.8934F, -1.6603F));

		PartDefinition cube_r7 = head.addOrReplaceChild("cube_r7",
				CubeListBuilder.create().texOffs(48, 117).addBox(-1.0F, -12.5F, -13.5F, 2.0F, 25.0F, 27.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(22.1534F, -23.607F, -0.466F, -1.5708F, 1.3963F, -1.0036F));

		PartDefinition cube_r8 = head.addOrReplaceChild("cube_r8",
				CubeListBuilder.create().texOffs(0, 137)
						.addBox(-3.0F, -5.5F, -12.5F, 1.0F, 9.0F, 19.0F, new CubeDeformation(0.0F)).texOffs(164, 123)
						.addBox(-3.0F, 3.5F, -18.5F, 1.0F, 9.0F, 32.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-8.6911F, -1.3676F, -23.954F, -2.0826F, 0.6529F, -0.6521F));

		PartDefinition cube_r9 = head.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(164, 0)
				.addBox(-0.4155F, 0.9091F, -14.6239F, 1.0F, 9.0F, 32.0F, new CubeDeformation(0.0F)).texOffs(0, 207)
				.addBox(-0.4155F, -8.0909F, -8.6239F, 1.0F, 9.0F, 19.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(35.9469F, -8.1457F, -3.8976F, -0.1686F, 1.1711F, -2.0603F));

		PartDefinition cube_r10 = head.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(112, 0)
				.addBox(-0.6324F, -9.0294F, -9.6153F, 1.0F, 9.0F, 19.0F, new CubeDeformation(0.0F)).texOffs(164, 41)
				.addBox(-0.6324F, -0.0294F, -15.6153F, 1.0F, 9.0F, 32.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(18.6102F, 17.4004F, -24.2736F, -1.1146F, 0.6335F, -1.4341F));

		PartDefinition cube_r11 = head.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(164, 82)
				.addBox(-0.5249F, 0.0519F, -16.622F, 1.0F, 9.0F, 32.0F, new CubeDeformation(0.0F)).texOffs(174, 213)
				.addBox(-0.5249F, -8.9481F, -10.622F, 1.0F, 9.0F, 19.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(2.8698F, -29.5745F, -3.6708F, -3.0542F, 1.2121F, -0.0601F));

		PartDefinition cube_r12 = head.addOrReplaceChild("cube_r12",
				CubeListBuilder.create().texOffs(106, 85).addBox(-1.0F, -12.5F, -13.5F, 2.0F, 25.0F, 27.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.2112F, 11.4985F, -26.3621F, -1.5708F, 0.6545F, -1.0036F));

		PartDefinition cube_r13 = head.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(58, 169)
				.addBox(42.6264F, -5.6492F, 2.5F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(98, 42)
				.addBox(42.6264F, -4.6492F, 9.5F, 2.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(78, 169)
				.addBox(42.6264F, 1.3508F, 2.5F, 2.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(98, 33)
				.addBox(42.6264F, -5.6492F, 1.5F, 2.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 194)
				.addBox(12.6264F, -4.6492F, 2.5F, 21.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(106, 33)
				.addBox(11.6264F, -14.6492F, -7.5F, 2.0F, 25.0F, 27.0F, new CubeDeformation(0.0F)).texOffs(0, 33)
				.addBox(-18.3736F, -8.2492F, -3.5F, 30.0F, 13.0F, 19.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 1.0036F, -1.0036F));

		PartDefinition cube_r14 = head.addOrReplaceChild("cube_r14",
				CubeListBuilder.create().texOffs(174, 201).addBox(-22.7596F, 8.1558F, -4.6371F, 34.0F, 3.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(11.0177F, -5.6223F, -27.9175F, -2.2549F, 0.396F, -0.5419F));

		PartDefinition cube_r15 = head.addOrReplaceChild("cube_r15",
				CubeListBuilder.create().texOffs(174, 207).addBox(-22.4016F, 1.6073F, 8.0067F, 34.0F, 3.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(11.0177F, -5.6223F, -27.9175F, -1.8067F, 0.422F, -1.5343F));

		PartDefinition cube_r16 = head.addOrReplaceChild("cube_r16",
				CubeListBuilder.create().texOffs(174, 195).addBox(-20.5F, -1.5F, 1.5F, 35.0F, 3.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(10.6987F, -26.6943F, -21.7364F, -1.1825F, 1.1289F, 0.2865F));

		PartDefinition cube_r17 = head.addOrReplaceChild("cube_r17",
				CubeListBuilder.create().texOffs(174, 189).addBox(-9.0F, -2.4F, -1.0F, 35.0F, 3.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(31.026F, -9.6167F, -11.6336F, -2.9543F, 1.0836F, -2.4085F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		antenna.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
		this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.head.xRot = headPitch / (180F / (float) Math.PI);
	}
}