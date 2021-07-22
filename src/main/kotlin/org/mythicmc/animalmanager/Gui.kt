package org.mythicmc.animalmanager

import org.bukkit.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class Gui(private var name: String, private var player: Player, private var plugin: Plugin) {
    // List of click event listeners.
    private var clickEventListeners = ArrayList<Listener>()
    // Whether or not the GUI is open.
    private var open = false
    // The inventory of the GUI.
    private var inventory: Inventory = Bukkit.createInventory(null, InventoryType.CHEST, name)

    @FunctionalInterface
    interface GuiCloseEventHandler {
        fun run(gui: Gui, e: InventoryCloseEvent)
    }

    @FunctionalInterface
    interface GuiClickEventHandler {
        fun run(gui: Gui, e: InventoryClickEvent)
    }

    fun addItemToGui(item: ItemStack, number: Int) {
        // Add the given number of items.
        for (i in 1..number) {
            inventory.addItem(item)
        }
    }
    fun addItemToGui(item: ItemStack) {
        inventory.addItem(item)
    }

    fun registerClickHandler(index: Int, executor: GuiClickEventHandler) {
        // Define the listener.
        val listener = object : Listener {
            @EventHandler
            fun onInventoryClickEvent(e: InventoryClickEvent) {
                // Check if the inventory title and the slot index match, and whether or not it is the top inventory.
                if (e.view.title == name && e.slot == index && e.clickedInventory == e.view.topInventory)
                    executor.run(this@Gui, e)
            }
        }
        // Add the listener to the list of listeners and then register it.
        clickEventListeners.add(listener)
        plugin.server.pluginManager.registerEvents(listener, plugin)
    }

    fun registerCloseHandler(executor: GuiCloseEventHandler) {
        // Register a listener to call the handler.
        plugin.server.pluginManager.registerEvents(object : Listener {
            @EventHandler
            fun onInventoryCloseEvent(e: InventoryCloseEvent) {
                // If the player and inventory name matches..
                if (e.player.name == player.name && e.view.title == name) {
                    // We call the handler and then unregister this listener.
                    executor.run(this@Gui, e)
                    InventoryCloseEvent.getHandlerList().unregister(this)
                }
            }
        }, plugin)
    }

    fun open () {
        player.openInventory(inventory)
        open = true
        // Register a clean-up operation.
        plugin.server.pluginManager.registerEvents(object : Listener {
            // When the inventory closes..
            @EventHandler
            fun onInventoryCloseEvent(e: InventoryCloseEvent) {
                if (e.player.name == player.name && e.view.title == name) {
                    // Set open to false.
                    open = false
                    // Unregister this listener and all other click event listeners.
                    InventoryCloseEvent.getHandlerList().unregister(this)
                    InventoryClickEvent.getHandlerList().unregister(this)
                    clickEventListeners.forEach { InventoryClickEvent.getHandlerList().unregister(it) }
                }
            }
            // When there's a click in the inventory..
            @EventHandler
            fun onInventoryClickEvent(e: InventoryClickEvent) {
                if (e.view.player.name == player.name && e.view.title == name && e.clickedInventory == e.view.topInventory)
                    e.isCancelled = true // Cancel the event.
            }
        }, plugin)
    }

    fun close () {
        // All appropriate events should be called to clean-up the GUI.
        if (open) player.closeInventory()
    }
}
