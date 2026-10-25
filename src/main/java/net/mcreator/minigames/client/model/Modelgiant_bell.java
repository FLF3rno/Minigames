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

// Made with Blockbench 5.2.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
public class Modelgiant_bell extends EntityModel<LivingEntityRenderState> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath("minigames", "modelgiant_bell"), "main");
	public final ModelPart bell;

	public Modelgiant_bell(ModelPart root) {
		super(root);
		this.bell = root.getChild("bell");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition bell = partdefinition.addOrReplaceChild("bell",
				CubeListBuilder.create().texOffs(0, 0).addBox(16.0F, -11.0F, -24.0F, 8.0F, 11.0F, 48.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-24.0F, -11.0F, -24.0F, 8.0F, 11.0F, 48.0F, new CubeDeformation(0.0F)).texOffs(160, 154)
						.addBox(11.0F, -56.0F, -12.0F, 8.0F, 45.0F, 24.0F, new CubeDeformation(0.0F)).texOffs(192, 154).addBox(-20.0F, -56.0F, -12.0F, 8.0F, 45.0F, 24.0F, new CubeDeformation(0.0F)).texOffs(0, 154)
						.addBox(-17.0F, -49.0F, -16.0F, 32.0F, 8.0F, 32.0F, new CubeDeformation(0.0F)).texOffs(81, 194).addBox(-5.0F, -66.0F, -4.0F, 9.0F, 66.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(103, 234)
						.addBox(-18.0F, -58.0F, -18.0F, 36.0F, 10.0F, 37.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition cube_r1 = bell.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(112, 77).addBox(-24.0F, -48.0F, -19.0F, 8.0F, 45.0F, 39.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -8.0F, 4.0F, 0.0F, -1.5708F, 0.0F));
		PartDefinition cube_r2 = bell.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(112, 0).addBox(16.0F, -48.0F, -19.0F, 8.0F, 45.0F, 39.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -8.0F, -4.0F, 0.0F, -1.5708F, 0.0F));
		PartDefinition cube_r3 = bell.addOrReplaceChild("cube_r3",
				CubeListBuilder.create().texOffs(15, 16).addBox(-24.0F, -7.0F, -16.0F, 8.0F, 11.0F, 32.0F, new CubeDeformation(0.0F)).texOffs(15, 16).addBox(16.0F, -7.0F, -16.0F, 8.0F, 11.0F, 32.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
		return LayerDefinition.create(meshdefinition, 512, 512);
	}

	public void setupAnim(LivingEntityRenderState state) {
		float limbSwing = state.walkAnimationPos;
		float limbSwingAmount = state.walkAnimationSpeed;
		float ageInTicks = state.ageInTicks;
		float netHeadYaw = state.yRot;
		float headPitch = state.xRot;

	}
}