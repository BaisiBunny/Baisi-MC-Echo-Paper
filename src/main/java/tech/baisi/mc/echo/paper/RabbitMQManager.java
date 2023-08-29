package tech.baisi.mc.echo.paper;

import com.rabbitmq.client.*;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQManager {
//    private RabbitMQManager rabbitMQManager;
    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Channel channel;
//    public RabbitMQManager getInstance(){
//        return this;
//    }

    public void init() throws IOException, TimeoutException {
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("e5.baisi.tech");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("Baisi");
        connectionFactory.setPassword("BaisiTech");
        connectionFactory.setVirtualHost("/mc");

        connection = connectionFactory.newConnection();

        channel = connection.createChannel();

        channel.basicConsume("mc_queue",true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //
                String message = new String(body);
                Bukkit.getLogger().info(message);
            }
        });
    }
}
