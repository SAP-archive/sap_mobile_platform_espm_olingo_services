package com.xsmp.espm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PurchaseOrderItemId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "PURCHASE_ORDER_ITEM_ID", length = 10)
	private String purchaseOrderId;

	@Column(name = "ITEM_NUMBER")
	private int itemNumber;

	public PurchaseOrderItemId() {
	}

	public PurchaseOrderItemId(String purchaseOrderId, int itemNumber) {
		this.purchaseOrderId = purchaseOrderId;
		this.itemNumber = itemNumber;
	}

	public String getPurchaseOrderId() {
		return this.purchaseOrderId;
	}

	public void setPurchaseOrderId(String purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}

	public int getItemNumber() {
		return this.itemNumber;
	}

	public void setItemNumber(int itemNumber) {
		this.itemNumber = itemNumber;
	}

	@Override
	public int hashCode() {
		return purchaseOrderId.hashCode() + itemNumber;
	}

	@Override
	public boolean equals(Object obj) {
		return ((obj instanceof PurchaseOrderItemId)
				&& this.purchaseOrderId.equals(((PurchaseOrderItemId) obj)
						.getPurchaseOrderId()) && (this.itemNumber == ((PurchaseOrderItemId) obj)
					.getItemNumber()));
	}

}
