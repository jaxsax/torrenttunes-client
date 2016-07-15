package com.torrenttunes.client;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        Arguments arguments = new Arguments();
        CmdLineParser parser = new CmdLineParser(arguments);
        Optional<Arguments> parsedArguments = parse(parser, args);

        if (!parsedArguments.isPresent()) {
            parser.printUsage(System.err);
            System.exit(-1);
        }
        new Application().main(parsedArguments.get());
    }

    private static Optional<Arguments> parse(CmdLineParser parser,
                                             String[] args) {
        Arguments arguments = new Arguments();

        try {
            parser.parseArgument(args);
            return Optional.of(arguments);

        } catch (CmdLineException e) {
            return Optional.empty();
        }
    }
}
