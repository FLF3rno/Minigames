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
public class Modelbookling extends EntityModel<LivingEntityRenderState> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath("minigames", "modelbookling"), "main");
	public final ModelPart body;
	public final ModelPart book;
	public final ModelPart lower;
	public final ModelPart upper;
	public final ModelPart top_leg_1;
	public final ModelPart bottom_leg_1;
	public final ModelPart top_leg_2;
	public final ModelPart bottom_leg_2;
	public final ModelPart top_leg_3;
	public final ModelPart bottom_leg_3;
	public final ModelPart top_leg_4;
	public final ModelPart bottom_leg_4;
	public final ModelPart top_leg_5;
	public final ModelPart bottom_leg_5;
	public final ModelPart top_leg_6;
	public final ModelPart bottom_leg_6;
	public final ModelPart top_leg_7;
	public final ModelPart bottom_leg_7;
	public final ModelPart top_leg_8;
	public final ModelPart bottom_leg_8;

	public Modelbookling(ModelPart root) {
		super(root);
		this.body = root.getChild("body");
		this.book = this.body.getChild("book");
		this.lower = this.book.getChild("lower");
		this.upper = this.book.getChild("upper");
		this.top_leg_1 = this.body.getChild("top_leg_1");
		this.bottom_leg_1 = this.top_leg_1.getChild("bottom_leg_1");
		this.top_leg_2 = this.body.getChild("top_leg_2");
		this.bottom_leg_2 = this.top_leg_2.getChild("bottom_leg_2");
		this.top_leg_3 = this.body.getChild("top_leg_3");
		this.bottom_leg_3 = this.top_leg_3.getChild("bottom_leg_3");
		this.top_leg_4 = this.body.getChild("top_leg_4");
		this.bottom_leg_4 = this.top_leg_4.getChild("bottom_leg_4");
		this.top_leg_5 = this.body.getChild("top_leg_5");
		this.bottom_leg_5 = this.top_leg_5.getChild("bottom_leg_5");
		this.top_leg_6 = this.body.getChild("top_leg_6");
		this.bottom_leg_6 = this.top_leg_6.getChild("bottom_leg_6");
		this.top_leg_7 = this.body.getChild("top_leg_7");
		this.bottom_leg_7 = this.top_leg_7.getChild("bottom_leg_7");
		this.top_leg_8 = this.body.getChild("top_leg_8");
		this.bottom_leg_8 = this.top_leg_8.getChild("bottom_leg_8");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition book = body.addOrReplaceChild("book", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition lower = book.addOrReplaceChild("lower", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -6.0F, -7.0F, 16.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)).texOffs(56, 32)
				.addBox(-8.0F, -8.0F, -7.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 47).addBox(-7.6F, -6.9F, -6.5F, 15.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition upper = book.addOrReplaceChild("upper",
				CubeListBuilder.create().texOffs(0, 16).addBox(-8.0F, -2.0F, -1.7F, 16.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)).texOffs(0, 32).addBox(-7.6F, -1.1F, -1.2F, 15.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -8.0F, -5.3F, -0.0262F, 0.0F, 0.0F));
		PartDefinition top_leg_1 = body.addOrReplaceChild("top_leg_1", CubeListBuilder.create(), PartPose.offset(7.9778F, -4.7961F, -4.5F));
		PartDefinition cube_r1 = top_leg_1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(57, 36).addBox(-1.1F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.1222F, -0.1039F, 0.0F, 0.0F, 0.0F, -0.1745F));
		PartDefinition bottom_leg_1 = top_leg_1.addOrReplaceChild("bottom_leg_1", CubeListBuilder.create(), PartPose.offsetAndRotation(3.4915F, -0.7398F, 0.0F, 0.0F, 0.0F, 0.2618F));
		PartDefinition cube_r2 = bottom_leg_1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(56, 52).addBox(2.9F, -0.5F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-3.3523F, 0.7654F, 0.0F, 0.0F, 0.0F, -0.1745F));
		PartDefinition top_leg_2 = body.addOrReplaceChild("top_leg_2", CubeListBuilder.create(), PartPose.offset(7.9778F, -4.7961F, -1.5F));
		PartDefinition cube_r3 = top_leg_2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(56, 38).addBox(-2.1F, -0.5F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.1222F, -0.1039F, 0.0F, 0.0F, 0.0F, -0.1745F));
		PartDefinition bottom_leg_2 = top_leg_2.addOrReplaceChild("bottom_leg_2", CubeListBuilder.create(), PartPose.offsetAndRotation(3.4915F, -0.7398F, 0.0F, 0.0F, 0.0F, 0.2618F));
		PartDefinition cube_r4 = bottom_leg_2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(60, 0).addBox(2.9F, -0.5F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-3.3523F, 0.7654F, 0.0F, 0.0F, 0.0F, -0.1745F));
		PartDefinition top_leg_3 = body.addOrReplaceChild("top_leg_3", CubeListBuilder.create(), PartPose.offset(7.9778F, -4.7961F, 4.5F));
		PartDefinition cube_r5 = top_leg_3.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(57, 40).addBox(-1.1F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.1222F, -0.1039F, 0.0F, 0.0F, 0.0F, -0.1745F));
		PartDefinition bottom_leg_3 = top_leg_3.addOrReplaceChild("bottom_leg_3", CubeListBuilder.create(), PartPose.offsetAndRotation(3.4915F, -0.7398F, 0.0F, 0.0F, 0.0F, 0.2618F));
		PartDefinition cube_r6 = bottom_leg_3.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(60, 8).addBox(2.9F, -0.5F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-3.3523F, 0.7654F, 0.0F, 0.0F, 0.0F, -0.1745F));
		PartDefinition top_leg_4 = body.addOrReplaceChild("top_leg_4", CubeListBuilder.create(), PartPose.offset(7.9778F, -4.7961F, 1.5F));
		PartDefinition cube_r7 = top_leg_4.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(56, 42).addBox(-2.1F, -0.5F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.1222F, -0.1039F, 0.0F, 0.0F, 0.0F, -0.1745F));
		PartDefinition bottom_leg_4 = top_leg_4.addOrReplaceChild("bottom_leg_4", CubeListBuilder.create(), PartPose.offsetAndRotation(3.4915F, -0.7398F, 0.0F, 0.0F, 0.0F, 0.2618F));
		PartDefinition cube_r8 = bottom_leg_4.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(60, 16).addBox(2.9F, -0.5F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-3.3523F, 0.7654F, 0.0F, 0.0F, 0.0F, -0.1745F));
		PartDefinition top_leg_5 = body.addOrReplaceChild("top_leg_5", CubeListBuilder.create(), PartPose.offsetAndRotation(-7.9722F, -4.8961F, 4.5F, 0.0F, 3.1416F, 0.0F));
		PartDefinition cube_r9 = top_leg_5.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(58, 44).addBox(-0.1F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.3278F, -0.0039F, 0.0F, 0.0F, 0.0F, -0.1745F));
		PartDefinition bottom_leg_5 = top_leg_5.addOrReplaceChild("bottom_leg_5", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0415F, -0.6398F, 0.0F, 0.0F, 0.0F, 0.2618F));
		PartDefinition cube_r10 = bottom_leg_5.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(60, 24).addBox(2.9F, -0.5F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-3.3523F, 0.7654F, 0.0F, 0.0F, 0.0F, -0.1745F));
		PartDefinition top_leg_6 = body.addOrReplaceChild("top_leg_6", CubeListBuilder.create(), PartPose.offsetAndRotation(-7.9722F, -4.9961F, 1.5F, 0.0F, 3.1416F, 0.0F));
		PartDefinition cube_r11 = top_leg_6.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(58, 46).addBox(-0.1F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.3278F, 0.0961F, 0.0F, 0.0F, 0.0F, -0.1745F));
		PartDefinition bottom_leg_6 = top_leg_6.addOrReplaceChild("bottom_leg_6", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0415F, -0.5398F, 0.0F, 0.0F, 0.0F, 0.2618F));
		PartDefinition cube_r12 = bottom_leg_6.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(60, 60).addBox(2.9F, -0.5F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-3.3523F, 0.7654F, 0.0F, 0.0F, 0.0F, -0.1745F));
		PartDefinition top_leg_7 = body.addOrReplaceChild("top_leg_7", CubeListBuilder.create(), PartPose.offsetAndRotation(-7.7722F, -4.8961F, -1.5F, 0.0F, 3.1416F, 0.0F));
		PartDefinition cube_r13 = top_leg_7.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(58, 48).addBox(-0.1F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.1278F, -0.0039F, 0.0F, 0.0F, 0.0F, -0.1745F));
		PartDefinition bottom_leg_7 = top_leg_7.addOrReplaceChild("bottom_leg_7", CubeListBuilder.create(), PartPose.offsetAndRotation(3.2415F, -0.6398F, 0.0F, 0.0F, 0.0F, 0.2618F));
		PartDefinition cube_r14 = bottom_leg_7.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(60, 52).addBox(2.9F, -0.5F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-3.3523F, 0.7654F, 0.0F, 0.0F, 0.0F, -0.1745F));
		PartDefinition top_leg_8 = body.addOrReplaceChild("top_leg_8", CubeListBuilder.create(), PartPose.offsetAndRotation(-7.8722F, -5.2961F, -4.5F, 0.0F, 3.1416F, 0.0F));
		PartDefinition cube_r15 = top_leg_8.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(58, 50).addBox(-0.1F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.2278F, 0.3961F, 0.0F, 0.0F, 0.0F, -0.1745F));
		PartDefinition bottom_leg_8 = top_leg_8.addOrReplaceChild("bottom_leg_8", CubeListBuilder.create(), PartPose.offsetAndRotation(3.1415F, -0.2398F, 0.0F, 0.0F, 0.0F, 0.2618F));
		PartDefinition cube_r16 = bottom_leg_8.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(56, 60).addBox(2.9F, -0.5F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-3.3523F, 0.7654F, 0.0F, 0.0F, 0.0F, -0.1745F));
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