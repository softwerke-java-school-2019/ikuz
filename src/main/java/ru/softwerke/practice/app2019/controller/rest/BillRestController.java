package ru.softwerke.practice.app2019.controller.rest;

import ru.softwerke.practice.app2019.model.Bill;
import ru.softwerke.practice.app2019.query.BillQuery;
import ru.softwerke.practice.app2019.query.Query;
import ru.softwerke.practice.app2019.service.EntityService;
import ru.softwerke.practice.app2019.util.IntegerParam;
import ru.softwerke.practice.app2019.util.DateTimeParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/bill")
@Produces("application/json;charset=utf-8")
public class BillRestController extends BaseRestController<Bill> {
    
    @Inject
    public BillRestController(EntityService<Bill> receiptService) {
        this.service = receiptService;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Bill> getBills(@Context UriInfo uriInfo) throws WebApplicationException {
        queryBuilder = new BillQuery.Builder();
        MultivaluedMap<String, String> queryParams = getCommonQueryProperty(uriInfo);
        BillQuery.Builder billQueryBuilder = (BillQuery.Builder) queryBuilder;
        for (String key : queryParams.keySet()) {
            switch (key) {
                case BillQuery.CUSTOMER_ID: {
                    billQueryBuilder.setCustomerId(new IntegerParam(queryParams.getFirst(key), BillQuery.CUSTOMER_ID,
                            Query.FILTER + Bill.getName()));
                    break;
                }
                case BillQuery.PURCHASE_DATE_TIME: {
                    billQueryBuilder.setPurchaseDateTime(
                            new DateTimeParam(queryParams.getFirst(key), BillQuery.PURCHASE_DATE_TIME,
                                    Query.FILTER + Bill.getName()));
                    break;
                }
                case BillQuery.PURCHASE_DATE_TIME_FROM: {
                    billQueryBuilder.setPurchaseDateTimeFrom(
                            new DateTimeParam(queryParams.getFirst(key), BillQuery.PURCHASE_DATE_TIME_FROM,
                                    Query.FILTER + Bill.getName()));
                    break;
                }
                case BillQuery.PURCHASE_DATE_TIME_TO: {
                    billQueryBuilder.setPurchaseDateTimeTo(
                            new DateTimeParam(queryParams.getFirst(key), BillQuery.PURCHASE_DATE_TIME_TO,
                                    Query.FILTER + Bill.getName()));
                    break;
                }
                case BillQuery.TOTAL_PRICE: {
                    billQueryBuilder.setTotalPrice(
                            new IntegerParam(queryParams.getFirst(key), BillQuery.TOTAL_PRICE,
                                    Query.FILTER + Bill.getName()));
                    break;
                }
                case BillQuery.TOTAL_PRICE_FROM: {
                    billQueryBuilder.setTotalPriceFrom(
                            new IntegerParam(queryParams.getFirst(key), BillQuery.TOTAL_PRICE_FROM,
                                    Query.FILTER + Bill.getName()));
                    break;
                }
                case BillQuery.TOTAL_PRICE_TO: {
                    billQueryBuilder.setTotalPriceTo(
                            new IntegerParam(queryParams.getFirst(key), BillQuery.TOTAL_PRICE_TO,
                                    Query.FILTER + Bill.getName()));
                    break;
                }
                case BillQuery.BILL_ITEM_DEVICE_ID: {
                    billQueryBuilder.setBillItemDeviceId(new IntegerParam(queryParams.getFirst(key), BillQuery.BILL_ITEM_DEVICE_ID,
                            Query.FILTER + Bill.getName()));
                    break;
                }
                case BillQuery.BILL_ITEM_PRICE: {
                    billQueryBuilder.setBillItemPrice(new IntegerParam(queryParams.getFirst(key), BillQuery.BILL_ITEM_PRICE,
                            Query.FILTER + Bill.getName()));
                    break;
                }
                case BillQuery.BILL_ITEM_QUANTITY: {
                    billQueryBuilder.setBillItemQuantity(new IntegerParam(queryParams.getFirst(key), BillQuery.BILL_ITEM_QUANTITY,
                            Query.FILTER + Bill.getName()));
                    break;
                }
                case Query.ORDER_TYPE: {
                    addOrderType(queryParams.get(key), BillQuery.getOrderParamsMap(), Bill.getName());
                    break;
                }
                default: {
                    sendWrongParamsMessage(key, Bill.getName());
                }
            }
        }
        billQueryBuilder.setComparator(entityComparator);
        return getEntities(billQueryBuilder.build());
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Bill getBillById(@PathParam("id") String idByString) throws WebApplicationException {
        return getEntityById(idByString, Bill.getName());
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Bill addNewBill(Bill newBill) throws WebApplicationException {
        return addNewEntity(newBill);
    }
}