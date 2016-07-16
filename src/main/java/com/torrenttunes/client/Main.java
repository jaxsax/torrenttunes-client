package com.torrenttunes.client;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class Main {
    public static void main(String[] args) {
        Arguments arguments = new Arguments();
        CmdLineParser parser = new CmdLineParser(arguments);

        parse(parser, args);

        new Application().main(arguments);
    }

    private static void parse(CmdLineParser parser,
                              String[] args) {
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            parser.printUsage(System.err);
            System.exit(-1);
        }
    }
}
