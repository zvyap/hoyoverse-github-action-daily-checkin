package com.zvyap.dailycheckin;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.zvyap.hoyoapi.APIEnvironment;
import com.zvyap.hoyoapi.GameType;
import com.zvyap.hoyoapi.HoyoGameRole;
import com.zvyap.hoyoapi.HoyoToken;
import com.zvyap.hoyoapi.HoyoverseAPI;
import com.zvyap.hoyoapi.exception.AlreadyCheckInException;
import com.zvyap.hoyoapi.exception.HoyoverseAPIException;
import com.zvyap.hoyoapi.exception.HoyoverseAPIRetCodeException;
import com.zvyap.hoyoapi.feature.daily.DailyCheckInFeature;
import com.zvyap.hoyoapi.response.HoyoDailyCheckInInfoResponse;
import com.zvyap.hoyoapi.response.HoyoDailyCheckInRewardResponse;
import com.zvyap.hoyoapi.response.HoyoDailyCheckInSignResponse;
import com.zvyap.hoyoapi.response.HoyoGamesDetailsResponse;
import com.zvyap.hoyoapi.util.Utils;

import java.awt.Color;
import java.text.MessageFormat;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HoyoDaily {

    public static void main(String[] args) {
        DailyCheckInFeature feature = new DailyCheckInFeature(new HoyoverseAPI(APIEnvironment.OVERSEA));
        webhookAction(feature, WebhookInfo.builder()
                        .url("https://canary.discord.com/api/webhooks/1106275612215222352/9J5cA_LrGC2aKHPvGvyaPWXYqaSHsj9Apiw3G_ZWeICIVtn0jglYKY9aCBaDzV4D2FPG")
                        .avatar("https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png")
                        .name("HoyoDaily")
                        .build(), GameType.GENSHIN_IMPACT, HoyoToken.of("90156679", "x8uYe30qVSDuUo0OZQiOkz2b4O3gLLD0DAVuk2L0"),
                null, new AlreadyCheckInException(-5003, "Testing"));
    }

    public static void checkIn(DailyCheckInFeature feature, List<CheckInAction> actions) {
        for (CheckInAction action : actions) {
            for (GameType type : action.getGames()) {
                long start = System.currentTimeMillis();
                try {
                    webhookAction(feature, action.getWebhook(), type, action.getToken(), feature.signDaily(type, action.getToken()), null);
                    printLog(action.getToken().getLtuid(), "successful", type, System.currentTimeMillis() - start);
                } catch (Exception e) {
                    if (e instanceof HoyoverseAPIException) {
                        webhookAction(feature, action.getWebhook(), type, action.getToken(), null, (HoyoverseAPIException) e);
                        System.out.println(e.getMessage());
                    } else {
                        e.printStackTrace();
                    }
                    printLog(action.getToken().getLtuid(), "failed", type, System.currentTimeMillis() - start);
                }
            }
        }
    }

    private static void printLog(String id, String status, GameType gameType, long time) {
        System.out.println(id + " check in - " + status + " [" + gameType + "] " + time + "ms");
    }

    private static void webhookAction(DailyCheckInFeature feature, WebhookInfo info, GameType type, HoyoToken token, HoyoDailyCheckInSignResponse response, HoyoverseAPIException exception) {
        if (info == null) {
            return;
        }

        Color color = Color.GREEN;
        String officialMessage = response != null ? response.getMessage() : exception.getMessage();
        if (exception != null) {
            if (exception instanceof AlreadyCheckInException) {
                color = Color.BLUE;
            } else {
                color = Color.RED;
            }
        }

        WebhookClient client = WebhookClient.withUrl(info.getUrl());
        HoyoGamesDetailsResponse.GameDetails details = getDetails(feature.getAPI(), type);
        String message = WebhookMessage.WEBHOOK_SUCCESS.getMessage();
        if(exception instanceof AlreadyCheckInException) {
            message = WebhookMessage.WEBHOOK_ALREADY_CHECK_IN.getMessage();
        }else if(exception instanceof HoyoverseAPIRetCodeException) {
            message = MessageFormat.format(WebhookMessage.WEBHOOK_FAILED.getMessage(), exception.getMessage(), ((HoyoverseAPIRetCodeException) exception).getRetcode());
        }else if(response.getRetcode() != 0) {
            message = MessageFormat.format(WebhookMessage.WEBHOOK_FAILED.getMessage(), exception.getMessage(), response.getRetcode());
        }


        String title = WebhookMessage.WEBHOOK_TITLE.getMessage();
        if(title.contains("{")) {
            HoyoGameRole role = feature.getAPI().getGameRoles(token, type).stream().min(Comparator.comparing(HoyoGameRole::getLevel)).orElse(null);
            if(role != null){
                title = MessageFormat.format(title, role.getGameUid(), role.getNickname(), role.getRegion(), role.getLevel());
            }
        }

        WebhookEmbedBuilder builder = new WebhookEmbedBuilder()
                .setAuthor(new WebhookEmbed.EmbedAuthor("Powered by - Hoyoverse-API",
                        "https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png",
                        "https://github.com/zvyap/Hoyoverse-API"))
                .setTimestamp(OffsetDateTime.now())
                .setColor(color.getRGB())
                .setTitle(new WebhookEmbed.EmbedTitle(title, null))
                .setFooter(new WebhookEmbed.EmbedFooter(details.getName(), details.getIcon()))
                .setDescription(message)
                .addField(new WebhookEmbed.EmbedField(true, WebhookMessage.FIELD_GAME.getMessage(), feature.getAPI().getEnvironment().getAPIConstant(type).getName()));

        HoyoDailyCheckInInfoResponse checkInInfoResponse = feature.getDailyInfo(type, token);
        List<HoyoDailyCheckInRewardResponse.AwardsItem> rewards = getAllReward(feature, type);
        HoyoDailyCheckInRewardResponse.AwardsItem today = rewards.get(checkInInfoResponse.getData().getTotalSignDay() - 1);
        HoyoDailyCheckInRewardResponse.AwardsItem tomorrow;
        if (rewards.size() > checkInInfoResponse.getData().getTotalSignDay()) {
            tomorrow = rewards.get(checkInInfoResponse.getData().getTotalSignDay());
        } else {
            tomorrow = rewards.get(0);
        }

        builder.setThumbnailUrl(today.getIcon());
        builder.addField(new WebhookEmbed.EmbedField(true, WebhookMessage.FIELD_DAYS_CHECKED_IN.getMessage(), String.valueOf(checkInInfoResponse.getData().getTotalSignDay())))
                .addField(new WebhookEmbed.EmbedField(true, WebhookMessage.FIELD_DAYS_MISSING.getMessage(), String.valueOf(checkInInfoResponse.getData().getSignCntMissed())))
                .addField(new WebhookEmbed.EmbedField(true, WebhookMessage.FIELD_REWARD_TODAY.getMessage(), itemWithCount(today.getName(), today.getCount())))
                .addField(new WebhookEmbed.EmbedField(true, WebhookMessage.FIELD_REWARD_TOMORROW.getMessage(), itemWithCount(tomorrow.getName(), tomorrow.getCount())))
                .addField(new WebhookEmbed.EmbedField(false, WebhookMessage.FIELD_OFFICIAL_MESSAGE.getMessage(), officialMessage));

        client.send(new WebhookMessageBuilder()
                .setAvatarUrl(info.getAvatar())
                .setUsername(info.getName())
                .addEmbeds(builder.build()).build());
        client.close();
    }


    private static List<HoyoGamesDetailsResponse.GameDetails> details;

    public static HoyoGamesDetailsResponse.GameDetails getDetails(HoyoverseAPI api, GameType type) {
        if (details == null) {
            details = api.getGameDetails();
        }

        return details.stream().filter(d -> d.getId().equals(String.valueOf(type.getGameId()))).findFirst().orElse(null);
    }

    private static Map<GameType, HoyoDailyCheckInRewardResponse> rewardResponsesMap = new HashMap<>();

    public static List<HoyoDailyCheckInRewardResponse.AwardsItem> getAllReward(DailyCheckInFeature feature, GameType type) {
        return Utils.ifNullGetEmptyList(rewardResponsesMap.computeIfAbsent(type, (t) -> feature.getAllReward(type)).getData().getAwards());
    }

    private static String itemWithCount(String name, int count) {
        return name + " x" + count;
    }
}
