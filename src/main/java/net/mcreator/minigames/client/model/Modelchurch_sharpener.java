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

// Made with Blockbench 5.2.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
public class Modelchurch_sharpener extends EntityModel<LivingEntityRenderState> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath("minigames", "modelchurch_sharpener"), "main");
	public final ModelPart entity;
	public final ModelPart Head;
	public final ModelPart waist;
	public final ModelPart Body;
	public final ModelPart RightArm;
	public final ModelPart RightArm2;
	public final ModelPart Dagger;
	public final ModelPart LeftArm;
	public final ModelPart LeftArm2;
	public final ModelPart Dagger2;
	public final ModelPart RightLeg;
	public final ModelPart LeftLeg;

	public Modelchurch_sharpener(ModelPart root) {
		super(root);
		this.entity = root.getChild("entity");
		this.Head = this.entity.getChild("Head");
		this.waist = this.Head.getChild("waist");
		this.Body = this.waist.getChild("Body");
		this.RightArm = this.entity.getChild("RightArm");
		this.RightArm2 = this.RightArm.getChild("RightArm2");
		this.Dagger = this.RightArm2.getChild("Dagger");
		this.LeftArm = this.entity.getChild("LeftArm");
		this.LeftArm2 = this.LeftArm.getChild("LeftArm2");
		this.Dagger2 = this.LeftArm2.getChild("Dagger2");
		this.RightLeg = this.entity.getChild("RightLeg");
		this.LeftLeg = this.entity.getChild("LeftLeg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition entity = partdefinition.addOrReplaceChild("entity", CubeListBuilder.create(), PartPose.offset(0.0F, 12.6F, 9.0F));
		PartDefinition Head = entity.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -20.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.6F, -9.0F));
		PartDefinition waist = Head.addOrReplaceChild("waist", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition Body = waist.addOrReplaceChild("Body",
				CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(37, 10).addBox(-4.525F, -0.75F, -2.325F, 9.0F, 13.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -12.0F, 0.0F));
		PartDefinition RightArm = entity.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(32, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -13.6F, -9.0F));
		PartDefinition RightArm2 = RightArm.addOrReplaceChild("RightArm2", CubeListBuilder.create().texOffs(32, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 0.0F));
		PartDefinition Dagger = RightArm2.addOrReplaceChild("Dagger",
				CubeListBuilder.create().texOffs(51, 55).addBox(-0.4967F, -0.8386F, -5.5303F, 1.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(30, 54).addBox(-0.4967F, -0.8376F, -13.5303F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.1967F, 5.8376F, 0.1303F));
		PartDefinition cube_r1 = Dagger.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(30, 54).addBox(-0.49F, -0.5F, -4.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.0167F, 0.1751F, -9.4394F, -0.1309F, 0.0F, 0.0F));
		PartDefinition LeftArm = entity.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(8, 32).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -13.6F, -9.0F));
		PartDefinition LeftArm2 = LeftArm.addOrReplaceChild("LeftArm2", CubeListBuilder.create().texOffs(8, 32).addBox(-0.999F, -1.0F, -1.001F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 0.0F));
		PartDefinition Dagger2 = LeftArm2.addOrReplaceChild("Dagger2",
				CubeListBuilder.create().texOffs(51, 55).addBox(-0.4967F, -0.8386F, -6.5303F, 1.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(30, 54).addBox(-0.4967F, -0.8376F, -14.5303F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-0.0033F, 5.8376F, 1.1303F));
		PartDefinition cube_r2 = Dagger2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(30, 54).addBox(-0.49F, -0.5F, -4.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.0167F, 0.1751F, -10.4394F, -0.1309F, 0.0F, 0.0F));
		PartDefinition RightLeg = entity.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(0, 32).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 15.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -3.6F, -9.0F));
		PartDefinition LeftLeg = entity.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(24, 16).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 16.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -3.6F, -9.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	public void setupAnim(LivingEntityRenderState state) {
		float limbSwing = state.walkAnimationPos;
		float limbSwingAmount = state.walkAnimationSpeed;
		float ageInTicks = state.ageInTicks;
		float netHeadYaw = state.yRot;
		float headPitch = state.xRot;

		this.LeftLeg.xRot = Mth.cos(limbSwing * 1.0F) * -1.0F * limbSwingAmount;
		this.RightLeg.xRot = Mth.cos(limbSwing * 1.0F) * 1.0F * limbSwingAmount;
	}
}