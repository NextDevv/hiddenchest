package me.hydraplugins.hidenchest.database

import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException

class LocalData {
    private var file: File? = null
    var customConfig: FileConfiguration? = null
        get() = field

    // Finds or generates a custom configuration file
    fun setUp() {
        file = File(Bukkit.getServer().pluginManager.getPlugin("HidenChest")!!.dataFolder, "localdata.yml")
        if(!file!!.exists()) {
            try {
                file!!.createNewFile()
            }catch (ignore: IOException) {}
        }
        customConfig = YamlConfiguration.loadConfiguration(file!!)
    }

    fun save() {
        try {
            customConfig?.save(file!!)
        }catch (ignore: IOException) {}
    }

    fun reload() {
        customConfig = YamlConfiguration.loadConfiguration(file!!)
    }
}