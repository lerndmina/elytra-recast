package me.lunaluna.fabric.elytrarecast.mixin;

import me.lunaluna.fabric.elytrarecast.config.ElytraRecastConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
@Environment(EnvType.CLIENT)
public class PlayerJumpCooldownMixin {

    @Shadow
    private int noJumpDelay;

    @SuppressWarnings("resource")
    private LocalPlayer player() {
        return Minecraft.getInstance().player;
    }

    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Inject(method = "aiStep", at = @At("HEAD"))
    public void reduceCooldown(CallbackInfo ci) {
        if (ElytraRecastConfig.jumpEnabled && (noJumpDelay > ElytraRecastConfig.jumpCooldown)
                && equals(player())) {
            noJumpDelay = ElytraRecastConfig.jumpCooldown;
        }
    }
}
