package br.com.matheusmendes.blog.repository;

import br.com.matheusmendes.blog.model.Comentario;
import br.com.matheusmendes.blog.model.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioInterface extends JpaRepository<Comentario, Long> {

    @Query("select c from Comentario c where c.postagem.id = ?1")
    List<Comentario> comentariosPorPostagem(Long id);

    @Query("select count(c.id) from Comentario  c where c.postagem = ?1")
    int contarComentariosDaPostagem(Long id);
}
