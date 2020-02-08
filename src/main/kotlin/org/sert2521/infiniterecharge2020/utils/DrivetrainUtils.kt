package org.sert2521.infiniterecharge2020.utils

fun Double.deadband(range: Double) =
        if (this < range && this > -range) 0.0 else this

fun ClosedRange<Double>.intersects(other: ClosedRange<Double>): Boolean =
        start in other || endInclusive in other

fun Number.remap(fromRange: ClosedRange<Double>, toRange: ClosedRange<Double>) =
        (this.toDouble() - fromRange.start) * (toRange.endInclusive - toRange.start) / (fromRange.endInclusive - fromRange.start) + toRange.start
