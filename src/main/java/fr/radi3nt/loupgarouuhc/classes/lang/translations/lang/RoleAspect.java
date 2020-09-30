package fr.radi3nt.loupgarouuhc.classes.lang.translations.lang;

import fr.radi3nt.loupgarouuhc.classes.roles.RoleSort;

public class RoleAspect {

    private final String id;
    private final String name;
    private final String short_desc;
    private final String desc;

    public RoleAspect(String id, String name, String short_desc, String desc) {
        this.id = id;
        this.name = name;
        this.short_desc = short_desc;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getShort_desc() {
        return short_desc;
    }

    public String getDesc() {
        return desc;
    }

    public RoleSort getRoleSort() {
        for (RoleSort value : RoleSort.values()) {
            if (value.getId().equals(id))
                return value;
        }
        return null;
    }

    public String getId() {
        return id;
    }
}
