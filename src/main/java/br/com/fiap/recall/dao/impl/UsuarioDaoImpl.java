package br.com.fiap.recall.dao.impl;

import br.com.fiap.recall.dao.UsuarioDao;
import br.com.fiap.recall.factory.UsuarioFactory;
import br.com.fiap.recall.model.Usuario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class UsuarioDaoImpl implements UsuarioDao {

    private final JdbcTemplate jdbcTemplate;

    public UsuarioDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Usuario> rowMapper = new RowMapper<Usuario>() {
        @Override
        public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
            String tipo = rs.getString("TP_USUARIO");
            String id = rs.getString("ID_USUARIO");
            String nome = rs.getString("NM_USUARIO");
            String email = rs.getString("DS_EMAIL");
            String cargo = rs.getString("DS_CARGO");
            String area = rs.getString("DS_AREA");

            Usuario usuario = UsuarioFactory.criar(tipo, id, nome, email, cargo, area);
            Timestamp ts = rs.getTimestamp("DT_CRIACAO");
            if (ts != null) {
                usuario.setDataCriacao(ts.toLocalDateTime());
            }
            return usuario;
        }
    };

    @Override
    public void inserir(Usuario usuario) {
        String sql = "INSERT INTO TB_RECALL_USUARIO (ID_USUARIO, NM_USUARIO, DS_EMAIL, DS_CARGO, DS_AREA, TP_USUARIO, DT_CRIACAO) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCargo(),
                usuario.getArea(),
                usuario.getTipoUsuario(),
                Timestamp.valueOf(usuario.getDataCriacao())
        );
    }

    @Override
    public Optional<Usuario> buscarPorId(String id) {
        String sql = "SELECT ID_USUARIO, NM_USUARIO, DS_EMAIL, DS_CARGO, DS_AREA, TP_USUARIO, DT_CRIACAO " +
                "FROM TB_RECALL_USUARIO WHERE ID_USUARIO = ?";
        List<Usuario> lista = jdbcTemplate.query(sql, rowMapper, id);
        return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        String sql = "SELECT ID_USUARIO, NM_USUARIO, DS_EMAIL, DS_CARGO, DS_AREA, TP_USUARIO, DT_CRIACAO " +
                "FROM TB_RECALL_USUARIO WHERE LOWER(DS_EMAIL) = LOWER(?)";
        List<Usuario> lista = jdbcTemplate.query(sql, rowMapper, email);
        return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
    }

    @Override
    public List<Usuario> listarTodos() {
        String sql = "SELECT ID_USUARIO, NM_USUARIO, DS_EMAIL, DS_CARGO, DS_AREA, TP_USUARIO, DT_CRIACAO " +
                "FROM TB_RECALL_USUARIO ORDER BY NM_USUARIO ASC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public void atualizar(Usuario usuario) {
        String sql = "UPDATE TB_RECALL_USUARIO SET NM_USUARIO = ?, DS_EMAIL = ?, DS_CARGO = ?, DS_AREA = ?, TP_USUARIO = ? " +
                "WHERE ID_USUARIO = ?";
        jdbcTemplate.update(sql,
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCargo(),
                usuario.getArea(),
                usuario.getTipoUsuario(),
                usuario.getId()
        );
    }

    @Override
    public void excluir(String id) {
        String sql = "DELETE FROM TB_RECALL_USUARIO WHERE ID_USUARIO = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existePorId(String id) {
        String sql = "SELECT COUNT(1) FROM TB_RECALL_USUARIO WHERE ID_USUARIO = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public boolean existePorEmail(String email) {
        String sql = "SELECT COUNT(1) FROM TB_RECALL_USUARIO WHERE LOWER(DS_EMAIL) = LOWER(?)";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }
}
