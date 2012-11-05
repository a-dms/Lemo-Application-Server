package de.lemo.apps.pages.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ajax.MultiZoneUpdate;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Retain;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.DateField;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONLiteral;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.util.EnumSelectModel;
import org.apache.tapestry5.util.EnumValueEncoder;
import org.slf4j.Logger;

import se.unbound.tapestry.breadcrumbs.BreadCrumb;
import se.unbound.tapestry.breadcrumbs.BreadCrumbInfo;

import de.lemo.apps.application.AnalysisWorker;
import de.lemo.apps.application.DateWorker;
import de.lemo.apps.application.UserWorker;
import de.lemo.apps.components.JqPlotBar;
import de.lemo.apps.entities.Course;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.restws.client.Analysis;
import de.lemo.apps.restws.client.Initialisation;
import de.lemo.apps.restws.entities.EResourceType;
import de.lemo.apps.restws.entities.ResourceRequestInfo;
import de.lemo.apps.restws.entities.ResultListLongObject;
import de.lemo.apps.restws.entities.ResultListRRITypes;
import de.lemo.apps.restws.entities.ResultListResourceRequestInfo;
import de.lemo.apps.services.internal.jqplot.TextValueDataItem;
import de.lemo.apps.services.internal.jqplot.XYDateDataItem;


@RequiresAuthentication
@BreadCrumb(titleKey="visualizationTitle")
public class VisualizationLO {

	@Property
	private BreadCrumbInfo breadCrumb;
	
	@Inject 
	private BeanModelSource beanModelSource;
	
	@Inject
	private Request request;
	
	@Inject
    private Logger logger;
	
	@Inject 
	private DateWorker dateWorker;
	
	@Inject
	private ComponentResources componentResources;
	
	@Inject
	private Messages messages;
	
	@Inject
	private TypeCoercer coercer;
	
	@Inject
	private Locale currentlocale;
	
	@Inject
	private JavaScriptSupport jsSupport;
	
	@Inject 
	private UserWorker userWorker;
	
	@Inject
	private Initialisation init;
	
	@Inject
	private AnalysisWorker analysisWorker;
	
	@Inject
	private Analysis analysis;
	
	@InjectPage
	VisualizationLO visPage;
	
	@Inject 
	private CourseDAO courseDAO;
	
//	@Inject
//    private Block chart;
	
	@InjectComponent
	private Zone formZone;
				//chartZone;
	
	@Component(id = "optionForm")
	private Form optionForm;
	
	@Component(parameters = {"dataItems=FirstQuestionDataItems"})
    private JqPlotBar chart1;
	
	@Property
	@Persist
	private List<EResourceType> activities;
	
	@Property
	//@Persist
    private Course course;
	
	@Property
	//@Persist
	private Long courseId;
	
	@Property
	private JSONObject paramsZone;
	
	@Component(id = "beginDate")
	private DateField beginDateField;

	@Component(id = "endDate")
	private DateField endDateField;
	
	@Persist
	@Property
	private Date endDate;
	
	@Persist
	@Property
	private Date beginDate;
	
	//@Property
	//@Persist
	Integer val;
	
	@Property
	//@Persist
	Integer max;
	
	@Property
	//@Persist
	Integer min;
	
	@Property
	//@Persist
	Integer slideZone;
	
	@Property
	@Persist
	Integer resolution;
	
	@Property
	//@Persist
	Integer activity;
	
	@Property
	@Persist
	private Boolean twentyFourhMode;
	
	@Property
    private ResourceRequestInfo resourceItem;
	
	@Persist
	private List<ResourceRequestInfo> showDetailsList;
	
