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

// Made with Blockbench 5.1.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
public class Modelwinnercrown extends EntityModel<LivingEntityRenderState> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath("minigames", "modelwinnercrown"), "main");
	public final ModelPart crown;

	public Modelwinnercrown(ModelPart root) {
		super(root);
		this.crown = root.getChild("crown");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition crown = partdefinition.addOrReplaceChild("crown",
				CubeListBuilder.create().texOffs(6, 36).addBox(-3.0F, -4.0F, -4.0F, 1.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(61, 59).addBox(-3.0F, -7.0F, -0.6F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(9, 6)
						.addBox(-11.0F, -4.9F, -3.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(12, 8).addBox(-10.0F, -5.4F, -2.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(15, 10)
						.addBox(-9.0F, -6.0F, -1.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offset(7.0F, -4.0F, -1.0F));
		PartDefinition cube_r1 = crown.addOrReplaceChild("cube_r1",
				CubeListBuilder.create().texOffs(61, 59).addBox(4.0F, -3.0F, -1.6F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(8, 38).addBox(4.0F, 0.0F, -4.0F, 1.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-7.0F, -4.0F, 1.0F, 0.0F, -1.5708F, 0.0F));
		PartDefinition cube_r2 = crown.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(11, 68).addBox(-4.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-7.0F, -4.0F, 1.0F, -3.1416F, 0.0F, 3.1416F));
		PartDefinition cube_r3 = crown.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(61, 59).addBox(4.0F, -3.0F, -1.6F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(8, 38)
				.addBox(4.0F, 0.0F, -4.0F, 1.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(3, 71).addBox(-5.0F, -2.0F, -4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -4.0F, 1.0F, 0.0F, 1.5708F, 0.0F));
		PartDefinition cube_r4 = crown.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(3, 71).addBox(-5.0F, -2.0F, -4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-7.0F, -4.0F, 1.0F, 0.0F, -1.5708F, 0.0F));
		PartDefinition cube_r5 = crown.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(11, 68).addBox(-4.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -4.0F, 1.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition cube_r6 = crown.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(2, 70).addBox(-0.5F, -2.0F, -5.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.5F, -4.0F, 1.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition cube_r7 = crown.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(10, 68).addBox(-5.0F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-11.5F, -4.0F, 1.0F, 0.0F, 1.5708F, 0.0F));
		PartDefinition cube_r8 = crown.addOrReplaceChild("cube_r8",
				CubeListBuilder.create().texOffs(61, 59).addBox(-0.5F, -3.0F, -1.6F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(6, 36).addBox(-0.5F, 0.0F, -5.0F, 1.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-11.5F, -4.0F, 1.0F, -3.1416F, 0.0F, 3.1416F));
		PartDefinition cube_r9 = crown.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(68, 32).addBox(-0.5F, -2.0F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-7.0F, -7.4F, 1.05F, 0.0F, -1.5708F, 0.0F));
		PartDefinition cube_r10 = crown.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(34, 68).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-7.0F, -7.4F, 1.0F, 0.0F, -1.5708F, 0.0F));
		PartDefinition cube_r11 = crown.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(2, 70).addBox(-8.0F, -0.5F, -1.5F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-10.0F, -5.5F, 4.5F, -3.1416F, 0.0F, 3.1416F));
		PartDefinition cube_r12 = crown.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(10, 68).addBox(-0.5F, -0.5F, -1.25F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-3.25F, -5.5F, -3.5F, 0.0F, -1.5708F, 0.0F));
		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	public void setupAnim(LivingEntityRenderState state) {
		float limbSwing = state.walkAnimationPos;
		float limbSwingAmount = state.walkAnimationSpeed;
		float ageInTicks = state.ageInTicks;
		float netHeadYaw = state.yRot;
		float headPitch = state.xRot;

	}
}