package com.viettridao.cafe.service;

import java.util.List;

import com.viettridao.cafe.dto.request.invoices.InvoiceItemListRequest;
import com.viettridao.cafe.dto.response.invoices.InvoiceItemResponse;

public interface InvoiceItemService {

	List<InvoiceItemResponse> addItemsToInvoice(InvoiceItemListRequest request);

}
