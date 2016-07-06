package com.torrenttunes.client.webservice;

import com.torrenttunes.client.tools.DataSources;
import com.torrenttunes.client.tools.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.*;

public class WebService {
    static final Logger log = LoggerFactory.getLogger(WebService.class);


    public static void start() {

        port(DataSources.SPARK_WEB_PORT);

        threadPool(8);

        Platform.setup();

        get("/hello", (req, res) -> {
            Tools.allowOnlyLocalHeaders(req, res);
            return "hi from the torrenttunes-client web service";
        });

        get("/torrenttunes", (req, res) -> {
            Tools.allowAllHeaders(req, res);
            Tools.set15MinuteCache(req, res);

            return Tools.readFile(DataSources.BASE_ENDPOINT);
        });


        get("/*", (req, res) -> {
            Tools.allowAllHeaders(req, res);
//			Tools.set15MinuteCache(req, res);

            String pageName = req.splat()[0];

            String webHomePath = DataSources.WEB_HOME() + "/" + pageName;

            Tools.setContentTypeFromFileName(pageName, res);


            return Tools.writeFileToResponse(webHomePath, req, res);

        });


    }
}
