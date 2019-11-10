import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class NewTask {



    public static void main(String[] args) throws Exception {
        final String NOME_FILA = "task_queue";
        final String NOME_FILA_DURAVEL = "task_queue_durable";
        ConnectionFactory connectionFactory = new ConnectionFactory();
        try {
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            String message = String.join ("", args);

            channel.basicPublish ("", "olá", null, message.getBytes ());
            System.out.println ("[x] Enviado '" + message + "'");

            //(queue, passive, durable, exclusive, autoDelete, arguments)
            channel.queueDeclare(NOME_FILA_DURAVEL, true, false, false, null);

            // ​(exchange, routingKey, mandatory, immediate, props, byte[] body)
            channel.basicPublish("", NOME_FILA_DURAVEL, false, false, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


