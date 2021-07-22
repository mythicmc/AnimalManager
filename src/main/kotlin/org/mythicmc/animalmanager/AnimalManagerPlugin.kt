package org.mythicmc.animalmanager

import org.bukkit.plugin.java.JavaPlugin

class AnimalManagerPlugin : JavaPlugin() {
    override fun onEnable() {
        super.onEnable()
        reload()
    }

    fun reload() {
        saveDefaultConfig()
        reloadConfig()
        // TODO: Reload other stuff.
    }
}