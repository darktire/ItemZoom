package mezz.itemzoom.client.render;

import javax.annotation.Nullable;
import java.util.function.Supplier;

import mezz.itemzoom.client.compat.JeiCompat;
import mezz.itemzoom.client.config.Config;
import net.minecraft.client.Minecraft;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class RenderHandler {
	@Nullable
	public static Rect2i rendering = null;
	@Nullable
	private static Rect2i renderedThisFrame = null;
	private final Config config;
	private final Supplier<Boolean> isEnableKeyHeld;
	private final ComponentItem itemRenderComponent;

	public RenderHandler(Config config, Supplier<Boolean> isEnableKeyHeld) {
		this.config = config;
		this.isEnableKeyHeld = isEnableKeyHeld;
		this.itemRenderComponent = new ComponentItem(config);
	}

	public void onScreenDrawn() {
		rendering = renderedThisFrame;
		renderedThisFrame = null;
	}

	public void onItemStackTooltip(GuiGraphics guiGraphics, @Nullable ItemStack itemStack, int x, int y) {
		if (!config.isToggledEnabled() && !isEnableKeyHeld.get()) {
			return;
		}
		if (itemStack == null || itemStack.isEmpty()) {
			return;
		}
		if (config.isJeiOnly() && !ItemStack.isSameItem(itemStack, JeiCompat.getStackUnderMouse())) {
			return;
		}

		Minecraft minecraft = Minecraft.getInstance();
		Screen currentScreen = minecraft.screen;
		if (currentScreen instanceof AbstractContainerScreen<?> containerScreen) {
			Rect2i renderArea = itemRenderComponent.getRenderingArea(containerScreen, x);
			// avoid rendering zoomed items in the same space as the item being hovered over
			if (!renderArea.contains(x, y)) {
				if (itemRenderComponent.renderZoomedStack(guiGraphics, itemStack, renderArea, minecraft)) {
					renderedThisFrame = renderArea;
				}
			}
		}
	}

}
