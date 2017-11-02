import Plugin.ClassLiteral;
import Plugin.GetTableLiteral;
import Plugin.HibernateSpecific.HibernateAnnotationDetection;
import Plugin.HibernateSpecific.HibernateGetMappingType;
import Plugin.JavaSpecific.CheckCall;
import Plugin.JavaSpecific.CheckClassReference;
import Plugin.JavaSpecific.CheckImport;
import Plugin.CheckReferences;
import Plugin.DTDCheckPlugin;
import Plugin.FileEndingPlugin;
import Plugin.HibernateSpecific.HibernateMappingAnalysis;
import Plugin.HibernateSpecific.HibernateRoleIdentification;
import Plugin.HibernateSpecific.HibernateMappingGetTable;
import Plugin.LiquidBaseDependencyPlugin;
import Plugin.HibernateSpecific.HibernateDependency;
import Plugin.NoXSDMatch;
import Plugin.ParserPlugin;
import Plugin.JavaSpecific.RetrieveClass;
import Plugin.RetrieveTableURI;
import Plugin.SQLSpecific.CreateStmtExtraction;
import Plugin.XSDCheckPlugin;
import Services.InputManagementService;
import Services.PluginManagmentService;
import Services.ServiceExtensions.APIDetectionExtension;
import Services.ServiceExtensions.ArtifactDetectionExtension;
import Services.ServiceExtensions.BuildReleaseExtension;
import Services.ServiceExtensions.MethodExtractionExtension;
import Services.ServiceExtensions.PackageDependencyExtension;
import Services.ServiceExtensions.PartOfDetectionExtension;
import Services.ServiceExtensions.PrefixCreationExtension;
import Services.ServiceExtensions.PreludeExtension;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
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
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Frederik on 21.06.2017.
 */
public class Program {
    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<BaseBuiltin> builtins= new ArrayList<BaseBuiltin>();
        String inputFile = "mrsData.ttl";
        System.setOut(new PrintStream(new FileOutputStream("/Users/freddy/Desktop/log_file.txt")));
    
    
        try {
            addPlugins(builtins);
        }
        catch (ParserConfigurationException e) {
           System.out.println("Error while setting up the plugins: " + e.getMessage());
           System.exit(1);
        }
        for (BaseBuiltin builtin : builtins) {
            BuiltinRegistry.theRegistry.register(builtin);
        }
    
        InputManagementService inputManagementService = new InputManagementService("input/", inputFile);
        managePlugins(inputManagementService);
       // Model model = FileManager.get().loadModel("data.ttl");
    
        Model model = inputManagementService.getModel();
        Model schema = FileManager.get().loadModel("ontology.rdf");
    
        InfModel infmodel = ModelFactory.createRDFSModel(schema, model);
        
        PluginManagmentService.getInstance().addPluginsInfModel(infmodel);
        
        ValidityReport validity = infmodel.validate();

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
       /*for (Statement x : infmodel.listStatements().toList())
            System.out.println(x);
       //System.exit(0);
        */
        // Apply reasoner with rules.
        Reasoner reasoner = new GenericRuleReasoner(Rule.rulesFromURL("rules.txt"));
        reasoner.setDerivationLogging(true);
        InfModel inf = ModelFactory.createInfModel(reasoner, model);
        // Print model after reasoning.
        inf.write(System.out, "N-TRIPLES");
        /*  for (Statement x : inf.listStatements().toList())
            System.out.println(x);*/
    
    }
    static void managePlugins(InputManagementService inputManagementService) {
        PluginManagmentService pluginManagmentService = PluginManagmentService.getInstance();
        
        pluginManagmentService.addExtension(new PreludeExtension(), new PrefixCreationExtension(), new BuildReleaseExtension(),
                new ArtifactDetectionExtension(), new PartOfDetectionExtension());
        
        
        pluginManagmentService.copyExtensions(inputManagementService);
        
        pluginManagmentService.addExtension(new APIDetectionExtension());
        pluginManagmentService.addExtension(new MethodExtractionExtension());
        pluginManagmentService.addExtension(new PackageDependencyExtension());
        
        pluginManagmentService.createPluginsOntology();
        inputManagementService.createInputFile();
    
    }
    
    static void addPlugins(ArrayList<BaseBuiltin> builtins) throws ParserConfigurationException {
        
        builtins.add(new XSDCheckPlugin());
        builtins.add(new LiquidBaseDependencyPlugin());
        builtins.add(new ParserPlugin());
        builtins.add(new NoXSDMatch());
        builtins.add(new DTDCheckPlugin());
        builtins.add(new FileEndingPlugin());
        builtins.add(new CheckReferences());
        builtins.add(new HibernateDependency());
        builtins.add(new HibernateRoleIdentification());
        builtins.add(new RetrieveClass());
        builtins.add(new HibernateMappingAnalysis());
        builtins.add(new CheckImport());
        builtins.add(new CheckCall());
        builtins.add(new HibernateGetMappingType());
        builtins.add(new ClassLiteral());
        builtins.add(new CheckClassReference());
        builtins.add(new RetrieveTableURI());
        builtins.add(new HibernateMappingGetTable());
        builtins.add(new GetTableLiteral());
        builtins.add(new HibernateAnnotationDetection());
        builtins.add(new CreateStmtExtraction());
    
    }

}

