package secretrooms.common.lib;

import secretrooms.common.block.BlockEmulator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TheTemportalist 1/24/15
 */
public class Emulation {

	private static final List<BlockEmulator> emulators = new ArrayList<BlockEmulator>();

	public static void addEmulator(BlockEmulator emulator) {
		Emulation.emulators.add(emulator);
	}

	public static List<BlockEmulator> getEmulators() {
		return Emulation.emulators;
	}

}
