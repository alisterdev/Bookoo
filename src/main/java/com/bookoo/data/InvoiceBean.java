package com.bookoo.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * Data Bean representing Orders
 * @author Anna Rogozin, Alex Ilea
 *
 */

@Named("invoiceBean")
@RequestScoped
public class InvoiceBean implements Serializable{
	
	private static final long serialVersionUID = -1729727801956861162L;
	private long saleId;
	private Timestamp saleDate;
	private long clientId;
	private BigDecimal netPrice;
	private BigDecimal PST;
	private BigDecimal GST;
	private BigDecimal HST;
	private BigDecimal grossPrice;
	private boolean available;
	
	public InvoiceBean() {
		super();
	}
	
	public long getSaleId() {
		return saleId;
	}
	public void setSaleId(long saleId) {
		this.saleId = saleId;
	}
	public Timestamp getSaleDate() {
		return saleDate;
	}
	
	public String getFormattedSaleDate() {
		return new SimpleDateFormat("MMMM dd, yyyy").format(saleDate);
	}
	
	public void setSaleDate(Timestamp saleDate) {
		this.saleDate = saleDate;
	}
	public long getClientId() {
		return clientId;
	}
	public void setClientId(long clientId) {
		this.clientId = clientId;
	}
	public BigDecimal getNetPrice() {
		return netPrice;
	}
	public void setNetPrice(BigDecimal netPrice) {
		this.netPrice = netPrice;
	}
	public BigDecimal getPST() {
		return PST;
	}
	public void setPST(BigDecimal pST) {
		PST = pST;
	}
	public BigDecimal getGST() {
		return GST;
	}
	public void setGST(BigDecimal gST) {
		GST = gST;
	}
	public BigDecimal getHST() {
		return HST;
	}
	public void setHST(BigDecimal hST) {
		HST = hST;
	}
	public BigDecimal getGrossPrice() {
		return grossPrice;
	}
	public void setGrossPrice(BigDecimal grossPrice) {
		this.grossPrice = grossPrice;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	@Override
	public String toString() {
		return "InvoiceBean [saleId=" + saleId + ", saleDate=" + saleDate
				+ ", clientId=" + clientId + ", netPrice=" + netPrice
				+ ", PST=" + PST + ", GST=" + GST + ", HST=" + HST
				+ ", grossPrice=" + grossPrice + ", available=" + available
				+ "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((GST == null) ? 0 : GST.hashCode());
		result = prime * result + ((HST == null) ? 0 : HST.hashCode());
		result = prime * result + ((PST == null) ? 0 : PST.hashCode());
		result = prime * result + (available ? 1231 : 1237);
		result = prime * result + (int) (clientId ^ (clientId >>> 32));
		result = prime * result
				+ ((grossPrice == null) ? 0 : grossPrice.hashCode());
		result = prime * result
				+ ((netPrice == null) ? 0 : netPrice.hashCode());
		result = prime * result
				+ ((saleDate == null) ? 0 : saleDate.hashCode());
		result = prime * result + (int) (saleId ^ (saleId >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InvoiceBean other = (InvoiceBean) obj;
		if (GST == null) {
			if (other.GST != null)
				return false;
		} else if (!GST.equals(other.GST))
			return false;
		if (HST == null) {
			if (other.HST != null)
				return false;
		} else if (!HST.equals(other.HST))
			return false;
		if (PST == null) {
			if (other.PST != null)
				return false;
		} else if (!PST.equals(other.PST))
			return false;
		if (available != other.available)
			return false;
		if (clientId != other.clientId)
			return false;
		if (grossPrice == null) {
			if (other.grossPrice != null)
				return false;
		} else if (!grossPrice.equals(other.grossPrice))
			return false;
		if (netPrice == null) {
			if (other.netPrice != null)
				return false;
		} else if (!netPrice.equals(other.netPrice))
			return false;
		if (saleDate == null) {
			if (other.saleDate != null)
				return false;
		} else if (!saleDate.equals(other.saleDate))
			return false;
		if (saleId != other.saleId)
			return false;
		return true;
	}
	
	
	
}
