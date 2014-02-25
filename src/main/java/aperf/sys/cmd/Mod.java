package aperf.sys.cmd;

import java.util.Map;

import net.minecraft.command.ICommandSender;

import org.bukkit.ChatColor;

import aperf.aPerf;
import aperf.commands.BaseCommand;
import aperf.commands.Command;

public class Mod extends BaseCommand {
    @Command(name = "aperf", syntax = "(?:reload|r)", description = "Reloads the whole mod", isPrimary = true, permission = "aperf.cmd.reload")
    public void reload(Object plugin, ICommandSender sender, Map<String, String> args) {
        aPerf.instance.reload();
        msg(sender, "%sReloaded", ChatColor.GREEN);
    }
}
