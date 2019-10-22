package com.FunTester.mockito.practise;

import com.fun.frame.SourceCode;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SourceCode.class)
public class Static {

    @Test
    public void testDD() {
        DemoJ demoJ = new DemoJ();
        PowerMockito.mockStatic(SourceCode.class);
        PowerMockito.when(SourceCode.getMark()).thenReturn(32334);
        int mark = demoJ.sss();
        Assert.assertTrue(mark == 32334);
    }


}
