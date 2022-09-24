package br.com.fabioestevam.sgm.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fabioestevam.sgm.enums.ConviteStatusEnum;
import br.com.fabioestevam.sgm.exceptions.ConviteException;
import br.com.fabioestevam.sgm.models.Convite;
import br.com.fabioestevam.sgm.models.Usuario;
import br.com.fabioestevam.sgm.repositories.ConviteRepository;
import br.com.fabioestevam.sgm.repositories.UsuarioRepository;

@Service
public class ConviteService {
	
	@Autowired
	private ConviteRepository conviteRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public List<Convite> findAll(){
		return conviteRepository.findAll();
	}
	
	public Convite findById(Long id) {
		return conviteRepository.findById(id).orElseThrow(()->new ConviteException("Convite não encontrado"));
	}
	
	public Convite findByToken(String token) {
		Convite convite = conviteRepository.findByToken(token).orElseThrow(()->new ConviteException("Convite invalido"));
		if(!convite.isValido()) {
			throw new ConviteException("Convite invalido");
		}
		return convite;
	}
	
	public Convite create(Usuario responsavel, String email, String token) {
		
		if(usuarioRepository.findByEmail(email).isPresent()) {
			throw new ConviteException("Usuario já cadastrado.");
		}
		
		Convite convite = new Convite();
		convite.setResponsavel(responsavel);
		convite.setEmail(email);
		convite.setToken(token);
		convite.setData(LocalDateTime.now());
		convite.setStatus(ConviteStatusEnum.PENDING);
		return conviteRepository.save(convite);
	}
	
	public Convite update(Convite convite) {
		Convite conviteAtualizado = findById(convite.getId());
		conviteAtualizado.setEmail(convite.getEmail());
		conviteAtualizado.setToken(convite.getToken());
		conviteAtualizado.setData(convite.getData());
		conviteAtualizado.setStatus(convite.getStatus());
		return conviteRepository.save(convite);
	}
	
	public void delete(Long id) {
		findById(id);
		conviteRepository.deleteById(id);
	}
	
}
