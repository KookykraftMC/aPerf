package aperf.sys.entity.cmd;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import aperf.commands.BaseCommand;
import aperf.commands.Command;
import aperf.commands.CommandException;
import aperf.sys.entity.SpawnLimiterModule;
import aperf.sys.objects.Filter;
import aperf.sys.objects.SpawnLimit;

public class SpawnLimiterCmd extends BaseCommand {
	@Command(name = "aperf", syntax = "(?:entity|e) (?:spawn|s)", description = "Lists the spawn limits", permission = "aperf.cmd.entity.spawn.list")
	public void list(Object plugin, ICommandSender sender, Map<String, String> args) {
		String format = "%s%s | %s%-6s%s | %-8s | %-20s | %s";

		msg(sender, "%s-----------------------------------------------------", EnumChatFormatting.GRAY);
		if (!SpawnLimiterModule.instance.isEnabled()) {
			msg(sender, "%s%s is currently disabled. Use /ap m for more info", EnumChatFormatting.LIGHT_PURPLE, SpawnLimiterModule.instance.getName());
		}

		msg(sender, "%sExecute onChunkLoad: %s%s%s, normalSpawn: %s%s%s, otherSpawn: %s%s%s", EnumChatFormatting.GREEN, SpawnLimiterModule.instance.executeRulesOnChunkLoad ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.RED, SpawnLimiterModule.instance.executeRulesOnChunkLoad ? "on" : "off", EnumChatFormatting.GREEN,

		SpawnLimiterModule.instance.executeRulesOnNormalSpawning ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.RED, SpawnLimiterModule.instance.executeRulesOnNormalSpawning ? "on" : "off", EnumChatFormatting.GREEN,

		SpawnLimiterModule.instance.exetuteRulesOnOtherwiseSpawned ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.RED, SpawnLimiterModule.instance.exetuteRulesOnOtherwiseSpawned ? "on" : "off", EnumChatFormatting.GREEN);

		msg(sender, format, EnumChatFormatting.DARK_GREEN, "#", "", "Active", "", "Type", "Filter", "Options");
		msg(sender, "%s-----------------------------------------------------", EnumChatFormatting.GRAY);

		int i = 1;
		for (SpawnLimit limit : SpawnLimiterModule.instance.limits) {
			msg(sender, format, EnumChatFormatting.GREEN, i++, limit.on ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.RED, limit.on ? "on" : "off", EnumChatFormatting.GREEN, limit.type.name(), limit.filter.serializeDisplay(), limit.getDisplayOptions());
		}

		msg(sender, "%s-----------------------------------------------------", EnumChatFormatting.GRAY);
	}

	@Command(name = "aperf", syntax = "(?:entity|e) (?:spawn|s) (?:add|a) <type> <filter> [options] [active]", description = "Add a new spawn limit\n" + "Type: '/ap e s types' for more info\n" + "Filter: '/ap filterhelp' for more info\n" + "Options: Limit type specific options. '/ap e s types' for more info\n" + "Active: on|off\n", permission = "aperf.cmd.entity.spawn.add")
	public void add(Object plugin, ICommandSender sender, Map<String, String> args) throws Exception {
		SpawnLimit limit = null;

		try {
			limit = SpawnLimit.fromUserInput(args.get("type"), args.get("filter"), args.get("options"), args.get("active"));
		} catch (Exception e) {
			throw new CommandException(e.toString(), e);
		}

		SpawnLimiterModule.instance.limits.add(limit);
		SpawnLimiterModule.instance.saveConfig();

		msg(sender, "%sLimit added", EnumChatFormatting.GREEN);

		list(plugin, sender, null);
	}

	@Command(name = "aperf", syntax = "(?:entity|e) (?:spawn|s) (?:remove|delete|r|d) <index>", description = "Removes the specified entity limit", permission = "aperf.cmd.entity.spawn.remove")
	public void remove(Object plugin, ICommandSender sender, Map<String, String> args) {
		int index = Integer.parseInt(args.get("index")) - 1;

		SpawnLimit limit = SpawnLimiterModule.instance.limits.remove(index);
		SpawnLimiterModule.instance.saveConfig();

		if (limit != null) {
			msg(sender, "%sLimit removed", EnumChatFormatting.GREEN);
		} else {
			msg(sender, "%sLimit not found", EnumChatFormatting.YELLOW);
		}

		list(plugin, sender, null);
	}

	@Command(name = "aperf", syntax = "(?:entity|e) (?:spawn|s) (?:toggle|t) <index>", description = "Toggle the specified entity limit on/off", permission = "aperf.cmd.entity.spawn.toggle")
	public void toggle(Object plugin, ICommandSender sender, Map<String, String> args) {
		int index = Integer.parseInt(args.get("index")) - 1;

		SpawnLimit limit = SpawnLimiterModule.instance.limits.get(index);
		if (limit == null) {
			msg(sender, "%sLimit not found", EnumChatFormatting.YELLOW);
			return;
		}

		limit.on = !limit.on;

		SpawnLimiterModule.instance.saveConfig();
		msg(sender, "%sLimit toggled", EnumChatFormatting.GREEN);

		list(plugin, sender, null);
	}

