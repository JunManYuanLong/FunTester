package com.funtester.utils

import com.funtester.frame.SourceCode

class CycleNumGenerator {

    List<Double> values = new ArrayList<>()

    int start

    int t

    static CycleNumGenerator newGenerator(int t, min, max) {
        if (t % 2 == 1) t += 1
        def generator = new CycleNumGenerator()
        generator.t = t
        def step = (max - min) / t * 2
        generator.start = SourceCode.getMark()
        def middle = t / 2
        for (i in 0..<t) {
            if (i < middle) {
                generator.values << min + step * i
            } else {
                generator.values << max - step * (i - middle)
            }
        }
        generator
    }

    double getNum() {
        def count = (SourceCode.getMark() - start) % t
        values.get(count)
    }

}
