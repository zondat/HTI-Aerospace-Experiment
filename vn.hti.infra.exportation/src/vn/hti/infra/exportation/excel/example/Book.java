package vn.hti.infra.exportation.excel.example;

public class Book {
    private Integer id;
	private String title;
    private Integer quantity;
    private Double price;
    private Double totalMoney;
    
    public Book(int id, String title, int quantity, double price) {
    	this.id = id;
    	this.title = title;
    	this.quantity = quantity;
    	this.price = price;
    }
    
    public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}
    
}