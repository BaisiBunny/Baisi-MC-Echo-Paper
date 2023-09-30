package tech.baisi.mc.echo.paper;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoneyManager {

    private final MySQLManager mySQLManager;
    private final Map<Player,Location> playerLocation = new HashMap<>();
    public MoneyManager(MySQLManager mySQLManager){
        this.mySQLManager = mySQLManager;
    }
    public void checkActive(){ //1小时 -> 1k硬币; 15min-250; 5min-250/3;
        List<Player> players = (List<Player>) Bukkit.getOnlinePlayers();
        if(players.isEmpty()){
//            Bukkit.getLogger().info("nop");
            return;
        }
//        Bukkit.getLogger().info("check");
        for(Player p : players){
            if(!playerLocation.containsKey(p)){
                playerLocation.put(p,p.getLocation());
//                Bukkit.getLogger().info("fail");
            }else {
                if(p.getLocation().getWorld() != playerLocation.get(p).getWorld()){
                    // give 250/3 coin
                    mySQLManager.addMoney(p.getName(),(int)(Math.random()*166.7));
                }
                else {
                    if(p.getLocation().distanceSquared(playerLocation.get(p)) >= 100){
                        // give 250/3 coin
                        mySQLManager.addMoney(p.getName(),(int)(Math.random()*166.7));
//                    Bukkit.getLogger().info("1");
                    }else{
                        // give 50/3 coin
                        mySQLManager.addMoney(p.getName(),(int)(Math.random()*33.4));
//                    Bukkit.getLogger().info("2");
                    }
                }

                playerLocation.replace(p,p.getLocation());
            }
        }
    }
}
