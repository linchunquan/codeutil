package codeutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import model.annotation.DBFieldLength;
import model.annotation.DBFieldType;
import model.annotation.NotMapping;
import model.annotation.RelatedType;
public class HibernateUtil {
	
	private static String encode     = "UTF-8";
	
	private static String outputRoot = "output/";
	
	private static String configRoot = "output/config/";
	
	private static String dbHibernateTemplateRoot = "config/template/dbHibernateTemplate.xml";
	
	private static String dbConnectionTemplateRoot = "config/template/dbConnectionTemplate.xml";
	
	private static String querierTemplateRoot     = "config/template/Querier.java";
	
	private static String querierImplTemplateRoot = "config/template/QuerierImpl.java";
	
	private static String extendQuerierTemplateRoot     = "config/template/ExtendedQuerier.java";
	
	private static String extendQuerierImplTemplateRoot = "config/template/ExtendedQuerierImpl.java";
	
	public static void createSqlForMySql(String className,StringBuffer tables,StringBuffer foreignKeyConstraint) throws Exception, ClassNotFoundException{
		
		Map<String,String>foreignKeyMap = Common.getForeignKeyMap(className);
		
		String[] fullClassPathName = className.split("\\.");
		String shortClassName = fullClassPathName[fullClassPathName.length-1];
		
		Field[] fields=Class.forName(className).getDeclaredFields();
		int fieldLength = fields.length;
		
		tables.append("create table "+shortClassName+"(\n");
		
		tables.append("	id integer auto_increment,\n");
		
		for(int i=0;i<fieldLength;i++){
			
			Field field=fields[i];
			
			String fieldName = field.getName();
			
			if(fieldName.equals("id")){
			//	tables.append("	id integer auto_increment,\n");
			}
			else{
				boolean shouldNotMapping = field.isAnnotationPresent(NotMapping.class);
				if(!shouldNotMapping){
					
					field.setAccessible(true);
					Class type = field.getType();
					
					System.out.println("fieldName:"+fieldName);
					System.out.println("type:"+type);
					
					String basicMySqlDBType = Common.basicMySqlDBTypeMap.get(type+"");
					boolean hasDBFieldType = field.isAnnotationPresent(DBFieldType.class);
					if(hasDBFieldType){
						basicMySqlDBType = ((DBFieldType)field.getAnnotation(DBFieldType.class)).type();
					}
					
					Integer fLength = Common.basicDBFieldLengthMap.get(type+"");
					boolean hasDBFieldLength = field.isAnnotationPresent(DBFieldLength.class);
					if(hasDBFieldLength){
						fLength = ((DBFieldLength)field.getAnnotation(DBFieldLength.class)).length();
					}
					String fLen = fLength == null?"":"("+fLength+")";
					
					if(basicMySqlDBType!=null){
						tables.append("	"+fieldName+" "+basicMySqlDBType+fLen+",\n");
					}
					else{
						boolean hasRelatedType = field.isAnnotationPresent(RelatedType.class);
						if(hasRelatedType){
							String relatedType = ((RelatedType)field.getAnnotation(RelatedType.class)).type();
							if(!(type+"").contains("java.util.Set")){
								if(((RelatedType)field.getAnnotation(RelatedType.class)).propertyRef().length()==0){
									
									String foreignKey = foreignKeyMap.get(fieldName);
									//tables.append("	"+relatedType+"_ID integer,\n");
									tables.append("	"+foreignKey+" integer,\n");
									//foreignKeyConstraint.append("ALTER TABLE "+shortClassName+" ADD FOREIGN KEY ("+relatedType+"_ID"+") REFERENCES "+relatedType+"(id);\n");
									foreignKeyConstraint.append("ALTER TABLE "+shortClassName+" ADD FOREIGN KEY ("+foreignKey+") REFERENCES "+relatedType+"(id);\n");
									
								}		
							}
						}
					}
					System.out.println("-----------------------------------------------\n");
				}
			}
		}
		tables.append("	primary key(id)\n);\n\n");
	}
	public static void createAllSqlForMysql(String dbName) throws Exception, ClassNotFoundException, IOException{
		
		List<String>allClasses = Common.getAllClasses();
        StringBuffer tables = new StringBuffer();
		StringBuffer foreignKeys = new StringBuffer();
		
		tables.append("drop database if exists "+dbName+";\n").append("create database "+dbName+";\n").append("use "+dbName+";\n\n");
		
		
        int size = allClasses.size();
        for(int i=0;i<size;i++){
        	
        	if(allClasses.get(i).endsWith("_View")){
        		continue;
        	}
        	
        	createSqlForMySql(allClasses.get(i),tables,foreignKeys);
        }
        System.out.println(tables);
		System.out.println(foreignKeys);
		
		tables.append(foreignKeys);
	
		File file = new File(configRoot);
		file.mkdirs();
		
		FileOutputStream   out   =   new   FileOutputStream(configRoot+dbName+".sql");
		byte[]content = tables.toString().getBytes();
		out.write(content, 0, content.length);
		out.flush();
		
		StringBuffer clearTables = new StringBuffer();
		for(int i=size-1;i>=0;i--){
			String className = allClasses.get(i);
			String[] fullClassPathName = className.split("\\.");
			String shortClassName = fullClassPathName[fullClassPathName.length-1];
			clearTables.append("drop table if exists "+shortClassName+";\n");
        }
		out = new FileOutputStream(configRoot+dbName+"_cleartable.sql");
		content = clearTables.toString().getBytes();
		out.write(content, 0, content.length);
		out.flush();
	}
	//----------------------------------------------------------------------
	public static String createHBMForClass(String className) throws Exception{
		
		Map<String,String>foreignKeyMap=Common.getForeignKeyMap(className);
		
		String[] fullClassPathName = className.split("\\.");
		String shortClassName = fullClassPathName[fullClassPathName.length-1];
		
		Field[] fields=Class.forName(className).getDeclaredFields();
		int fieldLength = fields.length;
		StringBuffer strBuf = new StringBuffer();
		//strBuf.append("<?xml version=\"1.0\" encoding=\"GBK\"?>\n<!DOCTYPE hibernate-mapping PUBLIC\n        \"-//Hibernate/Hibernate Mapping DTD 3.0//EN\"\n        \"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd\">\n<hibernate-mapping package=\"model\">\n");
		//strBuf.append("	<class name=\""+shortClassName+"\" table=\"Attachemnt\" lazy=\"false\">\n\n");
		for(int i=0;i<fieldLength;i++){
			Field field=fields[i];
			String fieldName = field.getName();
			if(!fieldName.equals("id")){
				boolean shouldNotMapping = field.isAnnotationPresent(NotMapping.class);
				if(!shouldNotMapping){
					
					field.setAccessible(true);
					Class type = field.getType();
					
					
					System.out.println("fieldName:"+fieldName);
					System.out.println("type:"+type);
					if(shouldNotMapping){  
		                System.out.println("shouldNotMapping");
		            }
					
					
					String basicType = Common.basicTypeMap.get(type+"");
					if(basicType!=null){
						strBuf.append("		").append("<property name=\"").append(fieldName).append("\"\n		          column=\"").append(fieldName).append("\"\n		          type=\"").append(basicType).append("\"/>\n\n");
					}
					else{
						boolean hasRelatedType = field.isAnnotationPresent(RelatedType.class);
						if(hasRelatedType){
							String relatedType = ((RelatedType)field.getAnnotation(RelatedType.class)).type();
							if((type+"").contains("java.util.Set")){
								if(!((RelatedType)field.getAnnotation(RelatedType.class)).isUnidirectional()){
									
									String cascade = "none";
									if(((RelatedType)field.getAnnotation(RelatedType.class)).isAssociateDelete()){
										cascade="delete";
									}
									//tables.append("	"+foreignKeyMap.get(fieldName)+" integer,\n");
									String foreignKey = foreignKeyMap.get(fieldName);
									strBuf.append("		<set\n			name=\"").append(fieldName).append("\"").append("\n			")
									      .append("table=\"").append(relatedType).append("\"\n			")
									      .append("cascade=\""+cascade+"\"\n			")
									      .append("inverse=\"true\"\n			")
									      .append("lazy=\"false\">\n			")
									      //.append("<key column=\""+shortClassName+"_ID\"/>\n			")
									      .append("<key column=\""+foreignKey+"\"/>\n			")
									      .append("<one-to-many class=\""+relatedType+"\"/>\n		</set>\n\n");
								}
								else{
									
									String cascade = "save-update";
									if(((RelatedType)field.getAnnotation(RelatedType.class)).isAssociateDelete()){
										cascade="all";
									}
									String foreignKey = foreignKeyMap.get(fieldName);
									strBuf.append("		<set\n			name=\"").append(fieldName).append("\"").append("\n			")
								      .append("table=\"").append(relatedType).append("\"\n			")
								      .append("cascade=\""+cascade+"\"\n			")
								      .append("lazy=\"false\">\n			")
								      //.append("<key column=\""+shortClassName+"_ID\"/>\n			")
								      .append("<key column=\""+foreignKey+"\"/>\n			")
								      .append("<one-to-many class=\""+relatedType+"\"/>\n		</set>\n\n");
								}
							}
							else{
								if(!((RelatedType)field.getAnnotation(RelatedType.class)).isOnleForForeignKey()){
									if(!((RelatedType)field.getAnnotation(RelatedType.class)).isOneToOne()){
										String foreignKey = foreignKeyMap.get(fieldName);
										strBuf.append("		<many-to-one\n         	name=\"").append(fieldName).append("\"\n         	")
										      .append("class=\""+relatedType+"\"\n            ")
										      //.append("column=\""+relatedType+"_ID\"\n        />\n\n");
										      .append("column=\""+foreignKey+"\"\n        />\n\n");
									}
									else{
										if(((RelatedType)field.getAnnotation(RelatedType.class)).propertyRef().length()==0){
											String foreignKey = foreignKeyMap.get(fieldName);
											strBuf.append("		<many-to-one\n         	name=\"").append(fieldName).append("\"\n         	")
										      .append("class=\""+relatedType+"\"\n            ")
										      .append("unique=\"true\"\n            ")
										      .append("cascade=\"all\"\n            ")
										      //.append("column=\""+relatedType+"_ID\"\n        />\n\n");
										      .append("column=\""+foreignKey+"\"\n        />\n\n");
										}
										else{
											strBuf.append("		<one-to-one\n         	name=\"").append(fieldName).append("\"\n         	")
										      .append("class=\""+relatedType+"\"\n            ")
											  .append("property-ref=\""+((RelatedType)field.getAnnotation(RelatedType.class)).propertyRef()+"\"\n        />\n\n");
										}
									}
								}
							}
						}
					}
					System.out.println("-----------------------------------------------\n");
				}
			}
		}
		return strBuf.toString();
	}
	

