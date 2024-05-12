package it.unisalento.recproject.adminservice.service.pdf.componimentoPDFOrdineReward;

import com.itextpdf.kernel.color.Color;

public class HeaderDetails {
    String invoiceTitle= ConstantUtil.INVOICE_TITLE;
    String invoiceNoText= ConstantUtil.INVOICE_NO_TEXT;
    String invoiceDateText= ConstantUtil.INVOICE_DATE_TEXT;
    String invoiceNo= ConstantUtil.EMPTY;
    String invoiceDate= ConstantUtil.EMPTY;
    Color borderColor= Color.GRAY;

    public HeaderDetails setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
        return this;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public String getInvoiceDateText() {
        return invoiceDateText;
    }

    public String getInvoiceNoText() {
        return invoiceNoText;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public HeaderDetails setInvoiceNoText(String invoiceNoText) {
        this.invoiceNoText = invoiceNoText;
        return this;
    }

    public HeaderDetails setInvoiceDateText(String invoiceDateText) {
        this.invoiceDateText = invoiceDateText;
        return this;
    }

    public HeaderDetails setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
        return this;
    }

    public HeaderDetails setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
        return this;
    }

    public HeaderDetails setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        return this;
    }
    public HeaderDetails build()
    {
        return  this;
    }
}
