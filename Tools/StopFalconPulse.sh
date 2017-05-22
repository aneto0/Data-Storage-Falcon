#!/bin/sh

echo "Stopping PON Sampler"
caput FALCON:PONSAMP:STATUS_CMD 0

echo "Stopping Fast acquisition"
caput FALCON:FAST:STATUS_CMD 0