	public void createAllHBM() throws Exception{
		
		String dir=outputRoot+"src/model/hbm/";
		//生成目录
		(new File(dir)).mkdirs();
		
		List<ClassProxcy> classProxcies = new LinkedList<ClassProxcy>();
		Map<String,ClassProxcy> classMap = new HashMap<String,ClassProxcy>();
		List<String>allClasses = Common.getAllClasses();
		
        int size = allClasses.size();
        
        for(int i=0;i<size;i++){
        	
        	ClassProxcy classProxcy  = new ClassProxcy();
        	
        	String className = allClasses.get(i);
        	String configContent = createHBMForClass(className);
        	
        	classProxcy.setClassName(className);
        	classProxcy.setConfigContent(configContent);

        	classProxcies.add(classProxcy);
        	classMap.put(className, classProxcy);
        }
        
        size = classProxcies.size();
        for(int i=0;i<size;i++){
        	ClassProxcy classProxcy=classProxcies.get(i);
        	String className = classProxcy.getClassName();
        	//System.out.println("Parent Class:"+Class.forName(className).getSuperclass());
        	//得到类的父类的名称
        	ClassProxcy parent = classMap.get((""+Class.forName(className).getSuperclass()).replace("class ", ""));
        	if(parent!=null){
        		//System.out.println("parent is not null!");
        		classProxcy.setParentClass(parent);
        		//System.out.println("sub class name:"+className);
        		parent.getSubClass().put(className, classProxcy);
        	}
        }
        
        while(classProxcies.size()>0){
        	Iterator<ClassProxcy> it = classProxcies.iterator();
        	while(it.hasNext()){
        		ClassProxcy classProxcy = it.next();
        		if(classProxcy.getSubClass().size()==0){
        			ClassProxcy parentClass = classProxcy.parentClass;
        			String className = classProxcy.getClassName();
    				String shortClassName = className.split("\\.")[className.split("\\.").length-1];
        			if(parentClass!=null){
        				String parentConfigContent = parentClass.getConfigContent();
        				parentConfigContent = parentConfigContent+"		<joined-subclass name=\""+className+"\" table=\""+shortClassName+"\">\n\n		<key column=\"id\"/>\n\n"+classProxcy.getConfigContent()+"		</joined-subclass>\n\n";
        				//System.out.println("Remove subclass:"+className);
        				//System.out.println("---------------------parentConfigContent-----------------");
        				//System.out.println(parentConfigContent);
        				parentClass.getSubClass().remove(className);
        				parentClass.setConfigContent(parentConfigContent);
        				//System.out.println("classProxcy.getSubClass().size:"+classProxcy.getSubClass().size());
        			}
        			else{
        				StringBuffer strBuf = new StringBuffer();
        				strBuf.append("<?xml version=\"1.0\" encoding=\"GBK\"?>\n<!DOCTYPE hibernate-mapping PUBLIC\n        \"-//Hibernate/Hibernate Mapping DTD 3.0//EN\"\n        \"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd\">\n<hibernate-mapping  package=\"model\">\n");
        				strBuf.append("	<class name=\""+className+"\" table=\""+shortClassName+"\" lazy=\"false\">\n\n");
        				strBuf.append("		<id name=\"id\" column=\"id\" type=\"long\" >\n			<generator class=\"identity\"/>\n		</id>\n\n").append(classProxcy.configContent);
        				strBuf.append("	</class>\n</hibernate-mapping>");
        				FileOutputStream   out   =   new   FileOutputStream(dir+shortClassName+".hbm.xml");
        				out.write(strBuf.toString().getBytes(),   0,   strBuf.toString().getBytes().length);
        			}
        			it.remove();
        		}
        	}
        	//System.out.println(classProxcies.size());
        }
	}
	
