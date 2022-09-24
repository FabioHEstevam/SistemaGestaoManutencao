package br.com.fabioestevam.sgm.resources;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.fabioestevam.sgm.dto.UsuarioDTO;
import br.com.fabioestevam.sgm.exceptions.ConviteException;
import br.com.fabioestevam.sgm.exceptions.UsuarioException;
import br.com.fabioestevam.sgm.models.Convite;
import br.com.fabioestevam.sgm.models.Email;
import br.com.fabioestevam.sgm.models.Usuario;
import br.com.fabioestevam.sgm.resources.utilities.Utilities;
import br.com.fabioestevam.sgm.services.ConviteService;
import br.com.fabioestevam.sgm.services.EmailService;
import br.com.fabioestevam.sgm.services.UsuarioService;
import net.bytebuddy.utility.RandomString;

@Controller
public class UsuarioController {

	@Autowired
	private EmailService emailService;

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ConviteService conviteService;
	
	@GetMapping("invite")
	public String invite() {
		return "invite";
	}

	@PostMapping("/invite")
	public String processInvite(HttpServletRequest request, Model model) {
		String email = request.getParameter("email");
		String token = RandomString.make(30);
		
		try {
			Usuario usuario = usuarioService.findByEmail(request.getRemoteUser());
			conviteService.create(usuario, email, token);
    		String conviteLink = Utilities.getSiteURL(request) + "/usuario/cadastro?token=" + token;
    		enviarConvite(usuario, email, conviteLink);
    		model.addAttribute("message", "Um convite foi enviado para o e-mail informado.");
    	}
    	catch(UsuarioException e) {
    		model.addAttribute("error", e.getMessage());
    	}
		catch(ConviteException e) {
			model.addAttribute("error", e.getMessage());
		}
    	catch(Exception e) {
    		model.addAttribute("error", "Erro ao enviar o convite, favor tentar novamente mais tarde.");
    	}
		
		return("invite");
    }
	
	@GetMapping("/usuario/cadastro")
    public String invite(@Param(value="token") String token, UsuarioDTO usuario, Model model) {
    	try {
    		Convite convite = conviteService.findByToken(token);
    		model.addAttribute("token",token);
    		usuario.setEmail(convite.getEmail());
    		
    	}
    	catch(UsuarioException e) {
    		model.addAttribute("error", e.getMessage());
    	}
    	
    	return "cadastro-usuario";
    }
	
	@PostMapping("/usuario/cadastro")
	public String cadatroUsuario(HttpServletRequest request, Model model, @Valid UsuarioDTO usuario, Errors errors) {
				
		if(errors.hasErrors()) {
			return "cadastro-usuario";
		}
		else {
			try {
			String token = request.getParameter("token");
			usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));
			usuarioService.create(usuarioService.fromDTO(usuario), token);
			model.addAttribute("message", "Cadastro realizado com sucesso.");
			return "redirect:/login";
			}
			catch (ConviteException e) {
				model.addAttribute("error", e.getMessage());
				return "cadastro-usuario";
			}
		}
            
	}
	
	public void enviarConvite(Usuario usuario, String email, String link)  {
    	Email mensagem = new Email();
    	
    	mensagem.setEmailFrom("invite@sgm.com.br");
    	mensagem.setEmailTo(email);
    	mensagem.setSubject("noreply Convite para cadastramento");
    	mensagem.setText(
    			"Olá, \n\n"
    			+ "Você recebeu um convite de " + usuario.getNome() + " para utilizar o Sistema de Gestão de Manutenção\n\n"
    			+ "Clique no link abaixo para efetuar o cadastro e começar a usar o sistema: \n\n"
    			+ link + "\n\n"
				+ "Atenciosamente,\n"
				+ "Sistema de gestão da manutenção");
    	
    	emailService.sendEmail(mensagem);
    }
}
