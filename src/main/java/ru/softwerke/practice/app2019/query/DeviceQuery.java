package ru.softwerke.practice.app2019.query;

import ru.softwerke.practice.app2019.model.Device;
import ru.softwerke.practice.app2019.service.DeviceColorService;
import ru.softwerke.practice.app2019.service.DeviceTypeService;
import ru.softwerke.practice.app2019.util.*;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriInfo;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@code DeviceQuery} class represents customer filter or order GET-query.
 */
public class DeviceQuery extends Query<Device> {
    private static Map<String, Comparator<Device>> orderParamsMap = new HashMap<>();
    
    private static final String MODEL = "modelName";
    private static final String MODEL_PREFIX = "modelNamePrefix";
    private static final String TYPE = "deviceType";
    private static final String TYPE_PREFIX = "deviceTypePrefix";
    private static final String COLOR = "colorName";
    private static final String COLOR_RGB = "colorRGB";
    private static final String MANUFACTURE_DATE = "manufactureDate";
    private static final String PRICE = "price";
    private static final String MANUFACTURER = "manufacturer";
    private static final String MANUFACTURER_PREFIX = "manufacturerPrefix";
    private static final String PRICE_FROM = "priceFrom";
    private static final String PRICE_TO = "priceTo";
    private static final String MANUFACTURE_DATE_FROM = "manufactureDateFrom";
    private static final String MANUFACTURE_DATE_TO = "manufactureDateTo";
    
    private static final String ORDER_MANUFACTURE_DATE_TO = "-manufactureDate";
    private static final String ORDER_PRICE_TO = "-price";
    
    static {
        orderParamsMap.put(MODEL, Comparator.comparing(Device::getModelName));
        orderParamsMap.put(TYPE, Comparator.comparing(Device::getDeviceType));
        orderParamsMap.put(MANUFACTURER, Comparator.comparing(Device::getManufacturer));
        orderParamsMap.put(MANUFACTURE_DATE, Comparator.comparing(Device::getManufacturerDate));
        orderParamsMap.put(ORDER_MANUFACTURE_DATE_TO, Comparator.comparing(Device::getManufacturerDate).reversed());
        orderParamsMap.put(PRICE, Comparator.comparing(Device::getPrice));
        orderParamsMap.put(ORDER_PRICE_TO, Comparator.comparing(Device::getPrice).reversed());
    }
    
