package me.hydraplugins.hidenchest.events

import me.hydraplugins.hidenchest.HidenChest
import me.hydraplugins.hidenchest.commands.HiddenChestCommand
import me.hydraplugins.hidenchest.database.DataBase
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object onBlockBreak: Listener {
    @EventHandler
    fun onBlockBreak(e:BlockBreakEvent) {
        val block = e.block
        val player = e.player


        for (item:ItemStack?  in player.inventory.contents) {
            if(item != null) {
                if(item.type != Material.AIR) {
                    if(item.hasItemMeta()) {
                        if(item.itemMeta.hasLore()) {
                            val lore = item.itemMeta.lore
                            if(lore.contains("§fClicca su una chest §6nascosta§f per aprirla")) {
                                val data = DataBase.getPlayerFromUUID(lore[2].toInt())
                                if(data != null) {
                                    val id = data.id

                                    if(lore.contains(id.toString())) {
                                        e.isCancelled = true
                                        val data = DataBase.getPlayerFromUUID(lore[2].toInt())
                                        if(data != null) {
                                            if(block.location == Location(player.world,data.locX,data.locY, data.locZ)) {
                                                DataBase().deletePlayerDatabase(data)
                                                player.inventory.removeItem(item)
                                                block.type = Material.AIR
                                                player.world.dropItem(block.location, HiddenChestCommand.createBlock())
                                                val items:Array<out ItemStack> = HidenChest.instance.config.get("players.inventory.${id.toString()}") as Array<out ItemStack>
                                                for(i:ItemStack in items) {
                                                    player.world.dropItem(block.location, i)
                                                }
                                                HidenChest.instance.config.set("players.inventory.${id.toString()}", null)
                                            }
                                        }
                                        break
                                    }else if(item.type == Material.TRIPWIRE_HOOK){
                                        e.isCancelled = true
                                        player.sendMessage("§cQuesta cesta non appartiene a te!")
                                        break
                                    }
                                }else {
                                    e.isCancelled = true
                                    player.sendMessage("§cQuesta cesta non appartiene a te!")
                                    break
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun Player.createInventory(player: Player, size: Int): Inventory {
    val inventory = Bukkit.createInventory(player,size,"§6Hidden Chest")
    player.openInventory
    return inventory
}