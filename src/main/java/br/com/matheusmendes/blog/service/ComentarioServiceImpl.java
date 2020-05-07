package br.com.matheusmendes.blog.service;

import br.com.matheusmendes.blog.model.Comentario;
import br.com.matheusmendes.blog.model.Postagem;
import br.com.matheusmendes.blog.repository.ComentarioInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ComentarioServiceImpl implements ComentarioServiceInterface {

    @Autowired
    private ComentarioInterface comentarioRepository;

    @Override
    public List<Comentario> listar() {
        return comentarioRepository.findAll();
    }

    @Override
    public void comentar(Comentario comentario) {
        comentarioRepository.save(comentario);
    }

    @Override
    public List<Comentario> comentariosPorPostagem(Long id) {
        return comentarioRepository.comentariosPorPostagem(id);
    }

    @Override
    public int contarComentario(Long id) {
        return comentarioRepository.contarComentariosDaPostagem(id);
    }

}
