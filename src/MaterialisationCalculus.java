import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.vlog4j.core.model.api.Atom;
import org.semanticweb.vlog4j.core.model.api.Predicate;
import org.semanticweb.vlog4j.core.model.api.Rule;
import org.semanticweb.vlog4j.core.model.api.Variable;
import org.semanticweb.vlog4j.core.model.implementation.Expressions;
import org.semanticweb.vlog4j.core.reasoner.Reasoner;
import org.semanticweb.vlog4j.core.reasoner.exceptions.EdbIdbSeparationException;
import org.semanticweb.vlog4j.core.reasoner.exceptions.IncompatiblePredicateArityException;
import org.semanticweb.vlog4j.core.reasoner.exceptions.ReasonerStateException;


/*
 * Make Expressions, Atoms and Rules 
 */
public class MaterialisationCalculus {
	protected static Map<String,Predicate> mapIDBPredicates;
	protected static Map<String,Predicate> mapEDBPredicates;
	final ArrayList<Predicate> arrListIDBPred;
	final Predicate v_inst ;
	final Predicate v_self;
	final Predicate v_triple;
	//final List listRules;
	protected final Set<Atom> setFacts;
	protected final List listAtomsEDB;
	//final Predicate inst = Expressions.makePredicate(name, 2);
	protected final Variable x = Expressions.makeVariable("x");
	protected final Variable y = Expressions.makeVariable("y");
	protected final Variable z = Expressions.makeVariable("z");
	protected final Variable a = Expressions.makeVariable("a");
	final Map<String, Atom> mapIDBXAtom;
	final Map<String, Atom> mapEDBXAtom;
	final Map<String, Rule> mapRule;
	
	//Atoms
	//final Atom nom_x = Expressions.makeAtom(predicateName, terms);
	
	
	public MaterialisationCalculus() {
		mapIDBPredicates = new HashMap<>();
		mapEDBPredicates = new HashMap<>();
		mapIDBXAtom = new HashMap<>();
		mapEDBXAtom = new HashMap<>();
		mapRule = new HashMap<>();
		arrListIDBPred = new ArrayList<>();
		v_inst = Expressions.makePredicate("inst", 2);
		v_self = Expressions.makePredicate("self", 2);
		v_triple = Expressions.makePredicate("triple", 3);
		setFacts = new HashSet<>();
		//listRules = new List();
		//listAtomsIDB = new List();
		listAtomsEDB = new List();
	}
	/**
	 * atoms and rules
	 * @throws ReasonerStateException 
	 * @throws IOException 
	 * @throws IncompatiblePredicateArityException 
	 * @throws EdbIdbSeparationException 
	 */
	public void rulesImplementation() throws ReasonerStateException, EdbIdbSeparationException, IncompatiblePredicateArityException, IOException {
		/* nom(?x) :- inst(?x) . */
		Atom nomX = Expressions.makeAtom("nom", x);
		Atom instX = Expressions.makeAtom("inst", x, y);
		Atom tripleX = Expressions.makeAtom("triple",x,y,z);
		Atom topX = Expressions.makeAtom("top", x);
		Atom botX = Expressions.makeAtom("bot", x);
		Atom clsX = Expressions.makeAtom("cls", x);
		Atom subClassX = Expressions.makeAtom("subClass", x,y);
		Atom subConjX = Expressions.makeAtom("subConj", x,y);
		Atom selfX = Expressions.makeAtom("self", x,x);
		Atom subExX = Expressions.makeAtom("subEx", x,y);
		Atom supExX = Expressions.makeAtom("supEx", x,y,z,a);
		
		//rules
		Rule rule1 = Expressions.makeRule(nomX, instX);
		Rule rule2 = Expressions.makeRule(Expressions.makeConjunction(nomX,tripleX), Expressions.makeConjunction(instX));
		Rule rule3 = Expressions.makeRule(Expressions.makeConjunction(subClassX,instX), Expressions.makeConjunction(instX));
		Rule rule4 = Expressions.makeRule(Expressions.makeConjunction(subConjX,instX,instX), Expressions.makeConjunction(instX));
		Rule rule5 = Expressions.makeRule(Expressions.makeConjunction(subExX,tripleX,instX), Expressions.makeConjunction(instX));
		Rule rule6 = Expressions.makeRule(Expressions.makeConjunction(subExX,selfX,instX), Expressions.makeConjunction(instX));
		Rule rule7 = Expressions.makeRule(Expressions.makeConjunction(supExX,instX), Expressions.makeConjunction(tripleX));
		Rule rule8 = Expressions.makeRule(Expressions.makeConjunction(supExX,instX), Expressions.makeConjunction(instX));
		
		//Rule rule9 = Expressions.makeRule(headAtom, bodyAtoms);
		//Rule rule10 = Expressions.makeRule(head, body);
		Reasoner reasoner = Reasoner.getInstance();
		reasoner.addRules(rule1, rule2, rule3, rule4, rule5, rule6, rule7, rule8);
		reasoner.addFacts(setFacts);
		reasoner.load();


	}

	/**
	 * instantiating facts and entities
	 */
	public void factBase() {
		long i=0,j=0,k=0;
		String x;
		for (String s: InferenceForOWLELMain.v_individualNames){
			//make predicates
			
			mapEDBPredicates.put(s,Expressions.makePredicate(s.substring(1,s.length() - 1 ), 1));
			mapIDBPredicates.put(s,Expressions.makePredicate("inst", 2));
			System.out.println(s.substring(5, s.length()-2));
			
			setFacts.add(Expressions.makeAtom(s.substring(1, s.length()-1), Expressions.makeConstant(s.substring(5, s.length()-2))));
		}
		
		for (String s: InferenceForOWLELMain.v_classNames) {
			//make predicates
			System.out.println(s.substring(5, s.length()-2));
			setFacts.add(Expressions.makeAtom(s.substring(1, s.length()-1), Expressions.makeConstant(s.substring(5, s.length()-2))));
		}
		
		for (String s: InferenceForOWLELMain.v_roleNames) {
			//make predicates
			System.out.println(s.substring(5, s.length()-2));
			setFacts.add(Expressions.makeAtom(s.substring(1, s.length()-1), Expressions.makeConstant(s.substring(5, s.length()-2))));
		}
		
		for (String s: InferenceForOWLELMain.v_otherLogicalAxioms) {
			System.out.println(s.substring(5, s.length()-2));
			setFacts.add(Expressions.makeAtom(s.substring(1, s.length()-1), Expressions.makeConstant(s.substring(5, s.length()-2))));
		}
	}
}
