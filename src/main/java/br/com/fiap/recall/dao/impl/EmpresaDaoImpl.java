package br.com.fiap.recall.dao.impl;

import br.com.fiap.recall.dao.EmpresaDao;
import br.com.fiap.recall.model.Empresa;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class EmpresaDaoImpl implements EmpresaDao {

    private final JdbcTemplate jdbcTemplate;

    public EmpresaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Empresa> rowMapper = new RowMapper<Empresa>() {
        @Override
        public Empresa mapRow(ResultSet rs, int rowNum) throws SQLException {
            Empresa emp = new Empresa();
            emp.setId(rs.getString("ID_EMPRESA"));
            emp.setNome(rs.getString("NM_EMPRESA"));
            emp.setIndustry(rs.getString("DS_INDUSTRY"));
            emp.setSummarization(rs.getString("DS_SUMMARIZATION"));
            emp.setJsonIntel(rs.getString("DS_JSON_INTEL"));
            Timestamp ts = rs.getTimestamp("DT_CRIACAO");
            if (ts != null) {
                emp.setDataCriacao(ts.toLocalDateTime());
            }
            return emp;
        }
    };

    @Override
    public void inserir(Empresa empresa) {
        String sql = "INSERT INTO TB_RECALL_EMPRESA (ID_EMPRESA, NM_EMPRESA, DS_INDUSTRY, DS_SUMMARIZATION, DS_JSON_INTEL, DT_CRIACAO) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                empresa.getId(),
                empresa.getNome(),
                empresa.getIndustry(),
                empresa.getSummarization(),
                empresa.getJsonIntel(),
                Timestamp.valueOf(empresa.getDataCriacao())
        );
    }

    @Override
    public Optional<Empresa> buscarPorId(String id) {
        String sql = "SELECT ID_EMPRESA, NM_EMPRESA, DS_INDUSTRY, DS_SUMMARIZATION, DS_JSON_INTEL, DT_CRIACAO " +
                "FROM TB_RECALL_EMPRESA WHERE ID_EMPRESA = ?";
        List<Empresa> lista = jdbcTemplate.query(sql, rowMapper, id);
        return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
    }

    @Override
    public Optional<Empresa> buscarPorNome(String nome) {
        String sql = "SELECT ID_EMPRESA, NM_EMPRESA, DS_INDUSTRY, DS_SUMMARIZATION, DS_JSON_INTEL, DT_CRIACAO " +
                "FROM TB_RECALL_EMPRESA WHERE LOWER(NM_EMPRESA) = LOWER(?)";
        List<Empresa> lista = jdbcTemplate.query(sql, rowMapper, nome);
        return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
    }

    @Override
    public List<Empresa> listarTodos() {
        String sql = "SELECT ID_EMPRESA, NM_EMPRESA, DS_INDUSTRY, DS_SUMMARIZATION, DS_JSON_INTEL, DT_CRIACAO " +
                "FROM TB_RECALL_EMPRESA ORDER BY NM_EMPRESA ASC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public void atualizar(Empresa empresa) {
        String sql = "UPDATE TB_RECALL_EMPRESA SET NM_EMPRESA = ?, DS_INDUSTRY = ?, DS_SUMMARIZATION = ?, DS_JSON_INTEL = ? " +
                "WHERE ID_EMPRESA = ?";
        jdbcTemplate.update(sql,
                empresa.getNome(),
                empresa.getIndustry(),
                empresa.getSummarization(),
                empresa.getJsonIntel(),
                empresa.getId()
        );
    }

    @Override
    public void excluir(String id) {
        String sql = "DELETE FROM TB_RECALL_EMPRESA WHERE ID_EMPRESA = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existePorId(String id) {
        String sql = "SELECT COUNT(1) FROM TB_RECALL_EMPRESA WHERE ID_EMPRESA = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public boolean existePorNome(String nome) {
        String sql = "SELECT COUNT(1) FROM TB_RECALL_EMPRESA WHERE LOWER(NM_EMPRESA) = LOWER(?)";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, nome);
        return count != null && count > 0;
    }
}
