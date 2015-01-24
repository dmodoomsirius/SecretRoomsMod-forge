package secretrooms.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.common.property.IExtendedBlockState;
import secretrooms.common.Helper;
import secretrooms.common.SecretRooms;
import secretrooms.common.block.BlockCamo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TheTemportalist
 */
public class ModelCamouflage implements ISmartBlockModel, ISmartItemModel {

	public static final ModelCamouflage instance = new ModelCamouflage(null);

	private final JsonObject states;

	public ModelCamouflage(JsonObject states) {
		this.states = states;
	}

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

	@Override
	public IBakedModel handleBlockState(IBlockState state) {
		if (state instanceof IExtendedBlockState) {
			JsonObject states = new JsonParser().parse(
					((IExtendedBlockState) state).getValue(SecretRooms.CAMO)
			).getAsJsonObject();
			if (states.has("g")) {
				return this.getModelForBlock(states.get("g").getAsString());
			}
			else {
				return new ModelCamouflage(states);
			}
		}
		return this.getModelForState(state);
	}

	@Override
	public IBakedModel handleItemState(ItemStack stack) {
		if (stack.hasTagCompound()) {
			JsonObject states = new JsonParser().parse(
					stack.getTagCompound().getString("camoJson")
			).getAsJsonObject();
			if (states.has("g")) {
				return this.getModelForBlock(states.get("g").getAsString());
			}
			else {
				return new ModelCamouflage(states);
			}
		}
		/*
		if (stack.hasTagCompound()) {
			return this.getModelForBlock(Helper.getCamoString(stack));
		}
		*/
		BlockCamo block = (BlockCamo) Block.getBlockFromItem(stack.getItem());
		if (block != null) {
			return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().
					getModelManager().getModel(
					new ModelResourceLocation(block.getName(), "inventory")
			);
		}
		return null;
	}

	@Override
	public List getFaceQuads(EnumFacing facing) {
		if (this.states != null && !this.states.has("g"))
			if (this.states.has(facing.getIndex() + "")) {
				String stateName = this.states.get(facing.getIndex() + "").getAsString();
				return this.getModelForBlock(stateName).getFaceQuads(facing);
			}
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
		return Minecraft.getMinecraft().getTextureMapBlocks()
				.getAtlasSprite("minecraft:blocks/slime");
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return ItemCameraTransforms.DEFAULT;
	}

}
