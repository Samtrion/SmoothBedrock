package net.samtrion.smoothbedrock;

import java.util.Random;

import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

@Mod(modid = SmoothBedrock.MOD_ID, name = SmoothBedrock.MOD_NAME, version = SmoothBedrock.MOD_VERSION, dependencies = SmoothBedrock.MOD_DEPENDENCIES)
public class SmoothBedrock implements IWorldGenerator {
	public static final String MOD_ID = "smoothbedrock";
	public static final String MOD_NAME = "SmoothBedrock";
	public static final String MOD_VERSION = "@VERSION@";
	public static final String MOD_DEPENDENCIES = "required-after:Forge@[10.13.2.1230,)";

	@EventHandler
	public void init(FMLInitializationEvent e) {
		GameRegistry.registerWorldGenerator(this, Integer.MAX_VALUE);
	}

	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkProvider generator, IChunkProvider provider) {
		if (world.provider.terrainType == WorldType.FLAT) {
			return;
		}

		int dimensionId = world.provider.dimensionId;
		if (dimensionId > 0 || dimensionId < -1) {
			return;
		}
		boolean isNether = dimensionId == -1;
		Block replaceBlock = isNether ? Blocks.netherrack : Blocks.stone;
		Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
		CheckChunkStorage(0, replaceBlock, chunk, new int[] { 5, 4, 3, 2, 1 });
		if (isNether) {
			CheckChunkStorage(7, replaceBlock, chunk, new int[] { 122, 123, 124, 125, 126 });
		}
	}

	private void CheckChunkStorage(int i, Block replaceBlock, Chunk chunk, int... yies) {
		ExtendedBlockStorage storage = chunk.getBlockStorageArray()[i];
		if (storage == null) {
			return;
		}
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y : yies) {
					if (storage.getBlockByExtId(x, y & 0xF, z) == Blocks.bedrock) {
						storage.func_150818_a(x, y & 0xF, z, replaceBlock);
						storage.setExtBlockMetadata(x, y & 0xF, z, 0);
					}
				}
			}
		}
	}
}
