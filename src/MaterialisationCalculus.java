import java.awt.List;
import org.semanticweb.vlog4j.core.model.api.Variable;
import org.semanticweb.vlog4j.core.model.implementation.Expressions;


/*
 * Make Expressions, Atoms and Rules 
 */
public class MaterialisationCalculus {
	final List listIDBPredicates;
	final List listEDBPredicates;
	final List listRules;
	final List listAtoms;
	//final Predicate inst = Expressions.makePredicate(name, 2);
	final Variable x = Expressions.makeVariable("x");
	final Variable y = Expressions.makeVariable("y");
	final Variable z = Expressions.makeVariable("z");
	
	public MaterialisationCalculus() {
		listIDBPredicates = new List();
		listEDBPredicates = new List();
		listRules = new List();
		listAtoms = new List();
	}
	
	public void rulesImplementation() {
		//rule 1
		
	}
	public void factBase() {
		
	}
	public void visitAll() {
		
	}	
}
