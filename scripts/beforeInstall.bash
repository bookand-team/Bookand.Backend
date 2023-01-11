#!/bin/bash

# I want to make sure that the directory is clean and has nothing left over from
# previous deployments. The servers auto scale so the directory may or may not
# exist.
if [ -d /home/ubuntu/bookand/ ]; then
    rm -rf /home/ubuntu/bookand/
fi
mkdir -vp /home/ubuntu/bookand/