import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

public class StudentServlet extends HttpServlet {

    private static final String JDBC_URL = "jdbc:h2:~/advanced_servlet_db;AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASS = "";

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("org.h2.Driver");
            try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
                 Statement stmt = conn.createStatement()) {
                String sql = "CREATE TABLE IF NOT EXISTS students (" +
                             "id VARCHAR(50) PRIMARY KEY, " +
                             "name VARCHAR(100), " +
                             "department VARCHAR(100), " +
                             "subjects VARCHAR(255), " +
                             "registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
                stmt.execute(sql);
            }
        } catch (Exception e) {
            throw new ServletException("Failed to initialize database table", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String department = request.getParameter("department");
        String[] subjectsArr = request.getParameterValues("subjects");
        String subjects = (subjectsArr != null && subjectsArr.length > 0)
                ? String.join(", ", subjectsArr)
                : "None Selected";

        String message = "";
        boolean isSuccess = false;

        if (id == null || id.trim().isEmpty() || name == null || name.trim().isEmpty()) {
            message = "Error: Student ID and Name are required fields.";
        } else {
            id = id.trim();
            name = name.trim();
            department = (department != null) ? department.trim() : "Unassigned";

            try {
                Class.forName("org.h2.Driver");
                try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS)) {
                    // Check if student ID already exists in SQL table
                    boolean exists = false;
                    try (PreparedStatement check = conn.prepareStatement("SELECT COUNT(*) FROM students WHERE id = ?")) {
                        check.setString(1, id);
                        try (ResultSet rs = check.executeQuery()) {
                            if (rs.next() && rs.getInt(1) > 0) {
                                exists = true;
                            }
                        }
                    }

                    if (exists) {
                        try (PreparedStatement update = conn.prepareStatement(
                                "UPDATE students SET name = ?, department = ?, subjects = ? WHERE id = ?")) {
                            update.setString(1, name);
                            update.setString(2, department);
                            update.setString(3, subjects);
                            update.setString(4, id);
                            update.executeUpdate();
                            message = "Student record with ID " + id + " was successfully updated in the SQL database via JDBC!";
                            isSuccess = true;
                        }
                    } else {
                        try (PreparedStatement insert = conn.prepareStatement(
                                "INSERT INTO students (id, name, department, subjects) VALUES (?, ?, ?, ?)")) {
                            insert.setString(1, id);
                            insert.setString(2, name);
                            insert.setString(3, department);
                            insert.setString(4, subjects);
                            insert.executeUpdate();
                            message = "New student record successfully inserted into SQL database via JDBC!";
                            isSuccess = true;
                        }
                    }
                }
            } catch (Exception e) {
                message = "Database Error: " + e.getMessage();
                isSuccess = false;
            }
        }

        renderRecordsPage(response, out, message, isSuccess);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        renderRecordsPage(response, out, null, true);
    }

    private void renderRecordsPage(HttpServletResponse response, PrintWriter out, String statusMessage, boolean isSuccess) {
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("  <meta charset='UTF-8'>");
        out.println("  <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("  <title>SQL Database Records - Advanced Servlet</title>");
        out.println("  <style>");
        out.println("    * { box-sizing: border-box; }");
        out.println("    body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: #f3f4f6; margin: 0; padding: 24px; display: flex; justify-content: center; align-items: center; min-height: 100vh; }");
        out.println("    .card { background: #ffffff; border: 1px solid #e5e7eb; border-radius: 8px; box-shadow: 0 4px 6px rgba(0,0,0,0.05); width: 100%; max-width: 820px; padding: 24px; }");
        out.println("    .header { text-align: center; margin-bottom: 20px; }");
        out.println("    h2 { margin: 0 0 6px 0; font-size: 22px; color: #111827; }");
        out.println("    p.sub { margin: 0; font-size: 13px; color: #6b7280; }");
        out.println("    .alert { padding: 12px 16px; border-radius: 6px; font-size: 13px; margin-bottom: 18px; font-weight: 500; }");
        out.println("    .alert-success { background: #ecfdf5; border: 1px solid #a7f3d0; color: #065f46; }");
        out.println("    .alert-danger { background: #fef2f2; border: 1px solid #fecaca; color: #991b1b; }");
        out.println("    .table-wrap { overflow-x: auto; border: 1px solid #e5e7eb; border-radius: 6px; margin-bottom: 20px; }");
        out.println("    table { width: 100%; border-collapse: collapse; font-size: 13px; text-align: left; }");
        out.println("    th { background: #f9fafb; padding: 10px 14px; font-weight: 600; color: #374151; border-bottom: 1px solid #e5e7eb; }");
        out.println("    td { padding: 10px 14px; border-bottom: 1px solid #f3f4f6; color: #1f2937; }");
        out.println("    tr:last-child td { border-bottom: none; }");
        out.println("    tr:hover { background: #f9fafb; }");
        out.println("    .id-tag { font-family: monospace; font-weight: 600; color: #2563eb; background: #eff6ff; padding: 2px 6px; border-radius: 4px; }");
        out.println("    .badge { display: inline-block; background: #f3f4f6; color: #374151; font-size: 11px; padding: 2px 8px; border-radius: 4px; border: 1px solid #d1d5db; margin: 2px; }");
        out.println("    .empty { padding: 28px; text-align: center; color: #6b7280; font-size: 14px; }");
        out.println("    .actions { display: flex; gap: 12px; justify-content: center; }");
        out.println("    .btn { display: inline-block; text-decoration: none; padding: 10px 18px; font-size: 13px; font-weight: 600; border-radius: 6px; border: none; cursor: pointer; transition: background 0.15s; }");
        out.println("    .btn-primary { background: #2563eb; color: #ffffff; }");
        out.println("    .btn-primary:hover { background: #1d4ed8; }");
        out.println("    .btn-secondary { background: #f3f4f6; color: #374151; border: 1px solid #d1d5db; }");
        out.println("    .btn-secondary:hover { background: #e5e7eb; }");
        out.println("  </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("  <div class='card'>");
        out.println("    <div class='header'>");
        out.println("      <h2>Advanced Servlet - Student SQL Database</h2>");
        out.println("      <p class='sub'>Jakarta Servlet connected to SQL Database via JDBC</p>");
        out.println("    </div>");

        if (statusMessage != null && !statusMessage.isEmpty()) {
            String alertClass = isSuccess ? "alert-success" : "alert-danger";
            out.println("    <div class='alert " + alertClass + "'>" + statusMessage + "</div>");
        }

        out.println("    <div class='table-wrap'>");
        out.println("      <table>");
        out.println("        <thead>");
        out.println("          <tr>");
        out.println("            <th>ID</th>");
        out.println("            <th>Student Name</th>");
        out.println("            <th>Department</th>");
        out.println("            <th>Enrolled Subjects</th>");
        out.println("            <th>Registered Time</th>");
        out.println("          </tr>");
        out.println("        </thead>");
        out.println("        <tbody>");

        int count = 0;
        try {
            Class.forName("org.h2.Driver");
            try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT id, name, department, subjects, registered_at FROM students ORDER BY registered_at DESC")) {

                while (rs.next()) {
                    count++;
                    String sId = rs.getString("id");
                    String sName = rs.getString("name");
                    String sDept = rs.getString("department");
                    String sSubjects = rs.getString("subjects");
                    Timestamp regTime = rs.getTimestamp("registered_at");
                    String timeStr = (regTime != null) ? regTime.toString().substring(0, 19) : "-";

                    out.println("          <tr>");
                    out.println("            <td><span class='id-tag'>" + sId + "</span></td>");
                    out.println("            <td><strong>" + sName + "</strong></td>");
                    out.println("            <td>" + sDept + "</td>");
                    out.println("            <td>");
                    if (sSubjects != null && !sSubjects.isEmpty()) {
                        String[] parts = sSubjects.split(", ");
                        for (String p : parts) {
                            out.println("              <span class='badge'>" + p + "</span>");
                        }
                    } else {
                        out.println("              <span style='color:#9ca3af;'>-</span>");
                    }
                    out.println("            </td>");
                    out.println("            <td>" + timeStr + "</td>");
                    out.println("          </tr>");
                }
            }
        } catch (Exception e) {
            out.println("          <tr><td colspan='5' style='color:#dc2626; padding:12px;'>Error fetching records: " + e.getMessage() + "</td></tr>");
        }

        if (count == 0) {
            out.println("          <tr><td colspan='5' class='empty'>No students registered yet in the SQL database.</td></tr>");
        }

        out.println("        </tbody>");
        out.println("      </table>");
        out.println("    </div>");

        out.println("    <div class='actions'>");
        out.println("      <a href='index.html' class='btn btn-primary'>+ Register Another Student</a>");
        out.println("      <a href='register' class='btn btn-secondary'>Refresh Table</a>");
        out.println("    </div>");
        out.println("  </div>");
        out.println("</body>");
        out.println("</html>");
    }
}
