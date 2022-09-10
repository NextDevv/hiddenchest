package me.hydraplugins.hidenchest

import me.hydraplugins.hidenchest.commands.HiddenChestCommand
import me.hydraplugins.hidenchest.database.DataBase
import me.hydraplugins.hidenchest.database.LocalData
import me.hydraplugins.hidenchest.events.onBlockBreak
import me.hydraplugins.hidenchest.events.onBlockPlace
import me.hydraplugins.hidenchest.events.onInvenotryEvent
import me.hydraplugins.hidenchest.events.onPlayerInteraction
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File




class HidenChest : JavaPlugin() {
    var database : DataBase? = null

    companion object {
        lateinit var instance:HidenChest
    }

    init {
        instance = this
    }

    override fun onEnable() {
        // Plugin startup logic
        logger.info("HidenChest is enabled")

        // Setting up the database
        database = DataBase()
        DataBase.InitilizeDatabase()

        // Registering the commands executor
        getCommand("hidenchest").executor = HiddenChestCommand()

        //
        Bukkit.getPluginManager().registerEvents(onBlockPlace(), this)
        Bukkit.getPluginManager().registerEvents(onBlockBreak, this)
        Bukkit.getPluginManager().registerEvents(onPlayerInteraction, this)
        Bukkit.getPluginManager().registerEvents(onInvenotryEvent, this)

        //
        config.options().copyDefaults(true)
        saveConfig()
        LocalData().setUp()
        LocalData().customConfig?.addDefault("players", "hiddenchests");
        LocalData().customConfig?.options()?.copyDefaults(true)
        LocalData().save()
    }

    override fun onDisable() {
        // Plugin shutdown logic
        database?.closeConnection()

        LocalData().customConfig?.options()?.copyDefaults(true)
        LocalData().save()
        config.options().copyDefaults(true)
        saveConfig()
    }
}