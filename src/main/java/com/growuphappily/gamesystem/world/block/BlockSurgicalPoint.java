package com.growuphappily.gamesystem.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class BlockSurgicalPoint extends Block {
    public static VoxelShape shape;

    static{
        shape = Shapes.or(
                Block.box(7,8,7,9,9,9),
                Block.box(7,10,5,9,12,6),
                Block.box(7,13,7,9,14,9),
                Block.box(6,9,6,10,13,10),
                Block.box(7,10,10,9,12,11),
                Block.box(5,10,7,6,12,9),
                Block.box(10,10,7,11,12,9)
        );
    }

    public BlockSurgicalPoint() {
        super(Properties.of(Material.ICE).destroyTime(5).noOcclusion());
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState p_60555_, @NotNull BlockGetter p_60556_, @NotNull BlockPos p_60557_, @NotNull CollisionContext p_60558_){
        return shape;
    }
}
