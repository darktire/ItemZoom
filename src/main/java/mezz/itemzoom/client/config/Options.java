package mezz.itemzoom.client.config;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

import static mezz.itemzoom.ItemZoom.config;

@OnlyIn(Dist.CLIENT)
public class Options {

    @Nullable
    public static Options INSTANCE;
    public final OptionInstance<?>[] OPTIONS;


    private Options() {
        OptionInstance<Boolean> toggleEnable = OptionInstance.createBoolean(
                "config.itemzoom.toggle.enabled",
                cachedConstantTooltip("config.itemzoom.toggle.enabled.comment"),
                config.getToggleEnable(),
                config::setToggleEnable
        );
        OptionInstance<Integer> zoomAmount = new OptionInstance<>(
                "config.itemzoom.zoom.amount",
                cachedConstantTooltip("config.itemzoom.zoom.amount.comment"),
                Options::genericValueLabel,
                new OptionInstance.IntRange(Config.MIN_ZOOM, Config.MAX_ZOOM),
                config.getZoomAmount(),
                config::setZoomAmount
        );
        OptionInstance<Boolean> showHelp = OptionInstance.createBoolean(
                "config.itemzoom.show.help.text",
                cachedConstantTooltip("config.itemzoom.show.help.text.comment"),
                config.getShowHelp(),
                config::setShowHelp
        );
        OptionInstance<Boolean> showDurability = OptionInstance.createBoolean(
                "config.itemzoom.show.damage.bar",
                cachedConstantTooltip("config.itemzoom.show.damage.bar.comment"),
                config.getShowDurability(),
                config::setShowDurability
        );
        OptionInstance<Boolean> showStakeSize = OptionInstance.createBoolean(
                "config.itemzoom.show.stack.size",
                cachedConstantTooltip("config.itemzoom.show.stack.size.comment"),
                config.getShowStackSize(),
                config::setShowStackSize
        );
        OptionInstance<Boolean> showCooldown = OptionInstance.createBoolean(
                "config.itemzoom.show.cooldown",
                cachedConstantTooltip("config.itemzoom.show.cooldown.comment"),
                config.getShowCooldown(),
                config::setShowCooldown
        );

        OPTIONS = new OptionInstance[]{toggleEnable, zoomAmount, showHelp, showDurability, showStakeSize, showCooldown};
    }

    public static void initInstance() {
        if (INSTANCE == null)
            INSTANCE = new Options();
    }

    private static <T> OptionInstance.TooltipSupplier<T> cachedConstantTooltip(String s) {
        Component component = Component.translatable(s);
        return (p_258116_) -> Tooltip.create(component);
    }

    private static Component genericValueLabel(Component component, int value) {
        return Component.translatable("options.generic_value", component, value);
    }

    private static Component percentValueLabel(Component component, int value) {
        return Component.translatable("options.percent_value", component, value);
    }
}
