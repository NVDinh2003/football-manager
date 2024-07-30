//package com.nvd.footballmanager.controller.test;
//
//import com.nvd.footballmanager.model.entity.Member;
//import com.nvd.footballmanager.model.entity.Team;
//import com.nvd.footballmanager.model.entity.User;
//import com.nvd.footballmanager.model.enums.MemberRole;
//import com.nvd.footballmanager.repository.MemberRepository;
//import com.nvd.footballmanager.repository.TeamRepository;
//import com.nvd.footballmanager.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.messaging.simp.annotation.SendToUser;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.util.HtmlUtils;
//
//import java.security.Principal;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//@Controller
//public class MessageController {
//
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private MemberRepository memberRepository;
//    @Autowired
//    private TeamRepository teamRepository;
//
//    @MessageMapping("/team-message")
//    public void sendTeamMessage(String message, Principal principal) {
//        User user = userRepository.findByUsername(principal.getName()).get();
//        Optional<Member> sender = memberRepository.findByRoleAndUserId(MemberRole.MANAGER, user.getId());
//        if (sender.isPresent()) {
//            Team team = sender.get().getTeam();
//            for (Member member : team.getMembers()) {
//                if (!member.equals(sender)) {
//                    messagingTemplate.convertAndSendToUser(
//                            member.getUser().getUsername(),
//                            "/topic/team-messages",
//                            new MessageDTO(message, sender.get().getUser().getName())
//                    );
//                }
//            }
//        } else {
//            messagingTemplate.convertAndSendToUser(
//                    principal.getName(),
//                    "/topic/errors",
//                    "Only managers can send team messages."
//            );
//        }
//    }
//
//    @MessageMapping("/manager-message")
//    public void sendMessageToManager(MessageRequest request, Principal principal) {
//        Member sender = memberService.getMemberById(request.getMemberId());
//        if (sender.getUser().getUsername().equals(principal.getName())) {
//            Member manager = memberService.getTeamManager(sender.getTeam().getId());
//            if (manager != null) {
//                messagingTemplate.convertAndSendToUser(
//                        manager.getUser().getUsername(),
//                        "/topic/manager-messages",
//                        new MessageResponse(request.getMessage(), sender.getNickname())
//                );
//            }
//        } else {
//            messagingTemplate.convertAndSendToUser(
//                    principal.getName(),
//                    "/topic/errors",
//                    "You don't have permission to send messages as this member."
//            );
//        }
//    }
//
//    @MessageMapping("/get-user-members")
//    @SendToUser("/topic/user-members")
//    public List<MemberInfo> getUserMembers(Principal principal) {
//        return memberService.getMembersByUsername(principal.getName());
//    }
//
//
//    @MessageMapping("/message")
//    @SendTo("/topic/messages")
//    public Map<String, String> getMessage(final String message) throws InterruptedException {
//        Thread.sleep(1000);
//        Map<String, String> response = new HashMap<>();
//        response.put("content", HtmlUtils.htmlEscape(message));
//        return response;
//    }
//
//    @MessageMapping("/private-message")
//    @SendToUser("/topic/private-messages")
//    public Map<String, String> getPrivateMessage(final String message, final Principal principal) throws InterruptedException {
//        Thread.sleep(1000);
//        Map<String, String> response = new HashMap<>();
//        response.put("content", HtmlUtils.htmlEscape("Private message to user " + principal.getName() + ": " + message));
//        return response;
//    }
//}
