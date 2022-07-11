#!/usr/bin/env bash

set -e

#TAG="quay.io/centos/centos:stream8"
TAG="fedora:36"

if command -v buildah; then
    CONTAINER_RUN=buildah
else
    echo "Unable to find a container runtime, install podman"
    exit 1
fi

mkdir -p keys

SSH_KEY="$(realpath ./keys)/ansible_ed25519"

if [[ ! -f $SSH_KEY ]]; then
  ssh-keygen -t ed25519 -f $SSH_KEY -C "Ansible lab key" -N ""
fi


BUILDER=$(buildah from $TAG)
buildah run $BUILDER dnf -y install basesystem bash systemd openssh-server passwd sudo python3 yum dnf findutils iproute NetworkManager iputils
buildah run $BUILDER useradd ansible -G wheel
buildah add $BUILDER $SSH_KEY /home/ansible/.ssh/authorized_keys
buildah run $BUILDER sh -c 'echo -e "ansible ALL=(ALL:ALL) NOPASSWD: ALL" >> /etc/sudoers.d/ansible'
buildah run $BUILDER useradd redhat -G wheel -p '$6$CoPu5pQehb0vQnWN$39y.7JoLr5A1no/jvjAsRGrBrYUufd43JVZiCPe8cHkz3M.ebOarMl/kc2QV3.sA8z7.4UvMzyJwCXcx7NYfz0'
buildah run $BUILDER sh -c "echo -e PermitRootLogin yes >> /etc/ssh/sshd_config"
buildah commit $BUILDER ansible-lab:base