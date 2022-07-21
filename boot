#!/usr/bin/env bash
set -e

ENVFILE="$(dirname $(realpath $0))/setenv"
if [[ -f $ENVFILE ]]; then
    . $ENVFILE
fi

usage() { echo "$0 usage: $0 [-a] [-h] [GROUP]"; }

#[ $# -eq 0 ] && usage && exit 0
DETACH='-d'

while getopts ':hav' OPTION; do
    case "$OPTION" in
        a)
            DETACH=''
        ;;
        
        v)
            VERBOSE=1
        ;;
        
        h)
            usage
            exit 0
        ;;
        
        ?)
            usage
            exit 1
        ;;
    esac
done
shift $(expr $OPTIND - 1)


if ! [ -x "$(command -v podman)" ]; then
    echo "podman is not installed" >&2
    exit 1
fi



if [ ! -z "$1" ]; then
    GROUP="$1"
else
    GROUP="ungrouped"
fi

# TODO check if image is present before run

LAST=$(podman ps -a --filter=label=$LABEL.group=$GROUP --format {{.Names}} | rev | cut --delimiter=- -f 1 | rev | sort -nr | head -n1)
podman run --privileged $DETACH -ti --label "$LABEL.version=$VERSION" --label "$LABEL.group=$GROUP" --rm --name $NAME-$GROUP-$((LAST + 1)) --hostname $NAME-$GROUP-$((LAST + 1)) --network $NETWORK --tmpfs /tmp --tmpfs /run $IMAGE_NAME:$VERSION /sbin/init