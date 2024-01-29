package br.com.joaoluisberute.todolist.Utils;

import java.beans.PropertyDescriptor;

public interface Utils {
    
    public String[] getNullPropertyNames( Object source ) {

        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        PropertyDescriptor[] descriptors = wrappedSource.getPropertyDescriptors();
        String[] src =  Stream.of( descriptors ).map( PropertyDescriptor::getName ).filter( propertyName -> wrappedSource.getPropertyValue( propertyName ) == null ).toArray( String[]::new );

        return src;
    }
}
