package br.com.matheusmendes.blog.controller;

import br.com.matheusmendes.blog.exception.NaoPossuiPermissaoException;
import br.com.matheusmendes.blog.model.Comentario;
import br.com.matheusmendes.blog.model.Postagem;
import br.com.matheusmendes.blog.model.Usuario;
import br.com.matheusmendes.blog.service.ComentarioServiceImpl;
import br.com.matheusmendes.blog.service.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Controller
public class PostController {

    @Autowired
    private PostServiceImpl service;

    @Autowired
    private UsuarioController usuarioController;

    @Autowired
    private ComentarioController comentarioController;

    private Postagem postagem;

    @GetMapping("/")
    public String home(){
        return "postagem/postagens";
    }


    @GetMapping("/dashboard")
    public String home(ModelMap model){
        if(usuarioController.usuarioEstahLogado()){
            model.addAttribute("nomeUsuario", usuarioController.retornarNomeDoUsuarioLogado());
        }
        return "dashboard/dashboard";
    }

    @ModelAttribute("posts")
    public List<Postagem> listarPosts(){
        return service.listar();
    }

    @GetMapping("/cadastro")
    public String cadastro(Postagem postagem, Usuario usuario, ModelMap model){
        if(usuarioController.usuarioEstahLogado()){
            model.addAttribute("nomeUsuario", usuarioController.retornarNomeDoUsuarioLogado());
        }
        return "postagem/cadastro";
    }

    Postagem postagemDoComentario;

    @PostMapping("/salvar")
    public String salvar(@Valid Postagem postagem, BindingResult result, RedirectAttributes ra)
    {
        if(result.hasErrors()){
            ra.addFlashAttribute("mensagemValidacao", "Por favor, verifique os campos!");
            return "redirect:/cadastro";
        }
        LocalDate agora = LocalDate.now();
        formatarData(agora);
        postagem.setData(agora);
        service.salvar(postagem);
        ra.addFlashAttribute("sucesso", "Postagem publicada com sucesso!");
        return "redirect:/postagens";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id")Postagem postagem, ModelMap model, RedirectAttributes ra){
        postagemInexistente(postagem);
        if(usuarioController.usuarioEstahLogado()){
            model.addAttribute("nomeUsuario", usuarioController.retornarNomeDoUsuarioLogado());
        }
        try {
            naoPossuiPermissaoParaEditarEExcluirPost(postagem.getUsuario().getId(), usuarioController.retornarIdDoUsuarioLogado(), model);
        } catch (Exception e) {
            ra.addFlashAttribute("naoPossuiPermissao", e.getMessage());
            return "redirect:/postagens";
        }
        model.addAttribute(postagem);
        return "postagem/cadastro";
    }

    public boolean postagemInexistente(Postagem postagem){
        if(postagem.getId() == null){
            throw new NullPointerException("Você não possui permissão!");
        }
        return false;
    }


    public void naoPossuiPermissaoParaEditarEExcluirPost(Long idUsuario, Long idPostagem, ModelMap model) throws NaoPossuiPermissaoException {
        if (!idUsuario.equals(idPostagem)) {
            throw new NaoPossuiPermissaoException("Desculpe, você não possui essa permissão.");
        }
        model.addAttribute("exibeBotao", "");
    }

    @GetMapping("/apagar/{id}")
    public String apagar(@PathVariable("id")Postagem postagem, RedirectAttributes ra, ModelMap model){
        postagemInexistente(postagem);
        try {
            naoPossuiPermissaoParaEditarEExcluirPost(postagem.getUsuario().getId(), usuarioController.retornarIdDoUsuarioLogado(), model);
        } catch (Exception e) {
            ra.addFlashAttribute("naoPossuiPermissao", e.getMessage());
            return "redirect:/postagens";
        }
        if(usuarioController.usuarioEstahLogado()){
            model.addAttribute("nomeUsuario", usuarioController.retornarNomeDoUsuarioLogado());
        }
        service.deletar(postagem);
        ra.addFlashAttribute("sucesso","Post apagado com sucesso!");
        return "redirect:/postagens";
    }

