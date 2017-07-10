import Plugin.LiquidBaseDependencyPlugin;
import Plugin.ParserPlugin;
import Plugin.XSDCheckPlugin;
import Services.PluginManagmentService;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ValidityReport;
import org.apache.jena.reasoner.rulesys.BuiltinRegistry;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDF;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.riot.RDFDataMgr;

/**
 * Created by Frederik on 21.06.2017.
 */
public class Program {
    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<BaseBuiltin> builtins= new ArrayList<BaseBuiltin>();
        try {
            builtins.add(new XSDCheckPlugin());
            builtins.add(new LiquidBaseDependencyPlugin());
            builtins.add(new ParserPlugin());
        }
        catch (ParserConfigurationException e) {
           System.out.println("Error while setting up the plugins: " + e.getMessage());
           System.exit(1);
        }
        for (BaseBuiltin builtin : builtins) {
            BuiltinRegistry.theRegistry.register(builtin);
        }
    
        Model model = FileManager.get().loadModel("data.ttl");
        Model schema = FileManager.get().loadModel("ontology.rdf");
    
        InfModel infmodel = ModelFactory.createRDFSModel(schema, model);
        ValidityReport validity = infmodel.validate();
    
        List<String> plugins = PluginManagmentService.getInstance().getPlugins();
        for(String plugin : plugins) {
            RDFDataMgr.read(infmodel, plugin);
        }
        
        if(validity.isValid()) {
            System.out.println("The model is valid");
        }
        else {
            System.out.println("\nConflicts");
            for (Iterator i = validity.getReports(); i.hasNext(); ) {
                ValidityReport.Report report = (ValidityReport.Report)i.next();
                System.out.println(" - " + report);
            }
        }
        
        Resource test = model.getResource("http://softlang.com/liquidbase");
        System.out.println(test.getProperty(RDF.type));
        System.out.println("\n");
       for (Statement x : infmodel.listStatements().toList())
            System.out.println(x);
       //System.exit(0);
    
        // Apply reasoner with rules.
        Reasoner reasoner = new GenericRuleReasoner(Rule.rulesFromURL("rules.txt"));
        reasoner.setDerivationLogging(true);
        InfModel inf = ModelFactory.createInfModel(reasoner, model);
    
        // Print model after reasoning.
        System.out.println("InfModel:");
        for (Statement x : inf.listStatements().toList())
            System.out.println(x);
    
    
    }
}
