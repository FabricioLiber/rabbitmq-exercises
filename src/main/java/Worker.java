import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;


public class Worker {



    public static void main(String[] args) throws Exception {
        final String NOME_FILA = "task_queue";
        final String NOME_FILA_DURAVEL = "task_queue_durable";
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection conexao = connectionFactory.newConnection();
        Channel canal = conexao.createChannel();
        System.out.println("Fabrício está esperando");
        canal.basicQos(1);
        canal.queueDeclare(NOME_FILA_DURAVEL, true, false, false, null);
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String mensagem = new String (delivery.getBody (), "UTF-8");

            System.out.println ("[x] Recebido '" + mensagem + "'");
            try {
                doWork(mensagem);
            } finally {
                System.out.println ("[x] Fabrício Recebeu");
                canal.basicAck(delivery.getEnvelope(). getDeliveryTag(), false);
            }
        };

        // fila, noAck, callback, callback em caso de cancelamento (por exemplo, a fila foi deletada)
        canal.basicConsume(NOME_FILA_DURAVEL, false, deliverCallback, consumerTag -> {
            System.out.println("Cancelaram a fila: " + NOME_FILA_DURAVEL);
        });
    }

    private static void doWork(String task) {
        for (char ch: task.toCharArray ()) {
            if (ch == '.') {
                try {
                    Thread.sleep (1000);
                } catch (InterruptedException _ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}


