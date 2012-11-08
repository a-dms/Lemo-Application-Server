package de.lemo.apps.restws.client;

import java.util.List;

import de.lemo.apps.restws.entities.ResultListLongObject;
import de.lemo.apps.restws.entities.ResultListRRITypes;
import de.lemo.apps.restws.entities.ResultListResourceRequestInfo;

public interface Analysis {

    ResultListLongObject computeQ1(
            List<Long> courses,
            List<Long> roles,
            List<Long> users,
            Long starttime,
            Long endtime,
            int resolution,
            List<String> resourceTypes);
    
    String computeQ1JSON(
    		List<Long> courses, 
    		List<Long> roles, 
    		Long starttime, 
    		Long endtime,
            int resolution, 
            List<String> resourceTypes);

    ResultListResourceRequestInfo computeQ1Extended(
            List<Long> courses,
            Long startTime,
            Long endTime,
            List<String> resourceTypes);
    
    String computeQ1ExtendedJSON(
    		List<Long> courses, 
    		Long startTime, 
    		Long endTime,
            List<String> resourceTypes);

    ResultListRRITypes computeQ1ExtendedDetails(
            List<Long> courses,
            Long startTime,
            Long endTime,
            Long resolution,
            List<String> resourceTypes);
    
    ResultListResourceRequestInfo computeLearningObjectUsage(
    		List<Long> courseIds, 
    		List<Long> userIds, 
    		List<String> types, 
    		Long startTime, 
    		Long endTime);


    String computeCourseUserPaths(
            List<Long> courseIds,
            Long startTime,
            Long endTime);

    String computeUserPathAnalysis(
            List<Long> courseIds,
            List<Long> userIds,
            List<String> types,
            boolean considerLogouts,
            Long startTime,
            Long endTime);

    ResultListLongObject computeCourseUsers(
            List<Long> courseIds,
            long startTime,
            long endTime);
    
    public String computeQFrequentPathBIDE(
    		List<Long> courseIds, 
    		List<Long> userIds, 
    		double minSup, 
    		boolean sessionWise,
    		long startTime,
    		long endTime);

}
