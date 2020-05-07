package br.com.matheusmendes.blog.repository;

import br.com.matheusmendes.blog.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioInterface  extends JpaRepository<Usuario, Long> {

    Usuario findByEmail(String email);

    @Query("select u.nome from Usuario u where u.email = ?1")
    String buscarNomeDoUsuarioLogado(String email);

    @Query("select u.id from Usuario u where u.email = ?1")
    Long buscarIdDoUsuarioLogado(String email);

}
