package com.climpy.profile.grant;

import com.climpy.redis.gson.JsonSerializer;
import com.google.gson.JsonObject;

public class GrantJsonSerializer implements JsonSerializer<Grant> {
    @Override
    public JsonObject serialize(Grant grant) {
        JsonObject object = new JsonObject();
        object.addProperty("uuid", grant.getUuid().toString());
        object.addProperty("rank", grant.getRank().getUniqueUUID().toString());
        object.addProperty("addedBy", grant.getAddedBy() == null ? null : grant.getAddedBy().toString());
        object.addProperty("addedAt", grant.getAddedAt());
        object.addProperty("addedReason", grant.getAddedReason());
        object.addProperty("duration", grant.getDuration());
        object.addProperty("removedBy", grant.getRemovedBy() == null ? null : grant.getRemovedBy().toString());
        object.addProperty("removedAt", grant.getRemovedAt());
        object.addProperty("removedReason", grant.getRemovedReason());
        object.addProperty("removed", grant.isRemoved());
        return object;
    }
}

