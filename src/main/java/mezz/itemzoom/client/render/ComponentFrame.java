package mezz.itemzoom.client.render;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.renderer.Rect2i;

public class ComponentFrame extends ComponentBase{

    protected Rect2i getRenderingArea(AbstractContainerScreen<?> containerScreen, int mouseX) {
        Minecraft minecraft = containerScreen.getMinecraft();
        Window window = minecraft.getWindow();
        int guiRight = containerScreen.getGuiLeft() + containerScreen.getXSize();
        int spaceOnLeft = getSpaceOnLeft(containerScreen);
        int spaceOnRight = window.getGuiScaledWidth() - guiRight;

        final boolean renderLeft;
        if (mouseX < containerScreen.getGuiLeft()) {
            // mouse is to the left side of the gui, render on the right.
            renderLeft = false;
        } else if (mouseX > guiRight) {
            // mouse is to the right side of the gui, render on the left.
            renderLeft = true;
        } else {
            // mouse is over the gui somewhere, pick whichever size has more space,
            // but bias a bit toward picking the left
            renderLeft = (spaceOnLeft * 1.1) >= spaceOnRight;
        }

        int y = containerScreen.getGuiTop();
        int height = containerScreen.getYSize();
        if (renderLeft) {
            return new Rect2i(0, y, spaceOnLeft, height);
        } else {
            return new Rect2i(guiRight, y, spaceOnRight, height);
        }
    }

    protected int getSpaceOnLeft(AbstractContainerScreen<?> containerScreen) {
        if (containerScreen instanceof RecipeUpdateListener recipeListener) {
            RecipeBookComponent guiRecipeBook = recipeListener.getRecipeBookComponent();
            if (guiRecipeBook.isVisible()) {
                return guiRecipeBook.tabButtons.stream()
                        .findAny()
                        .map(AbstractWidget::getX)
                        .orElse((guiRecipeBook.width - 147) / 2 - guiRecipeBook.xOffset);
            }
        }
        return containerScreen.getGuiLeft();
    }
}
