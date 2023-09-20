package tech.baisi.mc.echo.paper;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandManager implements CommandExecutor {
    public CommandManager(MySQLManager mySQLManager) {
        this.mySQLManager = mySQLManager;
    }

    private MySQLManager mySQLManager;
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0){
            return false;
        }
        if(args[0].equals("add") && args.length == 3){
            String name = args[1];
            int count = 0;
            try {
                count = Integer.parseInt(args[2]);
            }catch (Exception e){
                return false;
            }
            mySQLManager.addMoney(name,count);
            sender.sendMessage("Scuuess: "+name+" "+count);
            return true;
        }

        return false;
    }
}
