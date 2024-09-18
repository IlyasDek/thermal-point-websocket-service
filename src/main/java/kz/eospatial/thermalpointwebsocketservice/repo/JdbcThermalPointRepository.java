package kz.eospatial.thermalpointwebsocketservice.repo;

import kz.eospatial.thermalpointwebsocketservice.model.ThermalPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcThermalPointRepository implements ThermalPointRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<ThermalPoint> findAll() {
        String sql = "SELECT id, latitude, longitude, brightness, scan, track, acq_date, acq_time, local_time, satellite, confidence, version, bright_t31, frp, daynight " +
                "FROM fires";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new ThermalPoint(
                rs.getLong("id"),
                rs.getDouble("latitude"),
                rs.getDouble("longitude"),
                rs.getDouble("brightness"),
                rs.getDouble("scan"),
                rs.getDouble("track"),
                rs.getDate("acq_date").toLocalDate(),
                rs.getTime("acq_time") != null ? rs.getTime("acq_time").toLocalTime() : null,
                rs.getTime("local_time") != null ? rs.getTime("local_time").toLocalTime() : null,
                rs.getString("satellite"),
                rs.getString("confidence"),
                rs.getString("version"),
                rs.getDouble("bright_t31"),
                rs.getDouble("frp"),
                rs.getString("daynight"),
                null
        ));
    }

    @Override
    public List<ThermalPoint> findByForestryId(Long forestryId) {
        String sql = "SELECT f.id, f.latitude, f.longitude, f.brightness, f.scan, f.track, f.acq_date, f.acq_time, f.local_time, f.satellite, f.confidence, f.version, f.bright_t31, f.frp, daynight, r.forestry_id " +
                "FROM fires f " +
                "JOIN fire_forest_relations r ON f.id = r.fire_id " +
                "WHERE r.forestry_id = ?";
        return jdbcTemplate.query(sql, new Object[]{forestryId}, (rs, rowNum) -> new ThermalPoint(
                rs.getLong("id"),
                rs.getDouble("latitude"),
                rs.getDouble("longitude"),
                rs.getDouble("brightness"),
                rs.getDouble("scan"),
                rs.getDouble("track"),
                rs.getDate("acq_date").toLocalDate(),
                rs.getTime("acq_time") != null ? rs.getTime("acq_time").toLocalTime() : null,
                rs.getTime("local_time") != null ? rs.getTime("local_time").toLocalTime() : null,
                rs.getString("satellite"),
                rs.getString("confidence"),
                rs.getString("version"),
                rs.getDouble("bright_t31"),
                rs.getDouble("frp"),
                rs.getString("daynight"),
                rs.getLong("forestry_id")
        ));
    }
}
