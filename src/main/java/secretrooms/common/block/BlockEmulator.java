package secretrooms.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import secretrooms.common.Helper;
import secretrooms.common.lib.Emulation;
import secretrooms.common.tile.TEEmulator;

/**
 * @author TheTemportalist
 */
public class BlockEmulator extends Block implements ITileEntityProvider {

	public static final IUnlistedProperty<String> EMULATE = new IUnlistedProperty<String>() {
		@Override
		public String getName() {
			return "String";
		}

		@Override
		public boolean isValid(String value) {
			return Helper.getItemStack(value) != null;
		}

		@Override
		public Class<String> getType() {
			return String.class;
		}

		@Override
		public String valueToString(String value) {
			return value;
		}

	};

	public static String getEmulated(ItemStack stack) {
		if (stack.hasTagCompound())
			return stack.getTagCompound().getString("emulated");
		return null;
	}

	private final String modid, name;
	private final Class<? extends TEEmulator> tileClass;

	public BlockEmulator(Material materialIn, String modid, String name,
			Class<? extends TEEmulator> tileClass) {
		super(materialIn);
		this.modid = modid;
		this.name = name;
		this.tileClass = tileClass;
		this.setUnlocalizedName(this.getName());
		GameRegistry.registerBlock(this, this.name);
		this.isBlockContainer = true;
		Emulation.addEmulator(this);

	}

	public String getName() {
		return this.modid + ":" + this.name;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		super.breakBlock(worldIn, pos, state);
		worldIn.removeTileEntity(pos);
	}

	@Override
	public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID,
			int eventParam) {
		super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity != null && tileentity.receiveClientEvent(eventID, eventParam);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		try {
			return this.tileClass.getConstructor().newInstance();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	protected BlockState createBlockState() {
		return new ExtendedBlockState(this, new IProperty[0],
				new IUnlistedProperty[] { BlockEmulator.EMULATE });
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TEEmulator tile = (TEEmulator) world.getTileEntity(pos);
		if (tile != null && tile.getEmulated() != null)
			return ((IExtendedBlockState) state).withProperty(
					BlockEmulator.EMULATE, tile.getEmulated()
			);
		return state;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.CUTOUT;
	}

	@Override
	public boolean isFullCube() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state,
			EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		TEEmulator tile = (TEEmulator) worldIn.getTileEntity(pos);
		if (tile != null) {
			tile.emulate(Helper.getStateFromStack(playerIn.getCurrentEquippedItem()));
			worldIn.markBlockForUpdate(pos);
			return true;
		}
		return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
	}

}
