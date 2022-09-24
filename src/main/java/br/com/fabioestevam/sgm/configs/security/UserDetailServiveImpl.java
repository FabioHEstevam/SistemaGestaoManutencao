package br.com.fabioestevam.sgm.configs.security;

import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.fabioestevam.sgm.models.Usuario;
import br.com.fabioestevam.sgm.repositories.UsuarioRepository;

@Service
@Transactional
public class UserDetailServiveImpl implements UserDetailsService{

	final UsuarioRepository usuarioRepository;

    UserDetailServiveImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("E-mail "+ username + "não encontrado."));
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.isEnabled(), usuario.isAccountNonExpired(), usuario.isCredentialsNonExpired(), usuario.isAccountNonLocked(), usuario.getAuthorities());
	}

}
