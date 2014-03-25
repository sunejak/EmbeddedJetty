package com.ntnu.sune.resource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("serial")
public class DumpServletB extends HttpServlet
{
    private static Integer cnt;
    public DumpServletB()
    {
        cnt = 0;
    }

    public DumpServletB(int n)
    {
        cnt = n;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>DumpServletB</h1><pre>");
        response.getWriter().println("requestURI=" + request.getRequestURI());
        response.getWriter().println("contextPath=" + request.getContextPath());
        response.getWriter().println("servletPath=" + request.getServletPath());
        response.getWriter().println("pathInfo=" + request.getPathInfo());
        response.getWriter().println("session=" + request.getSession(true).getId());

        String r=request.getParameter("resource");
        if (r!=null)
            response.getWriter().println("resource("+r+")=" + getServletContext().getResource(r));

        response.getWriter().println("authType=" + request.getAuthType());

        response.getWriter().println("Free=" + Runtime.getRuntime().freeMemory());
        response.getWriter().println("Max=" + Runtime.getRuntime().maxMemory());
        response.getWriter().println("Total=" + Runtime.getRuntime().totalMemory());
        response.getWriter().println("count=" + cnt);
        synchronized (cnt) {
            cnt++;
        }
        response.getWriter().println("</pre>");
    }
}

