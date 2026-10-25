package net.mcreator.minigames.client.model;

import net.minecraft.util.Mth;
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
public class Modelflavio extends EntityModel<LivingEntityRenderState> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath("minigames", "modelflavio"), "main");
	public final ModelPart corpo_corposo;
	public final ModelPart body;
	public final ModelPart left_leg;
	public final ModelPart left_leg_bottom;
	public final ModelPart right_leg;
	public final ModelPart right_leg_bottom;
	public final ModelPart left_arm;
	public final ModelPart left_arm_bottom;
	public final ModelPart right_arm;
	public final ModelPart right_arm_bottom;
	public final ModelPart telecomando;
	public final ModelPart head;
	public final ModelPart halo;

	public Modelflavio(ModelPart root) {
		super(root);
		this.corpo_corposo = root.getChild("corpo_corposo");
		this.body = this.corpo_corposo.getChild("body");
		this.left_leg = this.body.getChild("left_leg");
		this.left_leg_bottom = this.left_leg.getChild("left_leg_bottom");
		this.right_leg = this.body.getChild("right_leg");
		this.right_leg_bottom = this.right_leg.getChild("right_leg_bottom");
		this.left_arm = this.body.getChild("left_arm");
		this.left_arm_bottom = this.left_arm.getChild("left_arm_bottom");
		this.right_arm = this.body.getChild("right_arm");
		this.right_arm_bottom = this.right_arm.getChild("right_arm_bottom");
		this.telecomando = this.right_arm_bottom.getChild("telecomando");
		this.head = this.body.getChild("head");
		this.halo = this.head.getChild("halo");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition corpo_corposo = partdefinition.addOrReplaceChild("corpo_corposo", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 0.0F));
		PartDefinition body = corpo_corposo.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 23).addBox(-6.0F, -18.0F, -3.4F, 12.0F, 17.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(1, 82)
				.addBox(-6.0F, -3.0F, -3.5F, 12.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(1, 82).addBox(-6.0F, -3.0F, -3.3F, 12.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, 0.0F));
		PartDefinition left_leg = body.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(20, 55).addBox(-3.0F, -1.0F, -3.4F, 5.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.0F, 0.0F));
		PartDefinition left_leg_bottom = left_leg.addOrReplaceChild("left_leg_bottom", CubeListBuilder.create().texOffs(46, 0).addBox(-3.0F, -8.0F, -3.4F, 5.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, 0.0F));
		PartDefinition right_leg = body.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(56, 37).addBox(-2.0F, 0.0F, -3.4F, 5.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -1.0F, 0.0F));
		PartDefinition right_leg_bottom = right_leg.addOrReplaceChild("right_leg_bottom", CubeListBuilder.create().texOffs(42, 55).addBox(-2.0F, -8.0F, -3.4F, 5.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));
		PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(36, 37).addBox(0.0F, -3.0F, -3.4F, 4.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, -15.0F, 0.0F));
		PartDefinition left_arm_bottom = left_arm.addOrReplaceChild("left_arm_bottom", CubeListBuilder.create().texOffs(62, 14).addBox(-2.0F, -9.0F, -3.4F, 4.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 18.0F, 0.0F));
		PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 46).addBox(-4.0F, -3.25F, -3.2F, 4.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -14.75F, -0.2F));
		PartDefinition right_arm_bottom = right_arm.addOrReplaceChild("right_arm_bottom", CubeListBuilder.create().texOffs(0, 64).addBox(-2.0F, -8.0F, -3.4F, 4.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 16.75F, 0.2F));
		PartDefinition telecomando = right_arm_bottom.addOrReplaceChild("telecomando",
				CubeListBuilder.create().texOffs(46, 14).addBox(2.4765F, -0.5235F, -6.5588F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(20, 46).addBox(-2.5235F, -0.5235F, -6.5588F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(68, 52).addBox(-2.5235F, 0.4765F, -5.5588F, 5.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(68, 0).addBox(-2.5235F, -0.4235F, -5.5588F, 5.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(64, 51)
						.addBox(-2.5235F, -0.5235F, -1.5588F, 6.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(36, 23).addBox(-3.5235F, -0.5235F, -6.5588F, 1.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(20, 49)
						.addBox(-0.5235F, -0.0235F, -9.5588F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(28, 49).addBox(-1.1235F, -0.6235F, -11.5588F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(46, 21)
						.addBox(1.4765F, -0.9235F, -0.5588F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(32, 46).addBox(-0.5235F, -0.9235F, -0.5588F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(50, 21)
						.addBox(-2.5235F, -0.9235F, -0.5588F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(20, 53).addBox(1.4765F, -0.9235F, 1.4412F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 53)
						.addBox(-0.5235F, -0.9235F, 1.4412F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(28, 53).addBox(-2.5235F, -0.9235F, 1.4412F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(32, 53)
						.addBox(-2.5235F, -0.9235F, 3.4412F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(54, 21).addBox(-0.5235F, -0.9235F, 3.4412F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(56, 51)
						.addBox(1.4765F, -0.9235F, 3.4412F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-0.4765F, 0.5235F, -0.4412F));
		PartDefinition head = body.addOrReplaceChild("head",
				CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -12.0F, -6.0F, 12.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)).texOffs(30, 82).addBox(-6.6F, -12.5F, -6.6F, 13.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -17.0F, 0.0F));
		PartDefinition halo = head.addOrReplaceChild("halo", CubeListBuilder.create().texOffs(20, 69).addBox(-0.6F, -11.0F, -0.4F, 1.0F, 11.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(64, 60)
				.addBox(-4.5F, -12.0F, -1.0F, 9.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(64, 63).addBox(-4.5F, -12.0F, -12.0F, 9.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 5.0F));
		PartDefinition cube_r1 = halo.addOrReplaceChild("cube_r1",
				CubeListBuilder.create().texOffs(62, 29).addBox(-7.0F, -0.5F, -6.5F, 13.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(62, 32).addBox(-7.0F, -0.5F, 4.5F, 13.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -11.5F, -5.0F, 0.0F, -1.5708F, 0.0F));
		PartDefinition cube_r2 = halo.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(24, 69).addBox(-1.0F, -3.5F, -0.5F, 2.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.05F, -0.2F, 0.2F, 0.0F, 0.0F, -2.2689F));
		PartDefinition cube_r3 = halo.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(68, 5).addBox(-1.0F, -3.5F, -0.5F, 2.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.125F, -0.2F, 0.3F, 0.0F, 0.0F, -0.9163F));
		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	public void setupAnim(LivingEntityRenderState state) {
		float limbSwing = state.walkAnimationPos;
		float limbSwingAmount = state.walkAnimationSpeed;
		float ageInTicks = state.ageInTicks;
		float netHeadYaw = state.yRot;
		float headPitch = state.xRot;

		this.left_leg.xRot = Mth.cos(limbSwing * 1.0F) * -1.0F * limbSwingAmount;
		this.right_leg.xRot = Mth.cos(limbSwing * 1.0F) * 1.0F * limbSwingAmount;
	}
}