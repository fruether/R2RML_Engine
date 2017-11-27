package util;

import nl.bigo.sqliteparser.MySqlParser;
import nl.bigo.sqliteparser.MySqlParserBaseListener;

import java.util.List;

/**
 * Created by freddy on 02.11.17.
 */
public class TableNameListener extends MySqlParserBaseListener {
	private String foundTable;
	
	public TableNameListener() {
		foundTable = "";
	}
	
	@Override public void enterQueryCreateTable(MySqlParser.QueryCreateTableContext ctx) {
		List<MySqlParser.Id_Context> tableId = ctx.table_name().id_();
		String tableNameValue  = cleanTableName(tableId.get(tableId.size() - 1).getText());
		foundTable = tableNameValue;
	}
	@Override public void enterColCreateTable(MySqlParser.ColCreateTableContext ctx) {
		List<MySqlParser.Id_Context> tableId = ctx.table_name().id_();
		String tableNameValue  = cleanTableName(tableId.get(tableId.size() - 1).getText());
		foundTable = tableNameValue;
	}
	
	private String cleanTableName(String name) {
		return name.replace("`", "");
	}
	public String getTableName() {
		return foundTable;
	}
	
}
