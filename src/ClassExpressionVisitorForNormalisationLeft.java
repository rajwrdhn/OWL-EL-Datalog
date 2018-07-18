import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitor;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;

/**
 * Visiting the sub class expression of subclassAxioms 
 * Super class expression is a named class!
 * @author raj
 *
 */
public class ClassExpressionVisitorForNormalisationLeft extends AxiomVisitorForNormalisation implements OWLClassExpressionVisitor {

	protected static OWLClassExpression v_classExpression = null;  
	public ClassExpressionVisitorForNormalisationLeft(OWLDataFactory factory) {
		super(factory);
		// TODO Auto-generated constructor stub
	}

	//A and B subsumes classexpression

	public void insertNormalisedConjunctionAsAxiom(OWLClassExpression sub_conj_ce) {
		v_Normalised_Axioms.add(v_factory.getOWLSubClassOfAxiom(sub_conj_ce, v_Right_Named_ClassExpression));
	}

	public boolean checkForNormalisedConjunct(OWLClassExpression sub_conj_ce) {
		int i = sub_conj_ce.asConjunctSet().size();
		boolean[] v_bool = new boolean[i];
		Iterator<OWLClassExpression> iter = sub_conj_ce.asConjunctSet().iterator();

		while(iter.hasNext()) {
			if(isNonComplementOFNamedClass(iter.next())) {
				v_bool[i] = true;
			}
			i--;
		}

		for(boolean b : v_bool) {
			if(!b) return false;
		} 
		return true;		
	}

	public void normalizeConjunctof(OWLClassExpression sub_conj) {
		Set<OWLClassExpression> descriptions = sub_conj.asConjunctSet(); 
		Set<OWLClassExpression> new_descriptions = new HashSet<>();
		int n = descriptions.size() % 2;
		int i = 0;
		for (OWLClassExpression ce: descriptions) {
			if(i==n)
				break;
			new_descriptions.add(ce);
			i++;
		}
		descriptions.removeAll(new_descriptions);
		OWLClassExpression ce1 = getIntersectionOf(descriptions);
		OWLClassExpression ce2 = getIntersectionOf(new_descriptions);
		insertIntoAxiomMap(ce1, ce2);
	}

	public void  insertIntoAxiomMap(OWLClassExpression ce1, OWLClassExpression ce2) {
		if (isNonComplementOFNamedClass(ce1)) {
			OWLAxiom axiom = v_factory.getOWLSubClassOfAxiom(
					v_factory.getOWLObjectIntersectionOf(addFreshClassName(freshConceptNumber),ce1),getCurrentClassExpression());
			v_Normalised_Axioms.add(axiom);
			addAxiomToMap(v_Iterable_KeyForMap+1,
					v_factory.getOWLSubClassOfAxiom(ce2, addFreshClassName(freshConceptNumber))
					);
			freshConceptNumber++;
		} else if (isNotNamedClass(ce1) && isNotNamedClass(ce2)) {
			OWLAxiom axiom = v_factory.getOWLSubClassOfAxiom(
					v_factory.getOWLObjectIntersectionOf(addFreshClassName(freshConceptNumber),ce1),getCurrentClassExpression());
			addAxiomToMap(v_Iterable_KeyForMap+1, axiom);
			addAxiomToMap(v_Iterable_KeyForMap+1,
					v_factory.getOWLSubClassOfAxiom(ce2, addFreshClassName(freshConceptNumber))
					);
			freshConceptNumber++;
		} else {
			OWLAxiom axiom = addAxiomOfConjunctSubClass(addFreshClassName(freshConceptNumber), ce2, getCurrentClassExpression());					
			v_Normalised_Axioms.add(axiom);
			addAxiomToMap(v_Iterable_KeyForMap+1,
					addSubClassAxiom(ce1, addFreshClassName(freshConceptNumber))
					);
			freshConceptNumber++;
		}
	}

