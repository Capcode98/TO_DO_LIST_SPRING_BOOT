package br.com.joaoluisberute.todolist.Utils;

import java.beans.PropertyDescriptor;
import org.springframework.beans.BeanUtils;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.lang.NonNull;


public interface Utils {

    //Primeira função chamada
    public static void copyNonNullProperties(@NonNull Object source, @NonNull Object target) {

        String[] result = getNullPropertyNames(source);
        BeanUtils.copyProperties(source, target, result);
    }
    
    //Segunda função chamada
    public static String[] getNullPropertyNames(@NonNull Object source ) {

        final BeanWrapper src = new BeanWrapperImpl(source);

        PropertyDescriptor[] descriptors = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();

        for (PropertyDescriptor pd : descriptors) {
            
            Object value = src.getPropertyValue(pd.getName());

            if(value == null){

                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];

        return emptyNames.toArray(result);
    }
}
