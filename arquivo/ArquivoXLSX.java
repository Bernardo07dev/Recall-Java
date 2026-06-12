package arquivo;

import java.util.ArrayList;
import java.util.List;

public class ArquivoXLSX extends Arquivo {

    private List<String> abas;

    public ArquivoXLSX(String url) {
        super(url, "XLSX", null);
        this.abas = new ArrayList<>();
    }

    @Override
    public String converterParaTexto() {
        // Lógica lerPlanilha
        return "Extraindo dados das abas do XLSX...";
    }
}
