package mezz.itemzoom.client.config;

import mezz.itemzoom.client.compat.JeiCompat;
import net.minecraft.Util;
import net.minecraft.client.gui.components.Button;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;

public class ConfigHelper extends Config{
    private static final File ADVANCED_SETTINGS = FMLPaths.CONFIGDIR.get().resolve("itemzoom-client.toml").toFile();

    public ConfigHelper() {
        super();
    }

    public void openAdvancedFile(Button button) {
        Util.getPlatform().openFile(ADVANCED_SETTINGS);
    }

    public boolean getToggleEnable() {
        return super.TOGGLE_ENABLE.get();
    }
    public void setToggleEnable(Boolean b) {
        super.TOGGLE_ENABLE.set(b);
    }
    public void toggleEnable() {
        super.TOGGLE_ENABLE.set(!super.TOGGLE_ENABLE.get());
        super.TOGGLE_ENABLE.save();
    }

    public int getZoomAmount() {
        return super.ZOOM_AMOUNT.get();
    }
    public void setZoomAmount(int newZoomAmount) {
        if (newZoomAmount > MAX_ZOOM) {
            newZoomAmount = MAX_ZOOM;
        } else if (newZoomAmount < MIN_ZOOM) {
            newZoomAmount = MIN_ZOOM;
        }

        int oldZoomAmount = super.ZOOM_AMOUNT.get();
        if (oldZoomAmount != newZoomAmount) {
            super.ZOOM_AMOUNT.set(newZoomAmount);
            super.ZOOM_AMOUNT.save();
        }
    }
    public void increaseZoom() {
        int newZoomAmount = Math.round(getZoomAmount() * 1.1f);
        setZoomAmount(newZoomAmount);
    }
    public void decreaseZoom() {
        int newZoomAmount = Math.round(getZoomAmount() / 1.1f);
        setZoomAmount(newZoomAmount);
    }

    public boolean getShowHelp() {
        return super.SHOW_HELP.get();
    }
    public void setShowHelp(Boolean b) {
        super.SHOW_HELP.set(b);
    }

    public boolean getShowDurability() {
        return super.SHOW_DURABILITY.get();
    }
    public void setShowDurability(Boolean b) {
        super.SHOW_DURABILITY.set(b);
    }

    public boolean getShowStackSize() {
        return super.SHOW_STACK_SIZE.get();
    }
    public void setShowStackSize(Boolean b) {
        super.SHOW_STACK_SIZE.set(b);
    }

    public boolean getShowCooldown() {
        return super.SHOW_COOLDOWN.get();
    }
    public void setShowCooldown(Boolean b) {
        super.SHOW_COOLDOWN.set(b);
    }

    public boolean isJeiOnly() {
        return super.JEI_ONLY.get() && JeiCompat.isLoaded();
    }

    public ForgeConfigSpec getConfigSpec() {
        return super.CONFIG_SPEC;
    }
    public void save() {
        super.CONFIG_SPEC.save();
    }
}
