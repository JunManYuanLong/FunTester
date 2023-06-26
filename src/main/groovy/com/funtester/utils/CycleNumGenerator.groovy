package com.funtester.utils

import com.funtester.frame.SourceCode

class CycleNumGenerator {

    int start

    int t

    int min

    int max

    def step

    def middle

    static CycleNumGenerator newGenerator(int t, min, max) {
        def generator = new CycleNumGenerator()
        generator.t = t
        generator.min = min
        generator.max = max
        generator.step = (max - min) / t * 2
        generator.start = SourceCode.getMark()
        generator.middle = t / 2
        generator
    }

    double getNum() {
        def count = (SourceCode.getMark() - start) % t
        if (count < middle) {
            min + step * count
        } else {
            count -= middle
            max - step * count
        }
    }
}
