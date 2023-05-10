package com.zvyap.dailycheckin;

import com.zvyap.hoyoapi.GameType;
import com.zvyap.hoyoapi.exception.AlreadyCheckInException;
import com.zvyap.hoyoapi.feature.daily.DailyCheckInFeature;

import java.util.List;

public class HoyoDaily {

    public static void main(String[] args) {

    }

    public static void checkIn(DailyCheckInFeature feature, List<CheckInAction> actions) {
        for(CheckInAction action : actions) {
            for(GameType type : action.getGames()) {
                try{
                    feature.signDaily(type, action.getToken());
                    System.out.println(action.getToken().getLtuid() + " check in - successful [" + type + "]");
                }catch (Exception e) {
                    if(e instanceof AlreadyCheckInException) {
                        System.out.println(action.getToken().getLtuid() + " check in - already checked in [" + type + "]");
                        return;
                    }
                    System.out.println(action.getToken().getLtuid() + " check in - failed [" + type + "]");
                    e.printStackTrace();
                }
            }
        }
    }
}
