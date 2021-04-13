package com.willfp.ecoenchants.command.commands;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.AbstractCommand;
import com.willfp.eco.core.command.AbstractTabCompleter;
import com.willfp.eco.util.NumberUtils;
import com.willfp.ecoenchants.command.tabcompleters.TabCompleterRandomEnchant;
import com.willfp.ecoenchants.display.EnchantmentCache;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandGiverandombook extends AbstractCommand {
    /**
     * Instantiate a new /ecoreload command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandGiverandombook(@NotNull final EcoPlugin plugin) {
        super(plugin, "giverandombook", "ecoenchants.randombook", false);
    }

    @Override
    public AbstractTabCompleter getTab() {
        return new TabCompleterRandomEnchant(this);
    }

    @Override
    public void onExecute(@NotNull final CommandSender sender,
                          @NotNull final List<String> args) {
        if (args.isEmpty()) {
            sender.sendMessage(this.getPlugin().getLangYml().getMessage("requires-player"));
            return;
        }
        Player player = Bukkit.getServer().getPlayer(args.get(0));

        if (player == null) {
            sender.sendMessage(this.getPlugin().getLangYml().getMessage("invalid-player"));
            return;
        }

        ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemStack.getItemMeta();
        Enchantment enchantment = Enchantment.values()[NumberUtils.randInt(0, Enchantment.values().length - 1)];
        int level = NumberUtils.randInt(1, enchantment.getMaxLevel());
        meta.addStoredEnchant(enchantment, level, true);
        itemStack.setItemMeta(meta);

        String message = this.getPlugin().getLangYml().getMessage("gave-random-book");
        message = message.replace("%enchantment%", EnchantmentCache.getEntry(enchantment).getName() + " " + NumberUtils.toNumeral(level) + "§r");
        sender.sendMessage(message);

        String message2 = this.getPlugin().getLangYml().getMessage("received-random-book");
        message2 = message2.replace("%enchantment%", EnchantmentCache.getEntry(enchantment).getName() + " " + NumberUtils.toNumeral(level) + "§r");
        player.sendMessage(message2);
    }
}