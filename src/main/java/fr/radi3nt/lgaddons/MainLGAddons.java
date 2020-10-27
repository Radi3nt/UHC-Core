package fr.radi3nt.lgaddons;

import fr.radi3nt.lgaddons.scenarios.BloodyDiamond;
import fr.radi3nt.loupgarouuhc.LoupGarouUHC;
import org.bukkit.plugin.java.JavaPlugin;

public class MainLGAddons extends JavaPlugin {

    @Override
    public void onEnable() {
        LoupGarouUHC.registerScenario(BloodyDiamond.class);
    }

    @Override
    public void onDisable() {

    }

}
