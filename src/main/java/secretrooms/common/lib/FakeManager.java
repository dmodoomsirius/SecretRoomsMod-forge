package secretrooms.common.lib;

import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

/**
 * @author TheTemportalist
 */
public class FakeManager {

	public static FakeManager instance;

	public static FakeManager get(){
		return FakeManager.instance;
	}

	private HashMap<Integer, FakeWorld> fakeWorlds;

	public FakeManager() {
		this.fakeWorlds = new HashMap<Integer, FakeWorld>();
	}

	public void onServerStop(FMLServerStoppingEvent e) {
		this.fakeWorlds.clear();
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onWorldLoad(WorldEvent.Load event) {
		this.fakeWorlds.put(event.world.provider.getDimensionId(), new FakeWorld(event.world));
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onWorldUnLoad(WorldEvent.Unload event) {
		this.fakeWorlds.remove(event.world.provider.getDimensionId());
	}

	public FakeWorld getFakeWorld(World world) {
		return this.fakeWorlds.get(world.provider.getDimensionId());
	}

}
