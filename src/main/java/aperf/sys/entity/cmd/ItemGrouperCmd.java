package aperf.sys.entity.cmd;

import java.util.Map;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import aperf.commands.BaseCommand;
import aperf.commands.Command;
import aperf.commands.CommandException;
import aperf.sys.entity.ItemGrouperModule;

public class ItemGrouperCmd extends BaseCommand {
	@Command(name = "aperf", syntax = "(?:entity|e) (?:group|g)", description = "Shows the status of item grouper", permission = "aperf.cmd.entity.group.status")
	public void status(Object plugin, ICommandSender sender, Map<String, String> args) {
		ItemGrouperModule m = ItemGrouperModule.instance;

		msg(sender, "%sItem grouper status", EnumChatFormatting.DARK_GREEN);
		msg(sender, "%s-----------------------------------------------------", EnumChatFormatting.GRAY);

		msg(sender, "%sItem grouping     [%s1%s]: %s%3s%s, XP orb grouping      [%s2%s]: %s%3s", EnumChatFormatting.GREEN, EnumChatFormatting.DARK_PURPLE, EnumChatFormatting.GREEN, m.groupItems ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.RED, m.groupItems ? "on" : "off", EnumChatFormatting.GREEN, EnumChatFormatting.DARK_PURPLE, EnumChatFormatting.GREEN, m.groupExpOrbs ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.RED, m.groupExpOrbs ? "on" : "off");

		msg(sender, "%sMatch range       [%s3%s]: %s%s%s, Move to new location [%s4%s]: %s%3s", EnumChatFormatting.GREEN, EnumChatFormatting.DARK_PURPLE, EnumChatFormatting.GREEN, EnumChatFormatting.DARK_GREEN, m.matchRange, EnumChatFormatting.GREEN, EnumChatFormatting.DARK_PURPLE, EnumChatFormatting.GREEN, EnumChatFormatting.DARK_GREEN, m.moveToNewLocation ? "yes" : "no ");

		msg(sender, "%sLived for atleast [%s5%s]: %s%3d%s, Run every x'th tick  [%s6%s]: %s%3d", EnumChatFormatting.GREEN, EnumChatFormatting.DARK_PURPLE, EnumChatFormatting.GREEN, EnumChatFormatting.DARK_GREEN, m.livedAtleast, EnumChatFormatting.GREEN, EnumChatFormatting.DARK_PURPLE, EnumChatFormatting.GREEN, EnumChatFormatting.DARK_GREEN, m.skipForTicks);

		msg(sender, "%s-----------------------------------------------------", EnumChatFormatting.GRAY);
	}

	@Command(name = "aperf", syntax = "(?:entity|e) (?:group|g) (?:set|s) <property> <value>", description = "Set the grouping property [n] -> int/float/(yes/no)", permission = "aperf.cmd.entity.group.set")
	public void set(Object plugin, ICommandSender sender, Map<String, String> args) throws CommandException {
		int property = Integer.valueOf(args.get("property"));
		String value = args.get("value");

		ItemGrouperModule m = ItemGrouperModule.instance;
		if (property == 1) {
			m.groupItems = value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("1") || value.equalsIgnoreCase("on");
		} else if (property == 2) {
			m.groupExpOrbs = value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("1") || value.equalsIgnoreCase("on");
		} else if (property == 3) {
			m.matchRange = Double.valueOf(value);
		} else if (property == 4) {
			m.moveToNewLocation = value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("1") || value.equalsIgnoreCase("on");
		} else if (property == 5) {
			m.livedAtleast = Integer.valueOf(value);
		} else if (property == 6) {
			m.skipForTicks = Integer.valueOf(value);
		} else {
			throw new CommandException("Unknown ");
		}

		m.saveConfig();

		msg(sender, "%sProperty set", EnumChatFormatting.GREEN);
		status(plugin, sender, null);
	}
}
