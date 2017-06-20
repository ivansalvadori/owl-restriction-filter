package br.ufsc.inf.lapesd.owl.restriction.filter.test;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.hp.hpl.jena.rdf.model.Resource;

import br.ufsc.inf.lapesd.owl.restriction.filter.OwlRestrictionReasoner;

public class filterTest {
    
    OwlRestrictionReasoner filter = new OwlRestrictionReasoner();

   
    @Before
    public void configure() throws IOException {
        String ontology = IOUtils.toString(this.getClass().getResourceAsStream("/ontology.owl"), "UTF-8");
        filter.setOntology(ontology);
        
        String data = IOUtils.toString(this.getClass().getResourceAsStream("/data.rdf"), "UTF-8");
        filter.setData(data);
        
        filter.init();
    }

    @Test
    public void mustListAllClasses(){
        List<String> listSemanticClasses = filter.listSemanticClasses();
        for (String string : listSemanticClasses) {
            System.out.println(string);
        }        
    }
    
    @Test
    public void mustListAllIndividualsOfPerson(){
        List<Resource> person = filter.listAllIndividuals("http://api.com#Person");  
        for (Resource resource : person) {
            System.out.println(resource);
        }
    }
    
    @Test
    public void mustListAllIndividualsOfBrasileirosAssasinados(){
        List<Resource> person = filter.listAllIndividuals("http://api.com#BrasileiroAssassinado");  
        for (Resource resource : person) {
            System.out.println(resource);
        }
    }
}
