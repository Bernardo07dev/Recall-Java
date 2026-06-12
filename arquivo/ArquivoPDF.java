package arquivo;

public class ArquivoPDF extends Arquivo {

    private Integer paginas;

    public ArquivoPDF(String url) {
        super(url, "PDF", null); // empresaId nulo por padrão neste construtor
        this.paginas = 0;
    }

    @Override
    public String converterParaTexto() {
        return "Extraindo texto do PDF...";
    }
}
