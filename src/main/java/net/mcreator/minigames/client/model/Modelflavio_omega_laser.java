package net.mcreator.minigames.client.model;

import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.EntityModel;

// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
public class Modelflavio_omega_laser extends EntityModel<LivingEntityRenderState> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath("minigames", "modelflavio_omega_laser"), "main");
	public final ModelPart laser;
	public final ModelPart base;
	public final ModelPart body;
	public final ModelPart head;
	public final ModelPart progress;

	public Modelflavio_omega_laser(ModelPart root) {
		super(root);
		this.laser = root.getChild("laser");
		this.base = this.laser.getChild("base");
		this.body = this.laser.getChild("body");
		this.head = this.laser.getChild("head");
		this.progress = this.head.getChild("progress");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition laser = partdefinition.addOrReplaceChild("laser", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition base = laser.addOrReplaceChild("base",
				CubeListBuilder.create().texOffs(0, 0).addBox(-14.0F, -4.8F, -14.0F, 28.0F, 5.0F, 28.0F, new CubeDeformation(0.0F)).texOffs(103, 208).addBox(-11.0F, -17.8F, -10.0F, 20.0F, 13.0F, 21.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition cube_r1 = base.addOrReplaceChild("cube_r1",
				CubeListBuilder.create().texOffs(160, 73).addBox(-14.0F, -5.0F, -10.0F, 9.0F, 5.0F, 20.0F, new CubeDeformation(0.0F)).texOffs(160, 73).addBox(-14.0F, -5.0F, -10.0F, 9.0F, 5.0F, 20.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-26.8F, 2.8F, 0.0F, -3.1416F, 0.0F, 2.9671F));
		PartDefinition cube_r2 = base.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 159).addBox(13.1068F, -5.6046F, -10.0F, 9.0F, 5.0F, 20.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -1.3117F, 0.0F, -1.5708F, -1.3963F, 1.5708F));
		PartDefinition cube_r3 = base.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(140, 156).addBox(13.1068F, -5.6046F, -10.0F, 9.0F, 5.0F, 20.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -1.3117F, 0.0F, 1.5708F, 1.3963F, 1.5708F));
		PartDefinition cube_r4 = base.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(112, 0).addBox(-14.0F, -5.0F, -10.0F, 9.0F, 5.0F, 20.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(26.8F, 2.8F, 0.0F, 0.0F, 0.0F, 0.1745F));
		PartDefinition body = laser.addOrReplaceChild("body", CubeListBuilder.create().texOffs(152, 92).addBox(-6.0F, -49.25F, -6.0F, 12.0F, 20.0F, 14.0F, new CubeDeformation(0.0F)).texOffs(152, 92)
				.addBox(-6.0F, -7.25F, -5.0F, 12.0F, 26.0F, 14.0F, new CubeDeformation(0.0F)).texOffs(162, -6).addBox(-5.0F, -53.25F, -5.0F, 10.0F, 79.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -30.55F, 0.0F));
		PartDefinition head = laser.addOrReplaceChild("head",
				CubeListBuilder.create().texOffs(80, 77).addBox(4.75F, -3.825F, -17.15F, 4.0F, 4.0F, 36.0F, new CubeDeformation(0.0F)).texOffs(58, 159).addBox(4.75F, -14.825F, -17.15F, 4.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(80, 156)
						.addBox(4.75F, -14.825F, -12.15F, 3.0F, 11.0F, 27.0F, new CubeDeformation(0.0F)).texOffs(72, 194).addBox(4.75F, -14.825F, 14.85F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(80, 117)
						.addBox(4.75F, -17.825F, -17.15F, 4.0F, 3.0F, 36.0F, new CubeDeformation(0.0F)).texOffs(88, 194).addBox(-9.25F, -14.825F, 14.85F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(88, 33)
						.addBox(-9.25F, -3.825F, -17.15F, 4.0F, 4.0F, 36.0F, new CubeDeformation(0.0F)).texOffs(194, 56).addBox(-9.25F, -14.825F, -17.15F, 4.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(0, 120)
						.addBox(-9.25F, -17.825F, -17.15F, 4.0F, 3.0F, 36.0F, new CubeDeformation(0.0F)).texOffs(192, 98).addBox(-5.25F, -17.825F, -17.15F, 10.0F, 18.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(0, 77)
						.addBox(-8.25F, -16.825F, -12.15F, 13.0F, 16.0F, 27.0F, new CubeDeformation(0.0F)).texOffs(194, 34).addBox(-5.25F, -17.825F, 14.85F, 10.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(194, 0)
						.addBox(-7.25F, -15.825F, -20.15F, 14.0F, 14.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(194, 17).addBox(-7.25F, -15.825F, 18.85F, 14.0F, 14.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(218, 231)
						.addBox(-3.25F, -13.825F, -31.15F, 8.0F, 8.0F, 11.0F, new CubeDeformation(0.0F)).texOffs(179, 238).addBox(-5.25F, -14.825F, -27.15F, 11.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(179, 238)
						.addBox(-4.75F, -14.325F, -34.15F, 10.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(179, 238).addBox(-4.75F, -13.825F, -41.15F, 9.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(72, 237)
						.addBox(-4.25F, -12.325F, -45.15F, 3.0F, 6.0F, 11.0F, new CubeDeformation(0.0F)).texOffs(222, 234).addBox(-2.25F, -12.825F, -42.15F, 6.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(36, 237)
						.addBox(0.75F, -12.325F, -45.15F, 3.0F, 6.0F, 11.0F, new CubeDeformation(0.0F)).texOffs(176, 234).addBox(-4.25F, -13.325F, -45.15F, 8.0F, 2.0F, 11.0F, new CubeDeformation(0.0F)).texOffs(176, 234)
						.addBox(-4.25F, -8.325F, -45.15F, 8.0F, 2.0F, 11.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.25F, -79.975F, 0.15F));
		PartDefinition progress = head.addOrReplaceChild("progress", CubeListBuilder.create().texOffs(0, 33).addBox(-8.5F, -8.5F, -27.5F, 17.0F, 17.0F, 27.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.25F, -8.825F, 15.35F));
		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	public void setupAnim(LivingEntityRenderState state) {
		float limbSwing = state.walkAnimationPos;
		float limbSwingAmount = state.walkAnimationSpeed;
		float ageInTicks = state.ageInTicks;
		float netHeadYaw = state.yRot;
		float headPitch = state.xRot;

		this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.head.xRot = headPitch / (180F / (float) Math.PI);
	}
}