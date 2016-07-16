package com.torrenttunes.client.tools;

import com.frostwire.jlibtorrent.AnnounceEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class DataSources {

    static final Logger log = LoggerFactory.getLogger(DataSources.class);

    public static String APP_NAME = "torrenttunes-client";

    public static String LAUNCHER_NAME = "torrenttunes-launcher";

    public static String VERSION = "0.7.8";

    public static Integer SPARK_WEB_PORT = 4568;

    public static final String WEB_SERVICE_URL = "http://localhost:" + SPARK_WEB_PORT + "/";

    public static final String WEB_SERVICE_URL_HOME = "http://localhost:" + SPARK_WEB_PORT + "/torrenttunes";

    public static String EXTERNAL_IP = Tools.httpGetString("http://api.ipify.org/").trim();

    public static String EXTERNAL_URL = "http://" + EXTERNAL_IP + ":" + SPARK_WEB_PORT + "/";

    public static final String TORRENTTUNES_IP = "torrenttunes.ml";

    public static final String TORRENTTUNES_PORT = "80";// Main is 80, dev is 4567

    public static String TORRENTTUNES_URL = "http://" + TORRENTTUNES_IP + ":" + TORRENTTUNES_PORT + "/";

    public static final String IP_HASH = Tools.getIPHash();

    public static final File SAMPLE_TORRENT = new File("/home/tyler/Downloads/[kat.cr]devious.maids.s03e01.hdtv.x264.asap.ettv.torrent");

    public static String HOME_DIR() {
        return System.getProperty("user.home") + "/." + APP_NAME;
    }

    public static String TORRENTS_DIR() {
        return HOME_DIR() + "/torrents";
    }

    public static String DEFAULT_MUSIC_STORAGE_PATH() {
        return HOME_DIR() + "/music";
    }

    public static String MUSIC_STORAGE_PATH = DEFAULT_MUSIC_STORAGE_PATH();

    public static String AUDIO_FILE(String fileName) {
        return MUSIC_STORAGE_PATH + "/" + fileName;
    }

    public static final String SAMPLE_MUSIC_DIR = "/home/tyler/Downloads";

    public static final String SAMPLE_SONG = SAMPLE_MUSIC_DIR + "/04 One Evening.mp3";

    public static String DB_FILE() {
        return HOME_DIR() + "/db/db.sqlite";
    }

    // This should not be used, other than for unzipping to the home dir
    public static final String CODE_DIR = System.getProperty("user.dir");

    public static String SOURCE_CODE_HOME() {
        return HOME_DIR() + "/src";
    }

    public static String SQL_FILE() {
        return SOURCE_CODE_HOME() + "/ddl.sql";
    }

    public static String SQL_VIEWS_FILE() {
        return SOURCE_CODE_HOME() + "/views.sql";
    }

    public static final String SHADED_JAR_FILE = CODE_DIR + "/target/" + APP_NAME + ".jar";

    public static String SHADED_JAR_FILE_2() {
        try {
            String path = DataSources.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            if (System.getProperty("os.name").contains("indow")) {
                path = path.substring(1);
            }

            return path;
        } catch (URISyntaxException e) {
            log.error("Invalid URI", e);
        }
        return null;
    }

    public static String ZIP_FILE() {
        return HOME_DIR() + "/" + APP_NAME + ".zip";
    }

    public static String JAR_FILE() {
        return HOME_DIR() + "/" + APP_NAME + ".jar";
    }

    public static String LAUNCHER_FILE() {
        return HOME_DIR() + "/" + LAUNCHER_NAME + ".jar";
    }

    public static String TOOLS_JS() {
        return SOURCE_CODE_HOME() + "/web/js/tools.js";
    }

    // Web pages
    public static String WEB_HOME() {
        return SOURCE_CODE_HOME() + "/web";
    }

    public static String WEB_HTML() {
        return WEB_HOME() + "/html";
    }

    public static String MAIN_PAGE_URL_EN() {
        return WEB_HTML() + "/main_en.html";
    }

    public static String MAIN_PAGE_URL_ES() {
        return WEB_HTML() + "/main_es.html";
    }

    public static String MAIN_PAGE_URL_FR() {
        return WEB_HTML() + "/main_fr.html";
    }

    public static String BASE_ENDPOINT = MAIN_PAGE_URL_EN();

    public static final Set<String> NON_STREAMING_BROWSERS = new HashSet<>(
            Arrays.asList("Firefox", "Android")
    );

    public static String SESSION_STATS_FILE() {
        return CODE_DIR + "/session_stats.0000.log";
    }

    public static final String TORRENT_UPLOAD_URL = TORRENTTUNES_URL + "torrent_upload";

    public static final String TORRENT_INFO_UPLOAD_URL = TORRENTTUNES_URL + "torrent_info_upload";

    public static String TORRENT_DOWNLOAD_URL(String infoHash) {
        return TORRENTTUNES_URL + "download_torrent/" + infoHash;
    }

    public static String TORRENT_INFO_DOWNLOAD_URL(String infoHash) {
        return TORRENTTUNES_URL + "download_torrent_info/" + infoHash;
    }

    public static String SEEDER_INFO_UPLOAD(String infoHash, String seeders) {
        return TORRENTTUNES_URL + "seeder_upload/" + infoHash + "/" + seeders;
    }


    public static String LIBTORRENT_OS_LIBRARY_PATH() {
        String osName = System.getProperty("os.name").toLowerCase();
        String jvmBits = System.getProperty("sun.arch.data.model");
        log.info("Operating system: " + osName + ", JVM bits: " + jvmBits);

        String ret = null;
        if (osName.contains("linux")) {
            if (jvmBits.equals("32")) {
                ret = SOURCE_CODE_HOME() + "/lib/x86/libjlibtorrent.so";
            } else {
                ret = SOURCE_CODE_HOME() + "/lib/x86_64/libjlibtorrent.so";
            }
        } else if (osName.contains("windows")) {
            if (jvmBits.equals("32")) {
                ret = SOURCE_CODE_HOME() + "/lib/x86/jlibtorrent.dll";
            } else {
                ret = SOURCE_CODE_HOME() + "/lib/x86_64/jlibtorrent.dll";
            }
        } else if (osName.contains("mac")) {
            ret = SOURCE_CODE_HOME() + "/lib/x86_64/libjlibtorrent.dylib";
        }

        log.info("Using libtorrent @ " + ret);
        return ret;
    }

    public static List<URI> ANNOUNCE_LIST() {
        List<URI> list = new ArrayList<>();
        try {
            list = Collections.singletonList(
                    new URI("udp://tracker.opentrackr.org:1337/announce"));
        } catch (URISyntaxException e) {
            log.error("Invalid URI", e);
        }

        return list;
    }

    public static List<AnnounceEntry> ANNOUNCE_ENTRIES() {
        return Collections.singletonList(
                new AnnounceEntry("udp://tracker.opentrackr.org:1337/announce")
        );
    }

    public static String WINDOWS_SHORTCUT_LINK() {
        return System.getProperty("user.home") + "/desktop/Torrent Tunes.lnk";
    }

    public static String ICON_LOCATION() {
        return WEB_HOME() + "/image/favicon.ico";
    }

    public static String ICON_MAC_LOCATION() {
        return WEB_HOME() + "/image/favicon.icns";
    }

    public static String MAC_ICON_APPLET() {
        return MAC_APP_LOCATION() + "/Contents/Resources/applet.icns";
    }

    public static String WINDOWS_INSTALL_VBS() {
        return SOURCE_CODE_HOME() + "/windows_install.vbs";
    }

    public static String LINUX_DESKTOP_FILE() {
        return System.getProperty("user.home") + "/.local/share/applications/" + APP_NAME + ".desktop";
    }

    public static String MAC_INSTALL_APPLESCRIPT() {
        return SOURCE_CODE_HOME() + "/mac_install.scpt";
    }

    public static String MAC_APP_LOCATION() {
        return System.getProperty("user.home") + "/Applications/TorrentTunes.app";
    }

    public static String FETCH_LATEST_RELEASE_URL() {
        return "https://github.com/tchoulihan/torrenttunes-client/releases/latest";
    }

    public static String INSTALLED_VERSION_FILE() {
        return SOURCE_CODE_HOME() + "/version";
    }

    public static final Date APP_START_DATE = new Date();

    public static String HTML_TEMPLATE_LOCATION() {
        return WEB_HTML() + "/main.template";
    }

    public static String STRINGS_EN_LOCATION() {
        return SOURCE_CODE_HOME() + "/values/strings_en.json";
    }

    public static String STRINGS_ES_LOCATION() {
        return SOURCE_CODE_HOME() + "/values/strings_es.json";
    }

    public static String STRINGS_FR_LOCATION() {
        return SOURCE_CODE_HOME() + "/values/strings_fr.json";
    }

    public static Integer MAX_DOWNLOAD_SPEED_BYTES = 0;
}
