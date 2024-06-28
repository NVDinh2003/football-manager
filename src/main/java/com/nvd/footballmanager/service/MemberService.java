package com.nvd.footballmanager.service;

public class MemberService {
<<<<<<< Updated upstream
=======

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final MembershipRequestRepository requestRepository;

    private final MemberMapper memberMapper;
    private final TeamMapper teamMapper;


    @Transactional
    public MemberDTO addMember(UUID teamId, UUID userId, MemberDTO memberDTO) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        // chekc if user is already a member of team
        Optional<Member> existingMember = memberRepository.findByUserIdAndTeamId(userId, teamId);
        if (existingMember.isPresent()) {
            throw new IllegalArgumentException(Constants.ALLREADY_MEMBER);
        }

        Member member = memberMapper.convertToEntity(memberDTO);
        member.setTeam(team);
        member.setUser(user);

        Member savedMember = memberRepository.save(member);

        return memberMapper.convertToDTO(savedMember);
    }

    public MemberDTO getMemberInfo(UUID teamId, UUID memberId) {
        teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        Member member = memberRepository.findByIdAndTeamId(memberId, teamId)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        return memberMapper.convertToDTO(member);
    }

    @Transactional
    public TeamDTO removeMember(UUID teamId, UUID memberId) {

        if (isNotManagerPermission(teamId)) {
            throw new AccessDeniedException(Constants.NOT_MANAGER_PERMISSION);
        }

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        Member member = memberRepository.findByIdAndTeamId(memberId, teamId)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        UUID userId = member.getUser().getId();

        // xóa luôn MembershipRequest của user mới remove để test lại send request
        MembershipRequest request = requestRepository.findByUserIdAndTeamId(userId, teamId)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));
        requestRepository.delete(request);

        memberRepository.delete(member);

        return teamMapper.convertToDTO(team);
    }

    public List<MemberDTO> getAllMembers(UUID teamId) {
        teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));
        List<Member> listMembers = memberRepository.findAllByTeamId(teamId);
        return memberMapper.convertListToDTO(listMembers);
    }

    @Transactional
    public MemberDTO updateMemberInfo(UUID teamId, UUID memberId, MemberDTO memberDTO) {
        teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        Member member = memberRepository.findByIdAndTeamId(memberId, teamId)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));


        Member updatedMember = memberMapper.updateEntity(memberDTO, member);
        memberRepository.save(updatedMember);

        return memberMapper.convertToDTO(updatedMember);
    }

    public boolean isNotManagerPermission(UUID teamId) {
        Optional<Member> member = memberRepository.findByUserIdAndTeamId(userService.getCurrentUser().getId(), teamId);
        return !(member.isPresent() && member.get().getRole().equals(MemberRole.MANAGER));
    }

    public Optional<Member> currentUserMemberInTeam(UUID teamId) {
        teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));
        UUID currentUserId = userService.getCurrentUser().getId();
        return memberRepository.findByUserIdAndTeamId(currentUserId, teamId);
    }

>>>>>>> Stashed changes
}
