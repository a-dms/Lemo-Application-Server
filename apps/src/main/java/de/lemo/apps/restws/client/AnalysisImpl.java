/**
 * 
 */
package de.lemo.apps.restws.client;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.tapestry5.json.JSONObject;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ProxyFactory;

import de.lemo.apps.restws.entities.EResourceType;
import de.lemo.apps.restws.entities.ResultListLongObject;
import de.lemo.apps.restws.entities.ResultListRRITypes;
import de.lemo.apps.restws.entities.ResultListResourceRequestInfo;
import de.lemo.apps.restws.proxies.questions.QActivityResourceType;
import de.lemo.apps.restws.proxies.questions.QActivityResourceTypeResolution;
import de.lemo.apps.restws.proxies.questions.QCourseActivity;
import de.lemo.apps.restws.proxies.questions.QCourseUserPaths;
import de.lemo.apps.restws.proxies.service.ServiceStartTime;

/**
 * @author johndoe
 *
 */
public class AnalysisImpl implements Analysis{
	
	private static final String SERVICE_STARTTIME_URL = "http://localhost:4443/starttime";
    private static final String QUESTIONS_BASE_URL = "http://localhost:4443/questions";


    @Override
    public ResultListLongObject computeQ1(List<Long> courses, List<Long> roles, Long starttime, Long endtime,  int resolution) {
		//Create resource delegate
		//logger.info("Getting Server Starttime");
		try {
			ClientRequest request = new ClientRequest(SERVICE_STARTTIME_URL);
			
			ClientResponse<ServiceStartTime> response;
			
				response = request.get(ServiceStartTime.class);
			
				
			
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
			}
			
			QCourseActivity qcourseActivity = ProxyFactory.create(QCourseActivity.class,
			QUESTIONS_BASE_URL);
			if (qcourseActivity != null){

				ResultListLongObject result = qcourseActivity.compute(courses, roles, starttime, endtime, resolution);
//				if(result!=null && result.getElements()!=null){
//					ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
//					List<Long> resultList = new ArrayList<Long>();
//					for (int i=0; i<result.getElements().size();i++)  { 
//						Long value = mapper.readValue(result.getElements().get(i).toString(), Long.class);
//						resultList.add(i, value);
//					}
//					 
//					return new ResultList(resultList);
//				}
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
    public ResultListResourceRequestInfo computeQ1Extended(List<Long> courses, Long startTime, Long endTime,  List<String> resourceTypes) {

		try {
			ClientRequest request = new ClientRequest(SERVICE_STARTTIME_URL);
			
			ClientResponse<ServiceStartTime> response;
			
				response = request.get(ServiceStartTime.class);
			
				
			
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
			}
			
			QActivityResourceType qActivityResourceType = ProxyFactory.create(QActivityResourceType.class,
			QUESTIONS_BASE_URL);
			if (qActivityResourceType != null){

				ResultListResourceRequestInfo result = qActivityResourceType.compute(courses, startTime, endTime, resourceTypes);

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
		return new ResultListResourceRequestInfo();
	}
	
	@Override
    public ResultListRRITypes computeQ1ExtendedDetails(List<Long> courses, Long startTime, Long endTime, Long resolution, List<String> resourceTypes) {

		try {
			ClientRequest request = new ClientRequest(SERVICE_STARTTIME_URL);
			
			ClientResponse<ServiceStartTime> response;
			
				response = request.get(ServiceStartTime.class);
			
				
			
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
					+ response.getStatus());
			}
			
			QActivityResourceTypeResolution qActivityResourceType = ProxyFactory.create(QActivityResourceTypeResolution.class,
			QUESTIONS_BASE_URL);
			if (qActivityResourceType != null){

				ResultListRRITypes result = qActivityResourceType.compute(courses, startTime, endTime, resolution, resourceTypes);

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
		return new ResultListRRITypes();
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
        System.out.println("Gebe leere Resultlist zurück");
        return "{}";
    }

}
