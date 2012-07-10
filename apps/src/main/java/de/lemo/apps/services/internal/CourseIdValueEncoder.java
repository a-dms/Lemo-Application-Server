package de.lemo.apps.services.internal;

import org.apache.tapestry5.ValueEncoder;

import de.lemo.apps.entities.Course;
import de.lemo.apps.integration.CourseDAO;

public interface CourseIdValueEncoder {

    public ValueEncoder<Course> create(Class<Course> arg0);

    public ValueEncoder<Course> create(Class<Course> type, CourseDAO courseDAO);

}
