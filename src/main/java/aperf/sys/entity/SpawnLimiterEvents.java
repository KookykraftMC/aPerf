package aperf.sys.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.ChunkEvent;

public class SpawnLimiterEvents {
	SpawnLimiterModule parent;

	public SpawnLimiterEvents(SpawnLimiterModule parent) {
		this.parent = parent;
	}

	@ForgeSubscribe
	public void entityJoinWorld(EntityJoinWorldEvent ev) {
		if (!(ev.entity instanceof EntityLivingBase) || ev.entity instanceof EntityPlayer) {
			return;
		}

		if (!parent.canEntityJoinWorld((EntityLivingBase) ev.entity, ev.world)) {
			ev.setCanceled(true);
		}
	}

	@ForgeSubscribe
	public void livingSpawnEventCheckSpawn(LivingSpawnEvent.CheckSpawn ev) {
		if (!parent.canEntitySpawnNaturally(ev.entityLiving, ev.world)) {
			ev.setResult(Result.DENY);
		}
	}

	@ForgeSubscribe
	public void chunkLoaded(ChunkEvent.Load ev) {
		synchronized (parent.fullyLoadedChunks) {
			parent.fullyLoadedChunks.add(EntityHelper.getChunkHash(ev.getChunk()));
		}
		parent.checkChunkEntities(ev.getChunk());
	}

	@ForgeSubscribe
	public void chunkUnloaded(ChunkEvent.Unload ev) {
		synchronized (parent.fullyLoadedChunks) {
			parent.fullyLoadedChunks.remove(EntityHelper.getChunkHash(ev.getChunk()));
		}
	}
}
