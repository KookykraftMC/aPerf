package aperf.sys.tile;

import aperf.sys.ModuleBase;

public class TileEntityModule extends ModuleBase {
	public static TileEntityModule instance = new TileEntityModule();

	public TileEntityModule() {
		addCommand(new aperf.sys.tile.cmd.TileEntityList());

		visible = false;
	}
}
