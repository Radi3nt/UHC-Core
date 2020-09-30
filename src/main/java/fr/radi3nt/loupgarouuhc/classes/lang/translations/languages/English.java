package fr.radi3nt.loupgarouuhc.classes.lang.translations.languages;

public enum English {
    ASSASSIN("Assassin", "You win alone", "Ton but est de gagner seul, tu a les livres power 3, sharpness 3, protection 3 et aussi un effet de force le jour."),
    LGBLANC("White werewolf", "You must betray the wolfs", "Tu possède 15 coeurs, son but est de gagner seul, tu connait la liste complète des loup-garou et les autres loups doivent donc se méfier de toi."),

    LOUP_GAROU("Werewolf", "You can kill villagers at night", "You must eliminate all the villager roles, to help you, during the night you have Strenght 1."),
    LOUP_PERFIDE("Loup perfide", "You're invisible the night", "Tu gagne avec les loup-garous, tu possède un effet de weakness et invisibility la nuit, tu peut voir les particules de la petite fille si celle-ci est invisible"),
    MONTREUR_OURS("Bear shower", "Grrrrrrrr", "Tu gagne avec le village, à chaque début d’épisode, l’ours va grogner. Si l’ours grogne, cela veut dire que l’une des personnes qui se trouvent à moins de 20 blocs de lui est un loup garou. L'ours grognera antant de fois que le nombre de loup garou dans son rayon d'action."),
    PETITE_FILLE("Little girl", "You're invisible the night", "Pendant la nuit, elle dispose de l’effet d’invisibilité pour éspionner les loup garous, elle peut voir les particules du loup-garou perfide si celui-ci est invisible."),
    SORCIERE("Witch", "You can respawn someone", "Tu gagne avec le village, tu peux voir les messages morts avant tout le monde et tu as une possibilité d’en ressusciter une par partie, tu possède aussi 3 splash potion de damage, 3 splash potions de heal et une splash potion regeneration."),
    VILLAGER("Villager", "You don't have special power", "You don't have any special powers, only your deductive abilities will enable you to identify the werewolves."),
    VOYANTE("Seer", "You can see roles of players", "Tu peux voir le rôle d’une personne a chaque début d’épisode. Tu as aussi 4 bibliothèques et 4 block d’obsidienne."),
    VP_LOUP("Nasty little wolf", "You have speed the night", "You have to kill all the villagers, during the night you've got the Strength 1 effect and you also got Speed 1 effect la nuit. Every time you kill someone, you get 2 additional heart for 1 min."),
    RENARD("Renard", "You have speed", "You hauring th you've got the Strength 1 effect and you also got Speed 1 effect la nuit. Every time you kill someone, you get 2 additional heart for 1 min."); //todo


    public static String id = "English";

    private final String name;
    private final String short_desc;
    private final String desc;

    English(String name, String short_desc, String desc) {
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
}
