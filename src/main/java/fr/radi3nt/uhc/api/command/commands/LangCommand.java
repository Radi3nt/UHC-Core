package fr.radi3nt.uhc.api.command.commands;

import fr.radi3nt.uhc.api.command.CommandArg;
import fr.radi3nt.uhc.api.command.CommandUtilis;
import fr.radi3nt.uhc.api.exeptions.common.NoPermissionException;
import fr.radi3nt.uhc.api.lang.lang.Language;
import fr.radi3nt.uhc.api.player.UHCPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static fr.radi3nt.uhc.api.command.CommandUtilis.requirePermission;

public class LangCommand extends CommandArg {
    @Override
    protected void onCommand(CommandUtilis utilis) throws NoPermissionException {
        if (utilis.checkIfPlayer()) {
            if (requirePermission(utilis.getSender(), "lg.lang", "")) {
                if (utilis.getArgs().length > 0) {
                    for (Language language : Language.getLanguages()) {
                        if (utilis.getArgs()[0].equalsIgnoreCase(language.getId()) || utilis.getArgs()[0].equalsIgnoreCase(language.getName())) {
                            UHCPlayer lgp = UHCPlayer.thePlayer((Player) utilis.getSender());
                            lgp.setLanguage(language);
                            lgp.saveLang();
                        }
                    }
                }
            }
        }
    }

    @Override
    protected List<String> tabComplete(CommandUtilis utilis) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (Language language : Language.getLanguages()) {
            if (!language.getId().equals(Language.DEFAULTID))
                arrayList.add(language.getName().toLowerCase());
        }
        arrayList.removeIf(s1 -> !s1.startsWith(utilis.getArgs()[0]));
        return arrayList;
    }
}
