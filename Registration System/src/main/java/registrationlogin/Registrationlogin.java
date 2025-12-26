package registrationlogin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.*;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = {"/UserAuthServlet", "/UserAuth"})
public class Registrationlogin extends HttpServlet {
	 private Connection connection = null;
	    private String Url = "jdbc:mysql://localhost:3306/user_auth_db";
	    private String Username = "root";
	    private String Password = "root";
	    
	   //init()- method
	    @Override
	    public void init() throws ServletException {
	        try {
	            
	            Class.forName("com.mysql.cj.jdbc.Driver");
	            
	            
	            connection = DriverManager.getConnection(Url, Username, Password);
	            
	            createUsersTable();
	           
	        } catch (ClassNotFoundException e) {
	            throw new ServletException("MySQL JDBC Driver not found!", e);
	        } catch (SQLException e) {
	            throw new ServletException("Failed to connect to database!", e);
	        }
	    }
	    
	    
	    private void createUsersTable() throws SQLException {
	        String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
	                               "id INT AUTO_INCREMENT PRIMARY KEY, " +
	                               "username VARCHAR(50) UNIQUE NOT NULL, " +
	                               "password VARCHAR(100) NOT NULL, " +
	                               "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
	                               ")";
	        
	        try (Statement stmt = connection.createStatement()) {
	            stmt.executeUpdate(createTableSQL);
	            System.out.println("Users table created/verified successfully!");
	        }
	    }
	    
	    // service() method 
	    @Override
	    public void service(HttpServletRequest request, HttpServletResponse response) 
	            throws ServletException, IOException {
	        
	        response.setContentType("text/html; charset=UTF-8");
	        response.setCharacterEncoding("UTF-8");
	        
	        String action = request.getParameter("action");
	        PrintWriter out = response.getWriter();
	        
	        out.println("<!DOCTYPE html>");
	        out.println("<html>");
	        out.println("<head>");
	        out.println("<title>User Authentication</title>");
	        out.println("<style>");
	        out.println("body { font-family: Arial, sans-serif; padding: 50px; background: #f0f0f0; }");
	        out.println(".container { background: white; padding: 30px; border-radius: 10px; width: 400px; margin: 0 auto; text-align: center; }");
	        out.println(".success { color: green; }");
	        out.println(".error { color: red; }");
	        out.println(".btn { background: blue; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; }");
	        out.println("</style>");
	        out.println("</head>");
	        out.println("<body>");
	        out.println("<div class='container'>");
	        
	        if ("register".equals(action)) {
	            handleRegistration(request, response, out);
	        } else if ("login".equals(action)) {
	            handleLogin(request, response, out);
	        } else {
	            out.println("<h2 class='error'>Invalid Action!</h2>");
	            out.println("<a href='Registration.html' class='btn'>Go to Registration</a>");
	        }
	        
	        out.println("</div>");
	        out.println("</body>");
	        out.println("</html>");
	        
	        out.close();
	    }
	    
	    
	     // Handles user registration
	     
	    private void handleRegistration(HttpServletRequest request, HttpServletResponse response, PrintWriter out) 
	            throws IOException {
	        String username = request.getParameter("username");
	        String password = request.getParameter("password");
	        
	        if (username == null || username.trim().isEmpty() || 
	            password == null || password.trim().isEmpty()) {
	            out.println("<h2 class='error'>All fields are required!</h2>");
	            out.println("<a href='Registration.html' class='btn'>Try Again</a>");
	            return;
	        }
	        
	        if (password.length() < 6) {
	            out.println("<h2 class='error'>Password must be at least 6 characters!</h2>");
	            out.println("<a href='Registration.html' class='btn'>Try Again</a>");
	            return;
	        }
	        
	        try {
	            // Check if user name already exists
	            String checkSQL = "SELECT COUNT(*) FROM users WHERE username = ?";
	            try (PreparedStatement checkStmt = connection.prepareStatement(checkSQL)) {
	                checkStmt.setString(1, username);
	                ResultSet rs = checkStmt.executeQuery();
	                rs.next();
	                if (rs.getInt(1) > 0) {
	                    out.println("<h2 class='error'>Username already exists!</h2>");
	                    out.println("<a href='register.html' class='btn'>Try Different Username</a>");
	                    return;
	                }
	            }
	            
	           
	            String insertSQL = "INSERT INTO users (username, password) VALUES (?, ?)";
	            try (PreparedStatement insertStmt = connection.prepareStatement(insertSQL)) {
	                insertStmt.setString(1, username);
	                insertStmt.setString(2, password); 
	                int rowsAffected = insertStmt.executeUpdate();
	                
	                if (rowsAffected > 0) {
	                    out.println("<h2 class='success'>Registration Successful!</h2>");
	                    out.println("<p>Welcome " + username + "! Your account has been created.</p>");
	                    out.println("<a href='Login.html' class='btn'>Login Now</a>");
	                } else {
	                    out.println("<h2 class='error'>Registration Failed!</h2>");
	                    out.println("<a href='Registration.html' class='btn'>Try Again</a>");
	                }
	            }
	            
	        } catch (SQLException e) {
	            out.println("<h2 class='error'>Database Error: " + e.getMessage() + "</h2>");
	            out.println("<a href='Registration.html' class='btn'>Try Again</a>");
	            e.printStackTrace();
	        }
	    }
	    
	    // Handles user login
	     
	    private void handleLogin(HttpServletRequest request, HttpServletResponse response, PrintWriter out) 
	            throws IOException {
	        String username = request.getParameter("username");
	        String password = request.getParameter("password");
	        
	        if (username == null || username.trim().isEmpty() || 
	            password == null || password.trim().isEmpty()) {
	            out.println("<h2 class='error'>All fields are required!</h2>");
	            out.println("<a href='Login.html' class='btn'>Try Again</a>");
	            return;
	        }
	        
	        try {
	            // Verify user credentials
	            String loginSQL = "SELECT * FROM users WHERE username = ? AND password = ?";
	            try (PreparedStatement loginStmt = connection.prepareStatement(loginSQL)) {
	                loginStmt.setString(1, username);
	                loginStmt.setString(2, password);
	                ResultSet rs = loginStmt.executeQuery();
	                
	                if (rs.next()) {
	                    
	                    HttpSession session = request.getSession();
	                    session.setAttribute("username", username);
	                    session.setAttribute("userId", rs.getInt("id"));
	                    
	                    
	                    response.sendRedirect("Welcome.jsp");
	                    return;
	                } else {
	                    out.println("<h2 class='error'>Invalid username or password!</h2>");
	                    out.println("<a href='Login.html' class='btn'>Try Again</a>");
	                }
	            }
	            
	        } catch (SQLException e) {
	            out.println("<h2 class='error'>Database Error: " + e.getMessage() + "</h2>");
	            out.println("<a href='Login.html' class='btn'>Try Again</a>");
	            e.printStackTrace();
	        }
	    }
	    
	    // destroy() method - Servlet Lifecycle Method 3
	     
	    @Override
	    public void destroy() {
	        try {
	            if (connection != null && !connection.isClosed()) {
	                connection.close();
	                System.out.println("Database connection closed successfully!");
	            }
	        } catch (SQLException e) {
	            System.err.println("Error closing database connection: " + e.getMessage());
	        }
	    }
}
