package secretrooms.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import secretrooms.common.ProxyCommon;
import secretrooms.common.SecretRooms;
import secretrooms.common.block.BlockCamo;

@SideOnly(value = Side.CLIENT)
public class ProxyClient extends ProxyCommon {

	@Override
	public void init() {
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		for (final BlockCamo block : SecretRooms.camouflaged) {
			renderItem.getItemModelMesher().register(
					Item.getItemFromBlock(block),
					new ItemMeshDefinition() {
						@Override
						public ModelResourceLocation getModelLocation(ItemStack stack) {
							return new ModelResourceLocation(block.getName(), null);
						}
					}
			);
		}
	}

	@SubscribeEvent
	public void bakeEvent(ModelBakeEvent event) {
		for (BlockCamo block : SecretRooms.camouflaged) {
			FMLLog.info("Baking " + block.getLocalizedName() + " to camo");
			event.modelRegistry.putObject(
					new ModelResourceLocation(block.getName(), null), ModelCamouflage.instance
			);
		}
	}

}
