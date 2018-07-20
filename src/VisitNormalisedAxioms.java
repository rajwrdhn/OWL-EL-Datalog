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
import org.semanticweb.vlog4j.core.model.implementation.Expressions;

public class VisitNormalisedAxioms extends DatalogTranslation implements OWLAxiomVisitor{
	public VisitNormalisedAxioms(Set<OWLAxiom> normalisedAxioms) {
		super(normalisedAxioms);
		// TODO Auto-generated constructor stub
	}

	protected static long auxnum = 0;
	
	protected Set<Atom> v_s_Facts = new HashSet<>();
	
	protected static Set<Predicate> v_s_nomEDB = new HashSet<>();
	protected static Set<Predicate> v_s_clsEDB = new HashSet<>();
	protected static Set<Predicate> v_s_rolEDB = new HashSet<>();	
	protected static Set<Predicate> v_s_subClassEDB = new HashSet<>();	
	protected static Set<Predicate> v_s_topEDB = new HashSet<>();
	protected static Set<Predicate> v_s_botEDB = new HashSet<>();
	protected static Set<Predicate> v_s_subConjEDB = new HashSet<>();
	protected static Set<Predicate> v_s_subExEDB = new HashSet<>();
	protected static Set<Predicate> v_s_supExEDB = new HashSet<>();
	protected static Set<Predicate> v_s_subSelfEDB = new HashSet<>();
	protected static Set<Predicate> v_s_supSelfEDB = new HashSet<>();
	protected static Set<Predicate> v_s_subRoleEDB = new HashSet<>();
	
	//EDB Predicates
	public void addToSubClassEDB(Predicate predicate) {
		v_s_subClassEDB.add(predicate);
	}
	
	public void addToSubConjEDB(Predicate predicate) {
		v_s_subConjEDB.add(predicate);
	}
	
	public void addToSubExEDB(Predicate predicate) {
		v_s_subExEDB.add(predicate);
	}
	
	public void addToSupExEDB(Predicate predicate) {
		v_s_supExEDB.add(predicate);
	}
	
	public void addToSubSelfEDB(Predicate predicate) {
		v_s_subSelfEDB.add(predicate);
	}
	
	public void addToSupSelfEDB(Predicate predicate) {
		v_s_subSelfEDB.add(predicate);
	}
	
	public void addToSubRoleEDB(Predicate predicate) {
		v_s_subRoleEDB.add(predicate);
	}
	
	public void addTopEDB(String predicatename) {
		v_s_topEDB.add(Expressions.makePredicate(predicatename, 1));
	}
	
	public void addBotEDB(String predicatename) {
		v_s_botEDB.add(Expressions.makePredicate(predicatename, 1));
	}
	
	public void addrolesEDB(String predicatename) {
		v_s_rolEDB.add(Expressions.makePredicate(predicatename, 1));
	}
	
	public void addNominalsEDB(String predicatename) {
		v_s_nomEDB.add(Expressions.makePredicate(predicatename, 1));
	}
	
	public void addClassNamesEDB(String predicatename) {
		v_s_clsEDB.add(Expressions.makePredicate(predicatename, 1));
	}
	
	//Facts
	public void addToDoubleConstantFacts(Predicate predicate, Constant c1, Constant c2) {
		Atom a = Expressions.makeAtom(predicate, c1, c2);
		//Add Atoms
		v_s_Facts.add(a);
	}
	
	public void addToFourConstantFacts(Predicate predicate, Constant c1, Constant c2, Constant c3, Constant c4) {
		Atom a = Expressions.makeAtom(predicate, c1, c2, c3, c4);
		//Add Atoms
		v_s_Facts.add(a);
	}
	
	public void addToThreeConstantFacts(Predicate predicate, Constant c1, Constant c2,Constant c3) {
		Atom a = Expressions.makeAtom(predicate, c1, c2, c3);
		//Add Atoms
		v_s_Facts.add(a);
	}
	
	public Set<Predicate> getSetNom() {
		return v_s_nomEDB;
	}
	
	public Set<Predicate> getSetCls() {
		return v_s_clsEDB;
	}
	
	public Set<Predicate> getSetRol() {
		return v_s_rolEDB;
	}
	
	public Set<Predicate> getSetsubcls() {
		return v_s_subClassEDB;
	}
	
	public Set<Predicate> getSetTop() {
		return v_s_topEDB;
	}
	
	public Set<Predicate> getSetBot() {
		return v_s_botEDB;
	}
	public Set<Predicate> getSetsubConj() {
		return v_s_subConjEDB;
	}
	
	public Set<Predicate> getSetsubEx() {
		return v_s_subExEDB;
	}
	
	public Set<Predicate> getSetsupEx() {
		return v_s_supExEDB;
	}
	
	public Set<Predicate> getSetsubSelf() {
		return v_s_subSelfEDB;
	}
	
	public Set<Predicate> getSetsupSelf() {
		return v_s_supSelfEDB;
	}
	
	public Set<Predicate> getSetsubRole() {
		return v_s_subRoleEDB;
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
			ClassExpressionVisitorForNormalisedAxiomRight ce_visit = new ClassExpressionVisitorForNormalisedAxiomRight(axiom.getSuperClass(), v_s_normalisedAxioms); 
			axiom.getSuperClass().accept(ce_visit);
		} else {
			ClassExpressionVisitorForNormalisedAxiomLeft ce_visit = new ClassExpressionVisitorForNormalisedAxiomLeft(axiom.getSubClass(), v_s_normalisedAxioms); 
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
		Iterator<OWLNamedIndividual> iter = axiom.individualsInSignature().iterator();
		OWLNamedIndividual[] indi  = new OWLNamedIndividual[2];
		int i = 0;
		while (iter.hasNext()) {
			indi[i] = iter.next();
			i++;
		}
		
		String predicatename = axiom.toString();
		Predicate predicate = Expressions.makePredicate(predicatename, 3);
		
		Constant c1 = Expressions.makeConstant(axiom.getProperty().toString());
		Constant c2 = Expressions.makeConstant(indi[0].toString());
		Constant c3 = Expressions.makeConstant(indi[1].toString());
		
		addNominalsEDB(indi[0].toString());
		addNominalsEDB(indi[1].toString());
		addrolesEDB(axiom.getProperty().toString());
		
		addToSubExEDB(predicate);
		
		addToThreeConstantFacts(predicate, c2, c1, c3);		
	}

	@Override
	public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
		throw new IllegalArgumentException();
	}

	@Override
	public void visit(OWLSubObjectPropertyOfAxiom axiom) {
		String predicatename = axiom.toString();
		Predicate predicate = Expressions.makePredicate(predicatename, 2);
		
		Constant c1 = Expressions.makeConstant(axiom.getSubProperty().toString());
		Constant c2 = Expressions.makeConstant(axiom.getSuperProperty().toString());
		
		addrolesEDB(axiom.getSubProperty().toString());
		addrolesEDB(axiom.getSuperProperty().toString());
		addToSubRoleEDB(predicate);
		
		addToDoubleConstantFacts(predicate, c1, c2);		
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
		String predicatename = axiom.toString();
		Predicate predicate = Expressions.makePredicate(predicatename, 2);
		
		Constant c1 = Expressions.makeConstant(axiom.getClassExpression().toString());
		Constant c2 = Expressions.makeConstant(axiom.getIndividual().toString());
		
		addClassNamesEDB(axiom.getClassExpression().toString());
		addNominalsEDB(axiom.getIndividual().toString());
		addToSubClassEDB(predicate);
		
		addToDoubleConstantFacts(predicate, c1, c2);
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
