package br.com.fabioestevam.sgm.resources;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.fabioestevam.sgm.exceptions.UsuarioException;
import br.com.fabioestevam.sgm.models.Email;
import br.com.fabioestevam.sgm.models.Usuario;
import br.com.fabioestevam.sgm.services.EmailService;
import br.com.fabioestevam.sgm.services.UsuarioService;
import net.bytebuddy.utility.RandomString;

@Controller
public class HomeController {

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping("/")
    public String root() {
        return "index";
    }

    @GetMapping("/user")
    public String userIndex() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "/error/access-denied";
    }
    
    @GetMapping("/recovery")
    public String recovery() {
    	return "recovery";
    }
    
    @PostMapping("/recovery")
    public String processRecovery(HttpServletRequest request, Model model) {
    	String email = request.getParameter("email");
    	String token = RandomString.make(30);
    	
    	try {
    		usuarioService.updateTokenRecuperacaoSenha(token, email);
    		String resetLink = getSiteURL(request) + "/reset?token=" + token;
    		sendEmail(email, resetLink);
    		model.addAttribute("message", "O link para redefinição de senha foi encaminhado para o seu e-mail.");
    	}
    	catch(UsuarioException e) {
    		model.addAttribute("error", e.getMessage());
    	}
    	catch(Exception ex) {
    		model.addAttribute("error", "Erro ao enviar o e-mail, favor tentar novamente mais tarde.");
    	}
    	return "recovery";
    }
    
    @GetMapping("/reset")
    public String reset(@Param(value="token") String token, Model model) {
    	try {
    		usuarioService.findByTokenRecuperacaoSenha(token);
    		model.addAttribute("token",token);
    	}
    	catch(UsuarioException e) {
    		model.addAttribute("error", e.getMessage());
    	}
    	return "reset";
    }
    
    @PostMapping("/reset")
    public String processReset(HttpServletRequest request, Model model) {
    	try{
	    	String token = request.getParameter("token");
	    	String senha = request.getParameter("senha");
	    	
	    	Usuario usuario = usuarioService.findByTokenRecuperacaoSenha(token);
	    	
	    	usuarioService.updateSenha(usuario, senha);
	    	
	    	model.addAttribute("message", "Sua senha foi redefinida.");
	    	
    	}
    	catch(UsuarioException e) {
    		model.addAttribute("error", e.getMessage());
    	}
    	
    	return "reset";
    }
    
    public void sendEmail(String email, String link)  {
    	Email mensagem = new Email();
    	
    	mensagem.setEmailFrom("recovery@sgm.com.br");
    	mensagem.setEmailTo(email);
    	mensagem.setSubject("noreply Link para recuperação de senha");
    	mensagem.setText(
    			"Olá, \n\n"
    			+ "Você solicitou para redefinir a sua senha \n"
    			+ "Clique no link abaixo para redefinir sua senha: \n\n"
    			+ link + "\n\n"
				+ "Caso não tenha requisitado a redefinição de senha ignore esse e-mail.\n\n"
				+ "Atenciosamente,\n"
				+ "Sistema de gestão da manutenção");
    	
    	emailService.sendEmail(mensagem);
    }
    
    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
    
}
