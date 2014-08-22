	protected void get[Class](HttpServletRequest request, HttpServletResponse response) throws IOException{

		long id = Long.valueOf(request.getParameter("id"));
		
		[Class] [class] = this.compositeDao.get[Class]Dao().get[Class](id);
		String json = JSONHelper.basicObjectToJson([class]);

		renderJSON(response,json);
	}
    protected void findAll[Classes](HttpServletRequest request, HttpServletResponse response) throws IOException{

		int start = Integer.valueOf(request.getParameter("start"));
		int limit = Integer.valueOf(request.getParameter("limit"));
		String sort = request.getParameter("sort");
		String dir = request.getParameter("dir");
		String[]queryCondition = parseQueryCondition(request);
		RecordCound recordCound = new RecordCound();
		
		List<[Class]> [classes] = this.compositeDao.get[Class]Querier().findAll[Classes](start, limit, queryCondition, sort, dir, recordCound);
		String json = constructPaggingRecordsJson(start,limit,recordCound.value,JSONHelper.basicObjectToJson([classes]));

		renderJSON(response,json);
	}
	protected void saveOrUpdate[Class](HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		String json = request.getParameter("json");
		[Class] object = ([Class]) JSONHelper.basicJsonToObject(json, [Class].class);
		compositeDao.get[Class]Dao().saveOrUpdate[Class](object);
	}
	protected void delete[Class](HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		String json = request.getParameter("json");
		List<[Class]> object = (List<[Class]>) JSONHelper.basicJsonToObject(json, new TypeReference<List<[Class]>>(){});
		compositeDao.get[Class]Dao().deleteAll[Classes](object);
	}