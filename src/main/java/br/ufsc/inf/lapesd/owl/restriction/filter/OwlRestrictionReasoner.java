package br.ufsc.inf.lapesd.owl.restriction.filter;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class OwlRestrictionReasoner {

    private String ontology;
    private String data;
    private OntModel ontologyModel;
    private OntModel dataModel;
    private InfModel pModel;

    public void init() throws IOException {
        ontologyModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        ontologyModel.read(new StringReader(ontology), null, "N3");

        dataModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        dataModel.read(new StringReader(data), null, "N3");
        ontologyModel.add(dataModel);

        pModel = ModelFactory.createInfModel(PelletReasonerFactory.theInstance().create(), ontologyModel);

    }

    public List<String> listSemanticClasses() {
        List<String> classes = new ArrayList<>();
        ExtendedIterator<OntClass> listClasses = ontologyModel.listClasses();
        while (listClasses.hasNext()) {
            OntClass next = listClasses.next();
            if (next.getURI() != null) {
                classes.add(next.getURI());
            }
        }
        return classes;
    }

    public List<Resource> listAllIndividuals(String semanticClass) {

        List<Resource> individuals = new ArrayList<>();

        ResIterator listSubjects = dataModel.listSubjects();
        while (listSubjects.hasNext()) {
            Resource next = listSubjects.next();
            Resource individual = pModel.getResource(next.getURI());
            StmtIterator listProperties = individual.listProperties();
            while (listProperties.hasNext()) {
                Statement property = listProperties.next();
                Property predicate = property.getPredicate();
                RDFNode object = property.getObject();
                if (predicate.toString().equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type") && object.toString().equals(semanticClass)) {
                    individuals.add(individual);
                }
            }
        }
        return individuals;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setOntology(String ontology) {
        this.ontology = ontology;
    }

}
