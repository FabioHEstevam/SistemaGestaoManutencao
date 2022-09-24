package br.com.fabioestevam.sgm.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.fabioestevam.sgm.dto.UsuarioDTO;
import br.com.fabioestevam.sgm.enums.ConviteStatusEnum;
import br.com.fabioestevam.sgm.enums.RegraEnum;
import br.com.fabioestevam.sgm.exceptions.UsuarioException;
import br.com.fabioestevam.sgm.models.Convite;
import br.com.fabioestevam.sgm.models.Usuario;
import br.com.fabioestevam.sgm.repositories.ConviteRepository;
import br.com.fabioestevam.sgm.repositories.RegraRepository;
import br.com.fabioestevam.sgm.repositories.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private RegraRepository regraRepository;
	
	@Autowired
	private ConviteRepository conviteRepository;
	
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
	
	public List<Usuario> findAll(){
		return usuarioRepository.findAll();
	}
	
	public Usuario findById(Long id) {
		return usuarioRepository.findById(id).orElseThrow(()->new UsuarioException("Usuario não encontrado"));
	}
	
	public Usuario findByEmail(String email) {
		return usuarioRepository.findByEmail(email).orElseThrow(()->new UsuarioException("Usuario não encontrado"));
	}
	
	public Usuario create(Usuario usuario, String token) {
		
		Convite convite = conviteRepository.findByToken(token).orElseThrow(()-> new UsuarioException("Convite invalido."));
		
		if(!convite.isValido()) {
			throw new UsuarioException("Convite invalido.");
		}
		
		usuario.getRegras().add(regraRepository.findByNome(RegraEnum.ROLE_USER).orElse(null));
		usuario = usuarioRepository.save(usuario);
		convite.setStatus(ConviteStatusEnum.ACCEPTED);
		conviteRepository.save(convite);
		
		return usuario;
	}
	
	public Usuario update(Usuario usuario) {
		Usuario usuarioAtualizado = findById(usuario.getId());
		
		usuarioAtualizado.setNome(usuario.getNome());
		usuarioAtualizado.setTelefone(usuario.getTelefone());
		
		return usuarioRepository.save(usuarioAtualizado);
	}
	
	public void delete(Long id) {
		if(findAll().size()>1) {
			findById(id);
			usuarioRepository.deleteById(id);
		}
	}
	
	public Usuario fromDTO(UsuarioDTO usuarioDTO) {
		Usuario usuario = new Usuario();
		usuario.setNome(usuarioDTO.getNome());
		usuario.setEmail(usuarioDTO.getEmail());
		usuario.setTelefone(usuarioDTO.getTelefone());
		usuario.setSenha(usuarioDTO.getSenha());
		return usuario;
	}
	
}
