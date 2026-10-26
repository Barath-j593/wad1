import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;

public class HelloServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        String name = request.getParameter("name");

        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<body>");
        out.println("<h1>Hello " + name + "</h1>");
        out.println("</body>");
        out.println("</html>");

        out.close();
    }
}