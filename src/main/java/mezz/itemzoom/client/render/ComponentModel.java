package mezz.itemzoom.client.render;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.itemzoom.client.config.ConfigHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class ComponentModel extends ComponentBase {
    private static final float ROTATE_COEFFICIENT = 0.4f;
    private static float rotateValue = 0;
    private static final int ENTITY_SIZE = 10;
    protected ComponentModel(ConfigHelper config){
        super(config);
    }

    @Override
    protected void drawGraphics(GuiGraphics guiGraphics, ItemStack itemStack, float xPosition, float yPosition, float scale) {
        rotateValue += ROTATE_COEFFICIENT;
        if (rotateValue % 360 == 0) {
            rotateValue = 0;
        }
        ArmorItem armorItem = (ArmorItem) itemStack.getItem();
        ArmorStand entity = new ArmorStand(EntityType.ARMOR_STAND, Minecraft.getInstance().level);
        entity.setItemSlot(armorItem.getEquipmentSlot(), itemStack);
        float size = ENTITY_SIZE * scale;
        float offset = 3 * scale;
        drawEntity(guiGraphics, xPosition + size / 2 + offset, yPosition + size + offset + 20, size, rotateValue, -45, entity);
    }

    @Override
    protected void renderOverlay(GuiGraphics guiGraphics, ItemStack itemStack) {}

    public static void resetRotation() {
        rotateValue = 0;
    }

    private void drawEntity(GuiGraphics guiGraphics, float x, float y, float size, float mouseX, float mouseY, LivingEntity entity) {
        float f = (float) Math.atan(mouseX / 40.0F);
        float g = (float) Math.atan(mouseY / 40.0F);
        Quaternionf quaternionf = (new Quaternionf()).rotateZ(3.1415927F);
        Quaternionf quaternionf2 = (new Quaternionf()).rotateX(g * 20.0F * 0.017453292F);
        quaternionf.mul(quaternionf2);
        entity.yBodyRot = mouseX;
        entity.setYRot(180.0F + f * 40.0F);
        entity.setXRot(-g * 20.0F);
        entity.yHeadRot = entity.getYRot();
        entity.yHeadRotO = entity.getYRot();
        drawEntity(guiGraphics, x, y, size, quaternionf, quaternionf2, entity);
    }

    private void drawEntity(GuiGraphics guiGraphics, float x, float y, float size, Quaternionf quaternionf, @Nullable Quaternionf quaternionf2, Entity entity) {

        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();

        poseStack.translate(x, y, 450);
        poseStack.mulPoseMatrix((new Matrix4f()).scaling(size, size, -size));
        poseStack.mulPose(quaternionf);
        Lighting.setupForEntityInInventory();

        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        if (quaternionf2 != null) {
            quaternionf2.conjugate();
            entityRenderDispatcher.overrideCameraOrientation(quaternionf2);
        }

        entityRenderDispatcher.setRenderShadow(false);
//        RenderSystem.runAsFancy(() -> {
        entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, poseStack, guiGraphics.bufferSource(), 15728880);
//        });
        guiGraphics.flush();
        entityRenderDispatcher.setRenderShadow(true);

        poseStack.popPose();
        Lighting.setupFor3DItems();
    }

    @Override
    protected void drawText(GuiGraphics guiGraphics, ItemStack itemStack, int availableAreaX, int availableAreaY, int availableAreaWidth, int availableAreaHeight, float scale, Minecraft minecraft) {
        super.drawText(guiGraphics, itemStack, availableAreaX, availableAreaY + 16, availableAreaWidth, availableAreaHeight, scale, minecraft);
    }
}
