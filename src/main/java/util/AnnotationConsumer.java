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
	public AnnotationConsumer() {
		annotationsWithArguments = new HashMap<>();
	}
	public class AnnotationValue {
		public String value;
		public Map<String, String> keyValues;
		public AnnotationValue(String value) {
			this.value = value;
			keyValues = null;
		}
		public AnnotationValue(Map<String, String> keyValues) {
			this.value = null;
			this.keyValues = keyValues;
		}
		
		public boolean hasKeys() {
			return keyValues != null;
		}
	}
	
	@Override
	public void accept(AnnotationExpr annotationExpr) {
		String annotationName = annotationExpr.getNameAsString();
		
		if(annotationExpr instanceof NormalAnnotationExpr) {
			List<MemberValuePair> memberValuePairs = ((NormalAnnotationExpr) annotationExpr).getPairs();
			HashMap keyValueMap = new HashMap();
			for(MemberValuePair memberValuePair : memberValuePairs) {
				String key = memberValuePair.getNameAsString();
				String value = memberValuePair.getValue().toString();
				keyValueMap.put(key, value);
			}
			annotationsWithArguments.put(annotationName, new AnnotationValue(keyValueMap));
		}
		else if (annotationExpr instanceof SingleMemberAnnotationExpr) {
			String value = ((SingleMemberAnnotationExpr) annotationExpr).getMemberValue().toString();
			annotationsWithArguments.put(annotationName, new AnnotationValue(value));
			System.out.println("Hello SingleMemberAnnotationExpr");
			
		}
		else {
			annotationsWithArguments.put(annotationName, null);
		}
	
	}
	
	public Map<String, AnnotationValue> getFoundAnnotations() {
		return annotationsWithArguments;
	}
}

