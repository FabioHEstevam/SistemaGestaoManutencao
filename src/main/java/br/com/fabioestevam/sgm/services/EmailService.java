package br.com.fabioestevam.sgm.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import br.com.fabioestevam.sgm.enums.EmailStatusEnum;
import br.com.fabioestevam.sgm.models.Email;
import br.com.fabioestevam.sgm.repositories.EmailRepository;

@Service
public class EmailService {
	
	@Autowired
	EmailRepository emailRepository;
	
	@Autowired
	JavaMailSender emailSender;
	
	public void sendEmail(Email email) {
		email.setDate(LocalDateTime.now());
		
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(email.getEmailFrom());
			message.setTo(email.getEmailTo());
			message.setSubject(email.getSubject());
			message.setText(email.getText());
			emailSender.send(message);
			email.setStatus(EmailStatusEnum.SENT);
		}
		catch (MailException e) {
			email.setStatus(EmailStatusEnum.ERROR);
		}
		finally {
			emailRepository.save(email);
		}
		
		
	}
}
