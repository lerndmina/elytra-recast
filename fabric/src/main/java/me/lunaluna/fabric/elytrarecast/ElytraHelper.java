package me.lunaluna.fabric.elytrarecast;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;

public class ElytraHelper {
    public static boolean castElytra(LocalPlayer player) {
        if (checkElytra(player) && checkFallFlyingIgnoreGround(player)) {
            var connection = player.connection;
            if (connection != null)
                connection
                        .send(new ServerboundPlayerCommandPacket(player, ServerboundPlayerCommandPacket.Action.START_FALL_FLYING));
            return true;
        } else
            return false;
    }

    public static boolean checkElytra(LocalPlayer player) {
        if (player.input.keyPresses.jump() && !player.getAbilities().flying && !player.isPassenger() && !player.onClimbable()) {
            var itemStack = player.getItemBySlot(EquipmentSlot.CHEST);
            return itemStack.is(Items.ELYTRA) && LivingEntity.canGlideUsing(itemStack, EquipmentSlot.CHEST);
        } else
            return false;
    }

    public static boolean checkFallFlyingIgnoreGround(LocalPlayer player) {
        if (!player.isInWater() && !player.hasEffect(MobEffects.LEVITATION)) {
            var itemStack = player.getItemBySlot(EquipmentSlot.CHEST);
            if (itemStack.is(Items.ELYTRA) && LivingEntity.canGlideUsing(itemStack, EquipmentSlot.CHEST)) {
                player.startFallFlying();
                return true;
            } else
                return false;
        } else
            return false;
    }
}