	void setupRender() {
		logger.debug(" ----- Bin in Setup Render");
		if (this.twentyFourhMode == null) this.twentyFourhMode = false;
		logger.debug("SetupRender Begin --- Is24H: "+twentyFourhMode+" BeginDate:"+ beginDate+" EndDate: "+endDate+" Res: "+resolution);
		this.course = courseDAO.getCourseByDMSId(courseId);
		if(showDetailsList== null){
			showDetailsList = new ArrayList<ResourceRequestInfo>();
		}
		if(this.endDate==null) 
			this.endDate = course.getLastRequestDate();
		if(this.beginDate==null) 
			this.beginDate= course.getFirstRequestDate();
		this.resolution=(dateWorker.daysBetween(beginDate, endDate)+1);
		Calendar beginCal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();
		beginCal.setTime(beginDate);
		endCal.setTime(endDate);
		if(this.twentyFourhMode){
			endCal.setTime(beginDate);
			beginCal.add(Calendar.DAY_OF_MONTH, -1);
			endCal.add(Calendar.DAY_OF_MONTH, +1);
			beginDate=beginCal.getTime();
			endDate=endCal.getTime();
			this.resolution=(dateWorker.daysBetween(beginDate, endDate)+1)*24;
			logger.debug("SetupRender End ---Is24H: "+twentyFourhMode+" BeginDate:"+ beginDate+" EndDate: "+endDate+" Res: "+resolution);
		} else this.resolution=dateWorker.daysBetween(beginDate, endDate);
		logger.debug("SetupRender End --- Is24H: "+twentyFourhMode+" BeginDate:"+ beginDate+" EndDate: "+endDate+" Res: "+resolution);
	}
	
	/**
	 * Evaluates the Page Activation Context and checks if the current user is permitted to see the selected course. If permission could not be granted, 
	 * the user will be send back to explorer page.
	 * 
	 * @param courseId
	 * @return
	 */
	Object onActivate(Long courseId){
		
		logger.debug("--- Bin im ersten onActivate");
		logger.debug("OnActivate Begin --- Is24H: "+twentyFourhMode+" BeginDate:"+ beginDate+" EndDate: "+endDate+" Res: "+resolution);
		List<Long> allowedCourses = userWorker.getCurrentUser().getMyCourses();
		if(allowedCourses!=null && courseId!= null && allowedCourses.contains(courseId)){
			this.courseId = courseId;
			
			return true;
		} else return Explorer.class;
	}
	
	Object onActivate(){
		logger.debug("--- Bin im zweiten onActivate");
		return Explorer.class;
	}
	
	Long onPassivate(){
		Boolean is24hModeTemp = this.twentyFourhMode;
		//logger.debug("On Passivate Begin --- Is24H: "+twentyFourhMode+" BeginDate:"+ beginDate+" EndDate: "+endDate+" Res: "+resolution);
		//componentResources.discardPersistentFieldChanges();
		//logger.debug("On Passivate End --- Is24H: "+twentyFourhMode+" BeginDate:"+ beginDate+" EndDate: "+endDate+" Res: "+resolution);
		this.twentyFourhMode = is24hModeTemp;
        return courseId;
	}
	
//	 @OnEvent(org.apache.tapestry5.EventConstants.PROGRESSIVE_DISPLAY) 
//	 public Object getActiveChart()
//	    {
//	        return chart;
//	    }
	
	@Inject
	private JavaScriptSupport javascriptSupport;

	
//	Object onProgressiveDisplayFromShowChart(){
//		//getFirstQuestionDataItems();
//		
//		try {  
//            Thread.sleep(2000);  
//        } catch (InterruptedException e) {  
//              
//            e.printStackTrace();  
//        }  
//        
//		return chart;
//	}
	
	@AfterRender
    void afterRender() {
		JSONObject spec = new JSONObject();
        javascriptSupport.addInitializerCall("jqPlotChart",spec);
	}
	
	void onPrepareFromOptionForm(){
		
		logger.debug(" ----- Bin in On Prepare");
		logger.debug("On Prepare Begin --- Is24H: "+twentyFourhMode+" BeginDate:"+ beginDate+" EndDate: "+endDate+" Res: "+resolution);
		
		this.course = courseDAO.getCourseByDMSId(courseId);
	}
	
