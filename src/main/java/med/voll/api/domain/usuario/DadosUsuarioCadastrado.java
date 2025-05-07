package med.voll.api.domain.usuario;

public record DadosUsuarioCadastrado (Long id, String login, String nome, EnumPapelAutorizacao autorizacao, Boolean ativo) {

    public DadosUsuarioCadastrado(Usuario usuario) {
        this(usuario.getId(), usuario.getLogin(), usuario.getNome(), usuario.getAutorizacao(), usuario.getAtivo());
    }
}