	@Command(name = "aperf", syntax = "(?:entity|e) (?:spawn|s) (?:togglegeneral|tg) <key>", description = "Toggle 'chunkload', 'normal' or 'other' ruleset on/off", permission = "aperf.cmd.entity.spawn.togglegeneral")
	public void toggleGeneral(Object plugin, ICommandSender sender, Map<String, String> args) {
		String key = args.get("key");
		boolean done = false;

		if (key.equalsIgnoreCase("chunkload")) {
			SpawnLimiterModule.instance.executeRulesOnChunkLoad = !SpawnLimiterModule.instance.executeRulesOnChunkLoad;
			done = true;
		} else if (key.equalsIgnoreCase("normal")) {
			SpawnLimiterModule.instance.executeRulesOnNormalSpawning = !SpawnLimiterModule.instance.executeRulesOnNormalSpawning;
			done = true;
		} else if (key.equalsIgnoreCase("other")) {
			SpawnLimiterModule.instance.exetuteRulesOnOtherwiseSpawned = !SpawnLimiterModule.instance.exetuteRulesOnOtherwiseSpawned;
			done = true;
		}

		if (!done) {
			msg(sender, "%sConfig not found. Use: 'chunkload', 'normal' or 'other'", EnumChatFormatting.YELLOW);
		} else {
			msg(sender, "%sConfig toggled and saved", EnumChatFormatting.GREEN);
			SpawnLimiterModule.instance.saveConfig();
			list(plugin, sender, null);
		}
	}

	@Command(name = "aperf", syntax = "(?:entity|e) (?:spawn|s) (?:set|s) <index> <key> [value]", description = "Sets the option for the limit", permission = "aperf.cmd.entity.spawn.set")
	public void setOption(Object plugin, ICommandSender sender, Map<String, String> args) throws Exception {
		int index = Integer.parseInt(args.get("index")) - 1;

		SpawnLimit limit = SpawnLimiterModule.instance.limits.get(index);
		if (limit == null) {
			msg(sender, "%sLimit not found", EnumChatFormatting.YELLOW);
			return;
		}

		limit.updateOption(args.get("key"), args.get("value"));

		SpawnLimiterModule.instance.saveConfig();
		msg(sender, "%sLimit option set", EnumChatFormatting.GREEN);

		list(plugin, sender, null);
	}

	@Command(name = "aperf", syntax = "(?:entity|e) (?:spawn|s) types", description = "Displays the limiter types", permission = "aperf.cmd.entity.spawn.types")
	public void types(Object plugin, ICommandSender sender, Map<String, String> args) throws InstantiationException, IllegalAccessException {
		msg(sender, "%s---------------- %sTypes%s -------------------------", EnumChatFormatting.GRAY, EnumChatFormatting.GOLD, EnumChatFormatting.GRAY);
		for (SpawnLimit.Type t : SpawnLimit.Type.values()) {
			msg(sender, "%s%s", EnumChatFormatting.DARK_PURPLE, t.name());
			msg(sender, "%s%s", EnumChatFormatting.GRAY, t.description);

			Map<String, String> targs = t.handler.newInstance().getDefinedArguments();

			for (Entry<String, String> kv : targs.entrySet()) {
				boolean optional = kv.getKey().endsWith("?");
				String key = optional ? kv.getKey().substring(0, kv.getKey().length() - 1) : kv.getKey();

				msg(sender, "%s   %s%s - %s", optional ? EnumChatFormatting.YELLOW : EnumChatFormatting.GOLD, key, EnumChatFormatting.GRAY, kv.getValue() + (optional ? "(optional)" : ""));
			}
		}
		msg(sender, "%s-----------------------------------------------------", EnumChatFormatting.GRAY);
	}

	@Command(name = "aperf", syntax = "(?:entity|e) (?:spawn|s) debug [filter]", description = "Sends you info about the specific filtered entity spawning or despawning.\nLeave filter empty to cancel logging.", permission = "aperf.cmd.entity.spawn.debug")
	public void debug(Object plugin, ICommandSender sender, Map<String, String> args) throws Exception {
		String filter = args.get("filter");

		if (filter == null || filter.equals("")) {
			Filter f = SpawnLimiterModule.instance.eventLoggers.remove(sender);

			if (f == null) {
				msg(sender, "%sYou aren't logging debug messages.", EnumChatFormatting.GREEN);
			} else {
				msg(sender, "%sStopped.", EnumChatFormatting.GREEN);
			}
		} else {
			Filter f = new Filter(args.get("filter"));

			SpawnLimiterModule.instance.eventLoggers.put(sender, f);
			msg(sender, "%sDebug logging started. Enter '/ap e s debug' to stop.", EnumChatFormatting.GREEN);
		}
	}
}
