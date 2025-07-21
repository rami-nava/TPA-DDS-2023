package models.domain.Notificaciones;


import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;


public class ViaMailJavax implements AdapterViaMail{
  private String remitente = "disenioGrupo9@gmail.com";
  private String claveemail = "bwlmzwnlroykvjas"; //clave de aplicacion

  public void recibirNotificacion(String mensaje, String mailDestinatario, String asunto){
    Properties props = System.getProperties();
    props.put("mail.smtp.host", "smtp.gmail.com");  //El servidor SMTP de Google
    props.put("mail.smtp.user", remitente);
    props.put("mail.smtp.clave", claveemail);    //La clave de la cuenta
    props.put("mail.smtp.auth", "true");    //Usar autenticación mediante usuario y clave
    props.put("mail.smtp.starttls.enable", "true"); //Para conectar de manera segura al servidor SMTP
    props.put("mail.smtp.port", "587"); //El puerto SMTP seguro de Google

    Session session = Session.getDefaultInstance(props);
    MimeMessage message = new MimeMessage(session);

    try {
      message.setFrom(new InternetAddress(remitente));
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailDestinatario));   //Se podrían añadir varios de la misma manera
      message.setSubject(asunto);
      message.setText(mensaje);
      Transport transport = session.getTransport("smtp");
      transport.connect("smtp.gmail.com", remitente, claveemail);
      transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
      transport.close();
    }
    catch (MessagingException me) {
      me.printStackTrace();   //Si se produce un error
    }
  }
  public void enviarArchivo(String ruta, String mailDestinatario, String asunto){
    Properties props = System.getProperties();
    props.put("mail.smtp.host", "smtp.gmail.com");  //El servidor SMTP de Google
    props.put("mail.smtp.user", remitente);
    props.put("mail.smtp.clave", claveemail);    //La clave de la cuenta
    props.put("mail.smtp.auth", "true");    //Usar autenticación mediante usuario y clave
    props.put("mail.smtp.starttls.enable", "true"); //Para conectar de manera segura al servidor SMTP
    props.put("mail.smtp.port", "587"); //El puerto SMTP seguro de Google

    Session session = Session.getDefaultInstance(props);
    MimeMessage message = new MimeMessage(session);

    try {
      message.setFrom(new InternetAddress(remitente));
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailDestinatario));   //Se podrían añadir varios de la misma manera
      message.setSubject(asunto);

      BodyPart messageBodyPart = new MimeBodyPart();
      messageBodyPart.setText("La información se encuentra adjunta");
      Multipart multipart = new MimeMultipart();
      multipart.addBodyPart(messageBodyPart);

      //Archivo:
      messageBodyPart = new MimeBodyPart();
      FileDataSource source = new FileDataSource(ruta);
      messageBodyPart.setDataHandler(new DataHandler(source));
      messageBodyPart.setFileName(ruta);
      multipart.addBodyPart(messageBodyPart);

      message.setContent(multipart);

      Transport transport = session.getTransport("smtp");
      transport.connect("smtp.gmail.com", remitente, claveemail);
      transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
      transport.close();
    }
    catch (MessagingException me) {
      me.printStackTrace();   //Si se produce un error
    }
  }
}