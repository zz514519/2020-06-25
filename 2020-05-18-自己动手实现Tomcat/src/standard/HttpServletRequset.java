package standard;

public interface HttpServletRequset extends ServletRequest{
    String getMethod();

    String getPath();

    String getParameter(String name);

    String getHeader(String name);
}
