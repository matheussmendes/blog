package br.com.matheusmendes.blog.controller;

import br.com.matheusmendes.blog.model.Comentario;
import br.com.matheusmendes.blog.model.Postagem;
import br.com.matheusmendes.blog.service.ComentarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Controller
public class ComentarioController {

    @Autowired
    private ComentarioServiceImpl service;

    @Autowired
    private PostController postController;


    @PostMapping("/comentar")
    public String comentar(@Valid Comentario comentario, BindingResult result, RedirectAttributes ra){
        if(result.hasErrors()){
            ra.addFlashAttribute("mensagemValidacao","Por favor, verifique os campos!");
            return "redirect:/detalhes/"+postController.postagemDoComentario.getId();
        }
        LocalDate agora = LocalDate.now();
        comentario.setData(agora);
        comentario.setPostagem(postController.postagemDoComentario);
        service.comentar(comentario);
        ra.addFlashAttribute("sucesso", "Comentário adicionado com sucesso!");
        return "redirect:/detalhes/"+postController.postagemDoComentario.getId();
    }

    @ModelAttribute("comentarios")
    public List<Comentario> comentariosPorPostagem(){
        List<Comentario> comentariosOrdenadoPorMaisRecentes = service.comentariosPorPostagem(postController.postagemDoComentario.getId());
        comentariosOrdenadoPorMaisRecentes.sort(Comparator.comparing(Comentario::getData).reversed());
        return comentariosOrdenadoPorMaisRecentes;
    }




}
