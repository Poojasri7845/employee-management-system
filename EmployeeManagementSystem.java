import java.io.*;
import java.sql.*;
import com.sun.net.httpserver.*;

public class EmployeeManagementSystem {
    private static final String URL = "jdbc:mysql://localhost:3306/employee_management";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/addEmployee", new AddEmployeeHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port 8000...");
    }

    static class AddEmployeeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response;
            try {
                if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                    InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
                    BufferedReader br = new BufferedReader(isr);
                    String requestBody = br.readLine();
                    isr.close();
                    br.close();
                    String[] data = requestBody.split("&");
                    String name = data[0].split("=")[1];
                    String email = data[1].split("=")[1];
                    String phone = data[2].split("=")[1];
                    String department = data[3].split("=")[1];
                    double salary = Double.parseDouble(data[4].split("=")[1]);

                    addEmployee(name, email, phone, department, salary);
                    response = "{\"message\": \"Employee added successfully.\"}";
                } else {
                    response = "{\"message\": \"Invalid HTTP method.\"}";
                }
            } catch (Exception e) {
                response = "{\"message\": \"Error adding employee: " + e.getMessage() + "\"}";
                e.printStackTrace();
            }

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private static void addEmployee(String name, String email, String phone, String department, double salary) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "INSERT INTO employees (name, email, phone, department, salary) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, name);
                pstmt.setString(2, email);
                pstmt.setString(3, phone);
                pstmt.setString(4, department);
                pstmt.setDouble(5, salary);
                pstmt.executeUpdate();
            }
        }
    }
}
