package tomcat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    private static final int PORT = 8081;
    public static void main(String[] args) throws IOException {
        //0.为了支持并发特性，使用线程池
        ExecutorService pool = Executors.newFixedThreadPool(8);

        //1.进行 TCP 监听
        try(ServerSocket serverSocket = new ServerSocket(PORT)){
            //2.不断进行 accept ，获取连接成功的连接
            while (true){
                Socket socket = serverSocket.accept();

                //进行业务处理
                //创建一个 HTTP 处理流程任务
                Task task = new Task(socket);
                pool.execute(task);
            }
        }
    }
}
