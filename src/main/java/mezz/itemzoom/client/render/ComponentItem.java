package mezz.itemzoom.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import mezz.itemzoom.client.config.ConfigHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class ComponentItem extends ComponentBase {

    protected ComponentItem(ConfigHelper config) {
        super(config);
    }

    @Override
    protected void drawGraphics(GuiGraphics guiGraphics, ItemStack itemStack, float xPosition, float yPosition, float scale) {

        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();

        poseStack.translate(xPosition, yPosition, 0);
        poseStack.scale(scale, scale, 1);

        guiGraphics.renderItem(itemStack, 0, 0);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        renderOverlay(guiGraphics, itemStack);

        poseStack.popPose();
    }

    protected void renderOverlay(GuiGraphics guiGraphics, ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();

        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        {
            if (config.getShowStackSize() && itemStack.getCount() != 1) {
                String countString = String.valueOf(itemStack.getCount());
                Font itemCountFont = getFont(minecraft, itemStack, IClientItemExtensions.FontContext.ITEM_COUNT);

                poseStack.translate(0.0F, 0.0F, 200.0F);
                Tesselator tesselator = Tesselator.getInstance();
                MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(tesselator.getBuilder());
                itemCountFont.drawInBatch(
                        countString,
                        17.0F - itemCountFont.width(countString),
                        9.0F,
                        0xFFFFFF,
                        true,
                        poseStack.last().pose(),
                        bufferSource,
                        Font.DisplayMode.NORMAL,
                        0,
                        0xF000F0
                );
                bufferSource.endBatch();
            }

            if (config.getShowDurability() && itemStack.isBarVisible()) {
                RenderSystem.disableDepthTest();
                int k = itemStack.getBarWidth();
                int l = itemStack.getBarColor();
                guiGraphics.fill(2, 13, 15, 15, -0xFFFFFF);
                guiGraphics.fill(2, 13, 2 + k, 14, l | -0xFFFFFF);
                RenderSystem.enableDepthTest();
            }

            if (config.getShowCooldown()) {
                LocalPlayer localplayer = minecraft.player;
                if (localplayer != null) {
                    ItemCooldowns cooldowns = localplayer.getCooldowns();
                    float cooldownPercent = cooldowns.getCooldownPercent(itemStack.getItem(), minecraft.getFrameTime());
                    if (cooldownPercent > 0.0F) {
                        RenderSystem.disableDepthTest();
                        int i1 = Mth.floor(16.0F * (1.0F - cooldownPercent));
                        int j1 = i1 + Mth.ceil(16.0F * cooldownPercent);
                        guiGraphics.fill(0, i1, 16, j1, Integer.MAX_VALUE);
                        RenderSystem.enableDepthTest();
                    }
                }
            }
        }
        poseStack.popPose();
    }

}
