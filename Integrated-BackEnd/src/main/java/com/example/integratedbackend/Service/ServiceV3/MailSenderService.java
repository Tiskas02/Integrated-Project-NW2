package com.example.integratedbackend.Service.ServiceV3;

import com.example.integratedbackend.ErrorHandle.EmailSentFailedException;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.AccessRight;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Collab;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class MailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleMail(String inviteName, String email, String boardName, AccessRight accessRight, String boardId) throws MessagingException, UnsupportedEncodingException {
        String subject = inviteName + " has invited to be collaborator in " + boardName + " board ";
        String accepted = "https://intproj23.sit.kmutt.ac.th/nw2/board/" + boardId + "/collab/invitations/";
        String content = "<html>"
                + "<head>"
                + "<style>"
                + "body { font-family: 'Verdana', sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }"
                + ".email-container { max-width: 600px; margin: 30px auto; background: #ffffff; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); }"
                + ".email-header { background-color: #0066cc; padding: 20px; border-radius: 10px 10px 0 0; text-align: center; color: #ffffff; }"
                + ".email-body { padding: 20px; color: #333333; }"
                + ".email-body h2 { margin-top: 0; color: #0066cc; }"
                + ".email-body p { line-height: 1.6; }"
                + ".email-footer { text-align: center; padding: 15px; background: #f9f9f9; border-radius: 0 0 10px 10px; color: #777777; font-size: 12px; }"
                + ".cta-button { display: block; margin: 20px auto; padding: 12px 20px; background-color: #0066cc; color: #ffffff; text-align: center; border-radius: 5px; text-decoration: none; font-weight: bold; }"
                + ".cta-button:hover { background-color: #004999; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class=\"email-container\">"
                + "<div class=\"email-header\">"
                + "<h1>You’re Invited!</h1>"
                + "</div>"
                + "<div class=\"email-body\">"
                + "<h2>Hi there,</h2>"
                + "<p><strong>" + inviteName + "</strong> has invited you to collaborate on the board <strong>" + boardName + "</strong>.</p>"
                + "<p><strong>Access Rights:</strong> " + accessRight + "</p>"
                + "<p>Click the button below to accept the invitation and start collaborating:</p>"
                + "<a href='" + accepted + "' class=\"cta-button\">Accept Invitation</a>"
                + "<p>If you didn’t expect this invitation, you can safely ignore this email.</p>"
                + "</div>"
                + "<div class=\"email-footer\">"
                + "<p>Thank you,<br>The NW2 Team</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setFrom("intprojnw2@gmail.com", "Do not reply");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new EmailSentFailedException("Failed to send an email");
        }

    }
}
