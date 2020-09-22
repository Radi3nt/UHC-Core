package fr.radi3nt.loupgarouuhc.classes.lang.translations.lang;

public enum Francais {

    ASSASSIN("Assassin", "Tu gagne seul", "Ton but est de gagner seul, tu a les livres power 3, sharpness 3, protection 3 et aussi un effet de force le jour."),
    LGBLANC("Loup garou blanc", "Tu doit trahir les loup garous", "Tu possède 15 coeurs, son but est de gagner seul, tu connait la liste complète des loup-garou et les autres loups doivent donc se méfier de toi."),

    LOUP_GAROU("Loup garou", "Tu peut tuer les villageois la nuit", "Tu doit éliminer tout les villagois, pour t'aider dans ta quête tu possèdes l'effet Strength 1 la nuit."),
    LOUP_PERFIDE("Loup perfide", "Tu est invisible la nuit", "Tu gagne avec les loup-garous, tu possède un effet de weakness et invisibility la nuit, tu peut voir les particules de la petite fille si celle-ci est invisible"),
    MONTREUR_OURS("Montreur d'ours", "Grrrrrrrr", "Tu gagne avec le village, à chaque début d’épisode, l’ours va grogner. Si l’ours grogne, cela veut dire que l’une des personnes qui se trouvent à moins de 20 blocs de lui est un loup garou. L'ours grognera antant de fois que le nombre de loup garou dans son rayon d'action."),
    PETITE_FILLE("Petite fille", "Tu devient invisible la nuit", "Pendant la nuit, tu dispose de l’effet d’invisibilité pour éspionner les loup garous, tu peut voir les particules du loup garou perfide si celui-ci est invisible."),
    SORCIERE("Sorcière", "Tu peut ressucsiter quelqu'un", "Tu gagne avec le village, tu peux voir les messages morts avant tout le monde et tu as une possibilité d’en ressusciter une par partie, tu possède aussi 3 splash potion de damage, 3 splash potions de heal et une splash potion regeneration."),
    VILLAGER("Villageois", "Tu n’as aucun pouvoir spécial", "Tu n’as aucun pouvoir spécial, seul ton esprit de déduction te permettra de découvrir les loup-garous."),
    VOYANTE("Voyante", "Tu peut voir le role des joueurs", "Tu peux voir le rôle d’une personne a chaque début d’épisode. Tu as aussi 4 bibliothèques et 4 block d’obsidienne."),
    VP_LOUP("Vilain petit loup", "Tu a un effet de speed la nuit", "Tu dois tuer tous les villageois, tu as l'effet Strength 1 la nuit ainsi que l'effet Speed 2 la nuit, à chaque kill que tu fais tu obtiens 2 coeurs en plus pendant 1 min."),
    MINEUR("Mineur", "Tu mine bien !!", "Tu gagne avec le village, tu te souviens d'avoir laissé tombé une pioche en diamand tout pres d'ici .... Tiens la voilà."),
    LOUP_FEUTRE("Loup feutré", "Tu es discret", "A chaque episode, tu a un role d'affichage. Si la voyante, le montreur d'ours, ou un role permettant de voir ceux des autres, il ne verra que ton role d'achage."),
    ;

    public static String id = "Francais";

    private final String name;
    private final String short_desc;
    private final String desc;

    Francais(String name, String short_desc, String desc) {
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