	void onSuccessFromOptionForm() {
		logger.debug(" ----- Bin in On Success");
		logger.debug("On Success Begin --- Is24H: "+twentyFourhMode+" BeginDate:"+ beginDate+" EndDate: "+endDate+" Res: "+resolution);
		//setupRender();
		//return this;//request.isXHR() ? formZone.getBody() : null;
	}
	

	
	Object onActionFromUpdateTimeMode() {
		
		this.course = courseDAO.getCourseByDMSId(courseId);
		if(this.twentyFourhMode==null){
			this.twentyFourhMode = true;
			}
			else if (!this.twentyFourhMode){
				this.twentyFourhMode=!this.twentyFourhMode;
			}else {
				this.beginDate= course.getFirstRequestDate();
				this.endDate = course.getLastRequestDate();
				this.twentyFourhMode=!this.twentyFourhMode;
			}
	  	return formZone;															 
	 }
	
	
	@Persist
	private Long[] resLongList3;
	
	
	Object onActionFromShowDetails(Long resourceId, String resourceType) {
		
		this.course = courseDAO.getCourseByDMSId(courseId);
		int k= 0;
		if (this.resolution == null)
			this.resolution=(dateWorker.daysBetween(beginDate, endDate)+1);
		Long[] resLongList2 = new Long[this.resolution+1];
		while (k < resLongList2.length) {
			resLongList2[k] = 0L;
			k++;
		}
		// Setze KurzID als Identifier
		resLongList2[0]=courseId;
		
		logger.debug("Bin in OnActionFromShowDetails ---- Hole ResourceDetailList ");
		List<ResourceRequestInfo> resourceTypeDetailList = getResourceDetails().getResultListByResourceType(EResourceType.valueOf(resourceType.toUpperCase()));
		int i = 0;
		logger.debug("Bin in OnActionFromShowDetails ---- Hole ResourceDetailList .... Fertig");
		if(resourceTypeDetailList!=null){
			logger.debug("Bin in OnActionFromShowDetails ---- Size: "+resourceTypeDetailList.size());
		}else logger.debug("Bin in OnActionFromShowDetails ---- ResourceTypeDetails List ist null");
		while (i < resourceTypeDetailList.size()) {
			logger.debug("Looking for Ressource "+resourceTypeDetailList.get(i).getResourcetype()+ " ID aktuell : "+resourceTypeDetailList.get(i).getId()+ " ID gesucht : "+resourceId+" ---- Resolution: "+resolution+" Index:" +i);
			if(resourceTypeDetailList.get(i).getId().equals(resourceId)){
				logger.debug("Ressource "+resourceId+" found ---- Resolution: "+resolution+" Index: "+i+" ResSlot: "+resourceTypeDetailList.get(i).getResolutionSlot().intValue()+" Value: "+resourceTypeDetailList.get(i).getRequests());
				resLongList2[resourceTypeDetailList.get(i).getResolutionSlot().intValue()+1] = resourceTypeDetailList.get(i).getRequests();
				
			}
			
			i++;
		}
		logger.debug("Called Show Details with ResId: "+resourceId+ " --- ResType: "+resourceType);
		this.resLongList3 = resLongList2;
		return formZone;
	}
	
	
	/**
	 * @return Returns a JSON Option Array with all parameters for the datepicker component
	 */
	public JSONLiteral getDatePickerParams(){
		return dateWorker.getDatePickerParams();
	}
	
	
    
    void cleanupRender() {
		optionForm.clearErrors();
		// Clear the flash-persisted fields to prevent anomalies in onActivate when we hit refresh on page or browser button
		this.courseId = null;
		this.course = null;
		this.activities = null;
		
	}
    
    /**
     *  Value Encoder for activity multi-select component
     */
    @Property(write=false)
    private final ValueEncoder<EResourceType> activityEncoder = new EnumValueEncoder<EResourceType>(coercer, EResourceType.class);
    
    /**
     *  Select Model for activity multi-select component
     */
    @Property(write=false)
    private final SelectModel activityModel = new EnumSelectModel(EResourceType.class, messages);
	
