package Services;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import util.AnnotationConsumer;
import util.DeclarationConsumer;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Consumer;
import java.security.MessageDigest;

/**
 * Created by freddy on 21.10.17.
 */
public class JavaService {
	private Map<String, CompilationUnit> cuCache;
	
	public JavaService() {
		cuCache = new WeakHashMap();
	}
	
	private String getHash(String input) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.update(input.getBytes());
		String hashedInput = messageDigest.digest().toString();
		return hashedInput;
	}
	
	private CompilationUnit getCompilationUnit(String content) {
		CompilationUnit resultCu;
		try {
			String hashedContent = getHash(content);
			if(cuCache.containsKey(hashedContent)) {
				resultCu = cuCache.get(hashedContent);
			}
			else {
				resultCu = JavaParser.parse( content );
				cuCache.put(hashedContent, resultCu);
			}
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			resultCu =  JavaParser.parse( content );
		}
		return resultCu;
	}
	
	
	public String getJavaClass(String content, String path) {
		CompilationUnit cu = JavaParser.parse( content );
		if(cu == null) {
			return "";
		}
		String packageName = cu.getPackageDeclaration().orElse(new PackageDeclaration()).getNameAsString();
		String className = path.substring(path.lastIndexOf("/") + 1, path.length() - 5);
		
		return packageName + (packageName.isEmpty() ? "" : "." ) + className;
	}
	
	public List<String> getJavaImportedElements(String content) {
		ArrayList<String> importedElements = new ArrayList<>();
		
		CompilationUnit cu = getCompilationUnit( content );
		com.github.javaparser.ast.NodeList<ImportDeclaration> imports = cu.getImports();
		PackageDeclaration packageDeclaration  = cu.getPackageDeclaration().orElse(null);
		if(packageDeclaration != null) {
			importedElements.add(packageDeclaration.getNameAsString());
		}
		for(int i = 0; i < imports.size(); i++) {
			String importName = imports.get(i).getNameAsString();
			importedElements.add(importName);
		}
		return importedElements;
	}
	
	public Set<String> getDeclaredClasses(String content, String className) {
		
		Set<String> classDeclarations = new HashSet<>();
		
		if(content == null || className == null) return classDeclarations;
		
		CompilationUnit compilationUnit = getCompilationUnit(content);
		ClassOrInterfaceDeclaration mainClass = compilationUnit.getClassByName(className).orElse(null);
		if(mainClass == null) return classDeclarations;
		
		classDeclarations.addAll(getExtendedClassTypes(mainClass));
		
		
		List<BodyDeclaration<?>>  members = mainClass.getMembers();
		for (BodyDeclaration member : members) {
			if(member instanceof FieldDeclaration) {
				FieldDeclaration field = (FieldDeclaration) member;
				String type = field.getElementType().toString();
				com.github.javaparser.ast.NodeList<VariableDeclarator> variableDeclaration =  field.getVariables();
				classDeclarations.add(type);
			}
		}
		DeclarationConsumer declarationConsumer = new DeclarationConsumer();
		
		// Search for declarations in methods
		List<MethodDeclaration> methods = mainClass.getMethods();
		for(MethodDeclaration method : methods) {
			classDeclarations.add(method.getType().toString());
			
			method.getParameters().forEach(declarationConsumer);
			
			BlockStmt blockStmt = method.getBody().orElseGet(null);
			blockStmt.getStatements().forEach(declarationConsumer);
		}
		
		 classDeclarations.addAll(declarationConsumer.getClassDeclaration());
		return classDeclarations;
	}
	
	
	private Set<String> getExtendedClassTypes(ClassOrInterfaceDeclaration mainClass) {
		HashSet<String> resultingTypes = new HashSet<>();
		
		NodeList<ClassOrInterfaceType> extendedTypes = mainClass.getExtendedTypes();
		for(ClassOrInterfaceType extendedType : extendedTypes) {
			resultingTypes.add(extendedType.getName().asString());
			
			NodeList<Type> arguments = extendedType.getTypeArguments().orElse(null);
			if(arguments == null) {
				continue;
			}
			for(Type type  : arguments) {
				resultingTypes.add(type.asString());
			}
		}
		return resultingTypes;
	}
	
	
	public List<String> getMethodCalls(String content, String className) {
		ArrayList<String> methodCalls = new ArrayList();
		CompilationUnit compilationUnit = getCompilationUnit(content);
		ClassOrInterfaceDeclaration mainClass = compilationUnit.getClassByName(className).orElseThrow(null);
		if(mainClass == null) return methodCalls;
		for(MethodDeclaration method : mainClass.getMethods()) {
			method.accept(new VoidVisitorAdapter<Void>(){
				
				              public void visit (MethodCallExpr n, Void arg) {
					              // Found a method call
					              methodCalls.add(n.getNameAsString());
					              super.visit(n, arg);
				              }
			              }
					, null);
			
		}
		return methodCalls;
	}
	
	public Map<String, AnnotationConsumer.AnnotationValue> getAnnotations(String content, String className) {
		AnnotationConsumer annotationConsumer = calculateAnnotations(content, className);
		
		return annotationConsumer.getFoundAnnotations();
	}
	
	public AnnotationConsumer.AnnotationValue getAnnotationByName(String content, String className, String name) {
		AnnotationConsumer annotationConsumer = calculateAnnotations(content, className);
		
		Map<String, AnnotationConsumer.AnnotationValue> annotations = annotationConsumer.getFoundAnnotations();
		return annotations.get(name);

	}
	
	public String getAnnotationByNameAndKey(String content, String className, String name, String key) {
		AnnotationConsumer annotationConsumer = calculateAnnotations(content, className);
		
		Map<String, AnnotationConsumer.AnnotationValue> annotations = annotationConsumer.getFoundAnnotations();
		AnnotationConsumer.AnnotationValue result =  annotations.get(name);
		System.out.println(result.keyValues.toString());
		if(result.hasKeys()) {
			return result.keyValues.get(key);
		}
		return null;
	}
	
	private AnnotationConsumer calculateAnnotations(String content, String className) {
		CompilationUnit compilationUnit = getCompilationUnit(content);
		AnnotationConsumer annotationConsumer = new AnnotationConsumer();
		
		ClassOrInterfaceDeclaration mainClass = compilationUnit.getClassByName(className).orElseThrow(null);
		int endPosition = mainClass.getRange().get().begin.line;
		annotationConsumer.setEndStatement(endPosition);
		mainClass.getAnnotations().forEach(annotationConsumer);
		com.github.javaparser.ast.NodeList<BodyDeclaration<?>> members = mainClass.getMembers();
		for(BodyDeclaration bodyDeclaration : members) {
			endPosition = bodyDeclaration.getRange().get().begin.line;
			annotationConsumer.setEndStatement(endPosition);
			bodyDeclaration.getAnnotations().forEach(annotationConsumer);
		}
		return annotationConsumer;
	}
	
}
