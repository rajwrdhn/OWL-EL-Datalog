import java.util.HashSet;
import java.util.Set;

import org.semanticweb.vlog4j.core.model.api.Atom;
import org.semanticweb.vlog4j.core.model.api.Predicate;
import org.semanticweb.vlog4j.core.model.api.Variable;
import org.semanticweb.vlog4j.core.model.implementation.Expressions;

import karmaresearch.vlog.Rule;


/*
 * Make Expressions, Atoms and Rules 
 */
public class MaterialisationCalculus {
	final Set<Predicate> setPredicates;
	final Set<Rule> setRules;
	final Set<Atom> setAtoms;
	//final Predicate inst = Expressions.makePredicate(name, 2);
	final Variable x = Expressions.makeVariable("x");
	final Variable y = Expressions.makeVariable("y");
	final Variable z = Expressions.makeVariable("z");
	
	public MaterialisationCalculus() {
		setPredicates = new HashSet<>();
		setRules = new HashSet<>();
		setAtoms = new HashSet<>();
	}
	
	public void rulesImplementation() {
		
	}
	public void factBase() {
		
	}
	public void visitAll() {
		
	}	
}
