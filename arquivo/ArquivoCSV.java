package arquivo;

public class ArquivoCSV extends Arquivo {

    private Integer linhas;

    public ArquivoCSV(String url) {
        super(url, "CSV", null);
        this.linhas = 0;
    }

    @Override
    public String converterParaTexto() {
        // Lógica lerCSV
        return "Convertendo dados do CSV para texto...";
    }
}
