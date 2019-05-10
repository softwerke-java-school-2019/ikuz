package ru.softwerke.practice.app2019.query;

import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import ru.softwerke.practice.app2019.model.Entity;
import ru.softwerke.practice.app2019.util.MultiStringParam;
import ru.softwerke.practice.app2019.util.ParseFromStringParam;
import ru.softwerke.practice.app2019.util.QueryUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * The {@code Query} class represents entity filter or order GET-query.
 *
 * @param <T> considered entity type
 */

public class Query<T extends Entity> {
    private static final String COUNT = "pageItems";
    private static final String OFFSET = "offset";
    private static final String PAGE = "page";
    
    private Comparator<T> entityComparator;
    
    static final String ORDER_TYPE = "orderBy";
    
    public static final String ID = "ID";
    
    QueryConditionsHolder<T> holder;
    MultivaluedMap<String, String> queryParams;
    
    /**
     * Entity query constructor
     *
     * Constructs an entity filter query by parsing the {@code UriInfo}. This class presents the general logic of filtering
     * and ordering queries for different entities.
     *
     * Each key parameter is checked for compliance with the filtering option(count, page, offset).
     * {@link Query#queryParams} is {@code MultivaluedMap}, where each key is associated with a list of arguments,
     * cause each key can be used several times in URI query, this API takes the value corresponding
     * to the first appearance of the key in the query, for example in query:
     * <blockquote>http://localhost:8080/api/bill?count=10&customerId=20&count=5</blockquote>
     * only the first value=10 for the key=count will be taken.
     *
     * After parsing common keys(count, page, offset) they are deleted for further parsing of the query in subclasses and
     * defining wrong keys of query.
     *
     * @param uriInfo URI information taken from GET-query.
     * @throws WebApplicationException if it was thrown by the following classes: {@link ParseFromStringParam}
     */
    Query(UriInfo uriInfo) throws WebApplicationException {
        queryParams = new MultivaluedStringMap(uriInfo.getQueryParameters());
        holder = new QueryConditionsHolder<>();
        for (String key : queryParams.keySet()) {
            switch (key) {
                case Query.COUNT: {
                    ParseFromStringParam<Integer> count =
                            new ParseFromStringParam<>(
                                    queryParams.getFirst(key),
                                    Query.COUNT,
                                    ParseFromStringParam.PARSE_INTEGER_FUN,
                                    ParseFromStringParam.POSITIVE_NUMBER_FORMAT
                            );
                    long parsedCountValue = count.getParsedValue();
                    QueryUtils.checkCountValue(parsedCountValue);
                    holder.setCountOfElementsParam(parsedCountValue);
                    break;
                }
                case Query.PAGE: {
                    ParseFromStringParam<Integer> page =
                            new ParseFromStringParam<>(
                                    queryParams.getFirst(key),
                                    Query.PAGE,
                                    ParseFromStringParam.PARSE_INTEGER_FUN,
                                    ParseFromStringParam.POSITIVE_NUMBER_FORMAT
                            );
                    holder.setPageParam(page.getParsedValue());
                    break;
                }
                case Query.OFFSET: {
                    ParseFromStringParam<Integer> offset =
                            new ParseFromStringParam<>(
                                    queryParams.getFirst(key),
                                    Query.OFFSET,
                                    ParseFromStringParam.PARSE_INTEGER_FUN,
                                    ParseFromStringParam.POSITIVE_NUMBER_FORMAT
                            );
                    holder.setOffsetParam(offset.getParsedValue());
                    break;
                }
            }
        }
        if (!queryParams.containsKey(Query.PAGE) && queryParams.containsKey(Query.OFFSET)) {
            holder.changeDisplayToOffsetOption();
        }
        queryParams.remove(Query.COUNT);
        queryParams.remove(Query.PAGE);
        queryParams.remove(Query.OFFSET);
    }
    
    /**
     * Takes the value {@code orderTypesByString} corresponding to the first occurrence of the 'orderBy' key.
     * If the commaparator is not yet set, assigns it the value corresponding to the first element of the sequence
     * of sorting parameters, otherwise it adds a sorting parameter with a lower priority to the comparator.
     *
     * @param orderTypesByString sorting options in a sequence from higher priority to lower
     * @param orderParamsMap a map assigning to each sorting parameter for an entity its comparator
     *
     * @throws WebApplicationException
     *         if it was thrown by the following classes: {@link MultiStringParam} and if one of the sequence parameters
     *         is set incorrectly or it is not in the map {@code orderParamsMap}
     */
    void addOrderType(String orderTypesByString, Map<String, Comparator<T>> orderParamsMap) throws WebApplicationException {
        MultiStringParam orderTypesParam = new MultiStringParam(orderTypesByString, ORDER_TYPE);
        List<String> orderTypes = orderTypesParam.getValueList();
        for (String type : orderTypes) {
            if (orderParamsMap.containsKey(type)) {
                if (entityComparator == null) {
                    entityComparator = orderParamsMap.get(type);
                } else {
                    entityComparator = entityComparator.thenComparing(orderParamsMap.get(type));
                }
            } else {
                Response response = QueryUtils.getResponseWithMessage(
                        Response.Status.BAD_REQUEST,
                        QueryUtils.MALFORMED_PARAMS_TYPE_ERROR,
                        QueryUtils.getMalformedValueParamsMessage(type)
                );
                throw new WebApplicationException(response);
            }
        }
        holder.setQueryComparator(entityComparator);
    }
    
    public QueryConditionsHolder<T> getConditionsHolder() {
        return holder;
    }
}
