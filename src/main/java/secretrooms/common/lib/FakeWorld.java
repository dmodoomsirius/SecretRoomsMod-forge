package secretrooms.common.lib;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.HashMap;

/**
 * @author TheTemportalist
 */
public class FakeWorld extends IBlockAccess {

	private final World worldIn;
	private final HashMap<BlockPos, BlockHolder> fakeMap;

	public FakeWorld(World worldIn) {
		this.worldIn = worldIn;
		this.fakeMap = new HashMap<BlockPos, BlockHolder>();
	}

	public void update(BlockPos pos, IBlockState state) {
		this.fakeMap.put(pos, new BlockHolder(state, (NBTTagCompound)null));
	}



}
