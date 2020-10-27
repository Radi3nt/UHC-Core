package fr.radi3nt.loupgarouuhc.modifiable.roles;

import fr.radi3nt.loupgarouuhc.classes.lang.lang.Languages;
import fr.radi3nt.loupgarouuhc.classes.lang.lang.RoleLang;

import java.util.Objects;

public class RoleIdentity {

    private final String id;
    private final WinType winType;
    private final RoleType roleType;

    public RoleIdentity(String id, WinType winType, RoleType roleType) {
        this.id = id;
        this.winType = winType;
        this.roleType = roleType;
    }

    public WinType getWinType() {
        return winType;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public String getId() {
        return id;
    }

    public String getName(Languages language) {
        for (RoleLang roleLang : language.getRoleAspects()) {
            if (roleLang.getId().equals(this.getId())) {
                return roleLang.getName();
            }
        }
        return language.getDefaultMessage();
    }

    public String getShortDescription(Languages language) {
        for (RoleLang roleLang : language.getRoleAspects()) {
            if (roleLang.getId().equals(this.getId())) {
                return roleLang.getShortDesc();
            }
        }
        return language.getDefaultMessage();
    }

    public String getRoleDescription(Languages language) {
        for (RoleLang roleLang : language.getRoleAspects()) {
            if (roleLang.getId().equals(this.getId())) {
                return roleLang.getDesc();
            }
        }
        return language.getDefaultMessage();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleIdentity that = (RoleIdentity) o;
        return id.equals(that.id) &&
                winType == that.winType &&
                roleType == that.roleType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, winType, roleType);
    }
}
