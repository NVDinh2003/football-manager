//package com.nvd.footballmanager.seeder.membership_request;
//
//import com.nvd.footballmanager.model.entity.MembershipRequest;
//import com.nvd.footballmanager.seeder.GenericBatchInserter;
//import org.springframework.jdbc.core.BatchPreparedStatementSetter;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.util.List;
//
//@Component
//public class MembershipRequestBatchInserter implements GenericBatchInserter<MembershipRequest> {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    public MembershipRequestBatchInserter(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    @Override
//    public void batchInsert(List<MembershipRequest> membershipRequests) {
//        String sql = "INSERT IGNORE INTO membership_request (id, status, request_date, response_date, user_id, team_id) " +
//                "VALUES (?, ?, ?, ?, ?, ?)";
//
//        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
//            @Override
//            public void setValues(PreparedStatement ps, int i) throws SQLException {
//                MembershipRequest request = membershipRequests.get(i);
//                ps.setString(1, request.getId().toString());
//                ps.setString(2, request.getStatus().name());
//                ps.setTimestamp(3, Timestamp.from(request.getRequestDate()));
//                ps.setTimestamp(4, request.getResponseDate() != null ? Timestamp.from(request.getResponseDate()) : null);
//                ps.setString(5, request.getUser().getId().toString());
//                ps.setString(6, request.getTeam().getId().toString());
//            }
//
//            @Override
//            public int getBatchSize() {
//                return membershipRequests.size();
//            }
//        });
//    }
//}
//
