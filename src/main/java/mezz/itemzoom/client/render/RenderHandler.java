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
import net.minecraft.world.item.ArmorItem;
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
	private final ComponentFrame componentFrame;
	private final ComponentModel componentModel;
	private final ComponentItem componentItem;

	public RenderHandler(Config config, Supplier<Boolean> isEnableKeyHeld) {
		this.config = config;
		this.isEnableKeyHeld = isEnableKeyHeld;
		this.componentFrame = new ComponentFrame(config);
		this.componentModel = new ComponentModel(config);
		this.componentItem = new ComponentItem(config);
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
			Rect2i renderArea = componentFrame.getRenderingArea(containerScreen, x);
				// avoid rendering zoomed items in the same space as the item being hovered over
			if (!renderArea.contains(x, y)) {
				boolean draw;
				if (itemStack.getItem() instanceof ArmorItem) {
					draw = componentModel.finalDraw(guiGraphics, itemStack, renderArea, minecraft);
				} else {
					draw = componentItem.finalDraw(guiGraphics, itemStack, renderArea, minecraft);
				}
				if (draw) {
					renderedThisFrame = renderArea;
				}
			}
		}
	}

}
