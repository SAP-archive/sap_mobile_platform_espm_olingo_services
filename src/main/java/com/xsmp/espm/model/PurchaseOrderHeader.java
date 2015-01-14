package com.xsmp.espm.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


@Entity
@Table(name = "ESPM_PURCHASE_ORDER_HEADER")
public class PurchaseOrderHeader {

	/* Purchase order ids are generated within a number range starting with 3 */
	@TableGenerator(name = "PurchaseOrderGenerator", table = "ESPM_ID_GENERATOR", pkColumnName = "GENERATOR_NAME", valueColumnName = "GENERATOR_VALUE", pkColumnValue = "PurchaseOrder", initialValue = 300000000, allocationSize = 100)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "PurchaseOrderGenerator")
	@Column(name = "PURCHASE_ORDER_ID", length = 10)
	private String purchaseOrderId;

	@Column(name = "SUPPLIER_ID", length = 10)
	private String supplierId;

	@Column(name = "CURRENCY_CODE", length = 5)
	private String currencyCode = "EUR";

	@Column(name = "GROSS_AMOUNT", precision = 15, scale = 3)
	private BigDecimal grossAmount;

	@Column(name = "NET_AMOUNT", precision = 15, scale = 3)
	private BigDecimal netAmount;

	@Column(name = "TAX_AMOUNT", precision = 15, scale = 3)
	private BigDecimal taxAmount;

	@OneToMany(mappedBy = "purchaseOrderHeader", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<PurchaseOrderItem> purchaseOrderItems;

	public PurchaseOrderHeader() {
		this.purchaseOrderItems = new ArrayList<PurchaseOrderItem>();
	}

	public String getPurchaseOrderId() {
		return purchaseOrderId;
	}

	public void setPurchaseOrderId(String param) {
		this.purchaseOrderId = param;
	}

	public void setSupplierId(String param) {
		this.supplierId = param;
	}

	public String getSupplierId() {
		return supplierId;
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

	public List<PurchaseOrderItem> getItems() {
		return this.purchaseOrderItems;
	}

	public void setItems(List<PurchaseOrderItem> items) {
		this.purchaseOrderItems = items;
	}

	public void addItem(PurchaseOrderItem item) {
		this.purchaseOrderItems.add(item);
	}

	public List<PurchaseOrderItem> getPurchaseOrderItems() {
		return purchaseOrderItems;
	}

	public void setPurchaseOrderItems(List<PurchaseOrderItem> param) {
		this.purchaseOrderItems = param;
	}

	@PrePersist
	private void persist() {
		int itemNumber = 10;
		this.netAmount = new BigDecimal("0.00");
		this.taxAmount = new BigDecimal("0.00");
		this.grossAmount = new BigDecimal("0.00");
		for (PurchaseOrderItem item : purchaseOrderItems) {
			item.setPurchaseOrderId(this.getPurchaseOrderId());
			item.setItemNumber(itemNumber);
			itemNumber += 10;
			item.persist();
			this.netAmount = this.netAmount.add(item.getNetAmount());
			this.taxAmount = this.taxAmount.add(item.getTaxAmount());
			this.grossAmount = this.grossAmount.add(item.getGrossAmount());
		}
	}

}