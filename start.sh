#!/usr/bin/env bash

set -e

if [ ! -z "$1" ]; then
    GROUP="$1-"
fi

podman run -ti --rm --name ansible-$GROUP$(shuf -i 8-28671 -n1) --network podman --tmpfs /tmp --tmpfs /run ansible-lab:base /sbin/init