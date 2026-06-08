package net.mcreator.minigames.client.model;

import net.minecraft.resources.ResourceLocation;
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
public class Modelpreacher extends EntityModel<LivingEntityRenderState> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("minigames", "modelpreacher"), "main");
	public final ModelPart Body;
	public final ModelPart Head;
	public final ModelPart RightArm;
	public final ModelPart LeftArm;
	public final ModelPart bible;
	public final ModelPart otherside;
	public final ModelPart RightLeg;
	public final ModelPart LeftLeg;

	public Modelpreacher(ModelPart root) {
		super(root);
		this.Body = root.getChild("Body");
		this.Head = root.getChild("Head");
		this.RightArm = root.getChild("RightArm");
		this.LeftArm = root.getChild("LeftArm");
		this.bible = this.LeftArm.getChild("bible");
		this.otherside = this.bible.getChild("otherside");
		this.RightLeg = root.getChild("RightLeg");
		this.LeftLeg = root.getChild("LeftLeg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition Head = partdefinition.addOrReplaceChild("Head",
				CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));
		PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(5.0F, 2.0F, 0.0F));
		PartDefinition bible = LeftArm.addOrReplaceChild("bible",
				CubeListBuilder.create().texOffs(0, 20).addBox(-3.0F, -5.0F, 0.0F, 4.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(1, 35).addBox(-2.9F, -5.0F, -0.1F, 4.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(1, 35)
						.addBox(-2.9F, -5.0F, -0.2F, 4.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(1, 35).addBox(-2.9F, -5.0F, -0.3F, 4.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(1, 35)
						.addBox(-2.9F, -5.0F, -0.4F, 4.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(1, 35).addBox(-2.9F, -5.0F, -0.5F, 4.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 11.0F, -3.0F));
		PartDefinition otherside = bible.addOrReplaceChild("otherside",
				CubeListBuilder.create().texOffs(1, 21).addBox(-0.9F, -4.7F, 0.0F, 5.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(2, 35).addBox(0.0F, -4.7F, -0.1F, 4.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(2, 35)
						.addBox(0.0F, -4.7F, -0.2F, 4.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(2, 35).addBox(0.0F, -4.7F, -0.3F, 4.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(2, 35)
						.addBox(0.0F, -4.7F, -0.4F, 4.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(2, 35).addBox(0.0F, -4.7F, -0.5F, 4.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.9F, -0.3F, 0.0F, 0.0F, 1.7453F, 0.0F));
		PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 12.0F, 0.0F));
		PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.9F, 12.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	public void setupAnim(LivingEntityRenderState state) {
		float limbSwing = state.walkAnimationPos;
		float limbSwingAmount = state.walkAnimationSpeed;
		float ageInTicks = state.ageInTicks;
		float netHeadYaw = state.yRot;
		float headPitch = state.xRot;

		this.Head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.Head.xRot = headPitch / (180F / (float) Math.PI);
	}
}