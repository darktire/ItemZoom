package mezz.itemzoom;

import com.mojang.logging.LogUtils;
import mezz.itemzoom.client.ItemZoomClient;
import mezz.itemzoom.client.config.ConfigHelper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkConstants;
import org.slf4j.Logger;

import javax.annotation.Nullable;

import static mezz.itemzoom.client.ItemZoomClient.FACTORY;

@Mod(ItemZoom.MOD_ID)
public class ItemZoom {
	public static final String MOD_NAME = "Item Zoom";
	public static final String MOD_ID = "itemzoom";
	public static final Logger MOD_LOG = LogUtils.getLogger();
	public static final Minecraft MINECRAFT = Minecraft.getInstance();
	@Nullable
	public static ConfigHelper config;
	public ItemZoom() {
		// Make sure the mod being absent on the other network side does not cause the client to
		// display the server as incompatible
		ModLoadingContext.get().registerExtensionPoint(
			IExtensionPoint.DisplayTest.class,
			() -> new IExtensionPoint.DisplayTest(
				() -> NetworkConstants.IGNORESERVERONLY,
				(a, b) -> true
			)
		);
		// Link Options Screen to Config button
		ModLoadingContext.get().registerExtensionPoint(FACTORY.getClass(), () -> FACTORY);

		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ItemZoomClient::run);
	}
}
