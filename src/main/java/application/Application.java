package application;

import fr.uga.pddl4j.encoding.CodedProblem;
import fr.uga.pddl4j.util.BitOp;
import fr.uga.pddl4j.util.BitState;
import model.AStar;
import model.Node;
import parser.Parser;
import utils.PDDLManager;
import utils.Utils;

import java.util.Iterator;
import java.util.List;

public class Application {

    public static void main(String[] args) {
        Parser parser = new Parser();
        CodedProblem codedProblem = parser.parseDomainAndProblem(Utils.PATH_PDDL_FILES_TYREWORLD_DOMAIN, Utils.PATH_PDDL_FILES_TYREWORLD_PROBLEM);
        PDDLManager.init(codedProblem);
        try {
            System.out.println(PDDLManager.getActionParameters("loosen"));
            System.out.println(PDDLManager.getActionPreconditions("loosen").cardinality());
            System.out.println(codedProblem.getInit());
            System.out.println(codedProblem.getOperators());
            AStar aStar = new AStar();
            BitState init = new BitState(codedProblem.getInit());
            Node current = new Node(init);
            List<BitOp> operators = aStar.findApplicableOperators(current, codedProblem.getOperators());
            System.out.println("Meus operadores:" + operators.size());
            System.out.println(codedProblem.getOperators().size());
            int count = 0;
            int index = 0;
            for(Iterator var18 = codedProblem.getOperators().iterator(); var18.hasNext(); ++index) {
                BitOp op = (BitOp)var18.next();
                if (op.isApplicable(current)) {
                    count++;
                }
            }
            System.out.println("Deles: " + count);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
