package mezz.itemzoom.client;

import com.mojang.blaze3d.platform.InputConstants;
import mezz.itemzoom.client.config.Options;
import mezz.itemzoom.client.gui.OptionsScreen;
import mezz.itemzoom.client.key.InputHandler;
import mezz.itemzoom.client.key.KeyBindings;
import mezz.itemzoom.client.config.ConfigHelper;
import mezz.itemzoom.client.render.RenderHandler;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static mezz.itemzoom.ItemZoom.MOD_ID;
import static mezz.itemzoom.ItemZoom.config;

public class ItemZoomClient {
	public static final ConfigScreenHandler.ConfigScreenFactory FACTORY = new ConfigScreenHandler.ConfigScreenFactory(
			(mc, screen) -> new OptionsScreen(screen)
	);
	public static void run() {
		config = new ConfigHelper();

		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(EventPriority.NORMAL, false, RegisterKeyMappingsEvent.class, KeyBindings::init);
		modEventBus.addListener(EventPriority.NORMAL, false, ModConfigEvent.Loading.class, event -> setup(config));
		modEventBus.addListener(EventPriority.NORMAL, false, ModConfigEvent.Loading.class, event -> {
			if (event.getConfig().getModId().equals(MOD_ID)) Options.initInstance();
		});
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, config.getConfigSpec());
	}

	private static void setup(ConfigHelper config) {
		InputHandler inputHandler = new InputHandler(config);
		RenderHandler renderHandler = new RenderHandler(config, inputHandler::isEnableKeyHeld);

		IEventBus eventBus = MinecraftForge.EVENT_BUS;
		setupInputHandler(inputHandler, eventBus);
		setupRenderHandler(renderHandler, eventBus);
	}

	private static void setupInputHandler(InputHandler inputHandler, IEventBus eventBus) {
		eventBus.addListener(EventPriority.LOW, false, ScreenEvent.KeyPressed.Post.class, (event) -> {
			InputConstants.Key input = InputConstants.getKey(event.getKeyCode(), event.getScanCode());
			if (inputHandler.handleInput(input)) {
				event.setCanceled(true);
			}
		});
		eventBus.addListener(EventPriority.LOW, false, ScreenEvent.KeyReleased.Post.class, (event) -> {
			InputConstants.Key input = InputConstants.getKey(event.getKeyCode(), event.getScanCode());
			if (inputHandler.handleInputReleased(input)) {
				event.setCanceled(true);
			}
		});
		eventBus.addListener(EventPriority.LOW, false, ScreenEvent.MouseButtonPressed.Pre.class, (event) -> {
			InputConstants.Key input = InputConstants.Type.MOUSE.getOrCreate(event.getButton());
			if (inputHandler.handleInput(input)) {
				event.setCanceled(true);
			}
		});
		eventBus.addListener(EventPriority.LOW, false, ScreenEvent.MouseButtonReleased.Pre.class, (event) -> {
			InputConstants.Key input = InputConstants.Type.MOUSE.getOrCreate(event.getButton());
			if (inputHandler.handleInputReleased(input)) {
				event.setCanceled(true);
			}
		});
	}

	private static void setupRenderHandler(RenderHandler renderHandler, IEventBus eventBus) {
		eventBus.addListener(EventPriority.NORMAL, false, ScreenEvent.Render.Post.class, (event) -> {
			renderHandler.onScreenDrawn();
		});
		eventBus.addListener(EventPriority.NORMAL, false, RenderTooltipEvent.Pre.class, (event) -> {
			renderHandler.onItemStackTooltip(event.getGraphics(), event.getItemStack(), event.getX(), event.getY());
		});
	}
}
