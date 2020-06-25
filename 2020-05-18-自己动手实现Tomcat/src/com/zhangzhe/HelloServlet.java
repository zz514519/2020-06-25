package com.zhangzhe;

import standard.HttpServlet;
import standard.HttpServletRequset;
import standard.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class HelloServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequset req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain;charset=utf-8");
        PrintWriter writer = resp.getWriter();
        writer.println("你好世界");
    }
}
