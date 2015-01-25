package secretrooms.common.lib;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author TheTemportalist
 */
public class BlockHolder {

	public IBlockState state;
	private final NBTTagCompound tagCompound;

	public BlockHolder(IBlockAccess world, BlockPos pos) {
		this(world.getBlockState(pos), world.getTileEntity(pos));
	}

	public BlockHolder(IBlockState state, NBTTagCompound tagCompound) {
		this.tagCompound = tagCompound;
		this.state = state;
	}

	public BlockHolder(IBlockState state, TileEntity tile) {
		this.state = state;
		if (tile != null) {
			this.tagCompound = new NBTTagCompound();
			this.tagCompound.setInteger("x", tile.getPos().getX());
			this.tagCompound.setInteger("y", tile.getPos().getY());
			this.tagCompound.setInteger("z", tile.getPos().getZ());
			tile.writeToNBT(this.tagCompound);

		}
		else
			this.tagCompound = null;
	}

	public TileEntity getTile(World world, BlockPos pos) {
		if (this.state == null || this.tagCompound == null) return null;
		TileEntity tile = TileEntity.createAndLoadEntity(this.tagCompound);
		tile.setWorldObj(world);
		tile.setPos(pos);
		return tile;
	}

}
