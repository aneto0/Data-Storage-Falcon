edit falcon_fast/new

add node .ADC0
add node .ADC1
add node .ADC2
add node .ADC3
add node .ADC4
add node .ADC5
add node .ADC6
add node .ADC7
add node .ADC8
add node .ADC9
add node .ADC10
add node .ADC11
add node .ADC12
add node .ADC13
add node .ADC14
add node .ADC15
add node .DEBUG

set def \FALCON_FAST::TOP.ADC0
add node .FAST
add node .SLOW
add node .FSTD
add node .SLWD

set def \FALCON_FAST::TOP.ADC1
add node .FAST
add node .SLOW
add node .FSTD
add node .SLWD

set def \FALCON_FAST::TOP.ADC2
add node .FAST
add node .SLOW
add node .FSTD
add node .SLWD

set def \FALCON_FAST::TOP.ADC3
add node .FAST
add node .SLOW
add node .FSTD
add node .SLWD

set def \FALCON_FAST::TOP.ADC4
add node .FAST
add node .SLOW
add node .FSTD
add node .SLWD

set def \FALCON_FAST::TOP.ADC5
add node .FAST
add node .SLOW
add node .FSTD
add node .SLWD

set def \FALCON_FAST::TOP.ADC6
add node .FAST
add node .SLOW
add node .FSTD
add node .SLWD

set def \FALCON_FAST::TOP.ADC7
add node .FAST
add node .SLOW
add node .FSTD
add node .SLWD

set def \FALCON_FAST::TOP.ADC8
add node .FAST
add node .SLOW
add node .FSTD
add node .SLWD

set def \FALCON_FAST::TOP.ADC9
add node .FAST
add node .SLOW
add node .FSTD
add node .SLWD

set def \FALCON_FAST::TOP.ADC10
add node .FAST
add node .SLOW
add node .FSTD
add node .SLWD

set def \FALCON_FAST::TOP.ADC11
add node .FAST
add node .SLOW
add node .FSTD
add node .SLWD

set def \FALCON_FAST::TOP.ADC12
add node .FAST
add node .SLOW
add node .FSTD
add node .SLWD

set def \FALCON_FAST::TOP.ADC13
add node .FAST
add node .SLOW
add node .FSTD
add node .SLWD

set def \FALCON_FAST::TOP.ADC14
add node .FAST
add node .SLOW
add node .FSTD
add node .SLWD

set def \FALCON_FAST::TOP.ADC15
add node .FAST
add node .SLOW
add node .FSTD
add node .SLWD

set def \FALCON_FAST::TOP.DEBUG
add node .TRIG
add node .TRIGD
add node .WAVE
add node .WAVED
add node .CYCLET
add node .CYCLETD

set def \FALCON_FAST::TOP
add node PORT0/usage=signal
add node PORT0D/usage=signal

add node COUNTER/usage=signal
add node COUNTERD/usage=signal

add node TIME/usage=signal
add node TIMED/usage=signal

write
close
exit
