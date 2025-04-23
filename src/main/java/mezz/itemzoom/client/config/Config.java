package mezz.itemzoom.client.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class Config {
	public static final int MIN_ZOOM = 10;
	public static final int MAX_ZOOM = 100;
	private static final int DEFAULT_ZOOM = 80;

	protected final ConfigValue<Boolean> TOGGLE_ENABLE;
	protected final ConfigValue<Integer> ZOOM_AMOUNT;
	protected final ConfigValue<Boolean> JEI_ONLY;
	protected final ConfigValue<Boolean> SHOW_HELP;
	protected final ConfigValue<Boolean> SHOW_DURABILITY;
	protected final ConfigValue<Boolean> SHOW_STACK_SIZE;
	protected final ConfigValue<Boolean> SHOW_COOLDOWN;

	protected final ForgeConfigSpec CONFIG_SPEC;

	public Config() {
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.push("itemzoom");

		TOGGLE_ENABLE = builder
				.comment("If set to \"false\", Item Zoom will be disabled.")
				.translation("config.itemzoom.toggle.enabled")
				.define("toggled.enabled", true);

		ZOOM_AMOUNT = builder
				.comment("Set lower amount to make the item finalDraw less.")
				.translation("config.itemzoom.finalDraw.amount")
				.defineInRange("zoom_amount", DEFAULT_ZOOM, MIN_ZOOM, MAX_ZOOM, Integer.class);

		JEI_ONLY = builder
				.comment("Zoom items only from the JEI ingredient and bookmark list overlays.")
				.translation("config.itemzoom.jei.only")
				.define("jei_only", false);

		SHOW_HELP = builder
				.comment("Display name \"Item Zoom\" and the hotkey to toggle this mod below the zoomed item.")
				.translation("config.itemzoom.show.help.text")
				.define("show.help_text", true);

		SHOW_DURABILITY = builder
				.comment("Display the item's durability bar when zoomed.")
				.translation("config.itemzoom.show.damage.bar")
				.define("show.damage_bar", false);

		SHOW_STACK_SIZE = builder
				.comment("Display the item's stack size when zoomed.")
				.translation("config.itemzoom.show.stack.size")
				.define("show.stack_size", false);

		SHOW_COOLDOWN = builder
				.comment("Display the item's cooldown animation when zoomed.")
				.translation("config.itemzoom.show.cooldown")
				.define("show.cooldown", false);

		CONFIG_SPEC = builder.build();
	}
}
