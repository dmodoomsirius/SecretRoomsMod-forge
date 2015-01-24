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

	public BlockCamo(Material materialIn, String name) {
		super(materialIn);
		this.setUnlocalizedName(name);
		GameRegistry.registerBlock(this, name);
		SecretRooms.camouflaged.add(this);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TECamo();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state,
			EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		TECamo tile = (TECamo)worldIn.getTileEntity(pos);
		IBlockState camoState = null;

		if (playerIn.getCurrentEquippedItem() != null) {
			ItemStack stack = playerIn.getCurrentEquippedItem();
			if (Block.getBlockFromItem(stack.getItem()) != null) {
				camoState = Block.getBlockFromItem(stack.getItem()).
						getStateFromMeta(stack.getMetadata());
			}
		}

		if (playerIn.isSneaking()) {
			// all sides
			return tile.setCamo(null, camoState);
		}
		else {
			// this side
			return tile.setCamo(side, camoState);
		}

	}

	@Override
	protected BlockState createBlockState() {
		return new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[1]);
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return ((IExtendedBlockState)state).withProperty(
				SecretRooms.CAMO, ((TECamo)world.getTileEntity(pos)).getCamo_Names()
		);
	}

	@Override
	public String getUnlocalizedName() {
		return SecretRooms.MODID + ":" + super.getUnlocalizedName();
	}

	@Override
	public int getRenderType() {
		return 3;
	}

}
