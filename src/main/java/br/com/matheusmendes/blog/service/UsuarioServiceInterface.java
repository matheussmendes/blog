package br.com.matheusmendes.blog.service;

import br.com.matheusmendes.blog.model.Usuario;
import org.springframework.stereotype.Service;

@Service
public interface UsuarioServiceInterface {

    void registrar(Usuario usuario);

    Usuario buscarUsuarioPorEmail(String email);

    String buscarNomeDoUsuarioLogado(String email);

    Long buscarIdDoUsuarioLogado(String email);
}
