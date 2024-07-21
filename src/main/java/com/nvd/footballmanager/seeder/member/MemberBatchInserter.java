//package com.nvd.footballmanager.seeder.member;
//
//import com.nvd.footballmanager.model.entity.Member;
//import com.nvd.footballmanager.seeder.GenericBatchInserter;
//import org.springframework.jdbc.core.BatchPreparedStatementSetter;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.List;
//
//@Component
//public class MemberBatchInserter implements GenericBatchInserter<Member> {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    public MemberBatchInserter(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    public void batchInsert(List<Member> members) {
//        String sql = "INSERT IGNORE INTO members (id, nickname, position, shirt_number, role, fee, user_id, team_id)" +
//                " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
//
//        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
//            @Override
//            public void setValues(PreparedStatement ps, int i) throws SQLException {
//                Member member = members.get(i);
//                ps.setString(1, member.getId().toString());
//                ps.setString(2, member.getNickname());
//                ps.setString(3, member.getPosition());
//                ps.setInt(4, member.getShirtNumber());
//                ps.setString(5, member.getRole().name());
//                ps.setDouble(6, member.getFee());
//                ps.setString(7, member.getUser().getId().toString());
//                ps.setString(8, member.getTeam().getId().toString());
//            }
//
//            @Override
//            public int getBatchSize() {
//                return members.size();
//            }
//        });
//    }
//}