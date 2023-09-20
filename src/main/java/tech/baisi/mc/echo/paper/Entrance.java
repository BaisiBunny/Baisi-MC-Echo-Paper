package tech.baisi.mc.echo.paper;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import tech.baisi.mc.echo.paper.Listeners.PlayerPreLogin;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

public class Entrance extends JavaPlugin {
    private ScoreboardTask scoreboardTask;
    public RabbitMQManager rabbitMQManager;
    public static Map<String,String> playerKeys = new HashMap<>();
    private Timer getPlayerKeysTimer = new Timer();
    private Timer addMoneyTimer = new Timer();
    public MySQLManager mySQLManager;
    private MoneyManager moneyManager;
    public static long start_time;
    public static HashSet<String> receivedCompensation = new HashSet<>();

    public static int gdp = 0;

    @Override
    public void onEnable() {
        //
        Bukkit.getLogger().info("Baisi-MC-Echo-Paper: OK.");

        //start timestamp
        start_time = System.currentTimeMillis();

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
        Bukkit.getPluginManager().registerEvents(new PlayerPreLogin(mySQLManager), this);
        Bukkit.getPluginCommand("coin").setExecutor(new CommandManager(mySQLManager));

        //task
        moneyManager = new MoneyManager(mySQLManager);
        addMoneyTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                moneyManager.checkActive();
            }
        },300000,300000);

        scoreboardTask = new ScoreboardTask();
        scoreboardTask.init();
        scoreboardTask.start();
    }

    @Override
    public void onDisable() {

        //
        scoreboardTask.stop();
        getPlayerKeysTimer.cancel();
        addMoneyTimer.cancel();
    }
}

