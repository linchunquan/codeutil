package model;

import java.util.HashSet;
import java.util.Set;

import model.annotation.ExtField;
import model.annotation.ExtWidge;
import model.annotation.RelatedType;

@ExtWidge(columns=3)
public class PeopleClass{

	private long id;
	
	@ExtField(fieldName="姓名")
	private String peopleName;
	
	@ExtField(fieldName="年龄")
	private int age;
	
	@ExtField(fieldName="邮箱地址")
	private String email;
	
	
	//@RelatedType(type="Location",isUnidirectional=true,isAssociateDelete=true)
	private Set<Location>locations = new HashSet<Location>();
	
	//@RelatedType(type="Money")
	//private Money money;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPeopleName() {
		return peopleName;
	}

	public void setPeopleName(String peopleName) {
		this.peopleName = peopleName;
	}

	public Set<Location> getLocations() {
		return locations;
	}

	public void setLocations(Set<Location> locations) {
		this.locations = locations;
	}
/*
    public Money getMoney() {
        return money;
    }

    public void setMoney(Money money) {
        this.money = money;
    }
   */
}
