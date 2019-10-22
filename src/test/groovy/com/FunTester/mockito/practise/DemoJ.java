package com.FunTester.mockito.practise;

import com.fun.frame.Output;
import com.fun.frame.SourceCode;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
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
        Output.output(ds);
    }

    @Test
    public final void answerTest2() {
        DemoJ mock = mock(DemoJ.class);
        // with doAnswer():
        when(mock.ds(anyInt(), anyInt())).thenAnswer(i -> {
            Object argument = i.getArguments();
            if (Integer.valueOf(argument.toString()) > 3) return 5;
            return 2;
        });
        int ds = mock.ds(53, 2);
        Output.output(ds);
    }

    public int ds(int i, int n) {
        return i + n;
    }


    public int sss() {
        return SourceCode.getMark();
    }

}
