package br.com.fabioestevam.sgm.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import br.com.fabioestevam.sgm.enums.ConviteStatusEnum;
import lombok.Data;

@Data
@Entity
public class Convite implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	private Usuario responsavel;
	@Column(nullable = false)
	private String email;
	@Column(nullable = false)
	private String token;
	
	private LocalDateTime data;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ConviteStatusEnum status;
	
	public boolean isValido() {

		if(this.getStatus()==ConviteStatusEnum.PENDING || LocalDateTime.now().isBefore(this.data.plus(7, ChronoUnit.DAYS))) {
			return true;
		}
		else {
			this.setStatus(ConviteStatusEnum.EXPIRED);
		}
		return false;
	}
}
