package br.com.fabioestevam.sgm.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

import br.com.fabioestevam.sgm.enums.RegraEnum;
import lombok.Data;

@Data
@Entity
public class Regra implements GrantedAuthority ,Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, unique = true)
	private RegraEnum name;

	@Override
	public String getAuthority() {
		return this.name.toString();
	}
	
	
	
}
