package aperf.sys.entity.limits;

import net.minecraft.block.BlockLiquid;

import java.util.Map;

public class WaterAbove extends VerticalBlockComparer {
	public WaterAbove() {
		upwards = true;
		//blockToFind = Block.waterStill.blockID;
		blockToFind = BlockLiquid.getIdFromBlock(BlockLiquid.getBlockFromName("waterStill"));
	}

	@Override
	protected void load(Map<String, String> args) throws Exception {
		super.load(args);
		count = getInt(args, "count");
		max = getInt(args, "max", 0);
	}

	@Override
	protected void save(Map<String, String> args) {
		super.save(args);
		args.put("count", String.valueOf(count));
		if (max > 0) {
			args.put("max", String.valueOf(max));
		}
	}

	@Override
	protected void getArguments(Map<String, String> list) {
		list.put("count", "Integer. How many blocks has to be water from the mob.");
		list.put("max?", "Integer. How many blocks water above the required is the maximum. Used for surface fish.");
		super.getArguments(list);
	}
}
