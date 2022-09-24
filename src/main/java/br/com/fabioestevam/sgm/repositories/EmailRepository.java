package br.com.fabioestevam.sgm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fabioestevam.sgm.models.Email;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

}
