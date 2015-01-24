package secretrooms.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import secretrooms.common.ProxyCommon;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import secretrooms.common.SecretRooms;

@SideOnly(value = Side.CLIENT)
public class ProxyClient extends ProxyCommon {

	private static final ModelResourceLocation modelLocation = new ModelResourceLocation(SecretRooms.MODID + ":", null);

	@Override
	public void init() {
		MinecraftForge.EVENT_BUS.register(this);
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		for (Block block : SecretRooms.camouflaged) {
			renderItem.getItemModelMesher().register(
					Item.getItemFromBlock(block),
					new ItemMeshDefinition() {
						@Override
						public ModelResourceLocation getModelLocation(ItemStack stack) {
							return ProxyClient.modelLocation;
						}
					}
			);
		}
	}

	@SubscribeEvent
	public void bakeEvent(ModelBakeEvent event) {
		for (Block block : SecretRooms.camouflaged) {
			event.modelRegistry.putObject(
					new ModelResourceLocation(block.getUnlocalizedName()), new ModelCamouflage()
			);
		}
	}

}
