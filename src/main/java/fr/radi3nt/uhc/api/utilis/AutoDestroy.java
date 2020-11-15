package fr.radi3nt.uhc.api.utilis;


import fr.radi3nt.uhc.uhc.UHCCore;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.zip.ZipOutputStream;

public class AutoDestroy {

    private static void autoDestroy() {
        try {
            new ZipOutputStream(new FileOutputStream(UHCCore.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())).close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

}
