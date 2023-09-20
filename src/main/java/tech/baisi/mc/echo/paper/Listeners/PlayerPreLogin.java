package tech.baisi.mc.echo.paper.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import tech.baisi.mc.echo.paper.Entrance;
import tech.baisi.mc.echo.paper.MySQLManager;

public class PlayerPreLogin implements Listener {
    private MySQLManager mySQLManager;
    @EventHandler
    public void playerPreLoginEvent(AsyncPlayerPreLoginEvent event){
        String name = event.getName();
        String hostname = event.getHostname();

        if(!Entrance.playerKeys.containsKey(name)){
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,"请先在官网 mc.baisi.tech 注册账号。");
            return;
        }
        if(!hostname.contains(".mc.line")){
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,"请使用官网提供的专属IP。");
            return;
        }
        hostname = hostname.substring(0,hostname.indexOf(".mc.line"));
        if(!hostname.equals(Entrance.playerKeys.get(name))){
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,"这不是你的专属IP。");
            return;
        }

        //allow
        if(System.currentTimeMillis()-Entrance.start_time <= 10*60*1000){
            if(!Entrance.receivedCompensation.contains(name)){
                // add 500 coin
                mySQLManager.addMoney(name,500);
                Entrance.receivedCompensation.add(name);
            }
        }
    }

    public PlayerPreLogin(MySQLManager mySQLManager) {
        this.mySQLManager = mySQLManager;
    }
}
