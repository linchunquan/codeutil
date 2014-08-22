package controller;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.*;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import dao.CompositeDao;

import util.datastruct.RecordCound;


public abstract class CommandHandleController extends AbstractController{
	
    private CompositeDao compositeDao;
    
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String cmd=request.getParameter("cmd");
		if(cmd!=null){
			
			if(CommandType.cmdMap.containsKey(cmd)){
			
				switch(CommandType.cmdMap.get(cmd)){		    
			    	case CommandType.findAllLocations:findAllLocations(request,response);break;
			    	case CommandType.findAllMoneies:findAllMoneies(request,response);break;
			    	case CommandType.findAllPeopleClasses:findAllPeopleClasses(request,response);break;
	
			    	default:{
			    		parseParameters(request);
			    	}
			    }
			}
			else {
				return handleRequestInternalForExtention(request,response);
			}
		}
		return null;
	}
	
	protected abstract ModelAndView handleRequestInternalForExtention(HttpServletRequest request,
			HttpServletResponse response) throws Exception;
	
	private void findAllLocations(HttpServletRequest request, HttpServletResponse response) throws IOException{

		int start = Integer.valueOf(request.getParameter("start"));
		int limit = Integer.valueOf(request.getParameter("limit"));
		String sort = request.getParameter("sort");
		String dir = request.getParameter("dir");
		String[]queryCondition = parseQueryCondition(request);
		RecordCound recordCound = new RecordCound();
		
		List<Location> locations = this.compositeDao.getLocationQuerier().findAllLocations(start, limit, queryCondition, sort, dir, recordCound);
		String json = constructPaggingRecordsJson(start,limit,recordCound.value,JSONHelper.basicObjectToJson(locations));

		System.out.println("json:"+json);
		
		renderJSON(response,json);
	}
	private void findAllMoneies(HttpServletRequest request, HttpServletResponse response) throws IOException{

		int start = Integer.valueOf(request.getParameter("start"));
		int limit = Integer.valueOf(request.getParameter("limit"));
		String sort = request.getParameter("sort");
		String dir = request.getParameter("dir");
		String[]queryCondition = parseQueryCondition(request);
		RecordCound recordCound = new RecordCound();
		
		List<Money> moneies = this.compositeDao.getMoneyQuerier().findAllMoneies(start, limit, queryCondition, sort, dir, recordCound);
		String json = constructPaggingRecordsJson(start,limit,recordCound.value,JSONHelper.basicObjectToJson(moneies));

		renderJSON(response,json);
	}
	private void findAllPeopleClasses(HttpServletRequest request, HttpServletResponse response) throws IOException{

		int start = Integer.valueOf(request.getParameter("start"));
		int limit = Integer.valueOf(request.getParameter("limit"));
		String sort = request.getParameter("sort");
		String dir = request.getParameter("dir");
		String[]queryCondition = parseQueryCondition(request);
		RecordCound recordCound = new RecordCound();
		
		List<PeopleClass> peopleClasses = this.compositeDao.getPeopleClassQuerier().findAllPeopleClasses(start, limit, queryCondition, sort, dir, recordCound);
		String json = constructPaggingRecordsJson(start,limit,recordCound.value,JSONHelper.basicObjectToJson(peopleClasses));

		renderJSON(response,json);
	}

 
	private void parseParameters(HttpServletRequest request) {
        System.out.println("Begin Parse Parameters...");
        Map<String,String[]> parameters = request.getParameterMap();
        Iterator it = parameters.keySet().iterator();
        while(it.hasNext()) {
            Object key = it.next();
            System.out.println(key+" --> "+parameters.get(key)[0]);
        }
    }

	private String[] parseQueryCondition(HttpServletRequest request) {
		String _queryCondition = request.getParameter("queryCondition");
		if( _queryCondition!=null ){
			return _queryCondition.split(",");
		}
		return null;
	}
	
	private String constructPaggingRecordsJson(int start,int limit,int recordCount,String lstJson){
		StringBuffer json=new StringBuffer();
		json.append("{success:true, start:").append(start).append(",limit:").append(limit).append(",totalProperty:").append(recordCount).append(",root:").append(lstJson).append("}");
		return json.toString();
	}
	
	protected void renderText(HttpServletResponse response, String text) throws IOException {
		render(response, text, "text/plain;charset=UTF-8");
	}

	protected void renderHtml(HttpServletResponse response, String text) throws IOException {
		render(response, text, "text/html;charset=UTF-8");
	}

	protected void renderXML(HttpServletResponse response, String text) throws IOException {
		render(response, text, "text/xml;charset=gb2312");
	}

	protected void renderJSON(HttpServletResponse response, String text) throws IOException { 
		render(response, text, "text/x-json;charset=UTF-8");
	}
	
	protected void render(HttpServletResponse response, String text, String contentType) throws IOException {
		response.setContentType(contentType);
		response.getWriter().write(text);
	}
	
	public CompositeDao getCompositeDao() {
		return compositeDao;
	}

	public void setCompositeDao(CompositeDao compositeDao) {
		this.compositeDao = compositeDao;
	}
}
