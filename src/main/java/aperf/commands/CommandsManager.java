package aperf.commands;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import aperf.Log;
import aperf.aPerf;

import com.google.common.base.Joiner;

public class CommandsManager {
	protected Map<String, Map<CommandSyntax, CommandBinding>> listeners = new LinkedHashMap<String, Map<CommandSyntax, CommandBinding>>();
	protected Object plugin;
	protected List<Object> helpObjects = new LinkedList<Object>();

	public CommandsManager(Object plugin) {
		this.plugin = plugin;
	}

	public void register(BaseCommand listener) {
		for (Method method : listener.getClass().getMethods()) {
			if (!method.isAnnotationPresent(Command.class)) {
				continue;
			}

			Command cmdAnnotation = method.getAnnotation(Command.class);

			Map<CommandSyntax, CommandBinding> commandListeners = listeners.get(cmdAnnotation.name());
			if (commandListeners == null) {
				commandListeners = new LinkedHashMap<CommandSyntax, CommandBinding>();
				listeners.put(cmdAnnotation.name(), commandListeners);
			}

			commandListeners.put(new CommandSyntax(cmdAnnotation.syntax(), cmdAnnotation.isPlayerOnly()), new CommandBinding(listener, method));
		}

		listener.onRegistered(this);
	}

	public boolean execute(ICommandSender sender, CommandBase command, String[] args) {
		Map<CommandSyntax, CommandBinding> callMap = listeners.get(command.getCommandName());

		if (callMap == null) {
			return false;
		}

		CommandBinding selectedBinding = null;
		int argumentsLength = args.length;
		String arguments = Joiner.on(" ").join(args);

		for (Entry<CommandSyntax, CommandBinding> entry : callMap.entrySet()) {
			CommandSyntax syntax = entry.getKey();
			if (syntax.playerNeeded && !(sender instanceof EntityPlayer)) {
				continue;
			}

			if (!syntax.isMatch(arguments)) {
				continue;
			}

			if (selectedBinding != null && syntax.getRegexp().length() < argumentsLength) {
				// but
				// there
				// already
				// more
				// fitted
				// variant
				continue;
			}

			CommandBinding binding = entry.getValue();
			binding.setParams(syntax.getMatchedArguments(arguments));
			selectedBinding = binding;
		}

		if (selectedBinding == null) // there is fitting handler
		{
			sender.sendChatToPlayer(ChatMessageComponent.createFromText(EnumChatFormatting.RED + "Error in command syntax. Check command help."));
			return true;
		}

		// Check permission
		if (sender instanceof EntityPlayer) // this method are not public and
											// required permission
		{
			if (!selectedBinding.checkPermissions((EntityPlayer) sender)) {
				Log.warning("User §4" + ((EntityPlayer) sender).username + " §etried to access chat command \"" + command.getCommandName() + " " + arguments + "\", but §4doesn't have permission §eto do this.");
				sender.sendChatToPlayer(ChatMessageComponent.createFromText(EnumChatFormatting.RED + "Sorry, you don't have enough permissions."));
				return true;
			}
		}

		try {
			selectedBinding.call(plugin, sender, selectedBinding.getParams());
		} catch (Throwable e) {
			if (e instanceof java.lang.reflect.InvocationTargetException) {
				e = ((java.lang.reflect.InvocationTargetException) e).getCause();
			}

			if (e instanceof CommandException) {
				sender.sendChatToPlayer(ChatMessageComponent.createFromText(EnumChatFormatting.RED + "Command error: " + e.getMessage()));
			} else {
				Log.severe("There is bogus command handler for " + command.getCommandName() + " command.", e);
				sender.sendChatToPlayer(ChatMessageComponent.createFromText(EnumChatFormatting.RED + "Command exception: " + e.getClass().getSimpleName() + " " + (e.getMessage() == null ? "" : e.getMessage())));
			}
		}

		return true;
	}

	public List<CommandBinding> getCommands() {
		List<CommandBinding> commands = new LinkedList<CommandBinding>();

		for (Map<CommandSyntax, CommandBinding> map : listeners.values()) {
			commands.addAll(map.values());
		}

		return commands;
	}

	protected class CommandSyntax {
		protected String originalSyntax;
		protected String regexp;
		protected List<String> arguments = new LinkedList<String>();
		public boolean playerNeeded = false;

		public CommandSyntax(String syntax, boolean playerNeeded) {
			originalSyntax = syntax;
			this.playerNeeded = playerNeeded;

			regexp = this.prepareSyntaxRegexp(syntax);
		}

		public String getRegexp() {
			return regexp;
		}

		private String prepareSyntaxRegexp(String syntax) {
			String expression = syntax;

			Matcher argMatcher = Pattern.compile("(?:[\\s]+)((\\<|\\[)([^\\>\\]]+)(?:\\>|\\]))").matcher(expression);

			int index = 0;
			while (argMatcher.find()) {
				if (argMatcher.group(2).equals("[")) {
					expression = expression.replace(argMatcher.group(0), "(?:(?:[\\s]+)(\"[^\"]+\"|[^\\s]+))?");
				} else {
					expression = expression.replace(argMatcher.group(1), "(\"[^\"]+\"|[\\S]+)");
				}

				arguments.add(index++, argMatcher.group(3));
			}

			return expression;
		}

		public boolean isMatch(String str) {
			return str.matches(regexp);
		}

		public Map<String, String> getMatchedArguments(String str) {
			Map<String, String> matchedArguments = new HashMap<String, String>(arguments.size());

			if (arguments.size() > 0) {
				Matcher argMatcher = Pattern.compile(regexp).matcher(str);

				if (argMatcher.find()) {
					for (int index = 1; index <= argMatcher.groupCount(); index++) {
						String argumentValue = argMatcher.group(index);
						if (argumentValue == null || argumentValue.isEmpty()) {
							continue;
						}

						if (argumentValue.startsWith("\"") && argumentValue.endsWith("\"")) { // Trim
																								// boundary
																								// colons
							argumentValue = argumentValue.substring(1, argumentValue.length() - 1);
						}

						matchedArguments.put(arguments.get(index - 1), argumentValue);
					}
				}
			}
			return matchedArguments;
		}
	}

	public class CommandBinding {
		protected Object object;
		protected Method method;
		protected Map<String, String> params = new HashMap<String, String>();

		public CommandBinding(Object object, Method method) {
			this.object = object;
			this.method = method;
		}

		public Command getMethodAnnotation() {
			return method.getAnnotation(Command.class);
		}

		public Map<String, String> getParams() {
			return params;
		}

		public void setParams(Map<String, String> params) {
			this.params = params;
		}

		public boolean checkPermissions(EntityPlayer player) {
			String permission = this.getMethodAnnotation().permission();

			if (permission.contains("<")) {
				for (Entry<String, String> entry : this.getParams().entrySet()) {
					if (entry.getValue() != null) {
						permission = permission.replace("<" + entry.getKey() + ">", entry.getValue().toLowerCase());
					}
				}
			}

			return aPerf.instance.permManager.canAccess(player.username, player.worldObj.provider.getDimensionName(), permission);
		}

		public void call(Object... args) throws Exception {
			method.invoke(object, args);
		}
	}
}
