package util;

import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by freddy on 21.10.17.
 */
public class AnnotationConsumer implements Consumer<AnnotationExpr> {
	private Map<String, AnnotationValue> annotationsWithArguments;
	private int endStatementLine;
	
	public AnnotationConsumer() {
		annotationsWithArguments = new HashMap<>();
	}
	
	public class AnnotationValue {
		public String value;
		public Map<String, String> keyValues;
		public int[] lineRange = new int[2];
		
		public AnnotationValue() {
			this.value = null;
			keyValues = null;
		}
		public AnnotationValue(int startLine, int endLine) {
			this.value = null;
			keyValues = null;
			lineRange[0] = startLine;
			lineRange[1] = endLine;
		}
		public AnnotationValue(String value) {
			this.value = value;
			keyValues = null;
		}
		public AnnotationValue(Map<String, String> keyValues) {
			this.value = null;
			this.keyValues = keyValues;
		}
		public AnnotationValue(String value, int startLine, int endLine) {
			this.value = value;
			keyValues = null;
			lineRange[0] = startLine;
			lineRange[1] = endLine;
		}
		public AnnotationValue(Map<String, String> keyValues, int startLine, int endLine) {
			this.value = null;
			this.keyValues = keyValues;
			lineRange[0] = startLine;
			lineRange[1] = endLine;
		}
		public boolean hasKeys() {
			return keyValues != null;
		}
		public boolean isEmpty() {
			return keyValues == null && value == null;
		}
		public int[] getRange() {
			return lineRange;
		}
		
		public int getStartLine() {
			return lineRange[0];
		}
		public int getEndLine() {
			return lineRange[1];
		}
		
	}
	
	@Override
	public void accept(AnnotationExpr annotationExpr) {
		String annotationName = annotationExpr.getNameAsString();
		int startLine = annotationExpr.getRange().get().begin.line;
		int endLine = (this.endStatementLine == 0) ? annotationExpr.getRange().get().end.line : this.endStatementLine;
		
		if(annotationExpr instanceof NormalAnnotationExpr) {
			List<MemberValuePair> memberValuePairs = ((NormalAnnotationExpr) annotationExpr).getPairs();
			HashMap keyValueMap = new HashMap();
			
			for(MemberValuePair memberValuePair : memberValuePairs) {
				String key = memberValuePair.getNameAsString();
				String value = memberValuePair.getValue().toString();
				keyValueMap.put(key, value);
			}
			annotationsWithArguments.put(annotationName, new AnnotationValue(keyValueMap, startLine, endLine));
		}
		else if (annotationExpr instanceof SingleMemberAnnotationExpr) {
			String value = ((SingleMemberAnnotationExpr) annotationExpr).getMemberValue().toString();
			annotationsWithArguments.put(annotationName, new AnnotationValue(value, startLine, endLine));
		}
		else {
			annotationsWithArguments.put(annotationName, new AnnotationValue(startLine, endLine));
		}
	
	}
	public void setEndStatement(int endStatement) {
		this.endStatementLine = endStatement;
	}
	public Map<String, AnnotationValue> getFoundAnnotations() {
		return annotationsWithArguments;
	}
}

