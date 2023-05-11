package com.zvyap.dailycheckin;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class WebhookInfo {
    @NonNull
    private final String url;
    private final String name;
    private final String avatar;
}
