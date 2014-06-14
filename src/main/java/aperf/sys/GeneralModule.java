package aperf.sys;

public class GeneralModule extends ModuleBase {
	public static GeneralModule instance = new GeneralModule();

	public GeneralModule() {
		addCommand(new aperf.sys.cmd.Help());
		addCommand(new aperf.sys.cmd.Status());
		addCommand(new aperf.sys.cmd.Module());
		addCommand(new aperf.sys.cmd.Mod());

		visible = false;
	}
}
