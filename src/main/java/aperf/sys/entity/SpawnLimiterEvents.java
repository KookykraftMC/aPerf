package aperf.sys.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.ChunkEvent;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class SpawnLimiterEvents {
	SpawnLimiterModule parent;

	public SpawnLimiterEvents(SpawnLimiterModule parent) {
		this.parent = parent;
	}

	@SubscribeEvent
	public void entityJoinWorld(EntityJoinWorldEvent ev) {
		if (!(ev.entity instanceof EntityLivingBase) || ev.entity instanceof EntityPlayer) {
			return;
		}

		if (!parent.canEntityJoinWorld((EntityLivingBase) ev.entity, ev.world)) {
			ev.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void livingSpawnEventCheckSpawn(LivingSpawnEvent.CheckSpawn ev) {
		if (!parent.canEntitySpawnNaturally(ev.entityLiving, ev.world)) {
			ev.setResult(Event.Result.DENY);
		}
	}

	@SubscribeEvent
	public void chunkLoaded(ChunkEvent.Load ev) {
		synchronized (parent.fullyLoadedChunks) {
			parent.fullyLoadedChunks.add(EntityHelper.getChunkHash(ev.getChunk()));
		}
		parent.checkChunkEntities(ev.getChunk());
	}

	@SubscribeEvent
	public void chunkUnloaded(ChunkEvent.Unload ev) {
		synchronized (parent.fullyLoadedChunks) {
			parent.fullyLoadedChunks.remove(EntityHelper.getChunkHash(ev.getChunk()));
		}
	}
}
