package aperf.sys.entity;

import java.util.HashSet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingSand;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.entity.projectile.EntityWitherSkull;
import aperf.sys.objects.EntityClassMapEntry;

public class EntityClassMap extends HashSet<EntityClassMapEntry<Class<? extends Entity>, String, String>> {
    private static final long serialVersionUID = 711211752844808663L;
    public static EntityClassMap instance = new EntityClassMap();

    public EntityClassMap() {
        put(Entity.class, "net.minecraft.entity", "Entity");
        put(EntityCreature.class, "net.minecraft.entity", "EntityCreature");
        put(EntityAgeable.class, "net.minecraft.entity", "EntityAgeable");
        put(EntityLiving.class, "net.minecraft.entity", "EntityLiving");
        put(EntityHanging.class, "net.minecraft.entity", "EntityHanging");
        put(EntityFlying.class, "net.minecraft.entity", "EntityFlying");

        put(EntityAmbientCreature.class, "net.minecraft.entity.passive", "EntityAmbientCreature");
        put(EntityTameable.class, "net.minecraft.entity.passive", "EntityTameable");
        put(EntityAnimal.class, "net.minecraft.entity.passive", "EntityAnimal");
        put(EntityBat.class, "net.minecraft.entity.passive", "EntityBat");
        put(EntityChicken.class, "net.minecraft.entity.passive", "EntityChicken");
        put(EntityCow.class, "net.minecraft.entity.passive", "EntityCow");
        put(EntityWaterMob.class, "net.minecraft.entity.passive", "EntityWaterMob");
        put(EntityWolf.class, "net.minecraft.entity.passive", "EntityWolf");
        put(EntityMooshroom.class, "net.minecraft.entity.passive", "EntityMooshroom");
        put(EntityOcelot.class, "net.minecraft.entity.passive", "EntityOcelot");
        put(EntityPig.class, "net.minecraft.entity.passive", "EntityPig");
        put(EntitySheep.class, "net.minecraft.entity.passive", "EntitySheep");
        put(EntitySquid.class, "net.minecraft.entity.passive", "EntitySquid");
        put(EntityVillager.class, "net.minecraft.entity.passive", "EntityVillager");

        put(EntityMob.class, "net.minecraft.entity.monster", "EntityMob");
        put(EntityGolem.class, "net.minecraft.entity.monster", "EntityGolem");
        put(EntitySnowman.class, "net.minecraft.entity.monster", "EntitySnowman");
        put(EntityIronGolem.class, "net.minecraft.entity.monster", "EntityIronGolem");
        put(EntityBlaze.class, "net.minecraft.entity.monster", "EntityBlaze");
        put(EntityCaveSpider.class, "net.minecraft.entity.monster", "EntityCaveSpider");
        put(EntityCreeper.class, "net.minecraft.entity.monster", "EntityCreeper");
        put(EntityEnderman.class, "net.minecraft.entity.monster", "EntityEnderman");
        put(EntityGhast.class, "net.minecraft.entity.monster", "EntityGhast");
        put(EntityGiantZombie.class, "net.minecraft.entity.monster", "EntityGiantZombie");
        put(EntityMagmaCube.class, "net.minecraft.entity.monster", "EntityMagmaCube");
        put(EntityPigZombie.class, "net.minecraft.entity.monster", "EntityPigZombie");
        put(EntitySilverfish.class, "net.minecraft.entity.monster", "EntitySilverfish");
        put(EntitySkeleton.class, "net.minecraft.entity.monster", "EntitySkeleton");
        put(EntitySlime.class, "net.minecraft.entity.monster", "EntitySlime");
        put(EntitySpider.class, "net.minecraft.entity.monster", "EntitySpider");
        put(EntityWitch.class, "net.minecraft.entity.monster", "EntityWitch");
        put(EntityZombie.class, "net.minecraft.entity.monster", "EntityZombie");

        put(EntityDragonPart.class, "net.minecraft.entity.boss", "EntityDragonPart");
        put(EntityDragon.class, "net.minecraft.entity.boss", "EntityDragon");
        put(EntityWither.class, "net.minecraft.entity.boss", "EntityWither");

        put(EntityWeatherEffect.class, "net.minecraft.entity.effect", "EntityWeatherEffect");
        put(EntityLightningBolt.class, "net.minecraft.entity.effect", "EntityLightningBolt");

        put(EntityItem.class, "net.minecraft.entity.item", "EntityItem");
        put(EntityEnderCrystal.class, "net.minecraft.entity.item", "EntityEnderCrystal");
        put(EntityBoat.class, "net.minecraft.entity.item", "EntityBoat");
        put(EntityFallingSand.class, "net.minecraft.entity.item", "EntityFallingSand");
        put(EntityMinecart.class, "net.minecraft.entity.item", "EntityMinecart");
        put(EntityTNTPrimed.class, "net.minecraft.entity.item", "EntityTNTPrimed");
        put(EntityEnderPearl.class, "net.minecraft.entity.item", "EntityEnderPearl");
        put(EntityExpBottle.class, "net.minecraft.entity.item", "EntityExpBottle");
        put(EntityEnderEye.class, "net.minecraft.entity.item", "EntityEnderEye");
        put(EntityFireworkRocket.class, "net.minecraft.entity.item", "EntityFireworkRocket");
        put(EntityXPOrb.class, "net.minecraft.entity.item", "EntityXPOrb");
        put(EntityPainting.class, "net.minecraft.entity.item", "EntityPainting");

        put(EntityArrow.class, "net.minecraft.entity.projectile", "EntityArrow");
        put(EntityFireball.class, "net.minecraft.entity.projectile", "EntityFireball");
        put(EntityFishHook.class, "net.minecraft.entity.projectile", "EntityFishHook");
        put(EntityLargeFireball.class, "net.minecraft.entity.projectile", "EntityLargeFireball");
        put(EntitySmallFireball.class, "net.minecraft.entity.projectile", "EntitySmallFireball");
        put(EntitySnowball.class, "net.minecraft.entity.projectile", "EntitySnowball");
        put(EntityThrowable.class, "net.minecraft.entity.projectile", "EntityThrowable");
        put(EntityEgg.class, "net.minecraft.entity.projectile", "EntityEgg");
        put(EntityPotion.class, "net.minecraft.entity.projectile", "EntityPotion");
        put(EntityWitherSkull.class, "net.minecraft.entity.projectile", "EntityWitherSkull");

        put(EntityPlayer.class, "net.minecraft.entity.player", "EntityPlayer");
        put(EntityPlayerMP.class, "net.minecraft.entity.player", "EntityPlayerMP");
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void put(Class<? extends Entity> clazz, String namespace, String name) {
        add(new EntityClassMapEntry(clazz, namespace, name));
    }

    public EntityClassMapEntry<Class<? extends Entity>, String, String> get(Class<? extends Entity> clazz) {
        for (EntityClassMapEntry<Class<? extends Entity>, String, String> e : this) {
            if (e.key.equals(clazz)) {
                return e;
            }
        }

        return null;
    }

    public Class<? extends Entity> classForType(String name) {
        for (EntityClassMapEntry<Class<? extends Entity>, String, String> e : this) {
            if ((e.mid + "." + e.value).equalsIgnoreCase(name)) {
                return e.key;
            }
        }

        return null;
    }
}
