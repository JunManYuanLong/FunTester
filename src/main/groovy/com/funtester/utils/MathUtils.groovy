package com.funtester.utils

import com.funtester.config.Constant
import com.funtester.frame.SourceCode
import groovy.util.logging.Log4j2

import java.util.stream.Collectors

@Log4j2
class MathUtils {

    /**
     * 标准差
     * @param numbers
     * @return
     */
    static def standardDeviation(List<? extends Number> numbers) {
        Math.sqrt(variance(numbers))
    }

    /**
     * 方差
     * @param numbers
     * @return
     */
    static def variance(List<? extends Number> numbers) {
        double avg = numbers.average()
        numbers.stream().map {Math.pow((it - avg), 2)}.collect(Collectors.toList()).average()
    }

    /**
     * 离散系数
     * @param numbers
     * @return
     */
    static double coefficientOfVariation(List<? extends Number> numbers) {
        try {
            double avg = numbers.average()
            def variation = Math.sqrt(numbers.stream().map {Math.pow((it - avg), 2)}.collect(Collectors.toList()).average()) / avg
            SourceCode.formatDouble(variation) as double
        } catch (e) {
            log.warn("count coefficientOfVariation fail", numbers)
            Constant.TEST_ERROR_CODE
        }
    }

}
