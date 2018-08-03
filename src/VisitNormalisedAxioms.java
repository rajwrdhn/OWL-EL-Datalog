import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLAxiomVisitor;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointUnionAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLHasKeyAxiom;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.vlog4j.core.model.api.Atom;
import org.semanticweb.vlog4j.core.model.api.Constant;
import org.semanticweb.vlog4j.core.model.api.Predicate;
import org.semanticweb.vlog4j.core.model.api.Term;
import org.semanticweb.vlog4j.core.model.implementation.Expressions;

public class VisitNormalisedAxioms extends DatalogTranslation implements OWLAxiomVisitor{


	protected static long auxnum = 0;

	protected static Set<Atom> v_s_Facts = new HashSet<>();	
	
	protected Predicate v_nomEDB = Expressions.makePredicate("nomEDB", 1);
	protected Predicate v_clsEDB = Expressions.makePredicate("clsEDB", 1);
	protected Predicate v_rolEDB = Expressions.makePredicate("rolEDB", 1);
	protected Predicate v_subClassEDB = Expressions.makePredicate("subClassEDB", 2);
	protected Predicate v_topEDB = Expressions.makePredicate("topEDB", 1);
	protected Predicate v_botEDB = Expressions.makePredicate("botEDB", 1);
	protected Predicate v_subConjEDB = Expressions.makePredicate("subConjEDB", 3);
	protected Predicate v_subExEDB = Expressions.makePredicate("subExEDB", 3);
	protected Predicate v_supExEDB = Expressions.makePredicate("supExEDB", 4);
	protected Predicate v_subSelfEDB = Expressions.makePredicate("subSelfEDB", 2);
	protected Predicate v_supSelfEDB = Expressions.makePredicate("supSelfEDB", 2);
	protected Predicate v_subRoleEDB = Expressions.makePredicate("subRoleEDB", 2);


	public VisitNormalisedAxioms(Set<OWLAxiom> normalisedAxioms) {
		super(normalisedAxioms);
	}

	//Facts
	public void insertAsFact(Atom a) {
		v_s_Facts.add(a);
	}
	//return Constant
	public Constant getConstant(String s) {
		return Expressions.makeConstant(s);
	}
	
	//create Atom and insert as Fact
	public void toSingleConstantFacts(Predicate predicate, Term c1) {
		Atom fact1 = Expressions.makeAtom(predicate, c1);
		insertAsFact(fact1);
	}

	public void toDoubleConstantFacts(Predicate predicate, Term c1, Term c2) {
		Atom fact1 = Expressions.makeAtom(predicate, c1, c2);
		insertAsFact(fact1);
	}

	public void toThreeConstantFacts(Predicate predicate, Term c1, Term c2, Term c3) {
		Atom fact1 = Expressions.makeAtom(predicate, c1, c2, c3);
		insertAsFact(fact1);
	}

	public void toFourConstantFacts(Predicate predicate, Term c1, Term c2, Term c3, Term c4) {
		Atom fact1 =  Expressions.makeAtom(predicate, c1, c2, c3, c4);
		insertAsFact(fact1);
	}

	//get Facts
	public Set<Atom> getFacts() {		
		return v_s_Facts;
	}

	@Override
	public void visit(OWLAnnotationAssertionAxiom axiom) {

	}

	@Override
	public void visit(OWLSubAnnotationPropertyOfAxiom axiom) {

	}

	@Override
	public void visit(OWLAnnotationPropertyDomainAxiom axiom) {

	}

	@Override
	public void visit(OWLAnnotationPropertyRangeAxiom axiom) {

	}

	@Override
	public void visit(OWLSubClassOfAxiom axiom) {
		if(axiom.getSubClass().isClassExpressionLiteral() && !axiom.getSuperClass().isClassExpressionLiteral()) {
		
			ClassExpressionVisitorForNormalisedAxiomRight ce_visit = 
					new ClassExpressionVisitorForNormalisedAxiomRight(axiom.getSuperClass(), v_s_normalisedAxioms); 
			
			axiom.getSuperClass().accept(ce_visit);
		
		} else {
		
			ClassExpressionVisitorForNormalisedAxiomLeft ce_visit = 
					new ClassExpressionVisitorForNormalisedAxiomLeft(axiom.getSubClass(), v_s_normalisedAxioms); 
			
			axiom.getSubClass().accept(ce_visit);
		}

	}

