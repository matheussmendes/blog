package br.com.matheusmendes.blog.controller;

import br.com.matheusmendes.blog.model.Usuario;
import br.com.matheusmendes.blog.service.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioServiceImpl service;

    @Autowired
    private HttpSession sessao;

    @GetMapping("/logar")
    public String logar(){
        return "autenticacao/login";
    }

    @GetMapping("/login-error")
    public String loginError(ModelMap model){
        model.addAttribute("loginError", "Login e/ou senha inválidos");
        return "autenticacao/login";
    }

    @GetMapping("/cadastre-se")
    public String abrirFormularioParaRegistrar(Usuario usuario){
       return "autenticacao/cadastre-se";
    }

    @PostMapping("/registrar")
    public String cadastrar(@Valid Usuario usuario, BindingResult result, RedirectAttributes ra){
        if(result.hasErrors()){
            ra.addFlashAttribute("mensagemValidacao","Por favor, verifique os campos");
            return "redirect:/cadastre-se";
        }
        usuario.setAtivo(true);
        service.registrar(usuario);
        ra.addFlashAttribute("sucesso","Usuário registrado com sucesso!");
        return "redirect:/logar";
    }

    public boolean usuarioEstahLogado(){
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }


    public String retornarNomeDoUsuarioLogado(){
        return service.buscarNomeDoUsuarioLogado(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public Long retornarIdDoUsuarioLogado(){
        return service.buscarIdDoUsuarioLogado(SecurityContextHolder.getContext().getAuthentication().getName());
    }

}
