package fr.radi3nt.loupgarouuhc.classes.lang.translations;

import java.util.ArrayList;

public enum Lang {

    VILLAGER("Villager"),
    LOUP_GAROU("LoupGarou"),

    PETITE_FILLE("PetiteFille"),
    VP_LOUP("VPLoup"),
    VOYANTE("Voyante"),
    LGBLANC("LGBlanc"),
    MONTREUR_OURS("MontreurOurs"),
    ASSASSIN("Assassin"),
    LOUP_PERFIDE("LoupPerfide"),
    SORCIERE("Sorciere"),
    MINEUR("Mineur"),
    LOUP_FEUTRE("LoupFeutre"),
    RENARD("Renard"),
    GUARD("Guard"),
    LG_INFECTE("LGInfect"),
    CUPIDON("Cupidon"),
    ;

    private String name;
    private String short_desc;
    private String desc;
    private final String id;

    Lang(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDesc() {
        return short_desc;
    }

    public void setShortDesc(String short_desc) {
        this.short_desc = short_desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return id;
    }
    public ArrayList<String> getIds() {
        ArrayList<String> array = new ArrayList<>();
        for (Lang values : values()) {
            array.add(values.id);
        }
        return array;
    }
}
