package com.climpy.profile.grant.procedure;

import com.climpy.profile.Profile;
import com.climpy.profile.grant.Grant;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

@Getter @Setter
public class GrantProcedure {
    private static Set<GrantProcedure> procedures = new HashSet<>();

    private Player issuer;
    private Profile recipient;

    private GrantProcedureType type;
    private GrantProcedureStage stage;

    private Grant grant;

    public GrantProcedure(Player issuer, Profile recipient, GrantProcedureType type, GrantProcedureStage stage) {
        this.issuer = issuer;
        this.recipient = recipient;
        this.type = type;
        this.stage = stage;
        procedures.add(this);
    }

    public void finish() {
        procedures.remove(this);
        this.recipient.saveProfile();
    }

    public void cancel() {
        procedures.remove(this);
    }

    public static GrantProcedure getByPlayer(Player player) {
        for (GrantProcedure procedure : procedures) {
            if (procedure.issuer.equals(player))
                return procedure;
        }
        return null;
    }

    public static Set<GrantProcedure> getProcedures() {
        return procedures;
    }
}
