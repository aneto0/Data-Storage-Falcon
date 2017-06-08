#!/bin/sh

echo "Stopping PON Sampler"
caput TEST-AUX-FDAQ:Ponsamp_Status_CMD 0

echo "Stopping Fast acquisition"
caput TEST-AUX-FDAQ:Fast_Status_CMD 0

