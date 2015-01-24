package secretrooms.common.tile;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author TheTemportalist
 */
public class TECamo extends TileEntity {

	private IBlockState globalState = null;
	private final IBlockState[] camoStates = new IBlockState[6];

	public TECamo() {
	}

	public boolean setCamo(EnumFacing side, IBlockState state) {
		if (side != null) {
			if (!state.getBlock().isVisuallyOpaque())
				return false;
			else
				this.camoStates[side.getIndex()] = state;
		}
		else
			this.globalState = state;
		return true;
	}

	public String getCamo_Names() {
		// todo move this to an origin helper class
		Gson g = new Gson();
		JsonObject json = new JsonObject();
		if (this.globalState != null)
			json.addProperty("g", this.getStateName(this.globalState));
		for (int i = 0; i < 6; i++)
			if (this.camoStates[i] != null) {
				json.addProperty("" + i, this.getStateName(this.camoStates[i]));
			}
		return g.toJson(json);
	}

	public String getStateName(IBlockState state) {
		return this.getName(state.getBlock()) + ":" + this.getInt(state);
	}

	public IBlockState getState(String fullName) {
		String[] parts = fullName.split(":");
		return GameRegistry.findBlock(parts[0], parts[1]).
				getStateFromMeta(Integer.valueOf(parts[2]));
	}

	public String getName(Block block) {
		GameRegistry.UniqueIdentifier ui = new GameRegistry.UniqueIdentifier(
				(String) GameData.getBlockRegistry().getNameForObject(block)
		);
		return ui.modId + ":" + ui.name;
	}

	public int getInt(IBlockState state) {
		return state.getBlock().getMetaFromState(state);
	}

	public void writeState(NBTTagCompound tag, IBlockState state) {
		tag.setString("stateName", this.getStateName(state));
	}

	public IBlockState readState(NBTTagCompound tag) {
		if (tag.hasKey("stateName"))
			return this.getState(tag.getString("stateName"));
		else
			return null;
	}

	@Override
	public void writeToNBT(NBTTagCompound comp) {
		super.writeToNBT(comp);

		if (this.globalState != null) {
			this.writeState(comp, this.globalState);
		}

		NBTTagList sidedStates = new NBTTagList();
		for (int i = 0; i < 6; i++)
			if (this.camoStates[i] != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setInteger("side", i);
				this.writeState(tag, this.camoStates[i]);
				sidedStates.appendTag(tag);
			}
		comp.setTag("sidedStates", sidedStates);

	}

	@Override
	public void readFromNBT(NBTTagCompound comp) {
		super.readFromNBT(comp);

		this.globalState = this.readState(comp);

		NBTTagList sidedStates = comp.getTagList("sidedStates", 10);
		for (int j = 0; j < sidedStates.tagCount(); j++) {
			NBTTagCompound tag = sidedStates.getCompoundTagAt(j);
			this.camoStates[tag.getInteger("side")] = this.readState(tag);
		}

	}

}
