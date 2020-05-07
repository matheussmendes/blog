package br.com.matheusmendes.blog.exception;

public class NaoPossuiPermissaoException extends Exception {

    public NaoPossuiPermissaoException(String mensagem) {
        super(mensagem);
    }
}
