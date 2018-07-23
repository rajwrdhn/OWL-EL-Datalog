import java.util.HashSet;
import java.util.Set;

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
	
	public ClassExpressionVisitorForNormalisationLeft(OWLDataFactory factory) {
		super(factory);
	}
	
	public void normalizeIntersectionOf(OWLClassExpression sub_conj) {
		
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
		descriptions.remove(null);
		new_descriptions.remove(null);
		
		OWLClassExpression ce1 = getIntersectionOf(descriptions);
		OWLClassExpression ce2 = getIntersectionOf(new_descriptions);
		
		//Normalisation
		if (isNonComplementOFNamedClass(ce1) && isNonComplementOFNamedClass(ce2)) {
			v_Normalised_Axioms.add(addAxiomOfConjunctSubClass(ce1, ce2, getCurrentClassExpression()));
		} else {
			insertIntoAxiomForNormalisation(ce1, ce2);
		}
	}
	
	public void  insertIntoAxiomForNormalisation(OWLClassExpression ce1, OWLClassExpression ce2) {
		
		OWLClassExpression newExpr = addFreshClassName(v_counter_FreshConcept);
		v_counter_FreshConcept++;
		
		if (ce1.asConjunctSet().size() >=2 && ce2.asConjunctSet().size()==1) {			
			
			if(isNonComplementOFNamedClass(ce2)) {
				
				v_Normalised_Axioms.add(addSubClassAxiom(ce2, newExpr));
				v_For_FurtherNormalisation.add(addAxiomOfConjunctSubClass(ce1, newExpr, getCurrentClassExpression()));
				setCurrentClassExpression(newExpr);
				
			
			} else {
				
				v_For_FurtherNormalisation.add(addSubClassAxiom(ce2, newExpr));
				v_For_FurtherNormalisation.add(addAxiomOfConjunctSubClass(ce1, newExpr, getCurrentClassExpression()));
				setCurrentClassExpression(newExpr);
			
			}
			
		} else if(ce1.asConjunctSet().size() ==1 && ce2.asConjunctSet().size() == 1) {
			
			if(isNonComplementOFNamedClass(ce1) && isNonComplementOFNamedClass(ce2)) {			
				v_Normalised_Axioms.add(addAxiomOfConjunctSubClass(ce1, ce2, getCurrentClassExpression()));
			
			} else {
			
				v_For_FurtherNormalisation.add(addAxiomOfConjunctSubClass(newExpr, ce2, getCurrentClassExpression()));
				v_For_FurtherNormalisation.add(addSubClassAxiom(ce1, newExpr));
				setCurrentClassExpression(newExpr);
			
			}
		} else {
			if(isNonComplementOFNamedClass(ce1)) {
			
				v_Normalised_Axioms.add(addSubClassAxiom(ce1, newExpr));
				v_For_FurtherNormalisation.add(addAxiomOfConjunctSubClass(ce2, newExpr, getCurrentClassExpression()));
				setCurrentClassExpression(newExpr);
			
			} else {
			
				v_For_FurtherNormalisation.add(addSubClassAxiom(ce1, getCurrentClassExpression()));
				v_For_FurtherNormalisation.add(addAxiomOfConjunctSubClass(ce2, newExpr, getCurrentClassExpression()));
				setCurrentClassExpression(newExpr);
			}
		}
	}
	
	public OWLClassExpression getIntersectionOf(Set<OWLClassExpression> ce_conjunct) {

		if (ce_conjunct.size() ==1) {
			return (OWLClassExpression)ce_conjunct;
		} else {
			return (OWLClassExpression)v_factory.getOWLObjectIntersectionOf(ce_conjunct);
		}	
	}
	
	@Override
	public void visit(OWLClass ce) {
		if(ce.isOWLNamedIndividual()) {
			v_Normalised_Axioms.add(addSubClassAxiom(ce, getCurrentClassExpression()));
		} else if (ce.isTopEntity()){			
			v_Normalised_Axioms.add(addSubClassAxiom(ce, getCurrentClassExpression()));
		} else {
			//Nothing //
		}
	}
	
	@Override
	public void visit(OWLObjectIntersectionOf ce) {
		normalizeIntersectionOf(ce); //done on top of the class
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
		if (isNonComplementOFNamedClass(ce.getFiller())) {
			v_Normalised_Axioms.add(addSubClassAxiom(ce, getCurrentClassExpression()));
		} else {

			OWLClassExpression new_Expr = addFreshClassName(v_counter_FreshConcept);
			v_counter_FreshConcept++;

			if (ce.getFiller() instanceof OWLObjectIntersectionOf) {
				v_For_FurtherNormalisation.add(addSubClassAxiom(ce.getFiller(), new_Expr));
				v_Normalised_Axioms.add(addSomevaluesFromAxiomLeft(new_Expr, ce.getProperty(), getCurrentClassExpression()));
				setCurrentClassExpression(new_Expr);
			} else {

				v_Normalised_Axioms.add(addSomevaluesFromAxiomLeft(new_Expr, ce.getProperty(), getCurrentClassExpression()));
				setCurrentClassExpression(new_Expr);
			}
		}
	}

	@Override
	public void visit(OWLObjectAllValuesFrom ce) {
		throw new IllegalStateException();
	}

	@Override
	public void visit(OWLObjectHasValue ce) {
		// Exists R {o} subsumes A
		v_Normalised_Axioms.add(addSubClassAxiom((OWLClassExpression)ce.getFiller(), getCurrentClassExpression()));
	}

	@Override
	public void visit(OWLObjectMinCardinality ce) {	
		if (isNonComplementOFNamedClass(ce.getFiller())) {
			v_Normalised_Axioms.add(addSubClassAxiom(ce, getCurrentClassExpression()));

		} else {

			OWLClassExpression new_Expr = addFreshClassName(v_counter_FreshConcept);
			v_counter_FreshConcept++;

			if (ce.getFiller() instanceof OWLObjectIntersectionOf) {
				v_For_FurtherNormalisation.add(addSubClassAxiom(ce.getFiller(), new_Expr));
				v_Normalised_Axioms.add(addSomevaluesFromAxiomLeft(new_Expr, ce.getProperty(), getCurrentClassExpression()));
				setCurrentClassExpression(new_Expr);
			} else {

				v_Normalised_Axioms.add(addSomevaluesFromAxiomLeft(new_Expr, ce.getProperty(), getCurrentClassExpression()));
				setCurrentClassExpression(new_Expr);
			}
		}
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
		v_Normalised_Axioms.add(addSubClassAxiom(ce, getCurrentClassExpression()));
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
