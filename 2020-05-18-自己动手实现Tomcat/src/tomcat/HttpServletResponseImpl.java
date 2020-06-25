package tomcat;

import standard.HttpServletResponse;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HttpServletResponseImpl implements HttpServletResponse {

    private int status = 200;
    private Map<String,String> headers = new HashMap<>();
    private OutputStream bodyOutputStream = new ByteArrayOutputStream(8192);
    private PrintWriter bodyPrintWriter;
    private OutputStream socketOutputStream;

    public static HttpServletResponse build(OutputStream os) throws UnsupportedEncodingException {
        HttpServletResponseImpl resp = new HttpServletResponseImpl();
        resp.bodyOutputStream = new ByteArrayOutputStream(8192);
        resp.bodyPrintWriter = new PrintWriter
                (new OutputStreamWriter(resp.bodyOutputStream,"UTF-8")
        );
       resp.socketOutputStream = os;

       return resp;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public void setHeader(String name, String value) {
        headers.put(name,value);
    }

    @Override
    public void setContentType(String contentType) {
        setHeader("Content-Type",contentType);
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return bodyPrintWriter;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return bodyOutputStream;
    }
    public void send() throws IOException {
        //把用户写的内容，刷新到 body 中
        bodyPrintWriter.flush();

        //按照响应的格式发送

        //响应行
        // HTTP/1.0 200 OK
        socketOutputStream.write("HTTP/1.0 ".getBytes("UTF-8"));
        socketOutputStream.write(String.valueOf(status).getBytes("UTF-8"));
        socketOutputStream.write(' ');
        socketOutputStream.write("OK\r\n".getBytes("UTF-8"));

        //响应头
        //Content-Length 需要我们自己计算
        int contentLength = ((ByteArrayOutputStream)bodyOutputStream).size();
        setHeader("Content-Length",String.valueOf(contentLength));
        for(Map.Entry<String,String> entry : headers.entrySet()){
            String name = entry.getKey();
            String value = entry.getValue();
            socketOutputStream.write(name.getBytes("UTF-8"));
            socketOutputStream.write(": ".getBytes("UTF-8"));
            socketOutputStream.write(value.getBytes("UTF-8"));
            socketOutputStream.write("\r\n".getBytes("UTF-8"));
        }
        socketOutputStream.write("\r\n".getBytes("UTF-8"));

        //响应体
        ((ByteArrayOutputStream) bodyOutputStream).writeTo(socketOutputStream);

        //刷新
        socketOutputStream.flush();
    }
}
