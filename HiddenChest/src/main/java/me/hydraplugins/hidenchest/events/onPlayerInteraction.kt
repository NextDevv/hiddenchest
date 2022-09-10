package me.hydraplugins.hidenchest.events

import me.hydraplugins.hidenchest.database.DataBase
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent


object onPlayerInteraction: Listener {
    @EventHandler
    fun onPlayerInteraction(event: PlayerInteractEvent) {
        val player = event.player
        val action = event.action

        if(action == Action.RIGHT_CLICK_BLOCK) {
            player.sendMessage("1")
            if(player.inventory.itemInHand != null || !player.inventory.itemInHand.type.equals(Material.AIR)) {
                val item = player.inventory.itemInHand
                player.sendMessage("2")
                if(!item.hasItemMeta()) return
                if(!item.itemMeta.hasLore()) return
                player.sendMessage("3")

                val lore = item.itemMeta.lore
                val block = event.clickedBlock
                if(lore.size < 3 ) return
                val data = DataBase.getPlayerFromUUID(lore[2].toInt()) ?: return
                val id = data.id
                val loc = Location(player.world, data.locX, data.locY, data.locZ)
                if(block.location == loc) {
                    if(lore.contains(id.toString())) {
                        val inventory = Bukkit.createInventory(player, 54, "§6Chesta Nascosta")
                        val id = DataBase().getChestID(lore[2].toInt())
                        println(id)
                        player.openInventory(inventory)
                    }
                }
            }
        }
    }
}