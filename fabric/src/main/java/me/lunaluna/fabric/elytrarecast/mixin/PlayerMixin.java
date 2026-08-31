package me.lunaluna.fabric.elytrarecast.mixin;

import me.lunaluna.fabric.elytrarecast.ElytraHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
@Environment(EnvType.CLIENT)
abstract class PlayerMixin {

    private LocalPlayer player() {
        return Minecraft.getInstance().player;
    }

    @Unique
    private boolean previousElytra = false;
    @Unique
    private boolean awaitingElytra = false;

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "aiStep", at = @At("TAIL"))
    public void recastIfLanded(CallbackInfo ci) {
        if (player() == null || !((Object) this instanceof LocalPlayer))
            return;
        boolean elytra = isFallFlying();
        if (awaitingElytra) {
            if (elytra)
                awaitingElytra = false;
        } else if (!elytra && previousElytra) {
            Minecraft.getInstance().getSoundManager().stop(SoundEvents.ELYTRA_FLYING.location(),
                    SoundSource.PLAYERS);
            ElytraHelper.castElytra(player());
            awaitingElytra = ElytraHelper.checkElytra(player());
        }
        previousElytra = elytra;
    }

    @Shadow
    public abstract boolean isFallFlying();
}
