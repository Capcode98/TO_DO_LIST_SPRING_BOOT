package br.com.joaoluisberute.todolist.Utils;

import java.beans.PropertyDescriptor;
import java.util.stream.Stream;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import lombok.NonNull;


public interface Utils {
    
    public default String[] getNullPropertyNames( Object source ) {

        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        PropertyDescriptor[] descriptors = wrappedSource.getPropertyDescriptors();
        String[] src =  Stream.of( descriptors ).map( PropertyDescriptor::getName ).filter( propertyName -> wrappedSource.getPropertyValue( propertyName ) == null ).toArray( String @NonNull []::new );

        return src;
    }
}
