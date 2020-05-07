package br.com.matheusmendes.blog.service;

import br.com.matheusmendes.blog.model.Comentario;
import br.com.matheusmendes.blog.model.Postagem;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface ComentarioServiceInterface {

    List<Comentario> listar();

    void comentar(Comentario comentario);

    List<Comentario> comentariosPorPostagem(Long id);

    int contarComentario(Long id);
}