    /**
     * Device query constructor
     *
     * Constructs a device query by parsing the {@code UriInfo}. After calling the {@link Query(UriInfo)} constructor
     * all remaining unprocessed key-value parameters of device are contained in {@link Query#queryParams}.
     * Each key parameter is checked for compliance with the filtering option if the key does not match
     * any of the filtering options than the method {@link QueryUtils#getWrongParamsMessage(String)} is called.
     * {@link Query#queryParams} is {@code MultivaluedMap}, where each key is associated with a list of arguments, because each key
     * can be used several times, this API takes the value corresponding to the first appearance of the key in the query, for example,
     * in query: <blockquote>http://localhost:8080/api/device?deviceType=smartphone&modelName=A-10&deviceType=laptop</blockquote>
     * only the first value=smartphone for the key=deviceType will be taken
     *
     * @param uriInfo  URI information taken from GET-query.
     * @throws WebApplicationException
     *         if key of {@link Query#queryParams} is none of filtering device option or
     *         if it was thrown by the following classes: {@link ParseFromStringParam}, {@link StringParam} or
     *         the following method {@link Query#addOrderType}}
     */
    public DeviceQuery(UriInfo uriInfo) throws WebApplicationException {
        super(uriInfo);
        DeviceColorService deviceColorService = DeviceColorService.getInstance();
        DeviceTypeService deviceTypeService = DeviceTypeService.getInstance();
        for (String key : queryParams.keySet()) {
            switch (key) {
                case DeviceQuery.MODEL: {
                    StringParam modelName = new StringParam(
                            queryParams.getFirst(key),
                            DeviceQuery.MODEL
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    Device::getModelName,
                                    FilterOperator.EQ,
                                    modelName.getValue()
                            )
                    );
                    break;
                }
                case DeviceQuery.TYPE: {
                    StringParam deviceType = new StringParam(
                            queryParams.getFirst(key),
                            DeviceQuery.TYPE
                    );
                    deviceTypeService.checkDoesDeviceTypeExist(deviceType.getValue());
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    Device::getDeviceType,
                                    FilterOperator.EQ,
                                    deviceType.getValue()
                            )
                    );
                    break;
                }
                case DeviceQuery.MANUFACTURER: {
                    StringParam manufacturer = new StringParam(
                            queryParams.getFirst(key),
                            DeviceQuery.MANUFACTURER
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    Device::getManufacturer,
                                    FilterOperator.EQ,
                                    manufacturer.getValue()
                            )
                    );
                    break;
                }
                case DeviceQuery.MODEL_PREFIX: {
                    StringParam modelNamePrefix = new StringParam(
                            queryParams.getFirst(key),
                            DeviceQuery.MODEL_PREFIX
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    Device::getModelName,
                                    modelNamePrefix.getValue()
                            )
                    );
                    break;
                }
                case DeviceQuery.TYPE_PREFIX: {
                    StringParam deviceTypePrefix = new StringParam(
                            queryParams.getFirst(key),
                            DeviceQuery.TYPE_PREFIX
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    Device::getDeviceType,
                                    deviceTypePrefix.getValue()
                            )
                    );
                    break;
                }
                case DeviceQuery.MANUFACTURER_PREFIX: {
                    StringParam manufacturerPrefix = new StringParam(
                            queryParams.getFirst(key),
                            DeviceQuery.MANUFACTURER_PREFIX
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    Device::getManufacturer,
                                    manufacturerPrefix.getValue()
                            )
                    );
                    break;
                }
                case DeviceQuery.COLOR: {
                    StringParam colorName = new StringParam(
                            queryParams.getFirst(key),
                            DeviceQuery.COLOR
                    );
                    deviceColorService.checkDoesColorNameExist(colorName.getValue());
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    Device::getColorName,
                                    FilterOperator.EQ,
                                    colorName.getValue()
                            )
                    );
                    break;
                }
                case DeviceQuery.COLOR_RGB: {
                    ParseFromStringParam<Integer> colorRGB = new ParseFromStringParam<>(
                            queryParams.getFirst(key),
                            DeviceQuery.COLOR_RGB,
                            ParseFromStringParam.PARSE_INTEGER_FUN,
                            ParseFromStringParam.POSITIVE_NUMBER_FORMAT
                    );
                    deviceColorService.checkDoesColorRGBExist(colorRGB.getParsedValue());
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    Device::getColorRGB,
                                    FilterOperator.EQ,
                                    colorRGB.getParsedValue()
                            )
                    );
                    break;
                }
                case DeviceQuery.MANUFACTURE_DATE: {
                    ParseFromStringParam<LocalDate> manufacturerDate = new ParseFromStringParam<>(
                            queryParams.getFirst(key),
                            DeviceQuery.MANUFACTURE_DATE,
                            ParseFromStringParam.PARSE_DATE_FUN,
                            ParseFromStringParam.DATE_FORMAT
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    Device::getManufacturerDate,
                                    FilterOperator.EQ,
                                    manufacturerDate.getParsedValue()
                            )
                    );
                    break;
                }
                case DeviceQuery.MANUFACTURE_DATE_FROM: {
                    ParseFromStringParam<LocalDate> manufacturerDateFrom = new ParseFromStringParam<>(
                            queryParams.getFirst(key),
                            DeviceQuery.MANUFACTURE_DATE_FROM,
                            ParseFromStringParam.PARSE_DATE_FUN,
                            ParseFromStringParam.DATE_FORMAT
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    Device::getManufacturerDate,
                                    FilterOperator.GREATER_OR_EQ,
                                    manufacturerDateFrom.getParsedValue()
                            )
                    );
                    break;
                }
                case DeviceQuery.MANUFACTURE_DATE_TO: {
                    ParseFromStringParam<LocalDate> manufacturerDateTo = new ParseFromStringParam<>(
                            queryParams.getFirst(key),
                            DeviceQuery.MANUFACTURE_DATE_TO,
                            ParseFromStringParam.PARSE_DATE_FUN,
                            ParseFromStringParam.DATE_FORMAT
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    Device::getManufacturerDate,
                                    FilterOperator.LESS_OR_EQ,
                                    manufacturerDateTo.getParsedValue()
                            )
                    );
                    break;
                }
                case DeviceQuery.PRICE: {
                    ParseFromStringParam<Long> price = new ParseFromStringParam<>(
                            queryParams.getFirst(key),
                            DeviceQuery.PRICE,
                            ParseFromStringParam.PARSE_LONG_FUN,
                            ParseFromStringParam.POSITIVE_NUMBER_FORMAT
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    Device::getPrice,
                                    FilterOperator.EQ,
                                    price.getParsedValue()
                            )
                    );
                    break;
                }
                case DeviceQuery.PRICE_FROM: {
                    ParseFromStringParam<Long> priceFrom = new ParseFromStringParam<>(
                            queryParams.getFirst(key),
                            DeviceQuery.PRICE_FROM,
                            ParseFromStringParam.PARSE_LONG_FUN,
                            ParseFromStringParam.POSITIVE_NUMBER_FORMAT
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    Device::getPrice,
                                    FilterOperator.GREATER_OR_EQ,
                                    priceFrom.getParsedValue()
                            )
                    );
                    break;
                }
                case DeviceQuery.PRICE_TO: {
                    ParseFromStringParam<Long> priceTo = new ParseFromStringParam<>(
                            queryParams.getFirst(key),
                            DeviceQuery.PRICE_TO,
                            ParseFromStringParam.PARSE_LONG_FUN,
                            ParseFromStringParam.POSITIVE_NUMBER_FORMAT
                    );
                    holder.addFilterCondition(
                            new QueryCondition<>(
                                    Device::getPrice,
                                    FilterOperator.LESS_OR_EQ,
                                    priceTo.getParsedValue()
                            )
                    );
                    break;
                }
                case Query.ORDER_TYPE: {
                    addOrderType(queryParams.getFirst(key), DeviceQuery.orderParamsMap);
                    break;
                }
                default: {
                    QueryUtils.getWrongParamsMessage(key);
                }
            }
        }
    }
}
