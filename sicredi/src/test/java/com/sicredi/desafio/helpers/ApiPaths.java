package com.sicredi.desafio.helpers;

import static com.sicredi.desafio.constants.ApiConstants.BASE;
import static com.sicredi.desafio.helpers.TestConstants.TOPICS;

public class ApiPaths {

    private ApiPaths() {
    }

    public static String topic(long topicId) {
        return TOPICS + "/" + topicId;
    }

    public static String sessions(long topicId) {
        return topic(topicId) + "/sessions";
    }

    public static String session(long topicId, long sessionId) {
        return sessions(topicId) + "/" + sessionId;
    }

    public static String sessionRoot(long sessionId) {
        return BASE + "/sessions/" + sessionId;
    }

    public static String votes(long sessionId) {
        return sessionRoot(sessionId) + "/votes";
    }

    public static String vote(long sessionId, long voteId) {
        return votes(sessionId) + "/" + voteId;
    }

    public static String voteCount(long sessionId) {
        return sessionRoot(sessionId) + "/votes/count";
    }

    public static String checkOpen(long topicId) {
        return topic(topicId) + "/sessions:check-open";
    }

    public static String openNow(long sessionId) {
        return sessionRoot(sessionId) + "/open-now";
    }

    public static String topicRelative(long topicId) {
        return "/topics/" + topicId;
    }

    public static String topicsPage(int page, int size) {
        return TOPICS + "?page=" + page + "&size=" + size;
    }
}
