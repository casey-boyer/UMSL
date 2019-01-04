package edu.umsl.servlets;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet(
        name = "defaultServlet",
        urlPatterns = {"/default"},
        loadOnStartup = 1
)
public class DefaultServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //By default, go to the student info page.
        if (request.getSession().getAttribute("action") == null) {
            request.getRequestDispatcher("/WEB-INF/jsp/studentInfo.jsp")
                    .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        //By default, go to the student info page.
        if (request.getSession().getAttribute("action") == null) {
            request.getRequestDispatcher("/WEB-INF/jsp/studentInfo.jsp")
                    .forward(request, response);
        }
    }
}
