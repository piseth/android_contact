package piseth.contact;

public class Contact {

	private int id;
	private String name;
	private String phone;

	public Contact() {
	}

	@Override
	public String toString() {
		return name ;
	}

    public int getId() {
        return this.id;
    }

	public String getName() {
		return this.name;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}