package secretrooms.common.tile;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import secretrooms.common.Helper;
import secretrooms.common.lib.FakeManager;

/**
 * @author TheTemportalist
 */
public class TECamo extends TileEntity {

	private IBlockState globalState = null;
	private IBlockState[] camoStates = new IBlockState[6];

	public TECamo() {
	}

	public boolean setCamo(EnumFacing side, IBlockState state) {
		if (state != null && !state.getBlock().isOpaqueCube()) {
			this.globalState = state;
		}
		else {
			this.globalState = null;
			this.camoStates[side.getIndex()] = state;
		}
		FakeManager.get().getFakeWorld(this.getWorld()).update(this.pos, this.globalState);
		return true;
	}

	public String getCamo_Names() {
		Gson g = new Gson();
		JsonObject json = new JsonObject();
		if (this.globalState != null)
			json.addProperty("g", Helper.getNameFromState(this.globalState));
		for (int i = 0; i < 6; i++)
			if (this.camoStates[i] != null) {
				json.addProperty("" + i, Helper.getNameFromState(this.camoStates[i]));
			}
		return g.toJson(json);
	}

	public void writeState(NBTTagCompound tag, IBlockState state) {
		tag.setString("stateName", Helper.getNameFromState(state));
	}

	public IBlockState readState(NBTTagCompound tag) {
		if (tag.hasKey("stateName"))
			return Helper.getStateFromName(tag.getString("stateName"));
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
