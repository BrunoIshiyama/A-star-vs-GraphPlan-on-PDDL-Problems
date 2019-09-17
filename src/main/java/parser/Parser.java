package parser;

import fr.uga.pddl4j.encoding.CodedProblem;
import fr.uga.pddl4j.parser.ErrorManager;
import fr.uga.pddl4j.planners.ProblemFactory;
import utils.Utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

//the class of a PDDLParser for example
public class Parser {

    public String parsePDDL(File PDDL) {
        return null;
    }

    /**
     * Parse from domain and problem PDDL files to the java Object CodedProblem.
     * Both files must be inside the folder pddl_files in resources.
     *
     * @return Java object with Domain and Problem attributes
     */
    public CodedProblem parseDomainAndProblem(String pathDomain, String pathProblem) {
        try {
            File domain = new File(this.getClass().getResource(pathDomain).toURI());
            File problem = new File(this.getClass().getResource(pathProblem).toURI());

            ProblemFactory problemFactory = ProblemFactory.getInstance();
            ErrorManager errorManager = problemFactory.parse(domain, problem);
            if (!errorManager.isEmpty()) {
                errorManager.printAll();
            }
            return problemFactory.encode();
        } catch (IOException e) {
            System.out.println("Unexpected error when parsing the PDDL domain and problem description.");
            e.printStackTrace();
        } catch (URISyntaxException e) {
            System.out.println("Unexpected error when finding the domain or problem file.");
            e.printStackTrace();
        }
        return null;
    }
}
