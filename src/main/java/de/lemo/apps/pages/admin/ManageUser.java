
/**
 * File ManageUser.java
 * Date Mar 14, 2013
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.apps.pages.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.slf4j.Logger;

import se.unbound.tapestry.breadcrumbs.BreadCrumbInfo;
import de.lemo.apps.entities.Course;
import de.lemo.apps.entities.User;
import de.lemo.apps.exceptions.RestServiceCommunicationException;
import de.lemo.apps.integration.CourseDAO;
import de.lemo.apps.integration.UserDAO;
import de.lemo.apps.pages.data.DashboardAdmin;
import de.lemo.apps.restws.client.Information;
import de.lemo.apps.restws.client.Initialisation;
import de.lemo.apps.restws.entities.CourseObject;
import de.lemo.apps.restws.entities.ResultListCourseObject;


/**
 * @author Andreas Pursian
 *
 */
@RequiresAuthentication
public class ManageUser {
	
	@Inject
	private Initialisation init;
	
	@Inject
	private Information info;
	
	@Inject
	private Logger logger;
	
	@Inject
	private Messages messages;

	@Inject
	@Path("../../images/icons/glyphicons_019_cogwheel.png")
	@Property
	private Asset wheel;

	@Inject
	private CourseDAO courseDAO;

	@Inject
	private UserDAO userDAO;

	@Property
	private BreadCrumbInfo breadCrumb;

	@Component(id = "accountform")
	private Form form;
	
	@Property
	@Persist
	private User userItem;
	
	@Property
	@Persist
	private Course courseItem;
	
	@Property
	@Persist
	private List<Course> searchCoursesList;
	
	@Property
	private String courseString;	
	
	@Component
	private Zone formZone;
	 
	@Inject
	private Request request;

	@Component
	private Form ajaxForm;
	
    Boolean onActivate(User user){
    	this.userItem = user;
    	searchCoursesList = null;
    	return false;
    }
    
//    Object onActivate(){
//    	if (this.userItem == null) {
//    		return DashboardAdmin.class; 
//    	} else return this;
//    }
    
	
	public List<Course> getUserCourses() {
		return this.courseDAO.findAllByOwner(userItem, false);
	}
	
	
	public User getUser(){
		return this.userItem;
	}
	
	Object onSuccess() {
		this.userDAO.update(this.userItem);
		return this;
	}
	
	public String getDeleteString() {
		return messages.format("sureToDelete", userItem.getFullname());
	}
	
	Object onActionFromDelete() {
	    this.userDAO.remove(this.userItem);
	    return DashboardAdmin.class;
	}
	
	Object onSelectedFromSearch() {
		if(courseString!=null) {
			searchCoursesList = new ArrayList<Course>();
			ResultListCourseObject rs = new ResultListCourseObject();
			courseString = courseString.trim();
			courseString = courseString.replaceAll(" +"," ");
			
			if (courseString.matches("[0-9 ]+")) {
				String[] idList = courseString.split(" ", 100);
				ArrayList<Long> lA = new ArrayList<Long>();
				Long l = -1L;
				for (String idString : idList) {
					logger.debug("Search String is : "+idString);
					l = Long.parseLong(idString);
					lA.add(l);
				}
				try {
					rs = this.init.getCoursesDetails(lA);
				} catch (RestServiceCommunicationException e) {
					e.printStackTrace();
				}
			}
			else {
				logger.debug("Search String is NOT Numeric: "+ courseString);
				try {
					rs = info.getCoursesByTitle(courseString, 100L, 0L);
				} catch (RestServiceCommunicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if (rs!=null) {
				logger.debug("Result object is NOT null");
				List<CourseObject> cd = rs.getElements();
				if (cd!=null) {
					logger.debug("Course result List is NOT null ... results: "+ cd.size());
					for (CourseObject co : cd) {
						logger.debug("Adding search reult to List ... "+ co.getDescription());
						searchCoursesList.add(new Course(co));
					}
				} else {logger.debug("Course result List is null");}
			} else {logger.debug("Result object is null");}
		}
		return request.isXHR() ? formZone.getBody() : null;
	}
	
	public List<Course> getSearchCourseList() {
		return this.searchCoursesList;
	}
	
	public boolean getSearchCourses() {
		if (searchCoursesList!=null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public Object onActionFromDeleteCourse(Long courseID) {
		List<Course> courseList =  this.courseDAO.findAllByOwner(userItem, false);
		int index=0;
		for (int i=0; i<courseList.size();i++) {
			if (courseList.get(i).getId()==courseID) {
				index=i;
				break;
			}
		}
		courseList.remove(index);
		userItem.setMyCourses(courseList);
		this.userDAO.update(userItem);
		return this;
	}
	
	public Object onActionFromAdd(Long courseID) {
		List<Course> courseList = this.courseDAO.findAll();
		List<Course> userCourses = this.courseDAO.findAllByOwner(userItem, false);
		int index=0;
		boolean exists = false;
		for (int i=0; i<courseList.size();i++) {
			if (courseList.get(i).getId()==courseID) {
				index=i;
				exists=true;
				break;
			}
		}
		if(exists) {
			userCourses.add(courseList.get(index));
		}
		else {
			CourseObject co = new CourseObject();
			try {
				co = this.init.getCourseDetails(courseID);
			} catch (RestServiceCommunicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return this;
			}
			userCourses.add(new Course(co));
		}
		userItem.setMyCourses(userCourses);
		this.userDAO.update(userItem);
		return this;
	}

}
