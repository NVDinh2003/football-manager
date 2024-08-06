package com.nvd.footballmanager.seeder.SeederService;

import com.nvd.footballmanager.model.entity.Member;
import com.nvd.footballmanager.repository.MatchRepository;
import com.nvd.footballmanager.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MemberMatchService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String MemberM_ID_CACHE = "member_m_id_cache";
    private static final String MMatch_ID_CACHE = "m_match_id_cache";

    @PostConstruct  // được gọi ngay sau khi các bean duoc khoi tao va các dependency da ược inject
    public void init() {
        if (Boolean.FALSE.equals(redisTemplate.hasKey(MemberM_ID_CACHE))) {
            List<Member> members = memberRepository.findAll();

            List<String> member_ids = members.stream()
                    .map(m -> m.getId().toString())
                    .collect(Collectors.toList());

            redisTemplate.opsForValue().set(MemberM_ID_CACHE, member_ids);
        }

//        List<String> matchIds = matchRepository.findRandomLimitOffset(0, 5);
//        int i = 1;

        if (Boolean.FALSE.equals(redisTemplate.hasKey(MMatch_ID_CACHE))) {
            List<String> matchIds = matchRepository.findAllMatchIds();
//            List<String> matchIds = matchRepository.findRandomLimitOffset(0, 5);

            List<String> matchIdStrings = matchIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList());

            redisTemplate.opsForValue().set(MMatch_ID_CACHE, matchIds);
        }

    }


    @Transactional
    public void setupMemberMatchRelationships() {
        List<String> memberIds = (List<String>) redisTemplate.opsForValue().get(MemberM_ID_CACHE);
        List<String> matchIds = (List<String>) redisTemplate.opsForValue().get(MMatch_ID_CACHE);

        String sql = "INSERT IGNORE INTO member_match (member_id, match_id) VALUES (?, ?)";
        int batchSize = 40_000;  // Kích thước của mỗi batch
        List<Object[]> batchArgs = new ArrayList<>();

        for (String memberId : memberIds) {
            Set<String> memberMatches = getRandomMatches(matchIds); // Lấy các match ngẫu nhiên cho mỗi member
            for (String matchId : memberMatches) {
                batchArgs.add(new Object[]{memberId, matchId});
            }

            if (batchArgs.size() >= batchSize) {
                log.info("Inserting batch of size: " + batchArgs.size());
                insertBatch(sql, batchArgs);
                batchArgs.clear(); // Xóa danh sách batchArgs để chuẩn bị cho batch tiếp theo
            }
        }

        // Chèn các bản ghi còn lại nếu còn
        if (!batchArgs.isEmpty()) {
            log.info("Inserting remaining batch of size: " + batchArgs.size());
            insertBatch(sql, batchArgs);
        }

        log.info("Finished inserting member_match records");
    }

    private void insertBatch(String sql, List<Object[]> batchArgs) {
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, (String) batchArgs.get(i)[0]);
                ps.setString(2, (String) batchArgs.get(i)[1]);
            }

            @Override
            public int getBatchSize() {
                return batchArgs.size();
            }
        });
    }


    private Set<String> getRandomMatches(List<String> matchesId) {
        Set<String> randomMatches = new HashSet<>();
        Random random = new Random();

        int minMatches = 30;
        int maxMatches = 50;
        int numberOfMatches = random.nextInt(maxMatches - minMatches + 1) + minMatches;

        while (randomMatches.size() < numberOfMatches) {
            String randomMatchId = matchesId.get(random.nextInt(matchesId.size()));
            randomMatches.add(randomMatchId);
        }

        return randomMatches;
    }
}
