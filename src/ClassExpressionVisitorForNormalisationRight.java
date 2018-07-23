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
import org.semanticweb.owlapi.model.OWLIndividual;
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

public class ClassExpressionVisitorForNormalisationRight extends AxiomVisitorForNormalisation implements OWLClassExpressionVisitor{

	public ClassExpressionVisitorForNormalisationRight(OWLDataFactory factory) {
		super(factory);
	}

	@Override
	public void visit(OWLClass ce) {
		if (ce.isOWLThing()) {			
			//Empty
		} else {
			getV_Normalised_Axioms().add(addSubClassAxiom(getCurrentClassExpression(), ce));
		}
	}

	@Override
	public void visit(OWLObjectIntersectionOf ce) {
		
		//OWLClassExpression new_expr = addFreshClassName(v_counter_FreshConcept);
		
		for (OWLClassExpression ce1 : ce.asConjunctSet()) {
			if(isNonComplementOFNamedClass(ce1)) {
				getV_Normalised_Axioms().add(addSubClassAxiom(getCurrentClassExpression(), ce1));				
			} /*else {
				v_For_FurtherNormalisation.add(addSubClassAxiom(getCurrentClassExpression(), ce1));
			}*/
		}
	}

	@Override
	public void visit(OWLObjectUnionOf ce) {
		throw new IllegalArgumentException("OWLObjectUnionOf not OWL 2 EL ! " + ce.toString()  );
	}

	@Override
	public void visit(OWLObjectComplementOf ce) {
		throw new IllegalArgumentException("OWLObjectComplementOf not OWL 2 EL ! " + ce.toString()  );
	}

	@Override
	public void visit(OWLObjectSomeValuesFrom ce) {
		
		OWLClassExpression new_expr = addFreshClassName(v_counter_FreshConcept);
		v_counter_FreshConcept++;
		
		if (isNonComplementOFNamedClass(ce.getFiller())) {
			//v_Normalised_Axioms.add(addSubClassAxiom(new_expr, ce.getFiller()));
			getV_Normalised_Axioms().add(addSomevaluesFromAxiomRight(getCurrentClassExpression(), ce.getProperty(), new_expr));

		} else {
			//v_For_FurtherNormalisation.add(addSubClassAxiom(new_expr, ce.getFiller()));
			getV_Normalised_Axioms().add(addSomevaluesFromAxiomRight(getCurrentClassExpression(), ce.getProperty(), new_expr));
		}
		
		setCurrentClassExpression(new_expr);		
	}

	@Override
	public void visit(OWLObjectAllValuesFrom ce) {
		throw new IllegalStateException("OWLObjectAllValuesFrom " + ce.toString());
	}

	@Override
	public void visit(OWLObjectHasValue ce) {
		if (getCurrentClassExpression() instanceof OWLIndividual) {
			getV_Normalised_Axioms().add(v_factory.getOWLObjectPropertyAssertionAxiom(ce.getProperty(), (OWLIndividual) getCurrentClassExpression(), ce.getFiller()));
		} else {
			getV_Normalised_Axioms().add(addSubClassAxiom(getCurrentClassExpression(), (OWLClassExpression)ce.getFiller()));
		}
	}

	@Override
	public void visit(OWLObjectMinCardinality ce) {
		
		OWLClassExpression new_expr = addFreshClassName(v_counter_FreshConcept);
		v_counter_FreshConcept++;
		
		if (isNonComplementOFNamedClass(ce.getFiller())) {
			getV_Normalised_Axioms().add(addSubClassAxiom(new_expr, ce));
			getV_Normalised_Axioms().add(addSomevaluesFromAxiomRight(getCurrentClassExpression(), ce.getProperty(), new_expr));

		} else {
			//v_For_FurtherNormalisation.add(addSubClassAxiom(new_expr, ce.getFiller()));
			getV_Normalised_Axioms().add(addSomevaluesFromAxiomRight(getCurrentClassExpression(), ce.getProperty(), new_expr));
		}
		
		setCurrentClassExpression(new_expr);		
	}

	@Override
	public void visit(OWLObjectExactCardinality ce) {
		throw new IllegalStateException("OWLObjectExactCardinality " + ce.toString());
	}

	@Override
	public void visit(OWLObjectMaxCardinality ce) {
		throw new IllegalStateException("OWLObjectMaxCardinality " + ce.toString());
	}

	@Override
	public void visit(OWLObjectHasSelf ce) {		
		getV_Normalised_Axioms().add(addSubClassAxiom(getCurrentClassExpression(), ce));
	}

	@Override
	public void visit(OWLObjectOneOf ce) {
		throw new IllegalStateException("OWLObjectOneOf " + ce.toString());
	}

	@Override
	public void visit(OWLDataSomeValuesFrom ce) {
		throw new IllegalStateException("OWLDataSomeValuesFrom " + ce.toString());
	}

	@Override
	public void visit(OWLDataAllValuesFrom ce) {
		throw new IllegalStateException("OWLDataAllValuesFrom " + ce.toString());
	}

	@Override
	public void visit(OWLDataHasValue ce) {
		throw new IllegalStateException("OWLDataHasValue " + ce.toString());
	}

	@Override
	public void visit(OWLDataMinCardinality ce) {
		throw new IllegalStateException("OWLDataMinCardinality " + ce.toString());
	}

	@Override
	public void visit(OWLDataExactCardinality ce) {
		throw new IllegalStateException("OWLDataExactCardinality " + ce.toString());
	}

	@Override
	public void visit(OWLDataMaxCardinality ce) {
		throw new IllegalStateException("OWLDataMaxCardinality " + ce.toString());
	}
}
