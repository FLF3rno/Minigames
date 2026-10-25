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
public class Modelspike_trap extends EntityModel<LivingEntityRenderState> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath("minigames", "modelspike_trap"), "main");
	public final ModelPart body;
	public final ModelPart spike;
	public final ModelPart spike2;
	public final ModelPart spike3;
	public final ModelPart spike4;
	public final ModelPart spike5;

	public Modelspike_trap(ModelPart root) {
		super(root);
		this.body = root.getChild("body");
		this.spike = this.body.getChild("spike");
		this.spike2 = this.body.getChild("spike2");
		this.spike3 = this.body.getChild("spike3");
		this.spike4 = this.body.getChild("spike4");
		this.spike5 = this.body.getChild("spike5");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition spike = body.addOrReplaceChild("spike", CubeListBuilder.create().texOffs(-25, -17).addBox(-9.4F, -25.0F, -9.4F, 19.0F, 2.0F, 19.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
				.addBox(-1.0F, -68.0F, -1.0F, 2.0F, 43.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(-9, -6).addBox(-4.0F, -23.0F, -4.0F, 8.0F, 23.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition cube_r1 = spike.addOrReplaceChild("cube_r1",
				CubeListBuilder.create().texOffs(-13, -13).addBox(7.1778F, 7.5803F, -7.25F, 0.0F, 6.0F, 15.0F, new CubeDeformation(0.0F)).texOffs(-8, -8).addBox(7.1778F, -8.4197F, -5.25F, 0.0F, 22.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(-5, -5)
						.addBox(7.1778F, -16.4197F, -3.25F, 0.0F, 30.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(7.1778F, -29.4197F, -1.25F, 0.0F, 43.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(-3, -3)
						.addBox(7.1778F, -21.4197F, -2.25F, 0.0F, 35.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(-1, -1).addBox(7.1778F, -25.4197F, -1.25F, 0.0F, 39.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(-11, -11)
						.addBox(7.1778F, 0.5803F, -6.25F, 0.0F, 13.0F, 13.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.1675F, -37.0032F, -0.45F, 1.5708F, -1.3875F, -1.5708F));
		PartDefinition cube_r2 = spike.addOrReplaceChild("cube_r2",
				CubeListBuilder.create().texOffs(-11, -11).addBox(7.1778F, 0.5803F, -6.25F, 0.0F, 13.0F, 13.0F, new CubeDeformation(0.0F)).texOffs(-8, -8).addBox(7.1778F, -8.4197F, -5.25F, 0.0F, 22.0F, 10.0F, new CubeDeformation(0.0F))
						.texOffs(-5, -5).addBox(7.1778F, -16.4197F, -3.25F, 0.0F, 30.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(-3, -3).addBox(7.1778F, -21.4197F, -2.25F, 0.0F, 35.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(-1, -1)
						.addBox(7.1778F, -25.4197F, -1.25F, 0.0F, 39.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(7.1778F, -29.4197F, -1.25F, 0.0F, 43.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(-13, -13)
						.addBox(7.1778F, 7.5803F, -7.25F, 0.0F, 6.0F, 15.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.1675F, -37.0032F, 0.25F, -1.5708F, 1.3875F, -1.5708F));
		PartDefinition cube_r3 = spike.addOrReplaceChild("cube_r3",
				CubeListBuilder.create().texOffs(-13, -13).addBox(0.0F, 6.25F, -7.25F, 0.0F, 6.0F, 15.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(0.0F, -30.75F, -1.25F, 0.0F, 43.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(-1, -1)
						.addBox(0.0F, -26.75F, -1.25F, 0.0F, 39.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(-3, -3).addBox(0.0F, -22.75F, -2.25F, 0.0F, 35.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(-5, -5)
						.addBox(0.0F, -17.75F, -3.25F, 0.0F, 30.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(-8, -8).addBox(0.0F, -9.75F, -5.25F, 0.0F, 22.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(-11, -11)
						.addBox(0.0F, -0.75F, -6.25F, 0.0F, 13.0F, 13.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-7.1325F, -37.0032F, 0.25F, -3.1416F, 0.0F, -2.9583F));
		PartDefinition cube_r4 = spike.addOrReplaceChild("cube_r4",
				CubeListBuilder.create().texOffs(-13, -13).addBox(0.0F, 15.4F, -7.0F, 0.0F, 6.0F, 15.0F, new CubeDeformation(0.0F)).texOffs(-11, -11).addBox(0.0F, 8.4F, -6.0F, 0.0F, 13.0F, 13.0F, new CubeDeformation(0.0F)).texOffs(-8, -8)
						.addBox(0.0F, -0.6F, -5.0F, 0.0F, 22.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(-5, -5).addBox(0.0F, -8.6F, -3.0F, 0.0F, 30.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(-3, -3)
						.addBox(0.0F, -13.6F, -2.0F, 0.0F, 35.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(-1, -1).addBox(0.0F, -17.6F, -1.0F, 0.0F, 39.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
						.addBox(0.0F, -21.6F, -1.0F, 0.0F, 43.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(5.8F, -46.0F, 0.0F, 0.0F, 0.0F, -0.1833F));
		PartDefinition cube_r5 = spike.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 0).addBox(1.7721F, -21.4923F, 4.4091F, 2.0F, 46.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.1635F, -45.852F, 0.131F, 0.1883F, 0.3529F, -0.1174F));
		PartDefinition cube_r6 = spike.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 0).addBox(1.1721F, -21.4923F, 4.4091F, 2.0F, 46.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.8365F, -45.852F, 0.131F, -2.9533F, -0.3529F, -3.0242F));
		PartDefinition cube_r7 = spike.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -23.0F, -1.0F, 2.0F, 46.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(4.6635F, -45.852F, -3.869F, -2.6245F, 1.1996F, -2.8338F));
		PartDefinition cube_r8 = spike.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -24.5F, -1.0F, 2.0F, 46.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-4.6F, -44.4F, 4.4F, 0.5171F, -1.1996F, -0.3078F));
		PartDefinition spike2 = body.addOrReplaceChild("spike2", CubeListBuilder.create().texOffs(-25, -17).addBox(-9.4F, -25.0F, -9.4F, 19.0F, 2.0F, 19.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
				.addBox(-1.0F, -68.0F, -1.0F, 2.0F, 43.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(-9, -6).addBox(-4.0F, -23.0F, -4.0F, 8.0F, 23.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(25.0F, 0.0F, -21.0F));
		PartDefinition cube_r9 = spike2.addOrReplaceChild("cube_r9",
				CubeListBuilder.create().texOffs(-13, -13).addBox(7.1778F, 7.5803F, -7.25F, 0.0F, 6.0F, 15.0F, new CubeDeformation(0.0F)).texOffs(-8, -8).addBox(7.1778F, -8.4197F, -5.25F, 0.0F, 22.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(-5, -5)
						.addBox(7.1778F, -16.4197F, -3.25F, 0.0F, 30.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(7.1778F, -29.4197F, -1.25F, 0.0F, 43.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(-3, -3)
						.addBox(7.1778F, -21.4197F, -2.25F, 0.0F, 35.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(-1, -1).addBox(7.1778F, -25.4197F, -1.25F, 0.0F, 39.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(-11, -11)
						.addBox(7.1778F, 0.5803F, -6.25F, 0.0F, 13.0F, 13.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.1675F, -37.0032F, -0.45F, 1.5708F, -1.3875F, -1.5708F));
		PartDefinition cube_r10 = spike2.addOrReplaceChild("cube_r10",
				CubeListBuilder.create().texOffs(-11, -11).addBox(7.1778F, 0.5803F, -6.25F, 0.0F, 13.0F, 13.0F, new CubeDeformation(0.0F)).texOffs(-8, -8).addBox(7.1778F, -8.4197F, -5.25F, 0.0F, 22.0F, 10.0F, new CubeDeformation(0.0F))
						.texOffs(-5, -5).addBox(7.1778F, -16.4197F, -3.25F, 0.0F, 30.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(-3, -3).addBox(7.1778F, -21.4197F, -2.25F, 0.0F, 35.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(-1, -1)
						.addBox(7.1778F, -25.4197F, -1.25F, 0.0F, 39.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(7.1778F, -29.4197F, -1.25F, 0.0F, 43.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(-13, -13)
						.addBox(7.1778F, 7.5803F, -7.25F, 0.0F, 6.0F, 15.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.1675F, -37.0032F, 0.25F, -1.5708F, 1.3875F, -1.5708F));
		PartDefinition cube_r11 = spike2.addOrReplaceChild("cube_r11",
				CubeListBuilder.create().texOffs(-13, -13).addBox(0.0F, 6.25F, -7.25F, 0.0F, 6.0F, 15.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(0.0F, -30.75F, -1.25F, 0.0F, 43.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(-1, -1)
						.addBox(0.0F, -26.75F, -1.25F, 0.0F, 39.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(-3, -3).addBox(0.0F, -22.75F, -2.25F, 0.0F, 35.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(-5, -5)
						.addBox(0.0F, -17.75F, -3.25F, 0.0F, 30.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(-8, -8).addBox(0.0F, -9.75F, -5.25F, 0.0F, 22.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(-11, -11)
						.addBox(0.0F, -0.75F, -6.25F, 0.0F, 13.0F, 13.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-7.1325F, -37.0032F, 0.25F, -3.1416F, 0.0F, -2.9583F));
		PartDefinition cube_r12 = spike2.addOrReplaceChild("cube_r12",
				CubeListBuilder.create().texOffs(-13, -13).addBox(0.0F, 15.4F, -7.0F, 0.0F, 6.0F, 15.0F, new CubeDeformation(0.0F)).texOffs(-11, -11).addBox(0.0F, 8.4F, -6.0F, 0.0F, 13.0F, 13.0F, new CubeDeformation(0.0F)).texOffs(-8, -8)
						.addBox(0.0F, -0.6F, -5.0F, 0.0F, 22.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(-5, -5).addBox(0.0F, -8.6F, -3.0F, 0.0F, 30.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(-3, -3)
						.addBox(0.0F, -13.6F, -2.0F, 0.0F, 35.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(-1, -1).addBox(0.0F, -17.6F, -1.0F, 0.0F, 39.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
						.addBox(0.0F, -21.6F, -1.0F, 0.0F, 43.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(5.8F, -46.0F, 0.0F, 0.0F, 0.0F, -0.1833F));
		PartDefinition cube_r13 = spike2.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 0).addBox(1.7721F, -21.4923F, 4.4091F, 2.0F, 46.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.1635F, -45.852F, 0.131F, 0.1883F, 0.3529F, -0.1174F));
		PartDefinition cube_r14 = spike2.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(0, 0).addBox(1.1721F, -21.4923F, 4.4091F, 2.0F, 46.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.8365F, -45.852F, 0.131F, -2.9533F, -0.3529F, -3.0242F));
		PartDefinition cube_r15 = spike2.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -23.0F, -1.0F, 2.0F, 46.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(4.6635F, -45.852F, -3.869F, -2.6245F, 1.1996F, -2.8338F));
		PartDefinition cube_r16 = spike2.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -24.5F, -1.0F, 2.0F, 46.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-4.6F, -44.4F, 4.4F, 0.5171F, -1.1996F, -0.3078F));
		PartDefinition spike3 = body.addOrReplaceChild("spike3", CubeListBuilder.create().texOffs(-25, -17).addBox(-9.4F, -25.0F, -9.4F, 19.0F, 2.0F, 19.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
				.addBox(-1.0F, -68.0F, -1.0F, 2.0F, 43.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(-9, -6).addBox(-4.0F, -23.0F, -4.0F, 8.0F, 23.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-27.0F, 0.0F, -21.0F));
		PartDefinition cube_r17 = spike3.addOrReplaceChild("cube_r17",
				CubeListBuilder.create().texOffs(-13, -13).addBox(7.1778F, 7.5803F, -7.25F, 0.0F, 6.0F, 15.0F, new CubeDeformation(0.0F)).texOffs(-8, -8).addBox(7.1778F, -8.4197F, -5.25F, 0.0F, 22.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(-5, -5)
						.addBox(7.1778F, -16.4197F, -3.25F, 0.0F, 30.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(7.1778F, -29.4197F, -1.25F, 0.0F, 43.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(-3, -3)
						.addBox(7.1778F, -21.4197F, -2.25F, 0.0F, 35.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(-1, -1).addBox(7.1778F, -25.4197F, -1.25F, 0.0F, 39.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(-11, -11)
						.addBox(7.1778F, 0.5803F, -6.25F, 0.0F, 13.0F, 13.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.1675F, -37.0032F, -0.45F, 1.5708F, -1.3875F, -1.5708F));
		PartDefinition cube_r18 = spike3.addOrReplaceChild("cube_r18",
				CubeListBuilder.create().texOffs(-11, -11).addBox(7.1778F, 0.5803F, -6.25F, 0.0F, 13.0F, 13.0F, new CubeDeformation(0.0F)).texOffs(-8, -8).addBox(7.1778F, -8.4197F, -5.25F, 0.0F, 22.0F, 10.0F, new CubeDeformation(0.0F))
						.texOffs(-5, -5).addBox(7.1778F, -16.4197F, -3.25F, 0.0F, 30.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(-3, -3).addBox(7.1778F, -21.4197F, -2.25F, 0.0F, 35.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(-1, -1)
						.addBox(7.1778F, -25.4197F, -1.25F, 0.0F, 39.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(7.1778F, -29.4197F, -1.25F, 0.0F, 43.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(-13, -13)
						.addBox(7.1778F, 7.5803F, -7.25F, 0.0F, 6.0F, 15.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.1675F, -37.0032F, 0.25F, -1.5708F, 1.3875F, -1.5708F));
		PartDefinition cube_r19 = spike3.addOrReplaceChild("cube_r19",
				CubeListBuilder.create().texOffs(-13, -13).addBox(0.0F, 6.25F, -7.25F, 0.0F, 6.0F, 15.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(0.0F, -30.75F, -1.25F, 0.0F, 43.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(-1, -1)
						.addBox(0.0F, -26.75F, -1.25F, 0.0F, 39.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(-3, -3).addBox(0.0F, -22.75F, -2.25F, 0.0F, 35.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(-5, -5)
						.addBox(0.0F, -17.75F, -3.25F, 0.0F, 30.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(-8, -8).addBox(0.0F, -9.75F, -5.25F, 0.0F, 22.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(-11, -11)
						.addBox(0.0F, -0.75F, -6.25F, 0.0F, 13.0F, 13.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-7.1325F, -37.0032F, 0.25F, -3.1416F, 0.0F, -2.9583F));
		PartDefinition cube_r20 = spike3.addOrReplaceChild("cube_r20",
				CubeListBuilder.create().texOffs(-13, -13).addBox(0.0F, 15.4F, -7.0F, 0.0F, 6.0F, 15.0F, new CubeDeformation(0.0F)).texOffs(-11, -11).addBox(0.0F, 8.4F, -6.0F, 0.0F, 13.0F, 13.0F, new CubeDeformation(0.0F)).texOffs(-8, -8)
						.addBox(0.0F, -0.6F, -5.0F, 0.0F, 22.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(-5, -5).addBox(0.0F, -8.6F, -3.0F, 0.0F, 30.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(-3, -3)
						.addBox(0.0F, -13.6F, -2.0F, 0.0F, 35.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(-1, -1).addBox(0.0F, -17.6F, -1.0F, 0.0F, 39.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
						.addBox(0.0F, -21.6F, -1.0F, 0.0F, 43.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(5.8F, -46.0F, 0.0F, 0.0F, 0.0F, -0.1833F));
		PartDefinition cube_r21 = spike3.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(0, 0).addBox(1.7721F, -21.4923F, 4.4091F, 2.0F, 46.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.1635F, -45.852F, 0.131F, 0.1883F, 0.3529F, -0.1174F));
		PartDefinition cube_r22 = spike3.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(0, 0).addBox(1.1721F, -21.4923F, 4.4091F, 2.0F, 46.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.8365F, -45.852F, 0.131F, -2.9533F, -0.3529F, -3.0242F));
		PartDefinition cube_r23 = spike3.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -23.0F, -1.0F, 2.0F, 46.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(4.6635F, -45.852F, -3.869F, -2.6245F, 1.1996F, -2.8338F));
		PartDefinition cube_r24 = spike3.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -24.5F, -1.0F, 2.0F, 46.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-4.6F, -44.4F, 4.4F, 0.5171F, -1.1996F, -0.3078F));
		PartDefinition spike4 = body.addOrReplaceChild("spike4", CubeListBuilder.create().texOffs(-25, -17).addBox(-9.4F, -25.0F, -9.4F, 19.0F, 2.0F, 19.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
				.addBox(-1.0F, -68.0F, -1.0F, 2.0F, 43.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(-9, -6).addBox(-4.0F, -23.0F, -4.0F, 8.0F, 23.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-27.0F, 0.0F, 23.0F));
		PartDefinition cube_r25 = spike4.addOrReplaceChild("cube_r25",
				CubeListBuilder.create().texOffs(-13, -13).addBox(7.1778F, 7.5803F, -7.25F, 0.0F, 6.0F, 15.0F, new CubeDeformation(0.0F)).texOffs(-8, -8).addBox(7.1778F, -8.4197F, -5.25F, 0.0F, 22.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(-5, -5)
						.addBox(7.1778F, -16.4197F, -3.25F, 0.0F, 30.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(7.1778F, -29.4197F, -1.25F, 0.0F, 43.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(-3, -3)
						.addBox(7.1778F, -21.4197F, -2.25F, 0.0F, 35.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(-1, -1).addBox(7.1778F, -25.4197F, -1.25F, 0.0F, 39.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(-11, -11)
						.addBox(7.1778F, 0.5803F, -6.25F, 0.0F, 13.0F, 13.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.1675F, -37.0032F, -0.45F, 1.5708F, -1.3875F, -1.5708F));
		PartDefinition cube_r26 = spike4.addOrReplaceChild("cube_r26",
				CubeListBuilder.create().texOffs(-11, -11).addBox(7.1778F, 0.5803F, -6.25F, 0.0F, 13.0F, 13.0F, new CubeDeformation(0.0F)).texOffs(-8, -8).addBox(7.1778F, -8.4197F, -5.25F, 0.0F, 22.0F, 10.0F, new CubeDeformation(0.0F))
						.texOffs(-5, -5).addBox(7.1778F, -16.4197F, -3.25F, 0.0F, 30.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(-3, -3).addBox(7.1778F, -21.4197F, -2.25F, 0.0F, 35.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(-1, -1)
						.addBox(7.1778F, -25.4197F, -1.25F, 0.0F, 39.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(7.1778F, -29.4197F, -1.25F, 0.0F, 43.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(-13, -13)
						.addBox(7.1778F, 7.5803F, -7.25F, 0.0F, 6.0F, 15.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.1675F, -37.0032F, 0.25F, -1.5708F, 1.3875F, -1.5708F));
		PartDefinition cube_r27 = spike4.addOrReplaceChild("cube_r27",
				CubeListBuilder.create().texOffs(-13, -13).addBox(0.0F, 6.25F, -7.25F, 0.0F, 6.0F, 15.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(0.0F, -30.75F, -1.25F, 0.0F, 43.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(-1, -1)
						.addBox(0.0F, -26.75F, -1.25F, 0.0F, 39.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(-3, -3).addBox(0.0F, -22.75F, -2.25F, 0.0F, 35.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(-5, -5)
						.addBox(0.0F, -17.75F, -3.25F, 0.0F, 30.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(-8, -8).addBox(0.0F, -9.75F, -5.25F, 0.0F, 22.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(-11, -11)
						.addBox(0.0F, -0.75F, -6.25F, 0.0F, 13.0F, 13.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-7.1325F, -37.0032F, 0.25F, -3.1416F, 0.0F, -2.9583F));
		PartDefinition cube_r28 = spike4.addOrReplaceChild("cube_r28",
				CubeListBuilder.create().texOffs(-13, -13).addBox(0.0F, 15.4F, -7.0F, 0.0F, 6.0F, 15.0F, new CubeDeformation(0.0F)).texOffs(-11, -11).addBox(0.0F, 8.4F, -6.0F, 0.0F, 13.0F, 13.0F, new CubeDeformation(0.0F)).texOffs(-8, -8)
						.addBox(0.0F, -0.6F, -5.0F, 0.0F, 22.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(-5, -5).addBox(0.0F, -8.6F, -3.0F, 0.0F, 30.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(-3, -3)
						.addBox(0.0F, -13.6F, -2.0F, 0.0F, 35.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(-1, -1).addBox(0.0F, -17.6F, -1.0F, 0.0F, 39.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
						.addBox(0.0F, -21.6F, -1.0F, 0.0F, 43.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(5.8F, -46.0F, 0.0F, 0.0F, 0.0F, -0.1833F));
		PartDefinition cube_r29 = spike4.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(0, 0).addBox(1.7721F, -21.4923F, 4.4091F, 2.0F, 46.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.1635F, -45.852F, 0.131F, 0.1883F, 0.3529F, -0.1174F));
		PartDefinition cube_r30 = spike4.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(0, 0).addBox(1.1721F, -21.4923F, 4.4091F, 2.0F, 46.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.8365F, -45.852F, 0.131F, -2.9533F, -0.3529F, -3.0242F));
		PartDefinition cube_r31 = spike4.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -23.0F, -1.0F, 2.0F, 46.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(4.6635F, -45.852F, -3.869F, -2.6245F, 1.1996F, -2.8338F));
		PartDefinition cube_r32 = spike4.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -24.5F, -1.0F, 2.0F, 46.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-4.6F, -44.4F, 4.4F, 0.5171F, -1.1996F, -0.3078F));
		PartDefinition spike5 = body.addOrReplaceChild("spike5", CubeListBuilder.create().texOffs(-25, -17).addBox(-9.4F, -25.0F, -9.4F, 19.0F, 2.0F, 19.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
				.addBox(-1.0F, -68.0F, -1.0F, 2.0F, 43.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(-9, -6).addBox(-4.0F, -23.0F, -4.0F, 8.0F, 23.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(25.0F, 0.0F, 23.0F));
		PartDefinition cube_r33 = spike5.addOrReplaceChild("cube_r33",
				CubeListBuilder.create().texOffs(-13, -13).addBox(7.1778F, 7.5803F, -7.25F, 0.0F, 6.0F, 15.0F, new CubeDeformation(0.0F)).texOffs(-8, -8).addBox(7.1778F, -8.4197F, -5.25F, 0.0F, 22.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(-5, -5)
						.addBox(7.1778F, -16.4197F, -3.25F, 0.0F, 30.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(7.1778F, -29.4197F, -1.25F, 0.0F, 43.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(-3, -3)
						.addBox(7.1778F, -21.4197F, -2.25F, 0.0F, 35.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(-1, -1).addBox(7.1778F, -25.4197F, -1.25F, 0.0F, 39.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(-11, -11)
						.addBox(7.1778F, 0.5803F, -6.25F, 0.0F, 13.0F, 13.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.1675F, -37.0032F, -0.45F, 1.5708F, -1.3875F, -1.5708F));
		PartDefinition cube_r34 = spike5.addOrReplaceChild("cube_r34",
				CubeListBuilder.create().texOffs(-11, -11).addBox(7.1778F, 0.5803F, -6.25F, 0.0F, 13.0F, 13.0F, new CubeDeformation(0.0F)).texOffs(-8, -8).addBox(7.1778F, -8.4197F, -5.25F, 0.0F, 22.0F, 10.0F, new CubeDeformation(0.0F))
						.texOffs(-5, -5).addBox(7.1778F, -16.4197F, -3.25F, 0.0F, 30.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(-3, -3).addBox(7.1778F, -21.4197F, -2.25F, 0.0F, 35.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(-1, -1)
						.addBox(7.1778F, -25.4197F, -1.25F, 0.0F, 39.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(7.1778F, -29.4197F, -1.25F, 0.0F, 43.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(-13, -13)
						.addBox(7.1778F, 7.5803F, -7.25F, 0.0F, 6.0F, 15.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.1675F, -37.0032F, 0.25F, -1.5708F, 1.3875F, -1.5708F));
		PartDefinition cube_r35 = spike5.addOrReplaceChild("cube_r35",
				CubeListBuilder.create().texOffs(-13, -13).addBox(0.0F, 6.25F, -7.25F, 0.0F, 6.0F, 15.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(0.0F, -30.75F, -1.25F, 0.0F, 43.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(-1, -1)
						.addBox(0.0F, -26.75F, -1.25F, 0.0F, 39.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(-3, -3).addBox(0.0F, -22.75F, -2.25F, 0.0F, 35.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(-5, -5)
						.addBox(0.0F, -17.75F, -3.25F, 0.0F, 30.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(-8, -8).addBox(0.0F, -9.75F, -5.25F, 0.0F, 22.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(-11, -11)
						.addBox(0.0F, -0.75F, -6.25F, 0.0F, 13.0F, 13.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-7.1325F, -37.0032F, 0.25F, -3.1416F, 0.0F, -2.9583F));
		PartDefinition cube_r36 = spike5.addOrReplaceChild("cube_r36",
				CubeListBuilder.create().texOffs(-13, -13).addBox(0.0F, 15.4F, -7.0F, 0.0F, 6.0F, 15.0F, new CubeDeformation(0.0F)).texOffs(-11, -11).addBox(0.0F, 8.4F, -6.0F, 0.0F, 13.0F, 13.0F, new CubeDeformation(0.0F)).texOffs(-8, -8)
						.addBox(0.0F, -0.6F, -5.0F, 0.0F, 22.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(-5, -5).addBox(0.0F, -8.6F, -3.0F, 0.0F, 30.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(-3, -3)
						.addBox(0.0F, -13.6F, -2.0F, 0.0F, 35.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(-1, -1).addBox(0.0F, -17.6F, -1.0F, 0.0F, 39.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
						.addBox(0.0F, -21.6F, -1.0F, 0.0F, 43.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(5.8F, -46.0F, 0.0F, 0.0F, 0.0F, -0.1833F));
		PartDefinition cube_r37 = spike5.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(0, 0).addBox(1.7721F, -21.4923F, 4.4091F, 2.0F, 46.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.1635F, -45.852F, 0.131F, 0.1883F, 0.3529F, -0.1174F));
		PartDefinition cube_r38 = spike5.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(0, 0).addBox(1.1721F, -21.4923F, 4.4091F, 2.0F, 46.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.8365F, -45.852F, 0.131F, -2.9533F, -0.3529F, -3.0242F));
		PartDefinition cube_r39 = spike5.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -23.0F, -1.0F, 2.0F, 46.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(4.6635F, -45.852F, -3.869F, -2.6245F, 1.1996F, -2.8338F));
		PartDefinition cube_r40 = spike5.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -24.5F, -1.0F, 2.0F, 46.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-4.6F, -44.4F, 4.4F, 0.5171F, -1.1996F, -0.3078F));
		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	public void setupAnim(LivingEntityRenderState state) {
		float limbSwing = state.walkAnimationPos;
		float limbSwingAmount = state.walkAnimationSpeed;
		float ageInTicks = state.ageInTicks;
		float netHeadYaw = state.yRot;
		float headPitch = state.xRot;

	}
}