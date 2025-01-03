package dk.tandhjulet;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.plugin.java.JavaPlugin;

import dk.tandhjulet.commands.CommandPaste;
import dk.tandhjulet.config.Config;
import dk.tandhjulet.map.ChunkProvider;
import dk.tandhjulet.map.MapManager;
import lombok.Getter;

public class SchematicPaster extends JavaPlugin {
	// Precalculates and stores indexes to id array
	public final static short[][][] CACHE_I = new short[256][16][16];
	public final static short[][][] CACHE_J = new short[256][16][16];

	/**
	 * For reverse lookup
	 * [ i | j ] => x, y or z
	 */
	public final static byte[][] CACHE_X = new byte[16][4096];
	public final static short[][] CACHE_Y = new short[16][4096];
	public final static byte[][] CACHE_Z = new byte[16][4096];

	@Getter
	private static SchematicPaster plugin;

	@Getter
	private static Config conf;

	@Override
	public void onEnable() {
		plugin = this;
		conf = new Config(this);

		try {
			Bukkit.getLogger().info("[SchematicPaster] Injecting custom ServerChunkProvider");

			// TODO: Make ChunkProvider for all worlds
			CraftWorld world = (CraftWorld) Bukkit.getWorlds().get(0);
			ChunkProvider.inject(world.getHandle());

		} catch (IllegalArgumentException | IllegalAccessException | CloneNotSupportedException e) {
			Bukkit.getLogger().severe("Failed to inject ChunkProvider.");
			ChunkProvider.setInjected(false);
			e.printStackTrace();
		}

		CommandPaste.register();
		MapManager.get();
	}

	public static boolean hasNBT(int id) {
		switch (id) {
			case 26:
			case 218:
			case 54:
			case 130:
			case 142:
			case 27:
			case 137:
			case 188:
			case 189:
			case 52:
			case 154:
			case 84:
			case 25:
			case 144:
			case 138:
			case 176:
			case 177:
			case 63:
			case 119:
			case 68:
			case 323:
			case 117:
			case 116:
			case 28:
			case 66:
			case 157:
			case 61:
			case 62:
			case 140:
			case 146:
			case 149:
			case 150:
			case 158:
			case 23:
			case 123:
			case 124:
			case 29:
			case 33:
			case 151:
			case 178:
			case 209:
			case 210:
			case 211:
			case 255:
			case 219:
			case 220:
			case 221:
			case 222:
			case 223:
			case 224:
			case 225:
			case 226:
			case 227:
			case 228:
			case 229:
			case 230:
			case 231:
			case 232:
			case 233:
			case 234:
				return true;
			default:
				return false;
		}
	}

	static {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < 256; y++) {
					final short i = (short) (y >> 4);
					final short j = (short) (((y & 0xF) << 8) | (z << 4) | x);
					CACHE_I[y][z][x] = i;
					CACHE_J[y][z][x] = j;

					CACHE_X[i][j] = (byte) x;
					CACHE_Y[i][j] = (short) y;
					CACHE_Z[i][j] = (byte) z;
				}
			}
		}
	}
}