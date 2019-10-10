package com.FunTester.mockito.practise

import org.apache.http.client.methods.HttpRequestBase
import spock.lang.Shared
import spock.lang.Specification

import static org.mockito.Mockito.*

class Demo extends Specification {

    @Shared
    HttpRequestBase httpRequestBase = mock(HttpRequestBase.class)


    def "dsafsf"() {
        given:
        List mockedList = mock(List.class);
        mockedList.add("one");
        mockedList.add("two");
        mockedList.add("three times");
        mockedList.add("three times");
        mockedList.add("three times");
        when(mockedList.size()).thenReturn(5);
        mockedList.add("3")
        mockedList.add("3")
        mockedList.add("3")
        mockedList.add("3")

        expect:
        5 == mockedList.size()
        false == verify(mockedList, atLeastOnce()).add("one")

    }

}
