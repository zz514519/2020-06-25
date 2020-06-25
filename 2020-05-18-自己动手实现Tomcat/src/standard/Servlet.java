package standard;

import java.io.IOException;

public interface Servlet {
    // Servlet 对象实例化之后，会调用
    void init();

    //每次处理请求时调用
    void process(ServletRequest req,ServletResponse resp )throws IOException;

    //Servlet 对象被销毁之前，调用
    void destroy();
}
