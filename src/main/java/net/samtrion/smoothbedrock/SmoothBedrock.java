package net.samtrion.smoothbedrock;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;

import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = SmoothBedrock.MOD_ID, name = SmoothBedrock.MOD_NAME, version = SmoothBedrock.MOD_VERSION, dependencies = SmoothBedrock.MOD_DEPENDENCIES)
public class SmoothBedrock implements IWorldGenerator {
	public static final String MOD_ID = "smoothbedrock";
	public static final String MOD_NAME = "SmoothBedrock";
	public static final String MOD_VERSION = "@VERSION@";
	public static final String MOD_DEPENDENCIES = "required-after:Forge@[12.18.3.2221,)";

	@EventHandler
	public void init(FMLInitializationEvent e) {
		GameRegistry.registerWorldGenerator(this, Integer.MAX_VALUE);
	}
	
	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator generator, IChunkProvider provider) {
		if (world.getWorldInfo().getTerrainType() == WorldType.FLAT) {
			return;
		}

		int dimensionId = world.provider.getDimension();
		if (dimensionId > 0 || dimensionId < -1) {
			return;
		}
		boolean isNether = dimensionId == -1;
		Block replaceBlock = isNether ? Blocks.NETHERRACK : Blocks.STONE;

		generateBedrock(world, chunkX, chunkZ, replaceBlock, 5, 4, 3, 2, 1);
		if (isNether) {
			generateBedrock(world, chunkX, chunkZ, replaceBlock, 122, 123, 124, 125, 126);
		}

	}

	private void generateBedrock(World world, int chunkX, int chunkZ, Block replaceBlock, int... yies) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int posX = chunkX * 16 + x;
				int posZ = chunkZ * 16 + z;
				for (int posY : yies) {
					BlockPos pos = new BlockPos(posX, posY, posZ);
					if (isBedrock(world, pos)) {
						world.setBlockState(pos, replaceBlock.getDefaultState(), 0);
					}
				}
			}
		}
	}

	private boolean isBedrock(World world, BlockPos pos) {
		return world.getBlockState(pos).getBlock() == Blocks.BEDROCK;
	}
}