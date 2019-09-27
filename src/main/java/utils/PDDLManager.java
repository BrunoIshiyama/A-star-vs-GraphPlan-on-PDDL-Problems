package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.uga.pddl4j.encoding.CodedProblem;
import fr.uga.pddl4j.util.BitExp;
import fr.uga.pddl4j.util.BitOp;

public class PDDLManager {
	private static Map<String, List<String>> actionToParameters;
	private static Map<String, BitExp> actionToPreconditions;

	/**
	 * initializes a Map to enable other PDDL operations
	 * 
	 * @param codedProblem - A PDDL problem parsed - CodedProblem
	 * @throws NullPointerException
	 */
	public static void init(CodedProblem codedProblem) {
		if (codedProblem == null)
			throw new NullPointerException("codedProblem at init() is null");

		initActionToParameters(codedProblem);
		initActionToPreconditions(codedProblem);

	}

	private static void initActionToParameters(CodedProblem codedProblem) {
		actionToParameters = new HashMap<>();
		List<String> typeList = new ArrayList<>();
		for (BitOp b : codedProblem.getOperators()) {
			if (actionToParameters.get(b.getName()) != null)
				continue;
			for (int i = 0; i < b.getArity(); i++) {
				typeList.add(codedProblem.getTypes().get(b.getTypeOfParameters(i)));
			}
			actionToParameters.put(b.getName(), typeList);
			typeList = new ArrayList<String>();
		}
	}

	private static void initActionToPreconditions(CodedProblem codedProblem) {
		actionToPreconditions = new HashMap<>();
		for (BitOp b : codedProblem.getOperators()) {
			if (actionToPreconditions.get(b.getName()) != null)
				continue;
			actionToPreconditions.put(b.getName(), b.getPreconditions());
		}
	}

	/**
	 * Retrieves an actions' parameters in order
	 * 
	 * @param action - A action described in domain.pddl file - String
	 * @return A list of the actions' parameters in order - List
	 * @throws Exception
	 */
	public static List<String> getActionParameters(String action) throws Exception {
		if (actionToParameters == null) {
			throw new Exception("PDDLManager might not have been initialized");
		}
		List<String> returnList = actionToParameters.get(action);
		if (returnList == null) {
			throw new Exception("Action \'" + action + "\' not found");
		}
		return returnList;
	}

	/**
	 * Retrieves an actions' preconditions in order
	 * 
	 * @param action - A action described in domain.pddl file - String
	 * @return A list of the actions' preconditions in order - List
	 * @throws Exception
	 */
	public static BitExp getActionPreconditions(String action) throws Exception {
		if (actionToPreconditions == null) {
			throw new Exception("PDDLManager might not have been initialized");
		}
		BitExp returnList = actionToPreconditions.get(action);
		if (returnList == null) {
			throw new Exception("Action \'" + action + "\' not found");
		}
		return returnList;
	}
}
