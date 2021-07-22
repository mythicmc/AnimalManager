package org.mythicmc.animalmanager

import org.bukkit.plugin.java.JavaPlugin

class AnimalManager : JavaPlugin() {
    lateinit var menuHandler: MenuHandler
    private set

    override fun onEnable() {
        // Register listener.
        menuHandler = MenuHandler()
        server.pluginManager.registerEvents(menuHandler, this)
    }
}
