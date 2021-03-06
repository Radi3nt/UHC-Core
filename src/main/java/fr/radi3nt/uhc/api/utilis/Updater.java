package fr.radi3nt.uhc.api.utilis;


import fr.radi3nt.uhc.uhc.UHCCore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public final class Updater {

    private final String urlBase;
    private final String currentVersion;

    public Updater(String resourceUrl) {
        urlBase = "https://api.spigotmc.org/legacy/update.php?resource=" + resourceUrl;
        currentVersion = UHCCore.getPlugin().getDescription().getVersion().split(" ")[0];
    }

    public UpdateResults checkForUpdates() {
        if (ConfigData.shouldCheckForUpdates())
        {
            try
            {
                HttpURLConnection con = (HttpURLConnection) new URL(urlBase).openConnection();

                con.setDoOutput(true);
                con.setRequestMethod("GET");
                con.getOutputStream()
                        .write((urlBase).getBytes(StandardCharsets.UTF_8));

                if (con.getResponseCode() == 500)
                    return new UpdateResults(UpdateResult.FAIL, "Server responded with code 500: Internal server error");

                String version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();

                con.disconnect();

                switch(compareVersion(version)){
                	case 0: return new UpdateResults(UpdateResult.NO_UPDATE, null);
                	case -1: return new UpdateResults(UpdateResult.UPDATE_AVAILABLE, version);
                	case 1: return new UpdateResults(UpdateResult.DEV, version);
                	default: return new UpdateResults(UpdateResult.FAIL, "This was not suppost to happen!");
                }

            } catch (Exception ex) {
                return new UpdateResults(UpdateResult.FAIL, ex.toString());
            }
        } else
            return new UpdateResults(UpdateResult.DISABLED, null);

    }

    public enum UpdateResult {
        NO_UPDATE,
        DISABLED,
        FAIL,
        UPDATE_AVAILABLE,
        DEV
    }

    public class UpdateResults {
        private final UpdateResult result;
        private final String version;

        public UpdateResults(UpdateResult result, String version) {
            this.result = result;
            this.version = version;
        }

        public UpdateResult getResult() {
            return result;
        }

        public String getVersion() {
            return version;
        }
    }

    private int compareVersion(String newVersion) {
        Scanner currentV = new Scanner(currentVersion);
        Scanner newV = new Scanner(newVersion);

        currentV.useDelimiter("\\.");
        newV.useDelimiter("\\.");

        while (currentV.hasNextInt() && newV.hasNextInt())
        {
            int cV = currentV.nextInt();
            int nV = newV.nextInt();

            if(cV < nV)
            {
                currentV.close();
                newV.close();

                return -1;
            }
            else if(cV > nV)
            {
                currentV.close();
                newV.close();

                return 1;
            }
        }

        if(currentV.hasNextInt())
        {
            currentV.close();
            newV.close();

            return 1;
        }

        if(newV.hasNextInt())
        {
            currentV.close();
            newV.close();

            return -1;
        }
        
        currentV.close();
        newV.close();
        return 0;
    }
}