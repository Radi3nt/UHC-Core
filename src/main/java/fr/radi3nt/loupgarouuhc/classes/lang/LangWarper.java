package fr.radi3nt.loupgarouuhc.classes.lang;

import fr.radi3nt.loupgarouuhc.classes.lang.translations.Lang;
import fr.radi3nt.loupgarouuhc.classes.lang.translations.lang.English;
import fr.radi3nt.loupgarouuhc.classes.lang.translations.lang.Francais;
import sun.reflect.generics.repository.FieldRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class LangWarper {

    public Language language;



    public String TIMER_DEGA;
    public String TIMER_PVP_SOON;
    public String TIMER_PVP;
    public String TIMER_FIN_EP;

    public void InitLang(Language language) {

        for (Lang roles : Lang.values()) {
            if (language.getId().equals(Francais.id)) {
                roles.setName(Francais.valueOf(roles.name()).getName());
                roles.setShortDesc(Francais.valueOf(roles.name()).getShortDesc());
                roles.setDesc(Francais.valueOf(roles.name()).getDesc());
                TIMER_FIN_EP="Fin Episode";
                TIMER_DEGA="Les degas (sauf PVP) sont activés !";
                TIMER_PVP_SOON="PVP activé dans 10min !";
                TIMER_PVP="PVP activé !";
            } else if (language.getId().equals(English.id)) {
                roles.setName(English.valueOf(roles.name()).getName());
                roles.setShortDesc(English.valueOf(roles.name()).getShortDesc());
                roles.setDesc(English.valueOf(roles.name()).getDesc());
                TIMER_FIN_EP="";
                TIMER_DEGA="";
                TIMER_PVP_SOON="";
                TIMER_PVP="";
            } else {
                System.out.println("Cannot find language !");
            }
        }
    }


}
