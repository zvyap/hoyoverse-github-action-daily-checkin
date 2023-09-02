# Github Action Hoyoverse Daily Check In

## Features
* Overseas server & Mainland China server ***supported***
* Multiaccount ***supported***
* Multigame ***supported***
  - Genshin Impact
  - Honkai Impact 3RD
  - Honkai: Star Rail
  - Tears of Themis
* Auto checked in every day
* [Discord Webhook](https://github.com/zvyap/hoyoverse-github-action-daily-checkin/wiki/Discord-Webhook)
* You can choose to update -> [HERE~](https://github.com/zvyap/hoyoverse-github-action-daily-checkin/wiki/Update-this-project-to-your-fork)

## How to use?
* Create a fork of this project
* Get the `id` and `token` of the account you want to check in daily
  - For those don't know how to get `id` and `token` -> [Click Me!](https://github.com/zvyap/Hoyoverse-API/wiki/Get-hoyoverse-API-token-(Hoyolab-Miyoushe))
* Go to `./src/test/java/com/zvyap/hoyoapi/test/CheckInDailyTest.java`
* Click `Edit this file` button
![image](https://github.com/zvyap/hoyoverse-github-action-daily-checkin/assets/52874570/6dee0268-d8c2-47fe-81a6-4eaf9ad9cf55)
* Change what game you want (just copy paste the line of code ***OR*** delete the line
![image](https://github.com/zvyap/hoyoverse-github-action-daily-checkin/assets/52874570/b812d3c2-57c0-4d13-a149-565cece9f398)
![image](https://github.com/zvyap/hoyoverse-github-action-daily-checkin/assets/52874570/0073a9a5-08c1-4e7e-aaf7-fb3664850cb2)
![image](https://github.com/zvyap/hoyoverse-github-action-daily-checkin/assets/52874570/1d81dff9-d733-4b4d-9152-d294f7eba9f5)
* GameType's name please follow [here](https://github.com/zvyap/Hoyoverse-API/blob/master/src/main/java/com/zvyap/hoyoapi/GameType.java) [GENSHIN_IMPACT, HONKAI_IMPACT_3RD, HONKAI_STAR_RAIL, TEARS_OF_THEMIS]
* For those only use for only account, you going to end here [If not scroll downward]
* Just click `Commit changes...` at bottom right & click `Commit changes`
![image](https://github.com/zvyap/hoyoverse-github-action-daily-checkin/assets/52874570/20756b0d-1f48-4dc6-a1cd-8a29e3a62162)
![image](https://github.com/zvyap/hoyoverse-github-action-daily-checkin/assets/52874570/a1490ec8-909d-43f8-aacd-06c77f6489f8)

## Put the token into github (LAST STEP)
* Click `Settings` and click `Secrets and variables` -> `Actions` -> `New repository secret`

![image](https://github.com/zvyap/hoyoverse-github-action-daily-checkin/assets/52874570/589f1a3a-ba94-48d3-8a34-0b31497a9abf)

![image](https://github.com/zvyap/hoyoverse-github-action-daily-checkin/assets/52874570/b0c46819-dce8-4b66-88ea-05a5e01a3f0a)

![image](https://github.com/zvyap/hoyoverse-github-action-daily-checkin/assets/52874570/30e8c938-4a1e-4971-ad98-8d30db536191)

![image](https://github.com/zvyap/hoyoverse-github-action-daily-checkin/assets/52874570/8245907c-5b78-4c2b-8e9c-bb0d76b31bf3)
* Follow the picture below
![image](https://github.com/zvyap/hoyoverse-github-action-daily-checkin/assets/52874570/36197c2e-0698-4bb2-9e09-18d673beb4f7)
![image](https://github.com/zvyap/hoyoverse-github-action-daily-checkin/assets/52874570/5883c6c8-a47d-410d-b45f-e92b04649b6e)

* If you got extra account, repeat the step with the secret you put in the `gradle.yml` file

## Multiaccount
* Copy this block of code
```java
                CheckInAction.builder()
                        .token(HoyoToken.of(System.getenv("USER_1_ID"), System.getenv("USER_1_TOKEN")))
                        .game(GameType.GENSHIN_IMPACT)
                        .game(GameType.HONKAI_STAR_RAIL)
                        .build()
```
* Paste it below the original code block in the file, also a `,` at the end of the previous code
![image](https://github.com/zvyap/hoyoverse-github-action-daily-checkin/assets/52874570/58efce14-4546-4084-bbcf-c4496a954084)
* ONLY THE LAST PART OF THE CODE DOESN'T NEED `,`
* Edit the `USER_1_ID` and `USER_1_TOKEN` to other number ***[IMPORTANT]***
  - Example: second user -> `USER_2_ID` and `USER_2_TOKEN`
  - Example: thrid user -> `USER_3_ID` and `USER_3_TOKEN`
  - Make sure it don't repeat
* Click `Commit changes...` at bottom right & click `Commit changes`
* Go to `/.github/workflows/gradle.yml`
* Click `Edit this file` button
![image](https://github.com/kissnavel/hoyoverse-github-action-daily-checkin/assets/53962152/23cd4a32-138a-4ac9-abbe-81ffd9f7a719)
* Add the extra user secret (Remember the secret name you put)
![image](https://github.com/zvyap/hoyoverse-github-action-daily-checkin/assets/52874570/1926c27f-8f68-4021-9d95-64c11a2babb5)
* Click `Commit changes...` at bottom right & click `Commit changes`

## Check is it working
* Go to `Actions` tab
* If `Actions` is not enabled, just enable it
* Select `Daily Check In Task`
![image](https://github.com/zvyap/hoyoverse-github-action-daily-checkin/assets/52874570/2029ffe5-6ef2-4892-a182-224e57de76b8)
* Click `Run workflow` -> `Run workflow`
![image](https://github.com/zvyap/hoyoverse-github-action-daily-checkin/assets/52874570/a1e4dfb7-4024-4cef-b43f-5e3cbe3d96e1)
![image](https://github.com/zvyap/hoyoverse-github-action-daily-checkin/assets/52874570/5104281a-b4aa-45b5-be76-d7ae14466877)
* If you see a green tick beside them, it mean checkin supported
![image](https://github.com/zvyap/hoyoverse-github-action-daily-checkin/assets/52874570/2a80e30d-df1b-4763-a3e2-1f57365d29e2)
* And it run every midnight 12:15
