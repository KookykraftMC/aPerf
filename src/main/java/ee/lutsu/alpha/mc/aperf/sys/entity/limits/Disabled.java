package ee.lutsu.alpha.mc.aperf.sys.entity.limits;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import ee.lutsu.alpha.mc.aperf.sys.objects.SpawnLimit;

public class Disabled extends SpawnLimit {
    @Override
    public boolean canSpawn(Entity e, World world) {
        return false;
    }
}