	@Property(write=false)
	@Retain
	private BeanModel resourceGridModel;
    {
    	resourceGridModel = beanModelSource.createDisplayModel(ResourceRequestInfo.class, componentResources.getMessages());
    	resourceGridModel.include("resourcetype","title","requests");
    	resourceGridModel.add("show",null);
    	    	
    }
    
    
    
    /**
	 * Gibt das Datum in der aktuell vom Nutzer gewaehlten Locale Einstellung aus.
	 * @param inputDate
	 * @return Ein Objekt vom Typ String
	 */
	public String getLocalizedDate(Date inputDate) {
		SimpleDateFormat df_date = new SimpleDateFormat( "MMM dd, yyyy", currentlocale );
		return df_date.format(inputDate);
	}
	
	public String getFirstRequestDate() {
		return getLocalizedDate(this.course.getFirstRequestDate());
	}
	
	public String getLastRequestDate() {
		return getLocalizedDate(this.course.getLastRequestDate());
	}
    
    public String getResourceTypeName(){
    	if(this.resourceItem!=null && this.resourceItem.getResourcetype() != "")
    		return messages.get("EResourceType."+this.resourceItem.getResourcetype());
    	else return messages.get("EResourceType.UNKNOWN");
    }
    
    @Cached
    public List<ResourceRequestInfo> getResourceList(){
    	this.course = courseDAO.getCourseByDMSId(courseId);
		
		List<ResourceRequestInfo> resultList;
		
		if(activities!=null && activities.size()>=1){
			logger.debug("Starting Extended Analysis - Including LearnbObject Selection ...  ");
			resultList =  analysisWorker.usageAnalysisExtended(this.course, beginDate, endDate, activities);
		}else {
			logger.debug("Starting Extended Analysis - Including ALL LearnObjects ....");
			resultList =  analysisWorker.usageAnalysisExtended(this.course, beginDate, endDate, null);
		}
		logger.debug("ExtendedAnalysisWorker Results: "+resultList);
		
    	return resultList;
    }
    
    @Cached
    public ResultListRRITypes getResourceDetails(){
    	ResultListRRITypes rri;
		 if(activities!=null && activities.size()>=1)
			rri =  analysisWorker.usageAnalysisExtendedDetails(this.course, beginDate, endDate, resolution, activities);
		else rri =  analysisWorker.usageAnalysisExtendedDetails(this.course, beginDate, endDate, resolution, null);
		
		return rri;	
    }
    
