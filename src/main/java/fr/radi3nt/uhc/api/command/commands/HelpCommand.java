package fr.radi3nt.uhc.api.command.commands;

import fr.radi3nt.uhc.api.command.CommandArg;
import fr.radi3nt.uhc.api.command.CommandUtilis;

import java.util.List;

public class HelpCommand extends CommandArg {

    @Override
    protected void onCommand(CommandUtilis utilis) {

    }

    @Override
    protected List<String> tabComplete(CommandUtilis utilis) {
        return null;
    }
}
