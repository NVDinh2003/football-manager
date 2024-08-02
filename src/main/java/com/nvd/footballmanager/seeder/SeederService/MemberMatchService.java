package com.nvd.footballmanager.seeder.SeederService;

import com.nvd.footballmanager.model.entity.Match;
import com.nvd.footballmanager.model.entity.Member;
import com.nvd.footballmanager.repository.MatchRepository;
import com.nvd.footballmanager.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
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

        if (Boolean.FALSE.equals(redisTemplate.hasKey(MMatch_ID_CACHE))) {
//            List<Match> matches = matchRepository.findAll();
            List<Match> matches = matchRepository.findRandomLimitOffset(0, 10);

            List<String> matche_ids = matches.stream()
                    .map(m -> m.getId().toString())
                    .collect(Collectors.toList());

            redisTemplate.opsForValue().set(MMatch_ID_CACHE, matche_ids);
        }

    }

    @Transactional
    public void setupMemberMatchRelationships() {
        List<String> memberIds = (List<String>) redisTemplate.opsForValue().get(MemberM_ID_CACHE);
        List<String> matchIds = (List<String>) redisTemplate.opsForValue().get(MMatch_ID_CACHE);

        List<Member> me = memberRepository.findRandomLimit(10);
        List<String> test = me.stream()
                .map(m -> m.getId().toString())
                .collect(Collectors.toList());

        String sql = "INSERT IGNORE INTO member_match (member_id, match_id) VALUES (?, ?)";

        for (String memberId : test) {
            Set<String> memberMatches = getRandomMatches(matchIds); // Lấy các match ngẫu nhiên cho mỗi member

            for (String matchId : memberMatches) {
                jdbcTemplate.update(sql, memberId, matchId);
            }
        }
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
