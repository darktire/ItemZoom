package mezz.itemzoom.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import mezz.itemzoom.ItemZoom;
import mezz.itemzoom.client.KeyBindings;
import mezz.itemzoom.client.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public abstract class ComponentBase {
    protected final Config config;

    protected ComponentBase(Config config) {
        this.config = config;
    }

    protected boolean finalDraw(GuiGraphics guiGraphics, ItemStack itemStack, Rect2i availableArea, Minecraft minecraft) {
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

        drawGraphics(guiGraphics, itemStack, xPosition, yPosition, scale);

        RenderSystem.applyModelViewMatrix();

        drawText(guiGraphics, itemStack, availableAreaX, availableAreaY, availableAreaWidth, availableAreaHeight, scale, minecraft);

        return true;
    }

    protected void drawText(GuiGraphics guiGraphics, ItemStack itemStack, int availableAreaX, int availableAreaY, int availableAreaWidth, int availableAreaHeight, float scale, Minecraft minecraft) {
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
    }

    protected abstract void drawGraphics(GuiGraphics guiGraphics, ItemStack itemStack, float xPosition, float yPosition, float scale);

    protected abstract void renderOverlay(GuiGraphics guiGraphics, ItemStack itemStack);

    protected static Font getFont(Minecraft minecraft, ItemStack itemStack, IClientItemExtensions.FontContext context) {
        IClientItemExtensions renderProperties = IClientItemExtensions.of(itemStack);
        Font fontRenderer = renderProperties.getFont(itemStack, context);
        if (fontRenderer == null) {
            fontRenderer = minecraft.font;
        }
        return fontRenderer;
    }
}
