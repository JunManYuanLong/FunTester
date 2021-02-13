package com.FunTester.mockito.practise;

import com.funtester.frame.SourceCode;
import com.funtester.httpclient.FunRequest;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.mockito.AdditionalAnswers.returnsSecondArg;
import static org.mockito.Mockito.*;

public class DemoJ extends SourceCode {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Captor
    private ArgumentCaptor<List<String>> captor;

    @Mock
    List<Integer> listsss;

    @Test
    public final void shouldContainCertainListItem() {
        List<String> asList = Arrays.asList("someElement_test", "someElement");
        final List<String> mockedList = mock(List.class);
        mockedList.addAll(asList);
        verify(mockedList).addAll(captor.capture());
        final List<String> capturedArgument = captor.getValue();
        assertThat(capturedArgument, hasItem("someElement"));
    }

    @Test
    public final void answerTest() {
        DemoJ mock = mock(DemoJ.class);
        // with doAnswer():
        doAnswer(returnsSecondArg()).when(mock).ds(anyInt(), anyInt());
        // with thenAnswer():
//        when(list.add(anyString())).thenAnswer(returnsFirstArg());
        // with then() alias:
//        when(list.add(anyString())).then(returnsFirstArg());
        int ds = mock.ds(3, 2);
        Assert.assertTrue(ds == 2);
    }

    @Test
    public final void answerTest2() {
        DemoJ mock = mock(DemoJ.class);
        // with doAnswer():
        when(mock.ds(anyInt(), anyInt())).thenAnswer(i -> {
            Object argument = i.getArguments();
            try {
                if (Integer.valueOf(argument.toString()) > 3) return 5;
                return 2;
            } catch (NumberFormatException e) {
                return 0;
            }
        });
        int ds = mock.ds(53, 2);
        Assert.assertTrue(ds == 0);
    }

    public int ds(int i, int n) {
        return i + n;
    }


    public int sss() {
        return SourceCode.getMark();
    }

    @Test
    public void testsse() {
        DemoJ mock = mock(DemoJ.class);
        when(mock.ds(anyInt(), anyInt())).thenReturn(12);
        int ds = mock.ds(3, 2);
        Assert.assertTrue(ds == 12);
    }

    @Test
    public void tetees() {
        when(listsss.get(anyInt())).thenReturn(3);
        Assert.assertTrue(3 == listsss.get(3));
    }


    @Mock
    HashMap<Object, Object> json;//无法mock  final类

    @Test
    public void stes() {
        doReturn(3).when(json).size();
        doReturn("FunTester").when(json).get(any());
        Assert.assertTrue(3 == json.size());
        Assert.assertEquals("FunTester", json.get(3));
    }

    @Mock
    FunRequest funRequest;

    @Test
    public void sdfs() {
        doReturn("/login").when(funRequest).getApiName();
        doReturn(null).when(funRequest).getResponse();
        Assert.assertNull(funRequest.getResponse());
        Assert.assertEquals("/login", funRequest.getApiName());
    }


}
