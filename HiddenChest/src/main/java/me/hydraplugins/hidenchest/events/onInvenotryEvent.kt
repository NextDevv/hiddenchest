package me.hydraplugins.hidenchest.events

import me.hydraplugins.hidenchest.HidenChest
import me.hydraplugins.hidenchest.database.DataBase
import me.hydraplugins.hidenchest.database.LocalData
import me.hydraplugins.hidenchest.database.PlayerData
import me.hydraplugins.hidenchest.service.SaveInventoryAPI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemStack


object onInvenotryEvent : Listener {
    @EventHandler
    fun onInvenotryEvent(e : InventoryCloseEvent) {
        val player = e.player
        val inventory = e.inventory

        val contents: Array<out ItemStack>? = inventory.contents

        if(inventory.name != "§6Chesta Nascosta") return

        val item = player.inventory.itemInHand
        val id:Int = item.itemMeta.lore[2].toInt()
        val config = HidenChest.instance.config
        config.set("players.inventory.${id.toString()}", contents)
        HidenChest.instance.saveConfig()
    }

    @EventHandler
    fun onInventoryOpen(e: InventoryOpenEvent) {
        val player = e.player
        val inventory = e.inventory
        val config = HidenChest.instance.config
        val item = player.inventory.itemInHand
        val id:Int = item.itemMeta.lore[2].toInt()

        inventory.contents = config.get("players.inventory.${id.toString()}") as Array<out ItemStack>?
    }
}


