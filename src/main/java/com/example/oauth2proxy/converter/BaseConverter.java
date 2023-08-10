package com.example.oauth2proxy.converter;

import java.io.Serializable;

public interface BaseConverter<T1 extends Serializable, T2 extends Serializable> {

    T2 transform(T1 entity);

    T1 reverseTransform(T2 entity);
}