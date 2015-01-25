package secretrooms.common.lib;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;

import java.util.HashMap;

/**
 * @author TheTemportalist
 */
public class FakeWorld implements IBlockAccess {

	private final World worldIn;
	private final HashMap<BlockPos, BlockHolder> fakeMap;

	public FakeWorld(World worldIn) {
		this.worldIn = worldIn;
		this.fakeMap = new HashMap<BlockPos, BlockHolder>();
	}

	public void update(BlockPos pos, IBlockState state) {
		this.fakeMap.put(pos, new BlockHolder(state, (NBTTagCompound) null));
	}

	public void setState(IBlockAccess realWorld, BlockPos pos, IBlockState state) {
		this.setState(pos, state, realWorld.getTileEntity(pos));
	}

	public void setState(BlockPos pos, IBlockState state, TileEntity tile) {
		this.setState(pos, new BlockHolder(state, tile));
	}

	public void setState(BlockPos pos, BlockHolder holder) {
		if (holder == null || holder.state == null)
			this.fakeMap.remove(pos);
		else
			this.fakeMap.put(pos, holder);
	}

	public void removeState(BlockPos pos) {
		this.fakeMap.remove(pos);
	}

	private IBlockAccess getAccessAtPos(BlockPos pos) {
		if (this.fakeMap.containsKey(pos))
			return this;
		else
			return this.worldIn;
	}

	@Override
	public boolean extendedLevelsInChunkCache() {
		return this.worldIn.extendedLevelsInChunkCache();
	}

	@Override
	public TileEntity getTileEntity(BlockPos pos) {
		if (this.fakeMap.containsKey(pos))
			// todo so procedurally create tile in the real world?
			return this.fakeMap.get(pos).getTile(this.worldIn, pos);
		else
			return this.worldIn.getTileEntity(pos);
	}

	@Override //todo
	public int getCombinedLight(BlockPos pos, int p_175626_2_) {
		return 0;
	}

	@Override
	public IBlockState getBlockState(BlockPos pos) {
		if (this.fakeMap.containsKey(pos))
			return this.fakeMap.get(pos).state;
		else
			return this.worldIn.getBlockState(pos);
	}

	@Override
	public boolean isAirBlock(BlockPos pos) {
		if (this.fakeMap.containsKey(pos))
			return this.fakeMap.get(pos).state.getBlock().isAir(this, pos);
		else
			return this.worldIn.getBlockState(pos).getBlock().isAir(this.worldIn, pos);
	}

	@Override
	public BiomeGenBase getBiomeGenForCoords(BlockPos pos) {
		return this.worldIn.getBiomeGenForCoords(pos);
	}

	@Override
	public int getStrongPower(BlockPos pos, EnumFacing direction) {
		if (this.fakeMap.containsKey(pos)) {
			IBlockState state = this.fakeMap.get(pos).state;
			return state.getBlock().isProvidingStrongPower(this, pos, state, direction);
		}
		else
			return this.worldIn.getStrongPower(pos, direction);
	}

	@Override
	public WorldType getWorldType() {
		return this.worldIn.getWorldType();
	}

	@Override
	public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
		if (this.fakeMap.containsKey(pos))
			return this.fakeMap.get(pos).state.getBlock().isSideSolid(this, pos, side);
		else
			return this.worldIn.isSideSolid(pos, side, _default);
	}

}