	private class ClassProxcy{
		
		private String className;
		private String configContent;
		private ClassProxcy parentClass;
		private Map<String,ClassProxcy>subClass=new HashMap<String,ClassProxcy>();
		
		public ClassProxcy(){};
		
		public String getClassName() {
			return className;
		}
		public void setClassName(String className) {
			this.className = className;
		}
		public String getConfigContent() {
			return configContent;
		}
		public void setConfigContent(String configContent) {
			this.configContent = configContent;
		}
		public ClassProxcy getParentClass() {
			return parentClass;
		}
		public void setParentClass(ClassProxcy parentClass) {
			this.parentClass = parentClass;
		}
		public Map<String, ClassProxcy> getSubClass() {
			return subClass;
		}
		public void setSubClass(Map<String, ClassProxcy> subClass) {
			this.subClass = subClass;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((className == null) ? 0 : className.hashCode());
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
			final ClassProxcy other = (ClassProxcy) obj;
			if (className == null) {
				if (other.className != null)
					return false;
			} else if (!className.equals(other.className))
				return false;
			return true;
		}
	}
	//------------------------------------创建基本curd操作方法----------------------------------------
	public static void createCURDDaoForClass(String className) throws Exception{
		String dir1=outputRoot+"src/dao/curd/";
		String dir2=outputRoot+"src/dao/impl/curd/";
		String[] fullClassPathName = className.split("\\.");
		String shortClassName = fullClassPathName[fullClassPathName.length-1];
		File file = new File(dir1);
		//接口文件夹
		file.mkdirs();
		file = new File(dir2);
		//实现类文件夹
		file.mkdirs();
		
		//创建接口
		StringBuffer strBuf = new StringBuffer();
		String lowerShortClassName =  Common.toLowerCaseOfTheFirstCharacter(shortClassName);
		String pluralityClassName = Common.getPlurality(lowerShortClassName);
		strBuf.append("package dao.curd;\n\nimport java.util.Collection;\nimport dao.ParentDao;\nimport model.").append(shortClassName).append(";\n\n")
		      .append("public interface ").append(shortClassName).append("Dao extends ParentDao{\n\n	")
		      .append(shortClassName).append(" get").append(shortClassName).append("(long id);\n\n	")
		      .append(shortClassName).append(" save").append(shortClassName).append("(").append(shortClassName).append(" ").append(lowerShortClassName).append(");\n\n	")
		      .append("void").append(" update").append(shortClassName).append("(").append(shortClassName).append(" ").append(lowerShortClassName).append(");\n\n	")
		      .append(shortClassName).append(" saveOrUpdate").append(shortClassName).append("(").append(shortClassName).append(" ").append(lowerShortClassName).append(");\n\n	")
		      .append("void").append(" delete").append(shortClassName).append("(").append(shortClassName).append(" ").append(lowerShortClassName).append(");\n\n	")
		      .append("void").append(" deleteAll").append(Common.getPlurality(shortClassName)).append("(Collection<").append(shortClassName).append("> ").append(pluralityClassName).append(");\n")
		.append("}");
		
		FileOutputStream   out   =   new   FileOutputStream(dir1+shortClassName+"Dao.java");
		out.write(strBuf.toString().getBytes(),   0,   strBuf.toString().getBytes().length);
		out.close();
		
		//创建实现类
		strBuf = new StringBuffer();
		strBuf.append("package dao.impl.curd;\n\nimport java.util.Collection;\nimport dao.impl.BasicHibernateDao;\nimport dao.curd.").append(shortClassName).append("Dao;\n").append("import model.").append(shortClassName).append(";\n\n")
		      .append("public class ").append(shortClassName).append("DaoImpl  extends BasicHibernateDao implements ").append(shortClassName).append("Dao{\n\n	")
		      
		      .append("public ").append(shortClassName).append(" get").append(shortClassName).append("(long id) {\n		")
		      .append("return ("+shortClassName+")getHibernateTemplate().get("+shortClassName+".class, id);")
		      .append("\n	}\n\n	")
		      
		      
		      .append("public ").append(shortClassName).append(" save").append(shortClassName).append("(").append(shortClassName).append(" ").append(lowerShortClassName).append(") {\n		")
		      .append("getHibernateTemplate().save("+lowerShortClassName+");\n		")
		      .append("return "+lowerShortClassName+";")
		      .append("\n	}\n\n	")
		      
		      
		      .append("public ").append("void").append(" update").append(shortClassName).append("(").append(shortClassName).append(" ").append(lowerShortClassName).append(") {\n		")
		      .append("getHibernateTemplate().update("+lowerShortClassName+");")
		      .append("\n	}\n\n	")
		      
		      
		      .append("public ").append(shortClassName).append(" saveOrUpdate").append(shortClassName).append("(").append(shortClassName).append(" ").append(lowerShortClassName).append(") {\n		")
		      .append("getHibernateTemplate().saveOrUpdate("+lowerShortClassName+");\n		")
		      .append("return "+lowerShortClassName+";")
		      .append("\n	}\n\n	")
		      
		      
		      .append("public ").append("void").append(" delete").append(shortClassName).append("(").append(shortClassName).append(" ").append(lowerShortClassName).append(") {\n		")
		      .append("getHibernateTemplate().delete("+lowerShortClassName+");")
		      .append("\n	}\n\n	")
		      
		      
		      .append("public ").append("void").append(" deleteAll").append(Common.getPlurality(shortClassName)).append("(Collection<").append(shortClassName).append("> ").append(pluralityClassName).append(") {\n		")
		      .append("getHibernateTemplate().deleteAll("+pluralityClassName+");")
		      .append("\n	}\n")
		      
		.append("}");
		
		out   =   new   FileOutputStream(dir2+shortClassName+"DaoImpl.java");
		out.write(strBuf.toString().getBytes(),   0,   strBuf.toString().getBytes().length);
		out.close();
	}
	
