package br.com.fiap.recall.dao.impl;

import br.com.fiap.recall.dao.InsightDao;
import br.com.fiap.recall.model.Categoria;
import br.com.fiap.recall.model.Criticidade;
import br.com.fiap.recall.model.Insight;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class InsightDaoImpl implements InsightDao {

    private final JdbcTemplate jdbcTemplate;

    public InsightDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Insight> rowMapper = new RowMapper<Insight>() {
        @Override
        public Insight mapRow(ResultSet rs, int rowNum) throws SQLException {
            Insight insight = new Insight();
            insight.setId(rs.getString("ID_INSIGHT"));
            String categoriaStr = rs.getString("DS_CATEGORIA");
            if (categoriaStr != null) {
                try {
                    insight.setCategoria(Categoria.valueOf(categoriaStr.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    insight.setCategoria(Categoria.OPERACIONAL);
                }
            }
            String criticidadeStr = rs.getString("DS_CRITICIDADE");
            if (criticidadeStr != null) {
                try {
                    insight.setCriticidade(Criticidade.valueOf(criticidadeStr.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    insight.setCriticidade(Criticidade.MEDIA);
                }
            }
            insight.setTitulo(rs.getString("NM_TITULO"));
            insight.setInsight(rs.getString("DS_INSIGHT"));
            insight.setAcaoSugerida(rs.getString("DS_ACAO_SUGERIDA"));
            insight.setEmpresaId(rs.getString("ID_EMPRESA"));
            Timestamp ts = rs.getTimestamp("DT_CRIACAO");
            if (ts != null) {
                insight.setDataCriacao(ts.toLocalDateTime());
            }
            return insight;
        }
    };

    @Override
    public void inserir(Insight insight) {
        String sql = "INSERT INTO TB_RECALL_INSIGHT (ID_INSIGHT, DS_CATEGORIA, DS_CRITICIDADE, NM_TITULO, DS_INSIGHT, DS_ACAO_SUGERIDA, ID_EMPRESA, DT_CRIACAO) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                insight.getId(),
                insight.getCategoria() != null ? insight.getCategoria().name() : Categoria.OPERACIONAL.name(),
                insight.getCriticidade() != null ? insight.getCriticidade().name() : Criticidade.MEDIA.name(),
                insight.getTitulo(),
                insight.getInsight(),
                insight.getAcaoSugerida(),
                insight.getEmpresaId(),
                Timestamp.valueOf(insight.getDataCriacao())
        );
    }

    @Override
    public Optional<Insight> buscarPorId(String id) {
        String sql = "SELECT ID_INSIGHT, DS_CATEGORIA, DS_CRITICIDADE, NM_TITULO, DS_INSIGHT, DS_ACAO_SUGERIDA, ID_EMPRESA, DT_CRIACAO " +
                "FROM TB_RECALL_INSIGHT WHERE ID_INSIGHT = ?";
        List<Insight> lista = jdbcTemplate.query(sql, rowMapper, id);
        return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
    }

    @Override
    public List<Insight> listarPorEmpresa(String empresaId) {
        String sql = "SELECT ID_INSIGHT, DS_CATEGORIA, DS_CRITICIDADE, NM_TITULO, DS_INSIGHT, DS_ACAO_SUGERIDA, ID_EMPRESA, DT_CRIACAO " +
                "FROM TB_RECALL_INSIGHT WHERE ID_EMPRESA = ? ORDER BY DT_CRIACAO DESC";
        return jdbcTemplate.query(sql, rowMapper, empresaId);
    }

    @Override
    public void atualizar(Insight insight) {
        String sql = "UPDATE TB_RECALL_INSIGHT SET DS_CATEGORIA = ?, DS_CRITICIDADE = ?, NM_TITULO = ?, DS_INSIGHT = ?, DS_ACAO_SUGERIDA = ? " +
                "WHERE ID_INSIGHT = ?";
        jdbcTemplate.update(sql,
                insight.getCategoria() != null ? insight.getCategoria().name() : Categoria.OPERACIONAL.name(),
                insight.getCriticidade() != null ? insight.getCriticidade().name() : Criticidade.MEDIA.name(),
                insight.getTitulo(),
                insight.getInsight(),
                insight.getAcaoSugerida(),
                insight.getId()
        );
    }

    @Override
    public void excluir(String id) {
        String sql = "DELETE FROM TB_RECALL_INSIGHT WHERE ID_INSIGHT = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existePorId(String id) {
        String sql = "SELECT COUNT(1) FROM TB_RECALL_INSIGHT WHERE ID_INSIGHT = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
}
