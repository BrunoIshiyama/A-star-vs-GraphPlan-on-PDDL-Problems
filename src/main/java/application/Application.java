package application;

import domain.AStar;
import domain.Heuristic;
import domain.Heuristic.Type;
import fr.uga.pddl4j.encoding.CodedProblem;
import fr.uga.pddl4j.planners.statespace.hsp.HSP;
import fr.uga.pddl4j.util.Plan;
import parser.Parser;
import utils.Utils;

public class Application {

	public static void main(String[] args) {
		Parser parser = new Parser();
		CodedProblem codedProblem = parser.parseDomainAndProblem(Utils.PATH_PDDL_FILES_DOMAIN,
				Utils.PATH_PDDL_FILES_PROBLEM);
		try {

			AStar aStar = new AStar(codedProblem);
			System.out.println(aStar.search(Heuristic.Type.Default));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
