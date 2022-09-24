package br.com.fabioestevam.sgm.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UsuarioDTO {
	
	private Long id;
	@NotBlank
	private String nome;
	@NotBlank
	@Email
	private String email;	
	private String telefone;
	@NotBlank
	private String senha;
	
}
