//package com.nvd.footballmanager.seeder.user;
//
//import com.nvd.footballmanager.model.entity.User;
//import com.nvd.footballmanager.seeder.GenericBatchInserter;
//import org.springframework.jdbc.core.BatchPreparedStatementSetter;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//
//import java.sql.Date;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.util.List;
//
//@Component
//public class UserBatchInserter implements GenericBatchInserter<User> {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    public UserBatchInserter(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    public void batchInsert(List<User> users) {
//        String sql = "INSERT IGNORE INTO users (id, username, password, name, dob, email, phone_number, role, enabled, created_at, updated_at)" +
//                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//
//        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
//            @Override
//            public void setValues(PreparedStatement ps, int i) throws SQLException {
//                User user = users.get(i);
//                ps.setString(1, user.getId().toString());
//                ps.setString(2, user.getUsername());
//                ps.setString(3, user.getPassword());
//                ps.setString(4, user.getName());
//                ps.setDate(5, user.getDob() != null ? Date.valueOf(user.getDob()) : null);
//                ps.setString(6, user.getEmail());
//                ps.setString(7, user.getPhoneNumber());
//                ps.setString(8, user.getRole().name());
//                ps.setBoolean(9, user.getEnabled());
//                ps.setTimestamp(10, Timestamp.from(user.getCreatedAt()));
//                ps.setTimestamp(11, Timestamp.from(user.getUpdatedAt()));
//            }
//
//            @Override
//            public int getBatchSize() {
//                return users.size();
//            }
//        });
//    }
//
////    @Override
////    public void batchInsert(List<User> users) {
////        String sql = "INSERT IGNORE INTO users (id, username, password, name, dob, email, phone_number, role, enabled, created_at, updated_at) VALUES "
////                + String.join(", ", Collections.nCopies(users.size(), "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"));
////
////        jdbcTemplate.update(connection -> {
////            PreparedStatement ps = connection.prepareStatement(sql);
////            int index = 1;
////            for (User user : users) {
////                ps.setString(index++, user.getId().toString());
////                ps.setString(index++, user.getUsername());
////                ps.setString(index++, user.getPassword());
////                ps.setString(index++, user.getName());
////                ps.setDate(index++, user.getDob() != null ? Date.valueOf(user.getDob()) : null);
////                ps.setString(index++, user.getEmail());
////                ps.setString(index++, user.getPhoneNumber());
////                ps.setString(index++, user.getRole().name());
////                ps.setBoolean(index++, user.getEnabled());
////                ps.setTimestamp(index++, Timestamp.from(user.getCreatedAt()));
////                ps.setTimestamp(index++, Timestamp.from(user.getUpdatedAt()));
////            }
////            return ps;
////        });
////    }
//}
//
