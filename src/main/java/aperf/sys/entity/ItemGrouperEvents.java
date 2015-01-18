package aperf.sys.entity;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;

public class ItemGrouperEvents {
	ItemGrouperModule parent;
	int ticks = 0;

	public ItemGrouperEvents(ItemGrouperModule parent) {
		this.parent = parent;
	}

	@SubscribeEvent
	public void tickEnd(TickEvent.WorldTickEvent event) {
		if (event.phase != TickEvent.Phase.END || event.side != Side.SERVER) {
			return;
		}

		if (!parent.isEnabled()) {
			return;
		}

		if (ticks > 0) {
			ticks--;
			return;
		}

		World w = event.world;
		ArrayList<Entity> toRemove = new ArrayList<Entity>();
		ArrayList<Entity> toAdd = new ArrayList<Entity>();

		for (int i = 0; i < w.loadedEntityList.size(); i++) {
			Object o = w.loadedEntityList.get(i);
			if (parent.groupItems && o instanceof EntityItem) {
				parent.groupItem((EntityItem) o, w, toAdd, toRemove);
			} else if (parent.groupExpOrbs && o instanceof EntityXPOrb) {
				parent.groupExpOrb((EntityXPOrb) o, w, toAdd, toRemove);
			}
		}

		for (Entity e : toRemove) {
			EntityHelper.removeEntity(e);
		}
		for (Entity e : toAdd) {
			w.spawnEntityInWorld(e);
		}

		ticks = MinecraftServer.getServer().worldServers.length
				* parent.skipForTicks;
	}
}
