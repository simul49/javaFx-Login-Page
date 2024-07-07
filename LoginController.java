package WeeklyReport_01;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import java.sql.*;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class LoginController {

    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;

    @FXML
    private void handleLogin() {
        String username = usernameInput.getText();
        String password = passwordInput.getText();

        if (validateLogin(username, password)) {
            sendEmail(username);
            showAlert(AlertType.INFORMATION, "Login Successful Mr JavaFx",
                    "Mission Done! An email has been sent to your account.");
        } else {
            showAlert(AlertType.ERROR, "Login Failed, Mr JavaFx",
                    "Mission Failed. Hey Java Coder, you gave me the wrong code, try again");
        }
    }

    private boolean validateLogin(String username, String password) {
        String url = "jdbc:mysql://localhost:3306/mydb1?serverTimezone=UTC";
        String user = "root";
        String pass = "paradox49"; // Replace with your MySQL password

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void sendEmail(String username) {
        final String fromEmail = "paradoxicalsimul@gmail.com";
        final String emailPassword = "41**********4";// Replace with your email password
        final String toEmail = "simul4690@gmail.com"; // Replace with recipient's email

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, emailPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Login Successful Mr/Ms Coder");
            message.setText("User " + username + " has successfully logged in.");

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