    @Cached
    public List getFirstQuestionDataItems(){
		List<List<TextValueDataItem>> dataList = CollectionFactory.newList();
        List<TextValueDataItem> list1 = CollectionFactory.newList();
        List<TextValueDataItem> list2 = CollectionFactory.newList();
        if(courseId!=null){
        	Long endStamp=0L;
        	Long beginStamp=0L;
        	if(endDate!=null){
        		endStamp = new Long(endDate.getTime()/1000);
        	} //else endtime= 1334447632L;
	        
        	if(beginDate!=null){
        		beginStamp = new Long(beginDate.getTime()/1000);
        	} //else starttime = 1308968800L;
        	
			if (this.resolution == null || this.resolution < 10 )
				this.resolution = 30;
			List<Long> roles = new ArrayList<Long>();
			List<Long> courses = new ArrayList<Long>();
			courses.add(courseId);
			
			//calling dm-server
			for (int i=0;i<courses.size();i++){
				logger.debug("Courses: "+courses.get(i));
			}
			logger.debug("Starttime: "+beginStamp+ " Endtime: "+endStamp+ " Resolution: "+resolution);
			List<ResourceRequestInfo> results = analysisWorker.learningObjectUsage(this.course, beginDate, endDate, activities);
			
			
			JSONArray graphParentArray = new JSONArray();
	        JSONObject graphDataObject = new JSONObject();
	        JSONArray graphDataValues = new JSONArray();
	       
	        if(results!= null && results.size() > 0)
		        for(Integer j=0 ;j<results.size();j++){
		        	JSONObject graphValue = new JSONObject();
		
		        	graphValue.put("x",new JSONLiteral("'"+results.get(j).getTitle()+"'"));
		        	graphValue.put("y",new JSONLiteral(results.get(j).getRequests().toString()));
		        	
		        	graphDataValues.put(graphValue);
		        }
	        
	        graphDataObject.put("values", graphDataValues);
	        graphDataObject.put("key","Requests");
			
	        JSONObject graphDataObject2 = new JSONObject();
	        JSONArray graphDataValues2 = new JSONArray();
	       
	        
	        if(results!= null && results.size() > 0)
		        for (Integer i = 0;i<results.size();i++){
		        	JSONObject graphValue2 = new JSONObject();
		     
		        	graphValue2.put("x",new JSONLiteral("'"+results.get(i).getTitle()+"'"));
		        	graphValue2.put("y",new JSONLiteral(results.get(i).getUsers().toString()));
		        	
		        	graphDataValues2.put(graphValue2);
		        }
	        
	        graphDataObject2.put("values", graphDataValues2);
	        graphDataObject2.put("key","User");
			
	        
	        graphParentArray.put(graphDataObject);
	        graphParentArray.put(graphDataObject2);
	        
	        logger.debug(graphParentArray.toString());
	        
			
			Calendar beginCal = Calendar.getInstance();
			beginCal.setTime(beginDate);
			logger.debug("BeginDate: "+beginDate);
			//checking if result size matches resolution 
//			ResultListRRITypes rri = getResourceDetails();
//			List<ResourceRequestInfo> resInfoList = rri.getResourcesRRI();
//			int i = 0;
//			int k= 0;
//			Long[] resLongList2 = new Long[resolution];
//			while (k < resLongList2.length) {
//				resLongList2[k] = 0L;
//				k++;
//			}
//			//List<Long> resLongList = new ArrayList<Long>();
//			while (i < resInfoList.size()) {
//				logger.debug("Looking for Ressource 46267 ---- Resolution: "+resolution+" Index:" +i);
//				if(resInfoList.get(i).getId() == 46267){
//					logger.debug("Ressource 46267 found ---- Resolution: "+resolution+" Index: "+i+" ResSlot: "+resInfoList.get(i).getResolutionSlot().intValue()+" Value: "+resInfoList.get(i).getRequests());
//					resLongList2[resInfoList.get(i).getResolutionSlot().intValue()] = resInfoList.get(i).getRequests();
//					
//				}
//					//else resLongList.add(resInfoList.get(i).getResolutionSlot().intValue(), 0L);
//				i++;
//			}
//			
			
			if(results!= null && results.size() > 0)
	        for(int j=0 ;j<results.size();j++){
	        	logger.debug("Building Chart JSON ---- Index:" +j);
//	        	if(this.twentyFourhMode)
//	        		beginCal.add(Calendar.HOUR_OF_DAY, 1);
//	        		else beginCal.add(Calendar.DAY_OF_MONTH, 1);
	        	list1.add(new TextValueDataItem(results.get(j).getTitle() , results.get(j).getRequests()));
	        	list2.add(new TextValueDataItem(results.get(j).getTitle() , results.get(j).getUsers()));
	        	//logger.debug("CourseID: "+this.courseId+" ResArrayID: "+resLongList3[0]+" Resolution: "+this.resolution+" ArrayLength: "+resLongList3.length);
//	        	if(resLongList3!=null && resolution.equals(resLongList3.length-1) && (this.courseId.compareTo(resLongList3[0]) == 0)){
//	        		logger.debug("Bin drin CourseID: "+this.courseId);
//	        		//list2.add(new XYDateDataItem(beginCal.getTime() , resLongList3[j+1]));
//	        	}
	        }
    	}
        dataList.add(list1);
        dataList.add(list2);
        return dataList;
	}

}