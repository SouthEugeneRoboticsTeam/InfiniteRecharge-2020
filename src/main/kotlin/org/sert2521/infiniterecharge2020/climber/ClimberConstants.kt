package org.sert2521.infiniterecharge2020.climber

const val CLIMBER_LIFT_SPEED = 0.9
const val WINCH_UNWIND_SPEED = -0.2
// ~2000 ticks higher than the actual bottom position so that we don't accidentally break anything
const val POSITION_AT_BOTTOM = 2000
// Approx. value read when the climber hits the top limit switch
const val POSITION_AT_TOP = 24000 // 26000 for BB-11
