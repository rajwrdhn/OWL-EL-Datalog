import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.vlog4j.core.model.api.Predicate;
import org.semanticweb.vlog4j.core.model.api.Rule;
import org.semanticweb.vlog4j.core.model.api.Variable;
import org.semanticweb.vlog4j.core.model.implementation.Expressions;

public class DatalogRules extends VisitNormalisedAxioms{
	protected final List<Rule> v_l_rules = new ArrayList<>();

	protected Predicate v_inst = Expressions.makePredicate("inst",2);
	protected Predicate v_triple = Expressions.makePredicate("triple",3);
	protected Predicate v_self = Expressions.makePredicate("self", 2);

	protected Variable x = Expressions.makeVariable("x");
	protected Variable y = Expressions.makeVariable("y");
	protected Variable z = Expressions.makeVariable("z");
	protected Variable v = Expressions.makeVariable("v");
	protected Variable w = Expressions.makeVariable("w");

	public DatalogRules(Set<OWLAxiom> normaxms) {
		super(normaxms);
	}

	public void makeRules() {

		// nom(x) :- v_inst(x,x)
		v_l_rules.add(Expressions.makeRule(Expressions.makeAtom(v_inst, x,x), Expressions.makeAtom(v_nomEDB, x)));
		
		//nom(x), v_triple(x,y,x) :- v_self(x,y)
		v_l_rules.add(Expressions.makeRule(
				Expressions.makeConjunction(Expressions.makeAtom(v_self, x,y)),Expressions.makeConjunction(
						Expressions.makeAtom(v_nomEDB, x),
						Expressions.makeAtom(v_triple, x,y,x)
						)));

		//v_topEDB(x), v_inst(y,z) :- v_inst (y,x)
		v_l_rules.add(Expressions.makeRule(
				Expressions.makeConjunction(Expressions.makeAtom(v_inst, y,x)),Expressions.makeConjunction(
						Expressions.makeAtom(v_topEDB, x),
						Expressions.makeAtom(v_inst, y,z)
						)));

		//use botEDB and clsEDB
		//bot(z) , v_inst (x,z) , v_inst (v,w) , cls(y) :- v_inst (v,y)
		v_l_rules.add(Expressions.makeRule(
				Expressions.makeConjunction(Expressions.makeAtom(v_inst, v,y)),
				Expressions.makeConjunction(Expressions.makeAtom(v_botEDB, z),
				Expressions.makeAtom(v_inst, x,z),
				Expressions.makeAtom(v_inst, v,w),
				Expressions.makeAtom(v_clsEDB, y))));

		//rule subclass (A,B) , subclass(B,C) :- subclass (A,C)   
		v_l_rules.add(Expressions.makeRule(
				Expressions.makeConjunction(Expressions.makeAtom(v_inst, x,z)),
				Expressions.makeConjunction(
						Expressions.makeAtom(v_subClassEDB, x,y),
						Expressions.makeAtom(v_inst, y,z)
						)));

		//subConj(x,y,z) , v_inst(v,x) , v_inst(v,y) :- v_inst(v,z)
		v_l_rules.add(Expressions.makeRule(
				Expressions.makeConjunction(Expressions.makeAtom(v_inst, v,z)),
				Expressions.makeConjunction(
						Expressions.makeAtom(v_subConjEDB, x,y,z),
						Expressions.makeAtom(v_inst, v,x),
						Expressions.makeAtom(v_inst, v,y)
						)));

		//subEx(v,y,z) , v_triple(x,v,w) , v_inst(w,y) :- v_inst(x,z)
		v_l_rules.add(Expressions.makeRule(
				Expressions.makeConjunction(Expressions.makeAtom(v_inst, x,z)),
				Expressions.makeConjunction(
						Expressions.makeAtom(v_subExEDB, v,y,z),
						Expressions.makeAtom(v_triple, x,v,w),
						Expressions.makeAtom(v_inst, w,y)
						)));
		//subEx(v,y,z) , v_self(x,v) , v_inst(x,y) :- v_inst(x,z)
		v_l_rules.add(Expressions.makeRule(
				Expressions.makeConjunction(Expressions.makeAtom(v_inst, x,z)),
				Expressions.makeConjunction(
						Expressions.makeAtom(v_subExEDB, v,y,z),
						Expressions.makeAtom(v_self, x,v),
						Expressions.makeAtom(v_inst, x,y)
						)));

		//supEx(v,w,y,z) , v_inst(x,v) :- v_triple(z,y)
		v_l_rules.add(Expressions.makeRule(
				Expressions.makeConjunction(Expressions.makeAtom(v_inst, z,y)),
				Expressions.makeConjunction(
						Expressions.makeAtom(v_supExEDB, v,w,y,z),
						Expressions.makeAtom(v_inst, x,v)
						)));
		//supEx(v,w,x,y) , v_inst(z,v) :- v_inst(y,x)
		v_l_rules.add(Expressions.makeRule(
				Expressions.makeConjunction(Expressions.makeAtom(v_inst, y,x)),
				Expressions.makeConjunction(
						Expressions.makeAtom(v_supExEDB, v,w,x,y),
						Expressions.makeAtom(v_inst, z,v)
						)));

		//subSelf(v,z) , v_self(x,v) :- v_inst(x,z)
		v_l_rules.add(Expressions.makeRule(
				Expressions.makeConjunction(Expressions.makeAtom(v_inst, x,z)),
				Expressions.makeConjunction(
						Expressions.makeAtom(v_subSelfEDB, v,z),
						Expressions.makeAtom(v_self, x,v)
						)));

		//supSelf(y,v) , v_inst(x,y) :- v_self(x,v)
		v_l_rules.add(Expressions.makeRule(
				Expressions.makeConjunction(Expressions.makeAtom(v_self, x,v)),
				Expressions.makeConjunction(
						Expressions.makeAtom(v_supSelfEDB, y,v),
						Expressions.makeAtom(v_inst, x,y)
						)));

		//v_inst(x,y) , nom(y) , v_inst(x,z) :- v_inst(y,z)
		v_l_rules.add(Expressions.makeRule(
				Expressions.makeConjunction(Expressions.makeAtom(v_inst, y,z)),
				Expressions.makeConjunction(
						Expressions.makeAtom(v_inst, x,y),
						Expressions.makeAtom(v_nomEDB, y),
						Expressions.makeAtom(v_inst, x,z)
						)));
		//v_inst(x,y) , nom(y) , v_inst(x,z) :- v_inst(x,z)
		v_l_rules.add(Expressions.makeRule(
				Expressions.makeConjunction(Expressions.makeAtom(v_inst, x,z)),
				Expressions.makeConjunction(
						Expressions.makeAtom(v_inst, x,y),
						Expressions.makeAtom(v_nomEDB, y),
						Expressions.makeAtom(v_inst, x,z)
						)));

		//v_inst(x,y) , nom(y) , v_triple(z,v,x) :- v_triple(z,v,y)
		v_l_rules.add(Expressions.makeRule(
				Expressions.makeConjunction(Expressions.makeAtom(v_triple, z,v,y)),
				Expressions.makeConjunction(
						Expressions.makeAtom(v_inst, x,y),
						Expressions.makeAtom(v_nomEDB, y),
						Expressions.makeAtom(v_triple, z,v,x)
						)));

		//role rules (13) (14) from paper
		v_l_rules.add(Expressions.makeRule(
				Expressions.makeConjunction(Expressions.makeAtom(v_triple, x,w,y)),
				Expressions.makeConjunction(
						Expressions.makeAtom(v_subRoleEDB, v,w),
						Expressions.makeAtom(v_triple, x,v,y)
						)));		
		v_l_rules.add(Expressions.makeRule(
				Expressions.makeConjunction(Expressions.makeAtom(v_self, x,w)),
				Expressions.makeConjunction(
						Expressions.makeAtom(v_subRoleEDB, v,w),
						Expressions.makeAtom(v_self, x,y)
						)));

	}
}