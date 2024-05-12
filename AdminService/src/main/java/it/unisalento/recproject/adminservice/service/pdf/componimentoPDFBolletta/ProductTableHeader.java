package it.unisalento.recproject.adminservice.service.pdf.componimentoPDFBolletta;

public class ProductTableHeader {
    String description= ConstantUtil.PRODUCT_TABLE_DESCRIPTION;
    String quantity=ConstantUtil.PRODUCT_TABLE_QUANTITY;
    String price=ConstantUtil.PRODUCT_TABLE_PRICE;

    public ProductTableHeader setDescription(String description) {
        this.description = description;
        return this;
    }

    public ProductTableHeader setQuantity(String quantity) {
        this.quantity = quantity;
        return this;
    }

    public ProductTableHeader setPrice(String price) {
        this.price = price;
        return this;
    }
    public ProductTableHeader build(){
        return this;
    }
}
