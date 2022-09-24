package br.com.fabioestevam.sgm.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fabioestevam.sgm.models.Convite;

@Repository
public interface ConviteRepository extends JpaRepository<Convite, Long>{

	public Optional<Convite> findByEmail(String email);
	
	public Optional<Convite> findByToken(String token);
	
}
