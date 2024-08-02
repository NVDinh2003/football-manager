package com.nvd.footballmanager.utils;

public class NotificationMessages {
    public static final String JOIN_TEAM_ACCEPTED_TITLE = "Your request to join team %s has been accepted!";
    public static final String JOIN_TEAM_ACCEPTED_CONTENT = "You are now a member of team %s.";
    public static final String JOIN_TEAM_REJECTED_TITLE = "Your request to join team %s has been rejected!";
    public static final String JOIN_TEAM_REJECTED_CONTENT = "Your request has been rejected by the team manager.";
    public static final String FAIL_SEND_NOTI_SIZE_LIMIT = "Failed to send notification due to size limit: {}";

    public static final String USER_SEND_MEMBERSHIP_REQUEST = " has sent a membership request to your team.";
    public static final String CONTENT_MATCH_RESULT = "Match between your team and %s has ended with score: %s %s - %s %s. Please confirm the result by click this link: %s/api/matches/%s/confirm-result";
}
