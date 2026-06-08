package net.mcreator.minigames.client.model;

import net.minecraft.util.Mth;
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
public class Modelgravedigger extends EntityModel<LivingEntityRenderState> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("minigames", "modelgravedigger"), "main");
	public final ModelPart head;
	public final ModelPart nose;
	public final ModelPart hat;
	public final ModelPart body;
	public final ModelPart RightLeg;
	public final ModelPart LeftLeg;
	public final ModelPart RightArm;
	public final ModelPart Shovel;
	public final ModelPart Lantern;
	public final ModelPart LeftArm;

	public Modelgravedigger(ModelPart root) {
		super(root);
		this.head = root.getChild("head");
		this.nose = this.head.getChild("nose");
		this.hat = this.head.getChild("hat");
		this.body = root.getChild("body");
		this.RightLeg = root.getChild("RightLeg");
		this.LeftLeg = root.getChild("LeftLeg");
		this.RightArm = root.getChild("RightArm");
		this.Shovel = this.RightArm.getChild("Shovel");
		this.Lantern = this.Shovel.getChild("Lantern");
		this.LeftArm = root.getChild("LeftArm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 4).addBox(-4.0F, -6.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition nose = head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));
		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition head_r1 = hat.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(28, 19).addBox(-4.0F, -6.0F, -4.0F, 9.0F, 6.0F, 9.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.5F, -4.1F, -0.4F, 0.1309F, 0.0F, 0.0F));
		PartDefinition head_r2 = hat.addOrReplaceChild("head_r2", CubeListBuilder.create().texOffs(31, -2).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.2F, 0.0F, 0.1309F, 0.0F, 0.0F));
		PartDefinition body = partdefinition.addOrReplaceChild("body",
				CubeListBuilder.create().texOffs(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, new CubeDeformation(0.5F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 12.0F, 0.0F));
		PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, 12.0F, 0.0F));
		PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create(), PartPose.offset(-5.0F, 2.0F, 0.0F));
		PartDefinition RightArm_r1 = RightArm.addOrReplaceChild("RightArm_r1", CubeListBuilder.create().texOffs(40, 46).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-1.0F, 2.0F, -4.2F, -1.3526F, 0.0F, 0.0F));
		PartDefinition Shovel = RightArm.addOrReplaceChild("Shovel", CubeListBuilder.create().texOffs(13, 30).addBox(-14.1538F, -0.4923F, -0.3553F, 22.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(28, 30)
				.addBox(9.8462F, -1.4923F, -0.3553F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(28, 30).addBox(7.8462F, -1.4923F, -0.3553F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(7.1538F, 3.4923F, -10.3947F));
		PartDefinition cube_r1 = Shovel.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(38, 30).addBox(-2.25F, 1.3907F, 1.9712F, 6.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-14.4038F, -0.0927F, -0.4408F, -0.8988F, 0.0F, 0.0F));
		PartDefinition cube_r2 = Shovel.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(38, 30).addBox(-2.25F, -2.6633F, 1.4969F, 6.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-14.4038F, -0.0927F, -0.4408F, 0.672F, 0.0F, 0.0F));
		PartDefinition cube_r3 = Shovel.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(38, 30).addBox(-3.25F, -1.9667F, 1.0672F, 7.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-14.4038F, -0.0927F, -0.4408F, 0.4363F, 0.0F, 0.0F));
		PartDefinition cube_r4 = Shovel.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(37, 30).addBox(-4.25F, -1.2368F, 0.8353F, 8.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-14.4038F, -0.0927F, -0.4408F, 0.2182F, 0.0F, 0.0F));
		PartDefinition cube_r5 = Shovel.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(38, 30).addBox(-3.75F, -2.1298F, 1.1289F, 7.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-14.4038F, -0.0927F, -0.4408F, 0.4276F, 0.0F, 3.1416F));
		PartDefinition cube_r6 = Shovel.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(37, 30).addBox(-3.75F, -1.3605F, 0.8494F, 8.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-14.4038F, -0.0927F, -0.4408F, 0.2094F, 0.0F, 3.1416F));
		PartDefinition cube_r7 = Shovel.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(36, 30).addBox(-3.75F, -0.5473F, 0.7517F, 9.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-14.4038F, -0.0927F, -0.4408F, 0.0F, 0.0F, 3.1416F));
		PartDefinition cube_r8 = Shovel.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(28, 30).addBox(-1.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(9.3462F, 2.0077F, 0.1447F, 0.0F, 0.0F, 1.5708F));
		PartDefinition cube_r9 = Shovel.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(28, 30).addBox(-1.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(9.3462F, 0.0077F, 0.1447F, 0.0F, 0.0F, 1.5708F));
		PartDefinition Lantern = Shovel.addOrReplaceChild("Lantern", CubeListBuilder.create().texOffs(29, 43).addBox(-1.5F, 2.8F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(9.3462F, 0.0077F, 0.1447F));
		PartDefinition cube_r10 = Lantern.addOrReplaceChild("cube_r10",
				CubeListBuilder.create().texOffs(27, 30).addBox(-0.5F, 1.5F, -2.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 30).addBox(-0.5F, 1.5F, -6.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 3.3F, 4.0F, 0.0F, 0.0F, 1.5708F));
		PartDefinition cube_r11 = Lantern.addOrReplaceChild("cube_r11",
				CubeListBuilder.create().texOffs(27, 30).addBox(-0.5F, 1.5F, -2.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(27, 30).addBox(-0.5F, 1.5F, -6.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(4.0F, 3.3F, 4.0F, 0.0F, 0.0F, 1.5708F));
		PartDefinition cube_r12 = Lantern.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(26, 26).addBox(2.5F, -2.5F, -2.5F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.3F, 0.0F, 0.0F, 0.0F, 1.5708F));
		PartDefinition cube_r13 = Lantern.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(26, 26).addBox(2.5F, -2.5F, -2.5F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -0.7F, 0.0F, 0.0F, 0.0F, 1.5708F));
		PartDefinition cube_r14 = Lantern.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(26, 28).addBox(0.5F, -0.5F, -1.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 1.5708F));
		PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create(), PartPose.offset(5.0F, 2.0F, 0.0F));
		PartDefinition LeftArm_r1 = LeftArm.addOrReplaceChild("LeftArm_r1", CubeListBuilder.create().texOffs(40, 46).mirror().addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(1.0F, 2.0F, -4.6F, -1.3526F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	public void setupAnim(LivingEntityRenderState state) {
		float limbSwing = state.walkAnimationPos;
		float limbSwingAmount = state.walkAnimationSpeed;
		float ageInTicks = state.ageInTicks;
		float netHeadYaw = state.yRot;
		float headPitch = state.xRot;

		this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.head.xRot = headPitch / (180F / (float) Math.PI);
		this.LeftLeg.xRot = Mth.cos(limbSwing * 1.0F) * -1.0F * limbSwingAmount;
		this.RightLeg.xRot = Mth.cos(limbSwing * 1.0F) * 1.0F * limbSwingAmount;
	}
}