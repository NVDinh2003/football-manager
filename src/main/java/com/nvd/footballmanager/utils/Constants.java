package com.nvd.footballmanager.utils;

public class Constants {

    public static final String DATA_NOT_FOUND = "DATA_NOT_FOUND";
    public static final String SUCCESS = "SUCCESS";
    public static final String CREATED = "CREATED";
    public static final String INVALID_USERNAME_PASSWORD = "Invalid username or password";
    public static final String BAD_REQUEST = "BAD_REQUEST";
    public static final String NOT_ACTIVE = "NOT_ACTIVE";
    public static final String EMAIL_ALREADY_EXISTS = "EMAIL_ALREADY_EXISTS";
    public static final String USERNAME_ALREADY_EXISTS = "USERNAME_ALREADY_EXISTS";
    public static final String PHONE_NUMBER_IS_ALREADY_IN_USE = "PHONE_NUMBER_IS_ALREADY_IN_USE";
    public static final String OBJECT_DELETED = "OBJECT_DELETED";
    public static final String NOT_MANAGER_PERMISSION = "Permission denied. Only team managers can do this !";
    public static final String ACCESS_DENIED = "Access denied. You don't have permission to do this !";
    public static final String ALREADY_MEMBER = "User is already a member of the team";
    public static final String NOT_JOIN_ANY_TEAM = "You must join a team first to create a match request";
    public static final String ALREADY_SEND_REQUEST = "You're already send request to this team";
    public static final Long EXPIRATION_TIME = 900000L;
    public static final String TOKEN_PREFIX = "Bearer  ";
    public static final String HEADER_STRING = "Authorization";
    public static final String NOT_ACCEPTED = "NOT_ACCEPTED";
    public static final String OBJECT_ALREADY_EXISTS = "OBJECT_ALREADY_EXISTS";
    public static final String ENTITY_NOT_FOUND = "Entity not found brủh!";
    public static final String ALREADY_MANAGER_OF_TEAM = "You are currently a manager of a team, you cannot create a new team!";
    public static final String SYSTEM = "System";
    public static final String MATCH_ALREADY_COMFIRMED = "Match result has been confirmed.";
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_PAGE_NUMBER = 1;
    public static final int NOTI_PAGE_SIZE = 20;
    public static final int MAX_TEAMS_PER_USER = 3;
    public static final String MAX_TEAMS_LIMIT_REACHED_MESSAGE = "User has reached the maximum number of teams allowed (" + MAX_TEAMS_PER_USER + ").";
    public static final int RANK_POINT_RANGE = 6;
    public static final String DESTINATION_PUSH_NOTI = "/topic/notifications";
}
