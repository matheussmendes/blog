package br.com.matheusmendes.blog.service;

import br.com.matheusmendes.blog.model.Postagem;
import br.com.matheusmendes.blog.model.Usuario;
import br.com.matheusmendes.blog.repository.PostInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PostServiceImpl implements PostServiceInterface {

    @Autowired
    private PostInterface postRepository;

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Override
    public List<Postagem> listar() {
        return postRepository.findAll();
    }

    @Override
    public Postagem buscarPorId(Long id) {
        return postRepository.findById(id).get();
    }

    @Override
    public void deletar(Postagem postagem) {
        postRepository.delete(postagem);
    }

    @Override
    public void salvar(Postagem postagem) {
        postagem.setUsuario(retornarUsuarioLogado());
        postRepository.save(postagem);
    }

    @Override
    public List<Postagem> carregarPostagensDoUsuario(Long id) {
        return postRepository.carregarPostagensDoUsuario(id);
    }

    @Override
    public List<Postagem> carregarPostagensPorTitulo(String texto) {
        return postRepository.findByTituloContaining(texto);
    }

    @Override
    public int retornarQuantidadeDePosts(Long id) {
        return postRepository.retornarQuantidadeDePosts(id);
    }

    @Override
    public int retornarQuantidadeDeComentariosPorUsuario(Long id) {
        return postRepository.retornarQuantidadeDeComentariosPorUsuario(id);
    }

    @Override
    public int retornarQuantidadeDeComentariosPorPost(Long id) {
        return postRepository.retornarQuantidadeDePosts(id);
    }

    public Usuario retornarUsuarioLogado(){
        Usuario usuario = new Usuario();
        usuario.setId(usuarioService.buscarIdDoUsuarioLogado(SecurityContextHolder.getContext().getAuthentication().getName()));
        return usuario;
    }
}
