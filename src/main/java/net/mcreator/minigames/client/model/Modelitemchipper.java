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

// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
public class Modelitemchipper extends EntityModel<LivingEntityRenderState> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath("minigames", "modelitemchipper"), "main");
	public final ModelPart itemchipper;
	public final ModelPart blade;
	public final ModelPart blade7;
	public final ModelPart blade4;
	public final ModelPart blade6;
	public final ModelPart blade2;
	public final ModelPart blade3;
	public final ModelPart blade5;
	public final ModelPart blade8;

	public Modelitemchipper(ModelPart root) {
		super(root);
		this.itemchipper = root.getChild("itemchipper");
		this.blade = this.itemchipper.getChild("blade");
		this.blade7 = this.itemchipper.getChild("blade7");
		this.blade4 = this.itemchipper.getChild("blade4");
		this.blade6 = this.itemchipper.getChild("blade6");
		this.blade2 = this.itemchipper.getChild("blade2");
		this.blade3 = this.itemchipper.getChild("blade3");
		this.blade5 = this.itemchipper.getChild("blade5");
		this.blade8 = this.itemchipper.getChild("blade8");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition itemchipper = partdefinition.addOrReplaceChild("itemchipper", CubeListBuilder.create().texOffs(44, 40).addBox(-8.4444F, -0.9745F, -1.0669F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.4444F, 20.9745F, 0.0669F));
		PartDefinition blade = itemchipper.addOrReplaceChild("blade", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -4.9713F, -5.0752F, 1.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.3444F, -0.0032F, 0.0084F));
		PartDefinition cube_r1 = blade.addOrReplaceChild("cube_r1",
				CubeListBuilder.create().texOffs(48, 62).addBox(-0.5F, -4.4159F, -5.7339F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(70, 0).addBox(-0.5F, -5.0999F, -3.8546F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(44, 62)
						.addBox(-0.5F, -5.784F, -1.9752F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 60).addBox(-0.5F, -6.468F, -0.0958F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(60, 59)
						.addBox(-0.5F, -7.152F, 1.7836F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(70, 33).addBox(-0.5F, 3.0999F, 2.8546F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(60, 56)
						.addBox(-0.5F, 3.784F, 0.9752F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 44).addBox(-0.5F, 4.468F, -0.9042F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(70, 30)
						.addBox(-0.5F, 5.152F, -2.7836F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(60, 53).addBox(-0.5F, 2.4159F, 4.7339F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -0.0014F, 0.0038F, -1.9199F, 0.0F, 0.0F));
		PartDefinition cube_r2 = blade.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(44, 47).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, 3.9248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r3 = blade.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(70, 27).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, -4.0752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r4 = blade.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(44, 44).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, -2.0752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r5 = blade.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(60, 50).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, -0.0752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r6 = blade.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(70, 24).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, 1.9248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r7 = blade.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(60, 47).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, 4.4248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r8 = blade.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(60, 44).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, 2.4248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r9 = blade.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(40, 60).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, 0.4248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r10 = blade.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(36, 60).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, -1.5752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r11 = blade.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(32, 60).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, -3.5752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition blade7 = itemchipper.addOrReplaceChild("blade7", CubeListBuilder.create().texOffs(44, 0).addBox(-0.5F, -4.9713F, -5.0752F, 1.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.6556F, -0.0032F, 0.0084F));
		PartDefinition cube_r12 = blade7.addOrReplaceChild("cube_r12",
				CubeListBuilder.create().texOffs(68, 68).addBox(-0.5F, -4.4159F, -5.7339F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(70, 18).addBox(-0.5F, -5.0999F, -3.8546F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(68, 65)
						.addBox(-0.5F, -5.784F, -1.9752F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 60).addBox(-0.5F, -6.468F, -0.0958F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(64, 68)
						.addBox(-0.5F, -7.152F, 1.7836F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(72, 56).addBox(-0.5F, 3.0999F, 2.8546F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(68, 62)
						.addBox(-0.5F, 3.784F, 0.9752F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(44, 59).addBox(-0.5F, 4.468F, -0.9042F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(72, 53)
						.addBox(-0.5F, 5.152F, -2.7836F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(60, 68).addBox(-0.5F, 2.4159F, 4.7339F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -0.0014F, 0.0038F, -1.9199F, 0.0F, 0.0F));
		PartDefinition cube_r13 = blade7.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(56, 56).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, 3.9248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r14 = blade7.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(72, 50).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, -4.0752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r15 = blade7.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(56, 53).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, -2.0752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r16 = blade7.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(68, 59).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, -0.0752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r17 = blade7.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(72, 47).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, 1.9248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r18 = blade7.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(68, 56).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, 4.4248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r19 = blade7.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(56, 68).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, 2.4248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r20 = blade7.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(68, 53).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, 0.4248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r21 = blade7.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(52, 68).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, -1.5752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r22 = blade7.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(68, 50).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, -3.5752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition blade4 = itemchipper.addOrReplaceChild("blade4", CubeListBuilder.create().texOffs(22, 20).addBox(-0.5F, -4.9713F, -5.0752F, 1.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.3444F, -0.0032F, 0.0084F));
		PartDefinition cube_r23 = blade4.addOrReplaceChild("cube_r23",
				CubeListBuilder.create().texOffs(8, 66).addBox(-0.5F, -4.4159F, -5.7339F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(70, 9).addBox(-0.5F, -5.0999F, -3.8546F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(66, 6)
						.addBox(-0.5F, -5.784F, -1.9752F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 60).addBox(-0.5F, -6.468F, -0.0958F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 66)
						.addBox(-0.5F, -7.152F, 1.7836F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 72).addBox(-0.5F, 3.0999F, 2.8546F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(66, 3)
						.addBox(-0.5F, 3.784F, 0.9752F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(52, 53).addBox(-0.5F, 4.468F, -0.9042F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 72)
						.addBox(-0.5F, 5.152F, -2.7836F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(66, 0).addBox(-0.5F, 2.4159F, 4.7339F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -0.0014F, 0.0038F, -1.9199F, 0.0F, 0.0F));
		PartDefinition cube_r24 = blade4.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(48, 53).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, 3.9248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r25 = blade4.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(4, 72).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, -4.0752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r26 = blade4.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(44, 53).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, -2.0752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r27 = blade4.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(0, 66).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, -0.0752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r28 = blade4.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(0, 72).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, 1.9248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r29 = blade4.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(64, 65).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, 4.4248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r30 = blade4.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(60, 65).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, 2.4248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r31 = blade4.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(56, 65).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, 0.4248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r32 = blade4.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(52, 65).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, -1.5752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r33 = blade4.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(48, 65).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, -3.5752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition blade6 = itemchipper.addOrReplaceChild("blade6", CubeListBuilder.create().texOffs(22, 40).addBox(-0.5F, -4.9713F, -5.0752F, 1.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(4.6556F, -0.0032F, 0.0084F));
		PartDefinition cube_r34 = blade6.addOrReplaceChild("cube_r34",
				CubeListBuilder.create().texOffs(48, 68).addBox(-0.5F, -4.4159F, -5.7339F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(70, 15).addBox(-0.5F, -5.0999F, -3.8546F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(68, 47)
						.addBox(-0.5F, -5.784F, -1.9752F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(20, 60).addBox(-0.5F, -6.468F, -0.0958F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(68, 44)
						.addBox(-0.5F, -7.152F, 1.7836F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(72, 44).addBox(-0.5F, 3.0999F, 2.8546F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(44, 68)
						.addBox(-0.5F, 3.784F, 0.9752F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(52, 56).addBox(-0.5F, 4.468F, -0.9042F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(40, 72)
						.addBox(-0.5F, 5.152F, -2.7836F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(40, 66).addBox(-0.5F, 2.4159F, 4.7339F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -0.0014F, 0.0038F, -1.9199F, 0.0F, 0.0F));
		PartDefinition cube_r35 = blade6.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(56, 50).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, 3.9248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r36 = blade6.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(36, 72).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, -4.0752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r37 = blade6.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(48, 56).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, -2.0752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r38 = blade6.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(66, 36).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, -0.0752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r39 = blade6.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(32, 72).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, 1.9248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r40 = blade6.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(36, 66).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, 4.4248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r41 = blade6.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(66, 33).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, 2.4248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r42 = blade6.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(32, 66).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, 0.4248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r43 = blade6.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(66, 30).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, -1.5752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r44 = blade6.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(28, 66).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, -3.5752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition blade2 = itemchipper.addOrReplaceChild("blade2", CubeListBuilder.create().texOffs(0, 20).addBox(-0.5F, -4.9713F, -5.0752F, 1.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.3444F, -0.0032F, 0.0084F));
		PartDefinition cube_r45 = blade2.addOrReplaceChild("cube_r45",
				CubeListBuilder.create().texOffs(28, 63).addBox(-0.5F, -4.4159F, -5.7339F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(70, 3).addBox(-0.5F, -5.0999F, -3.8546F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 63)
						.addBox(-0.5F, -5.784F, -1.9752F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 60).addBox(-0.5F, -6.468F, -0.0958F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(20, 63)
						.addBox(-0.5F, -7.152F, 1.7836F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(52, 71).addBox(-0.5F, 3.0999F, 2.8546F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(16, 63)
						.addBox(-0.5F, 3.784F, 0.9752F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 50).addBox(-0.5F, 4.468F, -0.9042F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 71)
						.addBox(-0.5F, 5.152F, -2.7836F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 63).addBox(-0.5F, 2.4159F, 4.7339F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -0.0014F, 0.0038F, -1.9199F, 0.0F, 0.0F));
		PartDefinition cube_r46 = blade2.addOrReplaceChild("cube_r46", CubeListBuilder.create().texOffs(44, 50).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, 3.9248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r47 = blade2.addOrReplaceChild("cube_r47", CubeListBuilder.create().texOffs(44, 71).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, -4.0752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r48 = blade2.addOrReplaceChild("cube_r48", CubeListBuilder.create().texOffs(48, 47).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, -2.0752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r49 = blade2.addOrReplaceChild("cube_r49", CubeListBuilder.create().texOffs(8, 63).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, -0.0752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r50 = blade2.addOrReplaceChild("cube_r50", CubeListBuilder.create().texOffs(70, 36).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, 1.9248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r51 = blade2.addOrReplaceChild("cube_r51", CubeListBuilder.create().texOffs(4, 63).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, 4.4248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r52 = blade2.addOrReplaceChild("cube_r52", CubeListBuilder.create().texOffs(0, 63).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, 2.4248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r53 = blade2.addOrReplaceChild("cube_r53", CubeListBuilder.create().texOffs(60, 62).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, 0.4248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r54 = blade2.addOrReplaceChild("cube_r54", CubeListBuilder.create().texOffs(56, 62).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, -1.5752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r55 = blade2.addOrReplaceChild("cube_r55", CubeListBuilder.create().texOffs(52, 62).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, -3.5752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition blade3 = itemchipper.addOrReplaceChild("blade3", CubeListBuilder.create().texOffs(22, 0).addBox(-0.5F, -4.9713F, -5.0752F, 1.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.3444F, -0.0032F, 0.0084F));
		PartDefinition cube_r56 = blade3.addOrReplaceChild("cube_r56",
				CubeListBuilder.create().texOffs(44, 65).addBox(-0.5F, -4.4159F, -5.7339F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(70, 6).addBox(-0.5F, -5.0999F, -3.8546F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(64, 62)
						.addBox(-0.5F, -5.784F, -1.9752F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 60).addBox(-0.5F, -6.468F, -0.0958F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(64, 59)
						.addBox(-0.5F, -7.152F, 1.7836F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(68, 71).addBox(-0.5F, 3.0999F, 2.8546F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(64, 56)
						.addBox(-0.5F, 3.784F, 0.9752F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(52, 50).addBox(-0.5F, 4.468F, -0.9042F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(64, 71)
						.addBox(-0.5F, 5.152F, -2.7836F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(64, 53).addBox(-0.5F, 2.4159F, 4.7339F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -0.0014F, 0.0038F, -1.9199F, 0.0F, 0.0F));
		PartDefinition cube_r57 = blade3.addOrReplaceChild("cube_r57", CubeListBuilder.create().texOffs(52, 47).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, 3.9248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r58 = blade3.addOrReplaceChild("cube_r58", CubeListBuilder.create().texOffs(60, 71).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, -4.0752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r59 = blade3.addOrReplaceChild("cube_r59", CubeListBuilder.create().texOffs(52, 44).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, -2.0752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r60 = blade3.addOrReplaceChild("cube_r60", CubeListBuilder.create().texOffs(64, 50).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, -0.0752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r61 = blade3.addOrReplaceChild("cube_r61", CubeListBuilder.create().texOffs(56, 71).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, 1.9248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r62 = blade3.addOrReplaceChild("cube_r62", CubeListBuilder.create().texOffs(64, 47).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, 4.4248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r63 = blade3.addOrReplaceChild("cube_r63", CubeListBuilder.create().texOffs(64, 44).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, 2.4248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r64 = blade3.addOrReplaceChild("cube_r64", CubeListBuilder.create().texOffs(40, 63).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, 0.4248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r65 = blade3.addOrReplaceChild("cube_r65", CubeListBuilder.create().texOffs(36, 63).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, -1.5752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r66 = blade3.addOrReplaceChild("cube_r66", CubeListBuilder.create().texOffs(32, 63).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, -3.5752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition blade5 = itemchipper.addOrReplaceChild("blade5", CubeListBuilder.create().texOffs(0, 40).addBox(-0.5F, -4.9713F, -5.0752F, 1.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(6.6556F, -0.0032F, 0.0084F));
		PartDefinition cube_r67 = blade5.addOrReplaceChild("cube_r67",
				CubeListBuilder.create().texOffs(66, 27).addBox(-0.5F, -4.4159F, -5.7339F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(70, 12).addBox(-0.5F, -5.0999F, -3.8546F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(66, 24)
						.addBox(-0.5F, -5.784F, -1.9752F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(16, 60).addBox(-0.5F, -6.468F, -0.0958F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 66)
						.addBox(-0.5F, -7.152F, 1.7836F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(28, 72).addBox(-0.5F, 3.0999F, 2.8546F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(66, 21)
						.addBox(-0.5F, 3.784F, 0.9752F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(56, 47).addBox(-0.5F, 4.468F, -0.9042F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 72)
						.addBox(-0.5F, 5.152F, -2.7836F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(20, 66).addBox(-0.5F, 2.4159F, 4.7339F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -0.0014F, 0.0038F, -1.9199F, 0.0F, 0.0F));
		PartDefinition cube_r68 = blade5.addOrReplaceChild("cube_r68", CubeListBuilder.create().texOffs(56, 44).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, 3.9248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r69 = blade5.addOrReplaceChild("cube_r69", CubeListBuilder.create().texOffs(20, 72).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, -4.0752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r70 = blade5.addOrReplaceChild("cube_r70", CubeListBuilder.create().texOffs(44, 56).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, -2.0752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r71 = blade5.addOrReplaceChild("cube_r71", CubeListBuilder.create().texOffs(66, 18).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, -0.0752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r72 = blade5.addOrReplaceChild("cube_r72", CubeListBuilder.create().texOffs(16, 72).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, 1.9248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r73 = blade5.addOrReplaceChild("cube_r73", CubeListBuilder.create().texOffs(16, 66).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, 4.4248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r74 = blade5.addOrReplaceChild("cube_r74", CubeListBuilder.create().texOffs(66, 15).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, 2.4248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r75 = blade5.addOrReplaceChild("cube_r75", CubeListBuilder.create().texOffs(66, 12).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, 0.4248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r76 = blade5.addOrReplaceChild("cube_r76", CubeListBuilder.create().texOffs(12, 66).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, -1.5752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r77 = blade5.addOrReplaceChild("cube_r77", CubeListBuilder.create().texOffs(66, 9).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, -3.5752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition blade8 = itemchipper.addOrReplaceChild("blade8", CubeListBuilder.create().texOffs(44, 20).addBox(-0.5F, -4.9713F, -5.0752F, 1.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(2.6556F, -0.0032F, 0.0084F));
		PartDefinition cube_r78 = blade8.addOrReplaceChild("cube_r78",
				CubeListBuilder.create().texOffs(40, 69).addBox(-0.5F, -4.4159F, -5.7339F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(70, 21).addBox(-0.5F, -5.0999F, -3.8546F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(36, 69)
						.addBox(-0.5F, -5.784F, -1.9752F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(28, 60).addBox(-0.5F, -6.468F, -0.0958F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(32, 69)
						.addBox(-0.5F, -7.152F, 1.7836F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(72, 68).addBox(-0.5F, 3.0999F, 2.8546F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(28, 69)
						.addBox(-0.5F, 3.784F, 0.9752F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(56, 59).addBox(-0.5F, 4.468F, -0.9042F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(72, 65)
						.addBox(-0.5F, 5.152F, -2.7836F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 69).addBox(-0.5F, 2.4159F, 4.7339F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -0.0014F, 0.0038F, -1.9199F, 0.0F, 0.0F));
		PartDefinition cube_r79 = blade8.addOrReplaceChild("cube_r79", CubeListBuilder.create().texOffs(52, 59).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, 3.9248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r80 = blade8.addOrReplaceChild("cube_r80", CubeListBuilder.create().texOffs(72, 62).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, -4.0752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r81 = blade8.addOrReplaceChild("cube_r81", CubeListBuilder.create().texOffs(48, 59).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, -2.0752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r82 = blade8.addOrReplaceChild("cube_r82", CubeListBuilder.create().texOffs(20, 69).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, -0.0752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r83 = blade8.addOrReplaceChild("cube_r83", CubeListBuilder.create().texOffs(72, 59).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5287F, 1.9248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r84 = blade8.addOrReplaceChild("cube_r84", CubeListBuilder.create().texOffs(16, 69).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, 4.4248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r85 = blade8.addOrReplaceChild("cube_r85", CubeListBuilder.create().texOffs(12, 69).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, 2.4248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r86 = blade8.addOrReplaceChild("cube_r86", CubeListBuilder.create().texOffs(8, 69).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, 0.4248F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r87 = blade8.addOrReplaceChild("cube_r87", CubeListBuilder.create().texOffs(4, 69).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, -1.5752F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r88 = blade8.addOrReplaceChild("cube_r88", CubeListBuilder.create().texOffs(0, 69).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -5.4713F, -3.5752F, -0.3491F, 0.0F, 0.0F));
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