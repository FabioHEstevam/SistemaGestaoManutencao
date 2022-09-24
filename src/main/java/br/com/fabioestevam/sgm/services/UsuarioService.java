package br.com.fabioestevam.sgm.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.fabioestevam.sgm.exceptions.UsuarioException;
import br.com.fabioestevam.sgm.models.Usuario;
import br.com.fabioestevam.sgm.repositories.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public void updateTokenRecuperacaoSenha(String token, String email) throws RuntimeException{
		Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(()->new UsuarioException("Não foi encontrado usuario cadastrado com email " + email+ "."));
		usuario.setTokenRecuperacaoSenha(token);
		usuarioRepository.save(usuario);
	}
	
	public Usuario findByTokenRecuperacaoSenha(String token) {
		if(token==null||token=="") {
			new UsuarioException("Token invalido.");	
		}
		return usuarioRepository.findByTokenRecuperacaoSenha(token).orElseThrow(()->new UsuarioException("Token invalido."));
	}
	
	public void updateSenha(Usuario usuario, String senha) {
		usuario.setSenha(new BCryptPasswordEncoder().encode(senha));
		usuario.setTokenRecuperacaoSenha(null);
		usuarioRepository.save(usuario);
	}
}
