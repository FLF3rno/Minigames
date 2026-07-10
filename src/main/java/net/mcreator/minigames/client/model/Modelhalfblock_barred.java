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

// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
public class Modelhalfblock_barred extends EntityModel<LivingEntityRenderState> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath("minigames", "modelhalfblock_barred"), "main");
	public final ModelPart bb_main;

	public Modelhalfblock_barred(ModelPart root) {
		super(root);
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main",
				CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(0, 59).addBox(-8.0F, -13.3F, 0.0F, 16.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(35, 61)
						.addBox(-6.5F, -11.8F, 0.3F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(35, 61).addBox(5.0F, -11.8F, 0.3F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(35, 61)
						.addBox(-6.5F, -4.5F, 0.3F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(35, 61).addBox(5.0F, -4.5F, 0.3F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 59)
						.addBox(-8.0F, -6.0F, 0.0F, 16.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	public void setupAnim(LivingEntityRenderState state) {
		float limbSwing = state.walkAnimationPos;
		float limbSwingAmount = state.walkAnimationSpeed;
		float ageInTicks = state.ageInTicks;
		float netHeadYaw = state.yRot;
		float headPitch = state.xRot;

	}
}