	public static void createAllCURDDao() throws Exception{
		
		List<String>allClasses = Common.getAllClasses();
		int size = allClasses.size();
		for(int i=0;i<size;i++){
			createCURDDaoForClass(allClasses.get(i));
		}
	}
	//---------------------------------------------------------------------------------------------
	
	public static void createCompositeDao()throws Exception{
		
		String dir1=outputRoot+"src/dao/";
		String dir2=outputRoot+"src/dao/impl/";
		File file = new File(dir1);
		//接口文件夹
		file.mkdirs();
		file = new File(dir2);
		//实现类文件夹
		file.mkdirs();
		List<String>allClasses = Common.getAllClasses();
		int size = allClasses.size();
		
		//创建接口
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("package dao;\n\nimport dao.curd.*;\nimport dao.query.*;\n\npublic interface CompositeDao{\n\n");
		//创建实现类
		StringBuffer strBufImpl = new StringBuffer();
		strBufImpl.append("package dao.impl;\n\nimport dao.impl.curd.*;\nimport dao.curd.*;\nimport dao.impl.query.*;\nimport dao.query.*;\nimport dao.CompositeDao;;\n\npublic class CompositeDaoImpl implements CompositeDao{\n\n");
		
		for(int i=0;i<size;i++){
			
			String className = allClasses.get(i);
			String[] fullClassPathName = className.split("\\.");
			String shortClassName = fullClassPathName[fullClassPathName.length-1];
			strBuf.append("	"+shortClassName+"Dao get"+shortClassName+"Dao();\n\n");
		}
		for(int i=0;i<size;i++){
			
			String className = allClasses.get(i);
			String[] fullClassPathName = className.split("\\.");
			String shortClassName = fullClassPathName[fullClassPathName.length-1];
			strBuf.append("	"+shortClassName+"Querier get"+shortClassName+"Querier();\n\n");
		}
		strBuf.append("	"+"ComplexQuerier getComplexQuerier();\n\n");
		
		for(int i=0;i<size;i++){
			
			String className = allClasses.get(i);
			String[] fullClassPathName = className.split("\\.");
			String shortClassName = fullClassPathName[fullClassPathName.length-1];
			strBufImpl.append("	private "+shortClassName+"Dao "+Common.toLowerCaseOfTheFirstCharacter(shortClassName)+"Dao;\n\n");
		}
		for(int i=0;i<size;i++){
			
			String className = allClasses.get(i);
			String[] fullClassPathName = className.split("\\.");
			String shortClassName = fullClassPathName[fullClassPathName.length-1];
			strBufImpl.append("	private "+shortClassName+"Querier "+Common.toLowerCaseOfTheFirstCharacter(shortClassName)+"Querier;\n\n");
		}
		strBufImpl.append("	private ComplexQuerier complexQuerier;\n\n");
		
		strBufImpl.append("	public CompositeDaoImpl(\n");
		
		for(int i=0;i<size;i++){
			
			String className = allClasses.get(i);
			String[] fullClassPathName = className.split("\\.");
			String shortClassName = fullClassPathName[fullClassPathName.length-1];
			strBufImpl.append("			"+shortClassName+"Dao "+Common.toLowerCaseOfTheFirstCharacter(shortClassName)+"Dao,\n");
		}
		for(int i=0;i<size;i++){
			
			String className = allClasses.get(i);
			String[] fullClassPathName = className.split("\\.");
			String shortClassName = fullClassPathName[fullClassPathName.length-1];
			strBufImpl.append("			"+shortClassName+"Querier "+Common.toLowerCaseOfTheFirstCharacter(shortClassName)+"Querier");
			strBufImpl.append(",\n");
		}
		strBufImpl.append("			"+"ComplexQuerier complexQuerier");
		strBufImpl.append(") {\n\n");
		for(int i=0;i<size;i++){
			
			String className = allClasses.get(i);
			String[] fullClassPathName = className.split("\\.");
			String shortClassName = Common.toLowerCaseOfTheFirstCharacter(fullClassPathName[fullClassPathName.length-1]);
			strBufImpl.append(" 		this."+shortClassName+"Dao = "+shortClassName+"Dao;\n");
			strBufImpl.append(" 		this."+shortClassName+"Dao.setCompositeDao(this);\n\n");
		}
		for(int i=0;i<size;i++){
			
			String className = allClasses.get(i);
			String[] fullClassPathName = className.split("\\.");
			String shortClassName = Common.toLowerCaseOfTheFirstCharacter(fullClassPathName[fullClassPathName.length-1]);
			strBufImpl.append(" 		this."+shortClassName+"Querier = "+shortClassName+"Querier;\n");
			strBufImpl.append(" 		this."+shortClassName+"Querier.setCompositeDao(this);\n\n");
		}
		strBufImpl.append(" 		this.complexQuerier = complexQuerier;\n");
		strBufImpl.append(" 		this.complexQuerier.setCompositeDao(this);\n\n");
		strBufImpl.append("	}\n");
		
		strBuf.append("}");
		strBufImpl.append("}");
		
		FileOutputStream   out   =   new   FileOutputStream(dir1+"CompositeDao.java");
		out.write(strBuf.toString().getBytes(),   0,   strBuf.toString().getBytes().length);
		out.close();
		
		out   =   new   FileOutputStream(dir2+"CompositeDaoImpl.java");
		out.write(strBufImpl.toString().getBytes(),   0,   strBufImpl.toString().getBytes().length);
		out.close();
	}
	
