package mx.reactive.data;

import io.vertx.core.json.JsonObject;
import todo.ImmutableTodo;
import todo.Todo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReactiveDataUtil {

    public static String toCamelCase(String snakeCase) {
        Matcher m = Pattern.compile("([_][a-z])").matcher(snakeCase.toLowerCase());
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, m.group().substring(1).toUpperCase());
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public static String toSnakeCase(String camelCase) {
        return camelCase.replaceAll("(.)(\\p{Upper})", "$1_$2").toLowerCase();
    }

    public static String insertColumnsFromJsonObject(JsonObject jsonObject) {
        String columnsStr = "";
        Set<String> fields = jsonObject.getMap().keySet();
        for(String field : fields) {
            String columnName = toSnakeCase(field);
            columnsStr += "\"" + columnName + "\", ";
        }
        return " (" + columnsStr.substring(0, columnsStr.length()-2) + ") ";
    }

    public static String insertValuesFromJsonObject(JsonObject jsonObject) {
        String valuesStr = "";
        Collection<Object> values = jsonObject.getMap().values();
        for(Object value : values) {
            String valueStr = value.toString().replace("'","''"); // Escape single quote for SQL
            valuesStr += "'" + valueStr + "', ";
        }
        return " (" + valuesStr.substring(0, valuesStr.length()-2) + ") ";
    }

    public static String updateValuesFromJsonObject(JsonObject jsonObject) {
        String updateStr = "";
        ArrayList<String> fields = new ArrayList(jsonObject.getMap().keySet());
        ArrayList<Object> values = new ArrayList(jsonObject.getMap().values());
        for(int i = 0; i < fields.size(); i++) {
            String columnName = toSnakeCase(fields.get(i));
            String valueStr = values.get(i).toString().replace("'","''"); // Escape single quote for SQL
            updateStr += "\"" + columnName + "\" = '" + valueStr + "', ";
        }
        return updateStr.substring(0, updateStr.length()-2);
    }

    public static void main(String[] args) {
        Todo todo = ImmutableTodo.builder().id(UUID.randomUUID()).title("Some Todo").order(0).completed(false).build();
        JsonObject jsonObject = JsonObject.mapFrom(todo);

        System.out.println(insertColumnsFromJsonObject(jsonObject));
        System.out.println(insertValuesFromJsonObject(jsonObject));
        System.out.println(updateValuesFromJsonObject(jsonObject));
    }
}
