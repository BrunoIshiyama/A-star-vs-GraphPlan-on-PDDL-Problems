package application;

import domain.AStar;
import fr.uga.pddl4j.encoding.CodedProblem;
import parser.Parser;
import utils.PDDLManager;
import utils.Utils;

public class Application {

    public static void main(String[] args) {
        Parser parser = new Parser();
        CodedProblem codedProblem = parser.parseDomainAndProblem(Utils.PATH_PDDL_FILES_TYREWORLD_DOMAIN, Utils.PATH_PDDL_FILES_TYREWORLD_PROBLEM);
        PDDLManager.init(codedProblem);
        try {

            AStar aStar = new AStar(codedProblem);
            System.out.println(aStar.search());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
