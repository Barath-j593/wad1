import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class DatabaseServlet extends HttpServlet {

    private static final String URL = "jdbc:mysql://localhost:3306/student";
    private static final String USER = "root";
    // Change this to your MySQL password if needed.
    private static final String PASSWORD = "ssn1";

    // READ: Display all students
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter();
             Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = con.prepareStatement("SELECT * FROM student");
             ResultSet rs = ps.executeQuery()) {

            out.println("<!DOCTYPE html><html><head><title>Student CRUD</title></head><body>");
            out.println("<h2>Student CRUD</h2>");

            // CREATE form
            out.println("<h3>Add Student</h3>");
            out.println("<form method='post' action='students'>");
            out.println("<input type='hidden' name='action' value='create'>");
            out.println("Name: <input name='name' required>");
            out.println("Email: <input type='email' name='email' required>");
            out.println("<button type='submit'>Add</button>");
            out.println("</form><hr>");

            // READ table
            out.println("<h3>Student List</h3>");
            out.println("<table border='1' cellpadding='8'>");
            out.println("<tr><th>ID</th><th>Name</th><th>Email</th><th>Actions</th></tr>");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = escapeHtml(rs.getString("name"));
                String email = escapeHtml(rs.getString("email"));

                out.println("<tr>");
                out.println("<td>" + id + "</td>");
                out.println("<td>" + name + "</td>");
                out.println("<td>" + email + "</td>");
                out.println("<td>");

                // UPDATE form
                out.println("<form method='post' action='students'>");
                out.println("<input type='hidden' name='action' value='update'>");
                out.println("<input type='hidden' name='id' value='" + id + "'>");
                out.println("<input name='name' placeholder='New name' required>");
                out.println("<input type='email' name='email' placeholder='New email' required>");
                out.println("<button type='submit'>Update</button>");
                out.println("</form>");

                // DELETE form
                out.println("<form method='post' action='students' onsubmit=\"return confirm('Delete this student?')\">");
                out.println("<input type='hidden' name='action' value='delete'>");
                out.println("<input type='hidden' name='id' value='" + id + "'>");
                out.println("<button type='submit'>Delete</button>");
                out.println("</form>");

                out.println("</td></tr>");
            }

            out.println("</table></body></html>");

        } catch (SQLException e) {
            throw new ServletException("Could not load students. Check MySQL and database configuration.", e);
        }
    }

    // CREATE, UPDATE, DELETE
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        String sql;

        if ("create".equals(action)) {
            sql = "INSERT INTO student (name, email) VALUES (?, ?)";
        } else if ("update".equals(action)) {
            sql = "UPDATE student SET name = ?, email = ? WHERE id = ?";
        } else if ("delete".equals(action)) {
            sql = "DELETE FROM student WHERE id = ?";
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            return;
        }

        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = con.prepareStatement(sql)) {

            if ("create".equals(action)) {
                ps.setString(1, requiredParameter(request, "name"));
                ps.setString(2, requiredParameter(request, "email"));
            } else if ("update".equals(action)) {
                ps.setString(1, requiredParameter(request, "name"));
                ps.setString(2, requiredParameter(request, "email"));
                ps.setInt(3, parseId(request.getParameter("id")));
            } else {
                ps.setInt(1, parseId(request.getParameter("id")));
            }

            ps.executeUpdate();
            response.sendRedirect(request.getContextPath() + "/students");

        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid input");
        } catch (SQLException e) {
            throw new ServletException("Database operation failed. Check MySQL and database configuration.", e);
        }
    }

    private static int parseId(String value) {
        try {
            int id = Integer.parseInt(value);
            if (id <= 0) {
                throw new IllegalArgumentException("ID must be positive");
            }
            return id;
        } catch (NumberFormatException | NullPointerException e) {
            throw new IllegalArgumentException("Invalid ID", e);
        }
    }

    private static String requiredParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing " + name);
        }
        return value.trim();
    }

    private static String escapeHtml(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#x27;");
    }
}
