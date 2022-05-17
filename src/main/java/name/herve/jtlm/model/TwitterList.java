package name.herve.jtlm.model;

public class TwitterList extends TwitterUserCollection {
	private static final long serialVersionUID = -1604047346472894886L;
	private long id;
	private String name;
	private String description;

	public TwitterList() {
		super();
	}

	public String getDescription() {
		return description;
	}

	public long getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	public TwitterList setDescription(String description) {
		this.description = description;
		return this;
	}

	public TwitterList setId(long id) {
		this.id = id;
		return this;
	}

	public TwitterList setName(String name) {
		this.name = name;
		return this;
	}

}
