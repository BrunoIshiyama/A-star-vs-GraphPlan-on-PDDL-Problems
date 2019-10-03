package application;

import domain.AStar;
import fr.uga.pddl4j.encoding.CodedProblem;
import fr.uga.pddl4j.planners.statespace.hsp.HSP;
import fr.uga.pddl4j.util.Plan;
import parser.Parser;
import utils.PDDLManager;
import utils.Utils;

public class Application {

    public static void main(String[] args) {
        Parser parser = new Parser();
        CodedProblem codedProblem = parser.parseDomainAndProblem(Utils.PATH_PDDL_FILES_DOMAIN, Utils.PATH_PDDL_FILES_PROBLEM);
        PDDLManager.init(codedProblem);
        try {
//            System.out.println(PDDLManager.getActionParameters("loosen"));
//            System.out.println(PDDLManager.getActionPreconditions("loosen").cardinality());

//            HSP hsp = new HSP();
//            Plan plan = hsp.search(codedProblem);
//            System.out.println(codedProblem.toString(plan));

            AStar aStar = new AStar();
            aStar.search(codedProblem);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
