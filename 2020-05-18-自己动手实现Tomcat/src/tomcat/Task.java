package tomcat;
import com.zhangzhe.HelloServlet;
import standard.HttpServlet;
import standard.HttpServletRequset;
import standard.HttpServletResponse;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Task implements Runnable{
    private final Socket socket;

    public Task(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        //1.读取客户端发来的数据，并按照 HTTP 协议请求格式进行解析，最终封装成 HttpServletRequest 对象
        try{
            InputStream is = socket.getInputStream();
            HttpServletRequset req = HttpServletRequestImpl.parse(is);

            //构建一个空的 HttpServletResponse 对象
            OutputStream os = socket.getOutputStream();
            HttpServletResponse resp =  HttpServletResponseImpl.build(os);

            //3.根据规则，选择合适的 HttpServlet 对其进行处理
            HttpServlet servlet = selectServlet(req);

            //4.调用 Servlet 的 process 方法，进行业务处理
            servlet.process(req,resp);          //所有，每次有请求过来，都会调用一次 process 方法

            //5.resp 对象已经被业务填充了正确的数据（状态/头信息/响应体）
            // 按照 HTTP 协议响应格式，构造响应数据并发送
            ((HttpServletResponseImpl)resp).send();

            //6.  HTTP/1.0 协议，短连接方式处理 --- 关闭 socket
            socket.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private HttpServlet servlet = new HelloServlet();
    private HttpServlet selectServlet(HttpServletRequset req) {
        return servlet;
    }
}