	//------------------------------------创建基本查询操作方法----------------------------------------
	public static void createQuerierForClass(String className) throws Exception{
		
		String dir1=outputRoot+"src/dao/query/";
		String dir2=outputRoot+"src/dao/impl/query/";
		
		String dir3=outputRoot+"extendedQuery/src/dao/query/extend/";
		String dir4=outputRoot+"extendedQuery/src/dao/impl/query/extend/";
		
		File file = new File(dir1);
		//创建接口文件夹
		file.mkdirs();
		file = new File(dir2);
		//创建实现类文件夹
		file.mkdirs();
		
		file = new File(dir3);
		//创建实现类文件夹
		file.mkdirs();
		
		file = new File(dir4);
		//创建实现类文件夹
		file.mkdirs();
		
		String[] fullClassPathName = className.split("\\.");
	
		String shortClassName = fullClassPathName[fullClassPathName.length-1];
		String lowerShortClassName =  Common.toLowerCaseOfTheFirstCharacter(shortClassName);
		String pluralityClassName = Common.getPlurality(shortClassName);
		
		//创建接口
		FileInputStream in = new FileInputStream(querierTemplateRoot);
        byte[] readBytes = new byte[in.available()];
        in.read(readBytes);
        String string4file = new String(readBytes,encode);
        string4file=string4file.replace("[Class]", shortClassName).replace("[class]", lowerShortClassName).replace("[Classes]", pluralityClassName);
        FileOutputStream   out   =   new   FileOutputStream(dir1+shortClassName+"Querier.java");
		out.write(string4file.toString().getBytes(),   0,   string4file.toString().getBytes().length);
		out.close();
		
		//创建实现类
		in = new FileInputStream(querierImplTemplateRoot);
        readBytes = new byte[in.available()];
        in.read(readBytes);
        string4file = new String(readBytes,encode);
        string4file=string4file.replace("[Class]", shortClassName).replace("[class]", lowerShortClassName).replace("[Classes]", pluralityClassName);
        out   =   new   FileOutputStream(dir2+shortClassName+"QuerierImpl.java");
		out.write(string4file.toString().getBytes(),   0,   string4file.toString().getBytes().length);
		out.close();
		
		//创建扩展接口
		in = new FileInputStream(extendQuerierTemplateRoot);
        readBytes = new byte[in.available()];
        in.read(readBytes);
        string4file = new String(readBytes,encode);
        string4file=string4file.replace("[Class]", shortClassName).replace("[class]", lowerShortClassName).replace("[Classes]", pluralityClassName);
        out   =   new   FileOutputStream(dir3+"Extended"+shortClassName+"Querier.java");
		out.write(string4file.toString().getBytes(),   0,   string4file.toString().getBytes().length);
		out.close();
		
		//创建扩展实现类
		in = new FileInputStream(extendQuerierImplTemplateRoot);
        readBytes = new byte[in.available()];
        in.read(readBytes);
        string4file = new String(readBytes,encode);
        string4file=string4file.replace("[Class]", shortClassName).replace("[class]", lowerShortClassName).replace("[Classes]", pluralityClassName);
        out   =   new   FileOutputStream(dir4+"Extended"+shortClassName+"QuerierImpl.java");
		out.write(string4file.toString().getBytes(),   0,   string4file.toString().getBytes().length);
		out.close();
	}
	
