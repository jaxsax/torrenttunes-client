package com.torrenttunes.client;

import com.torrenttunes.client.tools.DataSources;
import com.torrenttunes.client.tools.Tools;
import com.torrenttunes.client.tools.WriteMultilingualHTMLFiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;

public class ApplicationTools {
    private static final Logger log = LoggerFactory.getLogger(ApplicationTools.class);

    public static void setupDirectories() {
        if (!new File(DataSources.TORRENTS_DIR()).exists()) {
            log.info("Setting up ~/." + DataSources.APP_NAME + " dirs");
            new File(DataSources.HOME_DIR()).mkdirs();
            new File(DataSources.TORRENTS_DIR()).mkdirs();
            new File(DataSources.DEFAULT_MUSIC_STORAGE_PATH()).mkdirs();


        } else {
            log.info("Home directory already exists");
        }
    }

    public static void copyResourcesToHomeDir(boolean copyAnyway) {

        Optional<String> foundVersionOpt = readInstalledVersion(
                Paths.get(DataSources.INSTALLED_VERSION_FILE())
        );
        if (shouldCopyResources(copyAnyway, foundVersionOpt)) {

            Path copiedJar = Paths.get(copyJarToAppHome());
            Path appHome = Paths.get(DataSources.SOURCE_CODE_HOME());

            log.info("Copying resources to {}", appHome.toAbsolutePath());

            deleteTemporaryJarFile();

            // Unzip it and rename it
            Tools.unzip(
                    copiedJar.toFile(),
                    appHome.toFile()
            );

            new File(DataSources.ZIP_FILE()).renameTo(new File(DataSources.JAR_FILE()));

            // Update the version number
            Tools.writeFile(DataSources.VERSION, DataSources.INSTALLED_VERSION_FILE());

            Tools.installShortcuts();

            WriteMultilingualHTMLFiles.write();

        } else {
            log.info("The source directory already exists");
        }
    }

    private static void deleteTemporaryJarFile() {
        File jarFile = new File(DataSources.JAR_FILE());
        try {
            File currentJar = new File(Tools.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            if (!jarFile.equals(currentJar)) {
                jarFile.delete();
            }


        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static String copyJarToAppHome() {
        String zipFile = null;
        try {
            if (new File(DataSources.SHADED_JAR_FILE).exists()) {
                Files.copy(
                        Paths.get(DataSources.SHADED_JAR_FILE),
                        Paths.get(DataSources.ZIP_FILE()),
                        StandardCopyOption.REPLACE_EXISTING
                );

                zipFile = DataSources.SHADED_JAR_FILE;

            } else if (new File(DataSources.SHADED_JAR_FILE_2()).exists()) {
                Files.copy(Paths.get(DataSources.SHADED_JAR_FILE_2()), Paths.get(DataSources.ZIP_FILE()),
                        StandardCopyOption.REPLACE_EXISTING);
                zipFile = DataSources.SHADED_JAR_FILE_2();
            } else {
                log.info("you need to build the project first");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zipFile;
    }

    private static boolean shouldCopyResources(boolean overrideExisting,
                                               Optional<String> installedVersionOpt) {
        return overrideExisting ||
                new File(DataSources.SOURCE_CODE_HOME()).exists() ||
                !installedVersionOpt.isPresent() ||
                sourceNotEqualWithInstalled(installedVersionOpt.get());
    }

    private static boolean sourceNotEqualWithInstalled(String installedVersion) {
        return !installedVersion.equals(DataSources.VERSION);
    }

    private static Optional<String> readInstalledVersion(Path installedVersionPath) {
        try {
            List<String> lines = Files.readAllLines(installedVersionPath);
            if (lines.size() <= 0) {
                return Optional.empty();
            }
            return Optional.of(lines.get(0).trim());
        } catch (NoSuchFileException e) {
            return Optional.empty();
        } catch (IOException e) {
            log.error("Error reading installed version", e);
        }
        return Optional.empty();
    }

    public static void addExternalWebServiceVarToTools() {

        log.info("tools.js = " + DataSources.TOOLS_JS());
        try {
            List<String> lines = Files.readAllLines(Paths.get(DataSources.TOOLS_JS()));

            String interalServiceLine = "var localSparkService = '" +
                    DataSources.WEB_SERVICE_URL + "';";

            String torrentTunesServiceLine = "var torrentTunesSparkService ='" +
                    DataSources.TORRENTTUNES_URL + "';";

            String externalServiceLine = "var externalSparkService ='" +
                    DataSources.EXTERNAL_URL + "';";

            lines.set(0, interalServiceLine);
            lines.set(1, torrentTunesServiceLine);
            lines.set(2, externalServiceLine);

            Files.write(Paths.get(DataSources.TOOLS_JS()), lines);
        } catch (IOException e) {
            log.error("Error adding external webservice var to tools", e);
        }

    }
}
