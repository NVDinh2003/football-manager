//package com.nvd.footballmanager.seeder.financial_record;
//
//import com.nvd.footballmanager.model.entity.FinancialRecord;
//import com.nvd.footballmanager.seeder.GenericBatchInserter;
//import com.nvd.footballmanager.utils.Constants;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.jdbc.core.BatchPreparedStatementSetter;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Random;
//
//@Component
//public class FinancialRecordBatchInserter implements GenericBatchInserter<FinancialRecord> {
//
//    private final JdbcTemplate jdbcTemplate;
//    private final RedisTemplate<String, Object> redisTemplate;
//
//    public FinancialRecordBatchInserter(JdbcTemplate jdbcTemplate, RedisTemplate<String, Object> redisTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//        this.redisTemplate = redisTemplate;
//    }
//
//    @Override
//    public void batchInsert(List<FinancialRecord> financialRecords) {
//
//        String teamIds = (String) redisTemplate.opsForValue().get(Constants.MY_TEAM_ID_CACHE);
//        Random random = new Random();
//
//        String sql = "INSERT IGNORE INTO financial_records (id, type, note, amount, date, team_id, created_at, updated_at)" +
//                " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
//
//        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
//            @Override
//            public void setValues(PreparedStatement ps, int i) throws SQLException {
//                FinancialRecord record = financialRecords.get(i);
//                ps.setString(1, record.getId().toString());
//                ps.setString(2, record.getType().name());
//                ps.setString(3, record.getNote());
//                ps.setDouble(4, record.getAmount());
//                ps.setDate(5, java.sql.Date.valueOf(record.getDate()));
////                ps.setString(6, teamIds.get(random.nextInt(teamIds.size())));
//                ps.setString(6, teamIds);
//                ps.setTimestamp(7, java.sql.Timestamp.from(record.getCreatedAt()));
//                ps.setTimestamp(8, java.sql.Timestamp.from(record.getUpdatedAt()));
//
//            }
//
//            @Override
//            public int getBatchSize() {
//                return financialRecords.size();
//            }
//        });
//    }
//}
//
