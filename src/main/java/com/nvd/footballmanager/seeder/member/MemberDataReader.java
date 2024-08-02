//package com.nvd.footballmanager.seeder.member;
//
//import com.github.javafaker.Faker;
//import com.nvd.footballmanager.filters.MemberFilter;
//import com.nvd.footballmanager.model.entity.Member;
//import com.nvd.footballmanager.model.entity.Team;
//import com.nvd.footballmanager.model.entity.User;
//import com.nvd.footballmanager.model.enums.MemberRole;
//import com.nvd.footballmanager.repository.MemberRepository;
//import com.nvd.footballmanager.repository.TeamRepository;
//import com.nvd.footballmanager.repository.UserRepository;
//import com.nvd.footballmanager.seeder.GenericDataReader;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.core.task.TaskExecutor;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.UUID;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Future;
//
//@Component
//public class MemberDataReader implements GenericDataReader<Member> {
//    private static final ThreadLocal<Faker> fakerThreadLocal = ThreadLocal.withInitial(Faker::new);
//    private final UserRepository userRepository;
//    private final TeamRepository teamRepository;
//    private final MemberRepository memberRepository;
//    private final RedisTemplate<String, Object> redisTemplate;
//    private final TaskExecutor taskExecutor;
//
//    private static final String MANAGER_KEY = "manager_cache";
//    private static final String POSITION_CACHE_KEY = "position_cache";
//    private static final String TEAM_CACHE_KEY = "team_cache";
//    private static final String USER_CACHE_KEY = "user_cache";
//
//    public MemberDataReader(UserRepository userRepository, TeamRepository teamRepository,
//                            RedisTemplate<String, Object> redisTemplate,
//                            @Qualifier("dataExecutor") TaskExecutor taskExecutor,
//                            MemberRepository memberRepository) {
//        this.userRepository = userRepository;
//        this.teamRepository = teamRepository;
//        this.redisTemplate = redisTemplate;
//        this.taskExecutor = taskExecutor;
//        this.memberRepository = memberRepository;
//    }
//
//    @PostConstruct  // được gọi ngay sau khi các bean duoc khoi tao va các dependency da ược inject
//    public void init() {
//        if (Boolean.FALSE.equals(redisTemplate.hasKey(MANAGER_KEY))) {
//            List<Member> managers = memberRepository.findAllByRole(MemberRole.MANAGER); // set users co role MANAGER to cache
//            redisTemplate.opsForSet().add(MANAGER_KEY, managers.stream()
//                    .map(member -> member.getUser().getId().toString()).toArray());
//        }
//
//        if (Boolean.FALSE.equals(redisTemplate.hasKey(POSITION_CACHE_KEY))) {
//            List<String> positions = Arrays.asList(
//                    "Goalkeeper", "Defender", "Midfielder", "Forward",
//                    "Wing-back", "Centre-back", "Full-back", "Sweeper",
//                    "Attacking Midfielder", "Defensive Midfielder"
//            );
//            redisTemplate.opsForValue().set(POSITION_CACHE_KEY, positions);
//        }
//
////        cacheTeams();
////        cacheUsers();
//    }
//
////    public void cacheUsers() {
////        if (Boolean.FALSE.equals(redisTemplate.hasKey(USER_CACHE_KEY))) {
////            List<User> users = userRepository.findRandomLimit(18000);
////            redisTemplate.opsForValue().set(USER_CACHE_KEY, users.stream()
////                    .map(BaseModel::getId).toArray());
////        }
////    }
////
////    public void cacheTeams() {
////        if (Boolean.FALSE.equals(redisTemplate.hasKey(TEAM_CACHE_KEY))) {
////            Pageable limit = PageRequest.of(0, 693);
////            List<Team> teams = teamRepository.findAll(limit).getContent();
////            redisTemplate.opsForValue().set(TEAM_CACHE_KEY, teams.stream()
////                    .map(BaseModel::getId).toArray());
////        }
////    }
//
//    @Override
//    public List<Member> generateDataBatch(int batchSize) {
//        Pageable teamLimit = Pageable.ofSize(693);
//        List<Team> teams = teamRepository.findAll(teamLimit).getContent();
//        List<User> users = userRepository.findRandomLimit(batchSize);
//        List<Team> teams = teamRepository.findRandomLimit(batchSize);
//        List<Future<Member>> futures = new ArrayList<>();
//        for (int i = 0; i < batchSize; i++) {
//            futures.add(((ThreadPoolTaskExecutor) taskExecutor).submit(() -> generateData(teams, users)));
//        }
//        List<Member> members = new ArrayList<>(batchSize);
//        for (Future<Member> future : futures) {
//            try {
//                members.add(future.get());
//            } catch (InterruptedException | ExecutionException e) {
//                Thread.currentThread().interrupt();
//            }
//        }
//        return members;
//    }
//
//    //    @Override
//    public Member generateData(List<Team> teams, List<User> users) {
//        Faker faker = fakerThreadLocal.get();
//        Member member = new Member();
//        member.setId(UUID.randomUUID());
//        member.setNickname(faker.esports().player());
//
//        // random position
//        List<String> positions = (List<String>) redisTemplate.opsForValue().get(POSITION_CACHE_KEY);
//        String randomPosition = positions.get(faker.random().nextInt(positions.size()));
//        member.setPosition(randomPosition);
//
//        member.setShirtNumber(faker.number().numberBetween(1, 30));
//
////        List<UUID> teamIds = (List<UUID>) redisTemplate.opsForValue().get(TEAM_CACHE_KEY);
////        List<UUID> userIds = (List<UUID>) redisTemplate.opsForValue().get(USER_CACHE_KEY);
////        UUID tId = teamIds.get(faker.random().nextInt(teamIds.size()));
////        Team team = teamRepository.findById(tId).get();
////        UUID uId = teamIds.get(faker.random().nextInt(userIds.size()));
////        User user = userRepository.findById(uId).get();
//
//        Team team = teams.get(faker.random().nextInt(teams.size()));
//        User user = users.get(faker.random().nextInt(users.size()));
//        MemberFilter memberFilter = new MemberFilter();
//        memberFilter.setTeamId(team.getId());
//        boolean teamAlreadyHasManager = memberRepository.findAllWithFilter(null, memberFilter).getContent().stream()
//                .anyMatch(m -> m.getRole() == MemberRole.MANAGER);
//
//        // neu team chua có ai la MANAGER va user chua la MANAGER cua team nao
//        if (!teamAlreadyHasManager && !isUserManagerInAnyTeam(user.getId())) {
//            member.setRole(MemberRole.MANAGER);
//            redisTemplate.opsForSet().add(MANAGER_KEY, user.getId().toString());
//        } else {
//            member.setRole(faker.options().option(MemberRole.MEMBER, MemberRole.EXTERNAL_MEMBER));
//            if (member.getRole() == MemberRole.EXTERNAL_MEMBER)
//                member.setFee(faker.number().randomDouble(0, 250000, 1000000));
//        }
//
//        member.setUser(user);
//        member.setTeam(team);
//
//        return member;
//    }
//
//    private boolean isUserManagerInAnyTeam(UUID userId) {
//        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(MANAGER_KEY, userId.toString()));
//    }
//
//
//    @Override
//    public Member generateData() {
//        return null;
//    }
//}
//
//
