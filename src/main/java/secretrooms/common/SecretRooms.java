package secretrooms.common;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import secretrooms.common.block.BlockCamo;
import secretrooms.common.lib.FakeManager;
import secretrooms.common.tile.TECamo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

	public static final String MODID = "secretrooms";
	public static final String clientProxy = "secretrooms.client.ProxyClient";
	public static final String serverProxy = "secretrooms.common.ProxyCommon";

	@SidedProxy(clientSide = SecretRooms.clientProxy, serverSide = SecretRooms.serverProxy)
	public static ProxyCommon proxy;

	@Mod.Instance(value = SecretRooms.MODID)
	public static SecretRooms instance;

	public static final List<BlockCamo> camouflaged = new ArrayList<BlockCamo>();

	public static final IUnlistedProperty<String> CAMO = new IUnlistedProperty<String>() {
		@Override
		public String getName() {
			return "String";
		}

		@Override
		public boolean isValid(String value) {
			JsonObject json = new JsonParser().parse(value).getAsJsonObject();
			for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
				if (Helper.getItemStack(entry.getValue().getAsString()) == null)
					return false;
			}
			return true;
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

		public static Block basic;

		public static void registerTiles() {
			GameRegistry.registerTileEntity(TECamo.class, "camouflage");

		}

		public static void register() {
			SCBlocks.registerTiles();

			SCBlocks.basic = new BlockCamo(Material.rock, "basic");
			SCBlocks.basic.setCreativeTab(SecretRooms.tab);

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
		MinecraftForge.EVENT_BUS.register(SecretRooms.proxy);
		FakeManager.instance = new FakeManager();
		MinecraftForge.EVENT_BUS.register(FakeManager.instance);
		FMLCommonHandler.instance().bus().register(FakeManager.instance);

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
