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
		if (itemStack == null) return "";
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

	public static ItemStack getItemStack(String name) {
		if (!name.matches("(.*):(.*)")) return null;
		int endNameIndex = name.length();
		int metadata = OreDictionary.WILDCARD_VALUE;

		if (name.matches("(.*):(.*):(.*)")) {
			endNameIndex = name.lastIndexOf(':');
			metadata = Integer.parseInt(name.substring(endNameIndex + 1, name.length()));
		}

		String modid = name.substring(0, name.indexOf(':'));
		String itemName = name.substring(name.indexOf(':') + 1, endNameIndex);
		Block block = GameRegistry.findBlock(modid, itemName);
		Item item = GameRegistry.findItem(modid, itemName);
		return block != null ?
				new ItemStack(block, 1, metadata) :
				(item != null ? new ItemStack(item, 1, metadata) : null);
	}

	public static String getNameFromState(IBlockState state) {
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

	public static String getCamoString(ItemStack itemStack) {
		if (itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("camouflage_name"))
			return itemStack.getTagCompound().getString("camouflage_name");
		else return null;
	}

	public static void writeCamoToTag(NBTTagCompound tagCompound, ItemStack itemStack) {
		tagCompound.setString("camouflage_name", Helper.getName(itemStack, true, true));
	}

}
