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
public class Modeldemon extends EntityModel<LivingEntityRenderState> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("minigames", "modeldemon"), "main");
	public final ModelPart body;
	public final ModelPart left_lef;
	public final ModelPart right_leg;
	public final ModelPart torso;
	public final ModelPart right_arm;
	public final ModelPart left_hand;
	public final ModelPart staff;
	public final ModelPart head;

	public Modeldemon(ModelPart root) {
		super(root);
		this.body = root.getChild("body");
		this.left_lef = this.body.getChild("left_lef");
		this.right_leg = this.body.getChild("right_leg");
		this.torso = this.body.getChild("torso");
		this.right_arm = this.torso.getChild("right_arm");
		this.left_hand = this.torso.getChild("left_hand");
		this.staff = this.left_hand.getChild("staff");
		this.head = this.torso.getChild("head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(60, 72).addBox(-10.0F, -17.0F, -6.0F, 20.0F, 5.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition left_lef = body.addOrReplaceChild("left_lef", CubeListBuilder.create().texOffs(36, 91).addBox(-5.0F, -12.0F, -3.0F, 6.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.0F, 0.0F));
		PartDefinition right_leg = body.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(96, 89).addBox(-5.0F, -12.0F, -3.0F, 6.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 0.0F, 0.0F));
		PartDefinition torso = body.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 65).addBox(-17.0F, -15.0F, -6.0F, 19.0F, 15.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(7.55F, -17.0F, 0.45F));
		PartDefinition right_arm = torso.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 91).addBox(2.0F, -15.0F, -6.0F, 7.0F, 24.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition left_hand = torso.addOrReplaceChild("left_hand", CubeListBuilder.create().texOffs(60, 89).addBox(-3.25F, -2.5F, -5.25F, 7.0F, 24.0F, 11.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-20.75F, -12.5F, -0.75F, 1.3526F, 0.0F, 0.0F));
		PartDefinition staff = left_hand.addOrReplaceChild("staff", CubeListBuilder.create().texOffs(111, 205).addBox(-29.0F, 8.0F, -16.0F, 3.0F, 3.0F, 35.0F, new CubeDeformation(0.0F)).texOffs(100, 168)
				.addBox(-36.0F, 4.0F, 19.0F, 16.0F, 10.0F, 15.0F, new CubeDeformation(0.0F)).texOffs(96, 146).addBox(-46.0F, 9.0F, 19.0F, 34.0F, 0.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(27.75F, 12.5F, -9.25F));
		PartDefinition head = torso.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(-7.5F, -22.5F, -0.25F));
		PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(70, 52).addBox(-14.0F, -8.5F, -3.0F, 29.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.375F, -6.0F, 2.025F, -0.0436F, 0.0F, 0.0F));
		PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(3, 39).addBox(-7.0F, -6.5F, -6.0F, 14.0F, 14.0F, 12.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0436F, 0.0F, 0.0F));
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