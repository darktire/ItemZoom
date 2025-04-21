package mezz.itemzoom.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class ComponentFont extends ComponentFrame {

    protected static Font getFont(Minecraft minecraft, ItemStack itemStack, IClientItemExtensions.FontContext context) {
        IClientItemExtensions renderProperties = IClientItemExtensions.of(itemStack);
        Font fontRenderer = renderProperties.getFont(itemStack, context);
        if (fontRenderer == null) {
            fontRenderer = minecraft.font;
        }
        return fontRenderer;
    }
}
