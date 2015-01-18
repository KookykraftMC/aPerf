package aperf.sys.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import aperf.aPerf;
import aperf.commands.BaseCommand;
import aperf.commands.Command;
import aperf.commands.CommandsManager.CommandBinding;
import aperf.sys.objects.SubFilter;
import forgeperms.api.ForgePermsAPI;

public class Help extends BaseCommand {
	private static int helpCommandsPerPage = 4;

	@Command(name = "aperf", syntax = "(?:help|h) [page]", description = "Command list", isPrimary = true, permission = "aperf.cmd.help.list")
	public void help(Object plugin, ICommandSender sender, Map<String, String> args) {
		String arg = args.get("page");

		List<CommandBinding> commands = getCommands(sender);
		int pages = (int) Math.ceil((double) commands.size() / (double) helpCommandsPerPage);

		int page = arg == null ? 1 : Integer.parseInt(arg);
		if (page < 1) {
			page = 1;
		} else if (page > pages) {
			page = pages;
		}

		msg(sender, "%saPerf commands. Page %s%s %sof %s%s", EnumChatFormatting.DARK_GREEN, EnumChatFormatting.DARK_PURPLE, page, EnumChatFormatting.DARK_GREEN, EnumChatFormatting.DARK_PURPLE, pages);
		msg(sender, "%s----------------------------------", EnumChatFormatting.GRAY);

		boolean showPerm = sender instanceof EntityPlayer ? ForgePermsAPI.permManager.canAccess(((EntityPlayer) sender).getDisplayName(), ((EntityPlayer) sender).worldObj.provider.getDimensionName(), "aperf.show.cmd.perm") : true;

		int i = 0;
		for (CommandBinding cmd : commands) {
			Command desc = cmd.getMethodAnnotation();
			i++;
			if (i <= (page - 1) * helpCommandsPerPage || i > page * helpCommandsPerPage) {
				continue;
			}

			String syntax = desc.syntax().replaceAll("\\(\\?\\:|\\)", "");

			msg(sender, "%s/%s %s", EnumChatFormatting.GREEN, desc.name(), syntax);

			for (String d : desc.description().split("\n")) {
				msg(sender, "%s   %s", EnumChatFormatting.YELLOW, d);
			}

			if (showPerm) {
				msg(sender, "%s   Perm: %s%s", EnumChatFormatting.GOLD, EnumChatFormatting.YELLOW, desc.permission());
			}
		}
		msg(sender, "%s----------------------------------", EnumChatFormatting.GRAY);
	}

	private List<CommandBinding> getCommands(ICommandSender user) {
		List<CommandBinding> commands = aPerf.instance.commandsManager.getCommands();
		ArrayList<CommandBinding> ret = new ArrayList<CommandBinding>();

		for (CommandBinding cmd : commands) {
			Command desc = cmd.getMethodAnnotation();
			if (desc.isPlayerOnly() && !(user instanceof EntityPlayer)) {
				continue;
			}
			if (user instanceof EntityPlayer && !ForgePermsAPI.permManager.canAccess(((EntityPlayer) user).getDisplayName(), ((EntityPlayer) user).worldObj.provider.getDimensionName(), desc.permission())) {
				continue;
			}

			ret.add(cmd);
		}

		return ret;
	}

	@Command(name = "aperf", syntax = "(?:filterhelp|fh)", description = "Filter syntax manual", permission = "aperf.cmd.help.filterhelp")
	public void filterHelp(Object plugin, ICommandSender sender, Map<String, String> args) {
		msg(sender, "%s--------- %sFilter%s --------------", EnumChatFormatting.GRAY, EnumChatFormatting.GOLD, EnumChatFormatting.GRAY);
		msg(sender, "%sA entity/tileentity filter is a way to specify which entitys should be \"hit\" for a specific action.", EnumChatFormatting.GREEN);
		msg(sender, "%sFor a safe list it means which entities should never be messed with, for a remove comamnd it means which should be removed, for a spawn limit it means for which entities the limit is applied to and which entities are being counted (if a count limit).", EnumChatFormatting.GREEN);
		msg(sender, "%sThe filter is a comma-seperated list of sub-filters. To get a \"hit\" from a filter all the sub-filters have to be hit.", EnumChatFormatting.GREEN);
		msg(sender, "%sThe sub-filter consist of a key (type) and a value. Specifying the same key'd subfilter twice is in most cases pointless as it contradicts itself. In the future there could be regex based searches where it could be useful to define the same key multiple times.", EnumChatFormatting.GREEN);
		msg(sender, "%sCurrently defined sub-filter types and their possible values:", EnumChatFormatting.GREEN);

		for (SubFilter.Type filter : SubFilter.Type.values()) {
			msg(sender, "%s[%s]", EnumChatFormatting.RED, filter.toString());
			msg(sender, "%s   Desc: %s", EnumChatFormatting.GOLD, filter.description);
			msg(sender, "%s   Values: %s", EnumChatFormatting.GOLD, filter.valueDesc);
		}

		msg(sender, "%s----------------------------------", EnumChatFormatting.GRAY);
	}
}
