package com.github.abrarsyed.secretroomsmod.client;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import com.github.abrarsyed.secretroomsmod.common.OwnershipManager;
import com.github.abrarsyed.secretroomsmod.common.ProxyCommon;
import com.github.abrarsyed.secretroomsmod.common.SecretRooms;
import com.github.abrarsyed.secretroomsmod.network.PacketKey;
import com.github.abrarsyed.secretroomsmod.network.PacketManager;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value = Side.CLIENT)
public class ProxyClient extends ProxyCommon
{
	public static KeyBinding	key_OneWayFace;
	private boolean				oneWayFaceTowards	= true;

	public ProxyClient()
	{
		key_OneWayFace = new KeyBinding("key.secretroomsmod.oneWayface", Keyboard.KEY_BACKSLASH, "key.categories.gameplay");
		//MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void loadRenderStuff()
	{
		SecretRooms.render3DId = RenderingRegistry.getNextAvailableRenderId();
		SecretRooms.renderFlatId = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new BlockRenderer(SecretRooms.render3DId));
		RenderingRegistry.registerBlockHandler(new BlockRenderer(SecretRooms.renderFlatId));
	}

	@Override
	public void loadKeyStuff()
	{
		ClientRegistry.registerKeyBinding(key_OneWayFace);
		FMLCommonHandler.instance().bus().register(new SecretKeyHandler());
	}

	@Override
	public void onServerStop(FMLServerStoppingEvent e)
	{
		super.onServerStop(e);
		oneWayFaceTowards = true;
	}

	@Override
	public void onKeyPress(UUID uuid)
	{
		oneWayFaceTowards = !oneWayFaceTowards;
		PacketManager.sendToServer(new PacketKey());
	}

	@Override
	public boolean getFaceTowards(UUID uuid)
	{
		return oneWayFaceTowards;
	}

    @Override
    public boolean isOwner(IBlockAccess iba, int x, int y, int z)
    {
        // client only right?
        UUID uid = Minecraft.getMinecraft().thePlayer.getPersistentID();
        World world = Minecraft.getMinecraft().theWorld;
        
        if (iba instanceof World)
        {
            world = (World) iba;
        }
        
        return OwnershipManager.isOwner(uid, new ClientBlockLocation(world, x, y, z));
    }
}
