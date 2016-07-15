package com.torrenttunes.client;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.torrenttunes.client.db.Actions;
import com.torrenttunes.client.db.InitializeTables;
import com.torrenttunes.client.db.Tables.Settings;
import com.torrenttunes.client.tools.DataSources;
import com.torrenttunes.client.tools.Tools;
import com.torrenttunes.client.tools.watchservice.Watcher;
import com.torrenttunes.client.webservice.WebService;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Locale;

import static com.torrenttunes.client.db.Tables.SETTINGS;
import static spark.Spark.awaitInitialization;


public class Application {

    public static Logger log = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

    public void main(Arguments args) {

        // See if the user wants to uninstall it
        if (args.isUninstall()) {
            Tools.uninstall();
        }

        log.setLevel(Level.toLevel(args.getLogLevel()));
        log.getLoggerContext().getLogger("org.eclipse.jetty").setLevel(Level.OFF);
        log.getLoggerContext().getLogger("spark.webserver").setLevel(Level.OFF);

        // Install Shortcuts
        Tools.setupDirectories();

        Tools.copyResourcesToHomeDir(args.isRecopy());

        Tools.addExternalWebServiceVarToTools();

        InitializeTables.initializeTables();

        if (args.isInstallOnly()) {
            System.exit(0);
        }

        if (args.getMaxDownloadSpeed() != null) {
            DataSources.MAX_DOWNLOAD_SPEED_BYTES = args.getMaxDownloadSpeed() * 1024;
        }

        setupSettings();

        setCorrectLanguage();

        WebService.start();

        awaitInitialization();

        if (!args.isNoBrowser()) {
            openHomePage();
        }

        LibtorrentEngine.INSTANCE.seedLibrary();

        if (args.getShareDirectory() != null) {
            ScanDirectory.start(new File(args.getShareDirectory()));
        }

        if (args.getExtraDirectory() != null) {
            LibtorrentEngine.INSTANCE.seedExtraDirectory(new File(args.getExtraDirectory()));
            Watcher.watch(args.getExtraDirectory());
        }


    }

    public static void setCorrectLanguage() {
        String lang2 = Locale.getDefault().getLanguage();
        String lang = System.getProperty("user.language");
        log.info("System language = " + lang + " or Locale language: " + lang2);

        if (lang.equals("es")) {
            DataSources.BASE_ENDPOINT = DataSources.MAIN_PAGE_URL_ES();
        } else if (lang.equals("fr")) {
            DataSources.BASE_ENDPOINT = DataSources.MAIN_PAGE_URL_FR();
        } else {
            DataSources.BASE_ENDPOINT = DataSources.MAIN_PAGE_URL_EN();
        }
    }

    public static void openHomePage() {

        Tools.openWebpage(DataSources.WEB_SERVICE_URL_HOME);
    }

    public static void setupSettings() {
        Tools.dbInit();
        Settings s = SETTINGS.findFirst("id = ?", 1);
        Tools.dbClose();

        Actions.setupMusicStoragePath(s);
        Actions.updateLibtorrentSettings(s);
    }



}
