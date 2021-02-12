package com.FunTester.mockito.practise;

import com.funtester.frame.SourceCode;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SourceCode.class)
public class Static {

    @Test
    public void testDD() {
        DemoJ demoJ = new DemoJ();
        mockStatic(SourceCode.class);
        when(SourceCode.getMark()).thenReturn(32334);
        int mark = demoJ.sss();
        Assert.assertTrue(mark == 32334);
    }

    @Test
    public void testDS() {
        mockStatic(SourceCode.class);
        Mockito.when(SourceCode.changeStringToInt(anyString())).thenAnswer(i -> {
            return i.getArgument(0).toString().length();
        });
        int i = SourceCode.changeStringToInt("3");
        Assert.assertTrue(i == 1);
    }

    @Test
    public void testPercent() {
        mockStatic(SourceCode.class);
        when(SourceCode.getRandomInt(anyInt())).then(AdditionalAnswers.returnsFirstArg());
        int anInt = SourceCode.getRandomInt(20);
        Assert.assertTrue(anInt == 20);
    }

    @Test
    public void stet() {
        mockStatic(SourceCode.class);
        when(SourceCode.isNumber(anyString())).thenReturn(true);
        Assert.assertTrue(SourceCode.isNumber("32"));
    }

}
