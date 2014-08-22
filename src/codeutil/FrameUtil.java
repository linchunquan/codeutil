package codeutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class FrameUtil {

    private static String framePageTemplate = "config/template/frame/frame.jsp";
    private static String framePageOutputDir = "output/WebRoot/frame/";
    
    private static String funcMenuTemplate = "config/template/frame/funcmenu.js";
    private static String funcMenuOutputDir = "output/WebRoot/frame/funcs/";
    
    private static String funcTemplate = "config/template/frame/functions.js";
    private static String funcGridTemplate = "config/template/frame/functions.Grid.js";
    private static String funcOutputDir = "output/WebRoot/frame/funcs/";
    
    public static void createFramePage() throws Exception{
    	
    	File file = new File(framePageOutputDir);
        file.mkdirs();
        
        FileInputStream in = new FileInputStream(framePageTemplate);
        byte[] readBytes = new byte[in.available()];
        in.read(readBytes);
        String string4file = new String(readBytes);
        
        in.close();
        
        List<String>allClasses = Common.getAllClassesForShort();
//  <script type="text/javascript" src="../testgrid/LocationGrid.js"></script>        
        int size = allClasses.size();
        StringBuffer strBuf = new StringBuffer();
        for(int i=0;i<size;i++) {
        	strBuf.append("  <script type=\"text/javascript\" src=\"grids/"+allClasses.get(i)+"Grid.js\"></script>\n");
        }
        
        StringBuffer strBuf2 = new StringBuffer();
        for(int i=0;i<size;i++) {
        	strBuf2.append("  <script type=\"text/javascript\" src=\"forms/"+allClasses.get(i)+"WidgeProxy.js\"></script>\n");
        }
        
        string4file = string4file.replace("[ImportGridJS]", strBuf.toString()).replace("[ImportWidgeProxyJS]", strBuf2.toString());
        FileOutputStream fos = new FileOutputStream(framePageOutputDir+"frame.jsp");
        fos.write(string4file.getBytes());
    }
    
    public static void createFuncMenu() throws Exception{
    	
    	File file = new File(funcMenuOutputDir);
        file.mkdirs();
        
        FileInputStream in = new FileInputStream(funcMenuTemplate);
        byte[] readBytes = new byte[in.available()];
        in.read(readBytes);
        String string4file = new String(readBytes);
        
        in.close();
        
        List<String>allClasses = Common.getAllClassesForShort();
//  <script type="text/javascript" src="../testgrid/LocationGrid.js"></script>        
        int size = allClasses.size();
        StringBuffer strBuf = new StringBuffer();
        for(int i=0;i<size;i++) {
        	String className = allClasses.get(i);
        	strBuf.append("        {text:\""+className+"\",id:\""+className+"\",iconCls:\"icon-cls\",tabName:\""+className+"\",leaf:true}");
        	if(i<size-1){
        		strBuf.append(",\n");
        	}
        }
        
        string4file = string4file.replace("[GridMenu]", strBuf.toString());
        FileOutputStream fos = new FileOutputStream(funcMenuOutputDir+"funcmenu.js");
        fos.write(string4file.getBytes());
    }
    
    public static void createAllFuncGrids() throws Exception{
    	
    	File file = new File(funcOutputDir);
        file.mkdirs();
        
        FileInputStream in = new FileInputStream(funcTemplate);
        byte[] readBytes = new byte[in.available()];
        in.read(readBytes);
        String string4file = new String(readBytes);
        in.close();
        
        in = new FileInputStream(funcGridTemplate);
        readBytes = new byte[in.available()];
        in.read(readBytes);
        String string4func = new String(readBytes);
        in.close();
        
        List<String>allClasses = Common.getAllClassesForShort();   
        int size = allClasses.size();
        StringBuffer strBuf = new StringBuffer();
        for(int i=0;i<size;i++) {
        	String className = allClasses.get(i);
        	String funcText = string4func.replace("[CLASS]", className);
        	strBuf.append(funcText);
        }
        
        string4file = string4file.replace("[FuncGrid]", strBuf.toString());
        FileOutputStream fos = new FileOutputStream(funcOutputDir+"functions.js");
        fos.write(string4file.getBytes());
    }
    
    public static void createAll() throws Exception{
    	createFramePage();
		createFuncMenu();
		createAllFuncGrids();
    }
    
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		createAll();
	}

}
