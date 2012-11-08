/**
 * 
 */
package de.lemo.apps.restws.client;

import static de.lemo.apps.restws.client.InitialisationImpl.DMS_BASE_URL;
import static de.lemo.apps.restws.client.InitialisationImpl.SERVICE_STARTTIME_URL;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ProxyFactory;
import org.slf4j.Logger;

import de.lemo.apps.restws.entities.ResultListLongObject;
import de.lemo.apps.restws.entities.ResultListRRITypes;
import de.lemo.apps.restws.entities.ResultListResourceRequestInfo;
import de.lemo.apps.restws.proxies.questions.QActivityResourceType;
import de.lemo.apps.restws.proxies.questions.QActivityResourceTypeResolution;
import de.lemo.apps.restws.proxies.questions.QActivityResourceTypeString;
import de.lemo.apps.restws.proxies.questions.QCourseActivity;
import de.lemo.apps.restws.proxies.questions.QCourseActivityString;
import de.lemo.apps.restws.proxies.questions.QCourseUserPaths;
import de.lemo.apps.restws.proxies.questions.QCourseUsers;
import de.lemo.apps.restws.proxies.questions.QFrequentPathsBIDE;
import de.lemo.apps.restws.proxies.questions.QLearningObjectUsage;
import de.lemo.apps.restws.proxies.questions.QUserPathAnalysis;
import de.lemo.apps.restws.proxies.service.ServiceStartTime;

/**
 * @author johndoe
 * 
 */
public class AnalysisImpl implements Analysis {

    private static final String QUESTIONS_BASE_URL = DMS_BASE_URL + "/questions";
	@Inject
    private Logger logger;

