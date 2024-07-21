//package com.nvd.footballmanager.seeder.user;
//
//import com.nvd.footballmanager.model.entity.User;
//import lombok.RequiredArgsConstructor;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//
//import java.sql.Date;
//import java.sql.PreparedStatement;
//import java.sql.Timestamp;
//import java.util.Collections;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class UserBatchInserter {
//
//    private final JdbcTemplate jdbcTemplate;
//
//
//    //     insert -9s
//    public void batchInsert(List<User> users) {
//        String sql = "INSERT IGNORE INTO users (id, username, password, name, dob, email, phone_number, role, enabled, created_at, updated_at) VALUES "
//                + String.join(", ", Collections.nCopies(users.size(), "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"));
//
//        jdbcTemplate.update(connection -> {
//            PreparedStatement ps = connection.prepareStatement(sql);
//            int index = 1;
//            for (User user : users) {
//                ps.setString(index++, user.getId().toString());
//                ps.setString(index++, user.getUsername());
//                ps.setString(index++, user.getPassword());
//                ps.setString(index++, user.getName());
//                ps.setDate(index++, user.getDob() != null ? Date.valueOf(user.getDob()) : null);
//                ps.setString(index++, user.getEmail());
//                ps.setString(index++, user.getPhoneNumber());
//                ps.setString(index++, user.getRole().name());
//                ps.setBoolean(index++, user.getEnabled());
//                ps.setTimestamp(index++, Timestamp.from(user.getCreatedAt()));
//                ps.setTimestamp(index++, Timestamp.from(user.getUpdatedAt()));
//            }
//            return ps;
//        });
//    }
//
//    // insert -13s
////    public void batchInsert(List<User> users) {
////        StringBuilder sql = new StringBuilder("INSERT IGNORE INTO users (id, username, password, name, dob, email, phone_number, role, enabled, created_at, updated_at) VALUES ");
////        List<Object> params = new ArrayList<>();
////        for (User user : users) {
////            sql.append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?),");
////            params.add(user.getId().toString());
////            params.add(user.getUsername());
////            params.add(user.getPassword());
////            params.add(user.getName());
////            params.add(user.getDob() != null ? Date.valueOf(user.getDob()) : null);
////            params.add(user.getEmail());
////            params.add(user.getPhoneNumber());
////            params.add(user.getRole().name());
////            params.add(user.getEnabled());
////            params.add(Timestamp.from(user.getCreatedAt()));
////            params.add(Timestamp.from(user.getUpdatedAt()));
////        }
////        sql.deleteCharAt(sql.length() - 1); // remove last comma (,)
////        jdbcTemplate.update(sql.toString(), params.toArray());
////    }
//
////    public void batchInsert(List<User> users) {
////        String sql = "INSERT IGNORE INTO users (id, username, password, name, dob, email, phone_number, role, enabled, created_at, updated_at)" +
////                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
////
////        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
////            @Override
////            public void setValues(PreparedStatement ps, int i) throws SQLException {
////                User user = users.get(i);
////                ps.setString(1, user.getId().toString());
////                ps.setString(2, user.getUsername());
////                ps.setString(3, user.getPassword());
////                ps.setString(4, user.getName());
////                ps.setDate(5, user.getDob() != null ? Date.valueOf(user.getDob()) : null);
////                ps.setString(6, user.getEmail());
////                ps.setString(7, user.getPhoneNumber());
////                ps.setString(8, user.getRole().name());
////                ps.setBoolean(9, user.getEnabled());
////                ps.setTimestamp(10, Timestamp.from(user.getCreatedAt()));
////                ps.setTimestamp(11, Timestamp.from(user.getUpdatedAt()));
////            }
////
////            @Override
////            public int getBatchSize() {
////                return users.size();
////            }
////        });
////    }
//}
