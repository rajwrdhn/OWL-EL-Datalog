import java.awt.List;
import org.semanticweb.vlog4j.core.model.api.Variable;
import org.semanticweb.vlog4j.core.model.implementation.Expressions;


/*
 * Make Expressions, Atoms and Rules 
 */
public class MaterialisationCalculus {
	protected final List listIDBPredicates;
	//final List listEDBPredicates;
	//final List listRules;
	protected final List listAtomsIDB;
	protected final List listAtomsEDB;
	//final Predicate inst = Expressions.makePredicate(name, 2);
	protected final Variable x = Expressions.makeVariable("x");
	protected final Variable y = Expressions.makeVariable("y");
	protected final Variable z = Expressions.makeVariable("z");
	public MaterialisationCalculus() {
		listIDBPredicates = new List();
		//listEDBPredicates = new List();
		//listRules = new List();
		listAtomsIDB = new List();
		listAtomsEDB = new List();
	}
	/**
	 * rules
	 */
	public void rulesImplementation() {
		//rule 1
		
	}
	/**
	 * instantiating facts and entities
	 */
	public void factBase() {
		for (String s: InferenceForOWLELMain.v_individualNames) {
			//make predicate
			System.out.println(s.substring(1,s.length() - 1 ));
			//listAtomsEDB.add(Expressions.makeAtom(s.substring(1, s.length() -1 ), x));
		}
		
		for (String s: InferenceForOWLELMain.v_classNames) {
			System.out.println(s.substring(1, s.length() -1 ));
		}
		
		for (String s: InferenceForOWLELMain.v_roleNames) {
			System.out.println(s.substring(1, s.length() -1 ));
		}
	}
}
