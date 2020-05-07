package br.com.matheusmendes.blog.repository;


import br.com.matheusmendes.blog.model.Comentario;
import br.com.matheusmendes.blog.model.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostInterface extends JpaRepository<Postagem, Long> {

    @Query("select p from Postagem p where p.usuario.id = ?1")
    List<Postagem> carregarPostagensDoUsuario(Long id);

    List<Postagem> findByTituloContaining(String texto);

    @Query("select count(p.id) from Postagem  p where p.usuario.id = ?1")
    int retornarQuantidadeDePosts(Long id);

    @Query("select count(c.id) from Comentario c, Postagem p, Usuario u where  c.postagem.id = p.id and p.usuario.id = u.id and u.id = ?1")
    int retornarQuantidadeDeComentariosPorUsuario(Long id);

    @Query("select count(c.id) from Comentario c, Postagem p where c.postagem.id = p.id and p.id = ?1")
    int retornarQuantidadeDeComentariosPorPost(Long id);
}
