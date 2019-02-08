package mx.reactive.data.pgclient;

import mx.reactive.data.ReactiveDataUtil;
import io.reactiverse.reactivex.pgclient.Row;
import io.reactivex.Flowable;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

class ReactivePgDataUtil extends ReactiveDataUtil {

    static Flowable<?> rowFlowableToPojoFlowable(Flowable<Row> stream, Class pojoClass) {
        return rowFlowableToJsonObjectFlowable(stream)
                .map(jsonObject -> jsonObject.mapTo(pojoClass));
    }

    static Flowable<JsonObject> rowFlowableToJsonObjectFlowable(Flowable<Row> stream) {
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
}