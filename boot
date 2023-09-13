#!/usr/bin/env bash
set -e

ENVFILE="$(dirname $(realpath $0))/setenv"
if [[ -f $ENVFILE ]]; then
    . $ENVFILE
else
    echo "Environment file not found: $ENVFILE"
    exit 1
fi

usage() { echo "$0 usage: $0 [-a] [-p] [-h] [GROUP NAME]"; }
usage_long() {
    echo "  -a  attach to container"
    echo "  -p  persist container"
    echo "  -h  show this help"
}

#[ $# -eq 0 ] && usage && exit 0
DETACH='-d'
REMOVE='--rm'

while getopts ':hapv' OPTION; do
    case "$OPTION" in
        a)
            DETACH=''
        ;;
        
        p)
            REMOVE=''
        ;;
        
        v)
            VERBOSE=1
        ;;
        
        h)
            usage
            usage_long
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

if [ -z `podman images $IMAGE_NAME:$VERSION --format {{.ID}}` ]; then
    echo "Please run ./setup"
    exit 2
fi


if [ ! -z "$1" ]; then
    GROUP="$1"
else
    GROUP="ungrouped"
fi

# TODO check if image is present before run

LAST=$(podman ps -a --filter=label=$LABEL.group=$GROUP --format {{.Names}} | rev | cut --delimiter=- -f 1 | rev | sort -nr | head -n1)
podman run --privileged $DETACH $REMOVE -ti --label "$LABEL.version=$VERSION" --label "$LABEL.group=$GROUP" --rm --name $NAME-$GROUP-$((LAST + 1)) --hostname $NAME-$GROUP-$((LAST + 1)) --network $NETWORK --tmpfs /tmp --tmpfs /run $IMAGE_NAME:$VERSION /sbin/init