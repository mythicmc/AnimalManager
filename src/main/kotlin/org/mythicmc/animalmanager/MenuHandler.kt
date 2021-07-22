package org.mythicmc.animalmanager

import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent

class MenuHandler : Listener {
    @EventHandler fun onPlayerInteractEntityEvent(e: PlayerInteractEntityEvent) {
        val player = e.player
        val entity = e.rightClicked
        if (
            entity is LivingEntity &&
            isEntitySupported(entity) &&
            player.isSneaking &&
            player.hasPermission("animalmanager.view")
        ) {
            handleGui(player, entity)
        }
    }

    fun handleGui(player: Player, entity: LivingEntity) {
        if (!isEntitySupported(entity)) return
        // TODO: Handle the actual GUI rn.
    }

    // TODO.

    fun isEntitySupported(entity: LivingEntity): Boolean {
        return false
    }
}
