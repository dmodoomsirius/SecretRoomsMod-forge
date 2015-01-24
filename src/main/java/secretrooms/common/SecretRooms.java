package secretrooms.common;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TheTemportalist
 */
@Mod(modid = SecretRooms.MODID, name = "Secret Rooms", version = "@VERSION@", useMetadata = true,
		acceptableRemoteVersions = "@CHANGE_VERSION@",
		acceptedMinecraftVersions = "@MC_VERSION@",
		acceptableSaveVersions = "@CHANGE_VERSION@",
		dependencies = "after:malisisdoors"
)
public class SecretRooms {

	public static final String MODID = "secretroomsmod";
	public static final String clientProxy = "com.modwarriors.secretrooms.client.ProxyClient";
	public static final String serverProxy = "com.modwarriors.secretrooms.common.ProxyCommon";

	@SidedProxy(clientSide = SecretRooms.clientProxy, serverSide = SecretRooms.serverProxy)
	public static ProxyCommon proxy;

	@Mod.Instance(value = SecretRooms.MODID)
	public static SecretRooms instance;

	public static final List<Block> camouflaged = new ArrayList<Block>();

	public static final IUnlistedProperty<String> CAMO = new IUnlistedProperty<String>() {
		@Override
		public String getName() {
			return "String";
		}

		@Override
		public boolean isValid(String value) {
			return Helper.getItemStack(value) != null;
		}

		@Override
		public Class<String> getType() {
			return String.class;
		}

		@Override
		public String valueToString(String value) {
			return value;
		}
	};

	// creative tab
	public static CreativeTabs tab;

	public static class SCBlocks {

		public static void registerTiles() {

		}

		public static void register() {
			SCBlocks.registerTiles();

		}

		public static void recipes() {

		}

	}

	public static class SCItems {

		public static void register() {

		}

		public static void registerPostBlock() {

		}

		public static void recipes() {

		}

	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(SecretRooms.instance);

		SecretRooms.tab = new CreativeTabCamo();

		SecretRooms.SCItems.register();
		SecretRooms.SCBlocks.register();
		SecretRooms.SCItems.registerPostBlock();
		SecretRooms.SCItems.recipes();
		SecretRooms.SCBlocks.recipes();

	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		SecretRooms.proxy.init();

	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}

}
