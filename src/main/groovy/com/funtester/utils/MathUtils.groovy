package com.funtester.utils

import java.util.stream.Collectors

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
    static def coefficientOfVariation(List<? extends Number> numbers) {
        double avg = numbers.average()
        Math.sqrt(numbers.stream().map {Math.pow((it - avg), 2)}.collect(Collectors.toList()).average()) / avg
    }

}
