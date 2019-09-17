package application;

import fr.uga.pddl4j.encoding.CodedProblem;
import parser.Parser;
import utils.Utils;

//it's the main class to be run
public class Application {

    public static void main(String[] args) {
        System.out.println("My Java Application");
        // TEST
        Parser PDDLParser = new Parser();
        CodedProblem codedProblem = PDDLParser
                .parseDomainAndProblem(Utils.PATH_PDDL_FILES_TYREWORLD_DOMAIN, Utils.PATH_PDDL_FILES_TYREWORLD_PROBLEM);
        System.out.println(codedProblem.getConstants().toString());
    }
}
