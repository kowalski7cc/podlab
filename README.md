# podLAB

Build a simple lab using podman and systemd, ready for exercising with Ansible, inspired by [@mrjackv](https://github.com/mrjackv) ansible-nspawn

## Requirements

- systemd
- podman
- buildah
- python
- ssh-keygen, ssh
- ansible

## Usage

First run the `setup`

```shell
./setup
```

This script will generate an ssh-key pair, setup the podlab network and then download a `centos:stream9` image and perform the following steps on it:

- add podlab labels
- install userful packages (see [Included Pakcages](#included-packages)) 

then start pods with the `boot` script, and you can set optionally the ansible gruop to which they will belong.

```shell
./boot [group]
```

You can also use the option `-a` to attach directly to the new container.

Finally you can run your playbooks like this:

```shell
ansible-playbook ./playbooks/example.yml
```

After you're done excercising, you can stop the lab with the command

```shell
./shutdown
```

You can also use the `-c` option to delete the image and the network. They will be created again next time tou run the `setup` script 

## What's inside?

A quick look at the files and directories included in podlab

```
.
├── playbooks
├── ansible.cfg
├── boot
├── inventory
├── LICENSE
├── podssh
├── README.md
├── setenv
├── setup
├── shutdown
└── ssh_config
```

1. `playbooks`: A direcotry where you can place you playbooks. podLAB comes with a playbook named `example.yml`. You can run it with the command `ansible-playbook playbooks/example.yml`
2. `ansible.cfg`: The configuration to tell ansible how to connect to podLAB containers
3. `boot`: Runs a podLAB container (see [usage](#usage))
4. `inventory`: Inventory script for Ansible in Python, it will filter out any non-podLAB containers
5. `LICENSE`: podlab's MIT license
6. `podssh`: ssh script in podman's unshare net namespace environment, it allows to ssh to any container easily.
7. `README.md`: This file :)
8. `setenv`: podLABS configuration
9. `setup`: Configures the environment for podLAB's usage (see [usage](#usage))
10. `shutdown`: Stops all podLAB's containers, and optionally clears images and networks (see [usage](#usage))
11. `ssh_config`: The ssh configuration file that allows ansible and manual ssh connection to containers

## Included packages

```
- basesystem
- bash
- systemd
- openssh-server
- passwd
- sudo
- python3
- yum
- dnf
- findutils
- iproute
- NetworkManager
- iputils
- bind-utils
- procps-ng
- openssh-clients
- nc
- at
- hostname
- acl
- xz
- zip
- info
- wget
- bzip2
- file
- ed
- nano
- quota
- less
- which
- strace
- symlinks
- tcpdump
- time
- tree
- jq
- pinfo
- vim-enhanced
- words
- util-linux-user
```

## License

MIT License