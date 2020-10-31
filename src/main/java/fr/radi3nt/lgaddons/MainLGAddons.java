package fr.radi3nt.lgaddons;

import fr.radi3nt.lgaddons.scenarios.BloodyDiamond;
import fr.radi3nt.loupgarouuhc.LoupGarouUHC;

public class MainLGAddons {

    public void onEnable() {
        LoupGarouUHC.registerScenario(BloodyDiamond.class);
    }

    public void onDisable() {

    }

}
