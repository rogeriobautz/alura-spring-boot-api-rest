package med.voll.api.domain.usuario;

public enum EnumPapelAutorizacao {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private String authority;

    private EnumPapelAutorizacao(String authority) {
        this.authority = authority;
    }
    
    public String getAuthority() {
        return authority;
    }
}
