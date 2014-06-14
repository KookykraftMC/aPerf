package aperf.commands;

public class CommandException extends Exception {
	private static final long serialVersionUID = -9074640277501611957L;

	public CommandException(String msg) {
		super(msg);
	}

	public CommandException(String msg, Exception e) {
		super(msg, e);
	}
}
