package mezz.itemzoom.client.key;

import com.mojang.blaze3d.platform.InputConstants;
import mezz.itemzoom.client.config.ConfigHelper;

public class InputHandler {
	private final ConfigHelper config;
	private boolean enableKeyHeld = false;

	public InputHandler(ConfigHelper config) {
		this.config = config;
	}

	public boolean handleInput(InputConstants.Key input) {
		KeyBindings keyBindings = KeyBindings.getInstance();
		if (keyBindings.toggle.isActiveAndMatches(input)) {
			config.toggleEnable();
			return true;
		} else if (keyBindings.zoomIn.isActiveAndMatches(input)) {
			config.increaseZoom();
			return true;
		} else if (keyBindings.zoomOut.isActiveAndMatches(input)) {
			config.decreaseZoom();
			return true;
		} else if (keyBindings.hold.isActiveAndMatches(input)) {
			enableKeyHeld = true;
			return true;
		}
		return false;
	}

	public boolean handleInputReleased(InputConstants.Key input) {
		KeyBindings keyBindings = KeyBindings.getInstance();
		if (keyBindings.hold.isActiveAndMatches(input)) {
			enableKeyHeld = false;
			return true;
		}
		return false;
	}

	public boolean isEnableKeyHeld() {
		return enableKeyHeld;
	}
}
