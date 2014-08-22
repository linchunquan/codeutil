package model;

import codeutil.Common;
import model.annotation.ExtField;
import model.annotation.RelatedType;

public class Location {

	private long id;
	
	@ExtField(fieldName="地址名称")
	private String locationName;
	
	@ExtField(fieldName="日期",fieldType = Common.DateType)
	private long locationDate;
	
	@ExtField(fieldName="数量",optValues="[[\"零\",0],[\"壹\",1],[\"贰\",2]]",shouldValidate=false)
    private int count;
	
	@ExtField(fieldName="面积",min=1000,max=2000)
    private double area;
	
	@ExtField(fieldName="罗宇静",colspan=2)
    private String luoyujing;
	
	@ExtField(fieldName="备注",fieldType = Common.TextAreaType,colspan=2)
    private String remark;
	
	@ExtField(fieldName="备注2",fieldType = Common.HtmlEditor,colspan=2,shouldAddToTab=true)
    private String remark2;
	
	@RelatedType(type="PeopleClass",isOneToOne=true)
	@ExtField(subFieldName="第一用户",shouldAddToTab=true)
	private PeopleClass peopleClass;
	
	@RelatedType(type="PeopleClass",isOneToOne=true)
	@ExtField(subFieldName="第二用户",shouldAddToTab=true)
	private PeopleClass peopleClass1;
	
	public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public PeopleClass getPeopleClass() {
		return peopleClass;
	}

	public void setPeopleClass(PeopleClass peopleClass) {
		this.peopleClass = peopleClass;
	}

	public PeopleClass getPeopleClass1() {
		return peopleClass1;
	}

	public void setPeopleClass1(PeopleClass peopleClass1) {
		this.peopleClass1 = peopleClass1;
	}

    public long getLocationDate() {
        return locationDate;
    }

    public void setLocationDate(long locationDate) {
        this.locationDate = locationDate;
    }

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark2() {
		return remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public String getLuoyujing() {
		return luoyujing;
	}

	public void setLuoyujing(String luoyujing) {
		this.luoyujing = luoyujing;
	}
}
