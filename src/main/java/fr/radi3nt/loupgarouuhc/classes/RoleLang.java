package fr.radi3nt.loupgarouuhc.classes;

public class RoleLang {

    private final String id;
    private final String name;
    private final String short_desc;
    private final String desc;

    public RoleLang(String id, String name, String short_desc, String desc) {
        this.id = id;
        this.name = name;
        this.short_desc = short_desc;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getShortDesc() {
        return short_desc;
    }

    public String getDesc() {
        return desc;
    }

    public String getId() {
        return id;
    }
}
