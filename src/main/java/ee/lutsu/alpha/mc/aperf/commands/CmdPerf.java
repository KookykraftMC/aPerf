package ee.lutsu.alpha.mc.aperf.commands;

import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

import com.sperion.forgeperms.ForgePerms;

import ee.lutsu.alpha.mc.aperf.aPerf;

public class CmdPerf extends CommandBase {
    @Override
    public List<?> getCommandAliases() {
        return Arrays.asList(new String[] { "ap", "alphaperf", "aperformance", "alphaperformance", "aP", "aPerf" });
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender cs) {
        if (cs instanceof EntityPlayerMP) {
            EntityPlayerMP p = (EntityPlayerMP) cs;
            return ForgePerms.getPermissionManager().canAccess(p.username, p.worldObj.provider.getDimensionName(), "aperf.cmd");
        }
        return false;
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender) {
        return "/" + getCommandName();
    }

    @Override
    public String getCommandName() {
        return "aperf";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        CommandsManager m = aPerf.instance.commandsManager;

        if (args.length > 0) {
            m.execute(sender, this, args);
        } else {
            m.execute(sender, this, new String[] { "status" });
        }
    }
}
