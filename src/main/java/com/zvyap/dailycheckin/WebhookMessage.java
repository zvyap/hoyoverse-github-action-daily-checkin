package com.zvyap.dailycheckin;

public enum WebhookMessage {
    WEBHOOK_TITLE("Daily check in task [{1}]"), // 0 - uid, 1 - in game name, 2 - region, 3 - level, for highest level account
    WEBHOOK_SUCCESS("Daily checked in successfully"),
    WEBHOOK_ALREADY_CHECK_IN("Already checked in"),
    WEBHOOK_FAILED("Daily check in failed - {0},{1}"), //message and retcode
    FIELD_GAME("Game"),
    FIELD_REWARD_TODAY("Reward Today"),
    FIELD_REWARD_TOMORROW("Reward Tomorrow"),
    FIELD_DAYS_CHECKED_IN("Days Checked In"),
    FIELD_DAYS_MISSING("Days Missing"),
    FIELD_OFFICIAL_MESSAGE("Official Message");

    private final String message;

    WebhookMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
