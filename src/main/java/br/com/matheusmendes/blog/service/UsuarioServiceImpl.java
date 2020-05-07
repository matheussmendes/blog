package br.com.matheusmendes.blog.service;

import br.com.matheusmendes.blog.model.Usuario;
import br.com.matheusmendes.blog.repository.UsuarioInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class UsuarioServiceImpl implements UsuarioServiceInterface, UserDetailsService {

    @Autowired
    private UsuarioInterface usuarioRepository;

    @Override
    public void registrar(Usuario usuario) {
        BCryptPasswordEncoder criptografa = new BCryptPasswordEncoder();
        usuario.setSenha(criptografa.encode(usuario.getSenha()));
        usuarioRepository.save(usuario);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = buscarUsuarioPorEmail(email);
        return new User(usuario.getEmail(), usuario.getSenha(), new HashSet<>());
    }

    @Override
    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public String buscarNomeDoUsuarioLogado(String email) {
        return usuarioRepository.buscarNomeDoUsuarioLogado(email);
    }

    @Override
    public Long buscarIdDoUsuarioLogado(String email) {
        return usuarioRepository.buscarIdDoUsuarioLogado(email);
    }

}
