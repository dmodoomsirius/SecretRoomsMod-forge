package secretrooms.common.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import secretrooms.common.Helper;
import secretrooms.common.lib.BlockHolder;
import secretrooms.common.lib.FakeManager;

/**
 * @author TheTemportalist 1/24/15
 */
public class TEEmulator extends TileEntity {

	private IBlockState emulatedBlock;

	public TEEmulator() {
		this.emulatedBlock = null;
	}

	public String getEmulated() {
		return Helper.getNameFromState(this.getEmulatedBlock());
	}

	public IBlockState getEmulatedBlock() {
		return this.emulatedBlock;
	}

	public void emulate(IBlockState state) {
		if (this.emulatedBlock != null) {
			Block.spawnAsEntity(this.getWorld(), this.getPos(),
					new ItemStack(this.emulatedBlock.getBlock(), 1,
							this.emulatedBlock.getBlock().getMetaFromState(this.emulatedBlock))
			);
		}
		this.emulatedBlock = state;
		if (this.emulatedBlock == null)
			FakeManager.get().getFakeWorld(this.getWorld()).setState(pos, null);
		else
			FakeManager.get().getFakeWorld(this.getWorld()).setState(
					pos, new BlockHolder(this.emulatedBlock, (NBTTagCompound) null)
			);
	}

}
