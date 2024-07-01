import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class FinalProgram {

    public static void main(String[] args) {

        // Datos de conexión a la base de datos
        String DBurl = "jdbc:mysql://localhost:3306/geemarie";
        String DBusuario = "root";
        String DBpass = "";

        // Consulta SQL para seleccionar datos
        String sql = "SELECT * FROM mockdata";

        try {
            // Establecer conexión a la base de datos
            Connection conn = DriverManager.getConnection(DBurl, DBusuario, DBpass);
            // Preparar la consulta
            Statement st = conn.createStatement();

            // Ejecutar la consulta SQL (Donde solo encuentro APTO y VERDADERO)
            String query = "SELECT Nombre, Correo, Estado_Postulacion, Pendiente_Correo FROM mockdata " + "WHERE Estado_Postulacion = 'Apto' AND Pendiente_Correo = 'VERDADERO'";
            ResultSet resultado = st.executeQuery(query);

            // Imprimir los datos
            while (resultado.next()) {
                String nombre = resultado.getString("Nombre");
                String correo = resultado.getString("Correo");
                System.out.println("Nombre: " + nombre + ", Correo: " + correo); //Apelldo, puesto
            }
        }

        catch (SQLException e) {
            e.printStackTrace();
        }


        // Configuración del servidor SMTP y credentials

        String MAILhost = "smtp.gmail.com"; // Cambia esto al servidor SMTP que corresponda
        String MAILuser = "mariesdev@gmail.com"; // Cambia esto a tu dirección de correo
        String MAILpass = "cvtr sxvf ufnv nyye"; // Cambia esto a tu contraseña // ENV BUSCAR!

        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", MAILhost);
        props.put("mail.smtp.port", "587");


        // Creación de la sesión
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MAILuser, MAILpass);
            }
        });


        // Lista de destinatarios aptos (aquí puedes cargar dinámicamente)

        List<String> destinatarios = Arrays.asList(
                "mariesdev@gmail.com"
                // Agrega más destinatarios si es necesario
        );

        try {
            // Creación del mensaje
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MAILuser));
            message.setSubject("¡Felicidades pasaste a la siguiente etapa!");

            // Configurar destinatarios del mensaje
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("geraldinemarie.cp@gmail.com"));

            // Contenido del mensaje
            message.setText("Estamos interesados en tu talento");

            // Enviar mensaje
            Transport.send(message);
            System.out.println("Correo enviado con éxito!");
        }

        catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}

