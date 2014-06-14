package aperf.sys.cmd;

import java.util.Map;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import aperf.aPerf;
import aperf.commands.BaseCommand;
import aperf.commands.Command;
import aperf.sys.ModuleBase;

public class Module extends BaseCommand {
	@Command(name = "aperf", syntax = "(?:module|m)", description = "Module list", isPrimary = true, permission = "aperf.cmd.module.list")
	public void list(Object plugin, ICommandSender sender, Map<String, String> args) {
		String format = "%s%-6s%s | %s%-7s%s | %s";

		msg(sender, format, EnumChatFormatting.DARK_GREEN, "Active", "", "", "Enabled", "", "Name");
		msg(sender, "%s---------------------------------------------", EnumChatFormatting.GRAY);
		for (ModuleBase m : aPerf.instance.modules) {
			if (!m.isVisible()) {
				continue;
			}

			msg(sender, format, m.isEnabled() ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.RED, m.isEnabled() ? "yes" : "no", EnumChatFormatting.GREEN, aPerf.instance.isEnabled(m) ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.RED, aPerf.instance.isEnabled(m) ? "yes" : "no", EnumChatFormatting.GREEN, m.getName());
		}

		msg(sender, "%s---------------------------------------------", EnumChatFormatting.GRAY);
	}

	@Command(name = "aperf", syntax = "(?:module|m) (?:set|switch|s) <name> <on> [enabled]", description = "Turn module on/off, if <enabled> is set, sets the parameters seperately", permission = "aperf.cmd.module.switch")
	public void moduleSwitch(Object plugin, ICommandSender sender, Map<String, String> args) {
		String name = args.get("name");
		String on = args.get("on");
		String enabled = args.get("enabled");

		boolean turnOn = on.equalsIgnoreCase("on") || on.equalsIgnoreCase("active") || on.equalsIgnoreCase("1") || on.equalsIgnoreCase("yes");
		boolean turnEnabled = enabled == null ? turnOn : enabled.equalsIgnoreCase("on") || enabled.equalsIgnoreCase("active") || enabled.equalsIgnoreCase("1") || enabled.equalsIgnoreCase("yes");

		ModuleBase module = null;
		for (ModuleBase m : aPerf.instance.modules) {
			if (m.isVisible() && m.getName().equalsIgnoreCase(name)) {
				module = m;
				break;
			}
		}

		if (module == null) {
			msg(sender, "Module not found", EnumChatFormatting.RED);
		} else if (module.getName() == "PacketLimiter") {
			try {
				net.minecraft.network.TcpConnection.class.getMethod("getPacketHandlers");
			} catch (NoSuchMethodException e) {
				msg(sender, "Packet Limiter is disabled. Please tell your server admin.", EnumChatFormatting.RED);
			} catch (SecurityException e) {
				msg(sender, "Packet Limiter is disabled. Please tell your server admin.", EnumChatFormatting.RED);
			}
		} else {
			aPerf.instance.setAutoLoad(module, turnEnabled);

			if (turnOn) {
				module.enable();
			} else {
				module.disable();
			}

			list(plugin, sender, null);
		}
	}

	@Command(name = "aperf", syntax = "(?:module|m) (?:reload|r) <name>", description = "Reloads the specified module, including it's config", permission = "aperf.cmd.module.reload")
	public void reload(Object plugin, ICommandSender sender, Map<String, String> args) {
		String name = args.get("name");

		ModuleBase module = null;
		for (ModuleBase m : aPerf.instance.modules) {
			if (m.isVisible() && m.getName().equalsIgnoreCase(name)) {
				module = m;
				break;
			}
		}

		if (module == null) {
			msg(sender, "Module not found", EnumChatFormatting.RED);
		} else {
			aPerf.instance.loadConfig();

			module.disable();
			module.enable();

			msg(sender, "Module reloaded", EnumChatFormatting.GREEN);
		}
	}
}