	public static void createAllQueriers() throws Exception{
		
		List<String>allClasses = Common.getAllClasses();
		int size = allClasses.size();
		for(int i=0;i<size;i++){
			createQuerierForClass(allClasses.get(i));
		}
	}
	//---------------------------------------------------------------------------------------------
	
	//------------------------------------创建Hibernate配置文件----------------------------------------
	public static void createHibernateFile() throws Exception{
		
		FileInputStream in = new FileInputStream(dbHibernateTemplateRoot);
		byte[] readBytes = new byte[in.available()];
		in.read(readBytes);
		String string4file = new String(readBytes,encode);
		
		List<String>allClasses = Common.getAllClasses();

		int size = allClasses.size();
		StringBuffer strBufHBMFiles = new StringBuffer();
		StringBuffer strBufDAOsForProxyCreator = new StringBuffer();
		StringBuffer strBufQueriersForProxyCreator = new StringBuffer();
		StringBuffer strBufDAOs = new StringBuffer();
		StringBuffer strBufQueriers = new StringBuffer();
		StringBuffer strBufDAOsForCompositeDao = new StringBuffer();
		StringBuffer strBufQueriersForCompositeDao = new StringBuffer();
		for(int i=0;i<size;i++){
			String className = allClasses.get(i);
			String[] fullClassPathName = className.split("\\.");
			String shortClassName = fullClassPathName[fullClassPathName.length-1];
			String lowerShortClassName =  Common.toLowerCaseOfTheFirstCharacter(shortClassName);
			
			if((Class.forName(className).getSuperclass()+"").equals("class java.lang.Object")){
				strBufHBMFiles.append("        		<value>model/hbm/"+shortClassName+".hbm.xml</value>\n");
			}
			
			strBufDAOsForProxyCreator.append("        		<value>"+lowerShortClassName+"Dao</value>\n");
			strBufQueriersForProxyCreator.append("        		<value>"+lowerShortClassName+"Querier</value>\n");
			strBufDAOs.append("    <bean id=\""+lowerShortClassName+"Dao\" class=\"dao.impl.curd."+shortClassName+"DaoImpl\" parent=\"daoTemplate\"/>\n");
			strBufQueriers.append("    <bean id=\""+lowerShortClassName+"Querier\" class=\"dao.impl.query.extend.Extended"+shortClassName+"QuerierImpl\" parent=\"daoTemplate\"/>\n");
			strBufDAOsForCompositeDao.append("    	<constructor-arg ref=\""+lowerShortClassName+"Dao\"/>\n");
			strBufQueriersForCompositeDao.append("    	<constructor-arg ref=\""+lowerShortClassName+"Querier\"/>\n");
		}
		
		strBufQueriersForProxyCreator.append("        		<value>complexQuerier</value>\n");
		strBufQueriers.append("    <bean id=\"complexQuerier\" class=\"dao.impl.query.ComplexQuerierImpl\" parent=\"daoTemplate\"><property name=\"dataSource\" ref=\"dataSource\"/></bean>\n");
		strBufQueriersForCompositeDao.append("    	<constructor-arg ref=\"complexQuerier\"/>\n");
		
		string4file=string4file.replace("<!--[HBMFiles]-->", strBufHBMFiles.toString());
		string4file=string4file.replace("<!--[DAOsForProxyCreator]-->", strBufDAOsForProxyCreator.toString());
		string4file=string4file.replace("<!--[QueriersForProxyCreator]-->", strBufQueriersForProxyCreator.toString());
		string4file=string4file.replace("<!--[DAOs]-->", strBufDAOs.toString());
		string4file=string4file.replace("<!--[Queriers]-->", strBufQueriers.toString());
		string4file=string4file.replace("<!--[DAOsForCompositeDao]-->", strBufDAOsForCompositeDao.toString());
		string4file=string4file.replace("<!--[QueriersForCompositeDao]-->",strBufQueriersForCompositeDao.toString());
		
		File file = new File(configRoot);
		file.mkdirs();
		FileOutputStream   out   =   new   FileOutputStream(configRoot+"dbHibernate.xml");
		out.write(string4file.toString().getBytes(),   0,   string4file.toString().getBytes().length);
	}
	
