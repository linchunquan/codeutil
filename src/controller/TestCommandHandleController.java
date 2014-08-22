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
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import util.datastruct.RecordCound;

//Test command handle controller
public class TestCommandHandleController extends AbstractController{
	
    private void parseParameters(HttpServletRequest request) {
        System.out.println("Begin Parse Parameters...");
        Map<String,String[]> parameters = request.getParameterMap();
        Iterator it = parameters.keySet().iterator();
        while(it.hasNext()) {
            Object key = it.next();
            System.out.println(key+" --> "+parameters.get(key)[0]);
        }
    }
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String _cmd=request.getParameter("cmd");
		if(_cmd!=null){
		    parseParameters(request);
		}
		return null;
	}
}
