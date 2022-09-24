package br.com.fabioestevam.sgm.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fabioestevam.sgm.enums.RegraEnum;
import br.com.fabioestevam.sgm.models.Regra;

@Repository
public interface RegraRepository extends JpaRepository<Regra, Long>{
	
	public Optional<Regra> findByNome(RegraEnum nome);

}
