package aperf;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

import forgeperms.api.IChatManager;
import forgeperms.api.IEconomyManager;
import forgeperms.api.IPermissionManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.network.NetworkMod;
import aperf.commands.BaseCommand;
import aperf.commands.CmdPerf;
import aperf.commands.CommandsManager;
import aperf.sys.GeneralModule;
import aperf.sys.ModuleBase;
import aperf.sys.entity.EntityModule;
import aperf.sys.entity.EntitySafeListModule;
import aperf.sys.entity.ItemGrouperModule;
import aperf.sys.entity.SpawnLimiterModule;
import aperf.sys.packet.PacketManagerModule;
import aperf.sys.tile.TileEntityModule;


@Mod(modid = "aPerf", name = "aPerf", version = "@VERSION@.@BUILD_NUMBER@")
@NetworkMod(clientSideRequired = false, serverSideRequired = true)
public class aPerf {
    public static String MOD_NAME = "aPerf";
    public File configFile;
    public Configuration config;
    public CommandsManager commandsManager = new CommandsManager(this);
    public ModuleBase[] modules = new ModuleBase[] { GeneralModule.instance, EntityModule.instance, EntitySafeListModule.instance, TileEntityModule.instance, SpawnLimiterModule.instance, ItemGrouperModule.instance, PacketManagerModule.Instance };

    public IPermissionManager permManager;
    public IChatManager chatManager;
    public IEconomyManager economyManager;

    @Instance("aPerf")
    public static aPerf instance;

    public void initPermManager() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> forgePerms = Class.forName("forgeperms.ForgePerms");
        permManager = (IPermissionManager) forgePerms.getMethod("getPermissionManager").invoke(null);
    }

    public void initChatManager() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> forgePerms = Class.forName("forgeperms.ForgePerms");
        chatManager = (IChatManager) forgePerms.getMethod("getChatManager").invoke(null);
    }

    public void initEcoManager() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class<?> forgePerms = Class.forName("forgeperms.ForgePerms");
        economyManager = (IEconomyManager) forgePerms.getMethod("getEconomyManager").invoke(null);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent ev) {
        configFile = ev.getSuggestedConfigurationFile();
    }

    @EventHandler
    public void load(FMLInitializationEvent var1) {}

    @EventHandler
    public void modsLoaded(FMLServerStartedEvent var1) {
        try {
            initPermManager();
            initChatManager();
            initEcoManager();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        reload();

        for (ModuleBase m : modules) {
            for (BaseCommand cmd : m.getCommands()) {
                commandsManager.register(cmd);
            }
        }

        ServerCommandManager mgr = (ServerCommandManager) MinecraftServer.getServer().getCommandManager();
        mgr.registerCommand(new CmdPerf());
    }

    public void loadConfig() {
        config = new Configuration(configFile, true);

        try {
            config.load();

            for (ModuleBase m : modules) {
                m.loadConfig();
            }
        } catch (Exception var8) {
            FMLLog.log(Level.SEVERE, var8, MOD_NAME + " was unable to load it's configuration successfully", new Object[0]);
            throw new RuntimeException(var8);
        } finally {
            config.save(); // re-save to add the missing configuration variables
        }
    }

    public boolean isEnabled(ModuleBase module) {
        return config.get("Modules", "Enable-" + module.getClass().getSimpleName(), module.getDefaultEnabled()).getBoolean(module.getDefaultEnabled());
    }

    public void setAutoLoad(ModuleBase module, boolean load) {
        config.get("Modules", "Enable-" + module.getClass().getSimpleName(), false).set(load);
        config.save();
    }

    protected void enableModules() {
        try {
            for (ModuleBase m : modules) {
                if (m.isVisible() && isEnabled(m)) {
                    m.enable();
                }
            }
        } finally {
            config.save();
        }
    }

    protected void disableModules() {
        for (ModuleBase m : modules) {
            if (m.isEnabled()) {
                m.disable();
            }
        }
    }

    public void reload() {
        loadConfig();

        try {
            disableModules();
            enableModules();
        } catch (Exception ex) {
            Log.severe("Load failed");
            throw new RuntimeException(ex.getMessage(), ex);
        }
        Log.info("Loaded");
    }
}
