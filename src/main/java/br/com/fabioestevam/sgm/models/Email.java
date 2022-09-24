package br.com.fabioestevam.sgm.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import br.com.fabioestevam.sgm.enums.EmailStatusEnum;
import lombok.Data;

@Data
@Entity
public class Email implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String emailFrom;
	private String emailTo;
	private String subject;
	@Column(columnDefinition = "TEXT")
	private String text;
	private LocalDateTime date;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private EmailStatusEnum status;
	
}
