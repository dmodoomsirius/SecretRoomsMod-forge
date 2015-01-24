package secretrooms.common.block;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.common.registry.GameRegistry;
import secretrooms.common.Helper;
import secretrooms.common.SecretRooms;
import secretrooms.common.tile.TECamo;

import java.util.List;

/**
 * @author TheTemportalist
 */
public class BlockCamo extends BlockContainer {

	private final String unlocalName;

	public BlockCamo(Material materialIn, String name) {
		super(materialIn);
		this.unlocalName = SecretRooms.MODID + ":" + name;
		this.setUnlocalizedName(this.getName());
		GameRegistry.registerBlock(this, name);
		SecretRooms.camouflaged.add(this);
	}

	public String getName() {
		return this.unlocalName;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TECamo();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state,
			EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		TECamo tile = (TECamo) worldIn.getTileEntity(pos);

		ItemStack handStack = playerIn.getCurrentEquippedItem();
		if (handStack == null) {
			return tile.setCamo(side, null);
		}
		else if (Block.getBlockFromItem(handStack.getItem()) != null) {
			IBlockState camoState = Block.getBlockFromItem(handStack.getItem()).
					getStateFromMeta(handStack.getMetadata());
			boolean setCamo = tile.setCamo(side, camoState);
			worldIn.markBlockForUpdate(pos);
			worldIn.scheduleUpdate(pos, state.getBlock(), 10);
			return setCamo;
		}
		return false;
	}

	@Override
	protected BlockState createBlockState() {
		return new ExtendedBlockState(this, new IProperty[0],
				new IUnlistedProperty[] { SecretRooms.CAMO });
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TECamo tile = (TECamo) world.getTileEntity(pos);
		String names = "{}";
		if (tile != null)
			names = tile.getCamo_Names();
		return ((IExtendedBlockState) state).withProperty(SecretRooms.CAMO, names);
	}

	@Override
	public int getRenderType() {
		return 3;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	public JsonObject getCamo(IBlockState state) {
		try {
			return new JsonParser().parse(((IExtendedBlockState) state).
					getValue(SecretRooms.CAMO)).getAsJsonObject();
		} catch (Exception e) {
			return new JsonObject();
		}
	}

	public IBlockState getGlobalState(IBlockState thisState) {
		JsonObject camo = this.getCamo(thisState);
		if (camo.has("g")) {
			return Helper.getStateFromName(camo.get("g").getAsString());
		}
		return null;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		IBlockState gState = this.getGlobalState(state);
		if (gState != null) {
			return gState.getBlock().getCollisionBoundingBox(worldIn, pos, gState);
		}
		return super.getCollisionBoundingBox(worldIn, pos, state);
	}

	@Override
	public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state,
			AxisAlignedBB mask, List list, Entity collidingEntity) {
		IBlockState gState = this.getGlobalState(state);
		if (gState != null) {
			gState.getBlock().addCollisionBoxesToList(worldIn, pos, gState, mask, list,
					collidingEntity);
		}
		else
			super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
		IBlockState gState = this.getGlobalState(worldIn.getBlockState(pos));
		if (gState != null) {
			gState.getBlock().setBlockBoundsBasedOnState(worldIn, pos);
		}
		else
			super.setBlockBoundsBasedOnState(worldIn, pos);
	}

}
