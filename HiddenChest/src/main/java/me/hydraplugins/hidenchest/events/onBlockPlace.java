package me.hydraplugins.hidenchest.events;

import me.hydraplugins.hidenchest.HidenChest;
import me.hydraplugins.hidenchest.commands.HiddenChestCommand;
import me.hydraplugins.hidenchest.database.DataBase;
import me.hydraplugins.hidenchest.database.PlayerData;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;

public class onBlockPlace implements Listener {
    HidenChest plugin = HidenChest.getPlugin(HidenChest.class);

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Block b  = e.getBlock();
        Player p = e.getPlayer();

        ItemStack item = p.getInventory().getItemInHand();
        if(!item.hasItemMeta()) return;
        if(!item.getItemMeta().hasLore()) return;

        List<String> lore = item.getItemMeta().getLore();
        if(lore.contains("§fClicca su una chest §6nascosta§f per aprirla")) {
            e.setCancelled(true);
        } else if (lore.contains("§fcrei una chest §6NASCOSTA§f e §6PRIVATA")) {
            int id = HiddenChestCommand.generateKeyID();
            p.getInventory().addItem(HiddenChestCommand.generateKey(p, id));

            PlayerData data = DataBase.getPlayerFromUUID(id);


            if(data == null) {
                data = new PlayerData(
                        id,
                        p.getDisplayName(),
                        p.getWorld().getName(),
                        b.getLocation().getX(),
                        b.getLocation().getY(),
                        b.getLocation().getZ()

                );
                new DataBase().createPlayerDatabase(data);
            }else {
                data.setId(id);
                data.setName(p.getDisplayName());
                data.setLocX(b.getLocation().getX());
                data.setLocY(b.getLocation().getY());
                data.setLocZ(b.getLocation().getZ());

                new DataBase().createPlayerDatabase(data);
            }

            boolean i = new DataBase().getChestID(id);
            System.out.println("i: " + i);

        }
    }
}