    @Override
    public ResultListLongObject computeQ1(List<Long> courses, List<Long> roles, List<Long> users,  Long starttime, Long endtime,
            int resolution, List<String> resourceTypes) {
        // Create resource delegate
        // logger.info("Getting Server Starttime");
        try {
            ClientRequest request = new ClientRequest(SERVICE_STARTTIME_URL);

            ClientResponse<ServiceStartTime> response;

            response = request.get(ServiceStartTime.class);

            if(response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            QCourseActivity qcourseActivity = ProxyFactory.create(QCourseActivity.class,
                QUESTIONS_BASE_URL);
            if(qcourseActivity != null) {

                ResultListLongObject result = qcourseActivity.compute(courses, roles, users, starttime, endtime, resolution,resourceTypes);
                // if(result!=null && result.getElements()!=null){
                // ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
                // List<Long> resultList = new ArrayList<Long>();
                // for (int i=0; i<result.getElements().size();i++) {
                // Long value = mapper.readValue(result.getElements().get(i).toString(), Long.class);
                // resultList.add(i, value);
                // }
                //
                // return new ResultList(resultList);
                // }
                return result;
            }

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Gebe leere Resultlist zurück");
        return new ResultListLongObject();
    }
    
    
    @Override
    public String computeQ1JSON(List<Long> courses, List<Long> roles, Long starttime, Long endtime,
            int resolution, List<String> resourceTypes) {
        // Create resource delegate
        // logger.info("Getting Server Starttime");
        try {
            ClientRequest request = new ClientRequest(SERVICE_STARTTIME_URL);

            ClientResponse<ServiceStartTime> response;

            response = request.get(ServiceStartTime.class);

            if(response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            QCourseActivityString qcourseActivity = ProxyFactory.create(QCourseActivityString.class,
                                                                  QUESTIONS_BASE_URL);
            if(qcourseActivity != null) {

                String result = qcourseActivity.compute(courses, roles, starttime, endtime, resolution,resourceTypes);
                // if(result!=null && result.getElements()!=null){
                // ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
                // List<Long> resultList = new ArrayList<Long>();
                // for (int i=0; i<result.getElements().size();i++) {
                // Long value = mapper.readValue(result.getElements().get(i).toString(), Long.class);
                // resultList.add(i, value);
                // }
                //
                // return new ResultList(resultList);
                // }
                return result;
            }

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Gebe leere Resultlist zurück");
        return "";
    }
    
    
    

    @Override
    public ResultListResourceRequestInfo computeQ1Extended(List<Long> courses, Long startTime, Long endTime,
            List<String> resourceTypes) {

        try {
            ClientRequest request = new ClientRequest(SERVICE_STARTTIME_URL);

            ClientResponse<ServiceStartTime> response;

            response = request.get(ServiceStartTime.class);

            if(response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            QActivityResourceType qActivityResourceType = ProxyFactory.create(QActivityResourceType.class,
                QUESTIONS_BASE_URL);
            if(qActivityResourceType != null) {

                ResultListResourceRequestInfo result = qActivityResourceType.compute(courses, startTime, endTime,
                    resourceTypes);

                return result;
            }

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Gebe leere Resultlist zur�ck");
        return new ResultListResourceRequestInfo();
    }

    
    @Override
    public String computeQ1ExtendedJSON(List<Long> courses, Long startTime, Long endTime,
            List<String> resourceTypes) {

        try {
            ClientRequest request = new ClientRequest(SERVICE_STARTTIME_URL);

            ClientResponse<ServiceStartTime> response;

            response = request.get(ServiceStartTime.class);

            if(response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            QActivityResourceTypeString qActivityResourceType = ProxyFactory.create(QActivityResourceTypeString.class,
                                                                              QUESTIONS_BASE_URL);
            if(qActivityResourceType != null) {

                String result = qActivityResourceType.compute(courses, startTime, endTime,resourceTypes);

                return result;
            }

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Gebe leere Resultlist zur�ck");
        return "";
    }
    
    
    
    @Override
    public ResultListRRITypes computeQ1ExtendedDetails(List<Long> courses, Long startTime, Long endTime,
            Long resolution, List<String> resourceTypes) {

        try {
            ClientRequest request = new ClientRequest(SERVICE_STARTTIME_URL);

            ClientResponse<ServiceStartTime> response;

            response = request.get(ServiceStartTime.class);

            if(response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            QActivityResourceTypeResolution qActivityResourceType = ProxyFactory
                    .create(QActivityResourceTypeResolution.class,
                        QUESTIONS_BASE_URL);
            if(qActivityResourceType != null) {
            	
            	if(resourceTypes!=null && resourceTypes.size()>0)
    	    		for(int i=0; i<resourceTypes.size();i++){
    	    			logger.info("Course Activity Request - CA Selection: "+resourceTypes.get(i));
    	    		}
    	    	else logger.info("Course Activity Request - CA Selection: NO Items selected ");

                ResultListRRITypes result = qActivityResourceType.compute(courses, startTime, endTime, resolution,
                    resourceTypes);

                return result;
            }

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Gebe leere Resultlist zur�ck");
        return new ResultListRRITypes();
    }
    
    
    @Override
    public ResultListResourceRequestInfo computeLearningObjectUsage(List<Long> courseIds, List<Long> userIds, List<String> types, Long startTime, Long endTime) {

        try {
            ClientRequest request = new ClientRequest(SERVICE_STARTTIME_URL);
            ClientResponse<ServiceStartTime> response = request.get(ServiceStartTime.class);

            if(response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }

            QLearningObjectUsage qLOUsage = ProxyFactory.create(QLearningObjectUsage.class, QUESTIONS_BASE_URL);
            if(qLOUsage != null) {
            	if(types!=null && types.size()>0)
            		for(int i=0; i<types.size();i++){
            			logger.debug("LO Request - LO Selection: "+types.get(i));
            		}
            	else logger.debug("LO Request - LO Selection: NO Items selected ");
            	
            	ResultListResourceRequestInfo result = qLOUsage.compute(courseIds, userIds, types, startTime, endTime);
                return result;
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Gebe leere Resultlist zurueck");
        return new ResultListResourceRequestInfo();
    }

    @Override
    public ResultListLongObject computeCourseUsers(
            List<Long> courseIds,
            long startTime,
            long endTime) {
        ResultListLongObject result = null;
        QCourseUsers analysis = ProxyFactory.create(QCourseUsers.class, QUESTIONS_BASE_URL);
        if(analysis != null)
            result = analysis.compute(courseIds, startTime, endTime);
        if(result == null)
            result = new ResultListLongObject();
        return result;
    }

    @Override
    public String computeUserPathAnalysis(
            List<Long> courseIds,
            List<Long> userIds,
            List<String> types,
            boolean considerLogouts,
            Long startTime,
            Long endTime) {

        QUserPathAnalysis analysis = ProxyFactory.create(QUserPathAnalysis.class, QUESTIONS_BASE_URL);
        if(analysis != null) {
            String result = analysis.compute(courseIds, userIds, types, considerLogouts, startTime, endTime);
            System.out.println("PATH result: " + result);
            return result;
        }
        return "{}";
    }

    @Override
    public String computeCourseUserPaths(List<Long> courseIds, Long startTime, Long endTime) {

        try {
            ClientRequest request = new ClientRequest(SERVICE_STARTTIME_URL);
            ClientResponse<ServiceStartTime> response = request.get(ServiceStartTime.class);

            if(response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }

            QCourseUserPaths qUserPath = ProxyFactory.create(QCourseUserPaths.class, QUESTIONS_BASE_URL);
            if(qUserPath != null) {
                String result = qUserPath.compute(courseIds, startTime, endTime);
                return result;
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Gebe leere Resultlist zurueck");
        return "{}";
    }

    @Override
    public String computeQFrequentPathBIDE(
            List<Long> courseIds,
            List<Long> userIds,
            double minSup,
            boolean sessionWise,
            long startTime,
            long endTime) {
        System.out.println("Starte BIDE Request");
        try {
            ClientRequest request = new ClientRequest(SERVICE_STARTTIME_URL);
            ClientResponse<ServiceStartTime> response = request.get(ServiceStartTime.class);

            if(response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }

            QFrequentPathsBIDE qFrequentPath = ProxyFactory.create(QFrequentPathsBIDE.class, QUESTIONS_BASE_URL);
            if(qFrequentPath != null) {
                String result = qFrequentPath.compute(courseIds, userIds, minSup, sessionWise, startTime, endTime);
                System.out.println("BIDE result: " + result);
                return result;
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Gebe leere Resultlist zurueck");
        return "{}";
    }

}