	private static void createDbConnectionFile(String dbName) throws Exception{
		
		FileInputStream in = new FileInputStream(dbConnectionTemplateRoot);
		byte[] readBytes = new byte[in.available()];
		in.read(readBytes);
		String string4file = new String(readBytes,encode);
		
		string4file=string4file.replace("[DBNAME]",dbName);
		
		File file = new File(configRoot);
		file.mkdirs();
		FileOutputStream   out   =   new   FileOutputStream(configRoot+"dbConnection.xml");
		out.write(string4file.toString().getBytes(),   0,   string4file.toString().getBytes().length);
	}
	//-----------------------------------------------------------------------------------------------
	
	public static void createAll(String dbName) throws ClassNotFoundException, IOException, Exception {
		createAllSqlForMysql(dbName);
		(new HibernateUtil()).createAllHBM();
		createAllCURDDao();
		createAllQueriers();
		createHibernateFile();
		createDbConnectionFile(dbName);
		createCompositeDao();
	}
	
	public static void main(String[]args) throws Exception, ClassNotFoundException{
		/*createAllSqlForMysql("codeutil");
		(new HibernateUtil()).createAllHBM();
		createAllCURDDao();
		createAllQueriers();
		createHibernateFile();
		createCompositeDao();*/
		createAll("codeutil");
	}
}
