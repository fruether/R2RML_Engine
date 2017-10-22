package util;

import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
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
				classDeclarations.add(parameter.getType().toString());
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
					classDeclarations.add(type);
				}
			}
		
		}
	};
	
	public Set<String> getClassDeclaration() {
		return classDeclarations;
	}
}
