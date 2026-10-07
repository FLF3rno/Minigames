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
public class Modeltesla_coil extends EntityModel<LivingEntityRenderState> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath("minigames", "modeltesla_coil"), "main");
	public final ModelPart tesla_coil;

	public Modeltesla_coil(ModelPart root) {
		super(root);
		this.tesla_coil = root.getChild("tesla_coil");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition tesla_coil = partdefinition.addOrReplaceChild("tesla_coil",
				CubeListBuilder.create().texOffs(18, 12).addBox(-8.0F, -26.0F, -8.0F, 16.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)).texOffs(34, 138).addBox(-1.5F, -45.0F, -1.5F, 3.0F, 19.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(19, 72)
						.addBox(-6.0F, -30.3F, -6.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(18, 99).addBox(-5.0F, -34.3F, -5.0F, 10.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(106, 134)
						.addBox(-4.0F, -38.3F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(3, 139).addBox(-3.0F, -42.3F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(64, 120)
						.addBox(-3.0F, -51.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 48.2F, 0.0F));
		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	public void setupAnim(LivingEntityRenderState state) {
		float limbSwing = state.walkAnimationPos;
		float limbSwingAmount = state.walkAnimationSpeed;
		float ageInTicks = state.ageInTicks;
		float netHeadYaw = state.yRot;
		float headPitch = state.xRot;

	}
}