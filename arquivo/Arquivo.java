package arquivo;

import java.util.UUID;

public abstract class Arquivo {
    private UUID id;
    private String url;
    private String tipo;
    private UUID empresaId;

    public Arquivo(String url, String tipo, UUID empresaId) {
        this.id = UUID.randomUUID();
        this.url = url;
        this.tipo = tipo;
        this.empresaId = empresaId;
    }

    public String getUrl() {
        return this.url;
    }

    public String getTipo() {
        return this.tipo;
    }

    public abstract String converterParaTexto();

    public Boolean validarFormato() {
        return url != null && !url.isEmpty(); // Lógica básica de validação
    }
}