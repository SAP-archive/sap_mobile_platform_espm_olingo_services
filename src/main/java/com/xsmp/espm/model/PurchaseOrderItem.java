package com.xsmp.espm.model;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ESPM_PURCHASE_ORDER_ITEM")
public class PurchaseOrderItem {
	private static final BigDecimal TAX_AMOUNT_FACTOR = new BigDecimal("0.19");
	private static final BigDecimal GROSS_AMOUNT_FACTOR = new BigDecimal("1.19");

	@EmbeddedId
	private PurchaseOrderItemId purchaseOrderItemId;

	@Column(name = "PRODUCT_ID", length = 10)
	private String productId;

	@Column(name = "CURRENCY_CODE", length = 5)
	private String currencyCode = "EUR";

	@Column(name = "GROSS_AMOUNT", precision = 15, scale = 3)
	private BigDecimal grossAmount;

	@Column(name = "NET_AMOUNT", precision = 15, scale = 3)
	private BigDecimal netAmount;

	@Column(name = "TAX_AMOUNT", precision = 15, scale = 3)
	private BigDecimal taxAmount;

	@Column(precision = 13, scale = 3)
	private BigDecimal quantity;

	@Column(name = "QUANTITY_UNIT", length = 3)
	private String quantityUnit = "EA";

	@OneToOne
	private Product product;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "PURCHASE_ORDER_ITEM_ID", referencedColumnName = "PURCHASE_ORDER_ID", insertable = false, updatable = false)
	private PurchaseOrderHeader purchaseOrderHeader;

	public PurchaseOrderItem() {
		this.purchaseOrderItemId = new PurchaseOrderItemId();
	}

	public PurchaseOrderItem(String purchaseOrderId, int itemNumber) {
		this.purchaseOrderItemId = new PurchaseOrderItemId(purchaseOrderId,
				itemNumber);
	}

	public PurchaseOrderItemId getPurchaseOrderItemId() {
		return purchaseOrderItemId;
	}

	public void setPurchaseOrderItemId(
			final PurchaseOrderItemId purchaseOrderItemId) {
		this.purchaseOrderItemId = purchaseOrderItemId;
	}

	public String getPurchaseOrderId() {
		return this.purchaseOrderItemId.getPurchaseOrderId();
	}

	public void setPurchaseOrderId(String purchaseOrderId) {
		this.purchaseOrderItemId.setPurchaseOrderId(purchaseOrderId);
	}

	public int getItemNumber() {
		return this.purchaseOrderItemId.getItemNumber();
	}

	public void setItemNumber(int itemNumber) {
		this.purchaseOrderItemId.setItemNumber(itemNumber);
	}

	public void setProductId(String param) {
		this.productId = param;
	}

	public String getProductId() {
		return productId;
	}

	public void setCurrencyCode(String param) {
		this.currencyCode = param;
	}

	public String getCurrencyCode() {
		return this.currencyCode;
	}

	public void setGrossAmount(BigDecimal param) {
		this.grossAmount = param;
	}

	public BigDecimal getGrossAmount() {
		return this.grossAmount;
	}

	public void setNetAmount(BigDecimal param) {
		this.netAmount = param;
	}

	public BigDecimal getNetAmount() {
		return this.netAmount;
	}

	public void setTaxAmount(BigDecimal param) {
		this.taxAmount = param;
	}

	public BigDecimal getTaxAmount() {
		return this.taxAmount;
	}

	public void setQuantity(BigDecimal param) {
		this.quantity = param;
	}

	public BigDecimal getQuantity() {
		return this.quantity;
	}

	public void setQuantityUnit(String param) {
		this.quantityUnit = param;
	}

	public String getQuantityUnit() {
		return this.quantityUnit;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product param) {
		this.product = param;
	}

	public PurchaseOrderHeader getPurchaseOrderHeader() {
		return purchaseOrderHeader;
	}

	public void setPurchaseOrderHeader(PurchaseOrderHeader param) {
		this.purchaseOrderHeader = param;
	}

	void persist() {
		BigDecimal price = Product.getPrice(this.getProductId());
		if (price == null) {
			return;
		}
		this.netAmount = price.multiply(this.getQuantity());
		this.taxAmount = this.netAmount.multiply(TAX_AMOUNT_FACTOR);
		this.grossAmount = this.netAmount.multiply(GROSS_AMOUNT_FACTOR);
	}

}