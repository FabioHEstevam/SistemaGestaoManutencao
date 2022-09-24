package br.com.fabioestevam.sgm.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fabioestevam.sgm.models.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	public Optional<Usuario> findByEmail(String email);
	
	public Optional<Usuario> findByTokenRecuperacaoSenha(String tokenRecuperacaoSenha);
	
}
