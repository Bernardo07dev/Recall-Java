package br.com.fiap.recall.dao.impl;

import br.com.fiap.recall.dao.ArquivoDao;
import br.com.fiap.recall.factory.ArquivoFactory;
import br.com.fiap.recall.model.Arquivo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class ArquivoDaoImpl implements ArquivoDao {

    private final JdbcTemplate jdbcTemplate;

    public ArquivoDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Arquivo> rowMapper = new RowMapper<Arquivo>() {
        @Override
        public Arquivo mapRow(ResultSet rs, int rowNum) throws SQLException {
            String tipo = rs.getString("TP_ARQUIVO");
            String id = rs.getString("ID_ARQUIVO");
            String url = rs.getString("DS_URL");
            Integer paginas = rs.getInt("NR_PAGINAS_LINHAS");
            if (rs.wasNull()) {
                paginas = null;
            }
            String empresaId = rs.getString("ID_EMPRESA");

            Arquivo arquivo = ArquivoFactory.criar(tipo, id, url, paginas, empresaId);
            Timestamp ts = rs.getTimestamp("DT_UPLOAD");
            if (ts != null) {
                arquivo.setDataUpload(ts.toLocalDateTime());
            }
            return arquivo;
        }
    };

    @Override
    public void inserir(Arquivo arquivo) {
        String sql = "INSERT INTO TB_RECALL_ARQUIVO (ID_ARQUIVO, DS_URL, TP_ARQUIVO, NR_PAGINAS_LINHAS, ID_EMPRESA, DT_UPLOAD) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                arquivo.getId(),
                arquivo.getUrl(),
                arquivo.getTipoArquivo(),
                arquivo.getPaginasLinhas(),
                arquivo.getEmpresaId(),
                Timestamp.valueOf(arquivo.getDataUpload())
        );
    }

    @Override
    public Optional<Arquivo> buscarPorId(String id) {
        String sql = "SELECT ID_ARQUIVO, DS_URL, TP_ARQUIVO, NR_PAGINAS_LINHAS, ID_EMPRESA, DT_UPLOAD " +
                "FROM TB_RECALL_ARQUIVO WHERE ID_ARQUIVO = ?";
        List<Arquivo> lista = jdbcTemplate.query(sql, rowMapper, id);
        return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
    }

    @Override
    public List<Arquivo> listarPorEmpresa(String empresaId) {
        String sql = "SELECT ID_ARQUIVO, DS_URL, TP_ARQUIVO, NR_PAGINAS_LINHAS, ID_EMPRESA, DT_UPLOAD " +
                "FROM TB_RECALL_ARQUIVO WHERE ID_EMPRESA = ? ORDER BY DT_UPLOAD DESC";
        return jdbcTemplate.query(sql, rowMapper, empresaId);
    }

    @Override
    public void atualizar(Arquivo arquivo) {
        String sql = "UPDATE TB_RECALL_ARQUIVO SET DS_URL = ?, TP_ARQUIVO = ?, NR_PAGINAS_LINHAS = ? " +
                "WHERE ID_ARQUIVO = ?";
        jdbcTemplate.update(sql,
                arquivo.getUrl(),
                arquivo.getTipoArquivo(),
                arquivo.getPaginasLinhas(),
                arquivo.getId()
        );
    }

    @Override
    public void excluir(String id) {
        String sql = "DELETE FROM TB_RECALL_ARQUIVO WHERE ID_ARQUIVO = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existePorId(String id) {
        String sql = "SELECT COUNT(1) FROM TB_RECALL_ARQUIVO WHERE ID_ARQUIVO = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
}
