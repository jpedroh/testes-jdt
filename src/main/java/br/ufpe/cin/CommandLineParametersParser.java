package br.ufpe.cin;

import java.nio.file.Path;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandLineParametersParser {
  public static class CommandLineParameters {
    public final Path classTargetPath;
    public final Path sourcePath;
    public final String targetClassPath;
    public final String targetMethodName;

    public CommandLineParameters(String classTargetPath, String sourcePath, String targetClassPath,
        String targetMethodName) {
      this.classTargetPath = Path.of(classTargetPath);
      this.sourcePath = Path.of(sourcePath);
      this.targetClassPath = targetClassPath;
      this.targetMethodName = targetMethodName;
    }
  }

  private final Options options;
  private final CommandLineParser parser;
  private final HelpFormatter formatter;

  private static final String classTargetPathTag = "c";
  private static final String sourcePathTag = "s";
  private static final String targetClassPath = "f";
  private static final String targetMethodNameTag = "m";
  private static final String helpTag = "h";

  public CommandLineParametersParser() {
    options = new Options();
    parser = new DefaultParser();
    formatter = new HelpFormatter();

    Option classTargetPathOption = Option.builder()
        .option(classTargetPathTag)
        .longOpt("classTargetPath")
        .hasArg()
        .desc("Class Target path")
        .required()
        .build();
    Option sourcePathOption = Option.builder()
        .option(sourcePathTag)
        .longOpt("sourcePath")
        .hasArg()
        .desc("Source code path")
        .required()
        .build();
    Option targetClassPathOption = Option.builder()
        .option(targetClassPath)
        .longOpt("targetClassPath")
        .hasArg()
        .desc("Target Class Path")
        .required()
        .build();
    Option targetMethodNameOption = Option.builder()
        .option(targetMethodNameTag)
        .longOpt("targetMethodName")
        .hasArg()
        .desc("Target Method Name")
        .required()
        .build();
    Option helpOption = Option.builder()
        .option(helpTag)
        .longOpt("help")
        .desc("Shows this help message")
        .build();

    options
        .addOption(classTargetPathOption)
        .addOption(sourcePathOption)
        .addOption(targetClassPathOption)
        .addOption(targetMethodNameOption)
        .addOption(helpOption);
  }

  public CommandLineParameters parse(String[] args) {
    try {
      CommandLine cmd = parser.parse(options, args);

      if (cmd.hasOption("help")) {
        formatter.printHelp("", options);
        return null;
      }

      return new CommandLineParameters(cmd.getOptionValue(classTargetPathTag), cmd.getOptionValue(sourcePathTag),
          cmd.getOptionValue(targetClassPath),
          cmd.getOptionValue(targetMethodNameTag));
    } catch (ParseException e) {
      formatter.printHelp("OSean.EX", options);
      throw new IllegalArgumentException(e.getMessage());
    }
  }
}