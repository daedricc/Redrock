package com.climpy.profile.grant;

import com.climpy.profile.rank.Rank;
import com.climpy.utils.TimeUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter @Setter
public class Grant {
    public static GrantJsonSerializer SERIALIZER = new GrantJsonSerializer();

    public static GrantJsonDeserializer DESERIALIZER = new GrantJsonDeserializer();

    private UUID uuid;

    private Rank rank;

    private UUID addedBy;

    private long addedAt;

    private String addedReason;

    private long duration;

    private UUID removedBy;

    private long removedAt;

    private String removedReason;

    private boolean removed;

    public Grant(UUID uuid, Rank rank, UUID addedBy, long addedAt, String addedReason, long duration) {
        this.uuid = uuid;
        this.rank = rank;
        this.addedBy = addedBy;
        this.addedAt = addedAt;
        this.addedReason = addedReason;
        this.duration = duration;
    }

    public boolean isPermanent() {
        return duration == Integer.MAX_VALUE;
    }

    public boolean hasExpired() {
        return (!isPermanent()) && (System.currentTimeMillis() >= addedAt + duration);
    }

    public String getExpiresAtDate() {
        if (duration == Integer.MAX_VALUE) {
            return "Never";
        }

        return TimeUtil.dateToString(new Date(addedAt + duration), "&7");
    }

    public String getDurationText() {
        if (removed) {
            return "Removed";
        }

        if (isPermanent()) {
            return "Permanent";
        }

        return TimeUtil.millisToRoundedTime(duration);
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Grant && ((Grant) object).uuid.equals(uuid);
    }
}
