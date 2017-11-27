package util;

import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithBody;
import com.github.javaparser.ast.nodeTypes.NodeWithCondition;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created by freddy on 22.10.17.
 */
public class DeclarationConsumer implements Consumer<Object> {
	
	private ParameterConsumer parameterConsumer;
	private StatementConsumer statementConsumer;
	private Set<String> classDeclarations;
	public DeclarationConsumer() {
		parameterConsumer = new ParameterConsumer();
		statementConsumer = new StatementConsumer();
		classDeclarations = new HashSet<>();
	}
	
	@Override
	public void accept(Object o) {
		if(o instanceof Statement) {
			Statement statement = (Statement) o;
			statementConsumer.accept(statement);
		}
		else if (o instanceof Parameter) {
			Parameter parameter = (Parameter) o;
			parameterConsumer.accept(parameter);
		}
	}
	
	private class ParameterConsumer implements Consumer<Parameter> {
		@Override
		public void accept(Parameter parameter) {
			if(parameter != null) {
				cleanTemplateTypes(parameter.getType().toString());
			}
		}
	};
	private class StatementConsumer implements Consumer<Statement> {
		@Override
		public void accept(Statement statement) {
			if(statement instanceof ExpressionStmt) {
				ExpressionStmt expressionStmt = (ExpressionStmt) statement;
				Expression expression = expressionStmt.getExpression();
				if(expression instanceof VariableDeclarationExpr) {
					VariableDeclarationExpr variableDeclaration = (VariableDeclarationExpr) expression;
					String type = variableDeclaration.getElementType().asString();
					cleanTemplateTypes(type);
				}
			}
			else if(statement instanceof NodeWithBody) {
				NodeWithBody nodeWithBody = (NodeWithBody) statement;
				Statement bodyStatement = nodeWithBody.getBody();
				if(bodyStatement instanceof BlockStmt) {
					((BlockStmt)bodyStatement).getStatements().forEach(this);
				}
			}
			else if(statement instanceof IfStmt) {
				IfStmt ifStmt = (IfStmt) statement;
				accept(ifStmt.getThenStmt());
				if(ifStmt.hasElseBlock()) {
					accept(ifStmt.getElseStmt().get());
				}
			}
		
		}
		
	};
	
	private void cleanTemplateTypes(String expression) {
		if(expression.contains("<")) {
			
			for(String value : expression.replace(">", "").split("<")) {
				classDeclarations.add(value);
			}
		}
		else {
			classDeclarations.add(expression);
		}
	}
	public Set<String> getClassDeclaration() {
		return classDeclarations;
	}
}