	public OWLClassExpression getIntersectionOf(Set<OWLClassExpression> ce_conjunct) {

		if (ce_conjunct.size() ==1) {
			return ce_conjunct.iterator().next();
		} else {
			return v_factory.getOWLObjectIntersectionOf(ce_conjunct);
		}	
	}

	/**
	 * set the class expression to be used as super ClassExpression Normalize 
	 */
	public void setCurrentClassExpression(OWLClassExpression ce) {
		v_classExpression = ce;
	}

	@Override
	public void visit(OWLClass ce) {
		if (ce.isOWLNamedIndividual()) {
			v_Normalised_Axioms.add(v_factory.getOWLSubClassOfAxiom(ce, v_Right_Named_ClassExpression));
		}
		/*		if(ce.isOWLNothing()) {
			v_Normalised_Axioms.add(v_factory.getOWLSubClassOfAxiom(ce, v_Right_Named_ClassExpression));
		} else {
			v_Normalised_Axioms.add(v_factory.getOWLSubClassOfAxiom(ce, v_Right_Named_ClassExpression));
		}*/
	}

	@Override
	public void visit(OWLObjectIntersectionOf ce) {
		int size = ce.asConjunctSet().size();
		Iterator<OWLClassExpression> iter = ce.asConjunctSet().iterator();
		if (size == 2) {
			if (this.checkForNormalisedConjunct(ce)) {
				this.insertNormalisedConjunctionAsAxiom(ce);
			} else {
				setCurrentClassExpression(v_Right_Named_ClassExpression);
				while(iter.hasNext()) {
					if (isNonComplementOFNamedClass(iter.next())) {
						v_Normalised_Axioms.add(v_factory.getOWLSubClassOfAxiom(
								v_factory.getOWLObjectIntersectionOf(iter.next(),addFreshClassName(freshConceptNumber)), getCurrentClassExpression()));
						setCurrentClassExpression(addFreshClassName(freshConceptNumber));
					} else {
						addAxiomToMap(v_Iterable_KeyForMap+1,
								v_factory.getOWLSubClassOfAxiom(iter.next(), v_Right_Named_ClassExpression)
								);
					}
				}
			}
		} else if (size>=3){
			normalizeConjunctof(ce);
		} else {
			System.out.println("visitor ce in left check ----"+ce.toString());
		}
	}

	@Override
	public void visit(OWLObjectUnionOf ce) {
		throw new IllegalStateException();
	}

	@Override
	public void visit(OWLObjectComplementOf ce) {
		throw new IllegalStateException();
	}

	@Override
	public void visit(OWLObjectSomeValuesFrom ce) {
		// TODO Auto-generated method stub
		OWLClassExpressionVisitor.super.visit(ce);
	}

	@Override
	public void visit(OWLObjectAllValuesFrom ce) {
		throw new IllegalStateException();
	}

	@Override
	public void visit(OWLObjectHasValue ce) {
		ce.asSomeValuesFrom();
	}

	@Override
	public void visit(OWLObjectMinCardinality ce) {
		
	}

	@Override
	public void visit(OWLObjectExactCardinality ce) {
		throw new IllegalStateException();
	}

	@Override
	public void visit(OWLObjectMaxCardinality ce) {
		throw new IllegalStateException();
	}

	@Override
	public void visit(OWLObjectHasSelf ce) {
		
	}

	@Override
	public void visit(OWLObjectOneOf ce) {
		throw new IllegalStateException();
	}

	@Override
	public void visit(OWLDataSomeValuesFrom ce) {
		throw new IllegalStateException();
	}

	@Override
	public void visit(OWLDataAllValuesFrom ce) {
		throw new IllegalStateException();
	}

	@Override
	public void visit(OWLDataHasValue ce) {
		throw new IllegalStateException();
	}

	@Override
	public void visit(OWLDataMinCardinality ce) {
		throw new IllegalStateException();
	}

	@Override
	public void visit(OWLDataExactCardinality ce) {
		throw new IllegalStateException();
	}

	@Override
	public void visit(OWLDataMaxCardinality ce) {
		throw new IllegalStateException();
	}


}
