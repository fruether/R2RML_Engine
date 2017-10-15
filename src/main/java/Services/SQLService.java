package Services;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import nl.bigo.sqliteparser.MySqlLexer;
import nl.bigo.sqliteparser.MySqlParser;
import nl.bigo.sqliteparser.MySqlParserBaseListener;
import nl.bigo.sqliteparser.SQLiteLexer;
import nl.bigo.sqliteparser.SQLiteParser;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by freddy on 15.10.17.
 */
public class SQLService implements ANTLRErrorListener {
	private boolean wellFormedSQL;
	
	public boolean parseSQL(String content) {
		String upperCase = content.toUpperCase();
		boolean result = parseSQLNormal(upperCase) || parseMySQL(upperCase) || parseSQLite(upperCase);
		return result;
	}
	
	private boolean parseSQLNormal(String content) {
		boolean result;
		try {
			CCJSqlParserUtil.parseStatements(content);
			result = true;
		}
		catch (JSQLParserException e) {
			return false;
		}
		return result;
	}
	private boolean parseMySQL(String content) {
		try {
			wellFormedSQL = true;
			MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(content));
			MySqlParser parser = new MySqlParser(new CommonTokenStream(lexer));
			parser.addErrorListener(this);
			parser.root();
		}
		catch (Throwable error) {
			return wellFormedSQL;
		}
		return wellFormedSQL;
	}
	
	private boolean parseSQLite(String content) {
		try {
			wellFormedSQL = true;
			SQLiteLexer lexer = new SQLiteLexer(CharStreams.fromString(content));
			SQLiteParser parser = new SQLiteParser(new CommonTokenStream(lexer));
			parser.addErrorListener(this);
			parser.parse();
		}
		catch (Throwable error) {
			return wellFormedSQL;
		}
		return wellFormedSQL;
	}
	
	
	public Set<String> mysql_get_tables(String content) {
		Set<String> foundTables = new HashSet<>();
		
		MySqlLexer lexer = new MySqlLexer(CharStreams.fromString(content.toUpperCase()));
		MySqlParser parser = new MySqlParser(new CommonTokenStream(lexer));
		
		ParseTree tree = parser.root();
		
		
		ParseTreeWalker.DEFAULT.walk(new MySqlParserBaseListener(){
			@Override public void enterQueryCreateTable(MySqlParser.QueryCreateTableContext ctx) {
				List<MySqlParser.Id_Context> tableId = ctx.table_name().id_();
				String tableNameValue  = tableId.get(tableId.size() - 1).getText();
				foundTables.add(tableNameValue);
			}
			@Override public void enterColCreateTable(MySqlParser.ColCreateTableContext ctx) {
				List<MySqlParser.Id_Context> tableId = ctx.table_name().id_();
				String tableNameValue  = tableId.get(tableId.size() - 1).getText();
				foundTables.add(tableNameValue);
			}
			
		}, tree);
		
		return foundTables;
	}
	
	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object o, int i, int i1, String s,
			RecognitionException e) {
			wellFormedSQL = false;
	}
	
	@Override
	public void reportAmbiguity(Parser parser, DFA dfa, int i, int i1, boolean b, BitSet bitSet,
			ATNConfigSet atnConfigSet) {
		
		
	}
	
	@Override
	public void reportAttemptingFullContext(Parser parser, DFA dfa, int i, int i1, BitSet bitSet,
			ATNConfigSet atnConfigSet) {
		
	}
	
	@Override
	public void reportContextSensitivity(Parser parser, DFA dfa, int i, int i1, int i2,
			ATNConfigSet atnConfigSet) {
		
	}
	
}
