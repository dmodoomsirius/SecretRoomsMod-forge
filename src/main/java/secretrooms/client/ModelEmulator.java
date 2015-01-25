package secretrooms.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.common.property.IExtendedBlockState;
import secretrooms.common.Helper;
import secretrooms.common.block.BlockEmulator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TheTemportalist 1/24/15
 */
public class ModelEmulator implements ISmartBlockModel, ISmartItemModel {

	private String emulatedBlock;

	private IBakedModel getModelForState(IBlockState state) {
		return Minecraft.getMinecraft().getBlockRendererDispatcher().
				getBlockModelShapes().getModelForState(state);
	}

	private IBakedModel getModelForBlock(String name) {
		IBlockState state = Helper.getStateFromName(name);
		if (state != null)
			return this.getModelForState(state);
		else
			return null;
	}

	private String getEmulatedBlock(IBlockState state) {
		if (state instanceof IExtendedBlockState)
			return ((IExtendedBlockState) state).getValue(BlockEmulator.EMULATE);
		return null;
	}

	@Override
	public IBakedModel handleBlockState(IBlockState state) {
		String emulatedBlock = this.getEmulatedBlock(state);
		if (emulatedBlock != null) {
			this.emulatedBlock = emulatedBlock;
			return this.getModelForBlock(emulatedBlock);
		}
		return this;
	}

	@Override
	public IBakedModel handleItemState(ItemStack stack) {
		String emulatedBlock = BlockEmulator.getEmulated(stack);
		if (emulatedBlock != null)
			return this.getModelForBlock(emulatedBlock);
		return this;
	}

	@Override
	public List getFaceQuads(EnumFacing direction) {
		return new ArrayList<IBakedModel>();
	}

	@Override
	public List getGeneralQuads() {
		return new ArrayList<IBakedModel>();
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
		String[] details = Helper.getDetails(this.emulatedBlock);
		if (details == null)
			return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(
				details[0] + ":blocks/" + details[1]
		);
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return ItemCameraTransforms.DEFAULT;
	}

}
