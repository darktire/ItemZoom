package mezz.itemzoom.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import mezz.itemzoom.ItemZoom;
import mezz.itemzoom.client.KeyBindings;
import mezz.itemzoom.client.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class ComponentItem extends ComponentFont {
    private final Config config;
    protected ComponentItem(Config config){
        this.config = config;
    }

    protected boolean renderZoomedStack(GuiGraphics guiGraphics, ItemStack itemStack, Rect2i availableArea, Minecraft minecraft) {
        final int availableAreaX = availableArea.getX();
        final int availableAreaY = availableArea.getY();
        final int availableAreaWidth = availableArea.getWidth();
        final int availableAreaHeight = availableArea.getHeight();

        // item is 16 wide, give it some extra space on each side by using 17 here
        final float scale = config.getZoomAmount() / 100f * availableAreaWidth / 17f;
        if (scale <= 2.0f) {
            // not enough room to be useful
            return false;
        }

        final float renderWidth = scale * 16;
        final float renderHeight = scale * 16;
        final float xPosition = availableAreaX + ((availableAreaWidth - renderWidth) / 2f);
        final float yPosition = availableAreaY + ((availableAreaHeight - renderHeight) / 2f);

        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        {
            poseStack.translate(xPosition, yPosition, 0);
            poseStack.scale(scale, scale, 1);

            guiGraphics.renderItem(itemStack, 0, 0);
            RenderSystem.setShader(GameRenderer::getPositionColorShader);

            renderItemOverlayIntoGUI(guiGraphics, itemStack);
        }
        poseStack.popPose();

        RenderSystem.applyModelViewMatrix();

        if (config.showHelpText()) {
            int y = availableAreaY + ((availableAreaHeight + Math.round(19 * scale)) / 2);

            String modName = ItemZoom.MOD_NAME;
            Font nameFont = getFont(minecraft, itemStack, IClientItemExtensions.FontContext.SELECTED_ITEM_NAME);

            int stringWidth = nameFont.width(modName);
            if (stringWidth < availableAreaWidth) {
                int x = availableAreaX + ((availableAreaWidth - stringWidth) / 2);
                guiGraphics.drawString(nameFont, modName, x, y, 4210752, false);

                y += nameFont.lineHeight;
            }

            if (config.isToggledEnabled()) {
                KeyBindings keyBindings = KeyBindings.getInstance();
                Component displayName = keyBindings.toggle.getTranslatedKeyMessage();
                String toggleText = displayName.getString();
                Font minecraftFont = minecraft.font;
                stringWidth = minecraftFont.width(toggleText);
                if (stringWidth < availableAreaWidth) {
                    int x = availableAreaX + ((availableAreaWidth - stringWidth) / 2);
                    guiGraphics.drawString(minecraftFont, toggleText, x, y, 4210752, false);
                }
            }
        }
        return true;
    }

    protected void renderItemOverlayIntoGUI(GuiGraphics guiGraphics, ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();

        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        {
            if (config.showStackSize() && itemStack.getCount() != 1) {
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

            if (config.showDurabilityBar() && itemStack.isBarVisible()) {
                RenderSystem.disableDepthTest();
                int k = itemStack.getBarWidth();
                int l = itemStack.getBarColor();
                guiGraphics.fill(2, 13, 15, 15, -0xFFFFFF);
                guiGraphics.fill(2, 13, 2 + k, 14, l | -0xFFFFFF);
                RenderSystem.enableDepthTest();
            }

            if (config.showCooldown()) {
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
