package tech.baisi.mc.echo.paper;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Entrance extends JavaPlugin {
    public RabbitMQManager rabbitMQManager;
    @Override
    public void onEnable() {
        //
        Bukkit.getLogger().info("插件运行成功！");

        //测试
        rabbitMQManager = new RabbitMQManager();
        try {
            rabbitMQManager.init();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        //
    }
}