    @GetMapping("/confirmarExclusao/{id}")
    public String confirmarExclusao(@PathVariable("id") Postagem postagem, ModelMap model, RedirectAttributes ra){
        postagemInexistente(postagem);
        try {
            naoPossuiPermissaoParaEditarEExcluirPost(postagem.getUsuario().getId(), usuarioController.retornarIdDoUsuarioLogado(), model);
        } catch (Exception e) {
            ra.addFlashAttribute("naoPossuiPermissao", e.getMessage());
            return "redirect:/postagens";
        }
        if(usuarioController.usuarioEstahLogado()){
            model.addAttribute("nomeUsuario", usuarioController.retornarNomeDoUsuarioLogado());
        }
        model.addAttribute(postagem);
        return "postagem/confirmaExclusao";
    }

    public LocalDate formatarData(LocalDate agora){
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MMM/yyyy");
        agora = LocalDate.now();
        formatador.format(agora);
        return agora;
    }

    @ModelAttribute("postagens")
    public List<Postagem> trazPostagens(ModelMap model){
        if(usuarioController.usuarioEstahLogado()){
            model.addAttribute("nomeUsuario", usuarioController.retornarNomeDoUsuarioLogado());
        }
       List<Postagem> postagensOrdenadasPorMaisRecente = service.listar();
       postagensOrdenadasPorMaisRecente.sort(Comparator.comparing(Postagem::getData).reversed());
        return postagensOrdenadasPorMaisRecente;
    }

    @ModelAttribute("postagensDoUsuario")
    public List<Postagem> carregarPostagensDoUsuario(ModelMap model){
        if(usuarioController.usuarioEstahLogado()){
            model.addAttribute("nomeUsuario", usuarioController.retornarNomeDoUsuarioLogado());
        }
        List<Postagem> postagensOrdenadasPorMaisRecente = service.carregarPostagensDoUsuario(usuarioController.retornarIdDoUsuarioLogado());
        postagensOrdenadasPorMaisRecente.sort(Comparator.comparing(Postagem::getData).reversed());
        return postagensOrdenadasPorMaisRecente;
    }

    @GetMapping("/postagensDoUsuario")
    public String abrirDashBoardDoUsuarioLogado(ModelMap model){
        if(usuarioController.usuarioEstahLogado()){
            model.addAttribute("nomeUsuario", usuarioController.retornarNomeDoUsuarioLogado());
        }
        return "postagem/postagensDoUsuario";
    }

    @GetMapping("/detalhes/{id}")
    public String carregarPostagemPorId(@PathVariable("id") Postagem postagem, Comentario comentario, ModelMap model) throws NaoPossuiPermissaoException {
        if(usuarioController.usuarioEstahLogado()){
            model.addAttribute("nomeUsuario", usuarioController.retornarNomeDoUsuarioLogado());
        }
        ehUsuarioDonoDaPostagem(postagem.getUsuario().getId(), model);
        postagemDoComentario = postagem;
        model.addAttribute(postagem);
        model.addAttribute("comentarios",comentarioController.comentariosPorPostagem()) ;
        return "postagem/detalhes";
    }

    public boolean ehUsuarioDonoDaPostagem(Long donoDaPostagem, ModelMap model){
        if (donoDaPostagem.equals(usuarioController.retornarIdDoUsuarioLogado())){
            model.addAttribute("exibeBotao", "");
            return true;
        }
        return false;
    }

    @GetMapping("/postagens")
    public String exibePostagens(ModelMap model) {
        if(usuarioController.usuarioEstahLogado()){
            model.addAttribute("nomeUsuario", usuarioController.retornarNomeDoUsuarioLogado());
        }
        return "postagem/postagens";
    }

    @GetMapping("/buscarPostPorTitulo")
    public String buscarPostPorTitulo(@RequestParam("titulo")String titulo, ModelMap model){
        model.addAttribute("postagens", service.carregarPostagensPorTitulo(titulo));
        return "postagem/postagens";
    }

    @ModelAttribute("quantidadeDePosts")
    public int retornarQuantidadeDePostsPorUsuario(){
        return service.retornarQuantidadeDePosts(usuarioController.retornarIdDoUsuarioLogado());
    }

    @ModelAttribute("quantidadeDeComentarios")
    public int retornarQuantidadeDeComentariosPorUsuario(){
        return service.retornarQuantidadeDeComentariosPorUsuario(usuarioController.retornarIdDoUsuarioLogado());
    }


}
