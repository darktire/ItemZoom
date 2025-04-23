package mezz.itemzoom.client.gui;

import mezz.itemzoom.client.config.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.SimpleOptionsSubScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import static mezz.itemzoom.ItemZoom.MINECRAFT;
import static mezz.itemzoom.ItemZoom.config;

public class OptionsScreen extends SimpleOptionsSubScreen {
    public OptionsScreen(Screen preScreen) {
        super(preScreen, MINECRAFT.options, Component.translatable("config.itemzoom.title"), Options.INSTANCE.OPTIONS);
    }

    @Override
    public void removed() {
        config.save();
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void createFooter() {
        this.addRenderableWidget(Button.builder(
                Component.translatable("config.itemzoom.open.file"),
                (button) -> config.openAdvancedFile(button)
            ).bounds(this.width / 2 - 155, this.height - 27, 150, 20).build());

        this.addRenderableWidget(Button.builder(
                CommonComponents.GUI_DONE,
                (button) -> MINECRAFT.setScreen(this.lastScreen)
            ).bounds(this.width / 2 + 5, this.height - 27, 150, 20).build());}
}
