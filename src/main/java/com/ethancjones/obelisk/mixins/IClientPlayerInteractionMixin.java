/* ==========================================================
 * Author : Ethan Jones
 * Date   : 21/06/2024
 * TODO   : Nothing
 * Uses   :
 * ==========================================================
 */
package com.ethancjones.obelisk.mixins;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPlayerInteractionManager.class)
public interface IClientPlayerInteractionMixin
{
    @Accessor("currentBreakingProgress")
    void setCurrentBreakingProgress(float currentBreakingProgress);

    @Accessor("blockBreakingCooldown")
    void setBlockBreakingCooldown(int blockBreakingCooldown);
}
