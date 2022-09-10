package me.hydraplugins.hidenchest.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HiddenChestCommand implements CommandExecutor {
    private static Player p;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return true;
        p = (Player)sender;
        p.sendMessage("§aItem give successfully");

        p.getInventory().addItem(createBlock());

        return true;
    }
    public static ItemStack createBlock() {
        ItemStack item = new ItemStack(Material.REDSTONE_LAMP_OFF, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§6Hidden Chest");
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<String> lore = new ArrayList<String>();
        lore.add("§6Pizzando questo blocco");
        lore.add("§fcrei una chest §6NASCOSTA§f e §6PRIVATA");
        meta.setLore(lore);

        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack generateKey(Player p, int id) {
        ItemStack item2 = new ItemStack(Material.TRIPWIRE_HOOK, 1);
        ItemMeta meta2 = item2.getItemMeta();

        meta2.setDisplayName("§6Hidden Chest Key");
        meta2.addEnchant(Enchantment.DURABILITY, 1, true);
        meta2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<String> lore2 = new ArrayList<String>();
        lore2.add("§fClicca su una chest §6nascosta§f per aprirla");

        Random rand = new Random();

        lore2.add(p.getDisplayName());
        lore2.add(String.valueOf(id));
        meta2.setLore(lore2);

        item2.setItemMeta(meta2);

        return item2;
    }

    public static int generateKeyID() {
        return new Random().nextInt(1000000000);
    }
}


