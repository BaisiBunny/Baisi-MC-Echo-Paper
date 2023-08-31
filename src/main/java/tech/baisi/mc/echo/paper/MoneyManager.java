package tech.baisi.mc.echo.paper;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoneyManager {

    private MySQLManager mySQLManager;
    private Map<Player,Location> playerLocation = new HashMap<>();
    public MoneyManager(MySQLManager mySQLManager){
        this.mySQLManager = mySQLManager;
    }
    public void checkActive(){ //1小时 -> 1k硬币; 15min-250; 5min-250/3;
        List<Player> players = (List<Player>) Bukkit.getOnlinePlayers();
        if(players.isEmpty()){
            return;
        }
        for(Player p : players){
            if(!playerLocation.containsKey(p)){
                playerLocation.put(p,p.getLocation());
//                Bukkit.getLogger().info("new");
            }else {
//                Bukkit.getLogger().info("old");
                if(p.getLocation().distanceSquared(playerLocation.get(p)) >= 100){
//                    Bukkit.getLogger().info("yes");
                    if(Math.random() > 0.666){
//                        Bukkit.getLogger().info("yes2");
                        mySQLManager.addMoney(p.getName(),250);
                    }
//                    else {
//                        Bukkit.getLogger().info("no2");
//                    }
                }
//                else {
//                    Bukkit.getLogger().info("no");
//                }
                playerLocation.replace(p,p.getLocation());
            }
        }
    }
}
