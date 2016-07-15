package com.torrenttunes.client;

import org.kohsuke.args4j.Option;

public class Arguments {

    @Option(name = "-uninstall",
            usage = "Uninstall client (this deletes your library)")
    private boolean uninstall;

    @Option(name = "-recopy", usage = "Recopies your source folders")
    private boolean recopy;

    @Option(name = "-installonly", usage = "Only installs, doesn't run")
    private boolean installOnly;

    @Option(name = "-logLevel", usage = "Sets the log level")
    private String logLevel = "INFO";

    @Option(name = "-sharedirectory", usage = "Scans a directory to share")
    private String shareDirectory = null;

    @Option(name = "-extradirectory", usage = "Adds an extra directory of torrents to share")
    private String extraDirectory = null;

    @Option(name = "-maxdownloadspeed", usage = "Sets a custom max download speed (kb/s)")
    private Integer maxDownloadSpeed = null;

    @Option(name = "-nobrowser", usage = "Don't launch a browser upon startup")
    private boolean noBrowser;

    public boolean isUninstall() {
        return uninstall;
    }

    public void setUninstall(boolean uninstall) {
        this.uninstall = uninstall;
    }

    public boolean isRecopy() {
        return recopy;
    }

    public void setRecopy(boolean recopy) {
        this.recopy = recopy;
    }

    public boolean isInstallOnly() {
        return installOnly;
    }

    public void setInstallOnly(boolean installOnly) {
        this.installOnly = installOnly;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getShareDirectory() {
        return shareDirectory;
    }

    public void setShareDirectory(String shareDirectory) {
        this.shareDirectory = shareDirectory;
    }

    public String getExtraDirectory() {
        return extraDirectory;
    }

    public void setExtraDirectory(String extraDirectory) {
        this.extraDirectory = extraDirectory;
    }

    public Integer getMaxDownloadSpeed() {
        return maxDownloadSpeed;
    }

    public void setMaxDownloadSpeed(Integer maxDownloadSpeed) {
        this.maxDownloadSpeed = maxDownloadSpeed;
    }

    public boolean isNoBrowser() {
        return noBrowser;
    }

    public void setNoBrowser(boolean noBrowser) {
        this.noBrowser = noBrowser;
    }
}
