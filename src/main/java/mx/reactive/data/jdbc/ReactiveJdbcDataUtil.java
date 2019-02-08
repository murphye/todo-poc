package mx.reactive.data.jdbc;

import mx.reactive.data.ReactiveDataUtil;

public class ReactiveJdbcDataUtil extends ReactiveDataUtil {

    // API is different than PG Client
    // https://vertx.io/docs/apidocs/index.html?io/vertx/rxjava/ext/sql/SQLClient.html

    /*
    static Flowable<?> rowFlowableToPojoFlowable(Flowable<Row> stream, Class pojoClass) {
        return rowFlowableToJsonObjectFlowable(stream)
                .map(jsonObject -> jsonObject.mapTo(pojoClass));
    }

    static Flowable<JsonObject> rowFlowableToJsonObjectFlowable(Flowable<Row> stream) {

        // Use SQLRowStream.toFlowable?

        return stream
                .map(row -> {
                    Map<String, Object> rowMap = new HashMap<String, Object>(row.size());

                    for (int i = 0; i < row.size(); i++) {
                        rowMap.put(toCamelCase(row.getColumnName(i)), row.getValue(i));
                    }

                    return rowMap;
                })
                .map(rowMap -> new JsonObject(rowMap));
    }

    */
}
