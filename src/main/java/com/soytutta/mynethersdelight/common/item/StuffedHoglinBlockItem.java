package com.soytutta.mynethersdelight.common.item;

import com.soytutta.mynethersdelight.common.utility.MNDTextUtils;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.item.PlaceableItem;

public class StuffedHoglinBlockItem extends PlaceableItem {

    public StuffedHoglinBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        BlockState potentialState = this.getBlock().getStateForPlacement(context);

        if (potentialState == null) {
            Level level = context.getLevel();

            if (level.isClientSide()) {
                Player player = context.getPlayer();
                if (player != null) {
                    player.displayClientMessage(MNDTextUtils.getTranslation("block.feast.space_required"), true);
                }
            }
            return InteractionResult.FAIL;
        }

        return super.place(context);
    }
}