	@Override
	public void visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
		throw new IllegalArgumentException();
	}

	@Override
	public void visit(OWLAsymmetricObjectPropertyAxiom axiom) {
		throw new IllegalArgumentException();
	}

	@Override
	public void visit(OWLReflexiveObjectPropertyAxiom axiom) {
		throw new IllegalArgumentException();
	}

	@Override
	public void visit(OWLDisjointClassesAxiom axiom) {
		throw new IllegalArgumentException();
	}

	@Override
	public void visit(OWLDataPropertyDomainAxiom axiom) {
		throw new IllegalArgumentException();
	}

	@Override
	public void visit(OWLObjectPropertyDomainAxiom axiom) {
		throw new IllegalArgumentException();
	}

	@Override
	public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
		throw new IllegalArgumentException();
	}

	@Override
	public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
		throw new IllegalArgumentException();
	}

	@Override
	public void visit(OWLDifferentIndividualsAxiom axiom) {
		throw new IllegalArgumentException();
	}

	@Override
	public void visit(OWLDisjointDataPropertiesAxiom axiom) {
		throw new IllegalArgumentException();
	}

	@Override
	public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
		throw new IllegalArgumentException();
	}

	@Override
	public void visit(OWLObjectPropertyRangeAxiom axiom) {
		throw new IllegalArgumentException();
	}

	@Override
	public void visit(OWLObjectPropertyAssertionAxiom axiom) {
		System.out.println(axiom.asOWLSubClassOfAxiom());
/*		Iterator<OWLNamedIndividual> iter = axiom.individualsInSignature().iterator();
		OWLNamedIndividual[] indi  = new OWLNamedIndividual[2];
		int i = 0;
		while (iter.hasNext()) {
			indi[i] = iter.next();
			i++;
		}
		
		Constant c1 = getConstant(axiom.getProperty().toString());
		Constant c2 = getConstant(indi[0].toString());
		Constant c3 = getConstant(indi[1].toString());

		toFourConstantFacts(v_supExEDB, c2, c1, c3, c3);		
		toSingleConstantFacts(v_nomEDB, c2);
		toSingleConstantFacts(v_nomEDB, c3);
		toSingleConstantFacts(v_rolEDB, c1);	*/
	}

	@Override
	public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
		throw new IllegalArgumentException();
	}

	@Override
	public void visit(OWLSubObjectPropertyOfAxiom axiom) {

		Constant c1 = getConstant(axiom.getSubProperty().toString());
		Constant c2 = getConstant(axiom.getSuperProperty().toString());
		
		toSingleConstantFacts(v_rolEDB, c1);
		toSingleConstantFacts(v_rolEDB, c2);
		toDoubleConstantFacts(v_subRoleEDB, c1, c2);		
	}

	@Override
	public void visit(OWLDisjointUnionAxiom axiom) {
		throw new IllegalArgumentException();
	}

	@Override
	public void visit(OWLSymmetricObjectPropertyAxiom axiom) {
		throw new IllegalArgumentException();
	}

	@Override
	public void visit(OWLDataPropertyRangeAxiom axiom) {
		throw new IllegalArgumentException();
	}

	@Override
	public void visit(OWLFunctionalDataPropertyAxiom axiom) {
		throw new IllegalArgumentException();
	}

	@Override
	public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
		throw new IllegalArgumentException();
	}

	@Override
	public void visit(OWLClassAssertionAxiom axiom) {

		Constant c1 = getConstant(axiom.getClassExpression().toString());
		Constant c2 = getConstant(axiom.getIndividual().toString());
		
		toDoubleConstantFacts(v_subClassEDB, c2, c1);
		toSingleConstantFacts(v_clsEDB, c1);
		toSingleConstantFacts(v_nomEDB, c2);
	}

	@Override
	public void visit(OWLEquivalentClassesAxiom axiom) {
		throw new IllegalArgumentException();
	}

	@Override
	public void visit(OWLDataPropertyAssertionAxiom axiom) {
		throw new IllegalArgumentException();
	}

	@Override
	public void visit(OWLTransitiveObjectPropertyAxiom axiom) {
		throw new IllegalArgumentException();
	}

	@Override
	public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
		throw new IllegalArgumentException();
	}

	@Override
	public void visit(OWLSubDataPropertyOfAxiom axiom) {
		throw new IllegalArgumentException();
	}

	@Override
	public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
		throw new IllegalArgumentException();
	}

	@Override
	public void visit(OWLSameIndividualAxiom axiom) {
		throw new IllegalArgumentException();
	}

	@Override
	public void visit(OWLSubPropertyChainOfAxiom axiom) {
		throw new IllegalArgumentException();
	}

	@Override
	public void visit(OWLInverseObjectPropertiesAxiom axiom) {
		throw new IllegalArgumentException();
	}

	@Override
	public void visit(OWLHasKeyAxiom axiom) {
		throw new IllegalArgumentException();
	}

	@Override
	public void visit(SWRLRule node) {
		throw new IllegalArgumentException();
	}
}
