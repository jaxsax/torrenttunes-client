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

        if (args.isUninstall()) {
            uninstallApplication();
        }

        setupLogging(args);
        installApplication(args);

        setupSettings(args);

        startAndAwaitWebService();

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

    private void startAndAwaitWebService() {
        WebService.start();
        awaitInitialization();
    }

    // TODO: Refactor Tools.uninstall to not exit, we should exit here instead
    private void uninstallApplication() {
        Tools.uninstall();
        System.exit(0);
    }

    private void setupLogging(Arguments arguments) {
        log.setLevel(Level.toLevel(arguments.getLogLevel()));
        log.getLoggerContext().getLogger("org.eclipse.jetty").setLevel(Level.OFF);
        log.getLoggerContext().getLogger("spark.webserver").setLevel(Level.OFF);
    }

    private void installApplication(Arguments arguments) {

        Tools.setupDirectories();
        Tools.copyResourcesToHomeDir(arguments.isRecopy());
        Tools.addExternalWebServiceVarToTools();

        InitializeTables.initializeTables();

        if (arguments.isInstallOnly()) {
            System.exit(0);
        }
    }

    public static void openHomePage() {

        Tools.openWebpage(DataSources.WEB_SERVICE_URL_HOME);
    }

    public static void setupSettings(Arguments arguments) {
        Tools.dbInit();
        Settings s = SETTINGS.findFirst("id = ?", 1);
        Tools.dbClose();

        Actions.setupMusicStoragePath(s);
        Actions.updateLibtorrentSettings(s);

        Integer maxDownloadSpeed = arguments.getMaxDownloadSpeed();
        if (maxDownloadSpeed != null) {
            DataSources.MAX_DOWNLOAD_SPEED_BYTES = maxDownloadSpeed;
        }

        setCorrectLanguage();
    }

    private static void setCorrectLanguage() {
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

}
