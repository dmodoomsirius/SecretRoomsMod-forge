package secretrooms.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.common.property.IExtendedBlockState;
import secretrooms.common.Helper;
import secretrooms.common.SecretRooms;

import java.util.Collections;
import java.util.List;

/**
 * @author TheTemportalist
 */
public class ModelCamouflage implements ISmartBlockModel, ISmartItemModel {

	public ModelCamouflage() {}

	private IBakedModel getModelForState(IBlockState state) {
		return Minecraft.getMinecraft().getBlockRendererDispatcher().
				getBlockModelShapes().getModelForState(state);
	}

	private IBakedModel getModelForBlock(String name) {
		IBlockState state = Helper.getStateFromName(name);
		if (state != null)
			return this.getModelForState(state);
		else return null;
	}

	@Override
	public IBakedModel handleBlockState(IBlockState state) {
		if (state instanceof IExtendedBlockState) {
			return this.getModelForBlock(
					((IExtendedBlockState)state).getValue(SecretRooms.CAMO_PROP)
			);
		}
		return null;
	}

	@Override
	public IBakedModel handleItemState(ItemStack stack) {
		if (stack.hasTagCompound()) {
			return this.getModelForBlock(Helper.getCamoString(stack));
		}
		return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack);
	}

	@Override
	public List getFaceQuads(EnumFacing facing) {
		return (List<BakedQuad>)Collections.EMPTY_LIST;
	}

	@Override
	public List getGeneralQuads() {
		return (List<BakedQuad>)Collections.EMPTY_LIST;
	}

	@Override
	public boolean isAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getTexture() {
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/slime");
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return ItemCameraTransforms.DEFAULT;
	}
}
