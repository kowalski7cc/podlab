#!/usr/bin/env bash

set -e

if [ ! -z "$1" ]; then
    GROUP="$1"
else
    GROUP="ungrouped"
fi

NETWORK="ansible-lab"

POD=$(podman run --privileged -tid --label "org.ansible-lab.group=$GROUP" --rm --name ansible-${GROUP}-new --network $NETWORK --tmpfs /tmp --tmpfs /run ansible-lab:base /sbin/init)
podman rename $POD ansible-${GROUP}-${POD::12}
#podman attach $POD
echo $POD