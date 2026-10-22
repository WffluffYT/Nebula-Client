package com.nebula.mixin;

import com.nebula.module.XrayModule;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockMixin {
	// VERSION-SENSITIVE (3): signature of Block.shouldRenderFace. This is the 3-arg form used since 1.21.5.
	@Inject(method = "shouldRenderFace", at = @At("HEAD"), cancellable = true)
	private static void nebula$xray(BlockState state, BlockState neighbor, Direction direction,
			CallbackInfoReturnable<Boolean> cir) {
		XrayModule xray = XrayModule.INSTANCE;
		if (xray != null && xray.isEnabled()) {
			cir.setReturnValue(xray.shouldShow(state));
		}
	}
}
