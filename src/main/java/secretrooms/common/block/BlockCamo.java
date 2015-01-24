package secretrooms.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.common.registry.GameRegistry;
import secretrooms.common.SecretRooms;
import secretrooms.common.tile.TECamo;

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

	public String getName() {return this.unlocalName;}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TECamo();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state,
			EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		TECamo tile = (TECamo)worldIn.getTileEntity(pos);

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
		return new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[]{SecretRooms.CAMO});
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TECamo tile = (TECamo)world.getTileEntity(pos);
		String names = "{}";
		if (tile != null) names = tile.getCamo_Names();
		return ((IExtendedBlockState)state).withProperty(SecretRooms.CAMO, names);
	}

	@Override
	public int getRenderType() {
		return 3;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}


}
