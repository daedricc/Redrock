package com.climpy.profile.moderator;

import com.climpy.profile.moderator.event.PlayerStartFollowingEvent;
import com.climpy.profile.moderator.event.PlayerStopFollowingEvent;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.*;

@Getter
public class FollowManager {
    private Map<UUID, UUID> followings;

    public FollowManager() {
        this.followings = new HashMap<>();
    }

    public void startFollowing(UUID followerPlayer, UUID followingPlayer) {
        this.followings.put(followerPlayer, followingPlayer);

        PlayerStartFollowingEvent playerStartFollowingEvent = new PlayerStartFollowingEvent(followerPlayer, followingPlayer);
        Bukkit.getPluginManager().callEvent(playerStartFollowingEvent);
    }

    public void stopFollowing(UUID followerPlayer) {
        UUID followingPlayer = this.followings.get(followerPlayer);
        if (followingPlayer == null) {
            return;
        }

        PlayerStopFollowingEvent playerStopFollowingEvent = new PlayerStopFollowingEvent(followerPlayer, followingPlayer);
        Bukkit.getPluginManager().callEvent(playerStopFollowingEvent);
    }

    public Set<UUID> getFollowers(UUID fallowingPlayer) {
        Set<UUID> followers = new HashSet<>();
        this.followings.forEach((follower, followed) -> {
            if (followed == fallowingPlayer) {
                followers.add(follower);
            }

        });

        return followers;
    }

    public UUID getFollowing(UUID uniqueUUID) {
        return this.followings.get(uniqueUUID);
    }

    public Set<UUID> getFollowings(UUID follower) {
        Set<UUID> followings = new HashSet<>();
        this.followings.forEach((cache, following) -> {
            if (cache.equals(follower)) {
                followings.add(following);
            }

        });

        return followings;
    }
}
