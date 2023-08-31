package tech.baisi.mc.echo.paper;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import tech.baisi.mc.echo.paper.Listeners.PlayerPreLogin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

public class Entrance extends JavaPlugin {
    public RabbitMQManager rabbitMQManager;
    public static Map<String,String> playerKeys = new HashMap<>();
    private Timer getPlayerKeysTimer = new Timer();
    private Timer addMoneyTimer = new Timer();
    public MySQLManager mySQLManager;
    private MoneyManager moneyManager;
    @Override
    public void onEnable() {
        //
        Bukkit.getLogger().info("Baisi-MC-Echo-Paper: OK.");

        //rabbitmq
        rabbitMQManager = new RabbitMQManager(this);
        try {
            rabbitMQManager.init();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }

        //sql
        mySQLManager = new MySQLManager();
        mySQLManager.init();
        getPlayerKeysTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                playerKeys = mySQLManager.getPlayerKeys();
            }
        },5000,2000);

        //register
        Bukkit.getPluginManager().registerEvents(new PlayerPreLogin(), this);

        //task
        moneyManager = new MoneyManager(mySQLManager);
        addMoneyTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                moneyManager.checkActive();
            }
        },300000,300000);
    }

    @Override
    public void onDisable() {
        //
    }
}

