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

// Made with Blockbench 5.1.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
public class Modelcandlehead extends EntityModel<LivingEntityRenderState> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath("minigames", "modelcandlehead"), "main");
	public final ModelPart waist;
	public final ModelPart Body;
	public final ModelPart Head;
	public final ModelPart RightArm;
	public final ModelPart RightLeg;
	public final ModelPart LeftLeg;

	public Modelcandlehead(ModelPart root) {
		super(root);
		this.waist = root.getChild("waist");
		this.Body = this.waist.getChild("Body");
		this.Head = root.getChild("Head");
		this.RightArm = root.getChild("RightArm");
		this.RightLeg = root.getChild("RightLeg");
		this.LeftLeg = root.getChild("LeftLeg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition waist = partdefinition.addOrReplaceChild("waist", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));
		PartDefinition Body = waist.addOrReplaceChild("Body",
				CubeListBuilder.create().texOffs(16, 17).addBox(-4.0F, -5.3271F, -3.0114F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(38, 0).addBox(-4.5F, -5.3271F, -3.5114F, 9.0F, 12.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -6.2045F, 1.0471F));
		PartDefinition cube_r1 = Body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(2, 2).addBox(-0.5F, -2.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(2.6F, 1.9044F, -4.1471F, -3.1416F, 0.7854F, -2.3998F));
		PartDefinition cube_r2 = Body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(2, 2).addBox(-0.5F, -2.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(2.6F, 1.9044F, -4.1471F, 0.0F, 0.7854F, 0.7418F));
		PartDefinition cube_r3 = Body.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(8, 28).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(2.6F, 1.9044F, -4.1471F, 0.0F, 0.0F, 0.7418F));
		PartDefinition cube_r4 = Body.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(2, 2).addBox(-0.5F, -2.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.6F, -1.0956F, -4.1471F, -3.1416F, 0.7854F, -2.3998F));
		PartDefinition cube_r5 = Body.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(2, 2).addBox(-0.5F, -2.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.6F, -1.0956F, -4.1471F, 0.0F, 0.7854F, 0.7418F));
		PartDefinition cube_r6 = Body.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(8, 28).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.6F, -1.0956F, -4.1471F, 0.0F, 0.0F, 0.7418F));
		PartDefinition cube_r7 = Body.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(2, 2).addBox(-0.5F, -2.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-1.4F, -4.1956F, -4.1471F, -3.1416F, 0.7854F, -2.3998F));
		PartDefinition cube_r8 = Body.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(2, 2).addBox(-0.5F, -2.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-1.4F, -4.1956F, -4.1471F, 0.0F, 0.7854F, 0.7418F));
		PartDefinition cube_r9 = Body.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(8, 28).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-1.4F, -4.1956F, -4.1471F, 0.0F, 0.0F, 0.7418F));
		PartDefinition Head = partdefinition.addOrReplaceChild("Head",
				CubeListBuilder.create().texOffs(0, 1).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(8, 28).addBox(-1.0F, -13.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(4, 28)
						.addBox(-2.0F, -11.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(8, 28).addBox(1.2F, -12.075F, -1.025F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(8, 28)
						.addBox(-3.1F, -12.075F, -1.025F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(8, 28).addBox(-3.3F, -11.375F, 1.375F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(8, 28)
						.addBox(-3.3F, -11.375F, -3.325F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(8, 28).addBox(-3.1F, -9.375F, -3.725F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(8, 28)
						.addBox(-0.1F, -9.875F, -3.725F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(8, 28).addBox(2.7F, -9.475F, -3.625F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(8, 28)
						.addBox(-3.55F, -9.475F, -3.825F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(8, 28).addBox(-3.675F, -9.175F, -0.925F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(9, 29)
						.addBox(-3.575F, -9.45F, 1.6F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(8, 28).addBox(2.675F, -9.175F, -0.925F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(10, 30)
						.addBox(2.7F, -9.575F, 2.075F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 28).addBox(1.4F, -11.375F, -3.325F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(8, 28)
						.addBox(1.4F, -11.375F, 1.375F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(8, 28).addBox(-1.0F, -12.075F, 1.175F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(8, 28)
						.addBox(-1.0F, -12.075F, -3.125F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition cube_r10 = Head.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(2, 2).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -12.575F, -2.125F, 0.0F, 0.7854F, 0.0F));
		PartDefinition cube_r11 = Head.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(2, 2).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -12.575F, -2.125F, 0.0F, 2.3562F, 0.0F));
		PartDefinition cube_r12 = Head.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(2, 2).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -12.575F, 2.175F, 0.0F, 2.3562F, 0.0F));
		PartDefinition cube_r13 = Head.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(2, 2).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -12.575F, 2.175F, 0.0F, 0.7854F, 0.0F));
		PartDefinition cube_r14 = Head.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(2, 2).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(2.4F, -11.875F, 2.375F, 0.0F, 2.3562F, 0.0F));
		PartDefinition cube_r15 = Head.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(2, 2).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(2.4F, -11.875F, 2.375F, 0.0F, 0.7854F, 0.0F));
		PartDefinition cube_r16 = Head.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(2, 2).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(2.4F, -11.875F, -2.325F, 0.0F, 2.3562F, 0.0F));
		PartDefinition cube_r17 = Head.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(2, 2).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(2.4F, -11.875F, -2.325F, 0.0F, 0.7854F, 0.0F));
		PartDefinition cube_r18 = Head.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(2, 2).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-2.3F, -11.875F, -2.325F, 0.0F, 2.3562F, 0.0F));
		PartDefinition cube_r19 = Head.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(2, 2).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-2.3F, -11.875F, -2.325F, 0.0F, 0.7854F, 0.0F));
		PartDefinition cube_r20 = Head.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(9, 29).addBox(-1.5F, -1.5F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-3.65F, -8.05F, 2.725F, 0.0F, 3.1416F, 0.0F));
		PartDefinition cube_r21 = Head.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(9, 29).addBox(-1.5F, -1.5F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.65F, -7.425F, 2.6F, 0.0F, 3.1416F, 0.0F));
		PartDefinition cube_r22 = Head.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(9, 29).addBox(-1.5F, -1.5F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(2.15F, -7.675F, 2.575F, 0.0F, 3.1416F, 0.0F));
		PartDefinition cube_r23 = Head.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(2, 2).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-2.3F, -11.875F, 2.375F, 0.0F, 2.3562F, 0.0F));
		PartDefinition cube_r24 = Head.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(2, 2).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-2.3F, -11.875F, 2.375F, 0.0F, 0.7854F, 0.0F));
		PartDefinition cube_r25 = Head.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(2, 2).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-2.1F, -12.575F, -0.025F, 0.0F, 2.3562F, 0.0F));
		PartDefinition cube_r26 = Head.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(2, 2).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-2.1F, -12.575F, -0.025F, 0.0F, 0.7854F, 0.0F));
		PartDefinition cube_r27 = Head.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(2, 2).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(2.2F, -12.575F, -0.025F, 0.0F, 0.7854F, 0.0F));
		PartDefinition cube_r28 = Head.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(2, 2).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(2.2F, -12.575F, -0.025F, 0.0F, 2.3562F, 0.0F));
		PartDefinition cube_r29 = Head.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(2, 2).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -13.5F, 0.0F, 0.0F, 2.3562F, 0.0F));
		PartDefinition cube_r30 = Head.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(2, 2).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -13.5F, 0.0F, 0.0F, 0.7854F, 0.0F));
		PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(40, 17).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));
		PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(0, 17).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 12.0F, 0.0F));
		PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(0, 17).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, 12.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 66, 35);
	}

	public void setupAnim(LivingEntityRenderState state) {
		float limbSwing = state.walkAnimationPos;
		float limbSwingAmount = state.walkAnimationSpeed;
		float ageInTicks = state.ageInTicks;
		float netHeadYaw = state.yRot;
		float headPitch = state.xRot;

		this.RightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
		this.LeftLeg.xRot = Mth.cos(limbSwing * 1.0F) * -1.0F * limbSwingAmount;
		this.Head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.Head.xRot = headPitch / (180F / (float) Math.PI);
		this.RightLeg.xRot = Mth.cos(limbSwing * 1.0F) * 1.0F * limbSwingAmount;
	}
}