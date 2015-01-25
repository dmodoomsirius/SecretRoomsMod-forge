package secretrooms.common;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

/**
 * @author TheTemportalist
 */
public class Helper {

	public static String getName(ItemStack itemStack, boolean hasID, boolean hasMeta) {
		if (itemStack == null) return null;
		String name;
		if (Block.getBlockFromItem(itemStack.getItem()) == null) {
			name = GameData.getItemRegistry().getNameForObject(itemStack.getItem()).toString();
		}
		else {
			name = GameData.getBlockRegistry().getNameForObject(Block.getBlockFromItem(
					itemStack.getItem())).toString();
		}
		return name + (hasMeta ? ":" + itemStack.getItemDamage() : "");
	}

	public static String[] getDetails(String name) {
		if (name == null || (!name.matches("(.*):(.*)"))) return null;
		int endNameIndex = name.length();
		int metadata = OreDictionary.WILDCARD_VALUE;

		if (name.matches("(.*):(.*):(.*)")) {
			endNameIndex = name.lastIndexOf(':');
			metadata = Integer.parseInt(name.substring(endNameIndex + 1, name.length()));
		}

		String modid = name.substring(0, name.indexOf(':'));
		String itemName = name.substring(name.indexOf(':') + 1, endNameIndex);
		return new String[]{modid, itemName, metadata + ""};
	}

	public static ItemStack getItemStack(String name) {
		if (name == null) return null;
		String[] details = Helper.getDetails(name);
		if (details == null) return null;
		Block block = GameRegistry.findBlock(details[0], details[1]);
		Item item = GameRegistry.findItem(details[0], details[1]);
		int metadata = Integer.parseInt(details[2]);
		return block != null ?
				new ItemStack(block, 1, metadata) :
				(item != null ? new ItemStack(item, 1, metadata) : null);
	}

	public static String getNameFromState(IBlockState state) {
		if (state == null) return null;
		Block block = state.getBlock();
		return Helper.getName(
				new ItemStack(block, 1, block.getMetaFromState(state)), true, true
		);
	}

	public static IBlockState getStateFromName(String name) {
		ItemStack itemStack = Helper.getItemStack(name);
		if (itemStack != null && itemStack.getItem() != null) {
			Block block = Block.getBlockFromItem(itemStack.getItem());
			if (block != null) {
				return block.getStateFromMeta(itemStack.getMetadata());
			}
		}
		return null;
	}

	public static ItemStack getStackFromState(IBlockState state) {
		return Helper.getItemStack(Helper.getNameFromState(state));
	}

	public static IBlockState getStateFromStack(ItemStack stack) {
		return Helper.getStateFromName(Helper.getName(stack, true, true));
	}

	public static String getCamoString(ItemStack itemStack) {
		if (itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("camouflage_name"))
			return itemStack.getTagCompound().getString("camouflage_name");
		else return null;
	}

	public static void writeCamoToTag(NBTTagCompound tagCompound, ItemStack itemStack) {
		tagCompound.setString("camouflage_name", Helper.getName(itemStack, true, true));
	}

}
