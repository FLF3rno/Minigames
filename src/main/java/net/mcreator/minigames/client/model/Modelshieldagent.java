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

// Made with Blockbench 5.1.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
public class Modelshieldagent extends EntityModel<LivingEntityRenderState> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("minigames", "modelshieldagent"), "main");
	public final ModelPart eye;
	public final ModelPart halo;
	public final ModelPart body;
	public final ModelPart shield;
	public final ModelPart shield2;
	public final ModelPart shield3;
	public final ModelPart shield4;

	public Modelshieldagent(ModelPart root) {
		super(root);
		this.eye = root.getChild("eye");
		this.halo = root.getChild("halo");
		this.body = root.getChild("body");
		this.shield = this.body.getChild("shield");
		this.shield2 = this.body.getChild("shield2");
		this.shield3 = this.body.getChild("shield3");
		this.shield4 = this.body.getChild("shield4");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition eye = partdefinition.addOrReplaceChild("eye", CubeListBuilder.create(), PartPose.offset(0.0F, 13.7F, 0.3F));
		PartDefinition cube_r1 = eye.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.4685F, 0.454F, -0.5987F));
		PartDefinition halo = partdefinition.addOrReplaceChild("halo",
				CubeListBuilder.create().texOffs(48, 20).addBox(-10.0F, -1.0F, -10.0F, 20.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(48, 20).addBox(-10.0F, -1.0F, 8.0F, 20.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.5041F, -9.25F, 0.2327F, 0.0F, -0.7854F, 0.0F));
		PartDefinition cube_r2 = halo.addOrReplaceChild("cube_r2",
				CubeListBuilder.create().texOffs(0, 83).addBox(-8.0F, -1.0F, -10.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(36, 83).addBox(-8.0F, -1.0F, 8.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));
		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition shield = body
				.addOrReplaceChild(
						"shield", CubeListBuilder.create().texOffs(0, 110).addBox(10.0F, -7.7F, -0.3F, 1.0F, 19.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(44, 24).addBox(-10.0F, -7.7F, 0.0F, 20.0F, 19.0F, 2.0F, new CubeDeformation(0.0F))
								.texOffs(16, 110).addBox(-11.0F, -7.7F, -0.3F, 1.0F, 19.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(48, 0).addBox(-11.0F, -8.7F, -0.3F, 22.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
						PartPose.offset(0.0F, -17.55F, 12.0F));
		PartDefinition cube_r3 = shield.addOrReplaceChild("cube_r3",
				CubeListBuilder.create().texOffs(39, 114).addBox(-1.5473F, -0.384F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(39, 114).addBox(-1.7327F, -1.309F, -1.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-2.7145F, 0.5127F, 1.6F, 0.0F, 0.0F, -2.7576F));
		PartDefinition cube_r4 = shield.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(39, 114).addBox(-1.542F, -0.035F, -1.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-2.7145F, 0.5127F, 1.6F, 0.0F, 0.0F, 2.9147F));
		PartDefinition cube_r5 = shield.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(39, 114).addBox(-2.5F, -0.5F, -1.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(3.6F, 0.6F, 1.6F, 0.0F, 0.0F, 0.384F));
		PartDefinition cube_r6 = shield.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(39, 114).addBox(-2.5F, 0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.8F, 0.6F, 1.6F, 0.0F, 0.0F, 0.384F));
		PartDefinition cube_r7 = shield.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(39, 114).addBox(-2.0F, -0.5F, -1.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(3.1364F, 1.4127F, 1.6F, 0.0F, 0.0F, -0.2269F));
		PartDefinition cube_r8 = shield.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(24, 111).addBox(-2.1684F, -1.0F, 12.5143F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.1684F, 0.3F, -11.9143F, 0.0F, 0.0F, 0.0F));
		PartDefinition cube_r9 = shield.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(104, 92).addBox(-0.5F, -9.0F, -1.0F, 1.0F, 20.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-6.0639F, 18.7566F, 0.7F, 0.0F, 0.0F, -0.5236F));
		PartDefinition cube_r10 = shield.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(52, 66).addBox(-1.0F, -5.5F, -1.0F, 10.0F, 15.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-5.9666F, 13.0281F, 0.825F, 0.0F, 0.0F, -0.5236F));
		PartDefinition cube_r11 = shield.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(104, 69).addBox(-1.0F, -9.5F, -1.0F, 2.0F, 20.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-5.1666F, 18.0281F, 0.925F, 0.0F, 0.0F, -0.5236F));
		PartDefinition cube_r12 = shield.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 87).addBox(-3.5F, -9.0F, -1.0F, 4.0F, 21.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(5.4611F, 14.4316F, 0.875F, 0.0F, 0.0F, 0.5672F));
		PartDefinition cube_r13 = shield.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(3, 87).addBox(-0.5F, -9.0F, -1.0F, 1.0F, 21.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-5.9389F, 14.2316F, 0.95F, 0.0F, 0.0F, -0.5236F));
		PartDefinition cube_r14 = shield.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(104, 46).addBox(-1.5F, -9.0F, -1.0F, 2.0F, 21.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(5.6611F, 17.4316F, 0.9F, 0.0F, 0.0F, 0.5672F));
		PartDefinition cube_r15 = shield.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(40, 87).addBox(-0.5F, -9.0F, -1.0F, 1.0F, 21.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(5.7611F, 18.5316F, 0.7F, 0.0F, 0.0F, 0.5672F));
		PartDefinition shield2 = body.addOrReplaceChild("shield2",
				CubeListBuilder.create().texOffs(0, 110).addBox(9.8316F, -22.4966F, 24.3643F, 1.0F, 19.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(44, 24).addBox(-10.1684F, -22.4966F, 24.6643F, 20.0F, 19.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(16, 110).addBox(-11.1684F, -22.4966F, 24.3643F, 1.0F, 19.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(48, 0).addBox(-11.1684F, -23.4966F, 24.3643F, 22.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.1684F, -2.7534F, 12.3357F, -3.1416F, 0.0F, 3.1416F));
		PartDefinition cube_r16 = shield2.addOrReplaceChild("cube_r16",
				CubeListBuilder.create().texOffs(39, 114).addBox(-1.7327F, -1.309F, -1.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(39, 114).addBox(-1.5473F, -0.384F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-2.8829F, -14.2839F, 26.2643F, 0.0F, 0.0F, -2.7576F));
		PartDefinition cube_r17 = shield2.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(39, 114).addBox(-1.542F, -0.035F, -1.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-2.8829F, -14.2839F, 26.2643F, 0.0F, 0.0F, 2.9147F));
		PartDefinition cube_r18 = shield2.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(39, 114).addBox(-2.0F, -0.5F, -1.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(2.968F, -13.3839F, 26.2643F, 0.0F, 0.0F, -0.2269F));
		PartDefinition cube_r19 = shield2.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(39, 114).addBox(-2.5F, 0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(3.6316F, -14.1966F, 26.2643F, 0.0F, 0.0F, 0.384F));
		PartDefinition cube_r20 = shield2.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(39, 114).addBox(-2.5F, -0.5F, -1.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(3.4316F, -14.1966F, 26.2643F, 0.0F, 0.0F, 0.384F));
		PartDefinition cube_r21 = shield2.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(24, 111).addBox(-2.1684F, -1.0F, 12.5143F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -14.4966F, 12.75F, 0.0F, 0.0F, 0.0F));
		PartDefinition cube_r22 = shield2.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(104, 92).addBox(-0.5F, -9.0F, -1.0F, 1.0F, 20.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-6.2323F, 3.96F, 25.3643F, 0.0F, 0.0F, -0.5236F));
		PartDefinition cube_r23 = shield2.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(52, 66).addBox(-1.0F, -5.5F, -1.0F, 10.0F, 15.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-6.1349F, -1.7686F, 25.4893F, 0.0F, 0.0F, -0.5236F));
		PartDefinition cube_r24 = shield2.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(104, 69).addBox(-1.0F, -9.5F, -1.0F, 2.0F, 20.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-5.3349F, 3.2314F, 25.5893F, 0.0F, 0.0F, -0.5236F));
		PartDefinition cube_r25 = shield2.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(0, 87).addBox(-3.5F, -9.0F, -1.0F, 4.0F, 21.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(5.2927F, -0.365F, 25.5393F, 0.0F, 0.0F, 0.5672F));
		PartDefinition cube_r26 = shield2.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(3, 87).addBox(-0.5F, -9.0F, -1.0F, 1.0F, 21.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-6.1073F, -0.565F, 25.6143F, 0.0F, 0.0F, -0.5236F));
		PartDefinition cube_r27 = shield2.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(104, 46).addBox(-1.5F, -9.0F, -1.0F, 2.0F, 21.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(5.4927F, 2.635F, 25.5643F, 0.0F, 0.0F, 0.5672F));
		PartDefinition cube_r28 = shield2.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(40, 87).addBox(-0.5F, -9.0F, -1.0F, 1.0F, 21.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(5.5927F, 3.735F, 25.3643F, 0.0F, 0.0F, 0.5672F));
		PartDefinition shield3 = body.addOrReplaceChild("shield3",
				CubeListBuilder.create().texOffs(0, 110).addBox(10.0F, -23.7F, 11.7F, 1.0F, 19.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(44, 24).addBox(-10.0F, -23.7F, 12.0F, 20.0F, 19.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(16, 110)
						.addBox(-11.0F, -23.7F, 11.7F, 1.0F, 19.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(48, 0).addBox(-11.0F, -24.7F, 11.7F, 22.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -1.55F, 0.0F, 0.0F, -1.5708F, 0.0F));
		PartDefinition cube_r29 = shield3.addOrReplaceChild("cube_r29",
				CubeListBuilder.create().texOffs(39, 114).addBox(-1.5473F, -0.384F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(39, 114).addBox(-1.7327F, -1.309F, -1.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-2.7145F, -15.4873F, 13.6F, 0.0F, 0.0F, -2.7576F));
		PartDefinition cube_r30 = shield3.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(39, 114).addBox(-1.542F, -0.035F, -1.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-2.7145F, -15.4873F, 13.6F, 0.0F, 0.0F, 2.9147F));
		PartDefinition cube_r31 = shield3.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(39, 114).addBox(-2.5F, -0.5F, -1.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(3.6F, -15.4F, 13.6F, 0.0F, 0.0F, 0.384F));
		PartDefinition cube_r32 = shield3.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(39, 114).addBox(-2.5F, 0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(3.8F, -15.4F, 13.6F, 0.0F, 0.0F, 0.384F));
		PartDefinition cube_r33 = shield3.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(39, 114).addBox(-2.0F, -0.5F, -1.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(3.1364F, -14.5873F, 13.6F, 0.0F, 0.0F, -0.2269F));
		PartDefinition cube_r34 = shield3.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(24, 111).addBox(-2.1684F, -1.0F, 12.5143F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.1684F, -15.7F, 0.0857F, 0.0F, 0.0F, 0.0F));
		PartDefinition cube_r35 = shield3.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(104, 92).addBox(-0.5F, -9.0F, -1.0F, 1.0F, 20.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-6.0639F, 2.7566F, 12.7F, 0.0F, 0.0F, -0.5236F));
		PartDefinition cube_r36 = shield3.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(52, 66).addBox(-1.0F, -5.5F, -1.0F, 10.0F, 15.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-5.9666F, -2.9719F, 12.825F, 0.0F, 0.0F, -0.5236F));
		PartDefinition cube_r37 = shield3.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(104, 69).addBox(-1.0F, -9.5F, -1.0F, 2.0F, 20.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-5.1666F, 2.0281F, 12.925F, 0.0F, 0.0F, -0.5236F));
		PartDefinition cube_r38 = shield3.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(0, 87).addBox(-3.5F, -9.0F, -1.0F, 4.0F, 21.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(5.4611F, -1.5684F, 12.875F, 0.0F, 0.0F, 0.5672F));
		PartDefinition cube_r39 = shield3.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(3, 87).addBox(-0.5F, -9.0F, -1.0F, 1.0F, 21.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-5.9389F, -1.7684F, 12.95F, 0.0F, 0.0F, -0.5236F));
		PartDefinition cube_r40 = shield3.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(104, 46).addBox(-1.5F, -9.0F, -1.0F, 2.0F, 21.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(5.6611F, 1.4316F, 12.9F, 0.0F, 0.0F, 0.5672F));
		PartDefinition cube_r41 = shield3.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(40, 87).addBox(-0.5F, -9.0F, -1.0F, 1.0F, 21.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(5.7611F, 2.5316F, 12.7F, 0.0F, 0.0F, 0.5672F));
		PartDefinition shield4 = body.addOrReplaceChild("shield4",
				CubeListBuilder.create().texOffs(0, 110).addBox(-2.6643F, -22.3466F, 11.5316F, 1.0F, 19.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(44, 24).addBox(-22.6643F, -22.4966F, 11.8316F, 20.0F, 19.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(16, 110).addBox(-23.6643F, -22.4966F, 11.5316F, 1.0F, 19.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(48, 0).addBox(-23.6643F, -23.4966F, 11.5316F, 22.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.3643F, -2.7534F, -12.3316F, 0.0F, 1.5708F, 0.0F));
		PartDefinition cube_r42 = shield4.addOrReplaceChild("cube_r42",
				CubeListBuilder.create().texOffs(39, 114).addBox(-1.7327F, -1.309F, -1.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(39, 114).addBox(-1.5473F, -0.384F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-15.0788F, -14.2839F, 13.4316F, 0.0F, 0.0F, -2.7576F));
		PartDefinition cube_r43 = shield4.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(24, 111).addBox(-2.1684F, -1.0F, 12.5143F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-12.1959F, -14.4966F, -0.0827F, 0.0F, 0.0F, 0.0F));
		PartDefinition cube_r44 = shield4.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(39, 114).addBox(-2.0F, -0.5F, -1.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-9.2279F, -13.3839F, 13.4316F, 0.0F, 0.0F, -0.2269F));
		PartDefinition cube_r45 = shield4.addOrReplaceChild("cube_r45", CubeListBuilder.create().texOffs(39, 114).addBox(-2.5F, 0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-8.5643F, -14.1966F, 13.4316F, 0.0F, 0.0F, 0.384F));
		PartDefinition cube_r46 = shield4.addOrReplaceChild("cube_r46", CubeListBuilder.create().texOffs(39, 114).addBox(-2.5F, -0.5F, -1.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-8.7643F, -14.1966F, 13.4316F, 0.0F, 0.0F, 0.384F));
		PartDefinition cube_r47 = shield4.addOrReplaceChild("cube_r47", CubeListBuilder.create().texOffs(39, 114).addBox(-1.542F, -0.035F, -1.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-15.0788F, -14.2839F, 13.4316F, 0.0F, 0.0F, 2.9147F));
		PartDefinition cube_r48 = shield4.addOrReplaceChild("cube_r48", CubeListBuilder.create().texOffs(104, 92).addBox(-0.5F, -9.0F, -1.0F, 1.0F, 20.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-18.7282F, 3.96F, 12.5316F, 0.0F, 0.0F, -0.5236F));
		PartDefinition cube_r49 = shield4.addOrReplaceChild("cube_r49", CubeListBuilder.create().texOffs(52, 66).addBox(-1.0F, -5.5F, -1.0F, 10.0F, 15.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-18.6309F, -1.7686F, 12.6566F, 0.0F, 0.0F, -0.5236F));
		PartDefinition cube_r50 = shield4.addOrReplaceChild("cube_r50", CubeListBuilder.create().texOffs(104, 69).addBox(-1.0F, -9.5F, -1.0F, 2.0F, 20.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-17.8309F, 3.2314F, 12.7566F, 0.0F, 0.0F, -0.5236F));
		PartDefinition cube_r51 = shield4.addOrReplaceChild("cube_r51", CubeListBuilder.create().texOffs(0, 87).addBox(-3.5F, -9.0F, -1.0F, 4.0F, 21.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-7.2032F, -0.365F, 12.7066F, 0.0F, 0.0F, 0.5672F));
		PartDefinition cube_r52 = shield4.addOrReplaceChild("cube_r52", CubeListBuilder.create().texOffs(3, 87).addBox(-0.5F, -9.0F, -1.0F, 1.0F, 21.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-18.6032F, -0.565F, 12.7816F, 0.0F, 0.0F, -0.5236F));
		PartDefinition cube_r53 = shield4.addOrReplaceChild("cube_r53", CubeListBuilder.create().texOffs(104, 46).addBox(-1.5F, -9.0F, -1.0F, 2.0F, 21.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-7.0032F, 2.635F, 12.7316F, 0.0F, 0.0F, 0.5672F));
		PartDefinition cube_r54 = shield4.addOrReplaceChild("cube_r54", CubeListBuilder.create().texOffs(40, 87).addBox(-0.5F, -9.0F, -1.0F, 1.0F, 21.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-6.9032F, 3.735F, 12.5316F, 0.0F, 0.0F, 0.5672F));
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