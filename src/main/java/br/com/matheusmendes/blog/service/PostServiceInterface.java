package br.com.matheusmendes.blog.service;

import br.com.matheusmendes.blog.model.Postagem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostServiceInterface {

    List<Postagem> listar();

    Postagem buscarPorId(Long id);

    void deletar(Postagem postagem);

    void salvar(Postagem postagem);

    List<Postagem> carregarPostagensDoUsuario(Long id);

    List<Postagem> carregarPostagensPorTitulo(String texto);

    int retornarQuantidadeDePosts(Long id);

    int retornarQuantidadeDeComentariosPorUsuario(Long id);

    int retornarQuantidadeDeComentariosPorPost(Long id);
}
