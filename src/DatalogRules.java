import org.semanticweb.vlog4j.core.model.api.Predicate;
import org.semanticweb.vlog4j.core.model.implementation.Expressions;

public class DatalogRules extends DatalogTranslation{
	public DatalogRules() {
		// TODO Auto-generated constructor stub
	}
	
	public void makeRules() {
		for (Predicate nom : v_s_nomEDB) {
			// nom(x) :- v_P_inst(x,x)
			v_l_Rules.add(Expressions.makeRule(Expressions.makeAtom(nom, x), 
					Expressions.makeAtom(v_P_inst, x,x)));
			//nom(x), v_P_triple(x,y,x) :- v_P_self(x,y)
			v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(nom, x),
					Expressions.makeAtom(v_P_triple, x,y,x)
					),
					Expressions.makeConjunction(Expressions.makeAtom(v_P_self, x,y))));
		}


		//top(x), v_P_inst(y,z) :- v_P_inst (y,x)
		for (Predicate top : v_s_topEDB) {
			v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(top, x),
					Expressions.makeAtom(v_P_inst, y,z)
					),
					Expressions.makeConjunction(Expressions.makeAtom(v_P_inst, y,x))));
		}
		//use botEDB and clsEDB
		//bot(z) , v_P_inst (x,z) , v_P_inst (v,w) , cls(y) :- v_P_inst (v,y)
		for (Predicate cls : v_s_clsEDB) {
			for (Predicate bot : v_s_botEDB) {
				v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
						Expressions.makeAtom(bot, z),
						Expressions.makeAtom(v_P_inst, x,z),
						Expressions.makeAtom(v_P_inst, v,w),
						Expressions.makeAtom(cls, y)
						),
						Expressions.makeConjunction(Expressions.makeAtom(v_P_inst, v,y))));
			}
		}


		for (Predicate subclass : v_s_subClassEDB) {
			//rule subclass (A,B) , subclass(B,C) :- subclass (A,C)   
			v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(subclass, x,y),
					Expressions.makeAtom(v_P_inst, y,z)
					),
					Expressions.makeConjunction(Expressions.makeAtom(v_P_inst, x,z))));
		}

		//subConj(x,y,z) , v_P_inst(v,x) , v_P_inst(v,y) :- v_P_inst(v,z)
		for (Predicate subconj : v_s_subConjEDB) {
			v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(subconj, x,y,z),
					Expressions.makeAtom(v_P_inst, v,x),
					Expressions.makeAtom(v_P_inst, v,y)
					),
					Expressions.makeConjunction(Expressions.makeAtom(v_P_inst, v,z))));
		}

		//subEx(v,y,z) , v_P_triple(x,v,w) , v_P_inst(w,y) :- v_P_inst(x,z)
		for (Predicate subex : v_s_subExEDB) {
			v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(subex, v,y,z),
					Expressions.makeAtom(v_P_triple, x,v,w),
					Expressions.makeAtom(v_P_inst, w,y)
					),
					Expressions.makeConjunction(Expressions.makeAtom(v_P_inst, x,z))));
			//subEx(v,y,z) , v_P_self(x,v) , v_P_inst(x,y) :- v_P_inst(x,z)
			v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(subex, v,y,z),
					Expressions.makeAtom(v_P_self, x,v),
					Expressions.makeAtom(v_P_inst, x,y)
					),
					Expressions.makeConjunction(Expressions.makeAtom(v_P_inst, x,z))));
		}



		for (Predicate supex : v_s_supExEDB) {
			//supEx(v,w,y,z) , v_P_inst(x,v) :- v_P_triple(z,y)
			v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(supex, v,w,y,z),
					Expressions.makeAtom(v_P_inst, x,v)
					),
					Expressions.makeConjunction(Expressions.makeAtom(v_P_inst, z,y))));
			//supEx(v,w,x,y) , v_P_inst(z,v) :- v_P_inst(y,x)
			v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(supex, v,w,x,y),
					Expressions.makeAtom(v_P_inst, z,v)
					),
					Expressions.makeConjunction(Expressions.makeAtom(v_P_inst, y,x))));
		}

		for (Predicate subself : v_s_subSelfEDB) {
			//subSelf(v,z) , v_P_self(x,v) :- v_P_inst(x,z)
			v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(subself, v,z),
					Expressions.makeAtom(v_P_self, x,v)
					),
					Expressions.makeConjunction(Expressions.makeAtom(v_P_inst, x,z))));
		}
		for (Predicate supself : v_s_supSelfEDB) {
			//supSelf(y,v) , v_P_inst(x,y) :- v_P_self(x,v)
			v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(supself, y,v),
					Expressions.makeAtom(v_P_inst, x,y)
					),
					Expressions.makeConjunction(Expressions.makeAtom(v_P_self, x,v))));
		}	
		for (Predicate nom : v_s_nomEDB) {
			//v_P_inst(x,y) , nom(y) , v_P_inst(x,z) :- v_P_inst(y,z)
			v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(v_P_inst, x,y),
					Expressions.makeAtom(nom, y),
					Expressions.makeAtom(v_P_inst, x,z)
					),
					Expressions.makeConjunction(Expressions.makeAtom(v_P_inst, y,z))));
			//v_P_inst(x,y) , nom(y) , v_P_inst(x,z) :- v_P_inst(x,z)
			v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(v_P_inst, x,y),
					Expressions.makeAtom(nom, y),
					Expressions.makeAtom(v_P_inst, x,z)
					),
					Expressions.makeConjunction(Expressions.makeAtom(v_P_inst, x,z))));

			//v_P_inst(x,y) , nom(y) , v_P_triple(z,v,x) :- v_P_triple(z,v,y)
			v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(v_P_inst, x,y),
					Expressions.makeAtom(nom, y),
					Expressions.makeAtom(v_P_triple, z,v,x)
					),
					Expressions.makeConjunction(Expressions.makeAtom(v_P_triple, z,v,y))));
		}
		for (Predicate subrole : v_s_subRoleEDB) {
			//role rules (13) (14) from paper
			v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(subrole, v,w),
					Expressions.makeAtom(v_P_triple, x,v,y)
					),
					Expressions.makeConjunction(Expressions.makeAtom(v_P_triple, x,w,y))));		
			v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(subrole, v,w),
					Expressions.makeAtom(v_P_self, x,y)
					),
					Expressions.makeConjunction(Expressions.makeAtom(v_P_self, x,w))));
		}
	}
}
