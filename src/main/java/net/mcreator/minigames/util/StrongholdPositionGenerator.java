package net.mcreator.minigames.util;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;

import java.util.List;
import java.util.ArrayList;

import static java.lang.Math.*;

public class StrongholdPositionGenerator {
    public static List<BlockPos> generate(long seed, int offsetX, int offsetZ) {
        List<BlockPos> positions = new ArrayList<>();
        RandomSource random = RandomSource.create(seed);

        double angle = random.nextDouble() * Math.PI * 2.0;

        int ring = 0;
        int ringSize = 3;
        int ringIndex = 0;
        for (int i = 0; i < 128; i++) {
            double distance = 128 + ring * 192;

            int x = (int) round(cos(angle) * distance);
            int z = (int) round(sin(angle) * distance);
            x += offsetX;
            z += offsetZ;
            positions.add(
                    new BlockPos(x, 0, z)
            );
            angle += random.nextDouble() * Math.PI * 2.0;

            ringIndex++;

            if (ringIndex == ringSize) {

                ring++;

                ringIndex = 0;

                ringSize += 2 * ringSize / (ring + 1);

                ringSize = Math.min(
                        ringSize,
                        128 - i
                );


            }
        }
        return positions;
    }
    public static List<BlockPos> POSITIONS = new ArrayList<>();

}
